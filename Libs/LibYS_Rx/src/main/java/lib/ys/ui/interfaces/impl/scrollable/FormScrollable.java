package lib.ys.ui.interfaces.impl.scrollable;

import android.widget.ScrollView;

import java.util.List;

import lib.ys.ui.interfaces.listener.scrollable.OnFormScrollableListener;

/**
 * @auther yuansui
 * @since 2017/7/20
 */
public class FormScrollable<T> extends BaseScrollable<T, ScrollView> {

    public FormScrollable(OnFormScrollableListener<T> l) {
        super(l);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void setData(List<T> data) {

    }

    @Override
    public void addAll(List<T> data) {

    }

    @Override
    public void invalidate(int position) {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setViews() {

    }
}
