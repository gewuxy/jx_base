package yy.doctor.ui.activity.me.unitnum;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import router.annotation.AutoIntent;
import router.annotation.Extra;
import lib.ys.YSLog;
import lib.ys.action.IntentAction;
import lib.ys.ui.other.NavBar;
import lib.yy.ui.activity.base.BaseActivity;
import yy.doctor.R;
import yy.doctor.ui.activity.data.PDFActivityIntent;
import yy.doctor.ui.frag.data.BaseDataUnitsFrag.DataType;
import yy.doctor.util.Util;

import static yy.doctor.Constants.FileSuffix.KPdf;
import static yy.doctor.Constants.FileSuffix.KPpt;
import static yy.doctor.Constants.FileSuffix.KPptX;

/**
 * 打开下载资料
 *
 * @author CaiXiang
 * @since 2017/5/17
 */
@AutoIntent
public class LaunchDownloadDataActivity extends BaseActivity {

    private ImageView mIv;
    private TextView mTvName;
    private TextView mTvSize;

    @Extra(optional = true)
    String mFilePath;
    @Extra(optional = true)
    String mFileName;
    @Extra(optional = true)
    String mFileSuffix;
    @Extra(optional = true)
    String mSize;
    @Extra(optional = true)
    String mDataFileId;
    @Extra(optional = true)
    String mFileNameEncryption;
    @Extra(optional = true)
    @DataType
    int mDataType;

    //private String mFileNameHashCode;

    @Override
    public void initData() {
        YSLog.d(TAG, "FileNameEncryption = " + mFileNameEncryption);
        // 恢复文件名
//        StringBuffer sb = new StringBuffer();
//        char[] chars = mFileNameEncryption.toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            char c = (char) (chars[i] - 5);
//            sb.append(c);
//        }
//        mFileNameHashCode = sb.toString();
//        YSLog.d(TAG, "FileNameHashCode = " + mFileNameHashCode);
    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_launch_download_data;
    }

    @Override
    public void initNavBar(NavBar bar) {
        Util.addBackIcon(bar, R.string.file_data, this);
    }

    @Override
    public void findViews() {

        mIv = findView(R.id.launch_download_data_ic);
        mTvName = findView(R.id.launch_download_data_tv_name);
        mTvSize = findView(R.id.launch_download_data_tv_size);
    }

    @Override
    public void setViews() {

        if (mFileSuffix.equals(KPdf)) {
            mIv.setImageResource(R.mipmap.open_data_ic_pdf);
        } else if (mFileSuffix.equals(KPpt) || mFileSuffix.equals(KPptX)) {
            mIv.setImageResource(R.mipmap.open_data_ic_ppt);
        } else {
            mIv.setImageResource(R.mipmap.open_data_ic_word);
        }
        mTvName.setText(mFileName);
        mTvSize.setText(mSize);

        setOnClickListener(R.id.open_download_data_tv_btn);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.open_download_data_tv_btn) {

            Intent intent = null;
            try {
                if (mFileSuffix.equals(KPdf)) {
                    PDFActivityIntent.create(
                            mFilePath,
                            mFileNameEncryption,
                            mFileName,
                            mDataFileId,
                            mDataType
                    )
                            .start(this);
                } else if (mFileSuffix.equals(KPpt) || mFileSuffix.equals(KPptX)) {
                    IntentAction.ppt()
                            .filePath(mFilePath + mFileNameEncryption)
                            .alert("没有打开PPT类应用")
                            .launch();
                } else {
                    IntentAction.word()
                            .filePath(mFilePath + mFileNameEncryption)
                            .alert("没有打开Word类应用")
                            .launch();
                }
            } catch (Exception e) {
                YSLog.d(TAG, " error msg " + e.getMessage());
                showToast(R.string.can_not_find_relevant_software);
            }
        }
    }

}
