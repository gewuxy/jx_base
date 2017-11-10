package lib.network.model.interfaces;

import lib.network.model.NetworkResp;

/**
 * 干涉Result的拦截器
 *
 * @auther yuansui
 * @since 2017/11/10
 */
public interface Interceptor {
    <T extends IResult<T>> T intercept(int id, NetworkResp r);
}
