package yy.doctor.adapter;

import lib.ys.adapter.AdapterEx;
import yy.doctor.R;
import yy.doctor.adapter.VH.meeting.TopicCaseVH;
import yy.doctor.model.exam.Topic;
import yy.doctor.model.exam.Topic.TTopic;

/**
 * 考试情况Adapter
 *
 * @author : GuoXuan
 * @since : 2017/4/28
 */

public class  TopicCaseAdapter extends AdapterEx<Topic, TopicCaseVH> {
    @Override
    public int getConvertViewResId() {
        return R.layout.layout_exam_topic_case_item;
    }

    @Override
    protected void refreshView(int position, TopicCaseVH holder) {
        Topic item = getItem(position);
        holder.getText().setText(item.getString(TTopic.id));
        holder.getText().setSelected(item.getBoolean(TTopic.finish));
    }

}