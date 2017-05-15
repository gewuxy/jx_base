package lib.ys.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONException;

import lib.network.error.NetError;
import lib.network.model.NetworkRequest;
import lib.network.model.NetworkResponse;
import lib.network.model.OnNetworkListener;
import lib.ys.LogMgr;
import lib.ys.ui.interfaces.opts.NetworkOpt;
import lib.ys.ui.interfaces.opts.impl.NetworkOptImpl;


abstract public class ServiceEx extends Service implements NetworkOpt, OnNetworkListener {

    protected final String TAG = getClass().getSimpleName();

    private NetworkOptImpl mNetwork;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public final void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent != null) {
            onHandleIntent(intent);
        }
    }

    abstract protected void onHandleIntent(Intent intent);

    /**
     * http task part
     */

    @Override
    public void exeNetworkRequest(int id, NetworkRequest request) {
        exeNetworkRequest(id, request, this);
    }

    @Override
    public void exeNetworkRequest(int id, NetworkRequest request, OnNetworkListener listener) {
        if (mNetwork != null) {
            mNetwork.exeNetworkRequest(id, request, listener);
        }
    }

    @Override
    public void cancelAllNetworkRequest() {
        if (mNetwork != null) {
            mNetwork.cancelAllNetworkRequest();
        }
    }

    @Override
    public void cancelNetworkRequest(int id) {
        if (mNetwork != null) {
            mNetwork.cancelNetworkRequest(id);
        }
    }

    @Override
    public Object onNetworkResponse(int id, NetworkResponse nr) throws JSONException {
        return null;
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {
    }

    @Override
    public void onNetworkError(int id, NetError error) {
        Exception e = error.getException();
        if (e != null) {
            LogMgr.d(TAG, "onNetworkError: id = " + id);
            LogMgr.d(TAG, "onNetworkError: e = " + e.getMessage());
            LogMgr.d(TAG, "onNetworkError: msg = " + error.getMessage());
            LogMgr.d(TAG, "onNetworkError: end=======================");
        } else {
            LogMgr.d(TAG, "onNetworkError(): " + "tag = [" + id + "], error = [" + error.getMessage() + "]");
        }

        retryNetworkRequest(id);
    }

    @Override
    public void onNetworkProgress(int id, float progress, long totalSize) {
    }

    protected boolean retryNetworkRequest(int id) {
        if (mNetwork != null) {
            return mNetwork.retryNetworkRequest(id);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mNetwork != null) {
            mNetwork.onDestroy();
            mNetwork = null;
        }
    }

    protected void startService(Class<? extends Service> clz) {
        startService(new Intent(this, clz));
    }
}
