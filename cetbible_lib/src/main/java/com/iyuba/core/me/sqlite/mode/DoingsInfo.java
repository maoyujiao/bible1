/**
 *
 */
package com.iyuba.core.me.sqlite.mode;

import android.graphics.Bitmap;

/**
 * 心情
 *
 * @author 陈彤
 */
public class DoingsInfo {

    public String doid;// 心情状态id
    public String from;// 发表来源(目前为空)
    public String dateline;// 发表时间，为系统秒数
    public String message;// (内层) 心情内容
    public String ip;// 发布时的ip
    public String replynum;// 回复数
    public String username;
    public String uid;
    public Bitmap userBitmap;

}
