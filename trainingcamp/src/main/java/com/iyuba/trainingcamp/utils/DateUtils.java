package com.iyuba.trainingcamp.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.utils
 * @class describe
 * @time 2018/8/17 11:52
 * @change
 * @chang time
 * @class describe
 */
public class DateUtils {
    //获取今天的日期 为"YYYY-MM-DD"格式
    public static String getToday(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
//        long s = System.currentTimeMillis(); // java.util.Date
        String today = format.format(curDate);
        Log.d("diao", "getToday: "+today);
        return today;
    }
}
