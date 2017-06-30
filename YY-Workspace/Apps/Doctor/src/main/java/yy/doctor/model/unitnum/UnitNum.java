package yy.doctor.model.unitnum;

import lib.ys.model.EVal;
import yy.doctor.model.search.IRec;
import yy.doctor.model.unitnum.UnitNum.TUnitNum;

/**
 * @author CaiXiang
 * @since 2017/5/6
 */
public class UnitNum extends EVal<TUnitNum> implements IRec {

    @Override
    public int getType() {
        return RecType.unit_num;
    }

    public enum TUnitNum {

        alpha, // 首字母
        headimg, // 单位号头像
        id, // 单位号id
        nickname, // 单位号昵称

    }
}
