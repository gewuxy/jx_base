package yy.doctor.activity.home;

import android.content.Context;
import android.content.Intent;

import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.yy.activity.base.BaseWebViewActivity;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.dialog.ShareDialog;
import yy.doctor.util.Util;

/**
 * Created by XPS on 2017/5/16.
 */

public class BannerActivity extends BaseWebViewActivity {

    private String mUrl;
    private String mTitle;

    public static void nav(Context context, String url, String title) {
        Intent i = new Intent(context, BannerActivity.class);
        i.putExtra(Extra.KData, url);
        i.putExtra(Extra.KName, title);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initData() {
        mUrl = getIntent().getStringExtra(Extra.KData);
        mTitle = getIntent().getStringExtra(Extra.KName);
    }

    @Override
    public void initNavBar(NavBar bar) {

        Util.addBackIcon(bar, "详情", this);
        bar.addViewRight(R.mipmap.nav_bar_ic_share, v -> {
            ShareDialog shareDialog = new ShareDialog(BannerActivity.this, mUrl, mTitle);
            shareDialog.show();
        });
    }

    @Override
    protected void onLoadStart() {
        loadUrl(mUrl);
    }

}