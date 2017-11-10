package lib.ys.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lib.ys.ConstantsEx;
import lib.ys.YSLog;
import lib.ys.adapter.interfaces.IPagerTitle;

/**
 * 功能:
 * 1. tab标题
 * <p>2. 动态删除添加数据:
 * <p>利用modify变量判断需要删除的时候, 改变{@link #getItemPosition(Object)}为{@link #POSITION_NONE}</p>
 * <p>3. 循环展示
 * <p>
 * 正常来说如果先调用detach({@link #destroyItem(ViewGroup, int, Object)})之后才调用attach({@link #instantiateItem(ViewGroup, int)}),
 * 完全可以使用super方法来完成, 但是实际上左右滑行为是不一样的, 在往右滑的时候(切换到前一个fragment), 是
 * 先调用attach之后才调用detach, 导致fragment最后其实是分离不可见的
 * 所以使用自己的{@link FragmentTransaction}来保证依附是在分离之后被调用, 详情见{@link #finishUpdate(ViewGroup)}
 * </p>
 *
 * @param <TITLE>
 */
abstract public class FragPagerAdapterEx<TITLE extends IPagerTitle> extends FragmentPagerAdapter {

    private String TAG = getClass().getSimpleName();

    private List<Fragment> mFrags;
    private List<TITLE> mTitles;

    private boolean mModify;

    private FragmentManager mFM;
    private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem = null;


    public FragPagerAdapterEx(FragmentManager fm) {
        super(fm);
        mFM = fm;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFrags == null) {
            return null;
        }

        Fragment t = null;
        try {
            if (isLoop()) {
                position %= getRealCount();
            }
            t = mFrags.get(position);
        } catch (Exception e) {
            YSLog.e(TAG, e);
        }

        return t;
    }

    @Override
    public int getCount() {
        int count = mFrags == null ? 0 : mFrags.size();
        if (isLoop()) {
            if (count < 3) {
                // 不够3个. 无法建立frag循环
                throw new IllegalStateException("need at least 3 count");
            }
            return Integer.MAX_VALUE;
        } else {
            return count;
        }
    }

    public int getRealCount() {
        return mFrags == null ? 0 : mFrags.size();
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public void setData(List<Fragment> data) {
        mFrags = data;
    }

    public void add(Fragment item) {
        if (item == null) {
            return;
        }

        if (mFrags == null) {
            mFrags = new ArrayList<>();
        }
        mFrags.add(item);
    }

    public void add(int position, Fragment item) {
        if (item == null) {
            return;
        }

        if (mFrags == null) {
            mFrags = new ArrayList<>();
        }
        mFrags.add(position, item);
    }

    public void addAll(List<Fragment> data) {
        if (data == null || data.isEmpty()) {
            return;
        }

        if (mFrags == null) {
            mFrags = data;
        } else {
            mFrags.addAll(data);
        }
    }

    public void addAll(int position, List<Fragment> item) {
        if (mFrags != null && item != null) {
            mFrags.addAll(position, item);
        }
    }

    public List<Fragment> getData() {
        return mFrags;
    }

    @Override
    public int getItemPosition(Object object) {
        if (mModify) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

    public void setTitles(List<TITLE> titles) {
        mTitles = titles;
    }

    public List<TITLE> getTitles() {
        return mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (mTitles == null || mTitles.isEmpty()) ? ConstantsEx.KEmpty : mTitles.get(position).getTitle();
    }

    public void removeAll() {
        if (mFrags != null) {
            mFrags.clear();
            mModify = true;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mModify) {
            if (mCurTransaction == null) {
                mCurTransaction = mFM.beginTransaction();
            }

            Fragment frag = getItem(position);
            int id = container.getId();
            mCurTransaction.add(id, frag, makeFragmentName(id, getItemId(position)));

            if (frag != mCurrentPrimaryItem) {
                frag.setMenuVisibility(false);
                frag.setUserVisibleHint(false);
            }

            return frag;
        } else {
            if (isLoop()) {
                if (mCurTransaction == null) {
                    mCurTransaction = mFM.beginTransaction();
                }

                String name = makeFragmentName(container.getId(), getItemId(position));
                Fragment frag = mFM.findFragmentByTag(name);
                if (frag != null) {
                    mCurTransaction.attach(frag);
                } else {
                    frag = getItem(position);
                    mCurTransaction.add(container.getId(), frag, name);
                }

                if (frag != mCurrentPrimaryItem) {
                    frag.setMenuVisibility(false);
                    frag.setUserVisibleHint(false);
                }

                return frag;
            } else {
                return super.instantiateItem(container, position);
            }
        }
    }

    @Override
    public final long getItemId(int position) {
        if (isLoop()) {
            position %= getRealCount();
        }

        return position;
    }

    /**
     * 保持这个提交顺序. 不能改变
     *
     * @param container
     */
    @Override
    public final void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);

        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }

    }

    private String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object != null) {
            mCurrentPrimaryItem = (Fragment) object;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mModify) {
            if (mCurTransaction == null) {
                mCurTransaction = mFM.beginTransaction();
            }
            mCurTransaction.remove((Fragment) object);
        } else {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mModify = false;
    }

    /**
     * 和viewpager的setOffscreenPageLimit冲突, loop为true的时候不能设置
     *
     * @return
     */
    public boolean isLoop() {
        return false;
    }
}
