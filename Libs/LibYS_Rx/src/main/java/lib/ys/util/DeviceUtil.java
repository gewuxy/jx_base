package lib.ys.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.v4.net.ConnectivityManagerCompat;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.Locale;

import lib.ys.AppEx;
import lib.ys.ConstantsEx;
import lib.ys.YSLog;
import lib.ys.util.permission.Permission;
import lib.ys.util.permission.PermissionChecker;

@SuppressWarnings("deprecation")
public class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

//    public static String getAPN() {
//        if (isNeedAPNProxy()) {
//            return "cmwap";
//        } else {
//            return "cmnet";
//        }
//    }

//    public static boolean isNeedAPNProxy() {
//        HttpHost proxy = null;
//        String proxyHost = Proxy.getDefaultHost();
//        if (proxyHost != null) {
//            proxy = new HttpHost(proxyHost, Proxy.getDefaultPort());
//            if (proxy.toString().length() > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }

//    public static HttpHost getAPNProxy() {
//        HttpHost proxy = null;
//        String proxyHost = Proxy.getDefaultHost();
//        if (proxyHost != null) {
//            proxy = new HttpHost(proxyHost, Proxy.getDefaultPort());
//            return proxy;
//        } else {
//            proxy = new HttpHost("10.0.0.172", 80);
//            return proxy;
//        }
//    }

    public static boolean isNetworkEnabled() {
        ConnectivityManager cm = (ConnectivityManager) AppEx.ct().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 对大数据传输时，需要调用该方法做出判断，如果流量敏感，应该提示用户
     *
     * @return true表示流量敏感，false表示不敏感
     */
    public static boolean isActiveNetworkMetered() {
        ConnectivityManager cm = (ConnectivityManager) AppEx.ct().getSystemService(Context.CONNECTIVITY_SERVICE);
        return ConnectivityManagerCompat.isActiveNetworkMetered(cm);
    }


    public static boolean isAirplaneModeOn(Context context) {
        return (0 != Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0));
    }

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        if (!PermissionChecker.allow(context, Permission.phone)) {
            return "";
        }

        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();

            if (imei == null || imei.equals("")) {
                imei = tm.getSubscriberId();
                if (imei == null) {
                    imei = ConstantsEx.KFakeIMEI;
                }
            }
        } catch (Exception e) {
            YSLog.e(TAG, e);
        }
        return imei;
    }

//    public static boolean isUsingWap(Context context) {
//        String apnName = getAPN();
//        if (("cmwap".equals(apnName) == true || "uniwap".equals(apnName) == true || "3gwap".equals(apnName) == true) && isWifi(context) == false) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * sdcard是否装好
     *
     * @return
     */
    public static boolean isSdcardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * sdcard是否可用
     *
     * @return
     */
    public static boolean isSdcardEnable() {
        return isSdcardMounted() && !Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED);
    }

    @SuppressLint("WifiManagerLeak")
    public static WifiInfo getWifiInfo() {
        WifiManager wifi = (WifiManager) AppEx.ct().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info;
    }

    public static long getRuntimeMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获取当前SDK的版本号
     *
     * @return
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    // 获取手机型号
    public static String getMobileType() {
        return Build.MODEL.replaceAll(" ", "");
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static File getSdcardDir() {
        File dir = Environment.getExternalStorageDirectory();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 是否>=6.0
     *
     * @return
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取android id
     *
     * @return
     */
    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * 设置资源加载的语言版本
     *
     * @param context
     * @param l
     */
    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    public static void setResLocale(@NonNull Context context, @NonNull Locale l) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (getSDKVersion() >= VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(l);
        } else {
            config.locale = l;
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
