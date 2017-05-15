package lib.ys;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import lib.network.Network;
import lib.ys.config.AppConfig;
import lib.ys.crash.CrashMgr;
import lib.ys.network.image.NetworkImageView;
import lib.ys.util.DeviceUtil;
import lib.ys.util.ProcessUtil;
import lib.ys.util.TextUtil;

/**
 * @author yuansui
 */
abstract public class AppEx extends Application {

    protected String TAG = getClass().getSimpleName();

    protected static Context mContext;

    private static AppConfig mConfig;


    @Override
    public void onCreate() {
        super.onCreate();

        if (!ProcessUtil.isMainProcess(this)) {
            // 只在主进程里初始化一次
            initInChildProcess();
            return;
        }

        mContext = getApplicationContext();

        mToast = initToast();

        NetworkImageView.init(this, getNetworkImageCacheDir(), (int) (DeviceUtil.getRuntimeMaxMemory() / 8));

        Network.init(this);

        mConfig = makeConfig();

        if (enableCatchCrash()) {
            CrashMgr.inst().init(e -> {
                LogMgr.e(TAG, "handleCrashException", e);
                handleCrash(e);
                return true;
            });
        }

        init();
    }

    abstract protected AppConfig makeConfig();

    public static AppConfig getConfig() {
        return mConfig;
    }

    /**
     * 设置主进程配置
     */
    abstract protected void init();

    /**
     * 设置子进程参数
     */
    protected void initInChildProcess() {
    }

    /**
     * 返回getApplicationContext()
     *
     * @return mContext
     */
    public static Context ct() {
        return mContext;
    }

    /**
     * 返回getApplicationContext()
     *
     * @return mContext
     */
    public static Context getContext() {
        return mContext;
    }

    public static ContentResolver getExContentResolver() {
        return mContext.getContentResolver();
    }

    private static Toast mToast = null;

    protected Toast initToast() {
        return Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
    }

    public static void showToast(String content) {
        if (TextUtil.isEmpty(content)) {
            return;
        }
        mToast.setText(content);
        mToast.show();
    }

    public static void showToast(@StringRes int... ids) {
        if (ids == null) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.length; ++i) {
            builder.append(mContext.getString(ids[i]));
        }
        mToast.setText(builder.toString());
        mToast.show();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        NetworkImageView.clearMemoryCache(mContext);
        System.gc();
    }

    abstract protected String getNetworkImageCacheDir();

    /**
     * 是否允许捕捉异常
     *
     * @return
     */
    protected boolean enableCatchCrash() {
        return false;
    }

    protected void handleCrash(Throwable e) {
    }

    public void doDestroy() {
    }
}
