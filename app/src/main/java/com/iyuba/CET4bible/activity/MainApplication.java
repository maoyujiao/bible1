package com.iyuba.CET4bible.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.iyuba.core.activity.CrashApplication;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.CET4bible.activity
 * @class describe
 * @time 2018/11/1 15:19
 * @change
 * @chang time
 * @class describe
 */
public class MainApplication extends CrashApplication {

    private long quitTime ,backTime;
    private int mCount ;
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCount ++ ;
                backTime = System.currentTimeMillis();
                if (mCount == 1){
                    if (backTime - quitTime > 180*1000){
                        Intent intent = new Intent(getContext(),Welcome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("is_resume",true);
                        activity.startActivity(intent);
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount -- ;
                if (mCount == 0){
                    quitTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

}
