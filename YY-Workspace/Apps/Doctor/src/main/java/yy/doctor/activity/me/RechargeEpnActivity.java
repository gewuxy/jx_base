package yy.doctor.activity.me;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import lib.yy.activity.base.BaseActivity;
import yy.doctor.R;
import yy.doctor.util.Util;

/**
 * 象数充值
 *
 * @author CaiXiang
 * @since 2017/4/14
 */
public class RechargeEpnActivity extends BaseActivity {

    private TextView mTv;

    @Override
    public void initData() {

    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_recharge_epn;
    }

    @Override
    public void initNavBar() {

        Util.addBackIcon(getNavBar(), "象数充值", this);

    }

    @Override
    public void findViews() {

    }

    @Override
    public void setViewsValue() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

}
