package lib.yy.frag.base;

import lib.ys.ui.frag.ViewPagerFragEx;
import lib.yy.Notifier;
import lib.yy.Notifier.OnNotify;
import lib.yy.Notifier.NotifyType;

/**
 * @auther yuansui
 * @since 2017/4/24
 */

abstract public class BaseVPFrag extends ViewPagerFragEx implements OnNotify {

    @Override
    protected void afterInitCompleted() {
        Notifier.inst().add(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Notifier.inst().remove(this);
    }

    protected void notify(@NotifyType int type, Object data) {
        Notifier.inst().notify(type, data);
    }

    protected void notify(@NotifyType int type) {
        Notifier.inst().notify(type);
    }

    @Override
    public void onNotify(@NotifyType int type, Object data) {
    }
}