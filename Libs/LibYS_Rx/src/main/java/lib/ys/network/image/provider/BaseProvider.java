package lib.ys.network.image.provider;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import lib.ys.ConstantsEx;
import lib.ys.network.image.NetworkImageListener;
import lib.ys.network.image.interceptor.Interceptor;
import lib.ys.network.image.renderer.Renderer;

/**
 * 图片内容提供者
 *
 * @author yuansui
 */
abstract public class BaseProvider {

    private Context mContext;
    private ImageView mIv;

    /**
     * 以下是详细属性
     */
    private String mHttpUrl;
    private String mStorageUrl;
    @DrawableRes
    private int mResId;
    private String mIdUrl;
    private String mContentProviderPath;
    private Uri mUri;

    private int mW;
    private int mH;
    @DrawableRes
    private int mPlaceHolder = ConstantsEx.KInvalidValue;
    private int mFade = ConstantsEx.KInvalidValue;
    private Renderer mRenderer;

    private List<Interceptor> mInterceptors;

    private NetworkImageListener mListener;


    public BaseProvider(Context context, ImageView iv) {
        mContext = context;
        mIv = iv;
        mInterceptors = new ArrayList<>();
    }

    public void url(String url) {
        mHttpUrl = url;

        mIdUrl = null;
        mResId = 0;
        mUri = null;
        mContentProviderPath = null;
        mStorageUrl = null;
    }

    public void storage(String s) {
        mStorageUrl = s;

        mHttpUrl = null;
        mIdUrl = null;
        mResId = 0;
        mUri = null;
        mContentProviderPath = null;
    }

    public void res(@DrawableRes int id) {
        mResId = id;

        mHttpUrl = null;
        mIdUrl = null;
        mUri = null;
        mContentProviderPath = null;
        mStorageUrl = null;
    }

    public void id(String id) {
        mIdUrl = id;

        mHttpUrl = null;
        mResId = 0;
        mUri = null;
        mContentProviderPath = null;
        mStorageUrl = null;
    }

    public void contentProvider(String path) {
        mContentProviderPath = path;

        mHttpUrl = null;
        mIdUrl = null;
        mResId = 0;
        mUri = null;
        mStorageUrl = null;
    }

    public void uri(Uri uri) {
        mUri = uri;

        mHttpUrl = null;
        mIdUrl = null;
        mResId = 0;
        mContentProviderPath = null;
        mStorageUrl = null;
    }

    public void renderer(Renderer renderer) {
        mRenderer = renderer;
    }

    public void addInterceptor(Interceptor i) {
        mInterceptors.add(i);
    }

    public void removeInterceptor(Interceptor i) {
        mInterceptors.remove(i);
    }

    public void listener(NetworkImageListener listener) {
        mListener = listener;
    }

    public void placeHolderRid(@DrawableRes int id) {
        mPlaceHolder = id;
    }

    public void fade(int duration) {
        mFade = duration;
    }

    public void resize(@IntRange(from = 1, to = Integer.MAX_VALUE) int w, @IntRange(from = 1, to = Integer.MAX_VALUE) int h) {
        mW = w;
        mH = h;
    }

    protected String getHttpUrl() {
        return mHttpUrl;
    }

    protected String getStorageUrl() {
        return mStorageUrl;
    }

    @DrawableRes
    protected int getResId() {
        return mResId;
    }

    protected String getIdUrl() {
        return mIdUrl;
    }

    protected String getContentProviderPath() {
        return mContentProviderPath;
    }

    protected Uri getUri() {
        return mUri;
    }

    protected int getW() {
        return mW;
    }

    protected int getH() {
        return mH;
    }

    protected int getPlaceHolder() {
        return mPlaceHolder;
    }

    protected int getFade() {
        return mFade;
    }

    protected Renderer getRenderer() {
        return mRenderer;
    }

    protected List<Interceptor> getInterceptors() {
        return mInterceptors;
    }

    protected NetworkImageListener getListener() {
        return mListener;
    }

    abstract public void clearFromCache(String url);

    abstract public void load();
}
