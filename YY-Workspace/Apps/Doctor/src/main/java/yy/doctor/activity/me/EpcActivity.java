package yy.doctor.activity.me;

import android.view.View;

import lib.ys.ui.other.NavBar;
import lib.yy.activity.base.BaseSRListActivity;
import yy.doctor.R;
import yy.doctor.adapter.EpcAdapter;
import yy.doctor.model.me.Epc;
import yy.doctor.model.me.Epc.TEpc;
import yy.doctor.network.NetFactory;

/**
 * 象城
 *
 * @author CaiXiang
 * @since 2017/4/26
 */
public class EpcActivity extends BaseSRListActivity<Epc, EpcAdapter> {

    @Override
    public void initData() {
    }

    @Override
    public void initNavBar(NavBar bar) {
        bar.addBackIcon(R.mipmap.nav_bar_ic_back, "象城", this);
        bar.addTextViewRight("订单", v -> startActivity(OrderActivity.class));
    }

    @Override
    public void onItemClick(View v, int position) {
        Epc item = getItem(position);
        EpcDetailActivity.nav(EpcActivity.this, item.getInt(TEpc.id), item.getString(TEpc.name));
    }

    @Override
    public void getDataFromNet() {
        exeNetworkReq(NetFactory.epc());
    }
}
