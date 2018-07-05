package com.example.camerasample;

import android.util.Log;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

public class LogUtils {
    private static final String TAG = LogUtils.class.getSimpleName();
    private static boolean DEBUG = true;
    private static String BASEDIR = "/sdcard/log/";
    
    public static void init(boolean debugAble,String baseDir){
        DEBUG = debugAble;
        BASEDIR = baseDir;
    }

    public static boolean isDebug(){
        return DEBUG;
    }


    public static void log(String tag, Object... objects) {
        if (objects == null || objects.length == 0) {
            return;
        }
        if (tag == null) {
            tag = "log";
        }

        StringBuilder builder = new StringBuilder();
        for (Object obj : objects) {
            if (obj == null) {
                builder.append("null");
            } else if (obj instanceof Class) {
                builder.append(((Class) obj).getSimpleName());
            } else {
                builder.append(obj.toString());
            }
            builder.append(", ");
        }

        Log.i(tag, builder.toString());
    }

    public static void log(Object... objects) {
        log("toffee", objects);
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String str_tag, Object... strs) {
        if (!DEBUG) return;
        StringBuilder sb = new StringBuilder();
        for (Object strObj : strs) {
            sb.append(String.valueOf(strObj));
        }
        Log.d(str_tag, sb.toString());
    }

    public static void i(String str_tag, Object... strs) {
        if (!DEBUG) return;
        StringBuilder sb = new StringBuilder();
        for (Object strObj : strs) {
            sb.append(String.valueOf(strObj));
        }
        Log.d(str_tag, sb.toString());
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void logEx(Throwable e) {
        e(TAG, Log.getStackTraceString(e));
    }

    public static void logEx(String msg, Throwable e) {
        e(TAG, msg + "\n" + Log.getStackTraceString(e));
    }

    public static void e(String tag, String msg,Throwable e) {
        if (DEBUG) {
            Log.e(tag, msg, e);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }


    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg,Throwable e) {
        if (DEBUG) {
            Log.w(tag, msg, e);
        }
    }


    public static void e(String content, Throwable tr) {
        if (DEBUG) {
            String tag = LogUtils.class.getSimpleName();
            Log.e(tag, content, tr);
        }
    }

    public static void printStackTrace(Throwable e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }

    private final static Hashtable<String, Long> TABLE = new Hashtable<>();

//    public static void startTimer(String key) {
//        TABLE.put(key + ThreadUtils.getCurThreadId(), System.currentTimeMillis());
//    }
//
//    public static long endTimer(String key) {
//        Long l = TABLE.remove(key + ThreadUtils.getCurThreadId());
//        if (l == null) {
//            return Long.MAX_VALUE;
//        }
//        return System.currentTimeMillis() - l;
//    }
//
//    public static void endTimerP(String tag, String key) {
//        long r = endTimer(key);
//        d(tag, String.valueOf(r));
//    }

    public static void writeErrFile(Exception e) {
        logEx(e);
        writeErrFile(Log.getStackTraceString(e));
    }

    public static void writeErrFile(String message) {
        if (!DEBUG) {
            return;
        }

        String fileName = BASEDIR + "err-" + currentDate() + ".dat";
        LogUtils.d(TAG, "writeErrFile " + fileName);
        message = "\n" + currentDateTime() + " " + message + "\n";
        try {
            FileOutputStream fout = new FileOutputStream(fileName, true);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
            LogUtils.i(TAG, "writeErrFile success");
        } catch (Exception e) {
            LogUtils.e(TAG, "writeErrFile fail\n" + Log.getStackTraceString(e));
        }
    }

    public static String currentDateTime() {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.getDefault());
        return dft.format(new Date());
    }

    public static String currentDate() {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dft.format(new Date());
    }

    public static void logCurrentThread(String msg, Thread thread) {
        d(TAG, msg + " currentThread id:" + thread.getId()
                + ", name:" + thread.getName() + ", priority:" + thread.getPriority() + ", state:" + thread.getState());
    }


    public static String appendRequestResult(String requestStr, String responseStr, int errno, String errMsg) {
        StringBuffer stringBuffer = new StringBuffer(requestStr);
        stringBuffer.append("\n");
        stringBuffer.append("result:");
        if (errno == 0) {
            stringBuffer.append("OK");
            stringBuffer.append("\n");
            stringBuffer.append(responseStr);
        } else {
            stringBuffer.append("ERROR");
            stringBuffer.append("\n");
            stringBuffer.append("errno:" + errno);
            stringBuffer.append("\n");
            stringBuffer.append("errMsg:" + errMsg);
        }
        return stringBuffer.toString();

    }


}
