package yy.doctor.ui.activity.login;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import lib.network.model.NetworkResp;
import lib.wx.WXLoginApi;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.ui.other.NavBar;
import lib.ys.util.RegexUtil;
import lib.ys.util.TextUtil;
import lib.yy.network.Result;
import lib.yy.notify.Notifier.NotifyType;
import lib.yy.ui.activity.base.BaseActivity;
import yy.doctor.Constants;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.dialog.ForgetPwdTooltipDialog;
import yy.doctor.dialog.HintDialogSec;
import yy.doctor.model.Profile;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;
import yy.doctor.sp.SpApp;
import yy.doctor.ui.activity.MainActivity;
import yy.doctor.ui.activity.register.RegisterActivity;
import yy.doctor.util.UISetter;
import yy.doctor.view.AutoCompleteEditText;

/**
 * 登录
 *
 * @author CaiXiang
 * @since 2017/4/19
 */
public class LoginActivity extends BaseActivity {

    private AutoCompleteEditText mEtName;
    private ImageView mIvClearName; // 清除用户名
    private EditText mEtPwd;
    private CheckBox mCbPwdVisible; // 密码是否可见
    private ImageView mIvClearPwd; // 清除密码
    private TextView mTvRegister;

    private String mRequest;
    private HintDialogSec mDialogWX;
    private ForgetPwdTooltipDialog mDialogForgetPwd;

    @Override
    public void initData() {
        mRequest = getIntent().getStringExtra(Extra.KData);
    }

    @NonNull
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
        mCbPwdVisible = findView(R.id.login_cb_visible_pwd);
        mIvClearName = findView(R.id.login_iv_cancel);
        mIvClearPwd = findView(R.id.login_iv_cancel_pwd);
        mTvRegister = findView(R.id.login_tv_login);
    }

    @Override
    public void setViews() {
        // 清空用户信息
        Profile.inst().clear();
        // 设置密码输入范围
        UISetter.setPwdRange(mEtPwd);

        setOnClickListener(R.id.login_tv_login);
        setOnClickListener(R.id.login_tv_register);
        setOnClickListener(R.id.login_tv_forget_pwd);
        setOnClickListener(R.id.login_cb_visible_pwd);
        setOnClickListener(R.id.login_layout_wechat);
        setOnClickListener(R.id.login_iv_cancel);
        setOnClickListener(R.id.login_iv_cancel_pwd);
        textChanged();

        mEtName.setText(SpApp.inst().getUserName());

        mCbPwdVisible.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //把光标设置到当前文本末尾
                mEtPwd.setSelection(mEtPwd.getText().length());
            } else {
                mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mEtPwd.setSelection(mEtPwd.getText().length());
            }
        });

        WXLoginApi.create(this, Constants.KAppId);
    }

    public void textChanged() {

        mTvRegister.setEnabled(false);
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((RegexUtil.isMobileCN(mEtName.getText().toString()) || RegexUtil.isEmail(mEtName.getText().toString()))
                        && !TextUtil.isEmpty(mEtPwd.getText().toString())) {
                    mTvRegister.setClickable(true);
                } else {
                    mTvRegister.setClickable(false);
                }

                if (TextUtil.isEmpty(mEtName.getText())) {
                    hideView(mIvClearName);
                } else {
                    showView(mIvClearName);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtil.isEmpty(mEtName.getText()) || TextUtil.isEmpty(mEtPwd.getText())) {
                    mTvRegister.setEnabled(false);
                } else {
                    mTvRegister.setEnabled(true);
                }

                if (TextUtil.isEmpty(mEtPwd.getText())) {
                    hideView(mIvClearPwd);
                } else {
                    showView(mIvClearPwd);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv_login: {
                String strName = mEtName.getText().toString().trim();
                String strPwd = mEtPwd.getText().toString().trim();
                if (TextUtil.isEmpty(strName)) {
                    showToast(R.string.input_name);
                    return;
                }
                if (TextUtil.isEmpty(strPwd)) {
                    showToast(R.string.input_pwd);
                    return;
                }
                refresh(RefreshWay.dialog);
                exeNetworkReq(NetFactory.login(strName, strPwd));
            }
            break;
            case R.id.login_tv_register: {
                startActivity(RegisterActivity.class);
            }
            break;
            case R.id.login_tv_forget_pwd: {
                mDialogForgetPwd = new ForgetPwdTooltipDialog(LoginActivity.this);
                mDialogForgetPwd.show();
            }
            break;
            case R.id.login_layout_wechat: {
                if (WXLoginApi.isWXAppInstalled()) {
                    WXLoginApi.sendReq();
                } else {
                    // 未安装微信
                    mDialogWX = new HintDialogSec(LoginActivity.this);
                    mDialogWX.setMainHint(R.string.wx_accredit_error);
                    mDialogWX.setSecHint(R.string.wx_check_normal);
                    mDialogWX.addButton(R.string.affirm, v1 -> mDialogWX.dismiss());
                    mDialogWX.show();
                }
            }
            break;
            case R.id.login_iv_cancel: {
                mEtName.setText("");
                SpApp.inst().saveUserName("");
            }
            break;
            case R.id.login_iv_cancel_pwd: {
                mEtPwd.setText("");
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
            //保存用户名
            SpApp.inst().saveUserName(mEtName.getText().toString().trim());
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
    protected void onDestroy() {
        super.onDestroy();

        if (mDialogWX != null) {
            mDialogWX.dismiss();
        }
    }

    @Override
    public void onNotify(@NotifyType int type, Object data) {

        if (type == NotifyType.login) {
            finish();
        }
    }

}
