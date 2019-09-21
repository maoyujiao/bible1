package com.iyuba.abilitytest.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.adapter.KeyboardAdapter;
import com.iyuba.abilitytest.adapter.ReviewAdapter;
import com.iyuba.abilitytest.entity.AbilityQuestion;
import com.iyuba.abilitytest.entity.AbilityResult;
import com.iyuba.abilitytest.entity.SendEvaluateResponse;
import com.iyuba.abilitytest.entity.TestRecord;
import com.iyuba.abilitytest.listener.OnRecyclerViewItemClickListener;
import com.iyuba.abilitytest.manager.DataManager;
import com.iyuba.abilitytest.network.AbilityTestRequestFactory;
import com.iyuba.abilitytest.network.EvaluateApi;
import com.iyuba.abilitytest.sqlite.TestRecordHelper;
import com.iyuba.abilitytest.utils.CommonUtils;
import com.iyuba.abilitytest.utils.FileUtil;
import com.iyuba.abilitytest.utils.Player;
import com.iyuba.abilitytest.widget.SuperGridView;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.RecordManager;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.util.ToastUtil;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.core.widget.dialog.CustomToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 写作,语法,阅读能力测试界面
 * Created by LiuZhenLi on 2016/8/15.
 * last modify time 20170903 by 刘振立
 */
public class AbilityTestActivity extends AppBaseActivity {
    private static final String TAG = "AbilityTestActivity";
    private final int SAVERESULT = 1001;
    private final int FINISHSELF = 1002;
    private final int CHANGEBGIMAGE = 10000;//改变背景
    private String mTitleName;
    private boolean flag;
    private int wordIndex = 0, wordLength = 0;
    private TextView mTv_uestion, timeUsed, quesIndex, mTv_jugde_ques, mTv_blank_ques;
    private RoundTextView btn_next;
    private RoundTextView tv_pre_ques;
    private CheckBox mCheckBox_A, mCheckBox_B, mCheckBox_C, mCheckBox_D, mCheckBox_E;
    private TextView tv_ability_chosn_ques;//上方的问题
    private TextView question;
    private Context mContext = this;
    private LinearLayout mll_jugde_consl;//判断题目答案布局
    private LinearLayout mll_blank_consl;//天空题目答题布局
    private LinearLayout ll_ability_listen_mulchose;//多选题目
    private TextView tv_question_type;
    private TextView tv_jude_detail;
    private RadioButton rb_option_a_1, rb_option_b_2, rb_option_c_0, rb_option_d, rb_option_e, rb_option_f;
    private EditText mEt_userAnswer;
    private int totalTime;
    private ProgressBar mPb_timeLeft;
    private TextView mTv_catetgory;
    private String[] userAns;
    private int rightNum[];
    private GetDeviceInfo deviceInfo;
    private String mBegintime;
    private final String[] arr = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private List standardLetters = Arrays.asList(arr);
    private AbilityResult abilityResult;
    private RoundProgressBar mRoundProgressBar;
    private List<String> letters = new ArrayList<>();
    private List<String> chosn = new ArrayList<>();
    private SuperGridView virtualKey;
    private String mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR + DataManager.getInstance().lessonId;
    private final int PLAYER_STATE_PLAY = 1;
    private final int PLAYER_STATE_PAUSE = 2;
    private final int PLAYER_STATE_STOP = 0;
    private int mPlayState;
    private Timer mTimer;
    /**
     * 题目的类型   0--单项选择  1--判断正误  2--标题对应  3--配对题 4--简答题 5--完成句子
     */
    private String[] mTestTypeArr;//围度的名称  中英力 应用力 等
    /**
     * 试题的纬度,也就是展示在圆上的测试类型
     */
    final int TYPE_0 = 0;
    final int TYPE_1 = 1;
    final int TYPE_2 = 2;
    final int TYPE_3 = 3;
    final int TYPE_4 = 4;
    final int TYPE_5 = 5;
    final int TYPE_6 = 6;
    final int TYPE_7 = 7;
    final int TYPE_8 = 8;
    final int TYPE_9 = 9;

    private int mTotal;//试题的总数
    private int[] mCategory;//测试题的种类 对应种类的题目个数

    private TestRecord mTestRecord;
    private Map<Integer, TestRecord> testRecordMap;// 2017.8.12 liuzhenli 由于可以返回上一题,使用list会重复添加,因此改为Map
    private ArrayList<AbilityQuestion.TestListBean> mQuesList;//题目详情
    private TestRecordHelper helper;
    private File mRecordFile ;
    private RecordManager mRecorder;

    private int flag_mode;
    private final int FLAG_TEST = 1;//测评模式
    private final int FLAG_PRACTICE = 2;//练习模式
    private String mTestCategory;
    private RadioGroup rg_judge;
    private SeekBar sb_player;
    private Player mPlayer;
    private LinearLayout ll_play_controller;
    private ImageButton ibtn_player_controller;
    private RelativeLayout keyboardr;
    private LinearLayout chosnChar;
    private ImageView wordImage;
    private LinearLayout ll_time_line;
    private RecyclerView rv_review;
    private RoundTextView tv_review_title;
    private TextView tv_ability_right;
    private TextView tv_practice_explain;
    private ScrollView sv_answer_container;
    private int index = -1;
    private ScrollView scrollViewAnswer;
    private ScrollView scrollViewQuestion;
    private Timer timer;
    private boolean isRecording;


    @Override
    protected int getLayoutResId() {
        return R.layout.act_readabilitytest;
    }

    public static void actionStart(Context context, String category, int mode) {
        Intent intent = new Intent(context, AbilityTestActivity.class);
        intent.putExtra("testCategory", category);
        intent.putExtra("mode", mode);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.pause();
        mPlayState = PLAYER_STATE_PAUSE;
        ibtn_player_controller.setImageResource(R.mipmap.ic_audio_play);
    }

    @Override
    protected void initVariables() {
        abilityResult = new AbilityResult();
//        iseManager = IseManager.getInstance(mContext, handler);
        deviceInfo = new GetDeviceInfo(mContext);
        mBegintime = deviceInfo.getCurrentTime();
        flag_mode = getIntent().getIntExtra("mode", 1);
        if (flag_mode == 1) { // 评测的lessonId为 -1
            mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR + "-1/";
        }
        mTestCategory = getIntent().getStringExtra("testCategory");
        LogUtils.e(TAG, "mTestCategory: " + mTestCategory);
        if (Constant.APP_CONSTANT.TYPE().equals("4") || Constant.APP_CONSTANT.TYPE().equals("6")) {
            if (mTestCategory.equals("G")) {
                mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR + "0/";
            }
        }

        totalTime = getIntent().getIntExtra("testTime", -1) * 60;
        mTitleName = CommonUtils.getAbilityTitleName(mTestCategory);
        mQuesList = new ArrayList<>();
        mQuesList = (ArrayList<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra("QuestionList");
//        testRecordList = new ArrayList<>();//记录用的答题记录
        testRecordMap = new HashMap<>();//记录用的答题记录
        //统计测评题目所包含的类型
        HashSet set = new HashSet();
        for (AbilityQuestion.TestListBean ques : mQuesList) {
            set.add(ques.getCategory());
        }
        mTestTypeArr = new String[set.size()];
        Iterator<String> iterator = set.iterator();
        int aa = 0;
        while (iterator.hasNext()) {
            mTestTypeArr[aa] = iterator.next();
            aa++;
        }
        mCategory = new int[set.size()];
        rightNum = new int[set.size()];
        userAns = new String[mQuesList.size()];
        mTotal = mQuesList.size();
        helper = TestRecordHelper.getInstance(mContext);

        mTimer = new Timer();
        mPlayer = new Player(mContext, null);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initTitleBar();
        scrollViewAnswer = findView(R.id.scroll_answer);
        scrollViewQuestion = findView(R.id.scroll_question);

        if (!Constant.APP_CONSTANT.isEnglish()
                && !Constant.APP_CONSTANT.TYPE().equals("3")
                && "L".equals(getIntent().getStringExtra("testCategory"))) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 4.5f);
            scrollViewAnswer.setLayoutParams(params);
        }

        btn_next = findView(R.id.tv_next_ques);
        tv_pre_ques = findView(R.id.tv_pre_ques);
        timeUsed = findView(R.id.time_num);
        quesIndex = findView(R.id.tv_ques_index);
        mRoundProgressBar = findViewById(R.id.word_read);
        mll_jugde_consl = findView(R.id.ll_ability_judge);//判断题目答题卡
        mll_blank_consl = findView(R.id.ll_ability_blank_consl);//填空题目答题卡
        ll_ability_listen_mulchose = findView(R.id.ll_choose_multy);//多选题目答题卡
        tv_question_type = findView(R.id.tv_question_type);//试题类型
        //多选
        mCheckBox_A = findView(R.id.checkBoxA);
        mCheckBox_B = findView(R.id.checkBoxB);
        mCheckBox_C = findView(R.id.checkBoxC);
        mCheckBox_D = findView(R.id.checkBoxD);
        mCheckBox_E = findView(R.id.checkBoxE);
        virtualKey = findViewById(R.id.virtual_keyboard);
        rg_judge = findView(R.id.rg_judge);
        //单选
        tv_jude_detail = findView(R.id.tv_ability_listen_judge_ques);//判断题目的 题目要求线面的问题详情
        wordImage = findViewById(R.id.word_img);
        mTv_uestion = findView(R.id.write_ques);
        ll_play_controller = findView(R.id.ll_play_controller);//播放布局
        ibtn_player_controller = findView(R.id.ibtn_player_controller);//播放按钮
        sb_player = findView(R.id.sb_player);

        mPb_timeLeft = findView(R.id.time_line);
        mPb_timeLeft.setMax(totalTime);
        mPb_timeLeft.setProgress(totalTime);
        mTv_catetgory = findView(R.id.tv_line);
        mTv_jugde_ques = findView(R.id.tv_ability_listen_judge);
        mTv_blank_ques = findView(R.id.tv_ability_blank_ques);
        chosnChar = findViewById(R.id.chosen_char);
        tv_ability_chosn_ques = findView(R.id.tv_ability_chosn_ques);
        question = findViewById(R.id.tv_ques_word);
        rb_option_a_1 = findView(R.id.rb_option_a_1);//a right
        rb_option_b_2 = findView(R.id.rb_option_b_2);//b 2 wrong
        rb_option_c_0 = findView(R.id.rb_option_c_0);//c 0 unknown
        rb_option_d = findView(R.id.rb_option_d);//d
        rb_option_e = findView(R.id.rb_option_e);//e
        rb_option_f = findView(R.id.rb_option_f);//f 默认选项  不可见
        keyboardr = findViewById(R.id.keyboardr);
        mEt_userAnswer = findView(R.id.et_ability_uanswer);
        rv_review = findView(R.id.rv_review);
        tv_review_title = findView(R.id.tv_review_title);
        tv_ability_right = findView(R.id.tv_ability_right);
        tv_practice_explain = findView(R.id.tv_practice_explain);
        tv_practice_explain.setMovementMethod(ScrollingMovementMethod.getInstance());
//        sv_answer_container = findView(R.id.sv_answer_container);
    }

    @Override
    protected void loadData() {
        //时间轴
        ll_time_line = findView(R.id.ll_time_line);
        if (flag_mode == FLAG_PRACTICE) { // 练习状态显示 需要显示答案
            ll_time_line.setVisibility(View.GONE);//时间轴隐藏
            tv_ability_right.setVisibility(View.VISIBLE);
        } else if (flag_mode == FLAG_TEST) {  //练习模式
            ll_time_line.setVisibility(View.VISIBLE);
            tv_ability_right.setVisibility(View.GONE);
            TextView tv_totaltime = findView(R.id.tv_totaltime);
            tv_totaltime.setText(totalTime / 60 + "m");
            thread.run();
        }
        btn_next.setOnClickListener(nextButtonClickListener(true));
        tv_pre_ques.setOnClickListener(nextButtonClickListener(false));
        //音频播放
        ibtn_player_controller.setOnClickListener(playButtonClickListener());
        sb_player.setOnSeekBarChangeListener(seekBarChangeListener());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 10);
        rv_review.setLayoutManager(gridLayoutManager);
        final ReviewAdapter ada = new ReviewAdapter(mContext, mTotal);
        rv_review.setAdapter(ada);
        ada.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (testRecordMap.size() > position) {
                    index = position + 1;
                    proIECC(false);
                } else {
                    ToastUtil.showToast(mContext, "本题未答");
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        proIECC(true);//自动出第一个题目

        tv_review_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = rv_review.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                ada.setCurrentPosition(testRecordMap.size());
                rv_review.setVisibility(visibility);
            }
        });
        tv_ability_right.setTextColor(getResources().getColor(R.color.text_gray_color));
        tv_ability_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVip()) {
                    ToastUtil.showToast(mContext, "查看解析目前仅对vip用户开放");
                    return;
                }
                if (tv_practice_explain.getVisibility() == View.VISIBLE) {
                    tv_practice_explain.setVisibility(View.GONE);
                    tv_ability_right.setTextColor(getResources().getColor(R.color.text_gray_color));
                    tv_ability_right.setBackgroundResource(R.drawable.btn_bg_ability_explains_grey);
                } else {
                    rv_review.setVisibility(View.GONE);
                    tv_practice_explain.setVisibility(View.VISIBLE);
                    tv_ability_right.setTextColor(getResources().getColor(R.color.text_bluenor_color));
                    tv_ability_right.setBackgroundResource(R.drawable.btn_bg_ability_explains);
                    scrollViewAnswer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollViewAnswer.scrollTo(0, scrollViewAnswer.getChildAt(0).getMeasuredHeight() + 200);
                        }
                    }, 300);
                }

            }
        });
    }

    private View.OnClickListener nextButtonClickListener(final boolean isNext) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_review.setVisibility(View.GONE);
                if (index <= mTotal) {//不是最后一个题目
                    scrollViewAnswer.fullScroll(ScrollView.FOCUS_UP);
                    scrollViewQuestion.fullScroll(ScrollView.FOCUS_UP);

                    mTestRecord.TestTime = deviceInfo.getCurrentTime();//这里在点击next 的时间作为答题时间  选择判断等点击按钮的时间为答题时间
                    //根据不同题型处理及记录答案
                    switch (mQuesList.get(index).getTestType()) {
                        case Constant.ABILITY_TESTTYPE_BLANK_WORD:
                            if (hasUserAnswer(chosn)) {
                                analysisUserAnswer2(mQuesList.get(index).getAnswer().trim(), chosn);
                            } else if (userAns[index] != null && !userAns[index].equals("")) {
                                analysisUserAnswer(mQuesList.get(index).getAnswer().trim(), userAns[index]);
                            } else if (isNext) {//点击下一题本题必答
                                ToastUtil.showToast(mContext, "本题目没有回答^_^");
                                return;
                            }
                            break;
                        case Constant.ABILITY_TESTTYPE_BLANK://填空题
                            if (!mEt_userAnswer.getText().toString().trim().equals("")) {//答题了
                                String uAnswer = mEt_userAnswer.getText().toString().trim();
                                if (uAnswer.length() > 150) { //用户的答案不能超过50个字符  这是服务器的限制,否则提交出错
                                    mEt_userAnswer.setError("答案不超过150字符!");
                                    return;
                                }
                                //正确答案为null  或者为  Students' own answer 算答对了
                                if (mQuesList.get(index).getAnswer() == null
                                        || mQuesList.get(index).getAnswer().equals("")
                                        || mQuesList.get(index).getAnswer().equals("Students' own answer")) {
                                    mTestRecord.AnswerResult = 1;
                                    LogUtils.e("蒙对了 ");
                                } else {
                                    String r = mQuesList.get(index).getAnswer();
                                    String[] rightAnswers = r.split("//");//填空题目,正确答案里面可能有多种情况  拿用户的答案和所有可能的答案比较,有一个相同算是答对了
                                    for (String s : rightAnswers) {
                                        LogUtils.e("正确的答案 " + s + "\n");
                                        if (s.equalsIgnoreCase(uAnswer)) {
                                            mTestRecord.AnswerResult = 1;
                                        } else {
                                            mTestRecord.AnswerResult = 0;
                                            LogUtils.e("没有蒙对");
                                        }
                                    }
                                }
                                mTestRecord.UserAnswer = mEt_userAnswer.getText().toString().trim();
                                testRecordMap.put(index, mTestRecord);
                                mQuesList.get(index).setUserAnswer(mTestRecord.UserAnswer);
                                mQuesList.get(index).setResult(mTestRecord.AnswerResult + "");
                                mQuesList.get(index).flag_ever_do = true;
                                DataManager.getInstance().practiceList = mQuesList;
                            } else if (isNext) {//点击下一题本题必答
                                ToastUtil.showToast(mContext, "本题目没有回答^_^");
                                return;
                            }
                            break;
                        case Constant.ABILITY_TESTTYPE_JUDGE://判断
                            String radioText = getUserAnswerJugde();
                            if (radioText != null && radioText.equals("A")) {
                                userAns[index] = "1";
                                analysisUserAnswer(mQuesList.get(index).getAnswer().trim(), userAns[index]);
                            } else if (radioText != null && radioText.equals("B")) {
                                userAns[index] = "0";
                                analysisUserAnswer(mQuesList.get(index).getAnswer().trim(), userAns[index]);
                            } else if (radioText != null && radioText.equals("C")) {
                                userAns[index] = "2";
                                analysisUserAnswer(mQuesList.get(index).getAnswer().trim(), userAns[index]);
                            } else if (isNext) {//点击下一题本题必答
                                ToastUtil.showToast(mContext, "本题目未答^_^");
                                return;
                            }
                            break;
                        case Constant.ABILITY_TESTTYPE_SINGLE://单选
                        case Constant.ABILITY_TESTTYPE_CHOSE_PIC://图片选择
                            radioText = getUserAnswerJugde();
                            userAns[index] = radioText;
                            if (radioText != null) {
                                analysisUserAnswer(mQuesList.get(index).getAnswer().trim(), userAns[index]);
                            } else if (isNext) {//点击下一题本题必答
                                ToastUtil.showToast(mContext, "本题目未答^_^");
                                return;
                            }
                            break;
                        case Constant.ABILITY_TESTTYPE_BLANK_CHOSE://空白选择
                        case Constant.ABILITY_TESTTYPE_MULTY://多选
                            String userAnswer = getUserAnswer();
                            if (isNext && userAnswer.trim().equals("")) {
                                ToastUtil.showToast(mContext, "本题目没有回答^_^");
                                return;
                            }
                            mTestRecord.TestTime = deviceInfo.getCurrentTime();
                            analysisUserAnswer(mQuesList.get(index).getAnswer(), userAnswer);
                            break;
                        case Constant.ABILITY_TESTTYPE_VOICE://语音评测
                            if (isNext && (testRecordMap.get(index) == null || TextUtils.isEmpty(testRecordMap.get(index).UserAnswer))) {
                                ToastUtil.showToast(mContext, "本题目没有回答^_^");
                                return;
                            }
                            break;
                    }
                    if (isNext && index < mTotal - 1) {
                        proIECC(isNext);//出下一道题目
                    } else if (isNext) {//点击下一道
                        if (flag_mode == FLAG_TEST) {//测评模式
                            gotoResultActivity();
                        } else if (flag_mode == 2) {//练习模式
                            handler.sendEmptyMessage(SAVERESULT);//保存分析记录  上传数据到服务器
                            AbilityTestActivity.this.finish();
                            Intent intent = new Intent(AbilityTestActivity.this, ShowAnalysisActivity.class);
                            intent.putExtra("testCategory", mTestCategory);
                            startActivity(intent);
                        }
                    } else {
                        proIECC(isNext);//出上一道题目
                    }
                }
            }
        };
    }

    private String getUserAnswerJugde() {
        if (rb_option_a_1.isChecked()) {
            return "A";
        } else if (rb_option_b_2.isChecked()) {
            return "B";
        } else if (rb_option_c_0.isChecked()) {
            return "C";
        } else if (rb_option_d.isChecked()) {
            return "D";
        } else if (rb_option_e.isChecked()) {
            return "E";
        } else if (rb_option_f.isChecked()) {
            return null;
        }
        return null;
    }

    /***
     * 判断单词拼写用户是否有答案
     *
     * @param answer 答题板上的答案
     * @return 用户是否拼写
     */
    private boolean hasUserAnswer(List<String> answer) {
        boolean has = false;
        for (String a : answer) {
            if (!a.equals("_")) {
                has = true;
                break;
            }
        }
        return has;
    }

    @NonNull
    private View.OnClickListener playButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String au = mLocalAudioPrefix + mQuesList.get(index).getSounds();
                LogUtils.e(index + " 音频: " + au);
                if (mPlayState == PLAYER_STATE_PLAY) {
                    mPlayer.pause();
                    mPlayState = PLAYER_STATE_PAUSE;
                    ibtn_player_controller.setImageResource(R.mipmap.ic_audio_play);
                } else if (mPlayState == PLAYER_STATE_PAUSE) {
                    mPlayer.play();
                    mPlayState = PLAYER_STATE_PLAY;
                    ibtn_player_controller.setImageResource(R.mipmap.ic_audio_pause);
                } else {
                    mPlayer.playUrl(au);
                    mPlayState = PLAYER_STATE_PLAY;
                    ibtn_player_controller.setImageResource(R.mipmap.ic_audio_pause);
                    mTimer.schedule(new RequestTimerTask(), 0, 10);
                }
            }
        };
    }

    @NonNull
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    LogUtils.e(TAG, "seekto :   " + progress);
                    mPlayer.seekTo(progress * mPlayer.getDur() / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private void proIECC(boolean isNext) {
        if (isNext) {//下一题
            index++;
            if (index > 0) {//音频接着放
                String au_pre = mLocalAudioPrefix + mQuesList.get(index - 1).getSounds();
                String au = mLocalAudioPrefix + mQuesList.get(index).getSounds();
                if (!au.equals(au_pre) && mPlayer.isPlaying()) {
                    LogUtils.e(TAG, index + "  这个音频: " + au + "\n上个音频 : " + au_pre);
                    mPlayer.pause();
                    mPlayState = PLAYER_STATE_STOP;
                    handler.sendEmptyMessage(1);
                }
            }
        } else {//上一题
            index--;
            if (index > 0) {//音频接着放
                String au_pre = mLocalAudioPrefix + mQuesList.get(index + 1).getSounds();
                String au = mLocalAudioPrefix + mQuesList.get(index).getSounds();
                if (!au.equals(au_pre) && mPlayer.isPlaying()) {
                    LogUtils.e(TAG, index + "  这个音频: " + au + "\n上个音频 : " + au_pre);
                    mPlayer.pause();
                    mPlayState = PLAYER_STATE_STOP;
                    handler.sendEmptyMessage(1);
                }
            }
        }
        String userTempAnsewer = mQuesList.get(index).getUserAnswer();//用于回显用户的答案
        boolean showAnswer = mQuesList.get(index).flag_ever_do;
        //自动隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn_next.getWindowToken(), 0);
        //答题记录的
        mTestRecord = new TestRecord();
        if (AccountManager.Instace(mContext).checkUserLogin()) {
            mTestRecord.uid = getUid();
        } else {
            mTestRecord.uid = "0";
        }
        mTestRecord.appId = Constant.APPID;
        mTestRecord.index = 1;
        mTestRecord.deviceId = deviceInfo.getLocalMACAddress();
        mTestRecord.TestCategory = mTestCategory;
        mTestRecord.mode = flag_mode;

        mTestRecord.BeginTime = deviceInfo.getCurrentTime();
        mTestRecord.Id = mQuesList.get(index).getId() + "";
        mTestRecord.TestNumber = mQuesList.get(index).getTestId();
        mTestRecord.UserAnswer = "";
        mTestRecord.RightAnswer = mQuesList.get(index).getAnswer();
        mTestRecord.Categroy = mQuesList.get(index).getCategory();
        userAns[index] = "";//这里是写0还是写空比较好一些呢?

        //判断有没有音频  有音频显示播放进度条
        if (TextUtils.isEmpty(mQuesList.get(index).getSounds())) {
            ll_play_controller.setVisibility(View.GONE);
        } else {
            ll_play_controller.setVisibility(View.VISIBLE);
        }

        if (mPlayState == PLAYER_STATE_STOP) {
            ibtn_player_controller.setImageResource(R.mipmap.ic_audio_play);
        }

        boolean hasAttach = !TextUtils.isEmpty(mQuesList.get(index).getAttach());
        //显示对应的答题卡
        if (!TextUtils.isEmpty(mQuesList.get(index).getQuestion())) {
            if (hasAttach) {
                tv_ability_chosn_ques.setVisibility(View.GONE);
            } else {
                tv_ability_chosn_ques.setVisibility(View.VISIBLE);
            }
            if (mQuesList.get(index).getQuestion().split("\\+\\+").length >= 2) {
                CommonUtils.showTextWithUnderLine(tv_ability_chosn_ques, mQuesList.get(index).getQuestion().split("\\+\\+")[0]);
                question.setVisibility(View.VISIBLE);
                question.setText(mQuesList.get(index).getQuestion().split("\\+\\+")[1]);//问题详情
                question.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            } else {
                CommonUtils.showTextWithUnderLine(tv_ability_chosn_ques, mQuesList.get(index).getQuestion());
                question.setVisibility(View.GONE);
            }
        } else {
            question.setVisibility(View.GONE);
            tv_ability_chosn_ques.setVisibility(View.GONE);
        }

        if (hasAttach) {
            mTv_jugde_ques.setVisibility(View.VISIBLE);
            mTv_blank_ques.setVisibility(View.VISIBLE);
            mTv_blank_ques.setText(mQuesList.get(index).getQuestion());

        } else {
            mTv_jugde_ques.setVisibility(View.GONE);
            mTv_blank_ques.setVisibility(View.GONE);
        }

        switch (mQuesList.get(index).getTestType()) {
            case Constant.ABILITY_TESTTYPE_BLANK://填空题
                mRoundProgressBar.setVisibility(View.GONE);
                mEt_userAnswer.setText("");
                mEt_userAnswer.setHint("请在此填写答案");
                mll_jugde_consl.setVisibility(View.GONE);//判断
                ll_ability_listen_mulchose.setVisibility(View.GONE);
                mll_blank_consl.setVisibility(View.VISIBLE);
                wordImage.setVisibility(View.GONE);
                keyboardr.setVisibility(View.GONE);//单词拼写
                chosnChar.setVisibility(View.GONE);
                //mTv_blank_ques.setVisibility(View.GONE);//根据需求隐藏该项 2017.7.22
//                mTv_blank_ques.setText(mQuesList.get(index).getQuestion());
                tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + "-填空)");

                if (showAnswer && userTempAnsewer != null) {
                    mEt_userAnswer.setText(userTempAnsewer);
                }
                break;
            case Constant.ABILITY_TESTTYPE_CHOSE_PIC:////单选题目  图 中选英
                mll_jugde_consl.setVisibility(View.VISIBLE);
                keyboardr.setVisibility(View.GONE);
                chosnChar.setVisibility(View.GONE);
                wordImage.setVisibility(View.VISIBLE);
                ll_ability_listen_mulchose.setVisibility(View.GONE);//选择题卡
                mll_blank_consl.setVisibility(View.GONE);
                mRoundProgressBar.setVisibility(View.GONE);
                wordImage.setImageBitmap(getImageBitmap(mQuesList.get(index).getImage()));//设置本地图片
                rb_option_f.setChecked(true);
                if (mQuesList.get(index).getTestType() == Constant.ABILITY_TESTTYPE_CHOSE_PIC) {
                    tv_jude_detail.setVisibility(View.GONE);
                    tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + "-单选)");
                    //单选题
                    rb_option_a_1.setText("A." + mQuesList.get(index).getAnswer1());
                    rb_option_b_2.setText("B." + mQuesList.get(index).getAnswer2());

                    if (TextUtils.isEmpty(mQuesList.get(index).getAnswer3())) {
                        rb_option_c_0.setVisibility(View.GONE);
                    } else {
                        rb_option_c_0.setVisibility(View.VISIBLE);
                        rb_option_c_0.setText("C." + mQuesList.get(index).getAnswer3());
                    }

                    if (TextUtils.isEmpty(mQuesList.get(index).getAnswer4())) {
                        rb_option_d.setVisibility(View.GONE);
                    } else {
                        rb_option_d.setVisibility(View.VISIBLE);
                        rb_option_d.setText("D." + mQuesList.get(index).getAnswer4());
                    }
                    if (TextUtils.isEmpty(mQuesList.get(index).getAnswer5())) {
                        rb_option_e.setVisibility(View.GONE);
                    } else {
                        rb_option_e.setVisibility(View.VISIBLE);
                        rb_option_e.setText("E." + mQuesList.get(index).getAnswer5());
                    }
                    if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("B")) {
                        rb_option_b_2.setChecked(true);
                    } else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("A"))
                        rb_option_a_1.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("C"))
                        rb_option_c_0.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("D"))
                        rb_option_d.setChecked(true);
                } else {
                    tv_jude_detail.setVisibility(View.VISIBLE);
                    tv_jude_detail.setText(mQuesList.get(index).getAnswer1());
                    tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + "-判断)");
                    //单选题
                    rb_option_a_1.setText("正确");
                    rb_option_b_2.setText("错误");
                    rb_option_c_0.setText("未知");
                    rb_option_c_0.setVisibility(View.VISIBLE);
                    rb_option_d.setVisibility(View.GONE);
                    rb_option_e.setVisibility(View.GONE);
                    //判断题目答案
                    if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("0"))
                        rb_option_b_2.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("1"))
                        rb_option_a_1.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("2"))
                        rb_option_c_0.setChecked(true);
                }
                break;
            case Constant.ABILITY_TESTTYPE_VOICE://语音评测 发音力
                keyboardr.setVisibility(View.GONE);
                chosnChar.setVisibility(View.GONE);
                mll_jugde_consl.setVisibility(View.GONE);//判断
                mll_blank_consl.setVisibility(View.GONE);
                handler.sendEmptyMessage(CHANGEBGIMAGE);
                wordImage.setVisibility(View.GONE);
                question.setVisibility(View.VISIBLE);
                ll_ability_listen_mulchose.setVisibility(View.GONE);
                mRoundProgressBar.setVisibility(View.VISIBLE);
                question.setText(mQuesList.get(index).getAnswer());//正确答案作为朗读
                final String wd = mQuesList.get(index).getAnswer();
                mRoundProgressBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AbilityQuestion.TestListBean bean = mQuesList.get(index) ;
                        mRecordFile = new File(Constant.getsimRecordAddr()+index+Constant.getrecordTag());

                        mRecorder = new RecordManager(mRecordFile);

                        if (isRecording){
                            timer.cancel();
                            mRecorder.stopRecord();
                            uploadVoiceRecorToNet(bean.getQuestion(),bean.getId()+"",bean.getLessonId()+"",index+"",Constant.mListen,
                                    AccountManager.Instace(mContext).userId,mRecordFile);
                        }else {
                            mTestRecord.TestTime = deviceInfo.getCurrentTime();
                            if (!NetWorkState.isConnectingToInternet()) {
                                CustomToast.showToast(mContext, R.string.alert_net_content, 1000);//网络未连接
                            } else {
                                mRecorder.startRecord();
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mRecorder.stopRecord();
                                        uploadVoiceRecorToNet(bean.getQuestion(),bean.getId()+"",bean.getLessonId()+"",index+"",Constant.mListen,
                                                AccountManager.Instace(mContext).userId,mRecordFile);
                                    }
                                },bean.getQuestion().length()*120+2000);
                                isRecording = true ;
                                mRoundProgressBar.setBackgroundResource(R.mipmap.speak_ques_stop);
                            }
                        }

                    }
                });
                tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + ")    tips: 测评成功会自动跳下一题");
                break;
            case Constant.ABILITY_TESTTYPE_MULTY://多选题目
                mRoundProgressBar.setVisibility(View.GONE);
                mll_jugde_consl.setVisibility(View.GONE);
                mll_blank_consl.setVisibility(View.GONE);
                ll_ability_listen_mulchose.setVisibility(View.VISIBLE);
                keyboardr.setVisibility(View.GONE);//单词拼写
                chosnChar.setVisibility(View.GONE);
                setOPtionUnchecked(null);
                if (TextUtils.isEmpty(mQuesList.get(index).getAnswer5())) {
                    mCheckBox_E.setVisibility(View.GONE);
                } else {
                    mCheckBox_E.setText("E." + mQuesList.get(index).getAnswer5());
                }
                mCheckBox_A.setText("A." + mQuesList.get(index).getAnswer1());
                mCheckBox_B.setText("B." + mQuesList.get(index).getAnswer2());
                mCheckBox_C.setText("C." + mQuesList.get(index).getAnswer3());
                if (TextUtils.isEmpty(mQuesList.get(index).getAnswer4())) {
                    mCheckBox_D.setVisibility(View.GONE);
                } else {
                    mCheckBox_D.setVisibility(View.VISIBLE);
                    mCheckBox_D.setText("D." + mQuesList.get(index).getAnswer4());
                }

                tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + "-多选)");

                if (showAnswer)
                    setOPtionUnchecked(userTempAnsewer);
                break;
            case Constant.ABILITY_TESTTYPE_SINGLE://单选题
            case Constant.ABILITY_TESTTYPE_JUDGE://判断题
                mll_jugde_consl.setVisibility(View.VISIBLE);
                mll_blank_consl.setVisibility(View.GONE);
                mRoundProgressBar.setVisibility(View.GONE);
                wordImage.setVisibility(View.GONE);

                String image = mQuesList.get(index).getImage();//设置本地图片
                if (!TextUtils.isEmpty(image)) {
                    wordImage.setVisibility(View.VISIBLE);
                    wordImage.setImageBitmap(getImageBitmap(mQuesList.get(index).getImage()));//设置本地图片
                }

                ll_ability_listen_mulchose.setVisibility(View.GONE);
                keyboardr.setVisibility(View.GONE);//单词拼写
                chosnChar.setVisibility(View.GONE);
                rb_option_f.setChecked(true);
                //mTv_jugde_ques.setVisibility(View.GONE);//根据需求隐藏该项  2017.7.22
                mTv_jugde_ques.setText(mQuesList.get(index).getQuestion());
                if (mQuesList.get(index).getTestType() == Constant.ABILITY_TESTTYPE_SINGLE) {
                    tv_jude_detail.setVisibility(View.GONE);
                    tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + "-单选)");
                    //单选题
                    rb_option_a_1.setText("A." + mQuesList.get(index).getAnswer1());
                    rb_option_b_2.setText("B." + mQuesList.get(index).getAnswer2());

                    if (TextUtils.isEmpty(mQuesList.get(index).getAnswer3())) {
                        rb_option_c_0.setVisibility(View.GONE);
                    } else {
                        rb_option_c_0.setVisibility(View.VISIBLE);
                        rb_option_c_0.setText("C." + mQuesList.get(index).getAnswer3());
                    }

                    if (TextUtils.isEmpty(mQuesList.get(index).getAnswer4())) {
                        rb_option_d.setVisibility(View.GONE);
                    } else {
                        rb_option_d.setVisibility(View.VISIBLE);
                        rb_option_d.setText("D." + mQuesList.get(index).getAnswer4());
                    }
                    if (TextUtils.isEmpty(mQuesList.get(index).getAnswer5())) {
                        rb_option_e.setVisibility(View.GONE);
                    } else {
                        rb_option_e.setVisibility(View.VISIBLE);
                        rb_option_e.setText("E." + mQuesList.get(index).getAnswer5());
                    }
                    if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("B"))
                        rb_option_b_2.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("A"))
                        rb_option_a_1.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("C"))
                        rb_option_c_0.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("D"))
                        rb_option_d.setChecked(true);
                } else {
                    tv_jude_detail.setVisibility(View.VISIBLE);
                    tv_jude_detail.setText(mQuesList.get(index).getAnswer1());
                    tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + "-判断)");
                    //单选题
                    rb_option_a_1.setText("正确");
                    rb_option_b_2.setText("错误");
                    rb_option_c_0.setText("未知");
                    rb_option_c_0.setVisibility(View.VISIBLE);
                    rb_option_d.setVisibility(View.GONE);
                    rb_option_e.setVisibility(View.GONE);
                    //判断题目答案
                    if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("0"))
                        rb_option_b_2.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("1"))
                        rb_option_a_1.setChecked(true);
                    else if (showAnswer && userTempAnsewer != null && userTempAnsewer.contains("2"))
                        rb_option_c_0.setChecked(true);
                }

                break;
            case Constant.ABILITY_TESTTYPE_BLANK_WORD://听写  拼写单词
                keyboardr.setVisibility(View.VISIBLE);
                chosnChar.setVisibility(View.VISIBLE);
                Collections.shuffle(standardLetters);//随机打乱
                mRoundProgressBar.setVisibility(View.GONE);
                wordImage.setVisibility(View.GONE);
                mll_jugde_consl.setVisibility(View.GONE);//判断
                mll_blank_consl.setVisibility(View.GONE);//填空
                ll_ability_listen_mulchose.setVisibility(View.GONE);
                chosnChar.removeAllViews();
                final String word = mQuesList.get(index).getAnswer().trim();//这是本道题用户要输入的词语
                LogUtils.e("单词答案: " + word);
                char[] words = word.toCharArray();
                wordLength = words.length;
                wordIndex = 0;
                letters.clear();
                chosn.clear();

                for (int i = 0; i < wordLength; i++) {
                    letters.add(String.valueOf(words[i]));
                    if (showAnswer && userTempAnsewer != null && i < userTempAnsewer.toCharArray().length) {
                        chosn.add(String.valueOf(userTempAnsewer.toCharArray()[i]));
                    } else {
                        chosn.add("_");
                    }
                }

                showChosnChars(chosn);
                //毛用??
                for (int i = 0; i < standardLetters.size(); i++) {
                    boolean flag;
                    flag = true;
                    for (int j = 0; j < letters.size(); j++) {
                        if (standardLetters.get(i).equals(letters.get(j)))
                            flag = false;
                    }
                    if (flag)
                        letters.add((String) standardLetters.get(i));
                    if (letters.size() == 19)
                        break;
                }

                Collections.shuffle(letters);
                virtualKey.setAdapter(new KeyboardAdapter(mContext, letters));//键盘上的显示字母

                virtualKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 19) {//最后一个按钮,需要对比答案  进入下一个题目  修改为删除按钮
                            if (wordIndex != 0) {
                                chosn.set(wordIndex - 1, "_");
                                wordIndex--;
                                showChosnChars(chosn);
                            }
                        } else {
                            TextView tv1 = view.findViewById(R.id.keyboard_text);
                            String a = (String) tv1.getText();
                            LogUtils.e("用户输入的呢::" + a);
                            if (wordIndex < wordLength) {
                                chosn.set(wordIndex, a);
                                showChosnChars(chosn);
                                wordIndex++;
                            }
                        }
                    }
                });
                tv_question_type.setText("Question(" + mQuesList.get(index).getTags() + "-拼写)");
                break;
        }
//        String mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR;
        String textFormText = FileUtil.getTextFromFile(mLocalAudioPrefix + mQuesList.get(index).getAttach());
        if (TextUtils.isEmpty(textFormText)) {
            mTv_uestion.setVisibility(View.GONE);
        } else {
            mTv_uestion.setVisibility(View.VISIBLE);
            CommonUtils.showTextWithUnderLine(mTv_uestion, textFormText);
        }
        tv_practice_explain.setText(FileUtil.getTextFromFile(mLocalAudioPrefix + mQuesList.get(index).Explains));
        LogUtils.e(index + 1 + "本道题目的答案:  " + mQuesList.get(index).getAnswer());
        quesIndex.setText(String.valueOf(index + 1) + "/" + mTotal);
        // 日语显示界面
        if (!Constant.APP_CONSTANT.isEnglish()) {
            if ("L".equals(mTestCategory)) {// 如果是听力的话
                tv_ability_chosn_ques.setVisibility(View.GONE);
                mTv_jugde_ques.setVisibility(View.VISIBLE);
                mTv_jugde_ques.setText(mQuesList.get(index).getQuestion());
                mTv_catetgory.setText("Sound(" + mQuesList.get(index).getCategory() + ")");
            } else {
                mTv_catetgory.setText("Text(" + mQuesList.get(index).getCategory() + ")");
            }
            if ("W".equals(mTestCategory)
                    || "G".equals(mTestCategory)) {// 如果是单词和阅读的话
//                ){
                mTv_catetgory.setVisibility(View.GONE);
                scrollViewQuestion.setVisibility(View.GONE);
                mTv_jugde_ques.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(mQuesList.get(index).getQuestion())) {
                    mTv_jugde_ques.setText(textFormText);
                } else {
                    mTv_jugde_ques.setText(mQuesList.get(index).getQuestion());
                }
            }
        }

        if (index == 0) {
            tv_pre_ques.setVisibility(View.GONE);
        } else {
            tv_pre_ques.setVisibility(View.VISIBLE);
        }
        if (index == mTotal - 1) {
            btn_next.setText(" 完 成 ");
        } else {
            btn_next.setText(" 下一题 ");
        }
        //显示解析按钮
        if (flag_mode == FLAG_PRACTICE && !TextUtils.isEmpty(mQuesList.get(index).Explains)) { // 练习状态显示 需要显示答案e
            tv_ability_right.setVisibility(View.VISIBLE);
            tv_ability_right.setTextColor(getResources().getColor(R.color.text_gray_color));
            tv_ability_right.setBackgroundResource(R.drawable.btn_bg_ability_explains_grey);
            tv_practice_explain.setVisibility(View.GONE);
        } else if (flag_mode == FLAG_PRACTICE && TextUtils.isEmpty(mQuesList.get(index).Explains)) { // 无解析
            tv_ability_right.setVisibility(View.GONE);
            tv_ability_right.setTextColor(getResources().getColor(R.color.text_gray_color));
            tv_ability_right.setBackgroundResource(R.drawable.btn_bg_ability_explains_grey);
            tv_practice_explain.setVisibility(View.GONE);
        } else if (flag_mode == FLAG_TEST) {  //评测模式
            tv_ability_right.setVisibility(View.GONE);
            tv_practice_explain.setVisibility(View.GONE);
        }
    }

    private Runnable thread = new Runnable() {
        @Override
        public void run() {
            totalTime--;
            handler.sendEmptyMessageDelayed(4, 1000);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RoundProgressBar tempBar = null;
            switch (msg.what) {
                case 1:
                    sb_player.setProgress(0);
                    break;
                case 2:// set the read button progress bar with value of voice volume
                    try {
                        tempBar = mRoundProgressBar;
                        int db = msg.arg1;
                        tempBar.setCricleProgressColor(0xfffea523);
                        tempBar.setMax(100);
                        tempBar.setProgress(db);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3: // reset the read button progress bar
                    try {
                        mRoundProgressBar.setBackgroundResource(R.mipmap.speak_ques_read);
                        tempBar = mRoundProgressBar;
                        tempBar.setCricleProgressColor(0xff87c973);
                        tempBar.setMax(100);
                        tempBar.setProgress(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    mPb_timeLeft.incrementProgressBy(-1);
                    String a = String.valueOf(totalTime / 60);
                    String b = String.valueOf(totalTime % 60);
                    timeUsed.setText(a + "m " + b + "s");
                    if (totalTime != 0) {
                        thread.run();
                    } else {
                        //时间结束
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle("提示");
                        dialog.setMessage(getResources().getString(R.string.clock_over));
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                gotoResultActivity();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }

                    break;
                case 6://语音评测
                    int score = msg.arg1;
                    int id = msg.arg2;
                    boolean is_rejected = (Boolean) msg.obj;
                    btn_next.setClickable(true);
                    if (is_rejected) {//测评失败
                        mTestRecord.UserAnswer = "-1";
                        mTestRecord.AnswerResult = 0;
                        CustomToast.showToast(mContext, "语音异常，请重新录入!", 1500);
                    } else {//测评成功  保存测评结果,进入下一道题目
                        mTestRecord.RightAnswer = mQuesList.get(id).getAnswer();
                        LogUtils.e("第" + (id + 1) + "题得分:" + score);
                        if (score >= 75) {
                            mTestRecord.AnswerResult = 1;
                            mTestRecord.UserAnswer = mQuesList.get(id).getAnswer();
                        } else {
                            mTestRecord.AnswerResult = 0;
                            mTestRecord.UserAnswer = "-1";
                        }
//                        testRecordList.add(mTestRecord);//11.2改
                        testRecordMap.put(index, mTestRecord);//2017.8.12
                        CustomToast.showToast(mContext, "评测成功!", 1200);

                        if (id < mTotal - 1) {
                            proIECC(true);//出下一道题目
                        } else {
                            btn_next.setText("完成");
                            ToastUtil.showLongToast(mContext, "已经是最后一个题目了");
                        }
                    }
                    break;
                case SAVERESULT:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<TestRecord> testRecordList = new ArrayList<>();
                            for (TestRecord rd : testRecordMap.values()) {
                                testRecordList.add(rd);
                            }
                            uploadTestRecordToNetLocal(testRecordList, abilityResult, flag_mode);//上传数据到服务器
                        }
                    }).start();

                    break;
                case FINISHSELF:
                    AbilityTestActivity.this.finish();
                    break;
                case CHANGEBGIMAGE:
                    mRoundProgressBar.setBackgroundResource(R.mipmap.speak_ques_read);
                    break;
                case 1000:
                    mRoundProgressBar.setClickable(true);//评测时网络连接中断
                    btn_next.setClickable(true);
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
            }
        }
    };

    /**
     * 记录用户的答案  多选题
     *
     * @return 用户的答案
     */
    private String getUserAnswer() {
        String ABCD = "";
        if (mCheckBox_A.isChecked()) ABCD += "A";
        if (mCheckBox_B.isChecked()) ABCD += "B";
        if (mCheckBox_C.isChecked()) ABCD += "C";
        if (mCheckBox_D.isChecked()) ABCD += "D";
        if (mCheckBox_E.isChecked()) ABCD += "E";
        return ABCD;
    }

    private void addAbilityResultsToEntity() {
        int totalrightnum = 0;
        String endTime = deviceInfo.getCurrentTime();
        //计算每个维度正确题目的个数
        for (TestRecord rd : testRecordMap.values()) {
            //题目分类
            for (int j = 0; j < mTestTypeArr.length; j++) {
                if (TextUtils.equals(rd.Categroy, mTestTypeArr[j])) {
                    mCategory[j]++;
                }
            }
            if (rd.AnswerResult == 1) {
                for (int j = 0; j < mTestTypeArr.length; j++) {
                    if (TextUtils.equals(rd.Categroy, mTestTypeArr[j])) {
                        rightNum[j]++;
                    }
                }
            }
        }
        for (int x : rightNum) {
            totalrightnum = totalrightnum + x;
        }
        //成绩分析保存数据库
//        abilityResult.TypeId = mCategoryType;
        abilityResult.TypeId = CommonUtils.getAbilityCategoryId(mTestCategory);//听说读写
        //Score字段格式   小项能力类型题目总数++正确的个数++能力类型
        switch (mTestTypeArr.length) {
            case 10:
                abilityResult.Score10 = parseSavePattern(TYPE_9);
            case 9:
                abilityResult.Score9 = parseSavePattern(TYPE_8);
            case 8:
                abilityResult.Score8 = parseSavePattern(TYPE_7);
            case 7:
                abilityResult.Score7 = parseSavePattern(TYPE_6);
            case 6:
                abilityResult.Score6 = parseSavePattern(TYPE_5);
            case 5:
                abilityResult.Score5 = parseSavePattern(TYPE_4);
            case 4:
                abilityResult.Score4 = parseSavePattern(TYPE_3);
            case 3:
                abilityResult.Score3 = parseSavePattern(TYPE_2);
            case 2:
                abilityResult.Score2 = parseSavePattern(TYPE_1);
            case 1:
                abilityResult.Score1 = parseSavePattern(TYPE_0);
                break;
        }
        abilityResult.DoRight = totalrightnum;//正确个数
        abilityResult.Total = mTotal;//题目总数
        abilityResult.UndoNum = 0;//没有回答的个数
        abilityResult.beginTime = mBegintime;//测试开始时间
        abilityResult.endTime = endTime;//测试结束时间
        abilityResult.uid = AccountManager.Instace(mContext).userId;
    }

    /**
     * 转化为存储到数据表的格式  题目总数+正确个数+题目分类
     *
     * @param id 与score 对应的 index
     * @return s
     */
    private String parseSavePattern(int id) {
        return mCategory[id] + "++" + rightNum[id] + "++" + mTestTypeArr[id];
    }

    //用户答题统计
    private void analysisUserAnswer(String rightAnswer, String userAnswer) {
        //测试记录
        mTestRecord.UserAnswer = userAnswer;
        LogUtils.e("题目的index =" + (index) + "  Right===" + rightAnswer + "  userAnswer==" + userAnswer);
        if (rightAnswer.equals(userAnswer)) {
            LogUtils.e("答对了");
            mTestRecord.AnswerResult = 1;
            LogUtils.e("蒙对了 ");
        } else {
            mTestRecord.AnswerResult = 0;
            LogUtils.e("答错了");
        }
        testRecordMap.put(index, mTestRecord);
        mQuesList.get(index).setUserAnswer(mTestRecord.UserAnswer);
        mQuesList.get(index).setResult(mTestRecord.AnswerResult + "");
        mQuesList.get(index).flag_ever_do = true;
        DataManager.getInstance().practiceList = mQuesList;
    }

    //用户答题统计  拼写题目
    private void analysisUserAnswer2(String standAnswer, List<String> userAnswerList) {
        String userAnswer = "";
        for (int i = 0; i < userAnswerList.size(); i++) {
            userAnswer = userAnswer + userAnswerList.get(i);
        }
        LogUtils.e("userAnswer   " + userAnswer);
        if (standAnswer.equals(userAnswer)) {
            LogUtils.e("蒙对了");
            mTestRecord.AnswerResult = 1;
        } else {
            LogUtils.e("打错了");
            mTestRecord.AnswerResult = 0;
        }
        //测试记录
        mTestRecord.UserAnswer = userAnswer;
        testRecordMap.put(index, mTestRecord);
        mQuesList.get(index).setUserAnswer(mTestRecord.UserAnswer);
        mQuesList.get(index).setResult(mTestRecord.AnswerResult + "");
        mQuesList.get(index).flag_ever_do = true;
        DataManager.getInstance().practiceList = mQuesList;
    }

    private void gotoResultActivity() {
        addAbilityResultsToEntity();
        handler.sendEmptyMessage(SAVERESULT);
        Intent intent = new Intent();
        intent.putExtra("testType", CommonUtils.getAbilityCategoryId(mTestCategory));
        intent.putExtra("abilityResults", abilityResult);
        intent.putExtra("testTypeArr", mTestTypeArr);
        intent.setClass(getApplicationContext(), AbilityTestResultActivity.class);
        intent.putExtra("testCategory", mTestCategory);
        handler.sendEmptyMessage(FINISHSELF);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mTimer != null) {
            flag = false;
        }
        ArrayList<TestRecord> testRecordList = new ArrayList<>();
        for (TestRecord rd : testRecordMap.values()) {
            testRecordList.add(rd);
        }
        showAlertDialog(mTotal, index, mTitleName, testRecordList, abilityResult, flag_mode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayState != PLAYER_STATE_STOP) {
            mPlayer.stop();
        }
        handler.removeMessages(4);//停止计时,防止闪退
    }

    /***
     * 初始化titlebar
     */
    private void initTitleBar() {
        ImageButton ibtn_back = findView(R.id.btn_nav_sub);
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tv_titlebar = findView(R.id.tv_titlebar_sub);
        String title = flag_mode == FLAG_TEST ? "能力评测(" + mTitleName + ")" : "能力练习(" + mTitleName + ")";
        tv_titlebar.setText(title);
    }

    private void setOPtionUnchecked(String str) {
        checkOptionChecked(str, "A", mCheckBox_A);
        checkOptionChecked(str, "B", mCheckBox_B);
        checkOptionChecked(str, "C", mCheckBox_C);
        checkOptionChecked(str, "D", mCheckBox_D);
        checkOptionChecked(str, "E", mCheckBox_E);
    }

    private void checkOptionChecked(String userAnswer, String OptionName, CheckBox cb) {
        if (userAnswer != null && userAnswer.contains(OptionName)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
    }

    /**
     * 获取图片资源
     *
     * @param imageName 图片的名字
     * @return
     */

    public Bitmap getImageBitmap(String imageName) {
        String imagePathString = mLocalAudioPrefix + "/" + imageName;

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        float density = dm.density;
        LogUtils.e("density:   " + density);
        try {
            FileInputStream fis = new FileInputStream(imagePathString);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int duration = 0, position = 0;

    class RequestTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mPlayer.isPlaying() && !flag) {
                flag = true;
            }
            if (mPlayer.isPlaying() && flag && mPlayer.mediaPlayer != null) {
                duration = mPlayer.getDur();
                position = mPlayer.mediaPlayer.getCurrentPosition();
                sb_player.setMax(100);
                sb_player.setProgress(position * 100 / duration);
                if (position * 100 / duration > 98 || mTitleName.equals("单词")) {
                    mPlayState = PLAYER_STATE_STOP;
                }
            }
            if (!mPlayer.isPlaying() && flag && mPlayState == PLAYER_STATE_STOP) {
                handler.removeMessages(1);
                handler.sendEmptyMessage(1);
                flag = false;
            }
        }
    }

    /**
     * 单词拼写用户输入的内容
     *
     * @param chosnn 用户的答案
     */
    private void showChosnChars(final List<String> chosnn) {
        chosnChar.removeAllViews();
        //下划线
        for (int i = 0; i < wordLength; i++) {
            TextView tv = new TextView(mContext);
            // tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv.getPaint().setAntiAlias(true);//抗锯齿
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            tv.setText(chosnn.get(i));
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(2, 0, 2, 0);
            chosnChar.addView(tv);
        }
    }

    /**
     * alertdialog 提示用户是继续还是退出测试
     *
     * @param total 试题总数
     * @param mode  1--测评 2 练习
     */
    public void showAlertDialog(int total, int index, final String type, final ArrayList<TestRecord> testRecordList, final AbilityResult abilityResult, final int mode) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("提示:");
        dialog.setMessage("测试进度:" + index + "/" + total + ",是否放弃测试?");
        dialog.setPositiveButton("离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pidition) {
                mTimer.cancel();
                ToastUtil.showToast(mContext, type + "测试未完成");
                //已经保存的数据库记录需要标记一下,不上传服务器
                if (mode == 1) {
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadTestRecordToNetLocal(testRecordList, abilityResult, mode);
                        }
                    }).start();
                }
                finish();
            }
        });
        dialog.setNegativeButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
    private void uploadVoiceRecorToNet(String sentence , String newsid , final String idIndex , String paraid , String type , String uid, File file) {
        Map<String, RequestBody> map = new HashMap<>(6);
        map.put(EvaluateApi.GetVoa.Param.Key.SENTENCE, AbilityTestRequestFactory.fromString(sentence));
        map.put(EvaluateApi.GetVoa.Param.Key.IDINDEX, AbilityTestRequestFactory.fromString(idIndex + ""));
        map.put(EvaluateApi.GetVoa.Param.Key.NEWSID, AbilityTestRequestFactory.fromString(newsid + ""));
        map.put(EvaluateApi.GetVoa.Param.Key.PARAID, AbilityTestRequestFactory.fromString(paraid + ""));
        map.put(EvaluateApi.GetVoa.Param.Key.TYPE,AbilityTestRequestFactory. fromString(type + ""));
        map.put(EvaluateApi.GetVoa.Param.Key.USERID, AbilityTestRequestFactory.fromString(uid));
        Call<SendEvaluateResponse> call = AbilityTestRequestFactory.getEvaluateApi().sendVoice(map,AbilityTestRequestFactory.fromFile(file));
        call.enqueue(new Callback<SendEvaluateResponse>() {
            @Override
            public void onResponse(Call<SendEvaluateResponse> call, Response<SendEvaluateResponse> response) {
                if (response.isSuccessful()){
                    if (null == response.body().getData()){
                        return;
                    }
//                    for (SendEvaluateResponse.DataBean.WordsBean bean : response.body().getData().getWords()) {
//                        if (bean.getScore() < 2) {
//                            list.add(bean.getIndex());
//                        }
//                    }
                    Message message = Message.obtain();
                    message.arg1 = (int) (Float.parseFloat(response.body().getData().getTotal_score()) * 20);
                    message.arg2 = Integer.parseInt(idIndex);
                    message.obj = false;
                    message.what = 6;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<SendEvaluateResponse> call, Throwable t) {

            }
        });
    }
}
