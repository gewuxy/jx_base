package yy.doctor.network.builder;

import lib.network.model.NetworkRequest;
import lib.network.model.NetworkRequest.Builder;
import yy.doctor.network.NetFactory;
import yy.doctor.network.NetFactory.RegisterParam;
import yy.doctor.network.UrlUtil.UrlRegister;

/**
 * 注册用的Builder
 */
public class RegisterBuilder {
    private Builder mBuilder;

    public RegisterBuilder() {
        mBuilder = NetFactory.newPost(UrlRegister.register);
    }

    /**
     * @param invite 邀请码
     */
    public RegisterBuilder invite(String invite) {
        mBuilder.param(RegisterParam.invite, invite);
        return this;
    }

    /**
     * @param username 用户登录名
     */
    public RegisterBuilder username(String username) {
        mBuilder.param(RegisterParam.username, username);
        return this;
    }

    /**
     * @param nickname 用户昵称
     */
    public RegisterBuilder nickname(String nickname) {
        mBuilder.param(RegisterParam.nickname, nickname);
        return this;
    }

    /**
     * @param linkman 真实姓名
     */
    public RegisterBuilder linkman(String linkman) {
        mBuilder.param(RegisterParam.linkman, linkman);
        return this;
    }

    /**
     * @param mobile 手机号
     */
    public RegisterBuilder mobile(String mobile) {
        mBuilder.param(RegisterParam.mobile, mobile);
        return this;
    }

    /**
     * @param pwd 密码
     */
    public RegisterBuilder pwd(String pwd) {
        mBuilder.param(RegisterParam.pwd, pwd);
        return this;
    }

    /**
     * @param province 省份
     */
    public RegisterBuilder province(String province) {
        mBuilder.param(RegisterParam.province, province);
        return this;
    }

    /**
     * @param city 城市
     */
    public RegisterBuilder city(String city) {
        mBuilder.param(RegisterParam.city, city);
        return this;
    }

    /**
     * @param hospital 医院名称
     */
    public RegisterBuilder hospital(String hospital) {
        mBuilder.param(RegisterParam.hospital, hospital);
        return this;
    }

    /**
     * @param department 科室名称
     */
    public RegisterBuilder department(String department) {
        mBuilder.param(RegisterParam.department, department);
        return this;
    }

    /**
     * @param licence 执业许可证号
     */
    public RegisterBuilder licence(String licence) {
        mBuilder.param(RegisterParam.licence, licence);
        return this;
    }

    public NetworkRequest build() {
        return mBuilder.build();
    }
}