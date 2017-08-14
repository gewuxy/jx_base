package yy.doctor.ui.activity.meeting;

import java.util.ArrayList;

import lib.processor.annotation.AutoIntent;
import lib.processor.annotation.Extra;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.ui.other.NavBar;
import yy.doctor.R;
import yy.doctor.model.meet.exam.Answer;
import yy.doctor.network.NetFactory;
import yy.doctor.util.Util;

/**
 * 问卷结果界面
 *
 * @author : GuoXuan
 * @since : 2017/6/10
 */
@AutoIntent
public class QueEndActivity extends BaseResultActivity {

    @Extra(optional = true)
    String mPaperId;
    @Extra(optional = true)
    ArrayList<Answer> mAnswers;

    @Override
    public void initNavBar(NavBar bar) {
        Util.addBackIcon(bar, R.string.que, this);
    }

    @Override
    public void setViews() {
        super.setViews();

        refresh(RefreshWay.embed);
        exeNetworkReq(NetFactory.submitSur()
                .meetId(mMeetId)
                .moduleId(mModuleId)
                .paperId(mPaperId)
                .items(mAnswers)
                .builder());
    }

    @Override
    protected void successResult() {
        hideView(mTvResultMsg);
        hideView(mTvWelcome);
        mTvResult.setText(R.string.thank_join);
    }

    @Override
    protected void errorResult(String error) {
    }
}
