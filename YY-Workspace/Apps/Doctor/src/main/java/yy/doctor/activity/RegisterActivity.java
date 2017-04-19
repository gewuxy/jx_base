package yy.doctor.activity;

import android.support.annotation.IntDef;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import lib.yy.activity.base.BaseFormActivity;
import yy.doctor.R;
import yy.doctor.model.form.Builder;
import yy.doctor.model.form.FormType;

public class RegisterActivity extends BaseFormActivity {

    private LinearLayout mFooterView;
    private EditText mKey;
    private TextView mGetKey;
    private TextView mRegister;

    @IntDef({
            RelatedId.email,
            RelatedId.name,
            RelatedId.password,
            RelatedId.password_marksure,
            RelatedId.location,
            RelatedId.hospital,
            RelatedId.key,
    })
    private @interface RelatedId {
        int email = 0;
        int name = 1;
        int password = 2;
        int password_marksure = 3;
        int location = 4;
        int hospital = 5;
        int key = 6;
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
        return inflate(R.layout.form_item_register);
    }

    @Override
    public void findViews() {
        super.findViews();

        mFooterView = findView(R.id.form_item_register);
        mKey = findView(R.id.register_et_key);
        mGetKey = findView(R.id.register_get_key);
        mRegister = findView(R.id.register);

        mFooterView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_et_key:
                break;
            case R.id.register_get_key:
                break;
            case R.id.register:
                break;
        }
    }
}
