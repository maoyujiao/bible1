/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.listener;

import android.content.Context;
import android.content.Intent;

/**
 * 针对操作完成 需intent的回调函数
 *
 * @author 陈彤
 */
public interface IntentCallBack {
    void onReceived(Context context, Intent intent);
}
