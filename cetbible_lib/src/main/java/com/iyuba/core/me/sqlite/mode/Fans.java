package com.iyuba.core.me.sqlite.mode;

import android.graphics.Bitmap;

/**
 * 粉丝
 *
 * @author 陈彤
 */
public class Fans {

    public String uid;// 我关注的uid
    public String username;// 我关注的用户名
    public String bkname;// 我关注着的备注(当互相关注时显示)
    public String dateline;// 添加关注的时间，系统秒数
    public String mutual;// 是否互相关注，1为是
    public Bitmap userBitmap;
    public String doing;
    public String isnew;
}
