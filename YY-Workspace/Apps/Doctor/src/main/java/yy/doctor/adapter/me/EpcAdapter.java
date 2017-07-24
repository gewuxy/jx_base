package yy.doctor.adapter.me;

import lib.ys.adapter.AdapterEx;
import lib.ys.network.image.renderer.CornerRenderer;
import yy.doctor.R;
import yy.doctor.adapter.VH.me.EpcVH;
import yy.doctor.model.me.Epc;
import yy.doctor.model.me.Epc.TEpc;

import static lib.ys.util.res.ResLoader.getString;

/**
 * 象城的adapter
 *
 * @author CaiXiang
 * @since 2017/4/26
 */
public class EpcAdapter extends AdapterEx<Epc, EpcVH> {

    @Override
    public int getConvertViewResId() {
        return R.layout.layout_epc_item;
    }

    @Override
    protected void refreshView(int position, EpcVH holder) {

        Epc item = getItem(position);

        holder.getTvName().setText(item.getString(TEpc.name));
        holder.getTvEpn().setText(item.getString(TEpc.price) + getString(R.string.epn));

        holder.getIv()
                .placeHolder(R.mipmap.ic_default_epc)
                .renderer(new CornerRenderer(fitDp(3)))
                .url(item.getString(TEpc.picture))
                .load();
    }

}