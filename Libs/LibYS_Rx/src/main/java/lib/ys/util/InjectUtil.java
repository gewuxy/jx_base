package lib.ys.util;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.Fragment;

import inject.annotation.router.Route;
import lib.ys.YSLog;


/**
 * 注入工具类
 *
 * @auther yuansui
 * @since 2017/8/2
 */
public class InjectUtil {
    private static final String TAG = InjectUtil.class.getSimpleName();

    interface InjectName {
        String KRouter = "Router";
        String KInject = "inject";
    }

    public static void bind(Activity activity) {
        intentBuilder(activity, activity.getIntent());
    }

    public static void bind(Fragment frag) {
        Class clz = frag.getClass();
        if (clz.isAnnotationPresent(Route.class)) {
            try {
                ReflectUtil.getMethod(clz.getName() + InjectName.KRouter,
                        InjectName.KInject,
                        clz
                ).invoke(null, frag);
            } catch (Exception e) {
                YSLog.e(TAG, "bind", e);
            }
        }
    }

    public static void bind(Service service, Intent i) {
        intentBuilder(service, i);
    }

    private static void intentBuilder(Object o, Intent i) {
        Class clz = o.getClass();
        if (clz.isAnnotationPresent(Route.class)) {
            try {
                ReflectUtil.getMethod(clz.getName() + InjectName.KRouter,
                        InjectName.KInject,
                        clz,
                        Intent.class
                ).invoke(null, o, i);
            } catch (Exception e) {
                YSLog.e(TAG, "intentBuilder", e);
            }
        }
    }
}
