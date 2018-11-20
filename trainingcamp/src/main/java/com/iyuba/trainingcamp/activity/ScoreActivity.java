package com.iyuba.trainingcamp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.Setting;
import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.adapter.ExamQuestionAdapter;
import com.iyuba.trainingcamp.adapter.SentencePlayAdapter;
import com.iyuba.trainingcamp.adapter.WordListAdapter;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.bean.RecordWithType;
import com.iyuba.trainingcamp.bean.SimpleResultBean;
import com.iyuba.trainingcamp.bean.TestResultBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.http.VipRequestFactory;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.GetDeviceInfo;
import com.iyuba.trainingcamp.utils.MD5;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.utils.SP;
import com.iyuba.trainingcamp.utils.TestRecord;
import com.iyuba.trainingcamp.utils.TimeUtils;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.iyuba.trainingcamp.utils.UploadUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yq QQ:1032006226
 */
public class ScoreActivity extends BaseActivity {


    private List<TestRecord> mTestRecords = new ArrayList<>();


    @BindView(R2.id.todo)
    TextView mTodo;
    @BindView(R2.id.recyclerView)
    android.support.v7.widget.RecyclerView mRecyclerView;
    @BindView(R2.id.next)
    TextView mNext;
    @BindView(R2.id.repeat)
    TextView mRepeat;
    @BindView(R2.id.score)
    TextView mScore;
    @BindView(R2.id.share)
    ImageView share;

    private ExecutorService mExecutorService;
    int testType;
    private List<AbilityQuestion.TestListBean> list;
    private List<LearningContent> mLearningContents;
    private List<AbilityQuestion.TestListBean> wrongList = new ArrayList<>();
    private List<LearningContent> wrongContents = new ArrayList<>();

    String score;

    public static final int WORD = 0;
    public static final int SENTENCE = 1;
    public static final int EXAM = 2;
    String lessonId;
    private Context mContext;
    private List<RecordWithType> mRecordWithTypes = new ArrayList<>();
    private DailyWordDBHelper mHelper;
    private KProgressHUD progrssDialog;
    private String sign;

    private void bindViews() {

        ButterKnife.bind(this);
        share.setVisibility(View.VISIBLE);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/DINMedium_0.ttf");
        mScore.setTypeface(tf);
        mHelper = new DailyWordDBHelper(this);
        lessonId = ACache.get(this).getAsString("id");
        progrssDialog = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("请稍等")
                .setDetailsLabel("正在加载数据...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            UploadUtils.uploadTestRecordToNet(mContext, mTestRecords, mRecordWithTypes, GoldApp.getApp(mContext).userId, 1, "测评", 1);
        }
    };

    @OnClick(R2.id.detail)
    public void showDetail(){
        if (testType == WORD) {
            Intent intent = new Intent(this, LearnResultActivity.class);
            intent.putExtra("show", false);
            intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mLearningContents);
            startActivity(intent);
        } else if (testType == SENTENCE) {
            Intent intent = new Intent(this, SentenceResultActivity.class);
            intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mLearningContents);
            startActivity(intent);
        } else if (testType == EXAM) {
            Intent intent = new Intent(this, ExamResultActivity.class);
            intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);
            startActivity(intent);
        }
    }

    @OnClick(R2.id.share)
    public void showShare(){
        showShareInterface();
    }

    @OnClick(R2.id.back)
    public void back(){
        showShareInterface();
    }

    @OnClick(R2.id.repeat)
    public void repeat(){
        if (testType == 0) {
            WordTestActivity.start(mContext, mLearningContents, list, lessonId + "");
            finish();
        } else if (testType == 1) {
            SentenceTestActivity.start(mContext, mLearningContents, list);
            finish();
        } else if (testType == 2) {
            Intent intent = new Intent(this, ExamActivity.class);
            intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R2.id.next)
    public void next(){
        if (testType == WORD) {
            sign = MD5.getMD5ofStr(GoldApp.getApp(mContext).getLessonType() + "S" + TimeUtils.getCurTime());
            getTodayData(sign, "S");
        } else if (testType == SENTENCE) {
            sign = MD5.getMD5ofStr(GoldApp.getApp(mContext).getLessonType() + "S" + TimeUtils.getCurTime());
            getTodayData(sign, "L");
        } else if (testType == EXAM) {
            Intent intent = new Intent(this, TestTestTest.class);
            List<Integer> list = new ArrayList<>();
            list.add(TestResultBean.getBean().wordScore);
            list.add(TestResultBean.getBean().sentenceScore);
            list.add(TestResultBean.getBean().examScore);
            intent.putExtra(ParaConstants.STUDY_TYPE_LABEL, 2);
            startActivity(intent);
        }
        finish();
    }

    public static void start(Context context, String lessonid, List<LearningContent> mLearningContents, List<AbilityQuestion.TestListBean> list, int type, String score) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mLearningContents);
        intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);
        intent.putExtra(ParaConstants.STUDY_TYPE_LABEL, type);
        intent.putExtra("lessonid", lessonid);
        intent.putExtra("score", score);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_score_activity);
        mExecutorService = Executors.newSingleThreadExecutor();
        mContext = this;
        lessonId = getIntent().getStringExtra("lessonid");
        testType = getIntent().getIntExtra(ParaConstants.STUDY_TYPE_LABEL, 0);
        score = getIntent().getStringExtra("score");

        String level = testType + 1 + "";
//        GoldShareActivity.start(mContext, level, score);
        if (testType == WORD) {
            refreshTodayRecord(1);

            list = (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);
            mLearningContents = (List<LearningContent>) getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);
            getToLearnContents(mLearningContents);
            mScore.setText((100 - wrongContents.size() * 100 / mLearningContents.size()) + "分");
            buildTestRecords(WORD);
            getRecordWithTypeList("W");

        } else if (testType == SENTENCE) {
            mScore.setText(getIntent().getStringExtra("score") + "分");
            refreshTodayRecord(2);
            mLearningContents = (List<LearningContent>) getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);
            list = (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);
            getToLearnContents(mLearningContents);
            buildTestRecords(SENTENCE);
            getRecordWithTypeList("S");
//            buildRecordsWithTypes("口语评测", getIntent().getStringExtra("score"));
        } else {
            list = (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);
            getToLearn(list);
            mScore.setText(getIntent().getStringExtra("score") + "分");
            refreshTodayRecord(3);
            buildTestRecords(EXAM);
            getRecordWithTypeList("L");
//            buildRecordsWithTypes("听力真题", (100 - wrongList.size() * 100 / list.size()) + "");
        }
        mExecutorService.execute(mRunnable);
        uploadStudyResult();
        setUI(testType);
    }

    private void refreshTodayRecord(int type) {
        Log.d("diao", "refreshTodayRecord: " + ACache.get(this).getAsString("id"));
        GoldDateRecord goldDateRecords = mHelper.selectDataById(GoldApp.getApp(mContext).userId, ACache.get(this).getAsString("id"));
        // 刷新今日的做题记录
        if (goldDateRecords != null) {
            if (goldDateRecords.getStep().equals(String.valueOf(type))) {
                mHelper.updateStudyProcess(
                        GoldApp.getApp(mContext).userId, String.valueOf(type + 1), ACache.get(this).getAsString("id"));
            }
        }
    }

    private void getRecordWithTypeList(String s) {
        List<String> hashSet = new ArrayList<>();
        for (AbilityQuestion.TestListBean bean : list) {
            if (hashSet.contains(bean.getTags())) {
                Log.d("diao","contains");

                for (RecordWithType record : mRecordWithTypes) {
                    if (record.getCategory().equals(bean.getTags() + "")) {
                        record.setTestCount(Integer.parseInt(record.getTestCount()) + 1 + "");
                        if (bean.getResult().equals("1")) {
                            record.setScore(Integer.parseInt(record.getScore()) + 1 + "");
                            int s2 = Integer.parseInt(record.getScore());
                            int count = Integer.parseInt(record.getTestCount());
                            record.setScore(String.valueOf(s2*100/count));
                        }
                    }
                }
            } else {
                Log.d("diao","!!!contains");

                hashSet.add(bean.getTags());
                // 第一次的话score传第一次的判断结果
                RecordWithType recordWithType = new RecordWithType(s, bean.getResult(), bean.getTags(), "1");
                mRecordWithTypes.add(recordWithType);
            }
        }
    }

    private void uploadStudyResult() {
        Call<SimpleResultBean> call = VipRequestFactory.getTestQuestionApi().
                uploadStudyProgress("80004", GoldApp.getApp(mContext).productId + "",
                        MD5.getMD5ofStr("80004class" + GoldApp.getApp(mContext).getUserId() + ""),
                        "json", GoldApp.getApp(mContext).getUserId(), lessonId + "",
                        mScore.getText().toString(), generateDayes(), 2 + "", String.valueOf(System.currentTimeMillis()/1000/60/60/24));

        call.enqueue(new Callback<SimpleResultBean>() {
            @Override
            public void onResponse(Call<SimpleResultBean> call, Response<SimpleResultBean> response) {
                if (response.body().result.equals("1")) {
                    ToastUtil.showToast(mContext, "成功了");
                }
            }

            @Override
            public void onFailure(Call<SimpleResultBean> call, Throwable t) {

            }
        });
    }


    // 获取1970-1-1到今天的日期数字
    private String generateDayes() {
        String s = "";
        GoldDateRecord record = mHelper.selectDataById(GoldApp.getApp(mContext).userId,lessonId);
        s = TimeUtils.formatDateToMills(record.getDate());
        long days = Long.parseLong(s) / 1000 / 60 / 60 / 24;
        return days + "";
    }

    private void buildTestRecords(int mode) {
        String cat = "";
        if (mode == SENTENCE) {
            cat = "S";
        } else if (mode == WORD) {
            cat = "W";
        } else {
            cat = "L";
        }
        for (int i = 0; i < list.size(); i++) {
            TestRecord record = new TestRecord();
            if (mode != SENTENCE) {
                if (list.get(i).getResult().equals("1")) {
                    record.AnswerResult = 1;
                } else {
                    record.AnswerResult = 0;
                }
            }else {
                record.AnswerResult = Integer.parseInt(list.get(i).getResult());
            }
            record.BeginTime = getCurrentTime();
            record.Categroy = list.get(i).getCategory();
            record.Id = list.get(i).getLessonId()+"";
            record.RightAnswer = list.get(i).getAnswer();
            record.TestId = list.get(i).getTestId();
            record.TestMode = cat;
            record.TestTime = getCurrentTime();
            record.UserAnswer = list.get(i).getUserAnswer();
            record.appId = GoldApp.getApp(mContext).getLessonType();
            record.Id = list.get(i).getId() + "";
            record.TestNumber = i;
            record.TestCategory = cat;
            record.deviceId = new GetDeviceInfo(this).getLocalMACAddress();
            record.IsUpload = false;
            record.index = i;
            record.mode = 1;
            record.uid = (String) SP.get(this, "uid", "");
            mTestRecords.add(record);
        }

    }

    private List<AbilityQuestion.TestListBean> getToLearn(List<AbilityQuestion.TestListBean> list) {
        for (AbilityQuestion.TestListBean bean : list) {
            if (bean.getResult().equals("0")) {
                wrongList.add(bean);
            }
        }
        return wrongList;
    }

    private List<LearningContent> getToLearnContents(List<LearningContent> list) {
        for (LearningContent bean : list) {
            if (bean.checkPassed == false) {
                wrongContents.add(bean);
            }
        }
        return wrongContents;
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
        String strCurrTime = formatter.format(curDate);
        return strCurrTime;
    }

    private void setUI(int type) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        switch (type) {
            case WORD:
                WordListAdapter adapter = new WordListAdapter(this, wrongContents);
                adapter.setShowIcon(false);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setLayoutManager(manager);
                mTodo.setText("要学习的单词");
                break;
            case SENTENCE:
                mRecyclerView.setAdapter(new SentencePlayAdapter(this, wrongContents));
                mRecyclerView.setLayoutManager(manager);
                mTodo.setText("要练习的句子");
                break;
            case EXAM:
                mRecyclerView.setAdapter(new ExamQuestionAdapter(this, wrongList));
                mRecyclerView.setLayoutManager(manager);
                mNext.setVisibility(View.GONE);
                mRepeat.setVisibility(View.GONE);
                if (!isSlideToBottom(mRecyclerView)) {
                    mNext.setVisibility(View.GONE);
                    mRepeat.setVisibility(View.GONE);
                } else if (wrongList.size() == 0 || wrongList.size() == 1) {
                    mNext.setVisibility(View.VISIBLE);
                    mRepeat.setVisibility(View.VISIBLE);
                }
                mTodo.setText("要学习的真题");
                mNext.setText("完成学习");

                mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (isSlideToBottom(recyclerView)) {
                            mNext.setVisibility(View.VISIBLE);
                            mRepeat.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            default:
                break;
        }

    }


    private void showShareInterface() {
        Intent intent = new Intent(mContext, GoldShareActivity.class);
        intent.putExtra("score", mScore.getText().toString().replace("分", ""));
        if (testType == EXAM) {
            intent.putExtra("level", "三");
        } else if (testType == SENTENCE) {
            intent.putExtra("level", "二");
        } else if (testType == WORD) {
            intent.putExtra("level", "一");
        }
        startActivity(intent);
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {

        if (recyclerView == null) return false;

        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange();
    }


    public void getTodayData(String sign, final String cat) {
        retrofit2.Call<AbilityQuestion> call = VipRequestFactory.getTestQuestionApi().testQuestionApi("20000", GoldApp.getApp(mContext).getLessonType(),
                cat, sign, "json", 3, GoldApp.getApp(mContext).getUserId(), Integer.parseInt(ACache.get(mContext).getAsString("id")));
        call.enqueue(new Callback<AbilityQuestion>() {
            @Override
            public void onResponse(retrofit2.Call<AbilityQuestion> call, Response<AbilityQuestion> res) {
                if (res.body() != null) {
                    if (res.body() != null && res.body().getTestList() != null && res.body().getTestList().size() > 0) {
                        Logger.json(res.toString());
                        list = res.body().getTestList();
                        progrssDialog.dismiss();
                        if (cat.equals("S")) {
                            startIntent(SentencListActivity.class);
                        } else if (cat.equals("L")) {
                            startIntent(ExamActivity.class);
                        } else {
                            startIntent(WordListActvity.class);
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(mContext, getString(R.string.trainingcamp_no_data_changed));
                            }
                        });
                        progrssDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AbilityQuestion> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(mContext, getString(R.string.trainingcamp_data_request_fault));
                        progrssDialog.dismiss();
                    }
                });
            }
        });
    }

    private void startIntent(Class target) {
        Intent intent = new Intent(mContext, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);
        intent.putExtra("lessonid", lessonId + "");
        startActivity(intent);
    }

}
