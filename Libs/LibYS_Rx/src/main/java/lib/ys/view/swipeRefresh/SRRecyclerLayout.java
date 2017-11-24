package lib.ys.view.swipeRefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import lib.ys.R;
import lib.ys.view.recycler.WrapRecyclerView;
import lib.ys.view.swipeRefresh.base.BaseSRLoadMoreLayout;
import lib.ys.view.swipeRefresh.footer.EmptyFooter;

/**
 * @author yuansui
 */
public class SRRecyclerLayout extends BaseSRLoadMoreLayout<WrapRecyclerView> {

    public SRRecyclerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected WrapRecyclerView initContentView(Context context, AttributeSet attrs) {
        WrapRecyclerView rv = new WrapRecyclerView(context, attrs);
        rv.setId(R.id.scrollable_view);
        return rv;
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
        // FIXME: 没有实现
    }

    @Override
    protected boolean canChildScrollUp() {
        LayoutManager manager = getContentView().getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL) {
                LinearLayoutManager lm = (LinearLayoutManager) manager;
                int position = lm.findFirstVisibleItemPosition();
                View firstVisibleItemView = lm.findViewByPosition(position);
                if (firstVisibleItemView instanceof EmptyFooter) {
                    /**
                     * 无数据只有一个load more footer和empty footer的时候, position == 1
                     * 这时候如果不 -1 操作的话, 计算出来的dis恒大于0, 就无法下拉了
                     */
                    position -= 1;
                    if (position < 0) {
                        position = 0;
                    }
                }
                int itemHeight = firstVisibleItemView.getHeight();
                int dis = position * itemHeight - firstVisibleItemView.getTop();
                return dis > 0;
            } else {
                // 不支持横向拉伸
                return true;
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) manager).getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                // FIXME: 计算精确的offset, 可能需要监听滑动...??暂缺
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
