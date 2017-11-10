package lib.network.model.pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther yuansui
 * @since 2017/11/10
 */

public class BasePairs<T> {
    private List<Pair<T>> mData;

    public BasePairs() {
        mData = new ArrayList<>();
    }

    public void add(String name, T val) {
        if (val == null) {
            return;
        }
        mData.add(new Pair(name, val));
    }

    public void add(BasePairs<T> pairs) {
        if (pairs == null) {
            return;
        }
        mData.addAll(pairs.getData());
    }

    public Pair<T> get(int index) {
        return mData.get(index);
    }

    public List<Pair<T>> getData() {
        return mData;
    }

    public boolean isEmpty() {
        return mData.isEmpty();
    }

    public int size() {
        return mData.size();
    }
}
