package yy.doctor.activity.meeting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import lib.ys.ui.dialog.DialogEx;
import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import yy.doctor.Constants.DateUnit;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.dialog.CommonOneDialog;
import yy.doctor.dialog.CommonTwoDialog;
import yy.doctor.frag.meeting.exam.TopicFrag;
import yy.doctor.model.meet.exam.Intro;
import yy.doctor.model.meet.exam.Intro.TIntro;
import yy.doctor.model.meet.exam.Paper.TPaper;
import yy.doctor.model.meet.exam.Topic;
import yy.doctor.model.meet.exam.Topic.TTopic;
import yy.doctor.util.Util;

/**
 * @author : GuoXuan
 * @since : 2017/4/27
 */

public class ExamTopicActivity extends BaseTopicActivity {

    private static final int KTextSizeDp = 16;
    private static final int KFiveMin = 300;

    private final int KXClose = 2;//X秒后自动关闭

    private long mCanUseTime; // 可以做题时间
    private long mUseTime; // 做题时间
    private DisposableSubscriber<Long> mSub;

    private TextView mTvTime;

    private CommonOneDialog mCloseDialog;//离考试结束的提示框
    private CommonOneDialog mSubmitDialog;
    private CommonTwoDialog mSubDialog;

    public static void nav(Context context, String meetId, String moduleId, Intro intro) {
        Intent i = new Intent(context, ExamTopicActivity.class)
                .putExtra(Extra.KMeetId, meetId)
                .putExtra(Extra.KModuleId, moduleId)
                .putExtra(Extra.KData, intro);
        LaunchUtil.startActivity(context, i);
    }

    @Override
    public void initData() {
        super.initData();

        mIntro = (Intro) getIntent().getSerializableExtra(Extra.KData);
        mPaper = mIntro.getEv(TIntro.paper);
        mPaperId = mPaper.getString(TPaper.id);
        mAllTopics = mPaper.getList(TPaper.questions);

        TopicFrag topicFrag = null;
        int all = mAllTopics.size();
        for (int i = 0; i < mAllTopics.size(); i++) {
            topicFrag = new TopicFrag();
            Topic topic = mAllTopics.get(i);
            topicFrag.setTopic(topic);
            //最后一题
            if (i == mAllTopics.size() - 1) {
                topicFrag.isLast();
            }
            topicFrag.setOnNextListener(v -> {
                getAnswer(mAllTopics);
                if (getCurrentItem() < all - 1) {
                    setCurrentItem(getCurrentItem() + 1);
                } else {
                    lastTopic(all - mCount);
                }
            });
            add(topicFrag);
        }
        mCanUseTime = mIntro.getInt(TIntro.usetime) * 60;
    }

    @Override
    public void initNavBar(NavBar bar) {
        super.initNavBar(bar);
        mTvLeft.setText("考试");
        //默认显示,外加倒计时
        mTvTime = new TextView(ExamTopicActivity.this);
        mTvTime.setText(Util.formatTime(mCanUseTime, DateUnit.hour));
        mTvTime.setGravity(Gravity.CENTER);
        mTvTime.setTextColor(Color.WHITE);
        mTvTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, KTextSizeDp);
        Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(mCanUseTime + 1)
                .map(aLong -> mCanUseTime - aLong) // 转换成倒数的时间
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createSub());
        bar.addViewMid(mTvTime);
    }

    @Override
    public void setViews() {
        super.setViews();//第一题
        setGv();
        String topicId = mAllTopics.get(0).getString(TTopic.sort);
        mTvAll.setText(topicId + "/" + mAllTopics.size());
        mTvNavAll.setText(topicId + "/" + mAllTopics.size());
    }

    @Override
    protected void topicCaseVisibility(boolean showState) {
        super.topicCaseVisibility(showState);
        if (getTopicCaseShow()) {
            mTvLeft.setText("题目");
            goneView(mTvTime);
        } else {
            mTvLeft.setText("考试");
            showView(mTvTime);
        }
    }

    @Override
    protected void lastTopic(int noFinish) {
        //考试时间未完
        if (mSubDialog == null) {
            mSubDialog = new CommonTwoDialog(ExamTopicActivity.this);
        }
        if (noFinish > 0) {
            //还有没作答
            mSubDialog.setTvMainHint("还有" + noFinish + "题未完成,继续提交将不得分")
                    .setTvSecondaryHint("是否确认提交答卷?");
        } else {
            //全部作答完了
            mSubDialog.setTvMainHint("确定提交答案?")
                    .hideSecond();
        }
        mSubDialog.mTvLeft(getString(R.string.exam_submit_sure))
                .mTvRight(getString(R.string.exam_continue))
                .setLayoutListener(new CommonTwoDialog.OnLayoutListener() {
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
        Intent i = new Intent(ExamTopicActivity.this, ExamEndActivity.class)
                .putExtra(Extra.KMeetId, mMeetId)
                .putExtra(Extra.KModuleId, mModuleId)
                .putExtra(Extra.KPaperId, mPaperId)
                .putExtra(Extra.KTime, mUseTime)
                .putExtra(Extra.KData, getAnswer(mAllTopics));
        LaunchUtil.startActivity(ExamTopicActivity.this, i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSub != null && !mSub.isDisposed()) {
            mSub.dispose();
            mSub = null;
        }
        recycleDialog(mCloseDialog);
        recycleDialog(mSubmitDialog);
        recycleDialog(mSubDialog);
    }

    private void recycleDialog(DialogEx dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
    }

    private DisposableSubscriber<Long> createSub() {
        mSub = new DisposableSubscriber<Long>() {

            @Override
            public void onNext(@NonNull Long aLong) {
                mUseTime = mCanUseTime - aLong;
                mTvTime.setText(Util.formatTime(aLong.intValue(), DateUnit.hour));
                // 剩余5分钟
                if (aLong == KFiveMin) {
                    mCloseDialog = new CommonOneDialog(ExamTopicActivity.this) {
                        @Override
                        public void close(Long aLong) {
                            setTvSecondaryHint(aLong + getString(R.string.exam_xs_close));
                        }
                    }
                            .setTvMainHint(getString(R.string.exam_five_min))
                            .setTvSecondaryHint(KXClose + getString(R.string.exam_xs_close));
                    mCloseDialog.start(KXClose);
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
            }

            @Override
            public void onComplete() {
                mSubmitDialog = new CommonOneDialog(ExamTopicActivity.this)
                        .setTvMainHint(getString(R.string.exam_end))
                        .setTvSecondaryHint(getString(R.string.exam_submit));
                mSubmitDialog.setCancelable(false);
                mSubmitDialog.setOnClickListener(v -> {
                    mSubmitDialog.dismiss();
                    submit();
                });
                mSubmitDialog.show();
            }
        };
        return mSub;
    }
}
