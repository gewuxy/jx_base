package lib.ys;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class YSLog {

    private static final String KSeparate = "=========";
    // JSON的缩进量
    private static final int KJsonIndent = 4;

    private static boolean mIsDebug = true;

    private YSLog() {
    }

    public static boolean isDebug() {
        return mIsDebug;
    }

    public static void setDebugState(boolean isDebug) {
        mIsDebug = isDebug;
    }

    public static int v(String tag, String msg) {
        if (mIsDebug) {
            return Log.v(tag, msg);
        } else {
            return 0;
        }

    }

    public static int v(String tag, String msg, Throwable tr) {
        if (mIsDebug) {
            return Log.v(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int d(String tag, String msg) {
        if (mIsDebug) {
            return Log.d(tag, msg);
        } else {
            return 0;
        }
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (mIsDebug) {
            return Log.d(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg) {
        if (mIsDebug) {
            return Log.i(tag, msg);
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (mIsDebug) {
            return Log.i(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (mIsDebug) {
            Log.e(tag, msg, tr);
        }
    }

    public static void d(String tag, Throwable e) {
        if (mIsDebug) {
            Log.d(tag, KSeparate + e.getClass().getSimpleName() + KSeparate, e);
        }
    }

    public static void e(String tag, Throwable e) {
        if (mIsDebug) {
            Log.e(tag, KSeparate + e.getClass().getSimpleName() + KSeparate, e);
        }
    }

    public static void e(String tag, String log) {
        if (mIsDebug) {
            Log.e(tag, log);
        }
    }

    public static void w(String tag, String log) {
        if (mIsDebug) {
            Log.w(tag, log);
        }
    }

    public static void json(String tag, String message) {
        if (!mIsDebug) {
            return;
        }

        String json;
        try {
            if (message.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(message);
                json = jsonObject.toString(KJsonIndent);
            } else if (message.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(message);
                json = jsonArray.toString(KJsonIndent);
            } else {
                json = message;
            }
        } catch (JSONException e) {
            json = message;
        }

        Log.d(tag, json);
    }
}
