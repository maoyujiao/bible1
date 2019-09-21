package com.iyuba.abilitytest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuzhenli on 2017/9/17.
 */

public class TimeFormatUtil {
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }
}
