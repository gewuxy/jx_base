package lib.ys.util.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

import lib.ys.AppEx;
import lib.ys.YSLog;
import lib.ys.util.TextUtil;

public class PermissionChecker {

    private static final String TAG = PermissionChecker.class.getSimpleName();

    private static PermissionChecker mInst;

    private PermissionChecker() {
    }

    public static PermissionChecker inst() {
        if (mInst == null) {
            mInst = new PermissionChecker();
        }
        return mInst;
    }

    public boolean check(CheckTask t) {
        int code = t.getCode();
        OnPermissionListener l = t.getListener();
        String[] ps = t.getPermissions();

        if (checkMiUiSms(ps)) {
            l.onPermissionResult(code, PermissionResult.never_ask);
            return false;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (l instanceof Activity) {
            return PermissionUtil.with((Activity) l)
                    .addRequestCode(code)
                    .permissions(ps)
                    .listener(l)
                    .request();
        } else if (l instanceof Fragment) {
            return PermissionUtil.with((Fragment) l)
                    .addRequestCode(code)
                    .permissions(ps)
                    .listener(l)
                    .request();
        }
        return false;
    }

    /**
     * 权限是否允许
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean allow(@NonNull Context context, String... permissions) {
        if (checkMiUiSms(permissions)) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        List<String> list = PermissionUtil.findDeniedPermissions(PermissionUtil.getActivity(context), permissions);
        if (list == null || list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 权限请求回调结果
     *
     * @param host
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(Object host, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (host instanceof Activity) {
            PermissionUtil.onRequestPermissionsResult((Activity) host, requestCode, permissions, grantResults);
        } else if (host instanceof Fragment) {
            PermissionUtil.onRequestPermissionsResult((Fragment) host, requestCode, permissions, grantResults);
        }
    }

    private static final String KXiaoMi = "Xiaomi";

    /**
     *
     * @param permissions
     * @return true 没有小米的短信权限
     */
    private static boolean checkMiUiSms(String... permissions) {
        String manufacturer = Build.MANUFACTURER;
        boolean miUiSms = false; // 不是小米短信
        if (TextUtil.isNotEmpty(manufacturer) && manufacturer.equals(KXiaoMi)) {
            for (String permission : permissions) {
                if (TextUtil.isNotEmpty(permission) &&
                        (permission.equals(Manifest.permission.SEND_SMS) ||
                                permission.equals(Manifest.permission.READ_SMS) ||
                                permission.equals(Manifest.permission.RECEIVE_SMS) ||
                                permission.equals(Manifest.permission.BROADCAST_SMS))) {
                    YSLog.d(TAG, "checkMiUiSms : have Sms");
                    miUiSms = checkSmsPermission(permission); // false 有权限
                    break;
                }
            }
        }
        return miUiSms;
    }

    private static boolean checkSmsPermission(String permission) {
        int result = android.support.v4.content.PermissionChecker.checkPermission(AppEx.getContext(), permission, Process.myPid(), Process.myUid(), AppEx.getContext().getPackageName());
        return result != android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
    }

}