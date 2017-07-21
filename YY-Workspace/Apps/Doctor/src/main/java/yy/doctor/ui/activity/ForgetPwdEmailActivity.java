package yy.doctor.ui.activity;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import lib.network.model.NetworkResp;
import lib.ys.ui.other.NavBar;
import lib.ys.util.RegexUtil;
import lib.ys.util.TextUtil;
import lib.yy.network.Result;
import lib.yy.ui.activity.base.BaseActivity;
import yy.doctor.R;
import yy.doctor.dialog.HintDialogMain;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;
import yy.doctor.ui.activity.login.LoginActivity;
import yy.doctor.util.Util;
import yy.doctor.view.AutoCompleteEditText;

/**
 * 邮件找回密码
 *
 * @author CaiXiang
 * @since 2017/4/20
 */
public class ForgetPwdEmailActivity extends BaseActivity {

    private AutoCompleteEditText mEt;
    private HintDialogMain mDialog;
    private ImageView mIvCancel;
    private TextView mTvSendEmail;

    @Override
    public void initData() {
    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_forget_pwd_email;
    }

    @Override
    public void initNavBar(NavBar bar) {
        Util.addBackIcon(bar, R.string.forget_pwd, this);
    }

    @Override
    public void findViews() {

        mEt = findView(R.id.forget_pwd_et_email);
        mIvCancel = findView(R.id.forget_email_iv_cancel);
        mTvSendEmail = findView(R.id.forget_pwd_tv_send_email);
    }

    @Override
    public void setViews() {
        mTvSendEmail.setEnabled(false);

        buttonChanged(mEt, mIvCancel);

        setOnClickListener(R.id.forget_pwd_tv_send_email);
        setOnClickListener(R.id.forget_email_iv_cancel);
    }

    private void buttonChanged(AutoCompleteEditText et, ImageView iv) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (RegexUtil.isEmail(et.getText().toString().trim())) {
                    mTvSendEmail.setEnabled(true);
                }else {
                    mTvSendEmail.setEnabled(false);
                }
                
                if (TextUtil.isEmpty(et.getText().toString())) {
                    hideView(iv);
                }else {
                    showView(iv);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.forget_pwd_tv_send_email: {
                if (TextUtil.isEmpty(mEt.getText().toString().trim())) {
                    showToast(R.string.input_email);
                    return;
                }
                //检查邮箱
                if (!RegexUtil.isEmail(mEt.getText().toString().trim())) {
                    showToast(R.string.input_right_email);
                    return;
                }
                exeNetworkReq(NetFactory.forgetPwd(mEt.getText().toString().trim()));
            }
            break;
            case R.id.forget_email_iv_cancel: {
                mEt.setText("");
            }
            break;
        }
    }

    @Override
    public Object onNetworkResponse(int id, NetworkResp r) throws Exception {
        return JsonParser.error(r.getText());
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {

        Result r = (Result) result;
        if (r.isSucceed()) {
            if (mDialog == null) {
                mDialog = new HintDialogMain(ForgetPwdEmailActivity.this);
                mDialog.setHint(getString(R.string.forget_pwd_success));
                mDialog.addButton(getString(R.string.know), v -> {
                    startActivity(LoginActivity.class);
                    mDialog.dismiss();
                    finish();
                });
            }
            mDialog.show();
        } else {
            showToast(r.getError());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
