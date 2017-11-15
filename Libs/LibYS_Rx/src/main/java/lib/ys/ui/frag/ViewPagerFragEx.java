package lib.ys.ui.frag;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import lib.ys.R;
import lib.ys.impl.FragPagerAdapterImpl;
import lib.ys.util.view.LayoutUtil;
import lib.ys.util.view.ViewUtil;
import lib.ys.view.pager.ViewPagerEx;
import lib.ys.view.pager.indicator.PageIndicator;
import lib.ys.view.pager.transformer.BaseTransformer;


abstract public class ViewPagerFragEx extends FragEx {
    private LinearLayout mLayoutHeader;
    private ViewPagerEx mVp;
    private FragPagerAdapterImpl mAdapter;
    private PageIndicator mIndicator;

    private boolean mFakeDrag;

    @Override
    public int getContentViewId() {
        return R.layout.layout_viewpager;
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

    @Override
    public void setViews() {
        mVp.setAdapter(getAdapter());

        mIndicator = initPageIndicator();
        if (mIndicator != null) {
            mIndicator.setViewPager(mVp);
        }
    }

    /**
     * 如果有自定义的viewpagerId, 重写此方法
     *
     * @return
     */
    protected int getViewPagerResId() {
        return R.id.vp;
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

    protected void setCurrentItem(int item) {
        if (mVp == null) {
            return;
        }
        mVp.setCurrentItem(item);
    }

    protected void setCurrentItem(int item, boolean smoothScroll) {
        mVp.setCurrentItem(item, smoothScroll);
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

    protected final FragPagerAdapterImpl getAdapter() {
        if (mAdapter == null) {
            mAdapter = createPagerAdapter();
        }
        return mAdapter;
    }

    @NonNull
    protected FragPagerAdapterImpl createPagerAdapter() {
        return new FragPagerAdapterImpl(getChildFragmentManager());
    }

    protected int getCount() {
        return getAdapter().getCount();
    }

    protected List<Fragment> getData() {
        return getAdapter().getData();
    }

    protected boolean isEmpty() {
        return getAdapter().isEmpty();
    }

    protected int getCurrentItem() {
        if (mVp == null) {
            return 0;
        }
        return mVp.getCurrentItem();
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
    protected void setPageTransformer(boolean reverseDrawingOrder, @NonNull BaseTransformer transformer) {
        if (transformer == null) {
            return;
        }
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

    protected void removeAll() {
        getAdapter().removeAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mVp != null) {
            mVp.clearOnPageChangeListeners();
            mAdapter.removeAll();
        }
    }
}
