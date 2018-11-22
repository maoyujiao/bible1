package com.iyuba.CET4bible.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flurry.android.FlurryAgent;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.ImportDatabase;
import com.iyuba.CET4bible.util.AdSplashUtil;
import com.iyuba.base.util.BrandUtil;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Web;
import com.iyuba.core.listener.ActionFinishCallBack;
import com.iyuba.core.me.sqlite.ZDBManager;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.ImportLibDatabase;
import com.iyuba.core.util.CopyFileToSD;
import com.iyuba.core.util.ReadBitmap;
import com.iyuba.core.util.SaveImage;
import com.umeng.analytics.MobclickAgent;

//import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

//import tv.lycam.gift.runner.EngineRunner;
import tv.lycam.mqtt.Mqtt;
import tv.lycam.mqtt.MqttService;

/**
 * 起始界面Activity
 *
 * @author chentong
 */
public class Welcome extends BasisActivity {
    private Context mContext;
    private boolean isNewUser = false;
    Thread copyThread = new Thread(new Runnable() {

        @Override
        public void run() {
            new CopyFileToSD(mContext, "writting", Constant.picSrcAddr,
                    new ActionFinishCallBack() {

                        @Override
                        public void onReceived() {
                            handler.removeMessages(1);
                            handler.removeMessages(3); // 5秒跳转
                            handler.sendEmptyMessage(0);
                        }
                    });
        }
    });
    private int lastVersion, currentVersion;
    private ImageView ad, base;
    private Button btn_skip;
    private int recLen = 5;
    private Timer timer = new Timer();
    /**
     * 0。 引导夜
     * 1。 首页
     * 3。 5秒后首页
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent;
            switch (msg.what) {
                case 0:
                    intent = new Intent(Welcome.this, HelpUse.class);
                    intent.putExtra("source", "welcome");
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    intent = new Intent(Welcome.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    copyThread.start();
                    break;
                case 3:
                    btn_skip.setText("跳过(" + recLen + "s)");
                    if (recLen < 1) {
                        timer.cancel();
                        if (!isNewUser) {
                            startMainActivity();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            recLen--;
            handler.sendEmptyMessage(3);

        }
    };

    private AdSplashUtil adSplashUtil;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        CrashApplication.getInstance().addActivity(this);
        FlurryAgent.onStartSession(this, "M9ZZXM95GV92XJC5B2ZQ");

//        initLiving();

        btn_skip = findViewById(R.id.btn_skip);
        btn_skip.setVisibility(View.INVISIBLE);
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                handler.sendEmptyMessage(1);
                handler.removeMessages(3);
            }
        });
        ad = findViewById(R.id.ad);
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adSplashUtil.isRequestEnd() && !ConfigManager.Instance().loadString("startuppic_Url").equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, Web.class);
                    intent.putExtra("url", ConfigManager.Instance().loadString("startuppic_Url"));
                    Log.e("InitActivity url ", ConfigManager.Instance().loadString("startuppic_Url"));
                    handler.removeMessages(1);
                    startActivityForResult(intent, 0);
                    timer.cancel();
                }
            }
        });
        base = findViewById(R.id.base);
        base.setImageBitmap(ReadBitmap.readBitmap(mContext, R.drawable.base));
        if (!Constant.APP_CONSTANT.isEnglish()) {
            String type = Constant.APP_CONSTANT.TYPE();
            if ("1".equals(type)) {
                base.setImageBitmap(ReadBitmap.readBitmap(mContext, R.drawable.base));
            } else if ("2".equals(type)) {
                base.setImageBitmap(ReadBitmap.readBitmap(mContext, R.drawable.base2));
            } else {
                base.setImageBitmap(ReadBitmap.readBitmap(mContext, R.drawable.base3));
            }
        }

        adSplashUtil = new AdSplashUtil(mContext, ad);
        adSplashUtil.setCallback(new AdSplashUtil.SCallback() {
            @Override
            public void loadLocal() {
                loadLocalAd();
            }

            @Override
            public void onAdClick() {
                handler.removeMessages(1);
                handler.removeMessages(3);
                timer.cancel();
            }

            @Override
            public void startTimer() {
                try {
                    btn_skip.setVisibility(View.VISIBLE);
                    timer.schedule(timerTask, 1000, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        adSplashUtil.requestAd();

        BrandUtil.requestQQGroupNumber(mContext);

        saveIcon();

        try {
            lastVersion = ConfigManager.Instance().loadInt("version");
            currentVersion = getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            lastVersion = 0;
            e.printStackTrace();
        }
        if (lastVersion == 0)// 新用户
        {
            isNewUser = true;
            ImportDatabase db = new ImportDatabase(mContext); // cet4 、cet6
            db.setPackageName(mContext.getPackageName());
            db.setVersion(Constant.APP_CONSTANT.cetDatabaseLastVersion(), Constant.APP_CONSTANT.cetDatabaseCurVersion());// 有需要数据库更改使用
            db.openDatabase(db.getDBPath());

            ImportLibDatabase libdb = new ImportLibDatabase(mContext); // lib_database
            libdb.setPackageName(mContext.getPackageName());
            libdb.setVersion(6, 7);// 有需要数据库更改使用
            libdb.openDatabase(libdb.getDBPath());

            SettingConfig.Instance().setSyncho(true);
            ConfigManager.Instance().putInt("version", currentVersion);
            ConfigManager.Instance().putBoolean("firstuse", true);
            ConfigManager.Instance().putInt("mode", 1);
            ConfigManager.Instance().putInt("type", 2);
            ConfigManager.Instance().putInt("applanguage", 0);
            ConfigManager.Instance().putInt("isvip", 0);
            ConfigManager.Instance().putString("updateAD", "1970-01-01");
            ConfigManager.Instance().putBoolean("push", true);
            ConfigManager.Instance().putBoolean("saying", true);
            ConfigManager.Instance().putString("wordsort", "0-0");
            ConfigManager.Instance().putString("vocabulary", "study");
            SettingConfig.Instance().setHighSpeed(false);
            SettingConfig.Instance().setSyncho(true);
            SettingConfig.Instance().setLight(true);
            SettingConfig.Instance().setAutoPlay(false);
            SettingConfig.Instance().setAutoStop(true);

            ZDBManager zdbManager = new ZDBManager(mContext); // zzaidb
            zdbManager.setVersion(0, 1);
            zdbManager.openDatabase();
            handler.sendEmptyMessage(2);


        } else if (currentVersion > lastVersion) {
            ImportDatabase db = new ImportDatabase(mContext);
            db.setPackageName(this.getPackageName());
            db.setVersion(Constant.APP_CONSTANT.cetDatabaseLastVersion(), Constant.APP_CONSTANT.cetDatabaseCurVersion());// 有需要数据库更改使用
            db.openDatabase(db.getDBPath());

            ImportLibDatabase libdb = new ImportLibDatabase(mContext);
            libdb.setPackageName(this.getPackageName());
            libdb.setVersion(6, 7);// 有需要数据库更改使用
            libdb.openDatabase(libdb.getDBPath());

            ConfigManager.Instance().putBoolean("firstuse", true);
            ConfigManager.Instance().putInt("version", currentVersion);

            ZDBManager zdbManager = new ZDBManager(mContext);
            zdbManager.setVersion(0, 1);
            zdbManager.openDatabase();


            handler.sendEmptyMessage(2);
        } else {

        }
//        handler.sendEmptyMessageDelayed(1, 3000);

    }

    private void loadLocalAd() {
        try {
            //File basePic = new File(Constant.envir + "/ad/base.jpg");
            File adPic = new File(Constant.envir + "/ad/ad.jpg");
            Log.d("diaodebug",Constant.envir);
            if (adPic.exists()) {
                Log.d("diaodebug","adFile exists");
                int screenWidth = ((Activity) mContext).getWindowManager()
                        .getDefaultDisplay().getWidth();
                int screenHeight = ((Activity) mContext).getWindowManager()
                        .getDefaultDisplay().getHeight();
                Log.d("diaodebug","adFile exists"+screenHeight+screenHeight);
                /*double screenRatio = (screenHeight * 0.14) / screenWidth;
                base.setImageBitmap(SaveImage.resizeImage(
						ReadBitmap.readBitmap(mContext,
								new FileInputStream(basePic)), screenRatio));*/
                double screenRatio = (screenHeight * 0.86) / screenWidth;

                ad.setImageBitmap(SaveImage.resizeImage(ReadBitmap
                                .readBitmap(mContext, new FileInputStream(adPic)),
                        screenRatio));
                Log.d("diaodebug","adFile exists");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            startMainActivity();
        }
    }

    private void saveIcon() {
        try {
            InputStream is = mContext.getResources().openRawResource(
                    R.raw.icon_icon);
            BufferedInputStream bis = new BufferedInputStream(is);
        /*if (!(new File(DB_PATH).exists())) {
            new File(DB_PATH).mkdir();
		}*/
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/icon_icon.png");
            BufferedOutputStream bfos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[400000];
            int count = 0;
            while ((count = bis.read(buffer)) > 0) {
                bfos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
            bis.close();
            bfos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (adSplashUtil.isClick()) {
            startMainActivity();
        }
    }

    @Override
    protected void onDestroy() {
        adSplashUtil.destroy();
        super.onDestroy();
    }

//    private void initLiving() {
//        EngineRunner.init(CrashApplication.getInstance(), null);
//        Mqtt.initialize(CrashApplication.getInstance(), MqttService.class);
//        x.Ext.init(CrashApplication.getInstance());
//    }
}
