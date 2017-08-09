package yy.doctor.model.form.edit;

import android.os.Build.VERSION_CODES;
import android.support.annotation.CallSuper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import lib.ys.util.DeviceUtil;
import lib.ys.util.TextUtil;
import lib.ys.util.view.ViewUtil;
import lib.yy.adapter.VH.FormVH;
import lib.yy.model.form.BaseForm;
import yy.doctor.Constants;
import yy.doctor.R;
import yy.doctor.util.Util;

/**
 * @author CaiXiang
 * @since 2017/4/17
 */
public class EditForm extends BaseForm implements TextWatcher {

    @Override
    public boolean check() {
        return checkInput();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.form_edit;
    }

    @Override
    protected void init(FormVH holder) {
        super.init(holder);

        EditText et = holder.getEt();
        et.addTextChangedListener(this);
        // 设置
        View clean = getHolder().getIvClean();
        if (clean != null) {
            setOnClickListener(clean);

            et.setOnFocusChangeListener((v, hasFocus) -> {
                // iv是否显示
                if (hasFocus && TextUtil.isNotEmpty(Util.getEtString(et))) {
                    ViewUtil.showView(clean);
                } else {
                    ViewUtil.goneView(clean);
                }
            });
        }

        // 5.0以上的版本设置水波纹点击背景
        if (DeviceUtil.getSDKVersion() >= VERSION_CODES.LOLLIPOP) {
            if (holder.getIv() != null) {
                holder.getIv().setBackgroundResource(R.drawable.item_selector_unbound);
            }
        }

        if (getLimit() != Constants.KInvalidValue) {
            ViewUtil.limitInputCount(holder.getEt(), getLimit());
        }
    }

    @Override
    protected void refresh(FormVH holder) {
        super.refresh(holder);

        int d = getDrawable();
        ImageView iv = holder.getIv();
        if (iv != null) {
            if (d != Constants.KInvalidValue) {
                iv.setOnClickListener(this);
            }
            setIvIfValid(iv, d);
        }

        EditText et = holder.getEt();
        et.setEnabled(isEnabled());
        et.setText(getText());
        et.setHint(getHint());
    }

    @CallSuper
    @Override
    protected boolean onViewClick(View v) {
        int id = v.getId();
        if (id == R.id.form_iv_clean) {
            getHolder().getEt().setText("");
            return true;
        }
        return super.onViewClick(v);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        save(s.toString(), s.toString());

        View clean = getHolder().getIvClean();
        if (clean != null ) {
            if (TextUtil.isNotEmpty(s)) {
                ViewUtil.showView(clean);
            } else {
                ViewUtil.goneView(clean);
            }
        }
    }
}
