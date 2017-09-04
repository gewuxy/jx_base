package lib.ys.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.NonNull;

import lib.ys.AppEx;
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

    private static PackageManager getPM() {
        return AppEx.ct().getPackageManager();
    }

    private static String getPkgName() {
        return AppEx.ct().getPackageName();
    }
}
