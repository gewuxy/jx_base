package lib.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class NetworkLog {

    private static final String TAG = NetworkLog.class.getSimpleName();

    private static final int KJsonIndent = 4;
    private static final String KSeparate = "=========";
    private static final boolean mIsDebug = true;

    private NetworkLog() {
    }

    public static boolean isDebug() {
        return mIsDebug;
    }

    public static void d(String msg) {
        if (mIsDebug) {
            printJson(msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (mIsDebug) {
            Log.e(TAG, msg, tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (mIsDebug) {
            Log.e(tag, msg, tr);
        }
    }

    public static void e(Throwable e) {
        if (mIsDebug) {
            Log.e(TAG, KSeparate + e.getClass().getSimpleName() + KSeparate, e);
        }
    }

    private static void printJson(String message) {
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

        Log.d(TAG, json);
    }
}
