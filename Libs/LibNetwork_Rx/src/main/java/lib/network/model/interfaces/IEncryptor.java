package lib.network.model.interfaces;

import lib.network.model.NetworkReq;

/**
 * 加密拦截器
 *
 * @auther yuansui
 * @since 2017/8/24
 */
public interface IEncryptor {
    void encrypt(NetworkReq.Builder builder);
}
