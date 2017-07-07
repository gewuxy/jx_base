package yy.doctor.util;

import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import lib.ys.fitter.DpFitter;
import lib.ys.fitter.LayoutFitter;
import lib.ys.util.TextUtil;
import lib.ys.util.TimeUtil;
import lib.ys.util.res.ResLoader;
import lib.ys.util.view.LayoutUtil;
import lib.ys.util.view.ViewUtil;
import yy.doctor.R;
import yy.doctor.model.home.RecUnitNum.Attention;
import yy.doctor.model.meet.Meeting.MeetState;
import yy.doctor.model.unitnum.FileData;
import yy.doctor.model.unitnum.FileData.TFileData;
import yy.doctor.ui.activity.me.DownloadDataActivity;

/**
 * @auther yuansui
 * @since 2017/6/8
 */

public class UISetter {

    private static final int KMeetIconSizeDp = 11;

    /**
     * 根据会议状态
     *
     * @param state
     * @param tv
     */
    public static void setMeetState(@MeetState int state, TextView tv) {
        String text = null;
        int color = 0;
        Drawable d = null;

        switch (state) {
            case MeetState.not_started: {
                text = "未开始";
                color = ResLoader.getColor(R.color.text_01b557);
                d = ResLoader.getDrawable(R.mipmap.meeting_ic_not_started);
            }
            break;
            case MeetState.under_way: {
                text = "进行中";
                color = ResLoader.getColor(R.color.text_e6600e);
                d = ResLoader.getDrawable(R.mipmap.meeting_ic_under_way);
            }
            break;
            case MeetState.retrospect: {
                text = "精彩回顾";
                color = ResLoader.getColor(R.color.text_5cb0de);
                d = ResLoader.getDrawable(R.mipmap.meeting_ic_retrospect);
            }
            break;
        }

        if (d != null) {
            d.setBounds(0, 0, DpFitter.dp(KMeetIconSizeDp), DpFitter.dp(KMeetIconSizeDp));
        }

        tv.setText(text);
        tv.setTextColor(color);
        tv.setCompoundDrawables(d, null, null, null);
    }

    public static void setDateDuration(TextView tvDate, TextView tvDuration, long startTime, long endTime) {
        tvDate.setText(TimeUtil.formatMilli(startTime, "MM月dd日 HH:mm"));
        tvDuration.setText("时长:" + Time.milliFormat(endTime - startTime));
    }

    public static void setFileData(LinearLayout layout, List<FileData> listFile, int id) {
        String fileName;
        String fileUrl;
        String fileType;
        for (int i = 0; i < listFile.size(); i++) {
            FileData fileItem = listFile.get(i);

            long fileSize = fileItem.getLong(TFileData.fileSize);
            fileName = fileItem.getString(TFileData.materialName);
            if (TextUtil.isEmpty(fileName)) {
                fileName = fileItem.getString(TFileData.name);
                fileUrl = fileItem.getString(TFileData.fileUrl);
                fileType = fileItem.getString(TFileData.fileType);
            } else {
                fileUrl = fileItem.getString(TFileData.materialUrl);
                fileType = fileItem.getString(TFileData.materialType);
            }

            String finalFileName = fileName;
            String finalFileUrl = fileUrl;
            String finalFileType = fileType;
            addFileItem(layout, fileName, v -> DownloadDataActivity.nav(v.getContext(),
                    CacheUtil.getUnitNumCacheDir(String.valueOf(id)),
                    finalFileName, finalFileUrl, finalFileType, fileSize));
        }

    }

    /**
     * 添加文件item
     *
     * @param text
     * @param l
     */
    public static void addFileItem(LinearLayout layout, CharSequence text, OnClickListener l) {

        View v = LayoutInflater.from(layout.getContext()).inflate(R.layout.layout_unit_num_detail_file_item, null);
        TextView tv = (TextView) v.findViewById(R.id.unit_num_detail_file_item_tv_name);
        tv.setText(text);
        v.setOnClickListener(l);

        LayoutFitter.fit(v);
        layout.addView(v, LayoutUtil.getLinearParams(LayoutUtil.MATCH_PARENT, LayoutUtil.WRAP_CONTENT));
    }

    /**
     * 关注按钮的状态改变
     *
     * @param tv
     * @param attention
     */
    public static void setAttention(TextView tv, int attention) {
        if (attention == Attention.yes) {
            tv.setText(R.string.already_attention);
            tv.setSelected(true);
            tv.setClickable(false);
        } else {
            tv.setText(R.string.attention);
            tv.setSelected(false);
            tv.setClickable(true);
        }
    }

    /**
     * 没有文字就隐藏TextView
     * 有文字内容就设置给TextView
     *
     * @param text
     * @param textView
     */
    public static void viewVisibility(String text, TextView textView) {
        if (TextUtil.isEmpty(text)) {
            ViewUtil.goneView(textView);
        } else {
            textView.setText(text);
        }
    }

    /**
     * 设置密码的输入范围格式
     *
     * @param et
     */
    public static void setPwdRange(EditText et) {

        et.setKeyListener(new NumberKeyListener() {

            @Override
            protected char[] getAcceptedChars() {
                String chars = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM-×÷＝%√°′″{}()[].|*/#~,:;?\"‖&*@\\^,$–…'=+!><.-—_";
                return chars.toCharArray();
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
        });
    }

}
