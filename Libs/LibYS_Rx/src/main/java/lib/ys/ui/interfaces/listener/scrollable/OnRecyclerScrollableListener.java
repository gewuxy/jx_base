package lib.ys.ui.interfaces.listener.scrollable;

import android.support.annotation.NonNull;

import lib.ys.adapter.MultiAdapterEx.OnAdapterClickListener;
import lib.ys.adapter.interfaces.IAdapter;
import lib.ys.ui.interfaces.listener.OnScrollMixListener;
import lib.ys.view.recycler.WrapRecyclerView;

/**
 * recycler view的所有方法
 *
 * @author yuansui
 */
public interface OnRecyclerScrollableListener<T, A extends IAdapter<T>>
        extends OnScrollableListener<T, WrapRecyclerView> {

    @NonNull
    A getAdapter();

    void setOnAdapterClickListener(OnAdapterClickListener listener);

    void setOnScrollListener(OnScrollMixListener listener);

    int getItemRealPosition(int position);

    int getFirstVisiblePosition();

    int getHeaderViewPosition();

}
