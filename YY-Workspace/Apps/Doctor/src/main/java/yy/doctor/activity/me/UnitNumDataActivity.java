package yy.doctor.activity.me;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.ys.util.TextUtil;
import lib.yy.activity.base.BaseSRListActivity;
import yy.doctor.Extra;
import yy.doctor.adapter.UnitNumDataAdapter;
import yy.doctor.model.unitnum.UnitNumDetailData;
import yy.doctor.model.unitnum.UnitNumDetailData.TUnitNumDetailData;
import yy.doctor.network.NetFactory;
import yy.doctor.util.CacheUtil;
import yy.doctor.util.Util;

/**
 * 单位号资料
 *
 * @author CaiXiang
 * @since 2017/5/3
 */
public class UnitNumDataActivity extends BaseSRListActivity<UnitNumDetailData, UnitNumDataAdapter> {

    private int mId;
    private String mType;
    private String mFilePath;

    private static String mFileName;
    private static String mFileUrl;
    private static String mFileType;
    private static long mFileSize;


    public static void nav(Context context, int id, String type) {
        Intent i = new Intent(context, UnitNumDataActivity.class)
                .putExtra(Extra.KData, id)
                .putExtra(Extra.KType, type);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initData() {
        mId = getIntent().getIntExtra(Extra.KData, 10);
        mType = getIntent().getStringExtra(Extra.KType);
    }

    @Override
    public void initNavBar(NavBar bar) {

        Util.addBackIcon(bar, "资料", this);
    }

    @Override
    public void setViews() {
        super.setViews();

        if (mType.equals(Extra.KUnitNumType)) {
            mFilePath = CacheUtil.getUnitNumCacheDir(String.valueOf(mId));
        } else if (mType.equals(Extra.KMeetingType)) {
            mFilePath = CacheUtil.getMeetingCacheDir(String.valueOf(mId));
        }
    }

    @Override
    public void getDataFromNet() {
        if (mType.equals(Extra.KUnitNumType)) {
            exeNetworkReq(NetFactory.unitNumData(mId, 1, 15));
        } else if (mType.equals(Extra.KMeetingType)) {
            exeNetworkReq(NetFactory.meetingData(String.valueOf(mId)));
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        super.onItemClick(v, position);

        UnitNumDetailData item = getItem(position);

        mFileSize = item.getLong(TUnitNumDetailData.fileSize);
        mFileName = item.getString(TUnitNumDetailData.materialName);
        if (TextUtil.isEmpty(mFileName)) {
            mFileName = item.getString(TUnitNumDetailData.name);
            mFileUrl = item.getString(TUnitNumDetailData.fileUrl);
            mFileType = item.getString(TUnitNumDetailData.fileType);
        } else {
            mFileName = item.getString(TUnitNumDetailData.materialName);
            mFileUrl = item.getString(TUnitNumDetailData.materialUrl);
            mFileType = item.getString(TUnitNumDetailData.materialType);
        }

        DownloadDataActivity.nav(this, mFilePath, mFileName,
                mFileUrl, mFileType, mFileSize);
    }

}
