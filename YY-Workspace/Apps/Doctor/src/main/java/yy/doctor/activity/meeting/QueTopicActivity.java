package yy.doctor.activity.meeting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import lib.network.model.NetworkResp;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.ui.decor.DecorViewEx.ViewState;
import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.yy.network.Result;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.dialog.CommonTwoDialog;
import yy.doctor.dialog.CommonTwoDialog.OnLayoutListener;
import yy.doctor.model.meet.exam.Intro;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;

/**
 * 问卷
 *
 * @author : GuoXuan
 * @since : 2017/4/27
 */
public class QueTopicActivity extends BaseTopicActivity {

    private CommonTwoDialog mSubDialog;
    private TextView mBarRight;

    public static void nav(Context context, String meetId, String moduleId) {
        Intent i = new Intent(context, QueTopicActivity.class)
                .putExtra(Extra.KMeetId, meetId)
                .putExtra(Extra.KModuleId, moduleId);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initNavBar(NavBar bar) {
        super.initNavBar(bar);

        mTvLeft.setText("问卷");
        mBarRight = bar.addTextViewRight("提交", v -> {
            if (mAllTopics != null && mAllTopics.size() > 0) {
                trySubmit(mAllTopics.size() - mCount);
            }
        });
    }

    @Override
    public void setViews() {
        super.setViews();

        refresh(RefreshWay.embed);
        exeNetworkReq(NetFactory.toSurvey(mMeetId, mModuleId));
    }

    @Override
    public Object onNetworkResponse(int id, NetworkResp r) throws Exception {
        return JsonParser.ev(r.getText(), Intro.class);
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {
        Result<Intro> r = (Result<Intro>) result;
        if (r.isSucceed()) {
            setViewState(ViewState.normal);
            mIntro = r.getData();

            if (mIntro != null) {
                initFrag();
                invalidate();
            }

            initFirstGv();
        } else {
            setViewState(ViewState.error);
            showToast(r.getError());
        }
    }

    @Override
    protected void trySubmit(int noFinish) {
        //考试时间未完
        if (mSubDialog == null) {
            mSubDialog = new CommonTwoDialog(QueTopicActivity.this);
        }
        if (noFinish > 0) {
            //还有没作答
            mSubDialog.setTvMainHint("还有" + noFinish + "题未完成")
                    .setTvSecondaryHint("是否确认提交问卷?");
        } else {
            //全部作答完了
            mSubDialog.setTvMainHint("确定提交问卷?")
                    .hideSecond();
        }
        mSubDialog.mTvLeft(getString(R.string.exam_submit_sure))
                .mTvRight(getString(R.string.exam_continue))
                .setLayoutListener(new OnLayoutListener() {
                    @Override
                    public void leftClick(View v) {
                        submit();
                    }

                    @Override
                    public void rightClick(View v) {

                    }
                });
        mSubDialog.show();
    }

    @Override
    protected void submit() {
        Intent i = new Intent(QueTopicActivity.this, QueEndActivity.class)
                .putExtra(Extra.KMeetId, mMeetId)
                .putExtra(Extra.KModuleId, mModuleId)
                .putExtra(Extra.KPaperId, mPaperId)
                .putExtra(Extra.KData, getAnswer(mAllTopics));
        LaunchUtil.startActivity(QueTopicActivity.this, i);
        finish();
    }

    @Override
    protected void topicCaseVisibility(boolean showState) {
        super.topicCaseVisibility(showState);
        if (getTopicCaseShow()) {
            mTvLeft.setText("题目");
            goneView(mBarRight);
        } else {
            mTvLeft.setText("问卷");
            showView(mBarRight);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSubDialog != null) {
            if (mSubDialog.isShowing()) {
                mSubDialog.dismiss();
            }
            mSubDialog = null;
        }
    }

}
