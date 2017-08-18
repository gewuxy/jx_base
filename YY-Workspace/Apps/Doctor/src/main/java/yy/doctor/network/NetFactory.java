package yy.doctor.network;

import java.util.ArrayList;
import java.util.List;

import lib.network.model.NetworkReq;
import lib.network.model.NetworkReq.Builder;
import lib.network.model.param.CommonPair;
import lib.ys.util.DeviceUtil;
import yy.doctor.model.Profile;
import yy.doctor.model.Profile.TProfile;
import yy.doctor.network.UrlUtil.UrlData;
import yy.doctor.network.UrlUtil.UrlEpn;
import yy.doctor.network.UrlUtil.UrlMeet;
import yy.doctor.network.UrlUtil.UrlRegister;
import yy.doctor.network.UrlUtil.UrlSearch;
import yy.doctor.network.UrlUtil.UrlUser;
import yy.doctor.network.builder.ModifyBuilder;
import yy.doctor.network.builder.SignBuilder;
import yy.doctor.network.builder.SubmitBuilder;
import yy.doctor.ui.activity.meeting.MeetingFolderActivity.ZeroShowType;
import yy.doctor.ui.frag.data.BaseDataUnitsFrag.DataType;

/**
 * @author CaiXiang
 * @since 2017/4/10
 */
public class NetFactory {

    private static final String TAG = NetFactory.class.getSimpleName();


    /*********************************
     * 以下是工具
     */

    /**
     * 获取post请求
     *
     * @param url
     * @return
     */
    public static Builder newPost(String url) {
        return NetworkReq.newBuilder(UrlUtil.getBaseUrl() + url)
                .post()
                .header(getBaseHeader());
    }

    public static Builder newPost(String url, int pageNum, int pageSize) {
        return NetworkReq.newBuilder(UrlUtil.getBaseUrl() + url)
                .post()
                .param(CommonParam.KPageNum, pageNum)
                .param(CommonParam.KPageSize, pageSize)
                .header(getBaseHeader());
    }

    /**
     * 获取get请求
     *
     * @param url
     * @return
     */
    public static Builder newGet(String url) {
        return NetworkReq.newBuilder(UrlUtil.getBaseUrl() + url)
                .get()
                .header(getBaseHeader());
    }

    public static Builder newGet(String url, int pageNum, int pageSize) {
        return NetworkReq.newBuilder(UrlUtil.getBaseUrl() + url)
                .get()
                .param(CommonParam.KPageNum, pageNum)
                .param(CommonParam.KPageSize, pageSize)
                .header(getBaseHeader());
    }

    /**
     * 获取upload请求
     *
     * @param url
     * @return
     */
    public static Builder newUpload(String url) {
        return NetworkReq.newBuilder(UrlUtil.getBaseUrl() + url)
                .upload()
                .header(getBaseHeader());
    }

    /**
     * 获取download请求
     *
     * @param url
     * @param filePath
     * @param fileName
     * @return
     */
    public static Builder newDownload(String url, String filePath, String fileName) {
        return NetworkReq.newBuilder(url)
                .download(filePath, fileName)
                .header(getBaseHeader());
    }

    private static List<CommonPair> getBaseHeader() {
        List<CommonPair> ps = new ArrayList<>();

        ps.add(newPair(BaseParam.KDevice, "android"));
        ps.add(newPair(BaseParam.KOSVersion, DeviceUtil.getSystemVersion()));
        ps.add(newPair(BaseParam.KAppVersion, DeviceUtil.getAppVersion()));

        if (Profile.inst().isLogin()) {
            ps.add(newPair(CommonParam.KToken, Profile.inst().getString(TProfile.token)));
        }
        return ps;
    }

    private static CommonPair newPair(String key, Object value) {
        return new CommonPair(key, value);
    }

    public interface BaseParam {
        String KOSVersion = "os_version";
        String KDevice = "os_type";
        String KAppVersion = "app_version";
    }

    public interface CommonParam {
        String KToken = "token";
        String KPreId = "preId";
        String KOffset = "offset";

        String KPageNum = "pageNum";
        String KPageSize = "pageSize";
    }

    public interface RegisterParam {
        String KUsername = "username";//用户名
        String KNickname = "nickname";//昵称
        String KLinkman = "linkman";//真实名字
        String KMobile = "mobile";//手机号
        String KType = "type";
        String KPassword = "password";//密码
        String KProvince = "province";//省份
        String KCity = "city";//城市
        String KZone = "zone"; // 区县
        String KHospital = "hospital";//医院
        String KCategory = "category";//专科一级名称
        String KName = "name";//专科一级名称
        String KHospitalLevel = "hospitalLevel";//医院级别
        String KDepartment = "department";//科室
        String KInvite = "invite";//科室
        String KTitle = "title";//邀请码
        String KLicence = "licence";//执业许可证号
        String KCaptcha = "captcha";//注册验证码
        String KMasterId = "masterId";//二维码参数，激活码提供方id
        String KVersion = "version"; // 配置信息的版本号
    }

    public interface UserParam {
        String KUserName = "username";
        String KPassword = "password";
        String KOldPwd = "oldPwd";
        String KNewPwd = "newPwd";
        String KJPushRegisterId = "registionId";
    }

    public interface MeetParam {
        String KState = "state";//会议状态
        String KDepart = "depart";//

        String KMeetId = "meetId";//会议
        String KPaperId = "paperId";//试卷
        String KModuleId = "moduleId";//模块
        String KSurveyId = "surveyId";//问卷
        String KCourseId = "courseId";//微课
        String KPreId = "preId";//视频明细
        String KDetailId = "detailId";//微课明细
        String KQuestionId = "questionId";//试题
        String KPositionId = "positionId";//签到位置

        String KAnswer = "answer";//答案
        String KItemJson = "itemJson";//答案列表
        String KDetails = "details";//答案列表

        String KUseTime = "usedtime";//微课用时  秒

        String KMessage = "message";//留言内容
        String KMsgType = "msgType";//留言类型

        String KSignLng = "signLng";//经度
        String KSignLat = "signLat";//维度

        String KFinish = "finished"; //  是否完成
        String KShowFlag = "showFlag"; // 是否显示0
    }

    private interface HomeParam {
        String KType = "type";
    }

    public interface ProfileParam {
        String KHeadImgUrl = "headimg";   //头像地址
        String KLinkman = "linkman";   //真实姓名
        String KHospital = "hospital";   //医院
        String KDepartment = "department";   //科室
        String KHospitalLevel = "hospitalLevel";  //医院等级
        String KCmeId = "cmeId";  //CME卡号
        String KLicence = "licence";   //执业许可证
        String KTitle = "title";   //职称
        String KMajor = "major";    //专长
        String KCategory = "category";    //专科一级
        String KName = "name";    //专科二级
        String KProvince = "province";   //省份
        String KCity = "city";    //城市
        String KArea = "zone";  //区

        String KMobile = "mobile";    //手机号
        String KAddress = "address";   //地址
    }

    private interface UpHeadImgParam {
        String KFile = "file";   //文件
    }

    public interface CityParam {
        String KCity = "preId";  //省份ID
    }

    public interface EpnRechargeParam {
        String KBody = "body";  //商品描述
        String KSubject = "subject";  //商品名称
        String KTotalAmount = "totalAmount";  //商品价格
    }

    private interface AttentionParam {
        String KMasterId = "masterId";    // 关注/取消关注的单位号id
        String KStatus = "status";     // 0:取消关注 1：关注
    }

    private interface UnitNumDetailParam {
        String KId = "id";  //单位号id
    }

    private interface SearchParam {
        String KKeyword = "keyword";  //关键字
    }

    public interface EpcExchangeParam {
        String KGoodsId = "goodsId";    //商品id
        String KPrice = "price";    //商品价格
        String KReceiver = "receiver";    //收货人
        String KPhone = "phone";    //手机号
        String KProvince = "province";   //省份
        String KAddress = "address";    //地址
        String KBuyLimit = "buyLimit";   //商品限购数
    }

    public interface EpcParam {
        String KGoodsId = "id";  //商品id
    }

    public interface CollectMeetingParam {
        String KMeetingId = "meetId";
        String KTurnTo = "turnTo";
    }

    public interface CollectionParam {
        String KType = "type";
        String KDataFileId = "dataFileId";
        String KCollectionStatus = "resourceId";
    }

    public interface ThomsonParam {
        String KPreId = "preId";
        String KCategoryId = "categoryId";
    }

    public interface DataUnitParam {
        String KPreId = "preId";    //父级文件夹的id,第一级不用传preId
        String KType = "type";      //type=0代表汤森,type=1代表药品目录，type=2代表临床
        String KLeaf = "leaf";      //下一级是否是文件夹, 下一级为文件返回true,下一级是文件夹返回false.第一级传null或空字符串
        String KCategoryId = "categoryId";//上级文件夹id
        String KDataFileId = "dataFileId";//文件id
        String KKeyword = "keyword";    //关键字
    }

    public interface WXParam {
        String KCode = "code";
        String KOpenId = "openid";
    }

    /**
     * 检查是否已被绑定
     *
     * @param code
     * @return
     */
    public static NetworkReq check_wx_bind(String code) {
        return newPost(UrlUser.KBindWX)
                .param(WXParam.KCode, code)
                .build();
    }

    /**
     * 绑定极光推送的ID
     *
     * @param registerId
     * @return
     */
    public static NetworkReq bindJPush(String registerId) {
        return newGet(UrlUser.KBindJPush)
                .param(UserParam.KJPushRegisterId, registerId)
                .retry(5, 1000)
                .build();
    }

    /**
     * 文件夹
     *
     * @param showFlag {@link ZeroShowType}
     * @return
     */
    public static NetworkReq meetFolder(String preId, int showFlag) {
        return newGet(UrlMeet.KMeetingFolder)
                .param(CommonParam.KPreId, preId)
                .param(MeetParam.KShowFlag, showFlag)
                .build();
    }

    /**
     * 文件夹里的文件夹
     *
     * @return
     */
    public static NetworkReq folderResource(String paperId) {
        return newGet(UrlMeet.KMeetingFolderResource)
                .param(MeetParam.KPaperId, paperId)
                .build();
    }


    /**
     * 个人信息
     *
     * @return
     */
    public static NetworkReq profile() {
        return newGet(UrlUser.KProfile).build();
    }

    /**
     * 个人信息修改
     *
     * @return
     */
    public static ModifyBuilder newModifyBuilder() {
        return new ModifyBuilder();
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public static NetworkReq changePwd(String oldPwd, String newPwd) {
        return newPost(UrlUser.KChangePwd)
                .param(UserParam.KOldPwd, oldPwd)
                .param(UserParam.KNewPwd, newPwd)
                .build();
    }

    /**
     * 获取省市区的资料
     *
     * @param id
     * @return
     */
    public static NetworkReq pcd(String... id) {
        if (id != null && id.length > 0) {
            return newGet(UrlRegister.KCity)
                    .param(CityParam.KCity, id[0])
                    .build();
        } else {
            return newGet(UrlRegister.KProvince).build();
        }
    }


    /**
     * 检查app版本
     *
     * @return
     */
    public static NetworkReq checkAppVersion() {
        return newGet(UrlUser.KCheckAppVersion)
                .build();
    }

    /**
     * 收藏的会议列表
     *
     * @param pageNum
     * @param pageSize
     * @return 该值为空或0时，表示会议类型
     */
    public static NetworkReq collection(int pageNum, int pageSize, int type) {
        return newGet(UrlUser.KCollection, pageNum, pageSize)
                .param(CollectionParam.KType, type)
                .build();
    }

    /**
     * 收藏的药品目录详情
     *
     * @param dataFileId
     * @return
     */
    public static NetworkReq collectionDetail(String dataFileId) {
        return newGet(UrlData.KCollectionDetail)
                .param(CollectionParam.KDataFileId, dataFileId)
                .build();
    }

    /**
     * 收藏或者取消收藏
     *
     * @param resourceId
     * @param type
     * @return
     */
    public static NetworkReq collectionStatus(String resourceId, @DataType int type) {
        return newGet(UrlData.KCollectionStatus)
                .param(CollectionParam.KCollectionStatus, resourceId)
                .param(CollectionParam.KType, type)
                .build();
    }


    /**
     * 象数明细
     *
     * @return
     */
    public static NetworkReq epnDetails() {
        return newGet(UrlEpn.KEpnDetails)
                .build();
    }

    /**
     * 象数充值
     *
     * @return
     */
    public static NetworkReq epnRecharge(String subject, int totalAmount) {
        return newPost(UrlEpn.KEpnRecharge)
                .param(EpnRechargeParam.KSubject, subject)
                .param(EpnRechargeParam.KTotalAmount, totalAmount)
                .build();
    }

    /**
     * 单位号资料下载
     *
     * @param url
     * @param filePath
     * @param fileName
     * @return
     */
    public static NetworkReq downloadData(String url, String filePath, String fileName) {
        return newDownload(url, filePath, fileName)
                .build();
    }

    /**
     * 汤森路透
     *
     * @param preId 不传值的时候，返回汤森路透下一层的子栏目，传值的时候返回该preId下面的子栏目
     * @return
     */
    public static NetworkReq thomson(String preId) {
        return newGet(UrlData.KThomson)
                .param(ThomsonParam.KPreId, preId)
                .build();
    }

    /**
     * 搜索页面推荐的单位号
     *
     * @return
     */
    public static NetworkReq searchRecUnitNum() {
        return newGet(UrlSearch.KSearchRecUnitNum)
                .build();
    }

    /**
     * 搜索单位号
     *
     * @param keyword
     * @return
     */
    public static NetworkReq searchUnitNum(String keyword, int page, int pageSize) {
        return newPost(UrlSearch.KSearchUnitNum, page, pageSize)
                .param(SearchParam.KKeyword, keyword)
                .build();
    }

    /**
     * 搜索会议
     *
     * @param keyword
     * @return
     */
    public static NetworkReq searchMeeting(String keyword, int page, int pageSize) {
        return newPost(UrlSearch.KSearchMeeting, page, pageSize)
                .param(SearchParam.KKeyword, keyword)
                .build();
    }

    /**
     * 关注过的公众的所有会议
     */
    public static NetworkReq meets(int state, String depart, int page, int pageSize) {
        return newPost(UrlMeet.KMeets, page, pageSize)
                .param(MeetParam.KState, state)
                .param(MeetParam.KDepart, depart)
                .build();
    }

    /**
     * 会议详情
     */
    public static NetworkReq meetInfo(String meetId) {
        return newGet(UrlMeet.KInfo)
                .param(MeetParam.KMeetId, meetId)
                .build();
    }

    /**
     * 会议资料
     *
     * @param meetingId
     * @return
     */
    public static NetworkReq meetingData(String meetingId, int pageNum, int pageSize) {
        return newGet(UrlMeet.KMeetingData, pageNum, pageSize)
                .param(MeetParam.KMeetId, meetingId)
                .build();
    }

    /**
     * 会议科室列表
     *
     * @return
     */
    public static NetworkReq meetingDepartment() {
        return newPost(UrlMeet.KMeetingDepartment)
                .build();
    }

    public static NetworkReq toBase(String url, String meetId, String moduleId) {
        return newGet(url)
                .param(MeetParam.KMeetId, meetId)
                .param(MeetParam.KModuleId, moduleId)
                .build();
    }

    /**
     * 考试入口
     */
    public static NetworkReq toExam(String meetId, String moduleId) {
        return toBase(UrlMeet.KToExam, meetId, moduleId);
    }

    /**
     * 问卷入口
     */
    public static NetworkReq toSurvey(String meetId, String moduleId) {
        return toBase(UrlMeet.KToSurvey, meetId, moduleId);
    }

    /**
     * 微课(语音+PPT)入口
     */
    public static NetworkReq toPPT(String meetId, String moduleId) {
        return toBase(UrlMeet.KToPPT, meetId, moduleId);
    }

    /**
     * 签到入口
     */
    public static NetworkReq toSign(String meetId, String moduleId) {
        return toBase(UrlMeet.KToSign, meetId, moduleId);
    }

    /**
     * 视频入口
     */
    public static NetworkReq toVideo(String meetId, String moduleId) {
        return newGet(UrlMeet.KToVideo)
                .param(MeetParam.KMeetId, meetId)
                .param(MeetParam.KModuleId, moduleId)
                .build();
    }

    /**
     * 考试提交
     */
    public static SubmitBuilder submitEx() {
        return new SubmitBuilder(UrlMeet.KSubmitEx);
    }

    /**
     * 问卷提交
     */
    public static SubmitBuilder submitSur() {
        return new SubmitBuilder(UrlMeet.KSubmitSur);
    }

    /**
     * 微课学习提交
     */
    public static SubmitBuilder submitPpt() {
        return new SubmitBuilder(UrlMeet.KSubmitPPT);
    }

    /**
     * 视频学习提交
     */
    public static SubmitBuilder submitVideo() {
        return new SubmitBuilder(UrlMeet.KMeetingVideoRecord);
    }


    /**
     * 会议时间提交
     */
    public static NetworkReq submitMeet(String meetId, long useTime) {
        return newGet(UrlMeet.KMeetingRecord)
                .param(MeetParam.KMeetId, meetId)
                .param(MeetParam.KUseTime, useTime)
                .retry(6, 1000)
                .build();
    }

    /**
     * 签到
     */
    public static SignBuilder sign() {
        return new SignBuilder();
    }

    /**
     * 查询视频子目录
     */
    public static NetworkReq video(String preId) {
        return newGet(UrlMeet.KVideo)
                .param(MeetParam.KPreId, preId)
                .build();
    }

    /**
     * 会议留言记录
     */
    public static NetworkReq histories(String meetId, int pageSize, int pageNum) {
        return newGet(UrlMeet.KHistories, pageNum, pageSize)
                .param(MeetParam.KMeetId, meetId)
                .build();
    }

    /**
     * 发表会议留言
     */
    public static NetworkReq send(String meetId, String message, String msgType) {
        return newPost(UrlMeet.KSend)
                .param(MeetParam.KMeetId, meetId)
                .param(MeetParam.KMessage, message)
                .param(MeetParam.KMsgType, msgType)
                .build();
    }

    /**
     * 收藏会议
     *
     * @param meetId
     * @param turnTo 0:取消收藏，1:收藏
     * @return
     */
    public static NetworkReq collectMeeting(String meetId, int turnTo) {
        return newGet(UrlMeet.KCollectMeeting)
                .param(CollectMeetingParam.KMeetingId, meetId)
                .param(CollectMeetingParam.KTurnTo, turnTo)
                .build();
    }

    /**
     * 会议评论 web socket
     *
     * @return
     */
    public static NetworkReq commentIM(String meetId) {
        return NetworkReq.newBuilder(UrlMeet.KWs + UrlUtil.getBaseHost() + UrlMeet.KIm)
                .param(CommonParam.KToken, Profile.inst().getString(TProfile.token))
                .param(MeetParam.KMeetId, meetId)
                .build();
    }


    /**
     * 绑定手机号
     *
     * @param mobile
     * @param captcha
     * @return
     */
    public static NetworkReq bindMobile(String mobile, String captcha) {
        return newGet(UrlUser.KBindMobile)
                .param(RegisterParam.KMobile, mobile)
                .param(RegisterParam.KCaptcha, captcha)
                .build();
    }

    /**
     * 绑定邮箱
     *
     * @param email
     * @return
     */
    public static NetworkReq bindEmail(String email) {
        return newGet(UrlUser.KBindEmail)
                .param(RegisterParam.KUsername, email)
                .build();
    }

    /**
     * 解绑邮箱
     *
     * @return
     */
    public static NetworkReq unBindEmail() {
        return newGet(UrlUser.KUnBindEmail)
                .build();
    }

    /**
     * 绑定微信
     *
     * @param code
     * @return
     */
    public static NetworkReq bindWX(String code) {
        return newGet(UrlUser.KBindWXSet)
                .param(WXParam.KCode, code)
                .build();
    }


    /**
     * 参会统计(个人参会统计)
     *
     * @return
     */
    public static NetworkReq statsMeet(int offset) {
        return newPost(UrlMeet.KStatsAttend)
                .param(CommonParam.KOffset, offset)
                .build();
    }

    /**
     * 参会统计(关注单位号发布会议统计)
     *
     * @return
     */
    public static NetworkReq statsUnitNum(int offset) {
        return newPost(UrlMeet.KStatsPublish)
                .param(CommonParam.KOffset, offset)
                .build();
    }

    /**
     * 逐项更改用户信息
     *
     * @param key
     * @param val
     * @return
     */
    public static NetworkReq modifyProfile(String key, String val) {
        return newPost(UrlUser.KModify)
                .param(key, val)
                .build();
    }

}
