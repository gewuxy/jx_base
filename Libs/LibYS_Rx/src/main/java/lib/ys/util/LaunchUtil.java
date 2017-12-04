package lib.ys.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import lib.ys.AppEx;

/**
 * PS: host仅限于activity或fragment
 *
 * @author yuansui
 */
public class LaunchUtil {

    public static void startActivity(@NonNull Intent intent, Bundle... extras) {
        putExtras(intent, extras);

        Context context = AppEx.ct();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(@NonNull Class<?> clz, @Nullable Bundle... extras) {
        Context context = AppEx.ct();
        Intent intent = new Intent(context, clz);
        startActivity(intent, extras);
    }

    public static void startActivity(@NonNull Context context, @NonNull Intent intent, @Nullable Bundle... extras) {
        putExtras(intent, extras);

        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void startActivity(@NonNull Object host, @NonNull Intent intent, @Nullable Bundle... extras) {
        putExtras(intent, extras);

        if (host instanceof Activity) {
            ((Activity) host).startActivity(intent);
        } else if (host instanceof Fragment) {
            ((Fragment) host).startActivity(intent);
        } else if (host instanceof Context) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Context) host).startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppEx.getContext().startActivity(intent);
        }
    }

    public static void startActivity(@NonNull Context context, @NonNull Class<?> clz, @Nullable Bundle... extras) {
        Intent intent = new Intent(context, clz);
        startActivity(context, intent, extras);
    }

    public static void startActivity(@NonNull Object host, @NonNull Class<?> clz, @Nullable Bundle... extras) {
        Intent intent = new Intent(AppEx.ct(), clz);
        startActivity(host, intent, extras);
    }

    public static void startActivityForResult(@NonNull Object host, @NonNull Intent intent, int code, @Nullable Bundle... extras) {
        putExtras(intent, extras);

        if (host instanceof Activity) {
            ((Activity) host).startActivityForResult(intent, code);
        } else if (host instanceof Fragment) {
            ((Fragment) host).startActivityForResult(intent, code);
        } else {
            throw new IllegalArgumentException("host can only be one of activity or fragment");
        }
    }

    public static void startActivityForResult(@NonNull Object host, @NonNull Class<?> clz, int code, Bundle... extras) {
        Intent intent = new Intent(AppEx.ct(), clz);
        startActivityForResult(host, intent, code, extras);
    }

    public static void startService(@NonNull Context context, @NonNull Class<?> clz, @Nullable Bundle... extras) {
        Intent intent = new Intent(context, clz);
        putExtras(intent, extras);
        context.startService(intent);
    }

    public static void startService(@NonNull Context context, @NonNull Intent intent, @Nullable Bundle... extras) {
        putExtras(intent, extras);
        context.startService(intent);
    }

    private static void putExtras(@NonNull Intent intent, @Nullable Bundle... extras) {
        if (extras != null) {
            for (int i = 0; i < extras.length; ++i) {
                intent.putExtras(extras[i]);
            }
        }
    }
}
