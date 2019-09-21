package com.iyuba.CET4bible.util;

import com.iyuba.CET4bible.BuildConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static int day;
    private static SimpleDateFormat sdf;

    /**
     * 获得当前时间
     *
     * @return
     */
    public static String getCurrentDate() {

        Date date = new Date(System.currentTimeMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(date);
        return currentDate;

    }

    /**
     * 获得四级考试时间
     *
     * @return
     */
    public static String getTestDate() {
        return BuildConfig.isEnglish ? getENTestDate() : getJPTestDate();
    }

    /**
     * 日语考试日期
     * 7月12月的第一个周日
     */
//    private static String getJPTestDate() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance(Locale.CHINA);
//        calendar.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
//
////        calendar.set(Calendar.MONTH, 11);
////        calendar.set(Calendar.DATE, 4);
//
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int today = calendar.get(Calendar.DAY_OF_MONTH);
//
//        calendar.set(Calendar.DATE, 1);
//        calendar.get(Calendar.DATE);
//
//        if (month < 7) {
//            calendar.set(Calendar.MONTH, 6);
//        } else if (month > 7 && month < 12) {
//            calendar.set(Calendar.MONTH, 11);
//        } else if (month == 7) {
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//日子设为星期天
//            int testDay = calendar.get(Calendar.DAY_OF_MONTH);
//            if (today <= testDay) { //
//                calendar.set(Calendar.MONTH, 6);
//            } else {
//                calendar.set(Calendar.MONTH, 11);
//            }
//        } else if (month == 12) {
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//日子设为星期天
//            int testDay = calendar.get(Calendar.DAY_OF_MONTH);
//            if (today <= testDay) { //
//                calendar.set(Calendar.MONTH, 11);
//            } else {
//                calendar.add(Calendar.YEAR, 1);
//                calendar.set(Calendar.MONTH, 6);
//            }
//        }
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//日子设为星期天
//
//        try {
//            return simpleDateFormat.format(calendar.getTime());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }


    private static String getJPTestDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        //设置year month
        if(calendar.get(Calendar.MONTH) < 6){
            calendar.set(Calendar.MONTH,6);
        } else if(calendar.get(Calendar.MONTH) > 6 && calendar.get(Calendar.MONTH) < 11){
            calendar.set(Calendar.MONTH,11);
        } else if(calendar.get(Calendar.MONTH) == 11){
            int i = 1;
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){//获取第一个星期日
                calendar.set(Calendar.DAY_OF_MONTH, ++i);
            }
            if(today > i){
                calendar.add(Calendar.YEAR, 1);
                calendar.set(Calendar.MONTH, 6);
            }else {
                calendar.set(Calendar.MONTH, 11);
            }
        } else if(calendar.get(Calendar.MONTH) == 6){
            int i = 1;
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                calendar.set(Calendar.DAY_OF_MONTH, ++i);//获取第一个星期日
            }
            if(today > i){
                calendar.set(Calendar.MONTH,11);
            }else {
                calendar.set(Calendar.MONTH, 6);
            }
        }
        //设置day
        int examDay = 1;
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
            calendar.set(Calendar.DAY_OF_MONTH, ++examDay);//找到第一个星期天
        }
        return simpleDateFormat.format(calendar.getTime());
    }

    private static String getENTestDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天

//        calendar.set(Calendar.MONTH, 12);
//        calendar.set(Calendar.DATE, 3);

        int month = calendar.get(Calendar.MONTH) + 1;
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DATE, 1);
        calendar.get(Calendar.DATE);

        if (month < 6) {
            calendar.set(Calendar.MONTH, 5);

        } else if (month > 6 && month < 12) {
            calendar.set(Calendar.MONTH, 11);

        } else if (month == 6) {
            calendar.set(Calendar.DATE, 1);
            calendar.get(Calendar.DATE);

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);//日子设为星期天
            calendar.add(Calendar.DATE, 14);

            calendar.get(Calendar.DATE);
            int testDay = calendar.get(Calendar.DAY_OF_MONTH);
            if (today <= testDay) { //
                calendar.set(Calendar.MONTH, 5);

                calendar.set(Calendar.DATE, 1);
                calendar.get(Calendar.DATE);
            } else {
                calendar.set(Calendar.MONTH, 11);

                calendar.set(Calendar.DATE, 1);
                calendar.get(Calendar.DATE);
            }

        } else if (month == 12) {
            calendar.set(Calendar.DATE, 1);
            calendar.get(Calendar.DATE);

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);//日子设为星期天
            calendar.add(Calendar.DATE, 14);

            calendar.get(Calendar.DATE);
            int testDay = calendar.get(Calendar.DAY_OF_MONTH);
            if (today <= testDay) { //
                calendar.set(Calendar.MONTH, 11);

                calendar.set(Calendar.DATE, 1);
                calendar.get(Calendar.DATE);

            } else {
                calendar.add(Calendar.YEAR, 1);
                calendar.set(Calendar.MONTH, 5);

                calendar.set(Calendar.DATE, 1);
                calendar.get(Calendar.DATE);
            }
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);//日子设为星期天
        calendar.add(Calendar.DATE, 14);
        calendar.get(Calendar.DATE);


        try {
            System.out.println(simpleDateFormat.format(calendar.getTime()));
            return simpleDateFormat.format(calendar.getTime())+"Days";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获得当前时间距离考试时间还有多久
     *
     * @return
     */
    public static String getDuration() {
        return BuildConfig.isEnglish ? getENDuration() : getJPDuration();
    }

    private static String getENDuration() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
        String dstr = getENTestDate();
        Date date;
        try {
            date = sdf.parse(dstr);
            long s1 = date.getTime();//将时间转为毫秒
            long s2 = getCurrentTime();//得到当前的毫秒
            day = (int) ((s1 - s2) / 1000 / 60 / 60 / 24);
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
        return String.valueOf(day);
    }

    private static String getJPDuration() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dstr = getJPTestDate();
        Date date;
        try {
            date = sdf.parse(dstr);
            long s1 = date.getTime();//将时间转为毫秒
            long s2 = getCurrentTime();//得到当前的毫秒
            day = (int) ((s1 - s2) / 1000 / 60 / 60 / 24);

        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
        return String.valueOf(day);
    }

    private static long getCurrentTime() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
        String date = sdf.format(new Date(System.currentTimeMillis()));

        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
