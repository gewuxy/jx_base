package yy.doctor.model.meet;

import lib.ys.model.EVal;
import lib.ys.model.inject.BindList;
import yy.doctor.model.meet.MeetDetail.TMeetDetail;

/**
 * 会议详情
 *
 * @author : GuoXuan
 * @since : 2017/5/5
 */

public class MeetDetail extends EVal<TMeetDetail> {
    public enum TMeetDetail {
        eduCredits,
        id,
        lecturer,//主讲者
        meetName,//会议名称
        meetType,//会议科室类型
        @BindList(Module.class)
        modules,//会议包含的模块
        organizer,//会议主办方
        requiredXs,
        startTime,//开始时间
        endTime,//结束时间
        state,//会议状态
        xsCredits,
    }
}
