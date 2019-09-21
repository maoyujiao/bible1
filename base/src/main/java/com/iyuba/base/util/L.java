package com.iyuba.base.util;

import android.util.Log;

import java.util.Locale;

/**
 * L
 *
 * @author wayne
 * @date 2017/11/18
 */
public class L {
    private static boolean isShow = true;

    public static void setEnable(boolean show) {
        L.isShow = show;
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "LogUtil:%s(%s.java:%d) %s";
        String callerClazzName = caller.getClassName();

        String[] classNameInfo = callerClazzName.split("\\.");
        if (classNameInfo.length > 0) {
            callerClazzName = classNameInfo[classNameInfo.length - 1];
        }
        if (callerClazzName.contains("$")) {
            callerClazzName = callerClazzName.split("\\$")[0];
        }

        tag = String.format(Locale.CHINA, tag, caller.getMethodName(), callerClazzName,
                caller.getLineNumber(), Thread.currentThread().getName());
        return tag;
    }

    public static void e(String msg) {
        if (isShow) {
            Log.e(generateTag(), msg);
        }
    }

    public static void w(String msg) {
        if (isShow) {
            Log.w(generateTag(), msg);
        }
    }

    public static void d(String msg) {
        if (isShow) {
            Log.d(generateTag(), msg);
        }
    }
}
