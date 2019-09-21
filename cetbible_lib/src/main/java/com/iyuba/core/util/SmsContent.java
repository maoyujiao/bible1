/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.util;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.iyuba.configation.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsContent extends ContentObserver {

    public static final String SMS_URI_INBOX = "content://sms/inbox";
    private Activity activity = null;
    private Handler handler;

    public SmsContent(Activity activity, Handler handler) {
        super(handler);
        this.activity = activity;
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = null;// 光标
        try {
            cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[]{
                            "_id", "address", "body", "read"}, "read=?",
                    new String[]{"0"}, "date desc");
            if (cursor != null) {// 如果短信为未读模式
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    String smsbody = cursor
                            .getString(cursor.getColumnIndex("body"));
                    if (smsbody.contains(Constant.APPName)) {
                        String regEx = "[^0-9]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(smsbody.toString());
                        handler.obtainMessage(0, m.replaceAll("").trim().toString())
                                .sendToTarget();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}