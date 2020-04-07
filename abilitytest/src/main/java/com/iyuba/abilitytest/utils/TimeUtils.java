package com.iyuba.abilitytest.utils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by iyuba on 2019/1/14.
 */

public class TimeUtils {
    public static long getDays() {
        //东八区;
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.set(1970, 0, 1, 0, 0, 0);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        long intervalMilli = now.getTimeInMillis() - cal.getTimeInMillis();
        long xcts = intervalMilli / (24 * 60 * 60 * 1000);
        return xcts;
    }
}
