package yy.doctor.network;

/**
 * @author CaiXiang
 * @since 2017/4/10
 */
public class UrlUtil {

    private static String KHttpDef = "http://";
    private static String mHostName = null;

    protected static String mBase = null;

    private static boolean mIsDebug = true;

    private static void init() {
        if (mIsDebug) {
            // 测试线  192.168.1.19:8082/
            mHostName = KHttpDef + "www.medcn.com:8081/v7/";
        } else {
            // 正式线
            mHostName = KHttpDef + "www.medcn.com:8081/v7/";
        }

        mBase = mHostName + "api/";
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

    public interface UrlMain {
        String KTttttt = "";
    }

    public interface UrlUser {
        String KAd = "advert";
        String KLogin = "login";
        String KLogout = "logout";
        String KProfile = "user/info";
        String KModify = "user/modify";
        String KUpHeaderImg = "user/upheadimg";
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
    }

    public interface UrlMeet {
        String KMeetBase = "meet/";
        String KMeets = KMeetBase + "meets";
        String KInfo = KMeetBase + "info";
        String KTypes = KMeetBase + "types";//返回会议搜索时的科室列表选择

        String KToExam = KMeetBase + "toexam";
        String KToSurvey = KMeetBase + "tosurvey";
        String KToSign = KMeetBase + "tosign";
        String KToPpt = KMeetBase + "toppt";

        String KSubmitSur = KMeetBase + "submitsur";
        String KSubmitEx = KMeetBase + "submitex";

        String KSign = KMeetBase + "sign";
        String KSend = KMeetBase + "message/send";
        String KHistories = KMeetBase + "message/histories";
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
        String KAttention = "publicAccount/SubscribeOrNot";
        String KUnitNumDetail = "publicAccount/unitInfo";
    }

    public interface UrlSearch {
        String KRecommendUnitNum = "publicAccount/recommend";
        String KSearchUnitNum = "publicAccount/search";
    }

}
