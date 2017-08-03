package yy.doctor.adapter.data;

import lib.ys.adapter.AdapterEx;
import yy.doctor.R;
import yy.doctor.adapter.VH.data.DataVH;
import yy.doctor.model.data.DrugCategoryData;
import yy.doctor.model.data.DrugCategoryData.TCategoryData;

/**
 * @author CaiXiang
 * @since 2017/7/14
 */

public class DrugListAdapter extends AdapterEx<DrugCategoryData, DataVH> {

    @Override
    public int getConvertViewResId() {
        return R.layout.layout_data_item;
    }

    @Override
    protected void refreshView(int position, DataVH holder) {
        if (position != 0) {
            goneView(holder.getDivider());
        }
        goneView(holder.getTvDetail());

        DrugCategoryData item = getItem(position);
        item.getString(TCategoryData.isFolder);
        holder.getTvName().setText(getItem(position).getString(TCategoryData.isFolder));
    }
}
