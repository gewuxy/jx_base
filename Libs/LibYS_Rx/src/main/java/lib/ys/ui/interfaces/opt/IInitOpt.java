package lib.ys.ui.interfaces.opt;


import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lib.ys.ui.other.NavBar;

/**
 * 初始化操作
 *
 * @author yuansui
 */
public interface IInitOpt {

    /**
     * 初始化数据
     * 由于提前初始化了, 所以这时候view还没有加载成功, 不要使用view相关方法
     */
    void initData();

    /**
     * 获取ContentView的Id
     *
     * @return id
     */
    @NonNull
    @LayoutRes
    int getContentViewId();

    /**
     * 获取ContentView的Header的Id
     *
     * @return
     */
    @Nullable
    @LayoutRes
    int getContentHeaderViewId();

    /**
     * 获取ContentView的Footer的Id
     *
     * @return
     */
    @Nullable
    @LayoutRes
    int getContentFooterViewId();

    /**
     * 初始化navBar
     */
    void initNavBar(NavBar bar);

    /**
     * 使用findViewById的方法
     */
    void findViews();

    /**
     * 设置事件或监听等
     */
    void setViews();
}
