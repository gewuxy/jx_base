package lib.ys.util.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    private String[] mPermissions;
    private int mRequestCode;
    private Object mObject; // activity or fragment

    /**
     * ********************* util *********************
     */

    public static List<String> getDeniedPermissions(Activity activity, String[] permissions) {
        return getDeniedPermissions((Object) activity, permissions);
    }

    public static List<String> getDeniedPermissions(Fragment fragment, String[] permissions) {
        return getDeniedPermissions((Object) fragment, permissions);
    }

    private static List<String> getDeniedPermissions(Object activity, String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }

        return findDeniedPermissions(getActivity(activity), permissions);
    }

    public static List<String> getNeverAskAgainPermissions(Activity activity, String[] permissions) {
        return getNeverAskAgainPermissions((Object) activity, permissions);
    }

    public static List<String> getNeverAskAgainPermissions(Fragment fragment, String[] permissions) {
        return getNeverAskAgainPermissions((Object) fragment, permissions);
    }

    private static List<String> getNeverAskAgainPermissions(Object activity, String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }

        return findNeverAskAgainPermissions(getActivity(activity), permissions);
    }

    public static List<String> getDeniedPermissionsWithoutNeverAskAgain(Activity activity, String[] permissions) {
        return getDeniedPermissionsWithoutNeverAskAgain((Object) activity, permissions);
    }

    public static List<String> getDeniedPermissionsWithoutNeverAskAgain(Fragment fragment, String[] permissions) {
        return getDeniedPermissionsWithoutNeverAskAgain((Object) fragment, permissions);
    }

    private static List<String> getDeniedPermissionsWithoutNeverAskAgain(Object activity, String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }

        return findDeniedPermissionWithoutNeverAskAgain(getActivity(activity), permissions);
    }

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        if (activity == null) {
            return null;
        }
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findNeverAskAgainPermissions(Activity activity, String... permission) {
        List<String> neverAskAgainPermission = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED &&
                    !activity.shouldShowRequestPermissionRationale(value)) {
                neverAskAgainPermission.add(value);
            }
        }

        return neverAskAgainPermission;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissionWithoutNeverAskAgain(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED &&
                    activity.shouldShowRequestPermissionRationale(value)) {
                denyPermissions.add(value);
            }
        }

        return denyPermissions;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public static boolean hasNeverAskAgainPermission(Activity activity, List<String> permission) {
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED &&
                    !activity.shouldShowRequestPermissionRationale(value)) {
                return true;
            }
        }

        return false;
    }

    public static String toString(List<String> permission) {
        if (permission == null || permission.isEmpty()) {
            return "";
        }

        return toString(permission.toArray(new String[permission.size()]));
    }

    public static String toString(String[] permission) {
        if (permission == null || permission.length <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String p : permission) {
            sb.append(p.replaceFirst("android.permission.", ""));
            sb.append(",");
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * ********************* init *********************
     */

    private static OnPermissionListener mPermissionListener;

    public PermissionUtil listener(OnPermissionListener l) {
        mPermissionListener = l;
        return this;
    }

    private PermissionUtil(Object object) {
        mObject = object;
    }

    public PermissionUtil permissions(String... permissions) {
        mPermissions = permissions;
        return this;
    }

    public PermissionUtil addRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public static PermissionUtil with(Activity activity) {
        return new PermissionUtil(activity);
    }

    public static PermissionUtil with(Fragment fragment) {
        return new PermissionUtil(fragment);
    }

    /**
     * ********************* request *********************
     */

    @TargetApi(value = Build.VERSION_CODES.M)
    public boolean request() {
        return requestPermissions(mObject, mRequestCode, mPermissions);
    }

    public static void needPermission(Activity activity, int requestCode, String[] permissions) {
        requestPermissions(activity, requestCode, permissions);
    }

    public static void needPermission(Fragment fragment, int requestCode, String[] permissions) {
        requestPermissions(fragment, requestCode, permissions);
    }

    public static void needPermission(Activity activity, int requestCode, String permission) {
        needPermission(activity, requestCode, new String[]{permission});
    }

    public static void needPermission(Fragment fragment, int requestCode, String permission) {
        needPermission(fragment, requestCode, new String[]{permission});
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private static boolean requestPermissions(Object object, int requestCode, String[] permissions) {
        if (!isOverMarshmallow()) {
            doExecuteSuccess(object, requestCode);
            return true;
        }
        List<String> deniedPermissions = findDeniedPermissions(getActivity(object), permissions);

        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getSimpleName() + " is not supported");
            }
            return false;
        } else {
            doExecuteSuccess(object, requestCode);
            return true;
        }
    }

    /**
     * ********************* on result *********************
     */

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    private static void requestResult(Object obj, int requestCode, String[] permissions, int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            if (hasNeverAskAgainPermission(getActivity(obj), deniedPermissions)) {
                doExecuteFailAsNeverAskAgain(obj, requestCode);
            } else {
                doExecuteFail(obj, requestCode);
            }
        } else {
            doExecuteSuccess(obj, requestCode);
        }
    }

    /**
     * ********************* reflect execute result *********************
     */

    private static void doExecuteSuccess(Object activity, int requestCode) {
        if (mPermissionListener != null) {
            mPermissionListener.onPermissionResult(requestCode, PermissionResult.granted);
        }
    }

    private static void doExecuteFail(Object activity, int requestCode) {
        if (mPermissionListener != null) {
            mPermissionListener.onPermissionResult(requestCode, PermissionResult.denied);
        }
    }

    private static void doExecuteFailAsNeverAskAgain(Object activity, int requestCode) {
        if (mPermissionListener != null) {
            mPermissionListener.onPermissionResult(requestCode, PermissionResult.never_ask);
        }
    }

}
