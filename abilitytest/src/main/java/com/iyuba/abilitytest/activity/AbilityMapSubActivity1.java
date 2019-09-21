package com.iyuba.abilitytest.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityQuestion;
import com.iyuba.abilitytest.entity.AbilityResult;
import com.iyuba.abilitytest.entity.ExamDetail;
import com.iyuba.abilitytest.entity.ExamScore;
import com.iyuba.abilitytest.entity.TestCategory;
import com.iyuba.abilitytest.manager.DataManager;
import com.iyuba.abilitytest.network.AbilityTestRequestFactory;
import com.iyuba.abilitytest.sqlite.TestRecordHelper;
import com.iyuba.abilitytest.utils.CommonUtils;
import com.iyuba.abilitytest.utils.NetWorkState;
import com.iyuba.abilitytest.widget.DrawView;
import com.iyuba.abilitytest.widget.WaittingDialog;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.Login;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.ToastUtil;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.multithread.DownloadProgressListener;
import com.iyuba.multithread.FileDownloader;
import com.iyuba.multithread.MultiThreadDownloadManager;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 展示听说读写各个模块能力图谱,加载网络试题
 * 2017.1.6 添加了练习模块维度的点击事件
 * 2017.3.6非vip用户可以练习10个题目
 *
 * @author liuzhenli  2016/9/? 16:42
 * @version 1.0.0
 */

public class AbilityMapSubActivity1 extends AppBaseActivity {
    private static final String TAG = "AbilityMapSubActivity";
    private final int FLAG_TEST = 1;//模式标记 1代表测评  2 代表练习 3查看答题记录
    private final int FLAG_PRACTICE = 2;//模式标记 1代表测评  2 代表练习  3查看答题记录
    private final int FLAG_SHOW_ANA = 3;//模式标记 1代表测评  2 代表练习  3查看答题记录

    private final int FLAG_DORIGHT = 1;//数据表中的答题状态 0打错 1答对  2 未答 3所有
    private final int FLAG_DOWRONG = 0;//答错的题目
    private final int FLAG_UNDO = 2;//未答的题目
    private final int FLAG_ALL = 3;//所有题目

    private int[] mScoreArr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//每个纬度对应的成绩  精确到个位
    private int practiceCount = 200;//定义练习题目的个数
    private String[] abilityTypeLocal = {};//
    private Context mContext;
    private final int FINISHSELF = 10000;
    private final int GETTESTCONTENTS = 10001;
    private final int BEGINTEST = 10002;
    private final int SHOW_DIALOG = 10003;//提示框  是否进行测试
    private final int DOWNLOAD_AUDIO = 10004;
    private final int GET_NET_RESULT = 10005;
    private final int GET_PRACTICE_RESULT_NET = 10006;//从服务器获取用户的答题记录 练习项
    private final int CHANGE_WAITBAR_TEXT = 10007;//改变waitbar 的文字
    private final int TO_ANALYSIS = 10008;
    /**
     * 用户答对的试题个数
     */
    private int mDoRightNum;
    private int mTotalTestNum = 100;//试题总数
    private String lastTestTime = "未知";//上次测试时间
    private ArrayList<AbilityQuestion.TestListBean> mQuesLists;
    private LinearLayout ll_progress_indictor;
    private ProgressBar mPb_download_test;
    private TextView tv_download;
    private CustomDialog mWaittingDialog;
    private String mTestCategory;
    private Button btn_goto_test_wrong, btn_goto_test_undo;
    private Class mTargetClass;
    private Button mBtn_goto_test;//开始测试按钮
    private TextView mTv_lasttime_resut;//上一次测试的结果 展示测试时间接掌握情况
    private DrawView drawView;
    private String mTargetCategory;//听力L 单词 W

    //最后测试分析
    private String testRecordInfo;

    private String mTitle;
    private TextView tv_nextTextTime;
    private int flag_mode;

    private TestRecordHelper testRecordHelper;
    private ArrayList<TestCategory> categoryListAll;//分类所有题目
    private ArrayList<TestCategory> categoryListR;//分类 正确题目
    private ArrayList<TestCategory> categoryListW;//分类 错误题目
    private TextView tv_waitbar; //有数据的时候 跟新文字

    private int mPTatal_All;//练习题目总数
    private int mPTatal_Wrong;//打错的题目总数
    private int mPTatal_Undo;//未答的题目总数
    private int mPTatal_Right;//答对的题目总数
    private String[] abilityType = {};
    private int mPritaceType;
    private boolean update_pritace;//一个标记 决定是否更新练习题目
    private int typeId;//根据听说读写,保存到数据表中用
    private String mCategory = "all";//维度 测试类型 all 表示所有维度

    private int flag_test_analy;//标记是跳往解析界面还是测试界面
    private int mLessonId;
    private boolean mIgnoreCount;//获取所有题目  决定是否根据vip限制题目数量

    @Override
    protected int getLayoutResId() {
        return R.layout.act_abilitymapsub;
    }

    @Override
    protected void initVariables() {
        mContext = this;
        testRecordHelper = TestRecordHelper.getInstance(mContext);
        alertDialog = new CustomDialog(mContext, R.style.dialog_style);
        mTestCategory = getIntent().getStringExtra("abilityType");//W--单词,G--语法,L--听力,S--口语,R--阅读,X--写作
        mLessonId = getIntent().getIntExtra("lessonId", -1);//单元
        setCurrentTypeId(mTestCategory);
        flag_mode = getIntent().getIntExtra("mode", FLAG_TEST);
        flag_test_analy = flag_mode;
        mWaittingDialog = new WaittingDialog().wettingDialog(mContext);//waiting dialog
        tv_waitbar = mWaittingDialog.findViewById(R.id.hint);
        mQuesLists = new ArrayList<>();
        mIgnoreCount = CommonUtils.ignoreCount(Constant.mListen);
        //判断flag_mode的值  如果是练习模式,先去本地查找是否有数据
        if (flag_mode == FLAG_PRACTICE) {
            update_pritace = true;//第一次  更新
            initPracticeData();
        }
    }

    /**
     * 如果界面是练习模式  初始化练习题目
     */
    private void initPracticeData() {
        //从本地获取练习题目总数,正确题目,错误题目总数
        if (categoryListAll != null) categoryListAll.clear();
        categoryListAll = testRecordHelper.getCategoryTypeAndCount(getUid(), mTestCategory, 3, mLessonId);//获取题目类型及总个数
        if (categoryListR != null) categoryListR.clear();
        categoryListR = testRecordHelper.getCategoryTypeAndCount(getUid(), mTestCategory, 1, mLessonId);//分类 正确题目
        if (categoryListW != null) categoryListW.clear();
        categoryListW = testRecordHelper.getCategoryTypeAndCount(getUid(), mTestCategory, 0, mLessonId);//分类 错误题目
        boolean hasData = categoryListAll.size() > 0;
        if (hasData) {//有本地数据
            if (NetWorkState.getAPNType() == 0) {//有网
                Toast.makeText(AbilityMapSubActivity1.this, "网络开小差了", Toast.LENGTH_SHORT).show();
            } else if (update_pritace) {
                handler.sendEmptyMessage(GET_PRACTICE_RESULT_NET);//访问网络 更新数据库
            }

            abilityType = new String[categoryListAll.size()];//给数据库里面的题目分类
            mScoreArr = new int[categoryListAll.size()];//每个类型对应的数量

            mPTatal_All = 0;//练习题目的总数
            mPTatal_Right = 0;//练习题目正确的总数
            mPTatal_Wrong = 0;//练习题目错误的总数

            for (int i = 0; i < categoryListAll.size(); i++) {
                mPTatal_All += categoryListAll.get(i).count;
                abilityType[i] = categoryListAll.get(i).type;
                for (int j = 0; j < categoryListR.size(); j++) {
                    if (categoryListAll.get(i).type.equals(categoryListR.get(j).type)) {
                        mScoreArr[i] = categoryListR.get(j).count * 100 / categoryListAll.get(i).count;
                    }
                }
            }

            for (int i = 0; i < categoryListR.size(); i++) {
                mPTatal_Right += categoryListR.get(i).count;
            }

            for (int i = 0; i < categoryListW.size(); i++) {
                mPTatal_Wrong += categoryListW.get(i).count;
            }
        } else {//没有本地数据
            if (mTestCategory.equals("W")) abilityType = Constant.WORD_TEST_ARR;//单词
            if (mTestCategory.equals("L")) abilityType = Constant.LISTEN_TEST_ARR;//听力
            if (mTestCategory.equals("R")) abilityType = Constant.READ_TEST_ARR;//阅读
            if (mTestCategory.equals("X")) abilityType = Constant.WRITE_TEST_ARR;//写作
            if (mTestCategory.equals("G")) abilityType = Constant.GRAM_TEST_ARR;//语法
            if (mTestCategory.equals("S")) abilityType = Constant.SPEAK_TEST_ARR;//口语

            if (isUserLogin())
                autoRefreshPractaceData(true, true);
        }
        mPTatal_Undo = mPTatal_All - mPTatal_Right - mPTatal_Wrong;//未打题目的数量
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        initTitlebar();
        drawView = new DrawView(this);
        drawMap();
        TextView tv_title = findView(R.id.tv_titlebar_sub);
        String titleBarstr = "我的能力图谱(" + mTitle + ")";
        tv_title.setText(titleBarstr);
        mBtn_goto_test = findView(R.id.btn_goto_test);//flag_mode=1时 加载试题  flag_mode=2时显示正确的题目
        if (flag_mode == FLAG_TEST) {//测评模式
            mTv_lasttime_resut = findView(R.id.tv_result_advice);
            mTv_lasttime_resut.setVisibility(View.VISIBLE);
            tv_nextTextTime = findView(R.id.tv_next_testtime);
            tv_nextTextTime.setVisibility(View.VISIBLE);
            setNextTime(lastTestTime);
            if (mTestCategory.equals(Constant.ABILITY_WORD))
                testRecordInfo = "上次测评时间:" + lastTestTime + "\n考试总词汇:4521个\n" + "您目前掌握的词汇:[[" + mDoRightNum * 4521 / mTotalTestNum + "]]个";
            else
                testRecordInfo = "上次测评时间:" + lastTestTime + "\n" + mTitle + "测评总分:100分\n" + "您的测评成绩为:[[" + mDoRightNum * 100 / mTotalTestNum + "]]分";
            CommonUtils.showTextWithUnderLine(mTv_lasttime_resut, testRecordInfo);
        } else if (flag_mode == FLAG_PRACTICE) {//练习模式
            initPractiveViews();
        }
        ll_progress_indictor = findView(R.id.ll_progress_indictor);//进度展示  包括进度条和百分比
        mPb_download_test = findView(R.id.pb_download);//下载的进度条
        tv_download = findView(R.id.tv_download);//下载进度百分比
        findViewById(R.id.ibtn_test_guide).setVisibility(View.GONE);
    }

    private void initTitlebar() {
        ImageButton selector_btn_bg = findView(R.id.btn_nav_sub);
        if (selector_btn_bg != null) {
            selector_btn_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void loadData() {
        drawView.setOnPositinoClickListener(positionClickListener);
        mBtn_goto_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPritaceType = FLAG_DORIGHT;//正确
                mCategory = "all";
                flag_test_analy = flag_mode;
                gotoAbilityTestClickListener();
            }
        });
    }

    /**
     * 维度文字点击事件
     */
    private DrawView.PositionClickListener positionClickListener = new DrawView.PositionClickListener() {
        @Override
        public void itemSelected(final int itmeId) {
            LogUtils.e(TAG, "itemId: " + itmeId);
            if (!isUserLogin()) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Login.class);
                startActivity(intent);
            } else if (flag_mode == FLAG_PRACTICE && itmeId > -1 && itmeId < abilityTypeLocal.length && categoryListAll.size() > 0) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(abilityTypeLocal[itmeId]);
                builder.setMessage("题目总数 :" + categoryListAll.get(itmeId).count + "\n" +
                        "答对个数 :" + categoryListAll.get(itmeId).count * mScoreArr[itmeId] / 100 + "\n" +
                        // "学习进度 :" + mScoreArr[itmeId] + "%\n" +
                        "查看答题记录?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCategory = abilityTypeLocal[itmeId];
                        mPritaceType = FLAG_ALL;//所有题目
                        flag_test_analy = FLAG_SHOW_ANA;
                        gotoAbilityTestClickListener();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
    };

    /**
     * 自动加载(如果用户登录并且没有题目)  并更新练习题目
     *
     * @param isDeleteData 是否删除数据
     * @param isRefresh    是否更新数据
     */
    private void autoRefreshPractaceData(final boolean isDeleteData, boolean isRefresh) {
        mWaittingDialog.show();
        mCompleteNum = 0;//点击初始化,否则下载是从头开始的,但是默认已经完成了上一次的个数.
        String lesson = Constant.mListen;
        LogUtils.e(TAG, "准备出题了-----");
        MD5 md5 = new MD5();
        //sign加密方式: md5(lesson+category+"yyyy-MM-dd" )
        String sign = MD5.getMD5ofStr(lesson + mTestCategory + getCurTime());
        String protocol = "20000";
        Call<AbilityQuestion> call = new AbilityTestRequestFactory().getTestQuestionApi().testQuestionApi(protocol, lesson, mTestCategory, sign, "json", flag_mode, getUid(), mLessonId);
        call.enqueue(new Callback<AbilityQuestion>() {
            @Override
            public void onResponse(Call<AbilityQuestion> call, Response<AbilityQuestion> res) {
                if (res.body() != null && res.body().getTestList() != null && res.body().getTestList().size() > 0) {
                    if (isDeleteData) {
                        now_doing = "正在删除旧数据...";
                        handler.sendEmptyMessage(CHANGE_WAITBAR_TEXT);
                        //如果服务器数据已经获取 删除数据库中已有的数据
                        boolean isSuccess = testRecordHelper.delPracticeDataByUser(getUid(), mTestCategory);
                        //数据库中插入新的题目
                        now_doing = "正在存储新数据...";
                        handler.sendEmptyMessage(CHANGE_WAITBAR_TEXT);
                        boolean b = testRecordHelper.insertPractices(res.body().getTestList(), mTestCategory, getUid());
                    }
                    handler.sendEmptyMessage(GET_PRACTICE_RESULT_NET);//再次访问服务器,更新插入的内容
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(mContext, "无数据更新");
                            mWaittingDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AbilityQuestion> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(mContext, "数据请求出错:");
                        mWaittingDialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 初始化练习试题界面
     */
    private void initPractiveViews() {
        ImageButton ibtn_test_reload = findView(R.id.ibtn_test_reload);//重新加载题目数据按钮
        ibtn_test_reload.setVisibility(View.VISIBLE);
        ibtn_test_reload.setOnClickListener(new View.OnClickListener() {//用户登录状态下更新数据
            @Override
            public void onClick(View v) {
                if (checkUserLoginAndLogin()) {//用户登录的情况下可以自动刷新
                    autoRefreshPractaceData(true, true);
                }
            }
        });
        if (categoryListAll != null && categoryListAll.size() > 0) {
            mBtn_goto_test.setText("正确(" + mPTatal_Right + ")");
            btn_goto_test_wrong = findView(R.id.btn_goto_test_wrong);//回答错去的
            btn_goto_test_wrong.setVisibility(View.VISIBLE);
            btn_goto_test_wrong.setText("错误(" + mPTatal_Wrong + ")");
            btn_goto_test_undo = findView(R.id.btn_goto_test_undo);//没有回答的
            btn_goto_test_undo.setVisibility(View.VISIBLE);
            btn_goto_test_undo.setText("未答(" + mPTatal_Undo + ")");

            btn_goto_test_wrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPritaceType = FLAG_DOWRONG;
                    mCategory = "all";
                    flag_test_analy = flag_mode;
                    gotoAbilityTestClickListener();
                }
            });
            btn_goto_test_undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPritaceType = FLAG_UNDO;
                    mCategory = "all";
                    flag_test_analy = flag_mode;
                    gotoAbilityTestClickListener();
                }
            });
        } else {
            mBtn_goto_test.setText("加载题目");
        }
    }

    /**
     * 加载试题  进入单词时界面  练习模式可以跳转到正确题目
     */
    private void gotoAbilityTestClickListener() {
        switch (flag_mode) {
            case 1://测评模式
                //判断距离上一次测试时间是否够7天 用户登录状态下判断是否可以测试
                if (isUserLogin() && lastTestTime.length() > 10 && !CommonUtils.timeOver(lastTestTime, 7)) {
                    Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.shakerl);
                    tv_nextTextTime.startAnimation(anim);
                    return;
                }
                if (checkUserLoginAndLogin()) {//需要在用户登录状态下才可以加载试题
                    if (mQuesLists == null || mQuesLists.size() == 0 || mCompleteNum < mTotalFileCount2Down)//当前没有出题,开始出题
                    {
                        mWaittingDialog.show();
                        handler.sendEmptyMessage(GETTESTCONTENTS);//从服务器获取试题内容
                    } else {//题目已经出完
                        mBtn_goto_test.setText("开始测评");
                        if (!alertDialog.isShowing())
                            handler.sendEmptyMessage(SHOW_DIALOG);
                    }
                }
                break;
            case 2://练习模式
                //如果用户不是vip 可以答题10道,从未答题目中选取.
                if (isUserLogin() && !isVip() && mPritaceType == FLAG_UNDO && !mIgnoreCount) {
                    practiceCount = 10;
                }
                boolean todayOver = isUserLogin() && CommonUtils.alreadyFinshTodayTest(mTestCategory);

                if (!isUserLogin()) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else if (isVip() || todayOver || (isUserLogin() && mPritaceType != FLAG_UNDO)) {//今天没有答题  或者用户是vip 才可以加载试题 非vip用户从未答题目中选题目计算
                    boolean hasLocalData = testRecordHelper.getParacticeTitles1(getUid(), mCategory, mTestCategory, 3, 3, mLessonId).size() > 0;//判断本地有无试题
                    if (hasLocalData) {//本地加载题目 需要判断音频是否存在
                        if (flag_test_analy == FLAG_SHOW_ANA) {//获取查看答题记录的题目
                            // 1.用户id  2.测试类型--听说读写  3.答对-打错-未答-所有 4.题目数量
                            mQuesLists = testRecordHelper.getParacticeTitles1(getUid(), mCategory, mTestCategory, mPritaceType, practiceCount, mLessonId);//获取指定数量的题目
                        } else if (mQuesLists == null || mQuesLists.size() == 0) {//避免不能重复加载
                            mQuesLists = testRecordHelper.getParacticeTitles1(getUid(), mCategory, mTestCategory, mPritaceType, practiceCount, mLessonId);//获取指定数量的题目
                        }
                        if (mQuesLists == null || mQuesLists.size() == 0) {
                            alertDialog.dismiss();
                            ToastUtil.showToast(mContext, "没有题目");
                            return;
                        }
                        initDownloads();//检测音频是否存在
                    } else if (mQuesLists == null || mQuesLists.size() == 0 || mCompleteNum < mTotalFileCount2Down) {//当前没有出题,开始出题
                        if (flag_test_analy == FLAG_SHOW_ANA) {
                            ToastUtil.showToast(mContext, "该模块你还没有练习过呢^_^……");
                            return;
                        }
                        mWaittingDialog.show();
                        handler.sendEmptyMessage(GETTESTCONTENTS);//从服务器获取试题内容
                    } else {//题目已经出完
                        mBtn_goto_test.setText("正确题目");//这里应该显示正确
                        if (!alertDialog.isShowing())
                            handler.sendEmptyMessage(SHOW_DIALOG);
                    }
                } else {
                    ToastUtil.showToast(mContext, "非vip用户每天可以答10道题目!");
                }
                break;
        }
    }

    /**
     * 绘制图谱
     */
    private void drawMap() {
        //用户没有登录,使用本地的数据
        ArrayList<AbilityResult> abilityResults = (ArrayList<AbilityResult>) getIntent().getSerializableExtra("resultLists");
        switch (mTestCategory) {
            case Constant.ABILITY_WRITE:
                mTitle = "写作";
                if (!mIgnoreCount) practiceCount = 40;//用于加载默认的题目个数
                if (flag_mode == FLAG_TEST) {
                    abilityTypeLocal = Constant.WRITE_ABILITY_ARR;
                    if (abilityResults != null && abilityResults.size() > 0) {
                        mScoreArr = getResults(abilityResults.get(abilityResults.size() - 1));
                    }
                } else if (flag_mode == FLAG_PRACTICE) {
                    abilityTypeLocal = abilityType;
                }
                drawView.setData(abilityTypeLocal, mScoreArr);
                drawMap(drawView);
                mTargetCategory = Constant.ABILITY_WRITE;
//                mTargetClass = WriteAbilityTestActivity.class;
                mTargetClass = AbilityTestActivity.class;
                if (flag_mode == FLAG_TEST) {
                    getUserRes();//从服务器获取每个纬度的成绩
                }
                break;
            case Constant.ABILITY_WORD:
                mTitle = "单词";
                if (!mIgnoreCount) practiceCount = 60;
                if (flag_mode == FLAG_TEST) {
                    abilityTypeLocal = Constant.WORD_ABILITY_ARR;
                    if (abilityResults != null && abilityResults.size() > 0)
                        mScoreArr = getResults(abilityResults.get(abilityResults.size() - 1));
                } else if (flag_mode == FLAG_PRACTICE) {
                    abilityTypeLocal = abilityType;
                }
                mTargetCategory = Constant.ABILITY_WORD;
                mTargetClass = AbilityTestActivity.class;
                drawView.setData(abilityTypeLocal, mScoreArr);
                drawMap(drawView);
                if (flag_mode == FLAG_TEST) {
                    getUserRes();
                }
                break;
            case Constant.ABILITY_GRAMMAR:
                mTitle = "语法";
                if (!mIgnoreCount) practiceCount = 50;
                if (flag_mode == FLAG_TEST) {
                    abilityTypeLocal = Constant.GRAM_ABILITY_ARR;
                    if (abilityResults != null && abilityResults.size() > 0)
                        mScoreArr = getResults(abilityResults.get(abilityResults.size() - 1));
                } else if (flag_mode == FLAG_PRACTICE) {
                    abilityTypeLocal = abilityType;
                }
                drawView.setData(abilityTypeLocal, mScoreArr);
                mTargetCategory = Constant.ABILITY_GRAMMAR;
                mTargetClass = AbilityTestActivity.class;
                drawMap(drawView);
                if (flag_mode == FLAG_TEST) {
                    getUserRes();
                }
                break;

            case Constant.ABILITY_LISTEN:
                mTitle = "听力";
                if (!mIgnoreCount) practiceCount = 35;
                if (flag_mode == FLAG_TEST) {
                    abilityTypeLocal = Constant.LIS_ABILITY_ARR;
                    if (abilityResults != null && abilityResults.size() > 0)
                        mScoreArr = getResults(abilityResults.get(abilityResults.size() - 1));
                } else if (flag_mode == FLAG_PRACTICE) {
                    abilityTypeLocal = abilityType;
                }
                drawView.setData(abilityTypeLocal, mScoreArr);
                mTargetCategory = Constant.ABILITY_LISTEN;
//                mTargetClass = ListenAbilityTestActivity.class;
                mTargetClass = AbilityTestActivity.class;
                drawMap(drawView);
                if (flag_mode == FLAG_TEST) {
                    getUserRes();
                }
                break;

            case Constant.ABILITY_SPEAK:
                mTitle = "口语";
                if (!mIgnoreCount) practiceCount = 30;
                if (flag_mode == FLAG_TEST) {//测评模式
                    abilityTypeLocal = Constant.SPEAK_ABILITY_ARR;
                    if (abilityResults != null && abilityResults.size() > 0)
                        mScoreArr = getResults(abilityResults.get(abilityResults.size() - 1));
                } else {//练习模式
                    abilityTypeLocal = abilityType;
                }
                drawView.setData(abilityTypeLocal, mScoreArr);
                mTargetCategory = Constant.ABILITY_SPEAK;
                mTargetClass = SpeakAbilityTestActivity.class;
                drawMap(drawView);
                if (flag_mode == FLAG_TEST) {
                    getUserRes();
                }
                break;
            case Constant.ABILITY_READ:
                mTitle = "阅读";
                if (!mIgnoreCount) practiceCount = 50;
                if (flag_mode == FLAG_TEST) {//测评模式
                    abilityTypeLocal = Constant.READ_ABILITY_ARR;//默认的测试维度
                    if (abilityResults != null && abilityResults.size() > 0) {
                        mScoreArr = getResults(abilityResults.get(abilityResults.size() - 1));
                    }
                } else {//练习模式
                    abilityTypeLocal = abilityType;//从数据库获取的维度
                }
                drawView.setData(abilityTypeLocal, mScoreArr);
                mTargetCategory = Constant.ABILITY_READ;
                mTargetClass = AbilityTestActivity.class;
                drawMap(drawView);
                if (flag_mode == FLAG_TEST) {//get abilityResult from network
                    getUserRes();
                }
                break;
            default:
                practiceCount = 25;
                break;
        }
    }


    /**
     * 在指定的view上绘制图谱
     *
     * @param view 绘制图谱的view
     */
    private void drawMap(DrawView view) {
        LinearLayout layout = findView(R.id.root);
        layout.removeAllViews();
        view.setMinimumHeight((int) (300 * CommonUtils.getScreenDensity(mContext)));//设置最小高度
        view.setMinimumWidth((int) (300 * CommonUtils.getScreenDensity(mContext)));//设置最小宽度
        view.invalidate();//通知view组件重绘
        layout.addView(view);
    }

    /**
     * 从服务器获取用于的各个模块的水平
     */
    private void getUserRes() {
        if (isUserLogin()) {//从服务器获取数据
            Message msg = handler.obtainMessage();
            msg.what = GET_NET_RESULT;
            handler.sendMessage(msg);
        }
    }

    /**
     * 获取数据库中存储的内容: Score1  Score2 ......
     *
     * @return sorts of test results
     */
    private int[] getResults(AbilityResult abilityResult) {
        int resCount = 0;
        if (!abilityResult.Score10.equals("-1")) {
            resCount = 10;
        } else if (!abilityResult.Score9.equals("-1")) {
            resCount = 9;
        } else if (!abilityResult.Score8.equals("-1")) {
            resCount = 8;
        } else if (!abilityResult.Score7.equals("-1")) {
            resCount = 7;
        } else if (!abilityResult.Score6.equals("-1")) {
            resCount = 6;
        } else if (!abilityResult.Score5.equals("-1")) {
            resCount = 5;
        } else if (!abilityResult.Score4.equals("-1")) {
            resCount = 4;
        } else if (!abilityResult.Score3.equals("-1")) {
            resCount = 3;
        } else if (!abilityResult.Score2.equals("-1")) {
            resCount = 2;
        } else if (!abilityResult.Score1.equals("-1")) {
            resCount = 1;
        }
        mScoreArr = new int[resCount];
        abilityTypeLocal = new String[resCount];

        mDoRightNum = 0;
        lastTestTime = "未知";
        //根据每个题目的类型转化成百分制成绩进行展示
        switch (resCount) {
            case 10:
                mScoreArr[9] = getResult(abilityResult.Score10);
                abilityTypeLocal[9] = getType(abilityResult.Score10);
            case 9:
                mScoreArr[8] = getResult(abilityResult.Score9);
                abilityTypeLocal[8] = getType(abilityResult.Score9);
            case 8:
                mScoreArr[7] = getResult(abilityResult.Score8);
                abilityTypeLocal[7] = getType(abilityResult.Score8);
            case 7:
                mScoreArr[6] = getResult(abilityResult.Score7);
                abilityTypeLocal[6] = getType(abilityResult.Score7);
            case 6:
                mScoreArr[5] = getResult(abilityResult.Score6);
                abilityTypeLocal[5] = getType(abilityResult.Score6);
            case 5:
                mScoreArr[4] = getResult(abilityResult.Score5);
                abilityTypeLocal[4] = getType(abilityResult.Score5);
            case 4:
                mScoreArr[3] = getResult(abilityResult.Score4);
                abilityTypeLocal[3] = getType(abilityResult.Score4);
            case 3:
                mScoreArr[2] = getResult(abilityResult.Score3);
                abilityTypeLocal[2] = getType(abilityResult.Score3);
            case 2:
                mScoreArr[1] = getResult(abilityResult.Score2);
                abilityTypeLocal[1] = getType(abilityResult.Score2);
            case 1:
                mScoreArr[0] = getResult(abilityResult.Score1);
                abilityTypeLocal[0] = getType(abilityResult.Score1);
                break;
        }
        mDoRightNum = abilityResult.DoRight;
        lastTestTime = abilityResult.endTime;
        mTotalTestNum = abilityResult.Total;
        return mScoreArr;
    }

    private String getType(String score) {
        return score.split("\\+\\+")[2];
    }

    /**
     * 根据数据表中获取的Score信息,转换为百分制的分数
     *
     * @param score 存储格式:本类型试题总数+用户答对的试题个数+测试类型
     * @return the score of this category
     */
    private int getResult(String score) {
        mTotalTestNum = Integer.parseInt(score.split("\\+\\+")[0]);//试题总数
        int userScore = Integer.parseInt(score.split("\\+\\+")[1]);//用户答对的题数
        return mTotalTestNum == 0 ? 0 : userScore * 100 / mTotalTestNum;
    }

    private String now_doing = "加载中";
    private int mTotalFileCount2Down;//总共需要下载的文件数量
    private ArrayList<AbilityQuestion.TestListBean> mListWithSound; //需要下载音频
    private ArrayList<AbilityQuestion.TestListBean> mListWithImage;//需要下载图片
    private ArrayList<AbilityQuestion.TestListBean> mListWithAttach;//需要下载Txt文档
    private int mTestTime;
    private MD5 md5 = new MD5();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FINISHSELF:
                    finish();
                    break;
                case GETTESTCONTENTS://从网络获取试题内容
                    mCompleteNum = 0;//点击初始化,否则下载是从头开始的,但是默认已经完成了上一次的个数.
                    String lesson = Constant.mListen;
                    LogUtils.e(TAG, "准备出题了-----");
                    String sign;
                    //sign加密方式: md5(lesson+category+"yyyy-MM-dd" )
                    sign = MD5.getMD5ofStr(lesson + mTestCategory + getCurTime());

                    String protocol = "20000";
                    Call<AbilityQuestion> call = new AbilityTestRequestFactory().getTestQuestionApi().testQuestionApi(protocol, lesson, mTestCategory, sign, "json", flag_mode, getUid(), mLessonId);
                    call.enqueue(new retrofit2.Callback<AbilityQuestion>() {
                        @Override
                        public void onResponse(Call<AbilityQuestion> call, Response<AbilityQuestion> res) {
                            if (res.body() != null && res.body().getResult().equals("1")) {
                                mWaittingDialog.dismiss();
                                mTestTime = res.body().getTime();
                                LogUtils.e(TAG, "出题成功了");
                                if (mQuesLists != null && mQuesLists.size() > 0) {
                                    mQuesLists.clear();
                                }
                                //判断如果是练习题目,需要保存到数据库
                                if (flag_mode == FLAG_PRACTICE && res.body().getTestList().size() > 0) {
                                    //插入之前先删除
                                    boolean bool = testRecordHelper.delPracticeDataByUser(getUid(), mTestCategory);
                                    //保存题目到数据库等插入到数据表之后,接着请求服务器,获取答题记录并修改数据表
                                    boolean b = testRecordHelper.insertPractices2(res.body().getTestList(), mTestCategory, getUid());
                                    handler.sendEmptyMessage(GET_PRACTICE_RESULT_NET);
                                }
                                mQuesLists.addAll(res.body().getTestList());
                                initDownloads();//初始化需要下载的信息
                            } else {
                                LogUtils.e(TAG, "返回数据为空,出题失败了");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWaittingDialog.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<AbilityQuestion> call, Throwable t) {
                        }
                    });
                    break;
                case BEGINTEST://开始测试
                    if (flag_mode == FLAG_UNDO && !isVip()) {//if the user is not vip, remember the date
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date now = new Date();
                        String today = format.format(now);
                        ConfigManager.Instance().putString("abilityTest-" + mTestCategory, today);
                    }

                    handler.sendEmptyMessage(FINISHSELF);
                    Intent intent = new Intent();
                    intent.putExtra("QuestionList", mQuesLists);
                    intent.putExtra("testTime", mTestTime);
                    intent.putExtra("mode", flag_mode);
                    intent.putExtra("testCategory", mTargetCategory);
                    intent.setClass(AbilityMapSubActivity1.this, mTargetClass);
                    startActivity(intent);
                    break;
                case TO_ANALYSIS://结果分析
                    intent = new Intent();
                    DataManager.getInstance().practiceList = mQuesLists;
                    intent.putExtra("testTime", mTestTime);
                    intent.putExtra("mode", flag_mode);
                    intent.setClass(AbilityMapSubActivity1.this, ShowAnalysisActivity.class);
                    startActivity(intent);
                    break;
                case SHOW_DIALOG:
                    if (mQuesLists.size() == 0) {
                        ToastUtil.showToast(mContext, "抱歉，该模块暂时没有题目");
                        return;
                    }
                    showToTestDialog();
                    break;
                case DOWNLOAD_AUDIO:
                    if (mTotalFileCount2Down < mListWithSound.size()) {//说明音频已经下载完成了
                        checkNetwork(mListWithSound.get(msg.arg1).getId(), mListWithSound.get(msg.arg1).getSounds());
                    } else {//下载音频
                        LogUtils.e(TAG, "下载音频的第几项?" + (msg.arg1));
                        checkNetwork(mListWithImage.get(msg.arg1).getId(), mListWithImage.get(msg.arg1).getImage());
                    }
                    break;
                case GET_NET_RESULT://从服务器获取上一次的测试结果
                    String sign1 = MD5.getMD5ofStr(getUid() + Constant.mListen + 3 + mTestCategory + Constant.APPID + getCurTime());
                    Call<ExamScore> call1 = new AbilityTestRequestFactory().getExamScoreApi().exampleScore(Constant.APPID, getUid(), Constant.mListen, mTestCategory, 3, sign1, "json");
                    call1.enqueue(new retrofit2.Callback<ExamScore>() {
                        @Override
                        public void onResponse(Call<ExamScore> call, Response<ExamScore> response) {
                            if (response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
                                int score = 0;
                                ArrayList<ExamScore.DataBean> results = response.body().getData();//每个纬度的分数
                                AbilityResult abilityResult = new AbilityResult();//结果用于存储到数据库
                                abilityResult.beginTime = results.get(0).getTestTime();
                                abilityResult.endTime = results.get(0).getTestTime();

                                mScoreArr = new int[results.size()];//分数
                                abilityType = new String[results.size()];
                                for (int i = 0; i < results.size(); i++) {
                                    abilityType[i] = results.get(i).getCategory();
                                    mScoreArr[i] = results.get(i).getScore();
                                    score += mScoreArr[i];

                                    abilityResult.Total = 100;
                                    abilityResult.uid = getUid();
                                    abilityResult.isUpload = 1;
                                    switch (i) {
                                        case 0:
                                            abilityResult.Score1 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 1:
                                            abilityResult.Score2 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 2:
                                            abilityResult.Score3 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 3:
                                            abilityResult.Score4 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 4:
                                            abilityResult.Score5 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 5:
                                            abilityResult.Score6 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 6:
                                            abilityResult.Score7 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 7:
                                            abilityResult.Score8 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 8:
                                            abilityResult.Score9 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;
                                        case 9:
                                            abilityResult.Score10 = 100 + "++" + results.get(i).getScore() + "++" + results.get(i).getCategory();
                                            break;

                                    }
                                    LogUtils.e(TAG, results.get(i).getCategory() + "  fenshu:  " + mScoreArr[i]);
                                }

                                score = mScoreArr.length == 0 ? 0 : score / mScoreArr.length;

                                abilityResult.DoRight = score;
                                abilityResult.TypeId = typeId;
                                boolean bb = testRecordHelper.seveTestRecord(abilityResult);
                                LogUtils.e(TAG, "结果存储：  " + bb);

                                lastTestTime = results.get(0).getTestTime();
                                //绘制图谱:
                                drawView.setData(abilityType, mScoreArr);
                                drawMap(drawView);
                                if (flag_mode == FLAG_TEST) {
                                    //文字部分的:
                                    if (mTestCategory.equals(Constant.ABILITY_WORD))
                                        testRecordInfo = "上次测评时间:" + lastTestTime.substring(0, 19) + "\n考试总词汇:" + Constant.TOTALWORDS + "个\n" + "您目前掌握的词汇:[[" + score * +Constant.TOTALWORDS / 100 + "]]个";
                                    else
                                        testRecordInfo = "上次测评时间:" + lastTestTime.substring(0, 19) + "\n" + mTitle + "测评总分:100分\n" + "您的测评成绩为:[[" + score + "]]分";
                                    CommonUtils.showTextWithUnderLine(mTv_lasttime_resut, testRecordInfo);
                                    setNextTime(lastTestTime);
                                }

                            } else {
                                LogUtils.e(TAG, "正常但是没有数据");
                            }
                        }

                        @Override
                        public void onFailure(Call<ExamScore> call, Throwable t) {
                            LogUtils.e(TAG, "onFailure");
                        }
                    });
                    break;
                case GET_PRACTICE_RESULT_NET://从服务器获取练习目录;  用户之前的答题记录
                    mWaittingDialog.show();
                    String sign2 = MD5.getMD5ofStr(getUid() + Constant.mListen + 2 + mTestCategory + Constant.APPID + getCurTime());
                    Call<ExamDetail> call2 = new AbilityTestRequestFactory().getExamDetailApi().exampleDetail(Constant.APPID, getUid(), Constant.mListen,
                            mTestCategory, 2, sign2, "json");
                    call2.enqueue(new retrofit2.Callback<ExamDetail>() {
                        @Override
                        public void onResponse(Call<ExamDetail> call, Response<ExamDetail> res) {
                            if (res.body() != null && res.body().getResult() == 1) {
                                List<ExamDetail.DataRightBean> mExamDataRightList = res.body().getDataRight();//练习题目 错误
                                List<ExamDetail.DataWrongBean> mExamDataWrongList = res.body().getDataWrong();//练习题目 正确
                                ArrayList<ExamDetail.DataBean> mExamDataList = new ArrayList<>();//练习题目

                                mExamDataList.addAll(mExamDataRightList);
                                mExamDataList.addAll(mExamDataWrongList);
                                now_doing = "正在更新学习记录...";
                                handler.sendEmptyMessage(CHANGE_WAITBAR_TEXT);
                                boolean b = testRecordHelper.updatePracticeDB(getUid(), mExamDataList, mTestCategory);//更新练习题目的答题记录
                                if (b) {
                                    update_pritace = false;//已经更新过,就不要更新了
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            initPracticeData();
                                            initPractiveViews();
                                            drawMap();
                                            if (mWaittingDialog.isShowing()) {
                                                mWaittingDialog.dismiss();
                                            }

                                        }
                                    });
                                }
                            } else {// 0 没有记录
                                update_pritace = false;//已经更新过,就不要更新了
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initPracticeData();
                                        initPractiveViews();
                                        drawMap();
                                        if (mWaittingDialog.isShowing())
                                            mWaittingDialog.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ExamDetail> call, Throwable t) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_waitbar.setText(now_doing);
                                }
                            });
                        }
                    });
                case CHANGE_WAITBAR_TEXT:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_waitbar.setText(now_doing);
                        }
                    });
                    break;
            }
        }

    };

    /**
     * 初始化需要下载的内容
     */
    private void initDownloads() {
        mListWithSound = new ArrayList<>();
        mListWithImage = new ArrayList<>();
        mListWithAttach = new ArrayList<>();
        for (int i = 0; i < mQuesLists.size(); i++) {
            if (mQuesLists.get(i).getImage() != null && !mQuesLists.get(i).getImage().trim().equals("")) {
                mListWithImage.add(mQuesLists.get(i));
                LogUtils.e(TAG, mQuesLists.get(i).getImage());
            }
            if (mQuesLists.get(i).getSounds() != null && !mQuesLists.get(i).getSounds().trim().equals("")) {
                mListWithSound.add(mQuesLists.get(i));
            }

            if (mQuesLists.get(i).getAttach() != null && !mQuesLists.get(i).getAttach().trim().equals("")) {
                mListWithAttach.add(mQuesLists.get(i));
            }
        }

        //判断有没有需要下载的文件
        if ((mListWithSound.size() + mListWithImage.size() + mListWithAttach.size()) > 0) {
            //显示进度条
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mWaittingDialog.isShowing())
                        mWaittingDialog.dismiss();
                    ll_progress_indictor.setVisibility(View.VISIBLE);
                }
            });
            mTotalFileCount2Down = mListWithSound.size() + mListWithImage.size() + mListWithAttach.size();
            mPercent = 100.0f / mTotalFileCount2Down;
        }

        LogUtils.e(TAG, "音频个数   " + mListWithSound.size() + "   图片个数  " + mListWithImage.size() + "txt个数:  " + mListWithAttach.size());
        if (mListWithSound.size() + mListWithImage.size() + mListWithAttach.size() == 0) {//没有文件加载
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(SHOW_DIALOG);
                    if (flag_mode == FLAG_TEST) mBtn_goto_test.setText("开始测评");
                }
            });
        }

        if (mListWithSound.size() != 0)//有音频
            checkNetwork(mListWithSound.get(0).getId(), mListWithSound.get(0).getSounds());

        if (mListWithImage.size() != 0)//有图片
            checkNetwork(mListWithImage.get(mListWithImage.size() - 1).getId(), mListWithImage.get(mListWithImage.size() - 1).getImage());

        if (mListWithAttach.size() != 0)//有文本 txt文档
            checkNetwork(mListWithAttach.get(mListWithAttach.size() - 1).getId(), mListWithAttach.get(mListWithAttach.size() - 1).getAttach());
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    private String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }

    /**
     * 音频下载
     */
    private void checkNetwork(final int audioId, final String filename) {
        // 判断有没有网络
        int apnType = NetWorkState.getAPNType();
        switch (apnType) {
            case 0:// 没有网
                ToastUtil.showToast(mContext, "网络连接失败,请检查网络是否可用");
                break;
            case 1:// 移动网络
                if (ConfigManager.Instance().loadBoolean("use4g")) {
                    downloadAudio(audioId, filename);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            dialog.setTitle("提示");
                            dialog.setMessage("您目前的网络状态,下载将耗费手机流量,是否继续下载？");
                            dialog.setPositiveButton("确认",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            downloadAudio(audioId, filename);
                                            ConfigManager.Instance().putBoolean("use4g", true);
                                        }
                                    });
                            dialog.setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ll_progress_indictor.setVisibility(View.INVISIBLE);
                                            dialog.dismiss();
                                        }
                                    });
                            dialog.show();
                        }
                    });
                }
                break;
            case 2:// wifi
                downloadAudio(audioId, filename);
                break;
            case 3:
                break;
            default:
                break;
        }

    }

    private int mCompleteNum;
    private float mPercent;

    protected void downloadAudio(int audioId, final String filename) {
        // 下载地址、存储路径
        String downloadUrl = "";
        String fileSaveDir;
        int threadNum = 4;
        fileSaveDir = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR + filename;

        if (filename.trim().toUpperCase().endsWith(".MP3")) {//音频
            downloadUrl = Constant.ABILITY_AUDIO_URL_PRE + filename;
        } else if (filename.trim().toUpperCase().endsWith(".PNG")) {//图片
            downloadUrl = Constant.ABILITY_IMAGE_URL_PRE + filename;
        } else if (filename.trim().toUpperCase().endsWith(".TXT")) {//txt文档
            downloadUrl = Constant.ABILITY_ATTACH_URL_PRE + filename;
        }
        LogUtils.e(TAG, "downloadUrl:" + downloadUrl);
        File file = new File(fileSaveDir);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {//音频存在不用下载
            //发消息,接着下载
            mCompleteNum++;
            LogUtils.e(TAG, "下载完成---" + mCompleteNum);
            if (mCompleteNum < mListWithSound.size()) {//音频
                downloadAudio(mListWithSound.get(mCompleteNum).getId(), mListWithSound.get(mCompleteNum).getSounds());

            } else if (mCompleteNum >= mListWithSound.size() && mCompleteNum < mListWithSound.size() + mListWithImage.size()) {//图片
                downloadAudio(mListWithImage.get(mListWithSound.size() + mListWithImage.size() - mCompleteNum - 1).getId(), mListWithImage.get(mListWithSound.size() + mListWithImage.size() - mCompleteNum - 1).getImage());

            } else if (mCompleteNum < mTotalFileCount2Down) {//txt
                downloadAudio(mListWithAttach.get(mTotalFileCount2Down - mCompleteNum - 1).getId(), mListWithAttach.get(mTotalFileCount2Down - mCompleteNum - 1).getAttach());
            } else {
                LogUtils.e(TAG, "全部下载完成了");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (flag_mode == FLAG_TEST) {
                            mBtn_goto_test.setText("开始测评");
                            if (!alertDialog.isShowing())
                                handler.sendEmptyMessage(SHOW_DIALOG);
                        } else if (canShowdialog) {
                            ll_progress_indictor.setVisibility(View.INVISIBLE);
                            initPracticeData();
                            initPractiveViews();
                            drawMap();
                            handler.sendEmptyMessage(SHOW_DIALOG);
                        }
                    }
                });
            }
            //下载未完成 接着下载
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPb_download_test.setProgress((int) (mCompleteNum * mPercent));
                    if ((int) (mCompleteNum * mPercent) < 100)
                        tv_download.setText((int) (mCompleteNum * mPercent) + "%");
                    else if (flag_mode == FLAG_TEST) mBtn_goto_test.setText("开始测评");
                }
            });
        } else {
            LogUtils.e(TAG, "url hashcode: " + downloadUrl.hashCode());
            MultiThreadDownloadManager.enQueue(mContext, downloadUrl.hashCode(), downloadUrl, new File(fileSaveDir), threadNum,
                    new DownloadProgressListener() {

                        @Override
                        public void onProgressUpdate(int id, String downloadurl, int fileDownloadSize) {
                        }

                        @Override
                        public void onDownloadStoped(int id) {
                            LogUtils.e(TAG, "下载停止了");
                        }

                        @Override
                        public void onDownloadStart(FileDownloader fileDownloader, int id, int fileTotalSize) {

                        }

                        @Override
                        public void onDownloadError(String errorMessage) {
                        }

                        @Override
                        public void onDownloadComplete(int id, String savePathFullName) {
                            mCompleteNum++;
                            LogUtils.e(TAG, "下载完成---" + mCompleteNum);
                            //发消息,接着下载
                            if (mCompleteNum < mListWithSound.size()) {//音频
                                downloadAudio(mListWithSound.get(mCompleteNum).getId(), mListWithSound.get(mCompleteNum).getSounds());

                            } else if (mCompleteNum >= mListWithSound.size() && mCompleteNum < mListWithSound.size() + mListWithImage.size()) {//图片
                                downloadAudio(mListWithImage.get(mListWithSound.size() + mListWithImage.size() - mCompleteNum - 1).getId(), mListWithImage.get(mListWithSound.size() + mListWithImage.size() - mCompleteNum - 1).getImage());

                            } else if (mCompleteNum < mTotalFileCount2Down) {//txt
                                downloadAudio(mListWithAttach.get(mTotalFileCount2Down - mCompleteNum - 1).getId(), mListWithAttach.get(mTotalFileCount2Down - mCompleteNum - 1).getAttach());
                            } else {
                                LogUtils.e(TAG, "全部下载完成了");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (flag_mode == FLAG_TEST) {
                                            mBtn_goto_test.setText("开始测试");
                                            if (!alertDialog.isShowing())
                                                handler.sendEmptyMessage(SHOW_DIALOG);
                                        } else if (canShowdialog) {
                                            // canShowdialog = false;
                                            initPracticeData();
                                            initPractiveViews();
                                            drawMap();
                                            ll_progress_indictor.setVisibility(View.INVISIBLE);
                                            handler.sendEmptyMessage(SHOW_DIALOG);
                                        }
                                    }
                                });
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPb_download_test.setProgress((int) (mCompleteNum * mPercent));
                                    if ((int) (mCompleteNum * mPercent) < 100)
                                        tv_download.setText((int) (mCompleteNum * mPercent) + "%");
                                    else if (flag_mode == FLAG_TEST) mBtn_goto_test.setText("开始测试");
                                }
                            });
                        }
                    }
            );
        }
    }

    private CustomDialog alertDialog;

    private void showToTestDialog() {//当前activity不可见的时候,调用show方法闪退
        ll_progress_indictor.setVisibility(View.INVISIBLE);

        /*自定义的dialog*/
        LayoutInflater inflater2 = LayoutInflater.from(mContext);
        View merge_view = inflater2.inflate(R.layout.dialog_custom, null);//要填充的layout

        alertDialog.setContentView(merge_view);
        if (canShowdialog) {
            alertDialog.show();
            canShowdialog = false;
        }

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                canShowdialog = true;
            }
        });

        TextView deleteTxt = merge_view.findViewById(R.id.tv_dialog_title);//标题
        if (deleteTxt != null)
            deleteTxt.setText(getResources().getString(R.string.warn_wormly));//warn_wormly"温馨提示"
        TextView content = merge_view.findViewById(R.id.tv_dialog_content);//提示的内容
        String text;
        if (flag_mode == FLAG_TEST) {
            text = mTitle + "测评即将开始,为确保测评结果接近您的真实水平,请在规定的时间内完成.";
        } else {
            text = mTitle + "练习题目加载完成,马上开始?";
        }

        content.setText(text);//should NullPointException here ?

        Button rechCancleBtn = merge_view.findViewById(R.id.btn_dialog_no);
        Button rechOkBtn = merge_view.findViewById(R.id.btn_dialog_yes);
        rechOkBtn.setText("开始");
        rechCancleBtn.setText("取消");
        rechCancleBtn.setOnClickListener(new View.OnClickListener() {//取消按钮
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        rechOkBtn.setOnClickListener(new View.OnClickListener() {//开始按钮
            @Override
            public void onClick(View v) {
                if (mWaittingDialog.isShowing()) {
                    mWaittingDialog.dismiss();
                }
                if (flag_test_analy == FLAG_SHOW_ANA) {
                    handler.sendEmptyMessage(TO_ANALYSIS);//解析界面
                } else {
                    handler.sendEmptyMessage(BEGINTEST); //测评或者练习界面
                }
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 是否可以显示dialog
     */
    private boolean canShowdialog = true;

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(SHOW_DIALOG);
        handler.removeMessages(GET_PRACTICE_RESULT_NET);
        canShowdialog = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MultiThreadDownloadManager.removeAllTask();//删除所有下载
        canShowdialog = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag_test_analy != FLAG_SHOW_ANA) {
            drawMap();
        }
        canShowdialog = true;
    }

    /**
     * 底部展示下一次测试的时间
     */
    private void setNextTime(String lastTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy年MM月dd日");
        //计算一个星期之后的时间
        if (!lastTime.equals("未知")) {//已经有过测试记录
            try {
                Date date = formatter.parse(lastTime.substring(0, 10));//获取当前的测试时间,毫秒级
                long nextTime = date.getTime() + 7 * 24 * 60 * 60 * 1000;//一个星期之后的测试时间  秒
                tv_nextTextTime.setText("*提示:您可以在" + formatter2.format(nextTime) + "进行下次测评");
                tv_nextTextTime.invalidate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        canShowdialog = false;
    }

    public void setCurrentTypeId(String testCategory) {
        switch (testCategory) {
            case Constant.ABILITY_WRITE:
                typeId = Constant.ABILITY_TETYPE_WRITE;
                break;
            case Constant.ABILITY_WORD:
                typeId = Constant.ABILITY_TETYPE_WORD;
                break;
            case Constant.ABILITY_GRAMMAR:
                typeId = Constant.ABILITY_TETYPE_GRAMMAR;
                break;
            case Constant.ABILITY_LISTEN:
                typeId = Constant.ABILITY_TETYPE_LISTEN;
                break;
            case Constant.ABILITY_SPEAK:
                typeId = Constant.ABILITY_TETYPE_SPEAK;
                break;
            case Constant.ABILITY_READ:
                typeId = Constant.ABILITY_TETYPE_READ;
                break;
        }
    }
}
