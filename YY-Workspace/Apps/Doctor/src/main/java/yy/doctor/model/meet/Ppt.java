package yy.doctor.model.meet;

import java.io.Serializable;

import lib.ys.model.EVal;
import lib.ys.model.inject.BindObj;
import yy.doctor.model.meet.Ppt.TPpt;

/**
 * 微课信息
 *
 * @author : GuoXuan
 * @since : 2017/5/8
 */

public class Ppt extends EVal<TPpt> implements Serializable {
    public enum TPpt {
        @BindObj(Course.class)
        course,//微课
        courseId,//微课ID
        id,//微课明细ID
        meetId,//会议ID
        moduleID,//模块ID
    }
}
