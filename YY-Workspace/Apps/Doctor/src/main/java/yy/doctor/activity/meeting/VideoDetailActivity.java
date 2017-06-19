package yy.doctor.activity.meeting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import lib.network.model.NetworkResp;
import lib.network.model.err.NetError;
import lib.network.model.interfaces.IListResult;
import lib.ys.adapter.MultiAdapterEx.OnAdapterClickListener;
import lib.ys.ui.decor.DecorViewEx.ViewState;
import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.yy.activity.base.BaseSRListActivity;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.adapter.meeting.VideoDetailAdapter;
import yy.doctor.model.meet.video.Detail;
import yy.doctor.model.meet.video.Detail.TDetail;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;
import yy.doctor.util.Util;

/**
 * 视频列表界面
 *
 * @author : GuoXuan
 * @since : 2017/5/24
 */
public class VideoDetailActivity extends BaseSRListActivity<Detail, VideoDetailAdapter> implements OnAdapterClickListener {

    private String mPreId;
    private List<Detail> mDetails;
    private TextView mBarTvRight; // 右边的文本
    private long mStudyTime; // 学习时间
    private int mClickPosition; // 点击第几个

    public static void nav(Context context, String preId) {
        Intent i = new Intent(context, VideoDetailActivity.class)
                .putExtra(Extra.KData, preId);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initData() {
        mPreId = getIntent().getStringExtra(Extra.KData);
    }

    @Override
    public void initNavBar(NavBar bar) {
        Util.addBackIcon(bar, getString(R.string.video), this);
        mBarTvRight = bar.addTextViewRight(getString(R.string.video_studied) + "0", null);
    }

    @Override
    public void setViews() {
        super.setViews();
        setOnAdapterClickListener(this);
    }

    @Override
    public void getDataFromNet() {
        exeNetworkReq(NetFactory.video(mPreId));
    }

    @Override
    public Object onNetworkResponse(int id, NetworkResp r) throws Exception {
        return JsonParser.evs(r.getText(), Detail.class);
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {
        super.onNetworkSuccess(id, result);
        setViewState(ViewState.normal);
        IListResult<Detail> r = (IListResult<Detail>) result;
        if (r.isSucceed()) {
            mDetails = r.getData();
            if (mDetails == null || mDetails.size() <= 0) {
                return;
            }
            addAll(mDetails);

            mStudyTime = 0;
            for (Detail detail : mDetails) {
                mStudyTime += detail.getLong(TDetail.duration);
            }
            if (mStudyTime > 0) {
                mBarTvRight.setText(getString(R.string.video_studied) + VideoDetailAdapter.format(mStudyTime));
            }
        }
    }

    @Override
    public void onNetworkError(int id, NetError error) {
        super.onNetworkError(id, error);
        setViewState(ViewState.error);
    }

    @Override
    public void onAdapterClick(int position, View v) {
        VideoActivity.nav(VideoDetailActivity.this,
                mDetails.get(position));
        mClickPosition = position;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            long duration = data.getLongExtra(Extra.KData, 0);
            mStudyTime += duration;
            if (mStudyTime > 0) {
                mBarTvRight.setText(getString(R.string.video_studied) + VideoDetailAdapter.format(mStudyTime));
            }
            getItem(mClickPosition).put(TDetail.duration, duration + getItem(mClickPosition).getLong(TDetail.duration));
            invalidate();
        }
    }
}
