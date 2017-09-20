package lib.ys.ui.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import butterknife.BindView;
import lib.ys.R;
import lib.ys.R2;


abstract public class SimpleSplashActivityEx extends SplashActivityEx {

    @BindView(R2.id.splash_iv)
    ImageView mIv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash_ex;
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
