package com.iyuba.core.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.Login;
import com.iyuba.core.bean.NewUserInfo;
import com.iyuba.core.bean.TouristIdBean;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.http.HttpConstant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.sqlite.mode.UserInfo;
import com.iyuba.core.widget.dialog.CustomToast;

import java.util.Date;

import okhttp3.Call;


/**
 * 临时账户
 */
public class TouristUtil {
    private Context context;

    public TouristUtil(Context context) {
        this.context = context;
    }

    /**
     * 是否为临时账户
     */
    public static boolean isTourist() {
        return ConfigManager.Instance().loadBoolean("isTourist");
    }

    /**
     * 设置当前为临时账户
     */
    public static void setTourist(boolean value) {
        ConfigManager.Instance().putBoolean("isTourist", value);
    }

    /**
     * 是否为临时账户退出
     */
    public static boolean isTouristLogout() {
        return ConfigManager.Instance().loadBoolean("isTouristLogout");
    }

    /**
     * 退出后，不再显示临时账户
     */
    public static void setTouristLogout(boolean value) {
        ConfigManager.Instance().putBoolean("isTouristLogout", value);
    }

    public static void loadUserInfo(Context context) {
        try {
            String info = ConfigManager.Instance().loadString("touristUserInfo");
            UserInfo userInfo = new Gson().fromJson(info, UserInfo.class);
            AccountManager.Instace(context).setUserInfo(userInfo);
            AccountManager.Instace(context).userId = userInfo.uid;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUserInfo(UserInfo userInfo) {
        ConfigManager.Instance().putString("touristUserInfo", new Gson().toJson(userInfo));
    }

    public static void showTouristInfoEditHint(Context mContext) {
        CustomToast.showToast(mContext, mContext.getString(R.string.no_account_hint));
        toLogin(mContext);
    }

    public static void showTouristHint(Context mContext) {
        CustomToast.showToast(mContext, mContext.getString(R.string.no_account_hint2));
        toLogin(mContext);
    }

    private static void toLogin(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Login.class);
        context.startActivity(intent);
    }

    public static void clearTouristInfo() {
        ConfigManager.Instance().putString("touristUserInfo", null);
        setTourist(false);
    }

    public void getUID() {
        //东八区的天数
        long unixTimestamp = new Date().getTime() / 1000 + 3600 * 8; //东八区;
        long days = unixTimestamp / 86400;

        String deviceId = Build.SERIAL;
        String sign = MD5.getMD5ofStr(deviceId + Constant.APPID + HttpConstant.ANDROID + days + "iyubaV2");

        String url = HttpConstant.BASE_URL + "?protocol=11003" +
                "&deviceId=" + deviceId + HttpConstant.PLATFORM_ANDROID +
                "&appid=" + Constant.APPID + HttpConstant.FORMAT_JSON +
                "&sign=" + sign;

        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                TouristIdBean touristBean = null;
                try {
                    touristBean = new Gson().fromJson(response, TouristIdBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    return;
                }
                //在这里面判断一下是否有网络如果有就用网络获取的uid,如果没有,就自己设置
                getUserInfo(touristBean.getUid());
            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtils.e("HTTP REQUEST ERROR : " + e.getMessage());
            }
        });
    }

    private void getUserInfo(final int uid) {
        final String sign = MD5.getMD5ofStr("20001" + uid + "iyubaV2");
        String URL = HttpConstant.BASE_URL +
                "?platform=" + HttpConstant.ANDROID +
                "&format=" + "json" +
                "&protocol=20001&id=" + uid +
                "&myid=" + uid +
                "&appid=" + Constant.APPID +
                "&sign=" + sign;

        Http.get(URL, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                NewUserInfo newUserInfo = null;
                try {
                    newUserInfo = new Gson().fromJson(response, NewUserInfo.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    return;
                }
                final UserInfo userInfo = new UserInfo();
                // userInfo.expireTime = newUserInfo.expireTime;
                userInfo.follower = newUserInfo.follower;
                userInfo.username = newUserInfo.username;
                userInfo.vipStatus = newUserInfo.vipStatus;
                userInfo.text = newUserInfo.text;
                userInfo.following = newUserInfo.following;
                userInfo.notification = newUserInfo.notification;
                userInfo.icoins = newUserInfo.icoins;
                userInfo.studytime = newUserInfo.studytime;
                userInfo.position = newUserInfo.position;
                userInfo.uid = uid + "";

                //如果为true表示是临时用户
                setTourist(true);
                AccountManager.Instace(context).setUserInfo(userInfo);
                AccountManager.Instace(context).userId = userInfo.uid;

            }

            @Override
            public void onError(Call call, Exception e) {
                LogUtils.e("HTTP REQUEST ERROR : " + e.getMessage());
            }
        });
    }
}
