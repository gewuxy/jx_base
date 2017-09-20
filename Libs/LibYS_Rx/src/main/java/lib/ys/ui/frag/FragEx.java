package lib.ys.ui.frag;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.network.Network;
import lib.network.model.NetworkError;
import lib.network.model.NetworkReq;
import lib.network.model.NetworkResp;
import lib.network.model.interfaces.OnNetworkListener;
import lib.ys.AppEx;
import lib.ys.R;
import lib.ys.YSLog;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.fitter.DpFitter;
import lib.ys.fitter.LayoutFitter;
import lib.ys.impl.LoadingDialogImpl;
import lib.ys.stats.Stats;
import lib.ys.ui.decor.DecorViewEx;
import lib.ys.ui.decor.DecorViewEx.TNavBarState;
import lib.ys.ui.decor.DecorViewEx.ViewState;
import lib.ys.ui.dialog.DialogEx;
import lib.ys.ui.interfaces.impl.NetworkOpt;
import lib.ys.ui.interfaces.impl.PermissionOpt;
import lib.ys.ui.interfaces.listener.OnRetryClickListener;
import lib.ys.ui.interfaces.opt.ICommonOpt;
import lib.ys.ui.interfaces.opt.IFitOpt;
import lib.ys.ui.interfaces.opt.IInitOpt;
import lib.ys.ui.interfaces.opt.INetworkOpt;
import lib.ys.ui.interfaces.opt.IRefreshOpt;
import lib.ys.ui.other.NavBar;
import lib.ys.util.DeviceUtil;
import lib.ys.util.InjectUtil;
import lib.ys.util.UtilEx;
import lib.ys.util.permission.OnPermissionListener;
import lib.ys.util.permission.Permission;
import lib.ys.util.permission.PermissionResult;
import lib.ys.util.res.ResLoader;
import lib.ys.util.view.ViewUtil;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

abstract public class FragEx extends Fragment implements
        IInitOpt,
        INetworkOpt,
        ICommonOpt,
        IFitOpt,
        IRefreshOpt,
        OnNetworkListener,
        OnClickListener,
        OnRetryClickListener,
        OnPermissionListener {

    protected final String TAG = getClass().getSimpleName();

    protected static final int RESULT_OK = Activity.RESULT_OK;

    private DecorViewEx mDecorView;

    private boolean mInitComplete = false;

    private FragEx mFragRoot;
    private FragEx mFragChild;

    private boolean mIsVisible = false;

    @RefreshWay
    private int mRefreshWay = getInitRefreshWay();

    /**
     * impls
     */
    private NetworkOpt mNetworkImpl;
    private PermissionOpt mPermission;

    private Unbinder mUnbinder;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDecorView != null) {
            return mDecorView;
        }

        initDecorView();

        return mDecorView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setOnRetryClickListener(this);

        if (mInitComplete) {
            return;
        }

        if (useLazyLoad()) {
            initNavBar(getNavBar());
            fit(getNavBar());

            // 如果这个时候只有一个fragment而且是显示的状态, 走到这里的时候已经过了onVisible的阶段了, 需要在这里调用init()
            if (getVisible()) {
                init();
            }
        } else {
            init();
        }
    }

    /**
     * viewpager专用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        handleVisibleChanged(isVisibleToUser);
    }

    /**
     * 传统方式使用
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        handleVisibleChanged(!hidden);
    }

    /**
     * {@link Fragment#isVisible()} is final
     *
     * @return
     */
    protected boolean getVisible() {
        return mIsVisible;
    }

    private void handleVisibleChanged(boolean visible) {
        mIsVisible = visible;

        if (useLazyLoad()) {
            if (visible) {
                if (getHost() != null) {
                    if (mDecorView == null) {
                        initDecorView();
                        init();
                    } else if (!mInitComplete) {
                        init();
                    }
                }
            }
        }

        if (visible) {
            onVisible();
            Stats.onFragmentVisible(getContext(), TAG);
        } else {
            onInvisible();
            Stats.onFragmentInvisible(getContext(), TAG);

            if (mInitComplete) {
                // 在切换fragment的时候清除掉fitter的set, 保证内存能顺利回收
                LayoutFitter.clearFitSet();
            }
        }
    }

    /**
     * fragment可见回调
     */
    protected void onVisible() {
    }

    /**
     * fragment不可见回调
     */
    protected void onInvisible() {
    }

    @Nullable
    @Override
    public int getContentHeaderViewId() {
        return 0;
    }

    @Nullable
    @Override
    public int getContentFooterViewId() {
        return 0;
    }

    private void initDecorView() {
        InjectUtil.bind(this);

        // 数据的初始化提前, 可以根据数据来装载不同的view id
        initData();

        mDecorView = new DecorViewEx(getActivity(), getNavBarState(), getInitRefreshWay(), initLoadingDialog());
        mDecorView.setContentView(getContentViewId(), getContentHeaderViewId(), getContentFooterViewId());
    }

    /**
     * 是否使用延迟加载, 可以和viewpager结合使用
     *
     * @return
     */
    protected boolean useLazyLoad() {
        return false;
    }

    private void init() {
        if (!useLazyLoad()) {
            initNavBar(getNavBar());
            fit(getNavBar());
        }

        mUnbinder = ButterKnife.bind(this, mDecorView);
        findViews();

        setViews();

        mInitComplete = true;
        afterInitCompleted();
    }

    @Override
    public void findViews() {
        // 不是必须要实现的了, 因为有butterKnife注入
    }

    protected View getHeaderView() {
        return mDecorView.getHeaderView();
    }

    protected View getFooterView() {
        return mDecorView.getFooterView();
    }

    /**
     * 初始化完成以后的通知
     */
    protected void afterInitCompleted() {
    }

    /**
     * 获取titleBar的状态
     *
     * @return null表示使用整体设置
     */
    protected TNavBarState getNavBarState() {
        return null;
    }

    @NonNull
    @RefreshWay
    public int getInitRefreshWay() {
        return AppEx.getConfig().getInitRefreshWay();
    }

    protected LayoutInflater getLayoutInflater() {
        return getActivity().getLayoutInflater();
    }

    protected View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
        return getLayoutInflater().inflate(resource, root);
    }

    protected View inflate(@LayoutRes int resource) {
        return getLayoutInflater().inflate(resource, null);
    }

    protected void runOnUIThread(Runnable r, long delay) {
        UtilEx.runOnUIThread(r, delay);
    }

    protected void runOnUIThread(Runnable r) {
        // 考虑到activity的切换动画时间, 加入延迟
        UtilEx.runOnUIThread(r, ResLoader.getInteger(R.integer.anim_default_duration));
    }

    /**
     * http task callback part
     */
    @Override
    public Object onNetworkResponse(int id, NetworkResp r) throws Exception {
        return null;
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {
    }

    @Override
    public void onNetworkError(int id, NetworkError error) {
        Exception e = error.getException();
        if (e != null) {
            YSLog.d(TAG, "onNetworkError: id = " + id);
            YSLog.d(TAG, "onNetworkError: e = " + e.getMessage());
            YSLog.d(TAG, "onNetworkError: msg = " + error.getMessage());
            YSLog.d(TAG, "onNetworkError: end=======================");
        } else {
            YSLog.d(TAG, "onNetworkError(): " + "tag = [" + id + "], error = [" + error.getMessage() + "]");
        }
        stopRefresh();
        showToast(error.getMessage());
    }

    @Override
    public void onNetworkProgress(int id, float progress, long totalSize) {
    }

    /**
     * http task part
     */
    @Override
    public void exeNetworkReq(NetworkReq req) {
        exeNetworkReq(KDefaultId, req);
    }

    @Override
    public void exeNetworkReq(int id, NetworkReq req) {
        exeNetworkReq(id, req, this);
    }

    @Override
    public void exeNetworkReq(int id, NetworkReq req, OnNetworkListener l) {
        if (mNetworkImpl == null) {
            mNetworkImpl = new NetworkOpt(this, this);
        }
        mNetworkImpl.exeNetworkReq(id, req, l);
    }

    @Override
    public WebSocket exeWebSocketReq(NetworkReq req, WebSocketListener l) {
        if (mNetworkImpl == null) {
            mNetworkImpl = new NetworkOpt(this, this);
        }
        return mNetworkImpl.exeWebSocketReq(req, l);
    }

    @Override
    public void cancelAllNetworkReq() {
        if (mNetworkImpl != null) {
            mNetworkImpl.cancelAllNetworkReq();
        }
    }

    @Override
    public void cancelNetworkReq(int id) {
        if (mNetworkImpl != null) {
            mNetworkImpl.cancelNetworkReq(id);
        }
    }

    protected NavBar getNavBar() {
        return mDecorView.getNavBar();
    }

    /**
     * 更改当前视图状态
     *
     * @param state
     */
    public final void setViewState(@ViewState int state) {
        mDecorView.setViewState(state);
    }

    protected boolean isActivityFinishing() {
        return getActivity() == null || getActivity().isFinishing();
    }

    public <T extends View> T findView(@IdRes int id) {
        return (T) mDecorView.findViewById(id);
    }

    @Override
    public void showView(View v) {
        ViewUtil.showView(v);
    }

    @Override
    public void hideView(View v) {
        ViewUtil.hideView(v);
    }

    @Override
    public void goneView(View v) {
        ViewUtil.goneView(v);
    }

    @Override
    public void showToast(String content) {
        AppEx.showToast(content);
    }

    @Override
    public void showToast(@StringRes int... resId) {
        AppEx.showToast(resId);
    }

    @Override
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(), clz));
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, this);
    }

    @Override
    public void startActivityForResult(Class<?> clz, int requestCode) {
        startActivityForResult(new Intent(getActivity(), clz), requestCode);
    }

    private void startActivityForResult(Intent intent, int requestCode, FragEx fragChild) {
        findRootFragment();
        if (mFragRoot != null) {
            // 不是root才需要保存
            mFragRoot.saveResponseChild(fragChild);
        }
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * 保存响应的子frag
     *
     * @param fragment
     */
    public void saveResponseChild(FragEx fragment) {
        mFragChild = fragment;
    }

    /**
     * 得到根Fragment
     *
     * @return 如果有root则返回root, 本身是root则返回null
     */
    @Nullable
    public void findRootFragment() {
        if (mFragRoot == null) {
            mFragRoot = (FragEx) getParentFragment();
            if (mFragRoot != null) {
                // 有root frag
                while (mFragRoot.getParentFragment() != null) {
                    mFragRoot = (FragEx) mFragRoot.getParentFragment();
                }
            }
        }
    }


    /**
     * 需要在此判断发起请求的对象, 特意用final修饰防止重写
     *
     * @see {@link #onResultData(int, int, Intent)}
     */
    @Deprecated
    @Override
    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFragChild != null) {
            // 子frag发起的需要回调
            mFragChild.onResultData(requestCode, resultCode, data);
        } else {
            // 本身发起的
            onResultData(requestCode, resultCode, data);
        }
    }

    /**
     * 用来替换{@link #onActivityResult(int, int, Intent)}来接收跳转回调的消息处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onResultData(int requestCode, int resultCode, Intent data) {
    }

    /************************
     * 适配相关
     */
    @Override
    public int fitDp(float dp) {
        return DpFitter.dp(dp);
    }

    @Override
    public void fitAbsByPx(View v, int x, int y) {
        LayoutFitter.fitAbsByPx(v, x, y);
    }

    @Override
    public void fit(View v) {
        LayoutFitter.fit(v);
    }

    protected void setBackgroundColor(@ColorInt int color) {
        mDecorView.setBackgroundColor(color);
    }

    protected void setBackgroundResource(@DrawableRes int resId) {
        mDecorView.setBackgroundResource(resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
        if (mNetworkImpl != null) {
            mNetworkImpl.onDestroy();
        }
    }

    protected Intent getIntent() {
        return getActivity().getIntent();
    }

    protected DecorViewEx getDecorView() {
        return mDecorView;
    }

    protected boolean initComplete() {
        return mInitComplete;
    }

    protected void setOnRetryClickListener(OnRetryClickListener listener) {
        mDecorView.setOnRetryClickListener(listener);
    }

    @ViewState
    protected int getViewState() {
        return mDecorView.getViewState();
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 设置点击事件监听
     *
     * @param resId view的id
     */
    protected void setOnClickListener(@IdRes int resId) {
        setOnClickListener(findView(resId));
    }

    protected void setOnClickListener(@NonNull View v) {
        if (v != null) {
            v.setOnClickListener(this);
        }
    }

    protected void setOnClickListener(@IdRes int resId, OnClickListener l) {
        View v = findView(resId);
        if (v != null) {
            v.setOnClickListener(l);
        }
    }

    protected void clearOnClickListener(@NonNull View v) {
        if (v != null) {
            v.setOnClickListener(null);
        }
    }

    @Override
    public boolean onRetryClick() {
        if (!DeviceUtil.isNetworkEnabled()) {
            showToast(Network.getConfig().getDisconnectToast());
            return true;
        }
        return false;
    }

    /**
     * 初始化loading dialog
     *
     * @return
     */
    protected DialogEx initLoadingDialog() {
        DialogEx dialog = new LoadingDialogImpl(getActivity());
        dialog.setOnCancelListener(dialog1 -> {
            stopRefresh();
            cancelAllNetworkReq();
        });
        return dialog;
    }

    protected void showLoadingDialog() {
        if (!isActivityFinishing() && mDecorView != null) {
            mDecorView.showLoadingDialog();
        }
    }

    protected void dismissLoadingDialog() {
        if (!isActivityFinishing() && mDecorView != null) {
            mDecorView.dismissLoadingDialog();
        }
    }

    protected void setResult(int resultCode, Intent data) {
        getActivity().setResult(resultCode, data);
    }

    protected void setResult(int resultCode) {
        getActivity().setResult(resultCode);
    }

    protected void finish() {
        getActivity().finish();
    }

    protected Fragment findFragmentById(int id) {
        return getFragmentManager().findFragmentById(id);
    }

    /**
     * 解决java.lang.IllegalStateException: No activity
     * <p>产生原因:当第一次从一个Activity启动Fragment，然后再去启动子Fragment的时候，存在指向Activity的变量，
     * 但当退出这些Fragment之后回到Activity，然后再进入Fragment的时候，这个变量变成null
     * </p>
     * <p>
     * 解决方法:在Fragment被detached的时候去重置ChildFragmentManager
     * </p>
     */
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (Exception e) {
            YSLog.e(TAG, "onDetach", e);
        }
    }

    /***********************************************************
     * 统一管理刷新方法
     */

    /**
     * 改变刷新形式, 不推荐使用, 会导致状态错乱
     * 暂时只给手动下拉刷新的回调使用
     *
     * @param way
     */
    public void setRefreshWay(@RefreshWay int way) {
        mRefreshWay = way;
    }

    /**
     * @param way
     */
    @Override
    public void refresh(@RefreshWay int way) {
        mRefreshWay = way;

        switch (way) {
            case RefreshWay.dialog: {
                dialogRefresh();
            }
            break;
            case RefreshWay.embed: {
                embedRefresh();
            }
            break;
            case RefreshWay.swipe: {
                swipeRefresh();
            }
            break;
        }
    }

    @Override
    public void stopRefresh() {
        switch (mRefreshWay) {
            case RefreshWay.dialog: {
                stopDialogRefresh();
            }
            break;
            case RefreshWay.embed: {
                stopEmbedRefresh();
            }
            break;
            case RefreshWay.swipe: {
                stopSwipeRefresh();
            }
            break;
        }
    }

    @Override
    public void dialogRefresh() {
        showLoadingDialog();
    }

    @Override
    public void embedRefresh() {
        setViewState(ViewState.loading);
    }

    @Override
    public void swipeRefresh() {
    }

    @Override
    public void stopDialogRefresh() {
        dismissLoadingDialog();
    }

    @Override
    public void stopEmbedRefresh() {
    }

    @Override
    public void stopSwipeRefresh() {
    }

    @RefreshWay
    public int getRefreshWay() {
        return mRefreshWay;
    }

    /***********************************
     * 有关DecorView的ViewTreeObserver的操作
     */

    /**
     * ViewTreeObserver是否Alive
     *
     * @return
     */
    protected boolean isViewTreeObserverAlive() {
        return mDecorView.getViewTreeObserver().isAlive();
    }

    protected ViewTreeObserver getViewTreeObserver() {
        return mDecorView.getViewTreeObserver();
    }

    protected boolean addOnPreDrawListener(OnPreDrawListener listener) {
        if (isViewTreeObserverAlive()) {
            getViewTreeObserver().addOnPreDrawListener(listener);
            return true;
        }
        return false;
    }

    protected void removeOnPreDrawListener(OnPreDrawListener listener) {
        if (getViewTreeObserver().isAlive()) {
            getViewTreeObserver().removeOnPreDrawListener(listener);
        }
    }

    protected boolean addOnGlobalLayoutListener(OnGlobalLayoutListener listener) {
        if (isViewTreeObserverAlive()) {
            getViewTreeObserver().addOnGlobalLayoutListener(listener);
            return true;
        }
        return false;
    }

    protected void removeOnGlobalLayoutListener(OnGlobalLayoutListener listener) {
        if (getViewTreeObserver().isAlive()) {
            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            } else {
                getViewTreeObserver().removeGlobalOnLayoutListener(listener);
            }
        }
    }

    /******************************
     * 以下是permission相关
     */

    /**
     * 检查权限
     *
     * @param ps
     * @param code
     * @return
     */
    protected boolean checkPermission(int code, @Permission String... ps) {
        if (mPermission == null) {
            mPermission = new PermissionOpt(getContext(), this);
        }
        return mPermission.checkPermission(code, ps);
    }

    /**
     * 已无用, ActivityEx去掉了对fragment的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Deprecated
    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(int code, @PermissionResult int result) {
    }
}
