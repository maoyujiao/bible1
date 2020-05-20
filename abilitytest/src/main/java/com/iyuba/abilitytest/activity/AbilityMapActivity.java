package com.iyuba.abilitytest.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityLessonInfoEntity;
import com.iyuba.abilitytest.entity.AbilityResult;
import com.iyuba.abilitytest.entity.ExamScore;
import com.iyuba.abilitytest.manager.DataManager;
import com.iyuba.abilitytest.network.AbilityTestRequestFactory;
import com.iyuba.abilitytest.sqlite.AbilityDBHelper;
import com.iyuba.abilitytest.sqlite.AbilityDBManager;
import com.iyuba.abilitytest.sqlite.TestRecordHelper;
import com.iyuba.abilitytest.utils.AbilityConstants;
import com.iyuba.abilitytest.widget.DrawView;
import com.iyuba.configation.Constant;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.ToastUtil;
import com.iyuba.core.widget.dialog.CustomToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的雅思英语能力图谱
 * 雅思能力测试 总界面
 * 包含 单词 语法 听力 口语 阅读 写作 等功能的入口
 *
 * @author liuzhenli 2016/9/? 10:51
 */

public class AbilityMapActivity extends AppBaseActivity {
    private final int FLAG_TEST = 1;//模式标记 1代表测评  2 代表练习
    private final int FLAG_PRACTICE = 2;//模式标记 1代表测评  2 代表练习

    private final int GET_NET_RESULT = 10000;//从服务器获取已经上传的数据
    private final int GET_LOCAL_RESULT = 10001;//从数据库获取成绩数据

    private ArrayList<AbilityResult> mWriteTestResults;
    private ArrayList<AbilityResult> mWordTestResults;
    private ArrayList<AbilityResult> mGrammarTestResults;
    private ArrayList<AbilityResult> mListenTestResults;
    private ArrayList<AbilityResult> mSpeakTestResults;
    private ArrayList<AbilityResult> mReadTestResults;

    private Context mContext;

    private String[] mAbilityTypeArr = Constant.ABILITY_TYPE_ARR;
    private int[] mResult = new int[mAbilityTypeArr.length];//测试结果,六个方面 依次为写作 单词 语法 听力 口语 阅读
    private int flag_mode;//1 测评  2练习

    private TextView tv_mode_test, tv_mode_practice;
    private TestRecordHelper helper;// 单词能力测试
    private TextView mBtn_words;// 语法
    private TextView mBtn_grammar; //听力能力测试
    private TextView mBtn_listen;// 口语
    private TextView mBtn_speak;//阅读
    private TextView mBtn_read;// 写作
    private TextView mBtn_write;
    private ImageButton mIbtn_guide;
    private ImageButton mIbtn_guide2;
    private int mLessonId;//高职英语中使用  作为unitid使用
    private DrawView drawView;


    @Override
    protected int getLayoutResId() {
        return R.layout.act_abilitymap;
    }

    /**
     * @param mode     1评测  2练习
     * @param lessonId 单元/年份
     */
    public static void actionStart(Context context, int mode, int lessonId) {
        Intent intent = new Intent(context, AbilityMapActivity.class);
        intent.putExtra("flag_mode", mode);
        intent.putExtra("lessonId", lessonId);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {

        mContext = this;
        Matrix matrix = new Matrix();
        matrix.setTranslate(0,0);
        Intent intent = getIntent();
        if (intent.getIntExtra("flag_mode", 0) != 0) {//可以接收外部传来的模式
            flag_mode = intent.getIntExtra("flag_mode", 1);
        } else {
            flag_mode = FLAG_TEST;//四级听力的默认项 测评
        }
        AbilityDBManager manager = new AbilityDBManager(mContext, AbilityConstants.DB_VERSION);
        manager.openDatabase();

        String appId = "";
        String type = Constant.APP_CONSTANT.TYPE();
        if (type.equals("4")) {
            appId = "207";
        } else if (type.equals("6")) {
            appId = "208";
        } else if (type.equals("1")) {
            appId = "205";
        } else if (type.equals("2")) {
            appId = "206";
        } else if (type.equals("3")) {
            appId = "203";
        }
        ArrayList<AbilityLessonInfoEntity> list = AbilityDBHelper.getInstance().getLessonInfosByBookId(appId);
        if (list == null || list.size() == 0) {
            CustomToast.showToast(mContext, "没有数据");
            finish();
            return;
        }
        Constant.mListen = list.get(0).bookName;
        mLessonId = getIntent().getIntExtra("lessonId", 0);
        handler.sendEmptyMessage(GET_LOCAL_RESULT);//用户是否登录,都先从本地数据库获取数据进行展示
        helper = TestRecordHelper.getInstance(mContext);
        //如果登录了,获取服务器保存的答题记录
        if (isUserLogin()) {
            //检测数据库中 答题记录是否有未上传的数据  考虑到测评和练习需要在有网的情况下进行的,失败的概率较小,原则上访问服务器的次数较少
            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadTestRecordToNet(getUid(), 0, "X", FLAG_TEST);//写作测试
                    uploadTestRecordToNet(getUid(), 1, "W", FLAG_TEST);//单词测试
                    uploadTestRecordToNet(getUid(), 2, "G", FLAG_TEST);//语法测试
                    uploadTestRecordToNet(getUid(), 3, "L", FLAG_TEST);//听力测试
                    uploadTestRecordToNet(getUid(), 4, "S", FLAG_TEST);//口语测试
                    uploadTestRecordToNet(getUid(), 5, "R", FLAG_TEST);//阅读测试

                    uploadTestRecordToNet(getUid(), 0, "X", FLAG_PRACTICE);//练习
                    uploadTestRecordToNet(getUid(), 1, "W", FLAG_PRACTICE);//练习
                    uploadTestRecordToNet(getUid(), 2, "G", FLAG_PRACTICE);//练习
                    uploadTestRecordToNet(getUid(), 3, "L", FLAG_PRACTICE);//练习
                    uploadTestRecordToNet(getUid(), 4, "S", FLAG_PRACTICE);//练习
                    uploadTestRecordToNet(getUid(), 5, "R", FLAG_PRACTICE);//练习
                }
            }).start();

            if (flag_mode == FLAG_TEST)//练习状态下不访问网络数据
                handler.sendEmptyMessage(GET_NET_RESULT);
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initTitlebar();
        drawView = new DrawView(this);
        drawView.setOnPositinoClickListener(new DrawView.PositionClickListener() {
            @Override
            public void itemSelected(int itmeId) {
                switch (itmeId) {
                    case Constant.ABILITY_TETYPE_WRITE:
                        if (!Constant.APP_CONSTANT.isEnglish()) {
                            CustomToast.showToast(mContext, "暂未开放");
                            return;
                        }
                        gotoTargetClass(AbilityMapSubActivity.class, mWriteTestResults, Constant.ABILITY_WRITE);
                        break;
                    case Constant.ABILITY_TETYPE_WORD:
                        gotoTargetClass(AbilityMapSubActivity.class, mWordTestResults, Constant.ABILITY_WORD);
                        break;
                    case Constant.ABILITY_TETYPE_GRAMMAR:
                        gotoTargetClass(AbilityMapSubActivity.class, mGrammarTestResults, Constant.ABILITY_GRAMMAR);
                        break;
                    case Constant.ABILITY_TETYPE_LISTEN:
                        gotoTargetClass(AbilityMapSubActivity.class, mListenTestResults, Constant.ABILITY_LISTEN);
                        break;
                    case Constant.ABILITY_TETYPE_SPEAK:
                        if (!Constant.APP_CONSTANT.isEnglish()) {
                            CustomToast.showToast(mContext, "暂未开放");
                            return;
                        }
                        gotoTargetClass(AbilityMapSubActivity.class, mSpeakTestResults, Constant.ABILITY_SPEAK);
                        break;
                    case Constant.ABILITY_TETYPE_READ:
                        gotoTargetClass(AbilityMapSubActivity.class, mReadTestResults, Constant.ABILITY_READ);
                        break;
                }
            }
        });
        mBtn_words = findView(R.id.btn_ability_words);
        mBtn_grammar = findView(R.id.btn_ability_grammar);
        mBtn_listen = findView(R.id.btn_ability_listen);
        mBtn_speak = findView(R.id.btn_ability_spoken);
        mBtn_read = findView(R.id.btn_ability_read);
        mBtn_write = findView(R.id.btn_ability_write);
        tv_mode_test = findView(R.id.tv_titlebar_ceping);//测试模式按钮
        tv_mode_practice = findView(R.id.tv_titlebar_test);//练习模式按钮
        mIbtn_guide = findView(R.id.ibtn_test_guide);
        mIbtn_guide2 = findView(R.id.ibtn_test_guide2);
    }

    private void initTitlebar() {
        LinearLayout ll_titlebar_test = findView(R.id.ll_titlebar_test);
        LinearLayout ll_titlebar_test_prac = findView(R.id.ll_titlebar_test_prac);
        TextView tv_titlebar_sub = findView(R.id.tv_titlebar_sub);
        if (Constant.mListen.equals("IELTS") || Constant.mListen.equals("N1")
                || Constant.mListen.equals("N2") || Constant.mListen.equals("N3")) {
            ll_titlebar_test.setVisibility(View.VISIBLE);
            ll_titlebar_test_prac.setVisibility(View.GONE);
        } else if (Constant.mListen.equals("Toefl")
                || Constant.mListen.equals("cet4")
                || Constant.mListen.equals("cet6")
                || Constant.mListen.equals("GZ1")
                || Constant.mListen.equals("GZ2")
                ) {
            ll_titlebar_test.setVisibility(View.VISIBLE);
            ll_titlebar_test_prac.setVisibility(View.GONE);
            if (flag_mode == FLAG_TEST) {
                tv_titlebar_sub.setText("我的能力图谱");
            } else {
                tv_titlebar_sub.setText("我的练习图谱");
            }
        }
        ImageButton selector_btn_bg = findView(R.id.btn_nav_sub);
        selector_btn_bg.setColorFilter(getResources().getColor(R.color.white));
        if (selector_btn_bg != null) {
            selector_btn_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ImageButton btn_nav_back = findView(R.id.btn_nav_back);
        if (btn_nav_back != null) {
            btn_nav_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void loadData() {
        mBtn_words.setOnClickListener(wordAblityButtonClickListener());
        mBtn_grammar.setOnClickListener(grammarAbilityButtonClickListener());
        mBtn_listen.setOnClickListener(listenAblityButtonClickListener());
        mBtn_speak.setOnClickListener(speakAblityButtonClickListener());
        mBtn_read.setOnClickListener(readAbilityButtonClickListener());
        mBtn_write.setOnClickListener(writeAbilityButtonClickListener());
        if (!Constant.APP_CONSTANT.isEnglish()) {
            mBtn_speak.setBackgroundResource(R.mipmap.ability_speak_disable);
            mBtn_speak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomToast.showToast(mContext, "暂未开放");
                }
            });
            mBtn_write.setBackgroundResource(R.mipmap.ability_write_disable);
            mBtn_write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomToast.showToast(mContext, "暂未开放");
                }
            });

            if ("3".equals(Constant.APP_CONSTANT.TYPE()) && mLessonId == 201507) {
                mBtn_listen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomToast.showToast(mContext, "此套听力暂未收录");
                    }
                });

            }
        } else {
            if (flag_mode == 1) {
                mBtn_speak.setOnClickListener(speakAblityButtonClickListener());
                mBtn_write.setOnClickListener(writeAbilityButtonClickListener());
            } else {
//                mBtn_speak.setBackgroundResource(R.mipmap.ability_speak_disable);
                mBtn_speak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(mContext, "该模块题目正在紧张制作中");
//                gotoTargetClass(AbilityMapSubActivity.class, mSpeakTestResults, Constant.ABILITY_SPEAK);
                    }
                });
//                mBtn_write.setBackgroundResource(R.mipmap.ability_write_disable);
                mBtn_write.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(mContext, "该模块题目正在紧张制作中");
//                gotoTargetClass(AbilityMapSubActivity.class, mWriteTestResults, Constant.ABILITY_WRITE);
                    }
                });
            }
        }


        tv_mode_test.setOnClickListener(changeModeClickListener(FLAG_TEST));
        tv_mode_practice.setOnClickListener(changeModeClickListener(FLAG_PRACTICE));
        mIbtn_guide.setOnClickListener(guideButtonClickListener());
        mIbtn_guide.setVisibility(View.VISIBLE);
        mIbtn_guide2.setOnClickListener(guideButtonClickListener());
    }

    @NonNull
    private View.OnClickListener guideButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbilityMapActivity.this, WebActivity.class);
                intent.putExtra("url", " http://app.iyuba.cn/wap/evaluation.jsp");
                intent.putExtra("title", "使用指南");
                startActivity(intent);
            }
        };
    }

    @NonNull
    private View.OnClickListener changeModeClickListener(final int mode) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == FLAG_TEST) {//测试模式
                    flag_mode = FLAG_TEST;
                    tv_mode_practice.setBackground(getResources().getDrawable(R.drawable.shape_btnbg_right_nor));
                    tv_mode_practice.setTextColor(getResources().getColor(R.color.text_bluenor_color));
                    tv_mode_test.setBackground(getResources().getDrawable(R.drawable.shape_btnbg_left_sel));
                    tv_mode_test.setTextColor(getResources().getColor(R.color.bg_ability));
                    handler.sendEmptyMessage(GET_LOCAL_RESULT);//用户是否登录,都先从本地数据库获取数据进行展示
                    //如果登录了,获取服务器保存的答题记录
                    if (isUserLogin()) {
                        handler.sendEmptyMessage(GET_NET_RESULT);
                    }
                } else if (mode == FLAG_PRACTICE) {//练习模式   在左侧
                    flag_mode = FLAG_PRACTICE;
                    tv_mode_practice.setBackground(getResources().getDrawable(R.drawable.shape_btnbg_right_sel));
                    tv_mode_test.setTextColor(getResources().getColor(R.color.text_bluenor_color));

                    tv_mode_test.setBackground(getResources().getDrawable(R.drawable.shape_btnbg_left_nor));
                    tv_mode_practice.setTextColor(getResources().getColor(R.color.bg_ability));

                    handler.sendEmptyMessage(GET_LOCAL_RESULT);//用户是否登录,都先从本地数据库获取数据进行展示
                }
            }
        };
    }


    /**
     * 写作能力测试点击事件
     */
    private View.OnClickListener writeAbilityButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constant.APP_CONSTANT.isEnglish()) {
                    CustomToast.showToast(mContext, "暂未开放");
                    return;
                }
                gotoTargetClass(AbilityMapSubActivity.class, mWriteTestResults, Constant.ABILITY_WRITE);
            }
        };
    }

    //单词能力测试
    private View.OnClickListener wordAblityButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTargetClass(AbilityMapSubActivity.class, mWordTestResults, Constant.ABILITY_WORD);
            }
        };
    }

    //语法能力测试点击事件
    @NonNull
    private View.OnClickListener grammarAbilityButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTargetClass(AbilityMapSubActivity.class, mGrammarTestResults, Constant.ABILITY_GRAMMAR);
            }
        };
    }

    //听力能力测试
    private View.OnClickListener listenAblityButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTargetClass(AbilityMapSubActivity.class, mListenTestResults, Constant.ABILITY_LISTEN);
            }
        };
    }

    /**
     * 口语能力测试
     */
    private View.OnClickListener speakAblityButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTargetClass(AbilityMapSubActivity.class, mSpeakTestResults, Constant.ABILITY_SPEAK);
            }
        };
    }

    /**
     * 阅读能力测试点击事件
     */
    private View.OnClickListener readAbilityButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTargetClass(AbilityMapSubActivity.class, mReadTestResults, Constant.ABILITY_READ);
            }
        };
    }

    /**
     * 向目标activity跳转
     *
     * @param targetClass 目标activity
     */
    private void gotoTargetClass(Class targetClass, ArrayList arr, String abilityType) {
        boolean b;
        if (Constant.mListen.equals("cet4") && isVip()) {//英语四级会员才能使用测评和练习
            b = true;
        } else {
            b = true;
        }
        if (b) {
            Intent intent = new Intent();
            intent.setClass(AbilityMapActivity.this, targetClass);
            intent.putExtra("resultLists", arr);
            intent.putExtra("abilityType", abilityType);
            intent.putExtra("mode", flag_mode);
            // 如果是四级和六级的语法和单词，不区分年份
            if ((Constant.APPID.equals("246") || Constant.APPID.equals("242"))
                    && (abilityType.equals(Constant.ABILITY_WORD) || abilityType.equals(Constant.ABILITY_GRAMMAR))) {
                intent.putExtra("lessonId", 0);
            } else {
                intent.putExtra("lessonId", mLessonId);
            }
            startActivity(intent);
        } else {
            ToastUtil.showToast(mContext, "您不是黄金会员,无法进行测试.");
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("提示");
            dialog.setMessage("您还不是爱语吧黄金会员,无法进行测评和练习.开通黄金VIP即可享受英语四级考试全程指导,四级考不过,全额退款.是否马上开通?");
            dialog.setPositiveButton("马上开通", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //跳转到开通界面
                }
            });
            dialog.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    /**
     * 六种能力单项分数
     *
     * @param testResults 数据库存储的数据
     * @return 分数
     */
    private int getAbilityTypeScore(ArrayList<AbilityResult> testResults) {
        int score = 0;
        if (testResults != null && testResults.size() != 0) {
            int doRight;
            int total;
            doRight = testResults.get(testResults.size() - 1).DoRight;
            total = testResults.get(testResults.size() - 1).Total;
            score = total == 0 ? 0 : doRight * 100 / total;
        }
        return score;
    }

    private void drawAbilityMap() {
        LinearLayout layout = findView(R.id.root);
        layout.removeAllViews();
        drawView.setData(mAbilityTypeArr, mResult);
        drawView.invalidate();
        layout.addView(drawView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag_mode == FLAG_TEST) {
            if (DataManager.getInstance().abilityResList != null && DataManager.getInstance().abilityResList.size() > 0) {//全局变量存有数据
                obtainTestResults(DataManager.getInstance().abilityResList);
            } else {
                handler.sendEmptyMessage(GET_LOCAL_RESULT);//用户是否登录,都先从本地数据库获取数据进行展示
            }
            if (isUserLogin()) {
                handler.sendEmptyMessage(GET_NET_RESULT);
            }
        } else if (flag_mode == FLAG_PRACTICE) {
            handler.sendEmptyMessage(GET_LOCAL_RESULT);//用户是否登录,都先从本地数据库获取数据进行展示
        }
    }

    private int getLoacalPercent(int lessonId, String category) {
        int right = helper.getPracticeCountByType(getUid(), 1, lessonId, category);
        int total = helper.getPracticeCountByType(getUid(), 3, lessonId, category);
        return total == 0 ? 0 : right * 100 / total;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_NET_RESULT:
                    String sign = MD5.getMD5ofStr(getUid() + Constant.mListen + "" + 1 + Constant.APPID + getCurTime());
                    //从服务器获取上一次的测试结果
                    Call<ExamScore> call = new AbilityTestRequestFactory().getExamScoreApi().exampleScore(Constant.APPID, getUid(), Constant.mListen, "", 1, sign, "json");
                    call.enqueue(new retrofit2.Callback<ExamScore>() {
                        @Override
                        public void onResponse(Call<ExamScore> call, Response<ExamScore> res) {
                            if (res.body() != null && res.body().getResult() == 1) {//获取网络数据成功了.重新绘制图谱
                                LogUtils.e("走了这里");
                                ArrayList<ExamScore.DataBean> results = res.body().getData();
                                DataManager.getInstance().abilityResList = results;//全局变量
                                obtainTestResults(results);//赋值并绘制图谱
                            } else {//获取网络数据失败,从本地获取
                                handler.sendEmptyMessage(GET_LOCAL_RESULT);
                            }
                        }

                        @Override
                        public void onFailure(Call<ExamScore> call, Throwable t) {

                        }
                    });

                    break;
                case GET_LOCAL_RESULT:
                    if (flag_mode == FLAG_TEST) {
                        String uid = getUid();//默认值是""
                        //获取本地存储的写作评测结果   约40个题目
                        mWriteTestResults = helper.getAbilityTestRecord(Constant.ABILITY_TETYPE_WRITE, uid, false);
                        mResult[0] = getAbilityTypeScore(mWriteTestResults);

                        //获取本地存储的单词评测结果 约100个题目
                        mWordTestResults = helper.getAbilityTestRecord(Constant.ABILITY_TETYPE_WORD, uid, false);
                        mResult[1] = getAbilityTypeScore(mWordTestResults);

                        //获取本地存储的语法评测结果 约60个题目
                        mGrammarTestResults = helper.getAbilityTestRecord(Constant.ABILITY_TETYPE_GRAMMAR, uid, false);
                        mResult[2] = getAbilityTypeScore(mGrammarTestResults);

                        //获取本地存储的听力评测结果 约40个题目
                        mListenTestResults = helper.getAbilityTestRecord(Constant.ABILITY_TETYPE_LISTEN, uid, false);
                        mResult[3] = getAbilityTypeScore(mListenTestResults);

                        //获取本地存储的口语评测结果 约40个题目
                        mSpeakTestResults = helper.getAbilityTestRecord(Constant.ABILITY_TETYPE_SPEAK, uid, false);
                        mResult[4] = getAbilityTypeScore(mSpeakTestResults);

                        //获取本地存储的阅读评测结果 约40个题目
                        mReadTestResults = helper.getAbilityTestRecord(Constant.ABILITY_TETYPE_READ, uid, false);
                        mResult[5] = getAbilityTypeScore(mReadTestResults);

                    } else if (flag_mode == FLAG_PRACTICE) {//从本地读 计算多维图谱的分值
                        mResult = new int[6];
                        mResult[Constant.ABILITY_TETYPE_WRITE] = getLoacalPercent(mLessonId, Constant.ABILITY_WRITE);
                        if ((Constant.APPID.equals("246") || Constant.APPID.equals("242"))) {
                            mResult[Constant.ABILITY_TETYPE_WORD] = getLoacalPercent(0, Constant.ABILITY_WORD);
                        } else {
                            mResult[Constant.ABILITY_TETYPE_WORD] = getLoacalPercent(mLessonId, Constant.ABILITY_WORD);
                        }
                        mResult[Constant.ABILITY_TETYPE_GRAMMAR] = getLoacalPercent(mLessonId, Constant.ABILITY_GRAMMAR);
                        mResult[Constant.ABILITY_TETYPE_LISTEN] = getLoacalPercent(mLessonId, Constant.ABILITY_LISTEN);
                        mResult[Constant.ABILITY_TETYPE_SPEAK] = getLoacalPercent(mLessonId, Constant.ABILITY_SPEAK);
                        mResult[Constant.ABILITY_TETYPE_READ] = getLoacalPercent(mLessonId, Constant.ABILITY_READ);
//                        ArrayList<TestCategory> practiceTestRecord_all;//练习结果 所有类别  根据听力L  阅读R  ...划分
//                        ArrayList<TestCategory> practiceTestRecord_right;
//                        if ((Constant.APPID.equals("207") || Constant.APPID.equals("208"))) {
//                            practiceTestRecord_all = helper.getPracticeTestRecord(getUid(), 3, 0);//练习结果 所有类别  根据听力L  阅读R  ...划分
//                            practiceTestRecord_right = helper.getPracticeTestRecord(getUid(), 1, 0);
//                        } else {
//                            practiceTestRecord_all = helper.getPracticeTestRecord(getUid(), 3, mLessonId);//练习结果 所有类别  根据听力L  阅读R  ...划分
//                            practiceTestRecord_right = helper.getPracticeTestRecord(getUid(), 1, mLessonId);
//                        }
//                        for (int j = 0; j < practiceTestRecord_all.size(); j++) {
//                            for (int i = 0; i < practiceTestRecord_right.size(); i++) {//此处改为根据right的类型去赋值  没有正确个数的 result 直接是零
//                                if (practiceTestRecord_all.get(j).type.equals(practiceTestRecord_right.get(i).type)) {
//                                    switch (practiceTestRecord_right.get(i).type) {
//                                        case Constant.ABILITY_WRITE:
//                                            mResult[Constant.ABILITY_TETYPE_WRITE] = practiceTestRecord_right.get(i).count * 100 / practiceTestRecord_all.get(j).count;
//                                            break;
//                                        case Constant.ABILITY_WORD:
//                                            mResult[Constant.ABILITY_TETYPE_WORD] = practiceTestRecord_right.get(i).count * 100 / practiceTestRecord_all.get(j).count;//java.lang.IndexOutOfBoundsException: Invalid index 1, size is 1
//                                            break;
//                                        case Constant.ABILITY_GRAMMAR:
//                                            mResult[Constant.ABILITY_TETYPE_GRAMMAR] = practiceTestRecord_right.get(i).count * 100 / practiceTestRecord_all.get(j).count;
//                                            break;
//                                        case Constant.ABILITY_LISTEN:
//                                            mResult[Constant.ABILITY_TETYPE_LISTEN] = practiceTestRecord_right.get(i).count * 100 / practiceTestRecord_all.get(j).count;
//                                            break;
//                                        case Constant.ABILITY_SPEAK:
//                                            mResult[Constant.ABILITY_TETYPE_SPEAK] = practiceTestRecord_right.get(i).count * 100 / practiceTestRecord_all.get(j).count;
//                                            break;
//                                        case Constant.ABILITY_READ:
//                                            mResult[Constant.ABILITY_TETYPE_READ] = practiceTestRecord_right.get(i).count * 100 / practiceTestRecord_all.get(j).count;
//                                            break;
//                                    }
//                                }
//                            }
//                        }
                    }
                    drawAbilityMap();//绘制图谱;
                    break;
            }
        }
    };

    private void obtainTestResults(ArrayList<ExamScore.DataBean> results) {
        for (int i = 0; i < results.size(); i++) {
            LogUtils.e(results.get(i).getTestMode() + "  fenshu:  " + results.get(i).getScore());
            switch (results.get(i).getTestMode()) {
                case Constant.ABILITY_WRITE:
                    mResult[0] = results.get(i).getScore();
                    break;
                case Constant.ABILITY_WORD:
                    mResult[1] = results.get(i).getScore();
                    break;
                case Constant.ABILITY_GRAMMAR:
                    mResult[2] = results.get(i).getScore();
                    break;
                case Constant.ABILITY_LISTEN:
                    mResult[3] = results.get(i).getScore();
                    break;
                case Constant.ABILITY_SPEAK:
                    mResult[4] = results.get(i).getScore();
                    break;
                case Constant.ABILITY_READ:
                    mResult[5] = results.get(i).getScore();
                    break;
            }
        }
        drawAbilityMap();
    }

    private String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }
}
