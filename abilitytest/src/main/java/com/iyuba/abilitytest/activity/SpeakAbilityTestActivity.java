package com.iyuba.abilitytest.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
//import com.iflytek.ise.result.Result;
import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityQuestion;
import com.iyuba.abilitytest.entity.AbilityResult;
import com.iyuba.abilitytest.entity.TestRecord;
import com.iyuba.abilitytest.manager.DataManager;
import com.iyuba.abilitytest.utils.FileUtil;
import com.iyuba.abilitytest.utils.NetWorkState;
import com.iyuba.configation.Constant;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.ToastUtil;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.core.widget.dialog.CustomToast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 口语能力测试界面
 * Created by LiuZhenLi on 2016/8/22.
 */
public class SpeakAbilityTestActivity extends AppBaseActivity {
    private final int SAVERESULT = 1001;
    private TextView questionIntro, questionContent, textA, textB, textC, textD,  timeUsed, quesIndex, tv_rightanswer;
    private RoundTextView btn_next;
    private Context mContext = this;
    private ArrayList<AbilityQuestion.TestListBean> mQuesList;
    private int point = 40, index = 0;
    private LinearLayout ansA, ansB, ansC, ansD, speakChoices, ll_rightanswer;
    private RelativeLayout speakVoice;
    private int totalTime;
    private ProgressBar pb;
    private RoundProgressBar rp;
//    private IseManager iseManager;
//    private Result rs;
    private int undoNum;//未答得题目数量
    private int rightNum[];//正确答案
    private String userAns[];//用户的答案
    private GetDeviceInfo mDeviceInfo;
    private String mBegintime;//开始做题的时间
    /**
     * 题目的类型   0--口语发音  1--口语表达  2--口语素材  3--口语逻辑
     */
    private String[] mTestTypeArr;// = Constant.SPEAK_ABILITY_ARR;
    private int mTestType;//展示与图谱上文字的索引
    private int TYPE_FAYIN = 0;//口语发音
    private int TYPE_BIAODA = 1;//口语表达
    private int TYPE_SUCAI = 2;//口语素材
    private int TYPE_LUOJI = 3;//口语逻辑
    private int TYPE_5 = 5;
    private int TYPE_6 = 6;
    private int TYPE_7 = 7;
    private int[] mCategory;// = new int[4];//记录每个题型的题目有几个

    private ArrayList<TestRecord> testRecordList;//存储答题记录 每一道题目的 2016.11.22

    private int mTotal;//试题的总数

    // 做题记录相关的变量
    private TestRecord mTestRecord;
    private GetDeviceInfo deviceInfo;
    private int flag_mode;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_speakabilitytest;
    }

    @Override
    protected void initVariables() {
        mQuesList = new ArrayList<>();
        mQuesList = (ArrayList<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra("QuestionList");
        mTotal = mQuesList.size();
        totalTime = getIntent().getIntExtra("testTime", -1) * 60;
        flag_mode = getIntent().getIntExtra("mode", 1);

        mDeviceInfo = new GetDeviceInfo(mContext);
        mBegintime = mDeviceInfo.getCurrentTime();
        userAns = new String[mTotal];
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
        testRecordList = new ArrayList<>();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initTitlebar();//初始化标题

//        iseManager = IseManager.getInstance(mContext, handler);
        btn_next = findView(R.id.tv_next_ques);
        timeUsed = findView(R.id.time_num);
        quesIndex = findView(R.id.tv_ques_index);
        questionIntro = findView(R.id.speak_ques_introduce);
        questionContent = findView(R.id.speak_ques_content);
        textA = findView(R.id.tv_ans_A);
        textB = findView(R.id.tv_ans_B);
        textC = findView(R.id.tv_ans_C);
        textD = findView(R.id.tv_ans_D);

        ansA = findView(R.id.ll_ansA);
        ansB = findView(R.id.ll_ansB);
        ansC = findView(R.id.ll_ansC);
        ansD = findView(R.id.ll_ansD);
        ll_rightanswer = findView(R.id.ll_rightanswer);
        tv_rightanswer = findView(R.id.tv_rightanswer);
        speakChoices = findView(R.id.speak_ques_choices);
        speakVoice = findView(R.id.speak_ques_speak);

        pb = findView(R.id.time_line);
        pb.setMax(totalTime);
        pb.setProgress(totalTime);
        rp = findView(R.id.sentence_read);

        btn_next.setOnClickListener(nextButtonClickListener());

//        ansA.setOnClickListener(optionClickListener("A"));
//        ansB.setOnClickListener(optionClickListener("B"));
//        ansC.setOnClickListener(optionClickListener("C"));
//        ansD.setOnClickListener(optionClickListener("D"));
        ansA.setOnClickListener(optionClikListener(ansA, "A"));
        ansB.setOnClickListener(optionClikListener(ansB, "B"));
        ansC.setOnClickListener(optionClikListener(ansC, "C"));
        ansD.setOnClickListener(optionClikListener(ansD, "D"));

        rp.setOnClickListener(roundProgressBarClickListener());

        LinearLayout ll_time_line = findView(R.id.ll_time_line);//时间轴
        if (flag_mode == 1) {
            TextView tv_totaltime = findView(R.id.tv_totaltime);
            tv_totaltime.setText(totalTime / 60 + "m");
            ll_time_line.setVisibility(View.VISIBLE);
            thread.run();//时间轴
        } else {
            ll_time_line.setVisibility(View.GONE);
        }
        proIECC();
    }

    private void initTitlebar() {
        TextView tv_titlebar = findView(R.id.tv_titlebar_sub);
        String title = flag_mode == 1 ? "能力评测(口语)" : "能力练习(口语)";
        tv_titlebar.setText(title);
        ImageButton ib_titlebar = findView(R.id.btn_nav_sub);
        ib_titlebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogLocal(mTotal, index, "口语", testRecordList, abilityResult, flag_mode);
            }
        });
    }

    /**
     * 给选中的选项标记颜色
     *
     * @param v 被选中的控件
     */
    private void markSelected(View v) {
        markUnselected();
        setOptionClickable(false);//设置按钮不可以被点击
        v.setBackground(getResources().getDrawable(R.drawable.btn_bg_select));

    }

    /**
     * 设置选项默认没有被选中
     */
    private void markUnselected() {
        ansA.setBackground(getResources().getDrawable(R.drawable.selector_btn_bg));
        ansB.setBackground(getResources().getDrawable(R.drawable.selector_btn_bg));
        ansC.setBackground(getResources().getDrawable(R.drawable.selector_btn_bg));
        ansD.setBackground(getResources().getDrawable(R.drawable.selector_btn_bg));
        ansA.invalidate();
        ansB.invalidate();
        ansC.invalidate();
        ansD.invalidate();
    }

    /**
     * 设置按钮不可被点击
     */
    private void setOptionClickable(boolean clickable) {
        ansA.setClickable(clickable);
        ansB.setClickable(clickable);
        ansC.setClickable(clickable);
        ansD.setClickable(clickable);
    }

    /***
     * 选择题目 答案点击事件
     *
     * @param ch 用户点击的选项 A B C D
     * @return 点击事件
     */
    private View.OnClickListener optionClickListener(final String ch) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index <= mTotal) {
                    //要保存的测试时间
                    mTestRecord.TestTime = deviceInfo.getCurrentTime();
                    userAns[index - 1] = ch;
                    analysisUserAnswer(mTestType, mQuesList.get(index - 1).getAnswer(), userAns[index - 1]);
                    //保存结果到数据库
                    if (index < mTotal) {
                        proIECC();
                    } else {
                        ToastUtil.showToast(mContext, "已经是最后一题了");
                        btn_next.setText("完成");
                    }
                }
            }
        };
    }

    /**
     * 选项被点击 比较答案是否正确
     *
     * @param ch 被点击的选项
     */
    private View.OnClickListener optionClikListener(final View v, final String ch) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (index <= mTotal) {
                    mTestRecord.TestTime = deviceInfo.getCurrentTime(); //要保存的测试时间
                    userAns[index - 1] = ch;

                    if (index == mTotal) {
                        btn_next.setText(getResources().getString(R.string.complete));//完成
                        ToastUtil.showLongToast(mContext, "已经是最后一个题目了");
                    }
                    if (index <= mTotal) {//测试模式下自动出题  练习模式下点击next跳转
                        LogUtils.e("useranswer: " + userAns[index - 1]);
                        if (flag_mode == 1 && index < mTotal) {
                            analysisUserAnswer(mTestType, mQuesList.get(index - 1).getAnswer().trim(), userAns[index - 1]);
                            proIECC();
                        } else if (flag_mode == 2) {//练习题目不自动加载  点击next时候跳转到下一道题目
                            ll_rightanswer.setVisibility(View.VISIBLE);
                            tv_rightanswer.setText(" 答案: " + mQuesList.get(index - 1).getAnswer());
                            markSelected(v);
                        }
                    }
                }
            }
        };
    }

    /**
     * 圆形进度条点击事件
     */
    @NonNull
    private View.OnClickListener roundProgressBarClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rp.setClickable(false);//如果正在测评,不要重复用点击
                btn_next.setClickable(false);//测评中,不可点击进下一题
                if (!NetWorkState.isConnectingToInternet()) {
                    CustomToast.showToast(mContext, R.string.alert_net_content, 1000);
                } else {
                    mTestRecord.TestTime = deviceInfo.getCurrentTime();
                    btn_next.setClickable(false);//语音评测期间如果跳转下一题,导致测试结果记录不准
                    String a[] = mQuesList.get(index - 1).getQuestion().split("-");
                    rp.setBackgroundResource(R.mipmap.speak_ques_stop);
//                    iseManager.startEvaluate(mQuesList.get(index - 1).getAnswer(), index - 1);
                }
            }
        };
    }

    /**
     * next按钮店家事件
     */
    private View.OnClickListener nextButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                iseManager.stopEvaluating();
                if (index <= mTotal) {
                    markUnselected();
                    setOptionClickable(true);
                    mTestRecord.TestTime = deviceInfo.getCurrentTime();//要保存的测试时间
                    if (userAns[index - 1] != null && !userAns[index - 1].equals("")) {
                        analysisUserAnswer(mTestType, mQuesList.get(index - 1).getAnswer().trim(), userAns[index - 1]);
                    } else {
                        undoNum++;
                        testRecordList.add(mTestRecord);
                    }
                    if (index < mTotal) {
                        proIECC();
                    } else {
                        btn_next.setText(getResources().getString(R.string.complete));//完成
                        if (flag_mode == 1) {//测评模式
                            gotoResultActivity();
                        } else if (flag_mode == 2) {//练习状态
//                            goToAbilityMapSubActivity();
                            btn_next.setText("查看详情");
                            handler.sendEmptyMessage(SAVERESULT);//保存分析记录  上传数据到服务器
                            SpeakAbilityTestActivity.this.finish();
                            Intent intent = new Intent(SpeakAbilityTestActivity.this, ShowAnalysisActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        };
    }

    /***
     * 练习模式进入该项图片界面
     */
    private void goToAbilityMapSubActivity() {
        handler.sendEmptyMessage(SAVERESULT);//保存分析记录  上传数据到服务器
        Intent intent = new Intent();
        intent.putExtra("mode", flag_mode);
        intent.putExtra("abilityType", Constant.ABILITY_SPEAK);
        intent.setClass(SpeakAbilityTestActivity.this, AbilityMapSubActivity.class);
        SpeakAbilityTestActivity.this.finish();
        startActivity(intent);
    }

    @Override
    protected void loadData() {

    }

    private Runnable thread = new Runnable() {
        @Override
        public void run() {
            totalTime--;
            handler.sendEmptyMessageDelayed(4, 1000);
        }
    };

    private String mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR;

    /**
     * 出题目
     */
    private void proIECC() {
        ll_rightanswer.setVisibility(View.GONE);
        LogUtils.e("答案:  " + mQuesList.get(index).getAnswer() + "  ::: testtype " + mQuesList.get(index).getTestType());

        //答题记录的
        mTestRecord = new TestRecord();
        deviceInfo = new GetDeviceInfo(mContext);
        mTestRecord.uid = isUserLogin() ? getUid() : "0";
        mTestRecord.appId = Constant.APPID;
        mTestRecord.index = 1;
        mTestRecord.deviceId = deviceInfo.getLocalMACAddress();
        mTestRecord.TestCategory = Constant.ABILITY_SPEAK;
        mTestRecord.mode = flag_mode;
        mTestRecord.BeginTime = deviceInfo.getCurrentTime();
        mTestRecord.Id = mQuesList.get(index).getId() + "";
        mTestRecord.TestNumber = mQuesList.get(index).getTestId();
        mTestRecord.UserAnswer = "";
        mTestRecord.RightAnswer = mQuesList.get(index).getAnswer();
        mTestRecord.Categroy = mQuesList.get(index).getCategory();

        for (int i = 0; i < mTestTypeArr.length; i++) {
            if (mQuesList.get(index).getCategory().equals(mTestTypeArr[i])) {
                mTestType = i;
            }
        }

        mCategory[mTestType]++;
        //TYPE_FAYIN 语音评测
        if (mQuesList.size() > 0 && mQuesList.get(index).getTestType() == Constant.ABILITY_TESTTYPE_VOICE) {
            speakChoices.setVisibility(View.GONE);
            speakVoice.setVisibility(View.VISIBLE);

            questionIntro.setText(mQuesList.get(index).getQuestion());
            questionContent.setText(mQuesList.get(index).getAnswer());

            questionIntro.setPadding(5, 0, 0, 0);
            questionContent.setPadding(5, 0, 0, 0);

            // 单词内的重读  单项选择题
        } else if (mQuesList.size() > 0 && mQuesList.get(index).getTestType() == Constant.ABILITY_TESTTYPE_SINGLE) {
            speakChoices.setVisibility(View.VISIBLE);
            speakVoice.setVisibility(View.GONE);
            //提问
            questionIntro.setText(mQuesList.get(index).getQuestion());
            if (mQuesList.get(index).getAttach() == null || mQuesList.get(index).getAttach().trim().equals("")) {
                questionContent.setText("");
            } else {
                questionContent.setText(FileUtil.getTextFromFile(mLocalAudioPrefix + mQuesList.get(index).getAttach()));
            }

            questionIntro.setPadding(5, 0, 0, 0);
            questionContent.setPadding(5, 0, 0, 0);

            showTextWithColor(textA, mQuesList.get(index).getAnswer1());
            showTextWithColor(textB, mQuesList.get(index).getAnswer2());
            showTextWithColor(textC, mQuesList.get(index).getAnswer3());
            showTextWithColor(textD, mQuesList.get(index).getAnswer4());

        } else if (mQuesList.size() > 0) {
            speakChoices.setVisibility(View.VISIBLE);
            speakVoice.setVisibility(View.GONE);

            questionIntro.setText(mQuesList.get(index).getQuestion());
            questionIntro.setPadding(5, 0, 0, 0);

            questionIntro.setText(mQuesList.get(index).getQuestion());
            if (mQuesList.get(index).getAttach() == null || mQuesList.get(index).getAttach().trim().equals(""))
                questionContent.setText("");
            else
                questionContent.setText(FileUtil.getTextFromFile(mLocalAudioPrefix + mQuesList.get(index).getAttach()));

            textA.setText(mQuesList.get(index).getAnswer1());
            textB.setText(mQuesList.get(index).getAnswer2());
            textC.setText(mQuesList.get(index).getAnswer3());
            textD.setText(mQuesList.get(index).getAnswer4());
        }
        quesIndex.setText(String.valueOf(index + 1) + "/" + mTotal);
        index++;
    }

    private final AbilityResult abilityResult = new AbilityResult();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RoundProgressBar tempBar = null;
            switch (msg.what) {
                case 2:// set the read button progress bar with value of voice volume
                    try {
                        tempBar = rp;
                        int db = msg.arg1;
                        tempBar.setCricleProgressColor(0xfffea523);
                        tempBar.setMax(100);
                        tempBar.setProgress(db);
                    } catch (Exception e) {
                        LogUtils.e("val", "handler.case2");
                    }
                    break;
                case 3: // reset the read button progress bar
                    try {
                        rp.setBackgroundResource(R.mipmap.speak_ques_read);
                        tempBar = rp;
                        tempBar.setCricleProgressColor(0xff87c973);
                        tempBar.setMax(100);
                        tempBar.setProgress(0);
                    } catch (Exception e) {
                        LogUtils.e("val", "handler.case3");
                    }
                    break;
                case 4:
                    pb.incrementProgressBy(-1);
                    String a = String.valueOf(totalTime / 60);
                    String b = String.valueOf(totalTime % 60);
                    timeUsed.setText(a + "m " + b + "s");
                    if (totalTime != 0) {
                        thread.run();
                    } else {
                        //时间结束
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle("提示:");
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
                case 6://语音测评类里面返回的
                    rp.setClickable(true);//已经有测评结果了
                    btn_next.setClickable(true);
                    int score = msg.arg1;
                    int index = msg.arg2;
                    btn_next.setClickable(true);
                    boolean is_rejected = (Boolean) msg.obj;
//                    rs = iseManager.getResultEva();
                    if (is_rejected) {
                        mTestRecord.UserAnswer = "-1";
                        CustomToast.showToast(mContext, "语音异常，请重新录入!", 1200);
                    } else {
                        mTestRecord.AnswerResult = score > 70 ? 100 : 0;
                        mTestRecord.UserAnswer = score > 70 ? mQuesList.get(index).getAnswer() : "-1";
                        testRecordList.add(mTestRecord);//11.2改
                        CustomToast.showToast(mContext, "评测成功", 1200);
                        rightNum[mTestType]++;
                        LogUtils.e("index = " + (index + 1) + "  mTotal=" + mTotal);
                        if (index < mTotal) {
                            proIECC();
                        } else {
                            btn_next.setText("完成");
                            ToastUtil.showToast(mContext, "已经是最后一个题目了");
                        }
                    }
                    break;
                case SAVERESULT:
                    //上传答题记录到大数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadTestRecordToNetLocal(testRecordList, abilityResult, flag_mode);//上传数据到服务器
                        }
                    }).start();
                    break;

                case 1000:
                    rp.setClickable(true);//评测时网络连接中断
                    btn_next.setClickable(true);
                    ToastUtil.showToast(mContext, msg.obj.toString());
                    break;
            }
        }
    };

    /**
     * 转化为存储到数据表的格式
     *
     * @param id 与score 对应的 index
     * @return s
     */
    private String parseSavePattern(int id) {
        LogUtils.e("测试结果:   " + mCategory[id] + "++" + rightNum[id] + "++" + mTestTypeArr[id]);
        return mCategory[id] + "++" + rightNum[id] + "++" + mTestTypeArr[id];
    }

    //用户答题统计  判断题
    private void analysisUserAnswer(int type, String rightAnswer, String userAnswer) {
        //测试记录
        mTestRecord.UserAnswer = userAnswer;
        LogUtils.e("题目的index =" + (index - 1) + "  Right===" + rightAnswer + "  userAnswer==" + userAnswer);
        if (rightAnswer.equals(userAnswer)) {
            LogUtils.e("答对了");
            rightNum[type] += 1;
            LogUtils.e("蒙对的个数 " + rightNum[type]);
            mTestRecord.AnswerResult = 1;
        } else {
            LogUtils.e("答错了");
            mTestRecord.AnswerResult = 0;
        }
        testRecordList.add(mTestRecord);

        mQuesList.get(index - 1).setUserAnswer(mTestRecord.UserAnswer);
        mQuesList.get(index - 1).setResult(mTestRecord.AnswerResult + "");
        DataManager.getInstance().practiceList = mQuesList;
    }

    /**
     * 进入展示结果界面
     */
    private void gotoResultActivity() {
        addAbilityResultsToEntity();//成绩分析记录传递给成绩记录对象;
        handler.sendEmptyMessage(SAVERESULT);//保存分析记录
        Intent intent = new Intent();
        intent.putExtra("testType", Constant.ABILITY_TETYPE_SPEAK);
        intent.putExtra("abilityResults", abilityResult);
        intent.putExtra("testTypeArr", mTestTypeArr);
        LogUtils.e(abilityResult.toString());
        intent.setClass(getApplicationContext(), AbilityTestResultActivity.class);
        this.finish();
        startActivity(intent);
    }

    //将数据赋值给对象
    private void addAbilityResultsToEntity() {
        int totalnum = 0;
        String endTime = deviceInfo.getCurrentTime();
        for (int i = 0; i < rightNum.length; i++) {
            totalnum = totalnum + rightNum[i];
        }
        //保存数据库
        abilityResult.TypeId = Constant.ABILITY_TETYPE_SPEAK;
        switch (mTestTypeArr.length) {
            case 7:
                abilityResult.Score7 = parseSavePattern(TYPE_7);
            case 6:
                abilityResult.Score6 = parseSavePattern(TYPE_6);
            case 5:
                abilityResult.Score5 = parseSavePattern(TYPE_5);
            case 4:
                abilityResult.Score4 = parseSavePattern(TYPE_LUOJI);
            case 3:
                abilityResult.Score3 = parseSavePattern(TYPE_SUCAI);
            case 2:
                abilityResult.Score2 = parseSavePattern(TYPE_BIAODA);
            case 1:
                abilityResult.Score1 = parseSavePattern(TYPE_FAYIN);
                break;
        }

        abilityResult.DoRight = totalnum;
        abilityResult.Total = mTotal;
        abilityResult.UndoNum = undoNum;
        abilityResult.beginTime = mBegintime;
        abilityResult.endTime = endTime;
        abilityResult.uid = getUid();
    }

    @Override
    public void onBackPressed() {
        //showAlertDialog(index, mTotal, "口语", mQuesList, flag_mode);
        showAlertDialogLocal(mTotal, index, "口语", testRecordList, abilityResult, flag_mode);
    }

    /***
     * @return 返回按钮点击事件
     */
    private View.OnClickListener backButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };
    }

    /**
     * 让某几个文字显示颜色
     *
     * @param str 字符串
     */
    private void showTextWithColor(TextView v, String str) {
        if (str.contains("[[") && str.contains("]]")) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xfffea523);//前景色
            String wordsor = str.replace("[[", "").replace("]]", "");
            SpannableStringBuilder words = new SpannableStringBuilder(wordsor);
            words.setSpan(colorSpan, str.indexOf("[["), str.indexOf("]]") - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            v.setText(words);
        } else {
            v.setText(str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(4);//停止计时,防止闪退
    }
}
