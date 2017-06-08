package yy.doctor.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import lib.network.model.NetworkResp;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.ui.other.NavBar;
import lib.yy.Notifier.NotifyType;
import lib.yy.activity.base.BaseActivity;
import lib.yy.network.Result;
import yy.doctor.BuildConfig;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.activity.register.RegisterActivity;
import yy.doctor.model.Profile;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;
import yy.doctor.view.AutoCompleteEditText;

/**
 * 登录
 *
 * @author CaiXiang
 * @since 2017/4/19
 */
public class LoginActivity extends BaseActivity {

    private AutoCompleteEditText mEtName;
    private EditText mEtPwd;
    private String mRequest;

    @Override
    public void initData() {
        mRequest = getIntent().getStringExtra(Extra.KData);
    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initNavBar(NavBar bar) {
    }

    @Override
    public void findViews() {

        mEtName = findView(R.id.login_et_name);
        mEtPwd = findView(R.id.login_et_pwd);
    }

    @Override
    public void setViews() {

        setOnClickListener(R.id.login_tv);
        setOnClickListener(R.id.login_tv_register);
        setOnClickListener(R.id.login_tv_forget_pwd);
        if (BuildConfig.TEST) {
            mEtName.setText("wucaixiang@medcn.cn");
            mEtPwd.setText("123456");
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.login_tv: {

                String strName = mEtName.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                if (strName.isEmpty()) {
                    showToast("请输入用户名");
                    return;
                }
                if (strPwd.isEmpty()) {
                    showToast("请输入密码");
                    return;
                }
                refresh(RefreshWay.dialog);
                exeNetworkReq(0, NetFactory.login(strName, strPwd));
            }
            break;
            case R.id.login_tv_register: {
                startActivity(RegisterActivity.class);
            }
            break;
            case R.id.login_tv_forget_pwd: {
                startActivity(ForgetPwdActivity.class);
            }
            break;
        }
    }

    @Override
    public Object onNetworkResponse(int id, NetworkResp r) throws Exception {
        return JsonParser.ev(r.getText(), Profile.class);
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {
        stopRefresh();

        Result<Profile> r = (Result<Profile>) result;
        if (r.isSucceed()) {
            Profile.inst().update(r.getData());
            //判断跳转到哪里
            if (mRequest == null) {
                startActivity(MainActivity.class);
            } else {
                setResult(RESULT_OK);
            }
            finish();
        } else {
            showToast(r.getError());
        }
    }

    @Override
    public void onNotify(@NotifyType int type, Object data) {
        super.onNotify(type, data);
        if (type == NotifyType.login) {
            LoginActivity.this.finish();
        }
    }

}
