package lib.ys.ui.activity;

import android.support.annotation.CallSuper;
import android.widget.ImageView;

import lib.ys.R;
import lib.ys.util.view.ViewUtil;


abstract public class SimpleSplashActivityEx extends SplashActivityEx {

    private ImageView mIv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash_ex;
    }

    @CallSuper
    @Override
    public void findViews() {
        super.findViews();

        mIv = findView(R.id.splash_iv);
    }

    @CallSuper
    @Override
    public void setViews() {
        super.setViews();

        mIv.setImageResource(getSplashImageResId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ViewUtil.recycleIvBmp(mIv);
    }

    abstract protected int getSplashImageResId();
}
