package yy.doctor.activity.meeting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import lib.network.model.NetworkResp;
import lib.network.model.err.NetError;
import lib.ys.LogMgr;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.ui.decor.DecorViewEx.ViewState;
import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.ys.util.view.ViewUtil;
import lib.yy.activity.base.BaseListActivity;
import lib.yy.network.ListResult;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.adapter.CommentAdapter;
import yy.doctor.model.meet.Comment;
import yy.doctor.model.meet.Comment.TComment;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;
import yy.doctor.util.Util;

/**
 * 会议评论界面
 *
 * @author : GuoXuan
 * @since : 2017/5/2
 */

public class MeetingCommentActivity extends BaseListActivity<String, CommentAdapter> {
    // TODO: 2017/6/6 未完
    private TextView mTvSend;
    private EditText mEtSend;
    private static final int KHistories = 0;
    private static final int KSend = 1;
    private String mMeetId;

    public static void nav(Context context, String meetId) {
        Intent i = new Intent(context, MeetingCommentActivity.class)
                .putExtra(Extra.KMeetId, meetId);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initData() {
        mMeetId = getIntent().getStringExtra(Extra.KMeetId);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_meeting_comment;
    }

    @Override
    public View createHeaderView() {
        return ViewUtil.inflateSpaceViewDp(18);
    }

    @Override
    public void initNavBar(NavBar bar) {
        Util.addBackIcon(bar, "评论", this);
    }

    @Override
    public void findViews() {
        super.findViews();
        mTvSend = findView(R.id.meeting_comment_tv_send);
        mEtSend = findView(R.id.meeting_comment_et_send);
    }

    @Override
    public void setViews() {
        super.setViews();
        mTvSend.setOnClickListener(this);
        refresh(RefreshWay.embed);
        exeNetworkReq(KHistories, NetFactory.histories(mMeetId));
    }

    @Override
    public Object onNetworkResponse(int id, NetworkResp r) throws Exception {
        if (id == KHistories) {
            return JsonParser.evs(r.getText(), Comment.class);
        }
        return JsonParser.error(r.getText());
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {
        setViewState(ViewState.normal);
        if (id == KHistories) {
            ListResult<Comment> r = (ListResult<Comment>) result;
            List<Comment> comments = r.getData();
            if (comments != null) {
                for (Comment comment : comments) {
                    LogMgr.d(TAG, comment.getString(TComment.message));
                }
            }
        } else if (id == KSend) {
            showToast("发送成功");
        }
    }

    @Override
    public void onNetworkError(int id, NetError error) {
        super.onNetworkError(id, error);
        setViewState(ViewState.error);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.meeting_comment_tv_send:
                String meesage = mEtSend.getText().toString().trim();
                refresh(RefreshWay.dialog);
                exeNetworkReq(KSend, NetFactory.send(mMeetId, meesage, "0"));
                break;
            default:
                break;
        }
    }
}
