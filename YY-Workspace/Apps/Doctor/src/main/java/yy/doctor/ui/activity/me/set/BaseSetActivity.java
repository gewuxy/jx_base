package yy.doctor.ui.activity.me.set;

import android.support.annotation.CallSuper;
import android.widget.TextView;

import lib.ys.ui.other.NavBar;
import lib.yy.ui.activity.base.BaseFormActivity;
import yy.doctor.R;
import yy.doctor.util.Util;

/**
 * @auther : GuoXuan
 * @since : 2017/7/24
 */
public abstract class BaseSetActivity extends BaseFormActivity {

    private TextView mTvSet;

    @CallSuper
    @Override
    public void findViews() {
        super.findViews();

        mTvSet = findView(R.id.activity_tv_set_set);
    }

    @Override
    public final int getContentViewId() {
        return R.layout.activity_set;
    }

    @Override
    public final void initNavBar(NavBar paramNavBar) {
        Util.addBackIcon(paramNavBar, getNavBarText(), this);
    }

    @CallSuper
    @Override
    public void setViews() {
        super.setViews();

        mTvSet.setEnabled(false);
        mTvSet.setText(getSetText());
        setOnClickListener(R.id.activity_tv_set_set);
    }

    protected final void setChanged(boolean paramBoolean) {
        mTvSet.setEnabled(paramBoolean);
    }

    public abstract CharSequence getNavBarText();

    public abstract CharSequence getSetText();
}