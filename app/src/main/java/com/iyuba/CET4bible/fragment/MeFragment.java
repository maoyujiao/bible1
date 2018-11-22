/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.activity.About;
import com.iyuba.CET4bible.activity.SetActivity;
import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.Login;
import com.iyuba.core.activity.Web;
import com.iyuba.core.discover.activity.DiscoverForAt;
import com.iyuba.core.http.Http;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.activity.AttentionCenter;
import com.iyuba.core.me.activity.FansCenter;
import com.iyuba.core.me.activity.InfoFullFillActivity;
import com.iyuba.core.me.activity.MessageCenter;
import com.iyuba.core.me.activity.NoticeCenter;
import com.iyuba.core.me.activity.PersonalHome;
import com.iyuba.core.me.activity.StudyRankingActivity;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.me.activity.WriteState;
import com.iyuba.core.me.pay.MD5;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.base.GradeRequest;
import com.iyuba.core.protocol.base.GradeResponse;
import com.iyuba.core.protocol.message.RequestBasicUserInfo;
import com.iyuba.core.protocol.message.RequestNewInfo;
import com.iyuba.core.protocol.message.RequestUserDetailInfo;
import com.iyuba.core.protocol.message.ResponseBasicUserInfo;
import com.iyuba.core.protocol.message.ResponseNewInfo;
import com.iyuba.core.protocol.message.ResponseUserDetailInfo;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.mode.UserInfo;
import com.iyuba.core.sqlite.mode.me.Emotion;
import com.iyuba.core.teacher.activity.CommunityActivity;
import com.iyuba.core.teacher.activity.QuesListActivity;
import com.iyuba.core.teacher.activity.QuestionNotice;
import com.iyuba.core.teacher.activity.TheQuesListActivity;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.CheckGrade;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.Expression;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.dailybonus.DailyBonusUtil;
import com.iyuba.dailybonus.SignActivity;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.ui.search.MSearchActivity;
import com.iyuba.imooclib.ImoocManager;
import com.iyuba.module.dl.DLActivity;
import com.iyuba.module.favor.ui.BasicFavorActivity;
import com.iyuba.module.intelligence.ui.LearningGoalActivity;
import com.iyuba.module.movies.ui.movie.MovieActivity;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class  MeFragment extends Fragment {
    private View noLogin, login; // 登录提示面板
    private Button loginBtn, logout;
    private Context mContext;
    private ImageView photo;
    private TextView name, state, fans, attention, notification, listem_time,
            integral, position, lv;
    private View person, local_panel;
    private View stateView, messageView, vipView;
    private View local, favor, read, back;
    private View attentionView, fansView, notificationView, integralView,
            discover_rqlist, discover_qnotice, discover_myq, discover_mysub;
    private UserInfo userInfo;
    private View root;
    private boolean showLocal;
    private View intel_userinfo;
    private View intel_learn_goal;
    private View intel_result;
    private View intel_test_result;
    private View intel_word_result;
    private View study_ranking;

    private View tv_sign;

    private boolean bInfoFullFill = false;
    private boolean bLearnTarget = false;
    private ResponseUserDetailInfo userDetailInfo;
    private View me_discover;
    private View me_privilege;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private String date = sdf.format(new Date());
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 1:
                    CustomToast.showToast(mContext, R.string.action_fail);
                    break;
                case 2:
                    root.findViewById(R.id.newletter).setVisibility(View.VISIBLE);
                    break;
                case 3:
                    setTextViewContent();
                    break;
                case 4:
                    AccountManager.Instace(mContext).loginOut();
                    CustomToast.showToast(mContext, R.string.loginout_success);
                    SettingConfig.Instance().setHighSpeed(false);
                    onResume();
                    break;
                case 5:
                    root.findViewById(R.id.newletter).setVisibility(View.GONE);
                    break;
                case 6:
                    Dialog dialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("提示")
                            .setMessage(
                                    "亲,请先完善个人信息~")
                            .setPositiveButton("确定", null)
                            .create();
                    dialog.show();// 如果要显示对话框，一定要加上这句
                    break;
                case 7:
                    Dialog dialog2 = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("提示")
                            .setMessage(
                                    "亲,请先补充您的学习目标~")
                            .setPositiveButton("确定", null)
                            .create();
                    dialog2.show();// 如果要显示对话框，一定要加上这句
                    break;
            }
        }
    };
    private OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Intent intent;
            int id = arg0.getId();
            String packageName = mContext.getPackageName();
            if (id == R.id.personalhome) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent(mContext, PersonalHome.class);
                    SocialDataManager.Instance().userid = AccountManager
                            .Instace(mContext).userId;
                    startActivity(intent);
                }
            } else if (id == R.id.me_state_change) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent(mContext, WriteState.class);
                    startActivity(intent);
                }
            } else if (id == R.id.me_vip) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent(mContext, VipCenter.class);
                    startActivity(intent);
                }
            } else if (id == R.id.me_privilege) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    if (TouristUtil.isTourist()) {
                        TouristUtil.showTouristHint(mContext);
                        return;
                    }

                    intent = new Intent();
                    intent.setClass(mContext, Web.class);
//                    http://vip.iyuba.com/mycode.jsp?uid=3830618&appid=148&sign=a4fa0c78b161c2f7b6fe91fdab352553
                    intent.putExtra("url", "http://vip.iyuba.com/mycode.jsp?"
                            + "uid=" + AccountManager.Instace(mContext).userId
                            + "&appid=" + Constant.APPID
                            + "&sign=" + MD5.getMD5ofStr(AccountManager.Instace(mContext).userId + "iyuba" + Constant.APPID + date));
                    intent.putExtra("title",
                            "优惠券");
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            } else if (id == R.id.me_discover) {
                intent = new Intent(mContext, DiscoverForAt.class);
                startActivity(intent);
            } else if (id == R.id.me_message) {
                intent = new Intent(mContext, MessageCenter.class);
                startActivity(intent);
            } else if (id == R.id.attention_area) {
                intent = new Intent(mContext, AttentionCenter.class);
                intent.putExtra("userid",
                        AccountManager.Instace(mContext).userId);
                startActivity(intent);
            } else if (id == R.id.fans_area) {
                intent = new Intent(mContext, FansCenter.class);
                intent.putExtra("userid",
                        AccountManager.Instace(mContext).userId);
                startActivity(intent);
            } else if (id == R.id.notification_area) {
                intent = new Intent(mContext, NoticeCenter.class);
                intent.putExtra("userid",
                        AccountManager.Instace(mContext).userId);
                startActivity(intent);
            } else if (id == R.id.Integral) {
                intent = new Intent(mContext, Web.class);
                intent.putExtra("title", "积分明细");
                intent.putExtra("url",
                        "http://api.iyuba.com/credits/useractionrecordmobileList1.jsp?uid="
                                + AccountManager.Instace(mContext).userId);
                startActivity(intent);
            } else if (id == R.id.discover_rqlist) {
                intent = new Intent();
                intent.setClass(mContext, QuesListActivity.class);
                startActivity(intent);
            } else if (id == R.id.discover_qnotice) {
                intent = new Intent();
                intent.setClass(mContext, QuestionNotice.class);
                startActivity(intent);
            } else if (id == R.id.discover_myq) {
                intent = new Intent();
                intent.setClass(mContext, TheQuesListActivity.class);
                intent.putExtra("utype", "4");
                startActivity(intent);
            } else if (id == R.id.discover_mysub) {
                intent = new Intent();
                intent.setClass(mContext, TheQuesListActivity.class);
                intent.putExtra("utype", "2");
                startActivity(intent);
            } else if (id == R.id.intel_userinfo) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent();
                    intent.setClass(mContext, InfoFullFillActivity.class);
                    startActivity(intent);
                }
            } else if (id == R.id.intel_goal) {
                if (bInfoFullFill) {
//                    intent = new Intent();
//                    intent.setClass(mContext, IntelLearningActivity.class);
                    try {
                        startActivity(LearningGoalActivity.buildIntent(mContext,
                                Integer.valueOf(AccountManager.Instace(mContext).getId()),
                                AccountManager.Instace(mContext).getUserName()
                        ));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(6);
                }
            } else if (id == R.id.intel_result) {
                if (bLearnTarget) {
                    try {
                        startActivity(com.iyuba.module.intelligence.ui.LearnResultActivity.buildIntent(mContext,
                                Integer.valueOf(AccountManager.Instace(mContext).getId())
                        ));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(7);
                }
            } else if (id == R.id.intel_test_result) {
                if (bLearnTarget) {
                    try {
                        startActivity(com.iyuba.module.intelligence.ui.TestResultActivity.buildIntent(mContext,
                                Integer.valueOf(AccountManager.Instace(mContext).getId())
                        ));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(7);
                }
            } else if (id == R.id.intel_word_result) {
                if (bLearnTarget) {
                    try {
                        startActivity(com.iyuba.module.intelligence.ui.WordResultActivity.buildIntent(mContext,
                                Integer.valueOf(AccountManager.Instace(mContext).getId())
                        ));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(7);
                }

            } else if (id == R.id.study_ranking) {
                intent = new Intent();
                intent.setClass(mContext, StudyRankingActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.me, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        person = root.findViewById(R.id.personalhome);
        photo = root.findViewById(R.id.me_pic);
        name = root.findViewById(R.id.me_name);
        state = root.findViewById(R.id.me_state);
        attention = root.findViewById(R.id.me_attention);
        listem_time = root.findViewById(R.id.me_listem_time);
        position = root.findViewById(R.id.me_position);
        lv = root.findViewById(R.id.lv);
        fans = root.findViewById(R.id.me_fans);
        notification = root.findViewById(R.id.me_notification);

        integral = root.findViewById(R.id.Integral_notification);
        stateView = root.findViewById(R.id.me_state_change);
        vipView = root.findViewById(R.id.me_vip);
        me_privilege = root.findViewById(R.id.me_privilege);
        me_discover = root.findViewById(R.id.me_discover);
        messageView = root.findViewById(R.id.me_message);
        attentionView = root.findViewById(R.id.attention_area);
        fansView = root.findViewById(R.id.fans_area);

        discover_rqlist = root.findViewById(R.id.discover_rqlist);
        discover_qnotice = root.findViewById(R.id.discover_qnotice);
        discover_myq = root.findViewById(R.id.discover_myq);
        discover_mysub = root.findViewById(R.id.discover_mysub);

        //智能化学习
        intel_userinfo = root.findViewById(R.id.intel_userinfo);
        intel_learn_goal = root.findViewById(R.id.intel_goal);
        intel_result = root.findViewById(R.id.intel_result);
        intel_word_result = root.findViewById(R.id.intel_word_result);
        intel_test_result = root.findViewById(R.id.intel_test_result);
        study_ranking = root.findViewById(R.id.study_ranking);


        notificationView = root.findViewById(R.id.notification_area);
        integralView = root.findViewById(R.id.Integral);

        noLogin = root.findViewById(R.id.noLogin);
        login = root.findViewById(R.id.login);
        logout = root.findViewById(R.id.logout);
        back = root.findViewById(R.id.button_back);
        back.setVisibility(View.GONE);

        tv_sign = root.findViewById(R.id.tv_sign);
        root.findViewById(R.id.ll_about).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, About.class));
            }
        });
        root.findViewById(R.id.discover_iyubaset).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SetActivity.class));
            }
        });
        root.findViewById(R.id.rl_favorite).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,BasicFavorActivity.class));
            }
        });
        root.findViewById(R.id.rl_download).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,DLActivity.class));
            }
        });
        root.findViewById(R.id.rl_community).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigManager.Instance().putInt("quesAbilityType", 0);
                int type = 0;
                if (BuildConfig.isEnglish) {
                    if (BuildConfig.isCET4) {
                        type = 4;
                    } else {
                        type = 5;
                    }
                } else {
                    String tttt = Constant.APP_CONSTANT.TYPE();

                    if (tttt.equals("1")) {
                        type = 7;
                    } else if (tttt.equals("2")) {
                        type = 8;
                    } else if (tttt.equals("3")) {
                        type = 9;
                    }
                }

                ConfigManager.Instance().putInt("quesAppType", type + 100);

                startActivity(new Intent(mContext, CommunityActivity.class));
            }
        });
        root.findViewById(R.id.ll_souyisou).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountManager.Instace(mContext.getApplicationContext())
                        .checkUserLogin()) {

                    ImoocManager.userId = ConfigManager.Instance().loadString("userId");
                    ImoocManager.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
                    ImoocManager.appId = Constant.APPID;

                    IHeadlineManager.appId = Constant.APPID;
                    IHeadlineManager.appName = Constant.AppName;
                    IHeadlineManager.userId = Integer.parseInt(ConfigManager.Instance().loadString("userId"));
                    IHeadlineManager.username = AccountManager.Instace(getActivity()).userName;
                    IHeadlineManager.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
                } else {

                    ImoocManager.userId = "0";
                    ImoocManager.vipStatus = "0";
                    ImoocManager.appId = Constant.APPID;

                    IHeadlineManager.appId = Constant.APPID;
                    IHeadlineManager.appName = Constant.AppName;
                    IHeadlineManager.userId = 0;
                    IHeadlineManager.username = "";
                    IHeadlineManager.vipStatus = "0";

                }


                startActivity(MSearchActivity.buildIntent(getContext(), "noqw"));
//                startActivity(new Intent(mContext, MSearchActivity.class).putExtra("showtype", "noqw"));
            }
        });
        root.findViewById(R.id.ll_kanyikan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MovieActivity.class));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        showLocal = args.getBoolean("showLocal");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getUserVisibleHint() && isVisibleToUser) {
            checkLogin();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        viewChange();
        verifyUsrInfoAndTarget();
    }

    private void verifyUsrInfoAndTarget() {
        ExeProtocol.exe(new RequestUserDetailInfo(AccountManager.Instace(mContext).userId),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        ResponseUserDetailInfo responseUserDetailInfo = (ResponseUserDetailInfo) bhr;
                        if (responseUserDetailInfo.result.equals("211")) {
                            userDetailInfo = responseUserDetailInfo;
                            bInfoFullFill = !userDetailInfo.gender.equals("")
                                    && !userDetailInfo.birthday.equals("")
                                    && !userDetailInfo.resideLocation.equals("")
                                    && !userDetailInfo.occupation.equals("")
                                    && !userDetailInfo.education.equals("")
                                    && !userDetailInfo.graduateschool.equals("");

                            if (!userDetailInfo.editUserInfo.getPlevel().equals("") &&
                                    !userDetailInfo.editUserInfo.getPtalklevel().equals("") &&
                                    !userDetailInfo.editUserInfo.getPreadlevel().equals("") &&
                                    !userDetailInfo.editUserInfo.getGlevel().equals("") &&
                                    !userDetailInfo.editUserInfo.getGtalklevel().equals("") &&
                                    !userDetailInfo.editUserInfo.getGreadlevel().equals("") &&
                                    Integer.valueOf(userDetailInfo.editUserInfo.getPlevel()) > 0
                                    && Integer.valueOf(userDetailInfo.editUserInfo.getPtalklevel()) > 0
                                    && Integer.valueOf(userDetailInfo.editUserInfo.getPreadlevel()) > 0
                                    && Integer.valueOf(userDetailInfo.editUserInfo.getGlevel()) > 0
                                    && Integer.valueOf(userDetailInfo.editUserInfo.getGtalklevel()) > 0
                                    && Integer.valueOf(userDetailInfo.editUserInfo.getGreadlevel()) > 0)
                                bLearnTarget = true;
                        } else {
                            bLearnTarget = false;
                        }
                    }

                    @Override
                    public void error() {
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }


    private void viewChange() {
        if (checkLogin()) {
            userInfo = AccountManager.Instace(mContext).userInfo;
            ClientSession.Instace()
                    .asynGetResponse(
                            new RequestNewInfo(
                                    AccountManager.Instace(mContext).userId),
                            new IResponseReceiver() {
                                @Override
                                public void onResponse(
                                        BaseHttpResponse response,
                                        BaseHttpRequest request, int rspCookie) {
                                    ResponseNewInfo rs = (ResponseNewInfo) response;
                                    if (rs.letter > 0) {
                                        handler.sendEmptyMessage(2);
                                    } else {
                                        handler.sendEmptyMessage(5);
                                    }
                                }


                            });
            loadData();
        }
    }

    private boolean checkLogin() {
        if (!AccountManager.Instace(mContext).checkUserLogin()) {
            noLogin.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            loginBtn = root.findViewById(R.id.button_to_login);
            loginBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            });
            logout.setVisibility(View.GONE);
            tv_sign.setVisibility(View.GONE);
            return false;
        } else {
            noLogin.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            tv_sign.setVisibility(View.VISIBLE);
            if (TouristUtil.isTourist()) {
                logout.setText("注册/登录");

                logout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, Login.class);
                        startActivity(intent);
                    }
                });
                tv_sign.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "请注册正式用户后再进行打卡", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                logout.setText(getString(R.string.logout));
                tv_sign.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        new DailyBonusUtil(getActivity(), AccountManager.Instace(mContext).getId(), Constant.APPID)
//                                .setOkHttpClient(Http.getOkHttpClient())
//                                .sign(new DailyBonusUtil.DailyBonusCallback() {
//                                    @Override
//                                    public void onADClick(String s) {
//                                        Intent intent = new Intent();
//                                        intent.setClass(mContext, Web.class);
//                                        intent.putExtra("url", s);
//                                        startActivity(intent);
//                                    }
//
//                                    @Override
//                                    public void umengDailyBonusEvent() {
//                                        MobclickAgent.onEvent(mContext, "dailybonus");
//                                    }
//                                }, new PopupWindow.OnDismissListener() {
//                                    @Override
//                                    public void onDismiss() {
//                                        refreshIntegral();
//                                    }
//                                });
                        Intent intent = new Intent(mContext, SignActivity.class);
                        startActivity(intent);
                    }
                });
                logout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(
                                        getResources().getString(R.string.alert_title))
                                .setMessage(
                                        getResources().getString(R.string.logout_alert))
                                .setPositiveButton(
                                        getResources().getString(R.string.alert_btn_ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int whichButton) {
                                                handler.sendEmptyMessage(4);
                                            }
                                        })
                                .setNeutralButton(
                                        getResources().getString(R.string.alert_btn_cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                            }
                                        }).create();
                        dialog.show();
                    }
                });
            }
            return true;
        }
    }

    /**
     *
     */
    private void loadData() {
        final String id = AccountManager.Instace(mContext).getId();
        init();
        ExeProtocol.exe(new RequestBasicUserInfo(id, id),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        ResponseBasicUserInfo response = (ResponseBasicUserInfo) bhr;
                        userInfo = response.userInfo;
                        AccountManager.Instace(mContext).setUserInfo(userInfo);
                        handler.sendEmptyMessage(3);
                        Looper.prepare();
                        ExeProtocol.exe(new GradeRequest(id),
                                new ProtocolResponse() {

                                    @Override
                                    public void finish(BaseHttpResponse bhr) {
                                        GradeResponse response = (GradeResponse) bhr;
                                        userInfo.studytime = Integer
                                                .parseInt(response.totalTime);
                                        userInfo.position = response.positionByTime;
                                        handler.sendEmptyMessage(3);
                                    }

                                    @Override
                                    public void error() {
                                        handler.sendEmptyMessage(0);
                                    }
                                });
                        Looper.loop();
                    }

                    @Override
                    public void error() {
                    }
                });
    }

    private void refreshIntegral() {
        final String id = AccountManager.Instace(mContext).getId();
        ExeProtocol.exe(new RequestBasicUserInfo(id, id),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        ResponseBasicUserInfo response = (ResponseBasicUserInfo) bhr;
                        userInfo = response.userInfo;
                        AccountManager.Instace(mContext).setUserInfo(userInfo);
                        handler.sendEmptyMessage(3);
                    }

                    @Override
                    public void error() {
                    }
                });
    }

    /**
     *
     */
    private void init() {
        if (AccountManager.Instace(mContext).isteacher.equals("1")) {
            discover_mysub.setVisibility(View.GONE);
            discover_myq.setVisibility(View.VISIBLE);
        } else {
            discover_myq.setVisibility(View.GONE);
            discover_mysub.setVisibility(View.VISIBLE);
        }

        setViewClick();
        if (userInfo != null) {
            setTextViewContent();
        }
    }

    /**
     *
     */
    private void setViewClick() {
        person.setOnClickListener(ocl);
        stateView.setOnClickListener(ocl);
        vipView.setOnClickListener(ocl);
        me_privilege.setOnClickListener(ocl);
        me_discover.setOnClickListener(ocl);
        messageView.setOnClickListener(ocl);
        attentionView.setOnClickListener(ocl);
        fansView.setOnClickListener(ocl);
        stateView.setOnClickListener(ocl);
        notificationView.setOnClickListener(ocl);
        integralView.setOnClickListener(ocl);
        discover_rqlist.setOnClickListener(ocl);
        discover_qnotice.setOnClickListener(ocl);
        discover_myq.setOnClickListener(ocl);
        discover_mysub.setOnClickListener(ocl);

        intel_userinfo.setOnClickListener(ocl);
        intel_learn_goal.setOnClickListener(ocl);
        intel_result.setOnClickListener(ocl);
        intel_word_result.setOnClickListener(ocl);
        intel_test_result.setOnClickListener(ocl);
        study_ranking.setOnClickListener(ocl);

    }

    /**
     *
     */
    private void setTextViewContent() {
        GitHubImageLoader.Instace(mContext).setCirclePic(
                AccountManager.Instace(mContext).userId, photo);

        if (TouristUtil.isTourist()) { // 临时账户显示为uId
            name.setText(AccountManager.Instace(mContext).userId);
        } else {
            name.setText(userInfo.username);
        }

        if (ConfigManager.Instance().loadInt("isvip") > 0) {
            Drawable img = mContext.getResources().getDrawable(R.drawable.vip);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            name.setCompoundDrawables(null, null, img, null);
        } else {
            Drawable img = mContext.getResources().getDrawable(
                    R.drawable.no_vip);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            name.setCompoundDrawables(null, null, img, null);
        }
        if (userInfo.text == null) {
            state.setText(R.string.social_default_state);
        } else {

            String zhengze = "image[0-9]{2}|image[0-9]";
            Emotion emotion = new Emotion();
            userInfo.text = Emotion.replace(userInfo.text);
            SpannableString spannableString = Expression.getExpressionString(
                    mContext, userInfo.text, zhengze);
            state.setText(spannableString);
        }
        attention.setText(userInfo.following);
        fans.setText(userInfo.follower);
        listem_time.setText(exeStudyTime());
        position.setText(exePosition());
        lv.setText(exeIyuLevel());
        notification.setText(userInfo.notification);
        integral.setText(userInfo.icoins);
    }

    private String exeStudyTime() {
        StringBuffer sb = new StringBuffer(
                mContext.getString(R.string.me_study_time));
        int time = userInfo.studytime;
        int minus = time % 60;
        int minute = time / 60 % 60;
        int hour = time / 60 / 60;
        if (hour > 0) {
            sb.append(hour).append(mContext.getString(R.string.me_hour));
        } else if (minute > 0) {
            sb.append(minute).append(mContext.getString(R.string.me_minute));
        } else {
            sb.append(minus).append(mContext.getString(R.string.me_minus));
        }
        return sb.toString();
    }

    private String exePosition() {
        StringBuffer sb = new StringBuffer(
                mContext.getString(R.string.me_study_position));
        int position = Integer.parseInt(userInfo.position);

        if (position < 10000) {
            sb.append(position);
        } else {
            sb.append(position / 10000 * 10000).append("+");
        }
        return sb.toString();
    }

    private String exeIyuLevel() {
        StringBuffer sb = new StringBuffer("");
        sb.append(mContext.getString(R.string.me_lv));
        sb.append(CheckGrade.Check(userInfo.icoins));
        sb.append(" ");
        sb.append(CheckGrade.CheckLevelName(userInfo.icoins));
        // sb.append("   ");
        // sb.append(mContext.getString(R.string.me_score));
        // sb.append(userInfo.icoins);
        return sb.toString();
    }
}
