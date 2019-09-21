package com.iyuba.core.sqlite;

import android.content.Context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User {
    public String userName;//用户名
    public String userId;//用户id
    public String userImg; // 用户头像
    public String userPwd; // 用户密码
    public String email;//用户邮箱
    public String amount;// 爱语币
    public int isvip;//是否是vip
    public String deadline = "";//vip截止日期
    public String createDate = ""; //vip创建时间
    private String validity;// 有效期

    public User() {// 新建user

    }

    public User(String s) {// 登录后user
        userName = s;
        isvip = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        createDate = sdf.format(getNetTime());
        deadline = createDate;
    }

    public User(String s, int type, Context mContext) {// 首次购买user，或有效期已过
        userName = s;
        isvip = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        createDate = sdf.format(getNetTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getNetTime());
        switch (type) {
            case 1:
                calendar.add(Calendar.DATE, 90);
                deadline = sdf.format(calendar.getTime());
                break;
            case 2:
                calendar.add(Calendar.DATE, 180);
                deadline = sdf.format(calendar.getTime());
                break;
            case 3:
                calendar.add(Calendar.YEAR, 1);
                deadline = sdf.format(calendar.getTime());
                break;
            case 4:
                deadline = "终身VIP";
                break;
            default:
                deadline = sdf.format(getNetTime());
                break;
        }
    }

    public User(String s, int type, String createDate, Context mContext) {// 卸载后重装user
        userName = s;
        isvip = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.createDate = createDate;
        Date tempDate = null;
        try {
            tempDate = sdf.parse(createDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);
        switch (type) {
            case 1:
                calendar.add(Calendar.DATE, 90);
                deadline = sdf.format(calendar.getTime());
                break;
            case 2:
                calendar.add(Calendar.DATE, 180);
                deadline = sdf.format(calendar.getTime());
                break;
            case 3:
                calendar.add(Calendar.YEAR, 1);
                deadline = sdf.format(calendar.getTime());
                break;
            case 4:
                deadline = "终身VIP";
                break;
            default:
                deadline = sdf.format(getNetTime());
                break;
        }
    }

    public User(String name, String deadlineDate, Context mContext) {// vip user
        userName = name;
        isvip = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        createDate = sdf.format(getNetTime());
        deadline = deadlineDate;
    }

    public static void main(String[] args) {

    }

    public Date getNetTime() {// 获取网络时间
        URL url;
        try {
            url = new URL("http://www.bjtime.cn");
            URLConnection uc = url.openConnection();
            uc.connect(); // 发出连接
            long ld = uc.getDate(); // 取得网站日期时间
            Date date = new Date(ld); // 转换为标准时间对象
            return date;
        } catch (MalformedURLException e) {
            return new Date();
        } catch (IOException e) {
            return new Date();
        }
    }
}
