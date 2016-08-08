package com.mumu.projectli.utility;

/**
 * Log saves logs to local buffer file and also outputs to logcat
 */

public final class Log {
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;

    public static void v(String tag, String msg) {
        android.util.Log.v(tag, msg);
        saveLogToFile(tag, msg, VERBOSE);
    }

    public static void d(String tag, String msg) {
        android.util.Log.d(tag, msg);
        saveLogToFile(tag, msg, DEBUG);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
        saveLogToFile(tag, msg, ERROR);
    }

    public static void i(String tag, String msg) {
        android.util.Log.i(tag, msg);
        saveLogToFile(tag, msg, INFO);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
        saveLogToFile(tag, msg, WARN);
    }

    private static void saveLogToFile(String tag, String msg, int level) {

    }

}
