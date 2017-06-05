package lib.ys;

public interface ConstantsEx {
    String KAndroidMarketPackageName = "com.android.vending";
    int KErrNotFound = -1;
    int KErrDefault = -1;
    int KInvalidValue = Integer.MIN_VALUE;

    String KEmptyValue = "";

    String KFakeIMEI = "35278404110901162";

    // 图片
    String KPngExtend = ".png";
    String KJpgExtend = ".jpg";

    int KAlphaMax = 255;
    int KAlphaMin = 0;

    // 魅族品牌
    String KBrandMeiZu = "Meizu";

    // FormItem体系需要的requestCode偏移参数
    int KGroupOffset = 1;
    int KChildOffset = 9;

    String KEncoding_utf8 = "utf-8";

    interface ListConstants {
        int KDefaultLimit = 20;
        int KDefaultInitOffset = 0;
        String KDefaultInitLastId = "0";
    }

    float KInterpolatorMax = 1.0f;

    /**
     * 定义毫秒的转换
     */
    interface Milli {
        long KSecond = 1000;
        long KMinute = KSecond * 60;
        long KHour = KMinute * 60;
        long KDay = KHour * 24;
        long KWeek = KDay * 7;
    }


}
