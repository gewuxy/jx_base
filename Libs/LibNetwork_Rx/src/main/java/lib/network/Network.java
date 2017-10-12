package lib.network;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lib.network.model.NetworkReq;
import lib.network.model.interfaces.OnNetworkListener;
import lib.network.provider.BaseProvider;
import lib.network.provider.ok.OkProvider;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 网络任务执行者
 *
 * @author yuansui
 * @since 2016/4/11
 */
public class Network {
    private OnNetworkListener mListener;
    private Object mTag;

    private static BaseProvider mProvider;
    private static Context mContext;
    private static NetworkConfig mConfig;

    public Network(Object tag, OnNetworkListener listener) {
        mListener = listener;
        mTag = tag;
    }

    public void load(int id, @NonNull NetworkReq req, @Nullable OnNetworkListener l) {
        if (l == null) {
            l = mListener;
        }
        mProvider.load(req, mTag, id, l);
    }

    public WebSocket loadWebSocket(@NonNull NetworkReq req, @NonNull WebSocketListener l) {
        return mProvider.loadWebSocket(req, l);
    }

    public void cancel(int id) {
        mProvider.cancel(mTag, id);
    }

    public void cancelAll() {
        mProvider.cancelAll(mTag);
    }

    public static void init(Context context, NetworkConfig config) {
        mContext = context;
        mConfig = config;
        mProvider = new OkProvider();
    }

    public static Context getContext() {
        return mContext;
    }

    public static NetworkConfig getConfig() {
        if (mConfig == null) {
            mConfig = NetworkConfigBuilder.create().build();
        }
        return mConfig;
    }

    /**
     * 关闭app的时候取消所有
     */
    public static void cancelOnTerminal() {
        if (mProvider != null) {
            mProvider.cancelAll();
        }
    }
}
