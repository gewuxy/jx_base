package lib.network.model.interfaces;


import lib.network.model.NetworkError;
import lib.network.model.NetworkResp;

/**
 * 网络任务监听
 *
 * @author yuansui
 */
public interface OnNetworkListener {
    /**
     * 任务数据回调
     *
     * @param id 编号
     * @param resp  返回的数据
     * @return
     */
    IResult onNetworkResponse(int id, NetworkResp resp) throws Exception;

    /**
     * 任务成功
     *
     * @param id     编号
     * @param r 解析后的数据
     */
    void onNetworkSuccess(int id, IResult r);

    /**
     * 任务错误
     *
     * @param id    编号
     * @param error 错误
     */
    void onNetworkError(int id, NetworkError error);

    /**
     * 任务下载或上传进度
     *
     * @param id        编号
     * @param progress  进度
     * @param totalSize 总大小
     */
    void onNetworkProgress(int id, float progress, long totalSize);
}
