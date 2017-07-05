package yy.doctor.ui.activity.me.unitnum;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.ys.util.TextUtil;
import lib.yy.ui.activity.base.BaseSRListActivity;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.ui.activity.me.DownloadDataActivity;
import yy.doctor.adapter.FileDataAdapter;
import yy.doctor.model.unitnum.FileData;
import yy.doctor.model.unitnum.FileData.TFileData;
import yy.doctor.network.NetFactory;
import yy.doctor.util.CacheUtil;
import yy.doctor.util.Util;

/**
 * 资料列表
 *
 * @author CaiXiang
 * @since 2017/5/3
 */
public class FileDataActivity extends BaseSRListActivity<FileData, FileDataAdapter> {

    private String mId;
    private String mType;
    private String mFilePath;

    private static String mFileName;
    private static String mFileUrl;
    private static String mFileType;
    private static long mFileSize;

    public static void nav(Context context, String id, String type) {
        Intent i = new Intent(context, FileDataActivity.class)
                .putExtra(Extra.KData, id)
                .putExtra(Extra.KType, type);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initData() {
        mId = getIntent().getStringExtra(Extra.KData);
        mType = getIntent().getStringExtra(Extra.KType);
    }

    @Override
    public void initNavBar(NavBar bar) {

        Util.addBackIcon(bar, R.string.file_data, this);
    }

    @Override
    public void setViews() {
        super.setViews();

        if (mType.equals(Extra.KUnitNumType)) {
            mFilePath = CacheUtil.getUnitNumCacheDir(mId);
        } else if (mType.equals(Extra.KMeetingType)) {
            mFilePath = CacheUtil.getMeetingCacheDir(mId);
        }
    }

    @Override
    public void getDataFromNet() {
        if (mType.equals(Extra.KUnitNumType)) {
            exeNetworkReq(NetFactory.unitNumData(mId, getOffset(), getLimit()));
        } else if (mType.equals(Extra.KMeetingType)) {
            exeNetworkReq(NetFactory.meetingData(mId, getOffset(), getLimit()));
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        super.onItemClick(v, position);

        FileData item = getItem(position);

        mFileSize = item.getLong(TFileData.fileSize);
        mFileName = item.getString(TFileData.materialName);
        if (TextUtil.isEmpty(mFileName)) {
            mFileName = item.getString(TFileData.name);
            mFileUrl = item.getString(TFileData.fileUrl);
            mFileType = item.getString(TFileData.fileType);
        } else {
            mFileName = item.getString(TFileData.materialName);
            mFileUrl = item.getString(TFileData.materialUrl);
            mFileType = item.getString(TFileData.materialType);
        }

        DownloadDataActivity.nav(this, mFilePath, mFileName,
                mFileUrl, mFileType, mFileSize);
    }

}