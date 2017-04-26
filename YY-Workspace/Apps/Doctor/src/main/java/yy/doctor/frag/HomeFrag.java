package yy.doctor.frag;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;

import lib.ys.LogMgr;
import lib.ys.ex.NavBar;
import lib.ys.model.Screen;
import lib.yy.frag.base.BaseFrag;
import yy.doctor.R;

/**
 * @author Administrator   extends BaseSRListFrag<Home>
 * @since 2017/4/5
 */
public class HomeFrag extends BaseFrag {

    private EditText mEtSearch;

    @Override
    public void initData() {

    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.frag_home;
    }

    @Override
    public void initNavBar(NavBar bar) {

        final View v = inflate(R.layout.layout_home_nav_bar);
        //bar.addViewLeft(v, null);
        bar.addViewRight(v, null);

        addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LogMgr.d("www", "onGlobalLayout: v = " + v.getWidth());
                LogMgr.d("www", "onGlobalLayout: w = " + Screen.getWidth());
                LogMgr.d("www", "onGlobalLayout: h = " + Screen.getHeight());
            }
        });

        bar.addViewRight(R.mipmap.nav_bar_ic_notice, new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void findViews() {


    }

    @Override
    public void setViewsValue() {

    }

    /*@Override
    public MultiRecyclerAdapterEx<Home, ? extends RecyclerViewHolderEx> createAdapter() {
        return new HomeAdapter();
    }

    @Override
    public void getDataFromNet() {
        exeNetworkRequest(0, NetFactory.home());
    }

    @Override
    public IListResponse<Home> parseNetworkResponse(int id, String text) throws JSONException {
        return JsonParser.home(text);
    }*/

}
