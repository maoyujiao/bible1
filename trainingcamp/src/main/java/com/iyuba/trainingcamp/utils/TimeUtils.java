package com.iyuba.trainingcamp.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.utils
 * @class describe
 * @time 2018/7/18 15:54
 * @change
 * @chang time
 * @class describe
 */
public class TimeUtils {

    public static String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }

    public static String getFormateDate(Long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(time);
    }

    public static int getMonthMax(String pointerdate) {
        String datep = pointerdate ;
        Log.d("diao", "getMonthMax: "+datep);
        switch (Integer.parseInt(datep.substring(5, 7))) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (isLeapYear(datep.substring(0,4))) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return  0;
        }
    }

    private static boolean isLeapYear(String substring) {
        int year = Integer.parseInt(substring);
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static String formatTime(int time) {
        int sec = time / 1000 % 60;
        int min = time / 1000 / 60;
        if (sec < 10) {
            if (min < 10) {
                return "0" + String.valueOf(min) + ":" + "0" + String.valueOf(sec);
            } else {
                return String.valueOf(min) + ":" + "0" + String.valueOf(sec);
            }
        } else {
            if (min < 10) {
                return "0" + String.valueOf(min) + ":" + String.valueOf(sec);
            } else {
                return String.valueOf(min) + ":" + String.valueOf(sec);
            }
        }
    }

    public static String formatDateToMills(String in){
        String s = "";
        DateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
        try {
            s = format.parse(in).getTime()+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  s ;
    }
}
