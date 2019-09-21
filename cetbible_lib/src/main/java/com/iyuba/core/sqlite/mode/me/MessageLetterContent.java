package com.iyuba.core.sqlite.mode.me;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;

/**
 * 私信内容
 *
 * @author 陈彤
 */
public class MessageLetterContent {

    public static final int MESSAGE_FROM = 0;
    public static final int MESSAGE_TO = 1;
    public String message;// 私信id，设置未读私信为已读需要的参数
    public String pmid;// 私信内容id，当删除私信的时候需要的参数
    public String authorid;// 若与url的id相同则为发送，否则为接收
    public String dateline;// 1362915420
    public SoftReference<Bitmap> userBitmap;
    public int direction = 0;

    public MessageLetterContent() {
        // setMessage(message);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public SoftReference<Bitmap> getUserBitmap() {
        return userBitmap;
    }

    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = new SoftReference<Bitmap>(userBitmap);
        userBitmap = null;
    }

}
