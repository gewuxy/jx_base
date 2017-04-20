package yy.doctor.activity.register;

import android.support.annotation.IntDef;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import lib.yy.activity.base.BaseFormActivity;
import yy.doctor.R;
import yy.doctor.model.form.Builder;
import yy.doctor.model.form.FormType;

/**
 * 注册界面
 *
 * 日期 : 2017/4/19
 * 创建人 : guoxuan
 */
public class RegisterActivity extends BaseFormActivity {

    private EditText mActivationCode;
    private TextView mGetActivationCode;
    private TextView mRegister;
    public static final int FromRegister = 1;

    @IntDef({
            RelatedId.email,
            RelatedId.name,
            RelatedId.password,
            RelatedId.password_marksure,
            RelatedId.location,
            RelatedId.hospital,
            RelatedId.ActivationCode,
    })
    private @interface RelatedId {
        int email = 0;
        int name = 1;
        int password = 2;
        int password_marksure = 3;
        int location = 4;
        int hospital = 5;
        int ActivationCode = 6;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    public void initData() {
        super.initData();

        addItem(new Builder(FormType.divider_large).build());

        addItem(new Builder(FormType.et_register)
                .related(RelatedId.email)
                .hint(R.string.register_email)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et_register)
                .related(RelatedId.name)
                .hint(R.string.register_name)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et_register)
                .related(RelatedId.password)
                .hint(R.string.register_password)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et_register)
                .related(RelatedId.password_marksure)
                .hint(R.string.register_password_marksure)
                .build());

        addItem(new Builder(FormType.divider_large).build());

        addItem(new Builder(FormType.et_register)
                .related(RelatedId.location)
                .hint(R.string.register_location)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et_register)
                .related(RelatedId.hospital)
                .hint(R.string.register_hospital)
                .drawable(R.mipmap.ic_more_hospital)
                .build());

    }

    @Override
    protected View createFooterView() {
        return inflate(R.layout.layout_register_footer);
    }

    @Override
    public void findViews() {
        super.findViews();

        mActivationCode = findView(R.id.register_et_activation_code);
        mGetActivationCode = findView(R.id.register_get_activation_code);
        mRegister = findView(R.id.register);

        mGetActivationCode.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_et_activation_code:
                break;
            case R.id.register_get_activation_code:
                startActivity(ActivationCodeExplainActivity.class);
                break;
            case R.id.register:
                startActivityForResult(HospitalActivity.class, FromRegister);
                break;
        }
    }
}
