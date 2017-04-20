package yy.doctor.activity.register;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONException;

import lib.ys.LogMgr;
import lib.ys.adapter.MultiGroupAdapterEx;
import lib.ys.adapter.ViewHolderEx;
import lib.ys.network.resp.IListResponse;
import lib.ys.util.UIUtil;
import lib.ys.view.SideBar;
import lib.yy.activity.base.BaseSRGroupListActivity;
import yy.doctor.BuildConfig;
import yy.doctor.R;
import yy.doctor.adapter.HospitalAdapter;
import yy.doctor.model.hospital.GroupHospital;
import yy.doctor.model.hospital.Hospital;

/**
 * 选择医院的界面
 *
 * 日期 : 2017/4/19
 * 创建人 : guoxuan
 */

public class HospitalActivity extends BaseSRGroupListActivity<GroupHospital> {

    private SideBar mSideBar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_hospital;
    }

    @Override
    public void findViews() {
        super.findViews();

        mSideBar = (SideBar) getDecorView().findViewById(R.id.side_bar);
        initSideBar();
    }

    @Override
    public void initData() {

        GroupHospital groupHospital = new GroupHospital();
        for (int i = 0; i < 10; i++) {
            Hospital hospital = new Hospital();
            groupHospital.add(hospital);
        }

        addItem(groupHospital);
        addItem(groupHospital);
        addItem(groupHospital);
    }

    /**
     * 初始化SideBar
     */
    private void initSideBar() {
        mSideBar.setTextSize(UIUtil.dpToPx(10.0F,HospitalActivity.this));
        mSideBar.setPaintColor(Color.parseColor("#888888"));
    }

    @Override
    public void initTitleBar() {
//        Util.addBackIcon(getTitleBar(), 0, this);
    }

    @Override
    public MultiGroupAdapterEx<GroupHospital, ? extends ViewHolderEx> createAdapter() {
        return new HospitalAdapter();
    }

    @Override
    public void setViewsValue() {
        super.setViewsValue();

        expandAllGroup();
    }

    @Override
    public void getDataFromNet() {
        //
    }

    @Override
    public IListResponse<GroupHospital> parseNetworkResponse(int id, String text) throws JSONException {
        return null;
    }

    @Override
    public boolean canAutoRefresh() {
        if (BuildConfig.TEST) {
            return false;
        } else {
            return true;
        }
    }
}
