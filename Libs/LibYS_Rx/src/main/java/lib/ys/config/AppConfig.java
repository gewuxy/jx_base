package lib.ys.config;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import inject.annotation.builder.Builder;
import lib.ys.ui.decor.ErrorDecorEx;

/**
 * App整体配置
 *
 * @author yuansui
 */
@Builder
public class AppConfig {

    /**
     * 页面刷新的方式
     */
    @IntDef({
            RefreshWay.un_know,
            RefreshWay.dialog,
            RefreshWay.embed,
            RefreshWay.swipe,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshWay {
        int un_know = -1;
        int dialog = 0;
        int embed = 1;
        int swipe = 2;
    }

    @DrawableRes
    int mBgRes = 0;

    @ColorRes
    int mBgColorRes = 0;

    Class<? extends ErrorDecorEx> mErrorDecorClz = null;

    /**
     * 是否使用滑动退出activity, 使用的时候需要注意事项:
     * 1. 底层的main activity一定要使用传统不透明的theme
     * 2. application theme使用{@link lib.ys.R.style#AppTheme_SwipeBack}
     */
    boolean mEnableSwipeFinish = false;

    /**
     * 初始化的加载样式
     */
    @RefreshWay
    int mInitRefreshWay = RefreshWay.dialog;

    /**
     * 设置沉浸式状态栏是否可用
     *
     * @param enable
     */
    boolean mEnableFlatBar = false;

    public Class<? extends ErrorDecorEx> getErrorDecorClz() {
        return mErrorDecorClz;
    }

    @DrawableRes
    public int getBgRes() {
        return mBgRes;
    }

    @ColorRes
    public int getBgColorRes() {
        return mBgColorRes;
    }

    @TargetApi(VERSION_CODES.KITKAT)
    public boolean isFlatBarEnabled() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            return mEnableFlatBar;
        } else {
            return false;
        }
    }

    @RefreshWay
    public int getInitRefreshWay() {
        return mInitRefreshWay;
    }

    public boolean isSwipeFinishEnabled() {
        return mEnableSwipeFinish;
    }

    public static AppConfigBuilder newBuilder() {
        return AppConfigBuilder.create();
    }
}
