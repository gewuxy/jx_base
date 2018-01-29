package lib.ys.network.image;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultBitmapMemoryCacheParamsSupplier;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import lib.ys.R;
import lib.ys.network.image.interceptor.Interceptor;
import lib.ys.network.image.provider.BaseProvider;
import lib.ys.network.image.provider.FrescoProvider;
import lib.ys.network.image.shape.Renderer;
import lib.ys.util.res.ResLoader;
import lib.ys.util.view.ViewUtil;

public class NetworkImageView extends SimpleDraweeView implements Functions {

    private BaseProvider mProvider;

    private Drawable mForeground;

    public NetworkImageView(Context context) {
        this(context, null, 0);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            setBackgroundColor(Color.BLUE);
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NetworkImageView);
        Drawable d = a.getDrawable(R.styleable.NetworkImageView_niv_foreground);
        setForeground(d);
        a.recycle();

        init(context);
    }

    private void init(Context context) {
        mProvider = new FrescoProvider(this);
    }

    @Override
    public NetworkImageView url(String url) {
        return mProvider.url(url);
    }

    @Override
    public NetworkImageView storage(String s) {
        return mProvider.storage(s);
    }

    @Override
    public NetworkImageView res(@DrawableRes int id) {
        return mProvider.res(id);
    }

    @Override
    public NetworkImageView id(String id) {
        return mProvider.id(id);
    }

    @Override
    public NetworkImageView contentProvider(String path) {
        return mProvider.contentProvider(path);
    }

    @Override
    public NetworkImageView uri(Uri u) {
        return mProvider.uri(u);
    }

    @Override
    public NetworkImageView renderer(Renderer renderer) {
        return mProvider.renderer(renderer);
    }

    @Override
    public NetworkImageView addInterceptor(Interceptor i) {
        return mProvider.addInterceptor(i);
    }

    @Override
    public NetworkImageView removeInterceptor(Interceptor i) {
        return mProvider.removeInterceptor(i);
    }

    @Override
    public NetworkImageView holder(boolean flag) {
        return mProvider.holder(flag);
    }

    @Override
    public NetworkImageView listener(NetworkImageListener listener) {
        return mProvider.listener(listener);
    }

    @Override
    public NetworkImageView placeHolder(@DrawableRes int id) {
        return mProvider.placeHolder(id);
    }

    @Override
    public NetworkImageView fade(int duration) {
        return mProvider.fade(duration);
    }

    @Override
    public NetworkImageView resize(@IntRange(from = 1, to = Integer.MAX_VALUE) int w, @IntRange(from = 1, to = Integer.MAX_VALUE) int h) {
        return mProvider.resize(w, h);
    }

    @Override
    public NetworkImageView scaleType(ScaleType type) {
        return mProvider.scaleType(type);
    }

    public void load() {
        mProvider.load();
    }

    /**
     * 初始化, 直接调用对应provider的初始化方法
     *
     * @param context
     * @param cacheDir           文件保存路径
     * @param maxMemoryCacheSize 最大内存使用数, 单位: byte
     */
    public static void init(Context context, String cacheDir, final int maxMemoryCacheSize) {
        if (cacheDir == null) {
            return;
        }

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(new File(cacheDir))
                .build();

        Supplier<MemoryCacheParams> supplier = new DefaultBitmapMemoryCacheParamsSupplier((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)) {

            @Override
            public MemoryCacheParams get() {
                return new MemoryCacheParams(
                        maxMemoryCacheSize,
                        256,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE);
            }
        };

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapMemoryCacheParamsSupplier(supplier)
                .setDownsampleEnabled(true)
//                .setCacheKeyFactory(cacheDir)
//                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//                .setExecutorSupplier(executorSupplier)
//                .setImageCacheStatsTracker(imageCacheStatsTracker)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .setNetworkFetchProducer(networkFetchProducer)
//                .setPoolFactory(poolFactory)
//                .setProgressiveJpegConfig(progressiveJpegConfig)
//                .setRequestListeners(requestListeners)
//                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .build();
        Fresco.initialize(context, config);
    }

    /**
     * 清理内存中cache
     *
     * @param context
     */
    public static void clearMemoryCache(Context context) {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    public void clearFromCache(String url) {
        mProvider.clearFromCache(url);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mForeground != null) {
            mForeground.setBounds(new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight()));
        }
    }

    @Override
    public void setForeground(Drawable d) {
        if (ViewUtil.setForeground(this, mForeground, d)) {
            mForeground = d;
        }
    }

    public void setForeground(@DrawableRes int id) {
        setForeground(ResLoader.getDrawable(id));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (mForeground != null) {
            mForeground.draw(canvas);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mForeground != null && mForeground.isStateful()) {
            mForeground.setState(getDrawableState());
        }
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();

        if (mForeground != null) {
            mForeground.jumpToCurrentState();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || (who == mForeground);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (mForeground != null) {
                    mForeground.setHotspot(e.getX(), e.getY());
                }
            }
        }
        return super.onTouchEvent(e);
    }
}
