package yy.doctor.ui.activity;

import lib.ys.ui.activity.SimpleSplashActivityEx;
import yy.doctor.BuildConfig;
import yy.doctor.R;
import yy.doctor.model.Profile;
import yy.doctor.ui.activity.login.LoginActivity;

/**
 * 推广页面
 *
 * @author CaiXiang
 * @since 2017/4/5
 */
public class SplashActivity extends SimpleSplashActivityEx {

    @Override
    protected int getSplashImageResId() {
        return R.mipmap.splash_bg;
    }

    @Override
    protected long getPastDelay() {
        return 1000;
    }

    @Override
    protected void goPast() {
        //登录过了并且没有退出过的话直接跳转到首页
        if (BuildConfig.TEST) {
            startActivity(TestActivity.class);
        } else if (Profile.inst().isLogin()) {
            startActivity(MainActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
    }
}
