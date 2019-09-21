/**
 *
 */
package com.iyuba.core.sqlite.mode.me;

import android.graphics.Bitmap;

/**
 * 私信列表
 *
 * @author 陈彤
 */
public class MessageLetter {

    public String friendid; // 对方id
    public int pmnum;// 当前互发私信数
    public String lastmessage;// 最后一条私信内容
    public String name;// 对方name
    public String plid; // 私信id，设置未读私信为已读需要的参数
    public String dateline;// 最后一条私信发送时间
    public String isnew; // 1代表未读 0代表已读
    public Bitmap userBitmap;// 头像
}
