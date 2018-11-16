package com.iyuba.trainingcamp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.DailyItem;
import com.iyuba.trainingcamp.bean.LessonIdBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.http.VipRequestFactory;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.DateUtils;
import com.iyuba.trainingcamp.utils.LogUtils;
import com.iyuba.trainingcamp.utils.MD5;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.utils.SP;
import com.iyuba.trainingcamp.utils.TimeUtils;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.iyuba.trainingcamp.widget.DailyReviewView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.text.CollationElementIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yq QQ:1032006226
 */
public class GoldActivity extends BaseActivity {

    /**
     * W 表示单词 S表示口语 L表示听力
     */
    /*
     *  sign加密方式: md5(lesson+category+"yyyy-MM-dd" )
     */
    Context context;
    String userId;
    List<AbilityQuestion.TestListBean> list;
    String[] months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "June", "July", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."};
    private TextView mMonth;
    String sign;

    DailyReviewView rv;
    private ImageView mLeft, mRight;
    private TextView mStudyWord, mStudySentence, mStudyTest;
    private ImageView icon_go_sentence, icon_go_exam, icon_go_word;
    private TextView introduce;
    private ImageView back;
    private int step;
    private boolean hasNext; //第二天是否有课程 如果没有课程 则请求网络数据加载
    private List<LessonIdBean.LessonListBean> idsBean;
    String s;
    ACache mCache;
    DailyWordDBHelper mHelper;
    // 每行显示的日期数
    private static final int NUM_DAYS = 8;
    //loading dialog
    private AVLoadingIndicatorView dialog;
    String pointerdate = "";     // 指针显示的日期
    String firstdate = "";       // 每行第一个日期
    List<DailyItem> dailyItemList = new ArrayList<>();
    String today = "";
    private int lessonId = 1; //第几天的数据,需要动态获取
    KProgressHUD progrssDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_gold);
        bindViews();

        mCache = ACache.get(this);
        userId = GoldApp.getApp(this).getUserId();
        today = DateUtils.getToday();
        //判断第二天是否有课程
        GoldDateRecord record = mHelper.selectDataByDate(formatDate(System.currentTimeMillis() + 1000 * 24 * 60 * 60),userId);
        if (record == null) {
            hasNext = false;
        } else {
            hasNext = true;
        }

        //第一次进入或者第二天没有课程
        if (!(Boolean) SP.get(context, "enter_gold", false) || !hasNext) {
            getLessonIdList();
        } else {
            refreshStudyContent(today);
        }
        pointerdate = today;
        firstdate = today;
        //获取日历的日期
        getRecentDate(0);
        pointerdate = today;
        firstdate = today;

        rv.setDailyReviewInterface(new DailyReviewView.DailyReviewInterface() {
            @Override
            public void setSelect(List<DailyItem> list, String s) {
                pointerdate = s;
                refreshStudyContent(pointerdate);
            }
        });
        rv.setDailyList(dailyItemList, today);
        refreshLearnedUI();
        s = System.currentTimeMillis() + "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLearnedUI();
        getRecentDate(0);
        pointerdate = today;
        firstdate = today;
        refreshStudyContent(today);
    }

    private void bindViews() {
        progrssDialog = KProgressHUD.create(GoldActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("请稍等")
                .setDetailsLabel("正在加载数据...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
//        mRegion.setPath()
        context = this;
        back = findViewById(R.id.back);
        mLeft = findViewById(R.id.left);
        rv = findViewById(R.id.daily_review);
        mRight = findViewById(R.id.right);
        mStudyWord = findViewById(R.id.study_word);
        mStudySentence = findViewById(R.id.study_sentence);
        mStudyTest = findViewById(R.id.study_test);
        introduce = findViewById(R.id.text);
        mMonth = findViewById(R.id.month);
        dialog = findViewById(R.id.avi);
        icon_go_sentence = findViewById(R.id.icon_go_sentence);
        icon_go_exam = findViewById(R.id.icon_go_exam);
        icon_go_word = findViewById(R.id.icon_go_word);
        mLeft.setOnClickListener(mOnClickListener);
        mRight.setOnClickListener(mOnClickListener);
        mStudyWord.setOnClickListener(mOnClickListener);
        mStudySentence.setOnClickListener(mOnClickListener);
        mStudyTest.setOnClickListener(mOnClickListener);
        introduce.setOnClickListener(mOnClickListener);
        back.setOnClickListener(mOnClickListener);
        introduce.setText("课程介绍");
        mHelper = new DailyWordDBHelper(this);
        mHelper.getWritableDatabase();
        mHelper.openDatabase();

    }

    private int getTodayGroup(String today) {
        return (Integer.parseInt(today.substring(8)) - 1) / NUM_DAYS;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String cat = bundle.getString("cat");
            sign = MD5.getMD5ofStr(GoldApp.getApp(context).getLessonType() + cat + TimeUtils.getCurTime());
            getTodayData(sign, cat);
            return;
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.study_word) {
                if (judgeDate()) return;
                sign = MD5.getMD5ofStr(GoldApp.getApp(context).getLessonType() + "W" + TimeUtils.getCurTime());
                getTodayData(sign, "W");
                progrssDialog.show();
            } else if (v.getId() == R.id.study_sentence) {
                if (judgeDate()) return;
                if (step <= 1) {
                    ToastUtil.showToast(context, "请先完成上一个学习任务");
                    return;
                }
                sign = MD5.getMD5ofStr(GoldApp.getApp(context).getLessonType() + "S" + TimeUtils.getCurTime());
                getTodayData(sign, "S");
                progrssDialog.show();
            } else if (v.getId() == R.id.study_test) {
                if (judgeDate()) return;
                if (step <= 2) {
                    ToastUtil.showToast(context, "请先完成上一个学习任务");
                    return;
                }
                sign = MD5.getMD5ofStr(GoldApp.getApp(context).getLessonType() + "L" + TimeUtils.getCurTime());
                getTodayData(sign, "L");
                progrssDialog.show();
            } else if (v.getId() == R.id.left) {
                getRecentDate(-1);
            } else if (v.getId() == R.id.right) {
                getRecentDate(1);
            } else if (v.getId() == R.id.text) {
                Intent intent = new Intent(context, BuyIndicatorActivity.class);
                intent.putExtra("flag", false);
                startActivity(intent);
            } else if (v.getId() == R.id.back) {
                if (getIntent().getBooleanExtra("in_trial_mode", false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("您现在正在试用黄金会员,退出后将需要开通会员才能进入,确定退出试用吗?")
                            .setPositiveButton("退出试用", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton("继续试用", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    finish();
                }
            }
        }
    };

    private boolean judgeDate() {
        if (pointerdate.compareTo(today) > 0) {
            ToastUtil.showToast(context, "只能查看今日之前的课程");
            return true;
        }
        if (mHelper.selectDataByDate(pointerdate,userId) == null) {
            LogUtils.d("diao", pointerdate);
            ToastUtil.showToast(context, "您在此日期没有课程");
            return true;
        }
        return false;
    }

    private void getLessonIdList() {

        progrssDialog.show();
        LogUtils.d("getLessonIdList: ");
        String sign = MD5.getMD5ofStr(GoldApp.getApp(context).getLessonType() + userId + DateUtils.getToday());
        retrofit2.Call<LessonIdBean> call = VipRequestFactory.getTestQuestionApi().getLessonIdApi("20005", GoldApp.getApp(context).getLessonType(), sign, "json", userId);

        s = today;
        call.enqueue(new Callback<LessonIdBean>() {
            @Override
            public void onResponse(retrofit2.Call<LessonIdBean> call, Response<LessonIdBean> res) {
                progrssDialog.dismiss();
                if (res.body() != null) {
                    if (res.body().getResult().equals("0")) {
                        ToastUtil.showToast(context, "失败");
                    } else {
                        idsBean = res.body().getLessonList();
                        Collections.sort(idsBean, new Comparator<LessonIdBean.LessonListBean>() {
                            @Override
                            public int compare(LessonIdBean.LessonListBean o1, LessonIdBean.LessonListBean o2) {
                                if (Integer.parseInt(o1.getLessonid()) > Integer.parseInt(o2.getLessonid())) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        });
                        for (int i = 0; i < idsBean.size(); i += 3) {
                            if (mHelper.writeDataToSchedule(userId,idsBean.get(i).getLessonid(), formatDate(Long.parseLong(s)))) {
                                s = Long.parseLong(s) + 1000 * 60 * 60 * 24 + "";
                            }
                        }
                        SP.put(context, "enter_gold", true);
                    }
                }
                refreshStudyContent(today);
            }

            @Override
            public void onFailure(retrofit2.Call<LessonIdBean> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progrssDialog.dismiss();
                        ToastUtil.showToast(context, getString(R.string.trainingcamp_data_request_fault));
                    }
                });
            }
        });
    }

    private void refreshLearnedUI() {
        GoldDateRecord records = mHelper.selectDataByDate(today,userId);
        if (records == null) {
            return;
        }
        setBitmapClickable(Integer.parseInt(records.getStep()));
        step = Integer.parseInt(records.getStep());
    }

    private void setBitmapClickable(int i) {
        if (i == 0) {
            icon_go_word.setImageResource(R.drawable.trainingcamp_icon_go_unlock);
            icon_go_sentence.setImageResource(R.drawable.trainingcamp_icon_go_unlock);
            icon_go_exam.setImageResource(R.drawable.trainingcamp_icon_go_unlock);
        } else if (i == 1) {
            icon_go_word.setImageResource(R.drawable.trainingcamp_icon_go);
            icon_go_sentence.setImageResource(R.drawable.trainingcamp_icon_go_unlock);
            icon_go_exam.setImageResource(R.drawable.trainingcamp_icon_go_unlock);
        } else if (i == 2) {
            icon_go_word.setImageResource(R.drawable.trainingcamp_icon_go);
            icon_go_sentence.setImageResource(R.drawable.trainingcamp_icon_go);
            icon_go_exam.setImageResource(R.drawable.trainingcamp_icon_go_unlock);
        } else if (i == 3) {
            icon_go_word.setImageResource(R.drawable.trainingcamp_icon_go);
            icon_go_sentence.setImageResource(R.drawable.trainingcamp_icon_go);
            icon_go_exam.setImageResource(R.drawable.trainingcamp_icon_go);
        }
    }

    private void refreshStudyContent(String day) {
        Log.d("diao", "refreshStudyContent: " + day);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
//            format.parse(day).getTime() > format.parse(today).getTime()
            if (false){
                ToastUtil.showToast(context, "只允许查看当前日期之前的课程呢~~~~");
                setBitmapClickable(0);
            } else {
                GoldDateRecord records = mHelper.selectDataByDate(day,userId);
                if (records != null) {

                    lessonId = Integer.parseInt(records.getLessonid());
                    mCache.put("id", records.getLessonid());
                    if (records.getStep() == "1") {
                        step = 1;
                    } else {
                        step = Integer.parseInt(records.getStep());
                    }
                    setBitmapClickable(step);
                } else {
                    setBitmapClickable(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取过去第几天的日期(-1 操作) 或者 未来 第几天的日期( +1 操作)
     */
    private void getRecentDate(int tempGroup) {
        int maxDate = 0;
        String dayString = "";
        String monthString = "";
        int month = Integer.parseInt(firstdate.substring(5, 7));
        int dayIndex = getTodayGroup(firstdate);
        int year = Integer.parseInt(firstdate.substring(0, 4));
        int passeditems = dayIndex + tempGroup;
        dayIndex = (passeditems) % (30 / NUM_DAYS + 1);
        if (dayIndex != -1) {
            dayString = 1 + NUM_DAYS * dayIndex + "";
            if (dayString.length() < 2) {
                dayString = "0" + dayString;
            }
        } else {
            dayString = "25";
        }

        if (dayIndex == -1) {
            month--;
            if (month == 0) {
                month = 1;
            }
        } else if (dayIndex == 0 & tempGroup == 1) {
            month++;
            if (month == 13) {
                month = 1;
                year++;
            }
        }
        if ((month + "").length() < 2) {
            monthString = "0" + month;
        } else {
            monthString = "" + month;
        }
        mMonth.setText(year + "\n" + months[month - 1]);
        firstdate = year + "-" + monthString + "-" + dayString;
        maxDate = TimeUtils.getMonthMax(firstdate);
        int dateIndex = (Integer.parseInt(firstdate.substring(8)) - 1) / NUM_DAYS;
        setDateList(dateIndex, maxDate);
    }

    private void setDateList(int n, int dateMax) {
        dailyItemList.clear();
        for (int day = n * NUM_DAYS + 1; day < (n + 1) * NUM_DAYS + 1; day++) {
            DailyItem item = new DailyItem();
            if (day < 10) {
                item.date = firstdate.substring(0, 8) + "0" + day;
            } else {
                item.date = firstdate.substring(0, 8) + day;
            }
            GoldDateRecord record = mHelper.selectDataByDate(item.date,userId);
            if (record == null) {
                item.studySign = false;
            } else if (record.getStep().equals("3")) {
                item.studySign = true;
            } else {
                item.studySign = false;
            }

            if (Integer.parseInt(item.date.substring(8)) <= dateMax) {
                dailyItemList.add(item);
            } else {
                item.isShow = false;
                dailyItemList.add(item);
            }
        }
        rv.setDailyList(dailyItemList, firstdate);
    }


    public void getTodayData(String sign, final String cat) {
        retrofit2.Call<AbilityQuestion> call = VipRequestFactory.getTestQuestionApi().testQuestionApi("20000", GoldApp.getApp(context).getLessonType(), cat, sign, "json", 3, userId, lessonId);
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
                                ToastUtil.showToast(context, getString(R.string.trainingcamp_no_data_changed));
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
                        ToastUtil.showToast(context, getString(R.string.trainingcamp_data_request_fault));
                        progrssDialog.dismiss();
                    }
                });
            }
        });
    }

    private void startIntent(Class target) {
        Intent intent = new Intent(context, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);
        intent.putExtra("lessonid", lessonId + "");
        startActivity(intent);
    }

    private String formatDate(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(millis);
    }

    public static Intent buildIntent(Context context, String Category, String lessonId) {
        Intent intent = new Intent(context, GoldActivity.class);
        intent.putExtra("cat", Category);
        intent.putExtra("lessonid", lessonId);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
}