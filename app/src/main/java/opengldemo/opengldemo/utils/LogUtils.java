package opengldemo.opengldemo.utils;

import android.util.Log;

/**
 * @desc ${TODD}
 */

public class LogUtils {
    public static boolean ALLOW_LOG = true;

    public static void v(String msg) {
        if (ALLOW_LOG) {
            Log.v("shan", msg);
        }
    }

    public static void e(String msg) {
        if (ALLOW_LOG) {
            Log.e("shan", msg);
        }
    }

    public static void d(String msg) {
        if (ALLOW_LOG) {
            Log.d("shan", msg);
        }
    }

    public static void i(String msg) {
        if (ALLOW_LOG) {
            Log.i("shan", msg);
        }
    }

    public static void w(String msg) {
        if (ALLOW_LOG) {
            Log.w("shan", msg);
        }
    }

    public static void wtf(String msg) {
        if (ALLOW_LOG) {
            Log.wtf("shan", msg);
        }
    }
}
