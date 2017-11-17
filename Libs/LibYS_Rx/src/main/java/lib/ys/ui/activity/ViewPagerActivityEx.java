package lib.ys.ui.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import lib.ys.R;
import lib.ys.adapter.FragPagerAdapterEx;
import lib.ys.impl.FragPagerAdapterImpl;
import lib.ys.util.view.LayoutUtil;
import lib.ys.util.view.ViewUtil;
import lib.ys.view.pager.ViewPagerEx;
import lib.ys.view.pager.indicator.PageIndicator;
import lib.ys.view.pager.transformer.BaseTransformer;


/**
 * 自动管理viewpager和indicator
 */
abstract public class ViewPagerActivityEx extends ActivityEx {

    private LinearLayout mLayoutHeader;
    private ViewPagerEx mVp;
    private FragPagerAdapterEx mAdapter;
    private PageIndicator mIndicator;

    private boolean mFakeDrag;

    @Override
    public int getContentViewId() {
        return R.layout.layout_viewpager;
    }

    /**
     * 如果有自定义的viewpagerId, 重写此方法
     *
     * @return
     */
    protected int getViewPagerResId() {
        return R.id.vp;
    }

    @Override
    public void findViews() {
        mVp = findView(getViewPagerResId());
        mLayoutHeader = findView(R.id.vp_header);

        if (mLayoutHeader != null) {
            View header = createHeaderView();
            if (header != null) {
                if (header.getLayoutParams() == null) {
                    mLayoutHeader.addView(header, LayoutUtil.getLinearParams(MATCH_PARENT, WRAP_CONTENT));
                } else {
                    mLayoutHeader.addView(header);
                }
            }
        }
    }

    @CallSuper
    @Override
    public void setViews() {
        mVp.setAdapter(getAdapter());

        mIndicator = initPageIndicator();
        if (mIndicator != null) {
            mIndicator.setViewPager(mVp);
        }
    }

    public View createHeaderView() {
        return null;
    }

    /**
     * 初始化PageIndicator
     *
     * @return
     */
    protected PageIndicator initPageIndicator() {
        return null;
    }

    public void setCurrPosition(int pos) {
        if (mVp == null) {
            return;
        }
        mVp.setCurrentItem(pos);
    }

    public void setCurrPosition(int pos, boolean smoothScroll) {
        if (mVp == null) {
            return;
        }
        mVp.setCurrentItem(pos, smoothScroll);
    }

    protected void setOffscreenPageLimit(int limit) {
        mVp.setOffscreenPageLimit(limit);
    }

    /**
     * 一定要通过此方法设置, 不然indicator会无效
     *
     * @param listener
     */
    protected void setOnPageChangeListener(OnPageChangeListener listener) {
        if (mIndicator != null) {
            mIndicator.setOnPageChangeListener(listener);
        } else {
            mVp.addOnPageChangeListener(listener);
        }
    }

    protected void add(Fragment frag) {
        getAdapter().add(frag);
    }

    protected final FragPagerAdapterEx getAdapter() {
        if (mAdapter == null) {
            mAdapter = createPagerAdapter();
        }
        return mAdapter;
    }

    @NonNull
    protected FragPagerAdapterEx createPagerAdapter() {
        return new FragPagerAdapterImpl(getSupportFragmentManager());
    }

    protected int getCount() {
        return getAdapter().getCount();
    }

    protected List<Fragment> getData() {
        return getAdapter().getData();
    }

    protected void removeAll() {
        getAdapter().removeAll();
    }

    protected boolean isEmpty() {
        return getAdapter().isEmpty();
    }

    protected int getCurrPosition() {
        if (mVp == null) {
            return 0;
        }
        return mVp.getCurrentItem();
    }

    protected <T extends Fragment> T getCurrItem() {
        return (T) getItem(getCurrPosition());
    }

    protected Fragment getItem(int position) {
        return getAdapter().getItem(position);
    }

    protected void showHeaderView() {
        showView(mLayoutHeader);
    }

    protected void goneHeaderView() {
        goneView(mLayoutHeader);
    }

    /**
     * 设置是否可滑动
     *
     * @param scrollable
     */
    protected void setScrollable(boolean scrollable) {
        mVp.setScrollable(scrollable);
    }

    /**
     * 设置滑动速率
     *
     * @param duration
     */
    protected void setScrollDuration(int duration) {
        ViewUtil.setViewPagerScrollDuration(mVp, duration);
    }

    /**
     * 设置切换动画
     *
     * @param reverseDrawingOrder
     * @param transformer
     */
    protected void setPageTransformer(boolean reverseDrawingOrder, BaseTransformer transformer) {
        mVp.setPageTransformer(reverseDrawingOrder, transformer);
        mFakeDrag = true;
    }

    protected ViewPagerEx getViewPager() {
        return mVp;
    }

    protected void invalidate() {
        getAdapter().notifyDataSetChanged();
        if (mFakeDrag && !isEmpty()) {
            /**
             * 必须模拟一次fake的拖动, 才能在初始化的时候调起transformer的效果
             */
            mVp.beginFakeDrag();
            mVp.fakeDragBy(-1);
            mVp.endFakeDrag();

            mFakeDrag = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mVp != null) {
            mVp.clearOnPageChangeListeners();
            mAdapter.removeAll();
        }
    }
}
