package com.iyuba.core.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.listener.IntentCallBack;

/**
 * 自定义的广播操作相关类，
 *
 * @author 陈彤
 */
public class OrderBroadcast {
    public static Context mContext = RuntimeManager.getContext();
    // 一下为自定义广播的ACTION值
    public static String ACTION_GO_TO_SLEEP = "";

    /**
     * 发送普通广播
     *
     * @param action action的名字
     */
    public static void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        mContext.sendBroadcast(intent);
    }

    /**
     * 发送有序广播
     *
     * @param action             action的名字
     * @param receiverPermission (optional) String naming a permissions that a receiver must
     *                           hold in order to receive your broadcast. If null, no
     *                           permission is required.
     */
    public static void sendOrderddBroadcast(String action,
                                            String receiverPermission) {
        Intent intent = new Intent(action);
        mContext.sendOrderedBroadcast(intent, receiverPermission);
    }

    /**
     * 生成并注册一个广播接收器，并返回该接收器
     *
     * @param action           注册的广播的过滤器action的名字
     * @param callBackReceiver 自定义的接口，其中的onReceived(context,
     *                         intent)方法中需要实现在一般广播接收器当中所要做的onreceive动作。
     * @return 被注册的此接收器
     */
    public static BroadcastReceiver buildReceiver(
            final IntentCallBack callBackReceiver, String... action) {

        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                callBackReceiver.onReceived(context, intent);
            }
        };
        IntentFilter mIntentFilter = new IntentFilter();
        for (String string : action) {
            mIntentFilter.addAction(string);
        }
        mContext.registerReceiver(mBroadcastReceiver, mIntentFilter);
        return mBroadcastReceiver;

    }

    public static BroadcastReceiver buildReceiver(Context context,
                                                  final IntentCallBack callBackReceiver, String... action) {

        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                callBackReceiver.onReceived(context, intent);
            }
        };
        IntentFilter mIntentFilter = new IntentFilter();
        for (String string : action) {
            mIntentFilter.addAction(string);
        }
        context.registerReceiver(mBroadcastReceiver, mIntentFilter);
        return mBroadcastReceiver;
    }
}
