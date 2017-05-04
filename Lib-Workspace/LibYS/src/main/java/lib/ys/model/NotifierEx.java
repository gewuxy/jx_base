package lib.ys.model;

import android.annotation.TargetApi;
import android.os.Build;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import lib.ys.util.GenericUtil;
import lib.ys.util.UtilEx;


/**
 * 泛型通知
 *
 * @param <I>
 */
abstract public class NotifierEx<I> {

    private List<I> mObservers = new ArrayList<>();

    public synchronized void add(I subscriber) {
        mObservers.add(subscriber);
    }

    public synchronized void remove(I subscriber) {
        mObservers.remove(subscriber);
    }

    /**
     * 发布通知-附带内容
     *
     * @param type
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.N)
    public synchronized void notify(final int type, final Object data) {
        I[] arrays = null;
        synchronized (this) {
            Class clz = GenericUtil.getClassType(getClass());
            arrays = (I[]) Array.newInstance(clz, mObservers.size());
            arrays = mObservers.toArray(arrays);
        }

        // 涉及到ui的刷新, 所以要保证在ui线程运行
        if (arrays != null) {
            I[] finalArrays = arrays;
            UtilEx.runOnUIThread(() -> {
                for (I o : finalArrays) {
                    callback(o, type, data);
                }
            });
        }
    }

    abstract protected void callback(I o, int type, Object data);

    /**
     * 发布通知-只有消息, 无内容
     *
     * @param type
     */
    public synchronized void notify(int type) {
        notify(type, null);
    }
}
