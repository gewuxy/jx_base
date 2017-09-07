package lib.ys.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

import lib.ys.AppEx;
import lib.ys.ConstantsEx;
import lib.ys.YSLog;

/**
 * @author CaiXiang
 * @since 2017/9/4
 */

public class PackageUtil {
    private static final String TAG = PackageUtil.class.getSimpleName();

    public static String getMetaValue(@NonNull String key) {
        if (key == null) {
            return null;
        }

        String apiKey = null;
        try {
            ApplicationInfo ai = getPM().getApplicationInfo(getPkgName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                Bundle metaData = ai.metaData;
                if (null != metaData) {
                    apiKey = String.valueOf(metaData.get(key));
                }
            }
        } catch (NameNotFoundException e) {
            YSLog.e(TAG, e);
        }
        return apiKey;
    }

    public static void setMetaValue(String key, String value) {
        if (TextUtil.isEmpty(key)) {
            return;
        }

        ApplicationInfo appInfo = null;
        try {
            appInfo = getPM().getApplicationInfo(getPkgName(), PackageManager.GET_META_DATA);
            appInfo.metaData.putString(key, value);
        } catch (NameNotFoundException e) {
            YSLog.e(TAG, e);
        }
    }

    public static boolean isAndroidMarketAvailable() {
        List<PackageInfo> packages = getPM().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(ConstantsEx.KAndroidMarketPackageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前软件版本名
     *
     * @return
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPM().getPackageInfo(AppEx.ct().getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            YSLog.e(TAG, e);
        }
        return versionName;
    }

    /**
     * 获取当前软件版本号
     *
     * @return
     */
    public static int getAppVersion() {
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPM().getPackageInfo(AppEx.ct().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            YSLog.e(TAG, e);
        }
        return versionCode;
    }

    /**
     * 获取App的名字
     *
     * @return
     */
    public static String getAppName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getPM();
            applicationInfo = packageManager.getApplicationInfo(AppEx.ct().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 获取app图标
     *
     * @return
     */
    public static Drawable getAppIcon() {
        try {
            return getPM().getApplicationIcon(AppEx.ct().getPackageName());
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    private static PackageManager getPM() {
        return AppEx.ct().getPackageManager();
    }

    private static String getPkgName() {
        return AppEx.ct().getPackageName();
    }
}
