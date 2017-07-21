package yy.doctor.model.form;

import android.support.annotation.NonNull;

import lib.yy.adapter.VH.FormVH;
import lib.yy.model.form.BaseForm;
import yy.doctor.R;

/**
 * @author CaiXiang
 * @since 2017/4/6
 */
public class ToggleButtonForm extends BaseForm {

    @NonNull
    @Override
    public int getType() {
        return FormType.toggle_button;
    }

    @Override
    protected void refresh(FormVH holder) {
        super.refresh(holder);

        holder.getTbSwitcher().setToggleState(true);
    }

    @Override
    public boolean check() {
        return false;
    }

    @Override
    public int getContentViewResId() {
        return R.layout.form_toggle_button;
    }

}
