package lib.ys.view.swipeRefresh.footer;

import android.content.Context;
import android.view.View;

import lib.ys.R;
import lib.ys.view.ProgressView;

/**
 * @author yuansui
 */
public class DefaultFooter extends BaseFooter {

    private View mLayoutLoading;
    private View mLayoutReloadMore;
    private ProgressView mProgressView;

    public DefaultFooter(Context context) {
        super(context);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_sr_scrollable_footer;
    }

    @Override
    protected void findViews() {
        mLayoutLoading = findViewById(R.id.sr_footer_layout_loading);
        mLayoutReloadMore = findViewById(R.id.sr_scrollable_footer_tv_reload_more);
        mProgressView = findViewById(R.id.sr_footer_progress_view);
    }

    @Override
    protected void setViews() {
        setOnRetryClickView(mLayoutReloadMore);
    }

    @Override
    public void onNormal() {
        showView(mLayoutLoading);
        hideView(mLayoutReloadMore);
        mProgressView.stop();
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onLoading() {
        showView(mLayoutLoading);
        hideView(mLayoutReloadMore);
        mProgressView.start();
    }

    @Override
    public void onFailed() {
        hideView(mLayoutLoading);
        showView(mLayoutReloadMore);
        mProgressView.stop();
    }

    @Override
    public void onFinish() {
        mProgressView.stop();
        hide();
    }
}
