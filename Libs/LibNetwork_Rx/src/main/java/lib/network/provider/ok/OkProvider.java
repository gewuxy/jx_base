package lib.network.provider.ok;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import lib.network.NetworkLog;
import lib.network.NetworkUtil;
import lib.network.model.NetworkMethod;
import lib.network.model.NetworkReq;
import lib.network.model.interfaces.OnNetworkListener;
import lib.network.provider.BaseProvider;
import lib.network.provider.ok.callback.CommonCallback;
import lib.network.provider.ok.callback.DownloadCallback;
import lib.network.provider.ok.callback.DownloadFileCallback;
import lib.network.provider.ok.task.DownloadTask;
import lib.network.provider.ok.task.GetTask;
import lib.network.provider.ok.task.PostTask;
import lib.network.provider.ok.task.Task;
import lib.network.provider.ok.task.UploadTask;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * @author yuansui
 */
public class OkProvider extends BaseProvider {

    private Map<Object, Map<Integer, Task>> mMapTag;

    public OkProvider() {
        mMapTag = new HashMap<>();
    }

    @Override
    public void load(NetworkReq req, Object tag, int id, OnNetworkListener lsn) {
        // FIXME: id的检测应该是在网络callback的时候进行, 暂时先放到这里, 如果出问题的话再更改
        Task t = getTask(tag, id);
        if (t != null) {
            if (t.isExecuted()) {
                removeTask(tag, id);
            } else {
                return;
            }
        }

        Task task = null;
        switch (req.getMethod()) {
            case NetworkMethod.get: {
                task = new GetTask(id, req, new CommonCallback(lsn));
            }
            break;
            case NetworkMethod.post: {
                task = new PostTask(id, req, new CommonCallback(lsn));
            }
            break;
            case NetworkMethod.upload: {
                task = new UploadTask(id, req, new CommonCallback(lsn));
            }
            break;
            case NetworkMethod.download: {
                task = new DownloadTask(id, req, new DownloadCallback(lsn));
            }
            break;
            case NetworkMethod.download_file: {
                task = new DownloadTask(id, req, new DownloadFileCallback(lsn, req.getDir(), req.getFileName()));
            }
            break;
        }

        if (task != null) {
            task.run();
            addTask(tag, id, task);
        }
    }

    @Override
    public WebSocket loadWebSocket(NetworkReq req, WebSocketListener lsn) {
        String url = NetworkUtil.generateGetUrl(req.getUrl(), req.getParams());
        NetworkLog.d("url_web socket = " + url);

        Request realReq = new Request.Builder()
                .url(url)
                .build();

        return OkClient.inst().newWebSocket(realReq, lsn);
    }

    @Override
    public void cancel(Object tag, int id) {
        Task task = getTask(tag, id);
        if (task != null) {
            task.cancel();
            removeTask(tag, id);
        }
    }

    @Override
    public void cancelAll(Object tag) {
        Flowable.just(mMapTag.get(tag))
                .filter(integerTaskMap -> integerTaskMap != null)
                .flatMap(integerTaskMap -> Flowable.fromIterable(integerTaskMap.values()))
                .subscribe(task -> task.cancel());
    }

    @Override
    public void cancelAll() {
        Flowable.fromIterable(mMapTag.values())
                .flatMap(integerTaskMap -> Flowable.fromIterable(integerTaskMap.values()))
                .subscribe(task -> task.cancel());
        mMapTag.clear();
    }

    private void addTask(Object tag, int id, Task task) {
        Map<Integer, Task> taskMap = mMapTag.get(tag);
        if (taskMap == null) {
            taskMap = new HashMap<>();
            mMapTag.put(tag, taskMap);
        }
        taskMap.put(id, task);
    }

    private Task getTask(Object tag, int id) {
        Map<Integer, Task> taskMap = mMapTag.get(tag);
        if (taskMap != null) {
            return taskMap.get(id);
        }
        return null;
    }

    private Task removeTask(Object tag, int id) {
        Map<Integer, Task> taskMap = mMapTag.get(tag);
        if (taskMap != null) {
            return taskMap.remove(id);
        }
        return null;
    }
}
