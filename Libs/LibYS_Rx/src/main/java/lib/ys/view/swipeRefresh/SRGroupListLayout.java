package lib.ys.view.swipeRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import lib.ys.R;
import lib.ys.view.GroupListView;
import lib.ys.view.swipeRefresh.base.BaseSRLoadMoreLayout;

/**
 * SwipeRefreshExpendableListView
 *
 * @author yuansui
 */
public class SRGroupListLayout extends BaseSRLoadMoreLayout<GroupListView> {

    public SRGroupListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected GroupListView initContentView(Context context, AttributeSet attrs) {
        GroupListView lv = new GroupListView(context, attrs);
        lv.setId(R.id.scrollable_view);
        return lv;
    }

    @Override
    protected void addLoadMoreFooterView(View footerView) {
        getContentView().addFooterView(footerView);
    }

    @Override
    public void addHeaderView(View v) {
        getContentView().addHeaderView(v);
    }

    @Override
    public void removeHeaderView(View v) {
        getContentView().removeHeaderView(v);
    }
}
