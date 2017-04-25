package lib.ys.network.image.provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import lib.ys.ConstantsEx;
import lib.ys.network.image.NetworkImageListener;
import lib.ys.network.image.renderer.BaseRenderer;
import lib.ys.network.image.renderer.CircleRenderer;
import lib.ys.network.image.renderer.CornerRenderer;

/**
 * @author yuansui
 */
public class FrescoProvider extends BaseProvider {

    private static final String KPrefixStorage = "file:";
    private static final String KPrefixRes = "res:///";

    private ControllerListener mCtrlListener;
    private PipelineDraweeControllerBuilder mBuilder;
    private SimpleDraweeView mSdv;

    @DrawableRes
    private Integer mPlaceHolderKeeper;

    private BaseRenderer mRendererKeeper;

    public FrescoProvider(Context context, ImageView iv) {
        super(context, iv);
        mBuilder = Fresco.newDraweeControllerBuilder();
        mSdv = (SimpleDraweeView) iv;
        mSdv.getHierarchy().setActualImageScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    public void load() {
        render(getPlaceHolder(), getRenderer());

        if (getFade() != ConstantsEx.KInvalidValue) {
            mSdv.getHierarchy().setFadeDuration(getFade());
        }

        DraweeController controller = null;
        if (getW() > 0 && getH() > 0) {
            controller = mBuilder
                    .setOldController(mSdv.getController())
                    .setImageRequest(newRequest())
                    .setControllerListener(mCtrlListener)
                    .build();
        } else {
            controller = mBuilder
                    .setOldController(mSdv.getController())
                    .setControllerListener(mCtrlListener)
                    .setUri(getUri())
                    .build();
        }
        mSdv.setController(controller);
    }

    @Override
    public void listener(final NetworkImageListener listener) {
        super.listener(listener);

        if (listener == null) {
            return;
        }

        if (mCtrlListener == null) {
            mCtrlListener = new BaseControllerListener<ImageInfo>() {

                @Override
                public void onFinalImageSet(String id, final ImageInfo imageInfo, Animatable animatable) {
                    if (mSdv != null && getListener() != null && imageInfo != null) {
                        lib.ys.network.image.ImageInfo info = new lib.ys.network.image.ImageInfo() {
                            @Override
                            public int getWidth() {
                                return imageInfo.getWidth();
                            }

                            @Override
                            public int getHeight() {
                                return imageInfo.getHeight();
                            }
                        };
                        listener.onImageSet(info, null);
                    }
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    if (mSdv != null && getListener() != null) {
                        listener.onFailure();
                    }
                }
            };
        }
    }

    /**
     * 从缓存中清除指定的url
     *
     * @param url
     */
    @Override
    public void clearFromCache(String url) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(url);
        imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);

        imagePipeline.evictFromCache(uri);
    }

    private void render(@DrawableRes int placeHolderRid, @Nullable BaseRenderer renderer) {
        if (placeHolderRid > 0) {
            if (mPlaceHolderKeeper == null || mPlaceHolderKeeper != placeHolderRid) {
                /**
                 * 注意一定要加这个判断(鄙视一下开发文档), 不然某些view自动放一会就会产生OOM, 比如auto viewpager
                 * 因为这个方法是每次set一个新的, 会重新new一个出来, 然后旧的那个并没有释放
                 */
                mSdv.getHierarchy().setPlaceholderImage(placeHolderRid);
                mPlaceHolderKeeper = placeHolderRid;
            }
        }

        if (renderer == null) {
            return;
        }

        if (mRendererKeeper == null || !mRendererKeeper.equals(renderer)) {
            if (renderer instanceof CircleRenderer) {
                RoundingParams rp = mSdv.getHierarchy().getRoundingParams();
                if (rp == null) {
                    rp = RoundingParams.asCircle();
                } else {
                    rp.setRoundAsCircle(true);
                }

                CircleRenderer r = (CircleRenderer) renderer;
                rp.setBorder(r.getBorderColor(), r.getBorderWidth());

                mSdv.getHierarchy().setRoundingParams(rp);
            } else if (renderer instanceof CornerRenderer) {
                CornerRenderer r = (CornerRenderer) renderer;
                RoundingParams rp = mSdv.getHierarchy().getRoundingParams();
                if (rp == null) {
                    rp = RoundingParams.fromCornersRadius(r.getRadius());
                } else {
                    rp.setCornersRadius(r.getRadius());
                }
                mSdv.getHierarchy().setRoundingParams(rp);
            }

            mRendererKeeper = renderer;
        }
    }

    /**
     * Fresco 不支持 相对路径的URI. 所有的 URI 都必须是绝对路径，并且带上该 URI 的 scheme
     * <p>
     * 远程图片	http:, https:
     * 本地文件	file:
     * asset目录下的资源	asset://
     * res目录下的资源	res://包名(任意包名或者不填)/ + R.drawable.xxx(或者是{@link lib.ys.util.res.ResLoader#getIdentifier(String, String)})
     * Uri中指定图片数据	data:mime/type;base64,	数据类型必须符合 rfc2397规定 (仅支持 UTF-8)
     * </p>
     *
     * @return 经过处理的uri
     */
    private Uri getUri() {
        Uri uri;
        if (getHttpUrl() != null) {
            uri = Uri.parse(getHttpUrl());
        } else if (getStorageUrl() != null) {
            uri = Uri.parse(KPrefixStorage + getStorageUrl());
        } else if (getResId() != 0) {
            uri = Uri.parse(KPrefixRes + getResId());
        } else if (getIdUrl() != null) {
            uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getIdUrl());
        } else {
            uri = Uri.EMPTY;
        }

        return uri;
    }

    @Override
    public void fetch() {
        ImageRequest r = ImageRequest.fromUri(getUri());
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(r, mSdv.getContext());
        dataSource.subscribe(new BaseBitmapDataSubscriber() {

            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                if (getListener() != null) {
                    getListener().onImageSet(null, bitmap.copy(bitmap.getConfig(), true));
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

            }
        }, CallerThreadExecutor.getInstance());
    }

    private ImageRequest newRequest() {
        return ImageRequestBuilder.newBuilderWithSource(getUri())
                .setResizeOptions(new ResizeOptions(getW(), getH()))
                .setRotationOptions(RotationOptions.autoRotate())
                .build();
    }
}
