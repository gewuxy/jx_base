package lib.ys.ui.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import lib.ys.R;

abstract public class SimpleSplashActivityEx extends SplashActivityEx {

    private ImageView mIv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash_ex;
    }

    @CallSuper
    @Override
    public void findViews() {
        mIv = findView(R.id.splash_iv);
    }

    @CallSuper
    @Override
    public void setViews() {
        super.setViews();

        mIv.setImageResource(getSplashImageResId());
    }

    @DrawableRes
    abstract protected int getSplashImageResId();
}
