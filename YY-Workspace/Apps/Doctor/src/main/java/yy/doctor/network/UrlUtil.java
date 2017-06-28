package yy.doctor.network;

/**
 * @author CaiXiang
 * @since 2017/4/10
 */
public class UrlUtil {

    private static String KHttpDef = "http://";
    private static String mHostName = null;
    private static String mBaseHost = null;

    protected static String mBase = null;

    private static boolean mIsDebug = true;

    private static void init() {
        if (mIsDebug) {
            // 测试线  10.0.0.250:8082/
            mBaseHost = "www.medcn.com/v7/";
//            mHostName = KHttpDef + "10.0.0.234:80/"; // 礼平电脑
//            mHostName = KHttpDef + "10.0.0.250:8082/"; // 轩哥电脑
        } else {
            // 正式线
            mBaseHost = "www.medcn.com/v7/";
        }

        mHostName = KHttpDef + mBaseHost;
        mBase = mHostName + "api/";
        mBaseHost += "api";
    }

    public static void setDebug(boolean isDebug) {
        mIsDebug = isDebug;
        init();
    }

    public static String getHostName() {
        return mHostName;
    }

    public static String getBaseUrl() {
        return mBase;
    }

    public static String getBaseHost() {
        return mBaseHost;
    }

    public interface UrlUser {
        String KAd = "advert";
        String KLogin = "login";
        String KLogout = "logout";
        String KForgetPwd = "regist/pwd/reset";
        String KProfile = "user/info";
        String KModify = "user/modify";
        String KUpHeaderImg = "user/upheadimg";
        String KChangePwd = "user/resetpwd";
        String KCollectionMeetings = "myFavorite";
        String KBindJPush = "bindJpush";
        String KCheckAppVersion = "version/newly";
    }

    public interface UrlRegister {
        String KRegisterBase = "regist/";
        String KProvince = KRegisterBase + "provinces";
        String KCity = KRegisterBase + "cities";
        String KRegister = KRegisterBase + "regist";
        String KHospital = KRegisterBase + "hos";
        String KDepart = KRegisterBase + "depart";
    }

    public interface UrlHome {
        String KBanner = "banner";
        String KRecommendMeeting = "meet/tuijian";
        String KRecommendUnitNum = "publicAccount/recommend";
    }

    public interface UrlMeet {
        String KMeetBase = "meet/";
        String KMeets = KMeetBase + "meets";
        String KInfo = KMeetBase + "info";
        String KTypes = KMeetBase + "types";

        String KToExam = KMeetBase + "toexam";
        String KToSurvey = KMeetBase + "tosurvey";
        String KToSign = KMeetBase + "tosign";
        String KToPPT = KMeetBase + "toppt";
        String KToVideo = KMeetBase + "tovideo";

        String KSubmitSur = KMeetBase + "submitsur";
        String KSubmitEx = KMeetBase + "submitex";
        String KSubmitPPT = KMeetBase + "ppt/record";

        String KSign = KMeetBase + "sign";
        String KSend = KMeetBase + "message/send";
        String KVideo = KMeetBase + "video/sublist";
        String KHistories = KMeetBase + "message/histories";

        String KCollectMeeting = KMeetBase + "favorite";

        String KMeetingData = KMeetBase + "pageMaterial";
        String KMeetingVideoRecord = KMeetBase + "video/record";

        String KMeetShare = KMeetBase + "share?meetId=";
        String KMeetingRecord = KMeetBase + "exit";

        String KWs = "ws://";
        String KIm = "/im";
    }

    public interface UrlData {
        String KThomsonAll = "data/thomson/all";
        String KThomson = "data/thomson/category";
        String KThomSonData = "data/thomson/datas";
        String KPreview = "data/view";
    }

    public interface UrlEpn {
        String KEpnDetails = "shop/tradeInfo";
        String KEpnRecharge = "alipay/recharge";
    }

    public interface UrlEpc {
        String KExchange = "shop/buy";
        String KOrder = "shop/order";
        String KEpc = "shop/goods";
        String KEpcDetail = "shop/goodInfo";
    }

    public interface UrlUnitNum {
        String KUnitNum = "publicAccount/mySubscribe";
        String KAttention = "publicAccount/subscribe";
        String KUnitNumDetail = "publicAccount/unitInfo";
        String KUnitNumData = "publicAccount/materialList";
    }

    public interface UrlSearch {
        String KSearchRecUnitNum = "publicAccount/recommend";
        String KSearchUnitNum = "publicAccount/search";
        String KSearchMeeting = "meet/search";
    }

}
