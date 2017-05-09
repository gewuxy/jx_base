package yy.doctor.activity.me;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import lib.ys.ui.other.NavBar;
import lib.ys.util.TextUtil;
import lib.yy.activity.base.BaseActivity;
import yy.doctor.R;
import yy.doctor.util.Util;

/**
 * 修改密码
 *
 * @author CaiXiang
 * @since 2017/4/20
 */
public class ChangePwdActivity extends BaseActivity {

    private EditText mEtOldPwd;
    private EditText mEtNewPwd;
    private EditText mEtConfirmPwd;

    @Override
    public void initData() {

    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    public void initNavBar(NavBar bar) {
        Util.addBackIcon(bar, "修改密码", this);
    }

    @Override
    public void findViews() {

        mEtOldPwd = findView(R.id.change_pwd_et_old);
        mEtNewPwd = findView(R.id.change_pwd_et_new);
        mEtConfirmPwd = findView(R.id.change_pwd_et_confirm);

    }

    @Override
    public void setViews() {

        setOnClickListener(R.id.change_pwd_tv);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id) {
            case R.id.change_pwd_tv: {

                String oldPwd = mEtOldPwd.getText().toString();
                String newPwd = mEtNewPwd.getText().toString();
                String confirmPwd = mEtConfirmPwd.getText().toString();

                if (TextUtil.isEmpty(oldPwd)) {
                    showToast("请输入旧密码");
                }

                if (TextUtil.isEmpty(newPwd)) {
                    showToast("请输入新密码");
                }

                if (TextUtil.isEmpty(confirmPwd)) {
                    showToast("请输入确认密码");
                }

                if (!newPwd.equals(confirmPwd)) {
                    showToast("确认密码与新密码不一致！");
                }


            }
            break;
        }

    }
}
