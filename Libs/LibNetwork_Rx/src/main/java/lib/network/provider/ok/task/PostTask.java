package lib.network.provider.ok.task;

import lib.network.NetworkLog;
import lib.network.NetworkUtil;
import lib.network.model.NetworkReq;
import lib.network.model.pair.Pair;
import lib.network.model.pair.Pairs;
import lib.network.provider.ok.callback.OkCallback;
import okhttp3.FormBody;
import okhttp3.Request;

/**
 * @auther yuansui
 * @since 2017/6/10
 */

public class PostTask extends Task {


    public PostTask(int id, NetworkReq req, OkCallback callback) {
        super(id, req, callback);
    }

    @Override
    public Request buildRealReq() {
        String url = NetworkUtil.generateGetUrl(getReq().getUrl(), getReq().getParams());
        NetworkLog.d("url_post = " + url);

        FormBody.Builder b = new FormBody.Builder();
        Pairs params = getReq().getParams();
        if (params != null) {
            for (Pair<String> p : params.getData()) {
                b.add(p.getName(), p.getVal());
            }
        }

        Request.Builder reqBuilder = new Request.Builder()
                .tag(getId())
                .url(getReq().getUrl())
                .post(b.build());

        addHeaders(reqBuilder);

        return reqBuilder.build();
    }
}
