package com.iyuba.trainingcamp.app;

import android.content.Context;

import com.iyuba.trainingcamp.utils.Constants;
import com.iyuba.trainingcamp.utils.SP;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.app
 * @class describe
 * @time 2018/7/18 15:59
 * @change
 * @chang time
 * @class describe
 */
public class GoldApp
 {

    private static GoldApp app;
    public static Context mContext;
    public String userId;
    public String LessonType;
    public String userName;
    public Boolean isVip;
    public String appId ;
    public String productId ;

    public static synchronized GoldApp getApp(Context context) {
        mContext = context;
        if (app == null) {
            app = new GoldApp();
        }
        return app;

    }



    public void initTheme(int theme){
        SP.put(mContext,"theme",theme);
    }

    public void init(String userId, String Username, String LessonType , String productid , String appId) {
        this.userId = userId;
        this.userName = Username;
        this.LessonType = LessonType;
        this.appId = appId;
        this.productId = productid;

        SP.put(mContext, "uid", userId);
        SP.put(mContext, "username", userName);
        SP.put(mContext, "lessontype", LessonType);
        SP.put(mContext, "appId", appId);
        SP.put(mContext, "productid", productid);

        Constants.TYPE = LessonType;
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(mContext.getApplicationContext());
//        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    public Context getmContext() {
        return mContext;
    }

    public String getUserId() {
        return (String) SP.get(mContext, "uid", "");
    }

    public String getLessonType() {
        return (String) SP.get(mContext, "lessontype", "");
    }

    public String getUserName() {
        return (String) SP.get(mContext, "username", "");
    }

    public boolean getVipStatus() {
        return isVip;
    }

    public void setVipStatus(Boolean isVip) {
        this.isVip = isVip;
    }

    private GoldApp() {
    }
}
