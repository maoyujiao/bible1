package com.iyuba.CET4bible.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.event.MainMicroClassEvent;
import com.iyuba.CET4bible.fragment.HomeFragment;
import com.iyuba.CET4bible.fragment.MeFragment;
import com.iyuba.CET4bible.listener.AppUpdateCallBack;
import com.iyuba.CET4bible.listening.SectionA;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.manager.VersionManager;
import com.iyuba.CET4bible.protocol.AdRequest;
import com.iyuba.CET4bible.protocol.AdResponse;
import com.iyuba.CET4bible.sqlite.ImportDatabase;
import com.iyuba.CET4bible.sqlite.op.Cet4WordOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeExplainOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionAAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionBAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionCAnswerOp;
import com.iyuba.CET4bible.sqlite.op.SectionATextOp;
import com.iyuba.CET4bible.thread.DownLoadAd;
import com.iyuba.CET4bible.util.exam.ExamDataUtil;
import com.iyuba.CET4bible.util.exam.ExamListBean;
import com.iyuba.abilitytest.activity.AbilityTestListActivity;
import com.iyuba.abilitytest.event.StartMicroClassEvent;
import com.iyuba.base.BaseActivity;
import com.iyuba.base.util.L;
import com.iyuba.base.util.T;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.discover.activity.GoPracticeEvent;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.manager.LogoutEvent;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.me.pay.PayOrderActivity;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.service.Background;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.ImportLibDatabase;
import com.iyuba.core.sqlite.op.UserInfoOp;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.ContextMenu;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.ui.circle.SpeakCircleEvent;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivityNew;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivityNew;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
import com.iyuba.headlinelibrary.ui.title.HolderType;
import com.iyuba.imooclib.ImoocManager;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;
import com.iyuba.imooclib.ui.web.Web;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;
import com.iyuba.module.favor.data.model.BasicFavorPart;
import com.iyuba.module.favor.event.FavorItemEvent;
import com.iyuba.module.movies.event.IMovieGoVipCenterEvent;
import com.iyuba.module.movies.ui.series.SeriesActivity;
import com.iyuba.module.toolbox.RxUtil;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.iyuba.pushlib.InitPush;
import com.iyuba.pushlib.PushConfig;
import com.iyuba.sdk.data.iyu.IyuAdClickEvent;
import com.iyuba.trainingcamp.activity.GoldNewFragment;
import com.iyuba.trainingcamp.activity.GoldShareActivity;
import com.iyuba.trainingcamp.app.TrainingManager;
import com.iyuba.trainingcamp.bean.SignBean;
import com.iyuba.trainingcamp.event.PayEvent;
import com.iyuba.trainingcamp.event.ShareEvent;
import com.iyuba.trainingcamp.event.StartMicroEvent;
import com.iyuba.trainingcamp.http.Http;
import com.iyuba.trainingcamp.http.HttpCallback;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.iyuba.wordtest.manager.WordManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import personal.iyuba.personalhomelibrary.PersonalType;
import personal.iyuba.personalhomelibrary.data.model.HeadlineTopCategory;
import personal.iyuba.personalhomelibrary.data.model.Voa;
import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;
import personal.iyuba.personalhomelibrary.manager.account.PersonalManager;
import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;
import timber.log.Timber;

/**
 * 类名
 *
 * @author 作者 <br/>
 * 实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
@RuntimePermissions
public class MainActivity extends BaseActivity implements OnClickListener,
        AppUpdateCallBack {

    private static boolean changeByApp;
    GoldNewFragment goldNewFragment;
    private TextView home, info, microclass;
    private TextView gold_vip;
    private HomeFragment homeFragment;
    private MeFragment meFragment;
    private MobClassFragment microClassListFragment;
    private DropdownTitleFragmentNew mExtraFragment;
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
    private Cet4WordOp db;
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
    private InitPush mInitPush;

    private void showFirstDialog() {
        if(getIntent().getExtras()!=null){
            Log.d("MainActivity", "showFirstDialog: ");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    com.iyuba.core.activity.Web.start(mContext, Constant.PROTOCOL_URL_HEADER+Constant.APPName,"用户隐私协议");
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(true);
                }
            } ;
            if (getIntent().getExtras().getBoolean("showDialog",false)){
                View view = LayoutInflater.from(mContext).inflate(R.layout.alert_text,null);
                TextView remindText = view.findViewById(R.id.remindText);
                String remindString = getResources().getString(R.string.user_protocol);
                SpannableStringBuilder spannableStringBuilder  = new SpannableStringBuilder(remindString);
                spannableStringBuilder.setSpan(clickableSpan, remindString.indexOf("用户协议"), remindString.indexOf("用户协议")+9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                remindText.setText(spannableStringBuilder);
                remindText.setMovementMethod(LinkMovementMethod.getInstance());
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("个人信息保护政策")
                        .setView(view)
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(mContext, Login.class));
                            }
                        })
                        .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        }
    }

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
        intent.putExtra("price", event.getPrice());
        intent.putExtra("isGold", true);
        mContext.startActivity(intent);
    }

    @Subscribe
    public void onEvent(LogoutEvent event){

        InitPush.getInstance().unRegisterToken(this,event.getUid());
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
        db = new Cet4WordOp(mContext);
        addEvaluateDatas();
        if (!ConfigManager.Instance().loadBoolean(ConfigManager.WORD_DB_NEW_LOADED,false )){
            db.writeToRoomDB(mContext);
            ConfigManager.Instance().putBoolean(ConfigManager.WORD_DB_NEW_LOADED, true);
        }
        CrashApplication.getInstance().addActivity(this);
        RuntimeManager.setApplication(getApplication());
        RuntimeManager.setDisplayMetrics(this);
        MobclickAgent.updateOnlineConfig(mContext);
        initViews();
        initPush();
        gold_vip = findView(R.id.gold_vip);
        if (Constant.APP_CONSTANT.isEnglish()) {
            gold_vip.setVisibility(View.VISIBLE);
        } else {
            gold_vip.setVisibility(View.GONE);
        }
        gold_vip.setOnClickListener(this);
        changeByApp = false;
        initSet();
        bindService();
        new NeedTimeOp().start();
        initFragments();

        setTabSelection(0);
        requestPermission();
        gold_vip.postDelayed(() -> showFirstDialog(),100);
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

    private void addEvaluateDatas() {
        SectionATextOp op = new SectionATextOp(this);
        op.addEvaluateDatas();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO};

            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    private void setImoocStatus() {
        ImoocManager.appId = Constant.APPID;
        User user = new User();

        if (AccountManager.Instace(mContext.getApplicationContext())
                .checkUserLogin()) {
            user.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
            user.name = AccountManager.Instace(mContext).getUserName();
            user.uid = Integer.parseInt(ConfigManager.Instance().loadString("userId"));
            IyuUserManager.getInstance().setCurrentUser(user);
        } else {
            user.vipStatus = "0";
            user.name = "";
            user.uid = 0;
            IyuUserManager.getInstance().setCurrentUser(user);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        initCommonConstants();
        intTraingCamp();
        if (microClassListFragment != null && mExtraFragment != null) {
            microClassListFragment.refreshContent();
        }
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
        pressAgainExit();
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

    private void intTraingCamp() {
        int vipStatus = AccountManager.Instace(mContext).getVipStatus();
        TrainingManager.appId = Constant.APPID;
        TrainingManager.productId = Constant.APP_CONSTANT.courseTypeId();
        if (BuildConfig.isCET4) {
            TrainingManager.LessonType = "cet4";
        } else {
            TrainingManager.LessonType = "cet6";
        }
        if (TextUtils.isEmpty(ConfigManager.Instance().loadString("userId")) || TextUtils.isEmpty(ConfigManager.Instance().loadString("userName"))) {
            TrainingManager.userName = "";
            TrainingManager.userId = "0";
        } else {
            TrainingManager.userName = ConfigManager.Instance().loadString("userName");
            TrainingManager.userId = ConfigManager.Instance().loadString("userId");
        }
        TrainingManager.vipstatus = vipStatus;

        if (vipStatus == com.iyuba.configation.Constant.VIP_STATUS) {
            TrainingManager.isVip = true;
        } else {
            TrainingManager.isVip = false;
        }
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
        me = findViewById(R.id.me);
        home.setOnClickListener(this);
        microclass.setOnClickListener(this);
        discover.setOnClickListener(this);
        me.setOnClickListener(this);
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
            case R.id.gold_vip:
                setTabSelection(4);
                break;
            default:
                break;
        }
    }

    public void setTabSelection(int index) {
        clearSelection();
        hideFragments();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (index) {
            case 0:
                setTextDrawable(R.drawable.home_press, home);
                home.setTextColor(getResources().getColor(R.color.colorPrimary));
                transaction.show(homeFragment);
                break;
            case 1:
                setTextDrawable(R.drawable.microclass_press, microclass);
                microclass.setTextColor(getResources().getColor(R.color.colorPrimary));
                transaction.show(microClassListFragment);
                break;
            case 2:
                setTextDrawable(R.drawable.me_press, me);
                me.setTextColor(getResources().getColor(R.color.colorPrimary));
                transaction.show(meFragment);
                break;
            case 3:
                setTextDrawable(R.drawable.main_headline_checked, discover);
                discover.setTextColor(getResources().getColor(R.color.colorPrimary));
                transaction.show(mExtraFragment);
                break;
            case 4:
                setTextDrawable(R.drawable.training_icon_pressed, gold_vip);
                gold_vip.setTextColor(getResources().getColor(R.color.colorPrimary));
                transaction.show(goldNewFragment);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void initFragments() {
        IHeadlineManager.appId = Constant.APPID;
        IHeadlineManager.appName = Constant.AppName;
        setImoocStatus();
        ImoocManager.appId = Constant.APPID;
        initCommonConstants();
        intTraingCamp();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        homeFragment = new HomeFragment();
        meFragment = new MeFragment();
        microClassListFragment = MobClassFragment.newInstance
                (MobClassFragment.buildArguments(Integer.parseInt(Constant.APP_CONSTANT.courseTypeId()), false, getEnglishFilter()));

        String[] types = new String[]{

                HeadlineType.BBC,
                HeadlineType.VOA,
                HeadlineType.CSVOA,
        };
        Bundle bundle = DropdownTitleFragmentNew.buildArguments(10, HolderType.SMALL, types, false);
        mExtraFragment = DropdownTitleFragmentNew.newInstance(bundle);
        goldNewFragment = new GoldNewFragment();
        transaction.add(R.id.content, microClassListFragment);
        transaction.add(R.id.content, meFragment);
        transaction.add(R.id.content, homeFragment);
        transaction.add(R.id.content, goldNewFragment);
        transaction.add(R.id.content, mExtraFragment);
        transaction.commit();
    }

    private void clearSelection() {
        setTextDrawable(R.drawable.home_bg, home);
        setTextDrawable(R.drawable.microclass_bg, microclass);
        setTextDrawable(R.drawable.main_headline_checked_bg, discover);
        setTextDrawable(R.drawable.training_icon_default, gold_vip);
        setTextDrawable(R.drawable.me_bg, me);
        gold_vip.setTextColor(0xffaeaeae);
        home.setTextColor(0xffaeaeae);
        microclass.setTextColor(0xffaeaeae);
        discover.setTextColor(0xffaeaeae);
        me.setTextColor(0xffaeaeae);
    }

    private void setTextDrawable(int drawable, TextView text) {
        Drawable img = mContext.getResources().getDrawable(drawable);
        text.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
    }

    private void hideFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (microClassListFragment != null) {
            transaction.hide(microClassListFragment);
        }
        if (mExtraFragment != null) {
            transaction.hide(mExtraFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        if (goldNewFragment != null) {
            transaction.hide(goldNewFragment);
        }
        transaction.commitAllowingStateLoss();
    }


    private void autoLogin() {
        int size = new UserInfoOp(mContext).getAccountSize();
        if (size > 0) {
            if (SettingConfig.Instance().isAutoLogin()) {
                Timber.d("onCreate: 正式用户");
                String[] nameAndPwd = AccountManager.Instace(mContext)
                        .getUserNameAndPwd();
                String userName = nameAndPwd[0];
                String userPwd = nameAndPwd[1];
                AccountManager.Instace(mContext).login(userName, userPwd, null);
                AccountManager.Instace(mContext).setLoginState(1);
            }
        } else if (!TouristUtil.isTouristLogout()) {
            if (TouristUtil.isTourist()) {
                TouristUtil.loadUserInfo(mContext);
            } else {
                TouristUtil t = new TouristUtil(mContext);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    MainActivityPermissionsDispatcher.doNextWithPermissionCheck(this,t);
                }else {
                    t.getUID(Build.SERIAL);
                }
            }
        } else {
            TouristUtil.setTourist(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void doNext(TouristUtil t) {
        t.getUID(Build.getSerial());
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

    public ArrayList<Integer> getEnglishFilter() {
        ArrayList<Integer> filter = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            filter.add(i);
        }
        filter.remove((Integer) 1);
        filter.remove((Integer) 5);
        filter.remove((Integer) 6);
        filter.add(61);
        filter.add(91);
        filter.add(52);
        return filter;
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
                                new DownLoadAd().execute("http://static3.iyuba.cn/dev/" + adResponse.adPicUrl,
                                        "ad");
                            }
                            if (!adResponse.basePicTime.equals("")) {
                                startDate = df.parse(adResponse.basePicTime);
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
//                FlurryAgent.onEndSession(mContext);
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

    private class NeedTimeOp extends Thread {
        @Override
        public void run() {
            super.run();
            boolean isfirst = ConfigManager.Instance().loadBoolean("firstuse");
            if (!isfirst) {
                checkAppUpdate();
            }
            Looper.prepare();
            autoLogin();
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

        switch (part.getType()) {
            case "news":
                startActivity(TextContentActivity.getIntent2Me(mContext, part.getId(), part.getTitle(), part.getTitleCn(), part.getType()
                        , part.getCategoryName(), part.getCreateTime(), part.getPic(), part.getSource()));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
                startActivity(AudioContentActivityNew.getIntent2Me(
                        mContext, part.getCategoryName(), part.getTitle(), part.getTitleCn(),
                        part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;

            case "song":
                startActivity(AudioContentActivity.getIntent2Me(
                        mContext, part.getCategoryName(), part.getTitle(), part.getTitleCn(),
                        part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivityNew.getIntent2Me(mContext,
                        part.getCategoryName(), part.getTitle(), part.getTitleCn(), part.getPic(),
                        part.getType(), part.getId(), part.getSound()));
                break;
            case "series":
                Intent intent = SeriesActivity.buildIntent(mContext, part.getSeriesId(), part.getId());
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DLItemEvent dlEvent) {
        //视频下载后点击
        BasicDLPart dlPart = dlEvent.items.get(dlEvent.position);
        switch (dlPart.getType()) {
            case "voa":
            case "csvoa":
            case "bbc":
                startActivity(AudioContentActivityNew.getIntent2Me(mContext, dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(mContext, dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivityNew.getIntent2Me(mContext,
                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId(), null));
                break;
        }
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
            ToastUtil.showToast(mContext, "请登录");
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
                ToastUtils.showShort("分享成功");
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

        final String time = new String(Base64.getEncoder().encode(dateString.getBytes()));

        String url = "http://api.iyuba.cn/credits/updateScore.jsp?srid=82&mobile=1&flag=" + time + "&uid=" + userID
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


                            MobclickAgent.onEvent(mContext, "dailybonus");
                            if (money == null) {
                                float moneyTotal = Float.parseFloat(totalCredit);
                                Toast.makeText(mContext, "分享成功," + "您已获得" + Integer.parseInt(addCredit) * 0.01 + "元,总计: " + Integer.parseInt(totalCredit) * 0.01 + "元," + "满10元可在\"爱语课吧\"公众号提现", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, "分享成功，您已获得" + Integer.parseInt(money) * 0.01 + "元，总计: " + Integer.parseInt(totalCredit) * 0.01 + "元", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
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
    public void onEvent(StartMicroEvent event) {
        Intent intent = MobClassActivity.buildIntent(mContext, Integer.parseInt(Constant.APP_CONSTANT.courseTypeId()), true, getEnglishFilter());
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(StartMicroClassEvent event) {
        Intent intent = MobClassActivity.buildIntent(mContext, Integer.parseInt(Constant.APP_CONSTANT.courseTypeId()), true, getEnglishFilter());
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(ImoocBuyVIPEvent event) {
        VipCenter.start(this, true);
    }

    @Subscribe
    public void onEvent(GoPracticeEvent event) {
        AbilityTestListActivity.actionStart(mContext, 2);
    }

    @Subscribe
    public void onEvent(SpeakCircleEvent event) {
        switch (event.flag) {
            case SpeakCircleEvent.ARTICLE_FLAG:
                startListeningActivity(event.topicId,event.paraId,event.idIndex);
                break;
            case SpeakCircleEvent.PERSONAL_FLAG:
                Intent intent = new Intent();
                SocialDataManager.Instance().userid = event.userId;
                intent = (PersonalHomeActivity.buildIntent(this,
                            Integer.parseInt(AccountManager.Instace(mContext).userId),
                            AccountManager.Instace(mContext).userName, 0));

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mContext.startActivity(intent);
                break;
            default:
                break;

        }
    }

    private void startListeningActivity(String year , String section , String idIndex) {
        Log.d("diao", year+":"+section+":"+idIndex);
        int type = Integer.parseInt(section)-1;
        Log.d("diao", section+"sec");
        String sec ;
        if (type == 0){
            sec = "A";
        }else if (type ==1){
            sec = "B";
        }else if (type ==2){
            sec = "C";
        }else {
            type = 0 ;
            sec = "A";
        }
        List list = getData(type, year);
        if (list == null || list.size() == 0) {
            startListeningActivity00(year, type, year,  sec);
        } else {
            Intent intent = new Intent(mContext, SectionA.class);
            intent.putExtra("section", section);
            intent.putExtra("isNewType", true);
            intent.putExtra("title", year);
            mContext.startActivity(intent);
        }
    }

    public static <T> ObservableTransformer<T, T> applyObservableIoSchedulerWith(final Consumer<? super Disposable> action) {
        return new ObservableTransformer<T,T>() {
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.compose(RxUtil.applyObservableIoScheduler()).doOnSubscribe(action);
            }
        };
    }



    private void startListeningActivity00(String year ,final int type, final String test, final String section ) {

        ExamDataUtil.requestExamData(mContext, Constant.APP_CONSTANT.TYPE(), section, year, new ExamDataUtil.DataCallback() {
            @Override
            public void onLoadData(boolean success) {
                if (success) {
                    getData(type, test);
                    Intent intent = new Intent(mContext, SectionA.class);
                    intent.putExtra("section", section);
                    intent.putExtra("isNewType", true);
                    mContext.startActivity(intent);
                } else {
                    T.showShort(mContext, "题库加载失败");
                    Glide.with(mContext).load(R.drawable.ic_chat_file_personal).listener(new RequestListener<Integer, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    });
                }
            }
        });
    }

    private List getData(int type, String year) {
        ListenDataManager.Instance().year = year;
        L.e("adapter === year ==== " + year);
        switch (type) {
            case 0:
                ListenDataManager.Instance().answerList = new NewTypeSectionAAnswerOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext).selectData(year);
                return ListenDataManager.Instance().answerList;
            case 1:
                ListenDataManager.Instance().answerList = new NewTypeSectionBAnswerOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext).selectData(year);
                return ListenDataManager.Instance().answerList;
            case 2:
                ListenDataManager.Instance().answerList = new NewTypeSectionCAnswerOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = new NewTypeExplainOp(mContext).selectData(year);
                return ListenDataManager.Instance().answerList;
            default:
                return null;
        }
    }


    private void initCommonConstants() {
        User user = new User();
        user.vipStatus = AccountManager.Instace(this).getVipStatus() + "";
        if (TextUtils.isEmpty(AccountManager.Instace(mContext).userId)){
            user.uid = 0 ;
        }else {
            user.uid = Integer.parseInt(AccountManager.Instace(mContext).userId);
        }
        user.name = AccountManager.Instace(mContext).userName;
        IyuUserManager.getInstance().setCurrentUser(user);
        PersonalManager.getInstance().AppId = Constant.APPID;
        PersonalManager.getInstance().categoryType = Constant.APPName;
        PersonalManager.getInstance().setUserId(user.uid);
        PersonalManager.getInstance().setUsername(user.name);
        PersonalManager.getInstance().vipState=user.vipStatus;//新增
        InitPush.getInstance().registerToken(this,user.uid);
    }

    private void initPush() {
        mInitPush= InitPush.getInstance();//初始化改变
        PushConfig config = new PushConfig();
        config.HUAWEI_ID=mContext.getString(R.string.push_huawei_id);
        config.HUAWEI_SECRET=mContext.getString(R.string.push_huawei_secret);
        config.MI_ID=mContext.getString(R.string.push_mi_id);
        config.MI_KEY=mContext.getString(R.string.push_mi_key);
        config.MI_SECRET=mContext.getString(R.string.push_mi_secret);
        config.OPPO_ID=mContext.getString(R.string.push_oppo_id);
        config.OPPO_KEY=mContext.getString(R.string.push_oppo_key);
        config.OPPO_SECRET=mContext.getString(R.string.push_oppo_secret);
        config.OPPO_MASTER_SECRET=mContext.getString(R.string.push_oppo_master_secret);//新增
        mInitPush.setInitPush(mContext,config);//包含了OPPO获取Token
        PersonalManager.getInstance().setMainPath(MainActivity.class.getName());//新的修改！！！
//        mInitPush.isShowToast=true;//是否开启推送模块的toast,默认关闭
//        String token=mInitPush.getToken(this).token;//获取token的方法 可能为空!!!
//        mInitPush.setSignPushCallback(getSignPush);//非必要，返回token
        if (mInitPush.isOtherPush()){//小米，华为，OPPO 之外的手机需要重新注册 ，需要请求权限
            MainActivityPermissionsDispatcher.requestPushWithPermissionCheck(MainActivity.this);
        }
    }

    @NeedsPermission({android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void requestPush() {
        mInitPush.resetPush(getApplication());
    }


    @Subscribe
    public void onEvent(IyuAdClickEvent event){
        startActivity(Web.buildIntent(this,event.info.linkUrl,event.info.title));
    }

    @Subscribe
    public void getVoadId(ArtDataSkipEvent event) {
        if (event.voa != null) {
            Voa voa = event.voa;
            switch (event.voa.categoryString) {
                case PersonalType.BBC:
                case PersonalType.VOA:
                case PersonalType.CSVOA:
                    startActivity(AudioContentActivityNew.getIntent2Me(
                            this, voa.categoryString,
                            voa.title, voa.title_cn, voa.pic, voa.playType
                            , voa.voaid + "",voa.sound
                    ));
                    break;
                case PersonalType.BBCWORDVIDEO:
                case PersonalType.MEIYU:
                case PersonalType.TED:
                case PersonalType.TOPVIDEOS:
                case PersonalType.VOAVIDEO:
                case PersonalType.JAPANVIDEOS:
                    startActivity(VideoContentActivityNew.getIntent2Me(mContext, voa.categoryString,
                            voa.title, voa.title_cn, voa.pic, voa.categoryString
                            , voa.voaid + "", voa.sound));
                    break;
            }
        } else if (event.headline!=null){
            HeadlineTopCategory headline = event.headline;
            switch (event.type) {
                case PersonalType.NEWS:
                    startActivity(TextContentActivity.getIntent2Me(
                            this,  headline.id + "",headline.Title,
                            headline.TitleCn,event.type,event.type,
                            headline.CreatTime,headline.getPic(),headline.Source));
            }
        }else {
            startListeningActivity(event.exam.topicId,event.exam.paraId,"0");
        }
    }

}

