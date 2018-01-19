package lib.ys.ui.interfaces.listener;

import lib.network.model.interfaces.IResult;

/**
 * @auther : GuoXuan
 * @since : 2018/1/19
 */
public interface onInterceptNetListener {
    boolean onIntercept(IResult r, Object... o);
}
