package lib.network.provider;

import lib.network.model.OnNetworkListener;
import lib.network.model.NetworkMethod;
import lib.network.model.NetworkReq;

/**
 * @author yuansui
 */
public interface IRequestBuilder {
    /**
     * 请求的id(what)
     *
     * @return
     */
    int id();

    /**
     * 绑定的tag(class name)
     *
     * @return
     */
    Object tag();

    @NetworkMethod
    int method();

    NetworkReq request();

    OnNetworkListener listener();

    <T> T build();
}
