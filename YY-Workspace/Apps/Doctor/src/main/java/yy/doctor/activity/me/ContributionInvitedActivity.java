package yy.doctor.activity.me;

import android.support.annotation.NonNull;

import lib.yy.activity.base.BaseActivity;
import yy.doctor.R;
import yy.doctor.util.Util;

/**
 * 征稿启事
 *
 * @author CaiXiang
 * @since 2017/4/20
 */
public class ContributionInvitedActivity extends BaseActivity {

    @Override
    public void initData() {

    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_contribution_invited;
    }

    @Override
    public void initTitleBar() {

        Util.addBackIcon(getTitleBar(), "征稿启事", this);

    }

    @Override
    public void findViews() {

    }

    @Override
    public void setViewsValue() {

    }

}
