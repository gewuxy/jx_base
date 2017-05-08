package lib.network;


import lib.network.model.OnNetworkListener;
import lib.network.model.NetworkRequest;
import lib.network.provider.BaseProvider;
import lib.network.provider.ok.OkProvider;

/**
 * 网络任务执行者
 *
 * @author yuansui
 * @since 2016/4/11
 */
public class NetworkExecutor {
    private OnNetworkListener mListener;
    private BaseProvider mProvider;

    public NetworkExecutor(Object tag, OnNetworkListener listener) {
        mListener = listener;
        mProvider = new OkProvider(tag);
    }

    public void execute(int id, NetworkRequest task) {
        execute(id, task, mListener);
    }

    public void execute(int id, NetworkRequest request, OnNetworkListener listener) {
        if (listener == null) {
            listener = mListener;
        }
        mProvider.load(request, id, listener);
    }

    public void cancel(int id) {
        mProvider.cancel(id);
    }

    public void cancelAll() {
        mProvider.cancelAll();
    }

    public void destroy() {
        cancelAll();
    }
}
