package com.iyuba.core.activity;

/**
 * 程序崩溃后操作
 *
 * @version 1.0
 * @author 陈彤
 * 修改日期    2014.3.29
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.configation.ConstantManager;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.util.LogUtils;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.data.local.HLDBManager;
import com.iyuba.imooclib.data.local.IMoocDBManager;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.module.movies.data.local.InfoHelper;
import com.iyuba.module.movies.data.local.db.DBManager;
import com.iyuba.pushlib.PushApplication;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.youdao.sdk.common.YoudaoSDK;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import personal.iyuba.personalhomelibrary.PersonalHome;

//import android.net.http.RequestQueue;

public class CrashApplication extends LitePalApplication {
    private static CrashApplication mInstance = null;
    private List<Activity> activityList = new LinkedList<>();


    private int mCount ;
    private long quitTime ,backTime;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    for (Activity activity : activityList) {
                        activity.finish();
                    }
                    break;

                default:
                    break;
            }
        }
    };
    // 全局volley请求队列队列
    private com.android.volley.RequestQueue queue;

    public static CrashApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
//        IMoviesApp.stopService();
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RuntimeManager.setApplicationContext(getApplicationContext());
        RuntimeManager.setApplication(this);
        mInstance = this;
        LitePal.initialize(this);
        PushApplication.initPush(this);//推送初始化
        YoudaoSDK.init(getApplicationContext());
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
        /*
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null);

        ConstantManager.init();

        LogUtils.e("bible","Crash!!!!!!!!!");
        BasicFavorDBManager.init(this);
        InfoHelper.init(this);
        DLManager.init(this,5);
        DBManager.init(this);
        HLDBManager.init(this);
        IMoocDBManager.init(this);
        BasicFavorInfoHelper.init(this);
        BasicDLDBManager.init(this);
        PersonalHome.init(getApplicationContext());

        MobSDK.init(this, Constant.SMSAPPID, Constant.SMSAPPSECRET);

        queue = Volley.newRequestQueue(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        initImageLoader();
        YoudaoSDK.init(this);

    }

    private void initImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.nearby_no_icon) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.nearby_no_icon) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.nearby_no_icon) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)//线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
//        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.nearby_no_icon2) // 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.nearby_no_icon2) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.nearby_no_icon2) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//                .build(); // 创建配置过得DisplayImageOption对象
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
//                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        ImageLoader.getInstance().init(config);
    }

    public RequestQueue getQueue() {
        return queue;
    }

    // 程序加入运行列表
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 程序退出
    public void exit() {
        handler.sendEmptyMessage(0);
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}