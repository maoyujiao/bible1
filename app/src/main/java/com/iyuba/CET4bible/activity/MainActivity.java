package com.iyuba.CET4bible.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.event.MainMicroClassEvent;
import com.iyuba.CET4bible.fragment.HomeFragment;
import com.iyuba.CET4bible.fragment.MeFragment;
import com.iyuba.CET4bible.listener.AppUpdateCallBack;
import com.iyuba.CET4bible.manager.VersionManager;
import com.iyuba.CET4bible.protocol.AdRequest;
import com.iyuba.CET4bible.protocol.AdResponse;
import com.iyuba.CET4bible.sqlite.ImportDatabase;
import com.iyuba.CET4bible.thread.DownLoadAd;
import com.iyuba.CET4bible.util.AdSplashUtil;
import com.iyuba.CET4bible.util.exam.ExamDataUtil;
import com.iyuba.CET4bible.util.exam.ExamListBean;
import com.iyuba.base.BaseActivity;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.configation.ConstantManager;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.me.pay.PayOrderActivity;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.service.Background;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.ImportLibDatabase;
import com.iyuba.core.sqlite.mode.UserInfo;
import com.iyuba.core.sqlite.op.UserInfoOp;
import com.iyuba.core.teacher.fragment.TeacherFragment;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.ContextMenu;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.data.model.Headline;
import com.iyuba.headlinelibrary.event.HeadlineCreditChangeEvent;
import com.iyuba.headlinelibrary.event.HeadlineCreditIncreaseEvent;
import com.iyuba.headlinelibrary.event.HeadlineSearchItemEvent;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivity;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragment;
import com.iyuba.headlinelibrary.ui.title.HolderType;
import com.iyuba.imooclib.ImoocManager;
import com.iyuba.imooclib.event.ImoocCreditIncreaseEvent;
import com.iyuba.imooclib.ui.content.ContentActivity;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;
import com.iyuba.module.favor.data.model.BasicFavorPart;
import com.iyuba.module.favor.event.FavorItemEvent;
import com.iyuba.module.movies.event.IMovieCreditChangeEvent;
import com.iyuba.module.movies.event.IMovieCreditIncreaseEvent;
import com.iyuba.module.movies.event.IMovieGoVipCenterEvent;
import com.iyuba.module.movies.ui.series.SeriesActivity;
import com.iyuba.trainingcamp.activity.BuyIndicatorActivity;
import com.iyuba.trainingcamp.activity.GoldNewFragment;
import com.iyuba.trainingcamp.activity.GoldShareActivity;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.SignBean;
import com.iyuba.trainingcamp.event.PayEvent;
import com.iyuba.trainingcamp.event.ShareEvent;
import com.iyuba.trainingcamp.event.StarMicroEvent;
import com.iyuba.trainingcamp.http.Http;
import com.iyuba.trainingcamp.http.HttpCallback;
import com.iyuba.trainingcamp.utils.Constants;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.apache.commons.codec.binary.Base64;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;


/**
 * 类名
 *
 * @author 作者 <br/>
 * 实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class MainActivity extends BaseActivity implements OnClickListener,
        AppUpdateCallBack {

    String s = "";
    private static boolean changeByApp;
    GoldNewFragment goldNewFragment;
    private TextView home, info,/*discover, me,set*/
            microclass;
    private TextView gold_vip;
    private HomeFragment homeFragment;
    private TeacherFragment teacherFragment;
    private MeFragment meFragment;
    private MobClassFragment microClassListFragment;
    private Fragment mainHeadlinesFragment;
    private DropdownTitleFragment mExtraFragment;
    private FragmentManager fragmentManager;
    private Context mContext;
    private String version_code, url;
    private changeLanguageReceiver mLanguageReceiver;
    private sleepReceiver msleepReceiver;
    private boolean isExit = false;// 是否点过退出
    private ContextMenu contextMenu;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            isExit = false;
        }
    };
    private TextView discover;
    private TextView me;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showAlertAndCancel(
                            getResources().getString(R.string.about_update_alert_1)
                                    + version_code
                                    + getResources().getString(
                                    R.string.about_update_alert_2),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, About.class);
                                    intent.putExtra("update", true);
                                    intent.putExtra("url", url);
                                    intent.putExtra("version", version_code);
                                    startActivity(intent);
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };

    @Subscribe
    public void onPayEvent(PayEvent event) {
        if (TouristUtil.isTourist()) {
            ToastUtil.showLongToast(mContext, "请注册或登录正式账号后操作");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, PayOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", event.getAmount());
        intent.putExtra("out_trade_no", event.getOut_trade_no());
        intent.putExtra("subject", event.getSubject());
        intent.putExtra("body", event.getBody());
//        intent.putExtra("price", event.getPrice());
        intent.putExtra("price", "0.01");
        mContext.startActivity(intent);
    }

    @Subscribe
    public void onShareEvent(ShareEvent event) {
        showShareOnMoment(mContext, AccountManager.Instace(mContext).userId, AccountManager.Instace(mContext).userName);
        (GoldShareActivity.activity).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setLanguage();
        setContentView(R.layout.main);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        RuntimeManager.setApplication(getApplication());
        RuntimeManager.setDisplayMetrics(this);

//        EventBus.getDefault().register(this);
//        ShareSDK.initSDK(mContext, "40ffc22c29e8");
        MobclickAgent.updateOnlineConfig(mContext);
        initViews();
        initXunFeiYUYin();
        gold_vip = findView(R.id.gold_vip);

        gold_vip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int vipStatus = AccountManager.Instace(mContext).getVipStatus();
                Log.d("vipstatus",vipStatus+"");

                if (vipStatus == Constant.VIP_STATUS) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    final String[] itemString = {"voa","bbc","cet4","cet6","ted","csvoa"};
                    builder.setSingleChoiceItems(itemString, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            s = itemString[which];
                            GoldApp.getApp(mContext).init(ConfigManager.Instance().loadString("userId"), ConfigManager.Instance().loadString("userName"), s, Constant.APP_CONSTANT.courseTypeId(), Constant.APPID);
                            GoldApp.getApp(mContext).initTheme(Constants.ORANGE);
                            GoldApp.getApp(mContext).setVipStatus(true);
                            setTabSelection(4);
                            dialog.dismiss();
                        }
                    }).show();

//                    Intent intent = new Intent(mContext, GoldActivity.class);
//                    Intent intent = new Intent(mContext, GoldNewActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, BuyIndicatorActivity.class);
                    intent.putExtra("flag", true);
                    GoldApp.getApp(mContext).init(ConfigManager.Instance().loadString("userId"), ConfigManager.Instance().loadString("userName"), Constant.mListen, Constant.APP_CONSTANT.courseTypeId(), Constant.APPID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                }
            }
        });
        fragmentManager = this.getSupportFragmentManager();
        boolean isfirst = ConfigManager.Instance().loadBoolean("firstuse");
        if (isfirst && ConstantManager.isN123()) {
            AlertDialog alert = new AlertDialog.Builder(mContext).create();
//            alert.setTitle(R.string.introduction_this_version);
//            alert.setMessage("1.全新改版,超强体验.\n2.名师直播,助您顺利通过考试.\n3.全新发现模块.\n4.如有使用软件问题请通过反馈与我们沟通~"
//                    + "\nQQ：2326344291");
            alert.setTitle("提示");
            alert.setMessage("可在设置中切换日语考试等级N1，N2，N3");
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, getResources()
                            .getString(R.string.alert_btn_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ConfigManager.Instance().putBoolean("firstuse", false);
                            Intent intent = new Intent("presstorefresh");
                            mContext.sendBroadcast(intent);
                        }
                    });
            alert.show();
        }
        changeByApp = false;
        initSet();
        bindService();
        initNight();
        new NeedTimeOp().start();
        setTabSelection(0);


        Looper.getMainLooper().myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                ExamDataUtil.requestList(Constant.APP_CONSTANT.TYPE(), new ExamDataUtil.ListCallback() {
                    @Override
                    public void onLoadData(List<ExamListBean.DataBean> list) {
                        try {
                            ExamDataUtil.writeListData2DB(mContext, list);
                            ExamDataUtil.setFirstRequestData(mContext, false);
                        } catch (Exception e) {
                        }
                    }
                });
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLanguageReceiver);
        unregisterReceiver(msleepReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return false;
    }


    @Override
    public void onBackPressed() {
//        if (contextMenu.isShown()) {
//            contextMenu.dismiss();
//        } else {
        pressAgainExit();
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (contextMenu.isShown()) {
                contextMenu.dismiss();
            } else {
                contextMenu.setText(mContext.getResources().getStringArray(
                        R.array.app_menu));
                contextMenu.setCallback(new ResultIntCallBack() {
                    @Override
                    public void setResult(int result) {
                        switch (result) {
                            case 0:
                                startActivity(new Intent(mContext, Feedback.class));
                                break;
                            case 1:
                                if (BackgroundManager.Instace().bindService
                                        .getPlayer().isPlaying()) {
                                    Intent i = new Intent(Intent.ACTION_MAIN);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    startActivity(i);
                                } else {
                                    exit();
                                }
                                break;
                            case 2:
                                exit();
                                break;
                            default:
                                break;
                        }
                        contextMenu.dismiss();
                    }
                });
                contextMenu.show();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void pressAgainExit() {
        if (isExit) {
            exit();
            finish();
        } else {
            CustomToast.showToast(getApplicationContext(), R.string.alert_press);
            doExitInOneSecond();
        }
    }

    private void checkAppUpdate() {
        VersionManager.Instace(mContext).checkNewVersion(
                VersionManager.version, this);
    }

    @Override
    public void appUpdateSave(String version_code, String newAppNetworkUrl) {
        this.version_code = version_code;
        this.url = newAppNetworkUrl;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void appUpdateFaild() {


    }

    private void initViews() {
        contextMenu = findViewById(R.id.context_menu);
        home = findViewById(R.id.home);
        microclass = findViewById(R.id.microclass);
        discover = findViewById(R.id.discover);
//        ask = (TextView) findViewById(R.id.ask);
        me = findViewById(R.id.me);

        home.setOnClickListener(this);
        microclass.setOnClickListener(this);
        discover.setOnClickListener(this);

        if (!BuildConfig.isEnglish || AdSplashUtil.isNoAdTime()) {
            discover.setText("视频");
        } else {
            discover.setText("头条");
        }
//        ask.setOnClickListener(this);
        me.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("a", "hahha");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                setTabSelection(0);
                break;
            case R.id.microclass:
                setTabSelection(1);
                break;
            case R.id.discover:
                setTabSelection(3);
                break;
            case R.id.me:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    public void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                setTextDrawable(R.drawable.home_press, home);
                home.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.replace(R.id.content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                setTextDrawable(R.drawable.microclass_press, microclass);
                microclass.setTextColor(getResources().getColor(R.color.colorPrimary));
//                if (microClassListFragment == null) {
//                    microClassListFragment = new MobClassListFragment(ImoocConstantManager.OWNERID);
//                    transaction.replace(R.id.content, microClassListFragment);
//                } else {
//                    transaction.show(microClassListFragment);
//                }
                if (AccountManager.Instace(getApplicationContext())
                        .checkUserLogin()) {

                    ImoocManager.userId = ConfigManager.Instance().loadString("userId");
                    ImoocManager.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
                } else {

                    ImoocManager.userId = "0";
                    ImoocManager.vipStatus = "0";
                }
                ImoocManager.appId = Constant.APPID;
//                MobClassActivity.buildIntent(mContext, 21);
                if (microClassListFragment == null) {
                    microClassListFragment = MobClassFragment.newInstance(MobClassFragment.buildArguments(Integer.parseInt(Constant.APP_CONSTANT.courseTypeId())));
                    microClassListFragment.onCreateView(LayoutInflater.from(mContext), (ViewGroup) findView(R.id.dl), null);

//                                    getSupportFragmentManager().beginTransaction().
//                                            add(com.iyuba.imooclib.R.id.frame_container, MobClassFragment.newInstance(args))
//                                            .commit();
                    transaction.add(R.id.content, microClassListFragment);
                } else {
                    microClassListFragment = MobClassFragment.newInstance(MobClassFragment.buildArguments(Integer.parseInt(Constant.APP_CONSTANT.courseTypeId())));
                    microClassListFragment.onCreateView(LayoutInflater.from(mContext), (ViewGroup) findView(R.id.dl), null);

//                                    getSupportFragmentManager().beginTransaction().
//                                            add(com.iyuba.imooclib.R.id.frame_container, MobClassFragment.newInstance(args))
//                                            .commit();
                    transaction.add(R.id.content, microClassListFragment);
//                    transaction.show(microClassListFragment);

                }
//                startActivity(new Intent(mContext, MobClassActivity.class));
                break;
            case 2:
                setTextDrawable(R.drawable.me_press, me);
                me.setTextColor(getResources().getColor(R.color.colorPrimary));
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.content, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                break;
            case 3:
                setTextDrawable(R.drawable.main_headline_checked, discover);
                discover.setTextColor(getResources().getColor(R.color.colorPrimary));
//                String uid;
//                String vipStatus;
//                if (AccountManager.Instace(mContext).getUserInfo() == null) {
//                    uid = "0";
//                    vipStatus = "0";
//                } else {
//                    uid = AccountManager.Instace(mContext).userInfo.uid;
//                    vipStatus = AccountManager.Instace(mContext).userInfo.vipStatus;
//                }
//                if (mainHeadlinesFragment == null) {
//                    String typeStr;
//
//                    if (BuildConfig.isEnglish) {
//                        if (AdSplashUtil.isNoAdTime()) {
//                            typeStr = HeadlinesConstantManager.buildTypeStr(
//                                    HeadlinesConstantManager.Type.ALL,
//                                    HeadlinesConstantManager.Type.SONG,
//                                    HeadlinesConstantManager.Type.MEIYU,
//                                    HeadlinesConstantManager.Type.TED
//                            );
//                        } else {
//                            typeStr = HeadlinesConstantManager.buildTypeStr(
//                                    HeadlinesConstantManager.Type.ALL,
//                                    HeadlinesConstantManager.Type.NEWS,
//                                    HeadlinesConstantManager.Type.VOA,
//                                    HeadlinesConstantManager.Type.CSVOA,
//                                    HeadlinesConstantManager.Type.SONG,
//                                    HeadlinesConstantManager.Type.BBC,
//                                    HeadlinesConstantManager.Type.VOAVIDEO,
//                                    HeadlinesConstantManager.Type.MEIYU,
//                                    HeadlinesConstantManager.Type.TED,
//                                    HeadlinesConstantManager.Type.BBCWORDVIDEO,
//                                    HeadlinesConstantManager.Type.TOPVIDEOS
//                            );
//                        }
//                    } else {
//                        typeStr = HeadlinesConstantManager.buildTypeStr(
//                                HeadlinesConstantManager.Type.JAPANVIDEOS
//                        );
//                    }
//                    mainHeadlinesFragment = DropdownMenuFragment.newInstance(
//                            uid, vipStatus,
//                            HeadlinesConstantManager.ALL_CATEGORY,
//                            HeadlinesConstantManager.BBC_TYPE, true,
//                            HeadlinesConstantManager.REQUEST_PIECES_MIX, typeStr);
//                    transaction.replace(R.id.content, mainHeadlinesFragment);
//                } else {
//                    transaction.show(mainHeadlinesFragment);
//                }


                IHeadlineManager.appId = Constant.APPID;
                IHeadlineManager.appName = Constant.AppName;
                if (null == AccountManager.Instace(mContext).userId) {
                    IHeadlineManager.userId = 0;
                    IHeadlineManager.vipStatus = "0";
                    IHeadlineManager.username = "";
                } else {
                    IHeadlineManager.userId = Integer.parseInt(AccountManager.Instace(mContext).userId);
                    IHeadlineManager.vipStatus = String.valueOf(AccountManager.Instace(mContext).getVipStatus());
                    IHeadlineManager.username = AccountManager.Instace(mContext).userName;
                }


                String[] types = new String[]{
                        HeadlineType.ALL,
                        HeadlineType.VOAVIDEO,
                        HeadlineType.MEIYU,
                        HeadlineType.TED,
                        HeadlineType.BBCWORDVIDEO,
                        HeadlineType.TOPVIDEOS,
                        HeadlineType.SONG
                };


                if (mExtraFragment == null) {
                    Bundle bundle = DropdownTitleFragment.buildArguments(2, 8, HolderType.SMALL, types);
                    mExtraFragment = DropdownTitleFragment.newInstance(bundle);
                    mExtraFragment.onCreateView(LayoutInflater.from(mContext), (ViewGroup) findView(R.id.dl), null);
//                    transaction.replace(R.id.content, mExtraFragment);
                    transaction.add(R.id.content, mExtraFragment);
                } else {
//                    transaction.show(mExtraFragment);
                    Bundle bundle = DropdownTitleFragment.buildArguments(2, 8, HolderType.SMALL, types);
                    mExtraFragment = DropdownTitleFragment.newInstance(bundle);
//                    transaction.replace(R.id.content, mExtraFragment);
                    mExtraFragment.onCreateView(LayoutInflater.from(mContext), (ViewGroup) findView(R.id.dl), null);

                    transaction.add(R.id.content, mExtraFragment);
                }
//                transaction.commit();
                break;
            case 4:
//                setTextDrawable(R.drawable.me_press, me);
//                me.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    Bundle bundle = GoldNewFragment.buildArguments(mContext);
                goldNewFragment = GoldNewFragment.newInstance();
                transaction.add(R.id.content, goldNewFragment);

                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void clearSelection() {
        setTextDrawable(R.drawable.home_bg, home);
        setTextDrawable(R.drawable.microclass_bg, microclass);
        setTextDrawable(R.drawable.main_headline_checked_bg, discover);
        setTextDrawable(R.drawable.me_bg, me);

        home.setTextColor(0xffaeaeae);
        microclass.setTextColor(0xffaeaeae);
        discover.setTextColor(0xffaeaeae);
        me.setTextColor(0xffaeaeae);
    }

    private void setTextDrawable(int drawable, TextView text) {
        Drawable img = mContext.getResources().getDrawable(drawable);
        text.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (microClassListFragment != null) {
            transaction.hide(microClassListFragment);
        }
        if (mExtraFragment != null) {
            transaction.hide(mExtraFragment);
        }
        if (teacherFragment != null) {
            transaction.hide(teacherFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        if (goldNewFragment != null) {
            transaction.hide(goldNewFragment);
        }
    }

    private void initNight() {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
//        String time = sdf.format(new Date());
//        String night = "23:00:00";
//        String day = "07:00:00";
//        String midNight = "24:00:00";
//        String midNight2 = "00:00:00";
//        boolean isNight = (time.compareTo(night) > 0)
//                && (time.compareTo(midNight) < 0);
//        boolean isNight2 = (time.compareTo(midNight2) > 0)
//                && (time.compareTo(day) < 0);
//        if (SettingConfig.Instance().isNight()) {
//            night();
//        } else if (isNight || isNight2) {
//            handler.sendEmptyMessage(1);
//            night();
//            SettingConfig.Instance().setNight(true);
//            changeByApp = true;
//        } else {
//            SettingConfig.Instance().setNight(false);
//            day();
//        }
    }

    private void AutoLogin() {
        //未登录
        int size = new UserInfoOp(mContext).getAccountSize();
        LogUtils.e("Account Size : " + size);
        if (size == 0 && !TouristUtil.isTouristLogout()) {
            if (!TouristUtil.isTourist()) {
                new TouristUtil(mContext).getUID();
            } else {
                TouristUtil.loadUserInfo(mContext);
            }

            return;
        }

        if (SettingConfig.Instance().isAutoLogin()) { // 自动登录

            String[] nameAndPwd = AccountManager.Instace(mContext).getUserNameAndPwd();
            String userName = nameAndPwd[0];
            String userPwd = nameAndPwd[1];

//            if (NetWorkState.isConnectingToInternet() && NetWorkState.getAPNType() != 1) {
//                if (userName != null && !userName.equals("")) {
//                    AccountManager.Instace(mContext).login(userName, userPwd,
//                            new OperateCallBack() {
//
//                                @Override
//                                public void success(String message) {
//                                }
//
//                                @Override
//                                public void fail(String message) {
//                                    AccountManager.Instace(mContext).setLoginState(1);
//                                }
//                            });
//                }
//            } else
            if (userName != null && !userName.equals("")) {
                AccountManager.Instace(mContext).setLoginState(1);
            }
        }
    }

    private void initSet() {
        // 改变语言
        mLanguageReceiver = new changeLanguageReceiver();
        IntentFilter miIntentFilter = new IntentFilter("changeLanguage");
        registerReceiver(mLanguageReceiver, miIntentFilter);
        msleepReceiver = new sleepReceiver();
        IntentFilter mmIntentFilter = new IntentFilter("gotosleep");
        registerReceiver(msleepReceiver, mmIntentFilter);
        PushAgent mPushAgent = PushAgent.getInstance(mContext);
        if (SettingConfig.Instance().isPush()) {
            mPushAgent.enable();
            PushAgent.getInstance(mContext).onAppStart();
        } else {
            mPushAgent.disable();
        }
        LoadFix();
    }

    private void setLanguage() {
        int id = ConfigManager.Instance().loadInt("applanguage");
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        switch (id) {
            case 0:
                config.locale = Locale.getDefault(); // 系统默认语言
                break;
            case 1:
                config.locale = Locale.SIMPLIFIED_CHINESE; // 简体中文
                break;
            case 2:
                config.locale = Locale.ENGLISH; // 英文
                break;
            case 3:
                // config.locale =new Locale("ar");
                break;
            default:
                config.locale = Locale.getDefault();
                break;
        }
        resources.updateConfiguration(config, dm);
    }

    private void LoadFix() {
        Constant.mode = ConfigManager.Instance().loadInt("mode");
        Constant.type = ConfigManager.Instance().loadInt("type");
        Constant.download = ConfigManager.Instance().loadInt("download");
    }

    private void saveFix() {
        if (AccountManager.Instace(mContext).userInfo != null && !TouristUtil.isTourist()) {
            new UserInfoOp(mContext).saveData(AccountManager.Instace(mContext).userInfo);
        }
        ConfigManager.Instance().putInt("mode", Constant.mode);
        ConfigManager.Instance().putInt("type", Constant.type);
        ConfigManager.Instance().putInt("download", Constant.download);
    }

    private void bindService() {
        Log.d("service", "bindService: ");
        Intent intent = new Intent(mContext, Background.class);
        bindService(intent, BackgroundManager.Instace().conn,
                Context.BIND_AUTO_CREATE);
    }

    private void unBind() {
        Log.d("service", "unBind: ");
        unbindService(BackgroundManager.Instace().conn);
    }

    private void changeWelcome() {
        ExeProtocol.exe(new AdRequest(), new ProtocolResponse() {
            @Override
            public void finish(BaseHttpResponse bhr) {
                AdResponse adResponse = (AdResponse) bhr;
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    String lastTime = ConfigManager.Instance().loadString(
                            "updateAD");
                    Date lastUpdateTime;
                    if (lastTime != null) {
                        lastUpdateTime = df.parse(lastTime);
                    } else {
                        lastUpdateTime = date;
                    }
                    if (!TextUtils.isEmpty(adResponse.type) && !adResponse.type.equals("web")) {
                        return;
                    }
                    if (!adResponse.adPicTime.equals("")) {
                        Date startDate = df.parse(adResponse.adPicTime);
                        if (lastUpdateTime.getTime() <= startDate.getTime()) {
                            Looper.prepare();
                            ConfigManager.Instance().putString("startuppic_Url", adResponse.startuppic_Url);
                            if (startDate.getTime() <= date.getTime()) {
                                new DownLoadAd().execute("http://static3.iyuba.com/dev/" + adResponse.adPicUrl,
                                        "ad");
                            }
                            if (!adResponse.basePicTime.equals("")) {
                                startDate = df.parse(adResponse.basePicTime);
                                if (startDate.getTime() <= date.getTime()) {
//                                    new DownLoadAd().execute(
//                                            adResponse.basePicUrl, "base");
                                }
                            }
                            Looper.loop();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                Log.d("下载出错", "下载广告图片出错");

            }
        });
    }

    private void addShortcut() {
        Intent shortcutintent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.app_name));
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                getApplicationContext(), R.drawable.trainingcamp_icon);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
                getApplicationContext(), Welcome.class));
        // 发送广播。OK
        Intent intent = new Intent();
        intent.setClass(mContext, Welcome.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        sendBroadcast(shortcutintent);
    }

    private void showAlertAndCancel(String msg,
                                    DialogInterface.OnClickListener ocl) {
        AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle(R.string.alert_title);
        alert.setMessage(msg);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.alert_btn_ok), ocl);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alert.show();
    }

    private void exit() {
        if (changeByApp) {
            SettingConfig.Instance().setNight(false);
        }
        saveFix();
        GitHubImageLoader.Instace(mContext).exit();
        new Thread() {
            @Override
            public void run() {
                super.run();
                unBind();
//                MobSDK.st.stopSDK(mContext);
                FlurryAgent.onEndSession(mContext);
                ImportDatabase.mdbhelperClose();
                ImportLibDatabase.mdbhelperClose();
                CrashApplication.getInstance().exit();
            }
        }.start();
    }

    private void doExitInOneSecond() {
        isExit = true;
        HandlerThread thread = new HandlerThread("doTask");
        thread.start();
        new Handler(thread.getLooper()).postDelayed(task, 1500);// 1.5秒内再点有效
    }

    private void initXunFeiYUYin() {
        // 将“12345678”替换成您申请的 APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与 appid 之间添加任务空字符或者转义符
//        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=57fc4ab0");//5752942e是雅思宝典在讯飞语音的apid
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=528db2fa");//5752942e是雅思宝典在讯飞语音的apid

    }

    private class NeedTimeOp extends Thread {
        @Override
        public void run() {
            super.run();
            boolean isfirst = ConfigManager.Instance().loadBoolean("firstuse");
            if (isfirst) {
            } else {
                checkAppUpdate();
            }
            Looper.prepare();
            AutoLogin();
            changeWelcome();
            Looper.loop();
        }
    }

    class changeLanguageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            unBind();
            if (changeByApp) {
                SettingConfig.Instance().setNight(false);
            }
            saveFix();
            ImportDatabase.mdbhelperClose();
            ImportLibDatabase.mdbhelperClose();
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }
    }

    public class sleepReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            exit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainMicroClassEvent event) {
        // 打开视频
        if ("6".equals(event.type)) {
            setTabSelection(3);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FavorItemEvent fEvent) {
        //收藏页面点击
        BasicFavorPart fPart = fEvent.items.get(fEvent.position);
        goFavorItem(fPart);

    }


    private void goFavorItem(BasicFavorPart part) {
        String userIdStr = AccountManager.Instace(mContext).userId;

        int userId;
        boolean isVip;


        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {
            e.printStackTrace();
            userId = 0;
        }


        UserInfo userInfo2 = AccountManager.Instace(mContext).getUserInfo();

        try {
            int vip = Integer.parseInt(userInfo2.vipStatus);
            isVip = vip > 0;
        } catch (Exception e) {
            e.printStackTrace();
            isVip = false;
        }


        switch (part.getType()) {
            case "news":

                startActivity(TextContentActivity.getIntent2Me(mContext, userId, Constant.APPID, part.getId(), part.getTitle(), part.getTitle_cn(), part.getType()
                        , part.getCategoryName(), part.getCreateTime(), part.getPic(), part.getSource()));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(mContext, Constant.APPID, userId,
                        isVip, part.getCategoryName(), part.getTitle(), part.getTitle_cn(),
                        part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":

                startActivity(VideoContentActivity.getIntent2Me(mContext, Constant.APPID, userId,
                        part.getCategoryName(), part.getTitle(), part.getTitle_cn(), part.getPic(),
                        part.getType(), part.getId()));
                break;
            case "series":

                Intent intent = SeriesActivity.buildIntent(mContext, part.getSeriseId(), part.getId());
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DLItemEvent dlEvent) {
        //视频下载后点击
        BasicDLPart dlPart = dlEvent.items.get(dlEvent.position);


        String userIdStr = AccountManager.Instace(mContext).userId;

        int userId;
        boolean isVip;


        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {
            e.printStackTrace();
            userId = 0;
        }


        UserInfo userInfo2 = AccountManager.Instace(mContext).getUserInfo();

        try {
            int vip = Integer.parseInt(userInfo2.vipStatus);
            isVip = vip > 0;
        } catch (Exception e) {
            e.printStackTrace();
            isVip = false;
        }

        switch (dlPart.getType()) {
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(mContext, Constant.APPID, userId,
                        isVip, dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitle_cn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivity.getIntent2Me(mContext, Constant.APPID, userId,
                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitle_cn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
        }
    }


//    **
//            *学习完成积分增加
//     *ImoocCreditIncreaseEvent --微课模块
//     *IMovieCreditIncreaseEvent --美剧模块
//     *HeadlineCreditIncreaseEvent --搜一搜模块
//     *
//             *
//    @param
//    event
//     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocCreditIncreaseEvent event) {

//        getPersonalInfo();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IMovieCreditIncreaseEvent event) {
//        getPersonalInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlineCreditIncreaseEvent event) {
//        getPersonalInfo();
    }

    /**
     * 分享积分增加
     * IMovieCreditChangeEvent 美剧模块
     * HeadlineCreditChangeEvent 搜一搜模块
     *
     * @param event
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IMovieCreditChangeEvent event) {
//        getPersonalInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlineCreditChangeEvent event) {
//        getPersonalInfo();
    }

    /**
     * 美剧-下载开通会员
     *
     * @param event
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IMovieGoVipCenterEvent event) {

        if (AccountManager.Instace(mContext).checkUserLogin()) {
            startActivity(new Intent(mContext, VipCenter.class));
        } else {
//            CustomDialog.showLoginDialog(context, false, new IOperationFinish() {
//                @Override
//                public void finish() {
//                    startActivity(new Intent(context, VipCenterActivity.class));
//                }
//            });
            ToastUtil.showToast(mContext, "请登录");
        }
    }


    /**
     * 搜一搜列表点击
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlineSearchItemEvent event) {
        Headline headline = event.headline;

        try {
            IHeadlineManager.userId = Integer.parseInt(AccountManager.Instace(mContext).userId);
        } catch (Exception e) {
            IHeadlineManager.userId = 0;
        }
        IHeadlineManager.appId = Constant.APPID;

        switch (headline.type) {
            case "news":

                startActivity(TextContentActivity.getIntent2Me(mContext, IHeadlineManager.userId, IHeadlineManager.appId, headline));

                break;
            case "voa":
            case "csvoa":
            case "bbc":

                startActivity(AudioContentActivity.getIntent2Me(mContext, IHeadlineManager.appId, IHeadlineManager.userId, IHeadlineManager.isVip(), headline));
                break;
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(mContext, IHeadlineManager.appId, IHeadlineManager.userId, IHeadlineManager.isVip(), headline));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
                startActivity(VideoContentActivity.getIntent2Me(mContext, IHeadlineManager.appId, IHeadlineManager.userId, headline));
                break;
            case "bbcwordvideo":
            case "topvideos":
                startActivity(VideoContentActivity.getIntent2Me(mContext, IHeadlineManager.appId, IHeadlineManager.userId, headline));
                break;
            case "class": {
                int packId = Integer.parseInt(headline.id);
                Intent intent = ContentActivity.buildIntent(mContext, packId);
                startActivity(intent);
                break;
            }
        }
    }


    public void showShareOnMoment(Context context, final String userID, final String AppId) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setPlatform(WechatMoments.NAME);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setImagePath(FilePath.getSharePicPath() + "share.png");


        oks.setSilent(true);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                startInterfaceADDScore(userID, AppId);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享失败===", throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享取消===", "....");
            }
        });
        // 启动分享GUI
        oks.show(context);
    }


    private void startInterfaceADDScore(String userID, String appid) {

        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
//        final String time = Base64Coder.encode(dateString);

        final String time = new String(Base64.encodeBase64(dateString.getBytes()));

        String url = "http:api.iyuba.com/credits/updateScore.jsp?srid=82&mobile=1&flag=" + time + "&uid=" + userID
                + "&appid=" + appid;
        Http.get(url, new HttpCallback() {

            @Override
            public void onSucceed(Call call, String response) {
                final SignBean bean = new Gson().fromJson(response, SignBean.class);
                if (bean.getResult().equals("200")) {
                    final String money = bean.getMoney();
                    final String addCredit = bean.getAddcredit();
                    final String days = bean.getDays();
                    final String totalCredit = bean.getTotalcredit();

                    //打卡成功,您已连续打卡xx天,获得xx元红包,关注[爱语课吧]微信公众号即可提现!
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (money != null) {
                                float moneyThisTime = Float.parseFloat(money);
                            }


                            MobclickAgent.onEvent(mContext, "dailybonus");
                            if (money == null) {
                                float moneyTotal = Float.parseFloat(totalCredit);
                                Toast.makeText(mContext, "分享成功," + "您已获得" + Integer.parseInt(addCredit) * 0.01 + "元,总计: " + Integer.parseInt(totalCredit) * 0.01 + "元," + "满10元可在\"爱语课吧\"公众号提现", Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(mContext, "分享成功，您已获得" + Integer.parseInt(money) * 0.01 + "元，总计: " + Integer.parseInt(totalCredit) * 0.01 + "元", Toast.LENGTH_LONG).show();


                            }
                        }
                    });


                } else

                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "您今日已分享", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }


            @Override
            public void onError(Call call, Exception e) {

            }
        });


    }

    @Subscribe
    public void onEvent(StarMicroEvent event) {
        if (AccountManager.Instace(getApplicationContext())
                .checkUserLogin()) {
            ImoocManager.userId = ConfigManager.Instance().loadString("userId");
            ImoocManager.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
        } else {

            ImoocManager.userId = "0";
            ImoocManager.vipStatus = "0";
        }
        ImoocManager.appId = Constant.APPID;
        Intent intent = MobClassActivity.buildIntent(mContext, event.getProductid());
        startActivity(intent);
    }

}
