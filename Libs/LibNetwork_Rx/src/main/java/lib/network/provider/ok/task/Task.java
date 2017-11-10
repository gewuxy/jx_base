package lib.network.provider.ok.task;

import lib.network.model.NetworkReq;
import lib.network.model.pair.Pair;
import lib.network.model.pair.Pairs;
import lib.network.provider.ok.OkClient;
import lib.network.provider.ok.callback.OkCallback;
import okhttp3.Call;
import okhttp3.Request;

/**
 * @auther yuansui
 * @since 2017/6/10
 */

abstract public class Task {

    private NetworkReq mReq;
    private OkCallback mCallback;
    private int mId;
    private Call mCall;

    public Task(int id, NetworkReq req, OkCallback callback) {
        mReq = req;
        mCallback = callback;
        mId = id;
    }

    public void run() {
        Request request = buildRealReq();
        mCall = OkClient.inst().newCall(request);
        mCall.enqueue(mCallback);
    }

    protected NetworkReq getReq() {
        return mReq;
    }

    protected void addHeaders(Request.Builder builder) {
        Pairs headers = getReq().getHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Pair<String> header : headers.getData()) {
                builder.addHeader(header.getName(), header.getVal());
            }
        }
    }

    protected int getId() {
        return mId;
    }

    protected OkCallback getCallback() {
        return mCallback;
    }

    abstract public Request buildRealReq();

    public void cancel() {
        if (mCall != null && !mCall.isCanceled() && mCall.isExecuted()) {
            mCall.cancel();
        }
    }

    public boolean isExecuted() {
        return mCall == null ? false : mCall.isExecuted();
    }
}
