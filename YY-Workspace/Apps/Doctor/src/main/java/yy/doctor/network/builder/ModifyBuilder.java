package yy.doctor.network.builder;

import lib.network.model.NetworkReq;
import lib.network.model.NetworkReq.Builder;
import yy.doctor.network.NetFactory;
import yy.doctor.network.NetFactory.ProfileParam;
import yy.doctor.network.UrlUtil.UrlUser;

/**
 * 修改个人资料的Builder
 */
public class ModifyBuilder {

    private Builder mBuilder;

    public ModifyBuilder() {
        mBuilder = NetFactory.newPost(UrlUser.KModify);
    }

    /**
     * 用户昵称
     *
     * @param nickname
     * @return
     */
    public ModifyBuilder nickname(String nickname) {
        mBuilder.param(ProfileParam.KNickname, nickname);
        return this;
    }

    /**
     * 真实姓名
     *
     * @param linkman
     * @return
     */
    public ModifyBuilder linkman(String linkman) {
        mBuilder.param(ProfileParam.KLinkman, linkman);
        return this;
    }


    /**
     * 手机号
     *
     * @param mobile
     * @return
     */
    public ModifyBuilder mobile(String mobile) {
        mBuilder.param(ProfileParam.KMobile, mobile);
        return this;
    }

    /**
     * 头像地址
     *
     * @param headImgUrl
     * @return
     */
    public ModifyBuilder headImgUrl(String headImgUrl) {
        mBuilder.param(ProfileParam.KHeadImgUrl, headImgUrl);
        return this;
    }

    /**
     * 医院
     *
     * @param hospital
     * @return
     */
    public ModifyBuilder hospital(String hospital) {
        mBuilder.param(ProfileParam.KHospital, hospital);
        return this;
    }

    /**
     * 医院等级
     *
     * @param hospitalLevel
     * @return
     */
    public ModifyBuilder hospitalLevel(String hospitalLevel) {
        mBuilder.param(ProfileParam.KHospitalLevel, hospitalLevel);
        return this;
    }

    /**
     * 科室
     *
     * @param department
     * @return
     */
    public ModifyBuilder department(String department) {
        mBuilder.param(ProfileParam.KDepartment, department);
        return this;
    }

    /**
     * 省份
     *
     * @param province
     * @return
     */
    public ModifyBuilder province(String province) {
        mBuilder.param(ProfileParam.KProvince, province);
        return this;
    }

    /**
     * 城市
     *
     * @param city
     * @return
     */
    public ModifyBuilder city(String city) {
        mBuilder.param(ProfileParam.KCity, city);
        return this;
    }

    /**
     * 区县
     *
     * @param area
     * @return
     */
    public ModifyBuilder area(String area) {
        mBuilder.param(ProfileParam.KArea, area);
        return this;
    }

    /**
     * CME卡号
     *
     * @param cmeId
     * @return
     */
    public ModifyBuilder cmeId(String cmeId) {
        mBuilder.param(ProfileParam.KCMEId, cmeId);
        return this;
    }

    /**
     * 执业许可证
     *
     * @param licence
     * @return
     */
    public ModifyBuilder licence(String licence) {
        mBuilder.param(ProfileParam.KLicence, licence);
        return this;
    }

    /**
     * 职称
     *
     * @param title
     * @return
     */
    public ModifyBuilder title(String title) {
        mBuilder.param(ProfileParam.KTitle, title);
        return this;
    }


    /**
     * 专长
     *
     * @param major
     * @return
     */
    public ModifyBuilder major(String major) {
        mBuilder.param(ProfileParam.KMajor, major);
        return this;
    }

    /**
     * 职务
     *
     * @param place
     * @return
     */
    public ModifyBuilder place(String place) {
        mBuilder.param(ProfileParam.KPlace, place);
        return this;
    }

    /**
     * 地址
     *
     * @param address
     * @return
     */
    public ModifyBuilder address(String address) {
        mBuilder.param(ProfileParam.KAddress, address);
        return this;
    }

    /**
     * 性别
     *
     * @param gender 1表示男 2表示女 0表示未设置
     * @return
     */
    public ModifyBuilder gender(int gender) {
        mBuilder.param(ProfileParam.KGender, gender);
        return this;
    }

    /**
     * 学历
     *
     * @param degree
     * @return
     */
    public ModifyBuilder degree(String degree) {
        mBuilder.param(ProfileParam.KDegree, degree);
        return this;
    }

    public NetworkReq builder() {
        return mBuilder.build();
    }

}