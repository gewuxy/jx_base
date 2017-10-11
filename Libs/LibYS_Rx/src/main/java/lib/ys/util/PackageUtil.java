package lib.ys.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import lib.ys.AppEx;
import lib.ys.ConstantsEx;
import lib.ys.YSLog;

public class PackageUtil {
    private static final String TAG = PackageUtil.class.getSimpleName();

    @Nullable
    public static String getMetaValue(@NonNull String key) {
        if (key == null) {
            return null;
        }

        ApplicationInfo info = getApplicationInfo(PackageManager.GET_META_DATA);
        if (info != null) {
            Bundle metaData = info.metaData;
            if (null != metaData) {
                return String.valueOf(metaData.get(key));
            }
        }
        return null;
    }

    /**
     * 设置meta信息
     *
     * @param key
     * @param value
     * @deprecated 只能更改内存中已经读出来的值, 不能更改声明的值
     */
    public static void setMetaValue(String key, String value) {
        if (TextUtil.isEmpty(key)) {
            return;
        }

        ApplicationInfo info = getApplicationInfo(PackageManager.GET_META_DATA);
        if (info != null) {
            info.metaData.putString(key, value);
        }
    }

    private final static String KAndroidMarketPackageName = "com.android.vending";

    /**
     * 是否安装了安卓市场
     *
     * @return
     */
    public static boolean isAndroidMarketAvailable() {
        List<PackageInfo> packages = getPM().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(KAndroidMarketPackageName)) {
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
        PackageInfo info = getPackageInfo();
        if (info != null) {
            return info.versionName;
        }
        return ConstantsEx.KEmpty;
    }

    /**
     * 获取当前软件版本号
     *
     * @return
     */
    public static int getAppVersionCode() {
        int versionCode = -1;
        PackageInfo info = getPackageInfo();
        if (info != null) {
            versionCode = info.versionCode;
        }
        return versionCode;
    }

    /**
     * 获取App的名字
     *
     * @return
     */
    public static CharSequence getAppName() {
        return getPM().getApplicationLabel(getApplicationInfo());
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

    @Nullable
    private static PackageInfo getPackageInfo() {
        try {
            return getPM().getPackageInfo(getPkgName(), 0);
        } catch (NameNotFoundException e) {
            YSLog.e(TAG, "getPackageInfo", e);
        }
        return null;
    }

    @Nullable
    private static ApplicationInfo getApplicationInfo() {
        return getApplicationInfo(0);
    }

    @Nullable
    private static ApplicationInfo getApplicationInfo(int flag) {
        try {
            return getPM().getApplicationInfo(getPkgName(), flag);
        } catch (NameNotFoundException e) {
            YSLog.e(TAG, "getApplicationInfo", e);
        }
        return null;
    }
}
