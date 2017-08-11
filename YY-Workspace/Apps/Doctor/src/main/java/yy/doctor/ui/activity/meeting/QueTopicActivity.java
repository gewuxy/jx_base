package yy.doctor.ui.activity.meeting;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import lib.network.model.NetworkError;
import lib.network.model.NetworkResp;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.ui.decor.DecorViewEx.ViewState;
import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.yy.network.Result;
import lib.yy.notify.Notifier.NotifyType;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.model.meet.exam.Intro;
import yy.doctor.model.meet.exam.Paper.TPaper;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;
import yy.doctor.popup.TopicPopup;
import yy.doctor.sp.SpApp;

/**
 * 问卷
 *
 * @author : GuoXuan
 * @since : 2017/4/27
 */
public class QueTopicActivity extends BaseTopicActivity {

    private TopicPopup mTopicPopup;

    public static void nav(Context context, String meetId, String moduleId) {
        Intent i = new Intent(context, QueTopicActivity.class)
                .putExtra(Extra.KMeetId, meetId)
                .putExtra(Extra.KModuleId, moduleId);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initNavBar(NavBar bar) {
        super.initNavBar(bar);

        bar.addTextViewMid(R.string.que);

        bar.addTextViewRight(R.string.submit, v -> {
            if (mAllTopics != null && mAllTopics.size() > 0) {
                toSubmit(mAllTopics.size() - mCount);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        notify(NotifyType.study_start);
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

            // 第一次进入考试时提示
            if (SpApp.inst().firstEnterQue()) {
                runOnUIThread(() -> {
                    mTopicPopup = new TopicPopup(QueTopicActivity.this);
                    mTopicPopup.setCheck(R.mipmap.que_popup_check);
                    mTopicPopup.setSlide(R.mipmap.que_popup_slide);
                    mTopicPopup.showAtLocation(getNavBar(), Gravity.CENTER, 0, 0);
                    SpApp.inst().saveEnterQue();
                }, 300);
            }
            initFirstGv();
        } else {
            setViewState(ViewState.error);
            showToast(r.getMessage());
        }
    }

    @Override
    public void onNetworkError(int id, NetworkError error) {
        super.onNetworkError(id, error);

        setViewState(ViewState.error);
    }

    @Override
    protected String submitHint(int noFinish) {
        if (noFinish > 0) {
            //还有没作答
            return String.format(getString(R.string.que_submit_hint_no_finish), noFinish);
        } else {
            //全部作答完了
            return getString(R.string.que_submit_hint_finish);
        }
    }

    @Override
    protected void submit() {
        // FIXME: 
        Intent i = new Intent(QueTopicActivity.this, QueEndActivity.class)
                .putExtra(Extra.KMeetId, mMeetId)
                .putExtra(Extra.KModuleId, mModuleId)
                .putExtra(Extra.KPaperId, mPaper.getString(TPaper.id))
                .putExtra(Extra.KData, mAnswers);
        LaunchUtil.startActivity(QueTopicActivity.this, i);
        finish();
    }

}
