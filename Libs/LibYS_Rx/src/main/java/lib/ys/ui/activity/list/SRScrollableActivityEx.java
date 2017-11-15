package lib.ys.ui.activity.list;

import lib.ys.adapter.interfaces.IAdapter;

/**
 * @author yuansui
 */
abstract public class SRScrollableActivityEx<T, A extends IAdapter<T>> extends SRListActivityEx<T, A> {

    /**
     * 子布局才会知道
     *
     * @return
     */
    @Override
    abstract public int getSRLayoutResId();

    @Override
    abstract public int getContentViewId();
}
