package lib.network.model.pair;

import android.text.TextUtils;

import lib.network.NetworkConstants;

public class Pair<T> {
    private String mName = NetworkConstants.KEmpty;
    private T mVal;

    public Pair(String name, T val) {
        setName(name);
        setVal(val);
    }

    public void setName(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setVal(T val) {
        if (val == null) {
            return;
        }
        mVal = val;
    }

    public T getVal() {
        return mVal;
    }

}
