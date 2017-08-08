package yy.doctor.adapter.data;

import lib.ys.adapter.AdapterEx;
import yy.doctor.R;
import yy.doctor.adapter.VH.data.DataVH;
import yy.doctor.model.data.DataUnit;
import yy.doctor.model.data.DataUnit.TDataUnit;

/**
 * @author CaiXiang
 * @since 2017/7/14
 */

public class DataUnitAdapter extends AdapterEx<DataUnit, DataVH> {

    private static final String KSymbol = "K";

    @Override
    public int getConvertViewResId() {
        return R.layout.layout_data_item;
    }

    @Override
    protected void refreshView(int position, DataVH holder) {
        if (position != 0) {
            goneView(holder.getDivider());
        } else {
            showView(holder.getDivider());
        }

        DataUnit item = getItem(position);
        holder.getTvName().setText(getItem(position).getString(TDataUnit.title));

        if (item.getBoolean(TDataUnit.isFile)) {
            goneView(holder.getTvDetail());
        } else {
            long size = item.getLong(TDataUnit.fileSize, -1);
            if (size != -1) {
                showView(holder.getTvDetail());
                holder.getTvDetail().setText(size + KSymbol);
            } else {
                goneView(holder.getTvDetail());
            }
        }

        setOnViewClickListener(position, holder.getDataItemLayout());
    }
}