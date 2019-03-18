package com.molidt.baby.touch.utils;

import android.util.Log;

import com.molidt.baby.touch.BuildConfig;

/**
 * Created by Jianan on 2019/3/18.
 */
public class LogUtil {

    private static final String TAG = "BABY TOUCH";

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
