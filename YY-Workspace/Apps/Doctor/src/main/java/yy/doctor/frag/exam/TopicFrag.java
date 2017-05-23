package yy.doctor.frag.exam;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import lib.ys.ui.other.NavBar;
import lib.yy.frag.base.BaseListFrag;
import yy.doctor.R;
import yy.doctor.adapter.TopicAdapter;
import yy.doctor.model.exam.Choose;
import yy.doctor.model.exam.Topic;
import yy.doctor.model.exam.Topic.TTopic;

/**
 * 单个考题/问卷
 *
 * @author : GuoXuan
 * @since : 2017/4/28
 */

public class TopicFrag extends BaseListFrag<Choose, TopicAdapter> {

    //TODO:最后一题

    private TextView mTvQ;
    private TextView mTvBtn;
    private Topic mTopic;                   //该题目的信息
    private List<Choose> mChooses;
    private OnNextListener mOnNextListener;

    public interface OnNextListener {
        void onNext(View v);
    }

    public void setOnNextListener(OnNextListener onNextListener) {
        mOnNextListener = onNextListener;
    }

    /**
     * 设置单个考题
     *
     * @param topic
     */
    public TopicFrag setTopic(Topic topic) {
        mTopic = topic;
        return this;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initNavBar(NavBar bar) {

    }

    @Override
    public void findViews() {
        super.findViews();
        mTvQ = findView(R.id.exam_topic_tv_question);
        mTvBtn = findView(R.id.exam_topic_footer_tv_btn);

    }

    @Override
    public void setViews() {
        super.setViews();

        setDividerHeight(0);
        setBackgroundResource(R.color.white);

        //设置题目
        mTvQ.setText(mTopic.getString(TTopic.id) + ". " + mTopic.getString(TTopic.title));
        //设置选项
        mChooses = mTopic.getList(TTopic.options);
        if (mChooses == null) {
            mChooses = mTopic.getList(TTopic.optionList);
        }
        setData(mChooses);

        //单选隐藏下一题的按钮
        if (mTopic.getInt(TTopic.qtype) == 0) {
            mTvBtn.setVisibility(View.GONE);
            getAdapter().setIsSingle(true);
            getAdapter().setOnItemCheckListener(v -> toNext(v));
        } else {
            // 设置多选
            getAdapter().setIsSingle(false);
            getAdapter().setOnItemCheckListener(v -> mTopic.put(TTopic.finish, v.isSelected()));
            //下一题
            mTvBtn.setOnClickListener(v -> toNext(v));
        }
    }

    private void toNext(View v) {
        if (mOnNextListener != null) {
            mOnNextListener.onNext(v);
        }
    }

    @Override
    public View createHeaderView() {
        return inflate(R.layout.layout_exam_topic_header);
    }

    @Override
    public View createFooterView() {
        return inflate(R.layout.layout_exam_topic_footer);
    }

}
