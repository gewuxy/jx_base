package lib.network.provider;

import lib.network.model.NetworkReq;
import lib.network.model.interfaces.OnNetworkListener;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * @author yuansui
 */
abstract public class BaseProvider {

    protected String TAG = getClass().getSimpleName();

    abstract public void load(NetworkReq req, Object tag, int id, OnNetworkListener lsn);

    /**
     * 开启一个web socket
     *
     * @param req
     * @param lsn 暂时先使用ok的listener
     */
    abstract public WebSocket loadWebSocket(NetworkReq req, WebSocketListener lsn);

    /**
     * 取消对应tag的单个请求
     *
     * @param tag
     * @param id
     */
    abstract public void cancel(Object tag, int id);

    /**
     * 取消对应tag的所有网络请求
     *
     * @param tag
     */
    abstract public void cancelAll(Object tag);

    /**
     * 取消所有网络请求
     */
    abstract public void cancelAll();
}
