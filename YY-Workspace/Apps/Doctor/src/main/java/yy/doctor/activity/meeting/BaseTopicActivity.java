package yy.doctor.activity.meeting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.yy.activity.base.BaseVPActivity;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.adapter.TopicCaseAdapter;
import yy.doctor.frag.exam.TopicFrag;
import yy.doctor.model.exam.Answer;
import yy.doctor.model.exam.Answer.TAnswer;
import yy.doctor.model.exam.Choice;
import yy.doctor.model.exam.Choice.TChoice;
import yy.doctor.model.exam.Intro;
import yy.doctor.model.exam.Intro.TIntro;
import yy.doctor.model.exam.Paper;
import yy.doctor.model.exam.Paper.TPaper;
import yy.doctor.model.exam.Topic;
import yy.doctor.model.exam.Topic.TTopic;

/**
 * 考试(问卷)题目界面
 *
 * @author : GuoXuan
 * @since : 2017/4/28
 */

public abstract class BaseTopicActivity extends BaseVPActivity {

    private static final long KDuration = 300l; //动画时长
    public static final int KVpSize = 3;

    private String mPaperId;
    private String mMeetId;
    private String mModuleId;

    private Paper mPaper;           //整套考题
    private TopicCaseAdapter mTopicCaseAdapter;  //考题情况的Adapter

    private GridView mGv;           //考题情况列表
    private TextView mTvAll;        //总数
    private TextView mTvCount;      //已完成数
    private LinearLayout mLayout;   //考题情况

    private Animation mEnter;       //进入动画
    private Animation mLeave;       //离开动画
    private boolean mHasAnimation;  //是否有动画在执行
    private boolean mTopicCaseShow; //是否在查看考题

    private int mCount;             //完成数量
    private View mViewMid;          //NavBar中边的View(显示考试情况的时候显示)
    private TextView mTvNavCount;   //完成情况
    private TextView mTvNavAll;     //总数

    protected Intro mIntro;
    protected List<Topic> mAllTopics;
    protected TextView mTvLeft;     //NavBar左边的TextView

    protected boolean getTopicCaseShow() {
        return mTopicCaseShow;
    }

    @Override
    public void initData() {
        mHasAnimation = false;
        mTopicCaseShow = false;

        mMeetId = getIntent().getStringExtra(Extra.KMeetId);
        mModuleId = getIntent().getStringExtra(Extra.KModuleId);
        mIntro = (Intro) getIntent().getSerializableExtra(Extra.KData);

        mPaperId = mIntro.getString(TIntro.id);
        mPaper = mIntro.getEv(TIntro.paper);
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

                    submit();
                }
            });
            add(topicFrag);
        }
    }

    @Override
    public void initNavBar(NavBar bar) {
        //添加左边text
        bar.addViewLeft(R.mipmap.nav_bar_ic_back, "", v -> {
            if (getTopicCaseShow()) {
                topicCaseVisibility(false);
            } else {
                finish();
            }
        });
        if (mTvLeft == null) {
            getTvLeft((ViewGroup) bar.getLayoutLeft());
        }
        //添加中间text
        //试题情况可见时显示
        mViewMid = inflate(R.layout.layout_exam_topic_nav_bar);
        bar.addViewMid(mViewMid);
        goneView(mViewMid);
    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_topic;
    }

    @Override
    public void findViews() {
        super.findViews();
        mLayout = findView(R.id.topic_case_all_layout);
        mGv = findView(R.id.topic_case_all_gv);
        mTvAll = findView(R.id.topic_tv_case_all);
        mTvCount = findView(R.id.topic_tv_case_finish_count);
        mTvNavCount = (TextView) mViewMid.findViewById(R.id.topic_tv_nar_bar_finish_count);
        mTvNavAll = (TextView) mViewMid.findViewById(R.id.topic_tv_nar_bar_all);
    }

    @Override
    public void setViews() {
        super.setViews();

        setOnClickListener(R.id.topic_layout_case_all);
        setOnClickListener(R.id.topic_tv_nar_bar_all);
        setGv();

        setOffscreenPageLimit(KVpSize);

        //第一题
        String topicId = mAllTopics.get(0).getString(TTopic.id);
        mTvAll.setText(topicId + "/" + mAllTopics.size());
        mTvNavAll.setText(topicId + "/" + mAllTopics.size());

        setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Topic topic = mAllTopics.get(position);
                String count = topic.getString(TTopic.id);
                mTvAll.setText(count + "/" + mAllTopics.size());
                mTvNavAll.setText(count + "/" + mAllTopics.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topic_layout_case_all:
                if (mEnter == null) {
                    mEnter = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF,
                            0.0f,
                            Animation.RELATIVE_TO_SELF,
                            0.0f,
                            Animation.RELATIVE_TO_SELF,
                            1.0f,
                            Animation.RELATIVE_TO_SELF,
                            0.0f);
                    setAnimation(mEnter);
                }
                if (mLeave == null) {
                    mLeave = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF,
                            0.0f,
                            Animation.RELATIVE_TO_SELF,
                            0.0f,
                            Animation.RELATIVE_TO_SELF,
                            0.0f,
                            Animation.RELATIVE_TO_SELF,
                            1.0f);
                    setAnimation(mLeave);
                }

                topicCaseVisibility(true);
                break;
            case R.id.topic_tv_nar_bar_all:
                topicCaseVisibility(false);
                break;
        }
    }

    /**
     * 获取试题答案
     *
     * @param topics
     */
    private List<Answer> getAnswer(List<Topic> topics) {
        List<Answer> answers = new ArrayList<>();//题号,答案
        mCount = 0;
        List<Choice> choices;
        Answer answer;
        StringBuffer answerStr;
        for (Topic topic : topics) {
            choices = topic.getList(TTopic.options);
            if (choices == null) {
                choices = topic.getList(TTopic.optionList);
            }

            answer = new Answer();
            answer.put(TAnswer.id, topic.getString(TTopic.id));
            //记录选中的选项
            answerStr = new StringBuffer();
            for (Choice choice : choices) {
                if (choice.getBoolean(TChoice.check)) {
                    answerStr.append(choice.getString(TChoice.key));
                }
            }
            answer.put(TAnswer.answer, answerStr.toString());
            answers.add(answer);

            //标记是否已作答
            if (answerStr.toString().length() > 0) {
                topic.put(TTopic.finish, true);
                mCount++;
            } else {
                topic.put(TTopic.finish, false);
            }
        }

        mTvCount.setText(String.valueOf(mCount));
        mTvNavCount.setText("已完成" + mCount);
        return answers;
    }

    /**
     * 提交答案
     */
    protected void submit() {
        Intent i = new Intent(BaseTopicActivity.this, ExamEndActivity.class)
                .putExtra(Extra.KMeetId, mMeetId)
                .putExtra(Extra.KModuleId, mModuleId)
                .putExtra(Extra.KPaperId, mPaperId)
                .putExtra(Extra.KData, (Serializable) getAnswer(mAllTopics));
        LaunchUtil.startActivity(BaseTopicActivity.this, i);
    }

    /**
     * 设置GridView
     * Adapter{@link TopicCaseAdapter}
     */
    private void setGv() {
        mTopicCaseAdapter = new TopicCaseAdapter();
        mTopicCaseAdapter.addAll(mAllTopics);

        mGv.setAdapter(mTopicCaseAdapter);
        mGv.setOnItemClickListener((parent, view, position, id) -> {
            setCurrentItem(position);
            topicCaseVisibility(false);
        });
    }

    /**
     * 考题情况是否显示
     *
     * @param showState true显示,false不显示
     */
    protected void topicCaseVisibility(boolean showState) {
        //没有动画执行的时候
        if (!mHasAnimation) {
            if (showState) {
                mTopicCaseAdapter.notifyDataSetChanged();
            }
            mTopicCaseShow = showState;
            mLayout.setVisibility(showState ? View.VISIBLE : View.GONE);
            mLayout.startAnimation(showState ? mEnter : mLeave);
            mViewMid.setVisibility(showState ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Animation设置
     * 设置播放时长
     * 设置监听
     *
     * @param animation
     */
    private void setAnimation(Animation animation) {
        animation.setDuration(KDuration);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mHasAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHasAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 获取左边文字
     *
     * @param layout
     */
    private void getTvLeft(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View childView = layout.getChildAt(i);
            if (childView instanceof ViewGroup) {
                getTvLeft((ViewGroup) childView);
            } else if (childView instanceof TextView) {
                mTvLeft = (TextView) childView;
            }
        }
    }
}