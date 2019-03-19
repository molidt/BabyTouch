package com.molidt.baby.touch.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * create by Jianan at 2019-03-19
 **/
public class TimeUtil {

    private static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    public static String formatTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return sdfTime.format(calendar.getTime());
    }
}
