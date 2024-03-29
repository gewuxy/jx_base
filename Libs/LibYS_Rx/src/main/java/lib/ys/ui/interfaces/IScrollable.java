package lib.ys.ui.interfaces;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

/**
 * 可以滑动的
 *
 * @author yuansui
 */
public interface IScrollable<T, V extends View> {

    void findViews(@NonNull View contentView,
                   @IdRes int scrollableId,
                   @Nullable View header,
                   @Nullable View footer,
                   @Nullable View empty);

    void setViews();

    V getScrollableView();

    void addFooterView(View v);

    void showFooterView();

    void hideFooterView();

    void showHeaderView();

    void hideHeaderView();

    boolean isEmpty();

    void addEmptyViewIfNonNull();

    void setData(List<T> data);

    void addAll(List<T> data);

    /**
     * 刷新整个列表
     */
    void invalidate();

    /**
     * 刷新单个item
     *
     * @param position
     */
    void invalidate(int position);

    void onDestroy();
}
