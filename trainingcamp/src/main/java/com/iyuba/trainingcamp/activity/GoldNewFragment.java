package com.iyuba.trainingcamp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.adapter.LessonAdapter;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.BBCInfoBean;
import com.iyuba.trainingcamp.bean.LessonIdBean;
import com.iyuba.trainingcamp.bean.SimpleResultBean;
import com.iyuba.trainingcamp.bean.StudyProgress;
import com.iyuba.trainingcamp.bean.VoaInfoBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.event.FinishGetIDEvent;
import com.iyuba.trainingcamp.event.LessonSelectEvent;
import com.iyuba.trainingcamp.event.StarMicroEvent;
import com.iyuba.trainingcamp.http.VipRequestFactory;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.DateUtils;
import com.iyuba.trainingcamp.utils.LogUtils;
import com.iyuba.trainingcamp.utils.MD5;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.utils.SP;
import com.iyuba.trainingcamp.utils.TimeUtils;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.iyuba.trainingcamp.widget.SubClassWindow;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yq QQ:1032006226
 */
public class GoldNewFragment extends Fragment {

    private static final int DAY_MILLIS= 1000*60*60*24;
    private int index;
    ViewPager mViewPager;
    LessonAdapter mAdapter;
    List<LessonIdBean.LessonListBean> mBeans = new ArrayList<>();
    static Context mContext;
    private TextView lesson;
    private ImageView showMore;
    SubClassWindow window = new SubClassWindow();
    private XLHRatingBar mRatingBarWord,mRatingBarSentence,mRatingBarExam;
    private Button microClass, introduce;
    KProgressHUD progrssDialog;
    ImageView warmup, word, sentence, exam;
    ImageView warmup_go, word_go, sentence_go, exam_go;
    LinearLayout ll_warmup, ll_words, ll_sentence, ll_exam;
    DailyWordDBHelper mHelper;
    LinearLayout ll_default ;
    TextView txt_score;
    String s;
    private int lessonId;
    List<AbilityQuestion.TestListBean> list;

    String sign;
    boolean isCet =false;
    private String userid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trainingcamp_gold_new, container, false);
        mHelper = new DailyWordDBHelper(mContext);
        if (GoldApp.getApp(mContext).getLessonType().contains("cet")){
            isCet = true;
        }
        userid = GoldApp.getApp(mContext).userId;
        s = String.valueOf(System.currentTimeMillis());
        progrssDialog = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("请稍等")
                .setDetailsLabel("正在加载数据...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        lesson = view.findViewById(R.id.lesson);
        view.findViewById(R.id.back).setVisibility(View.GONE);
        mRatingBarWord = view. findViewById(R.id.ratingBarword);
        mRatingBarSentence = view. findViewById(R.id.ratingBarSentence);
        mRatingBarExam = view. findViewById(R.id.ratingBarExam);
        ll_exam = view.findViewById(R.id.exam);
        ll_sentence = view.findViewById(R.id.sentence);
        ll_words = view.findViewById(R.id.word);
        ll_warmup = view.findViewById(R.id.warm_up);
        showMore = view.findViewById(R.id.show_more);
        microClass = view.findViewById(R.id.microclass);
        introduce = view.findViewById(R.id.introduce);
        warmup = view.findViewById(R.id.img_warmup);
        word = view.findViewById(R.id.img_words);
        sentence = view.findViewById(R.id.img_sentence);
        exam = view.findViewById(R.id.img_exam);
        warmup_go = view.findViewById(R.id.img_warmup_go);
//        word_go = view.findViewById(R.id.img_words_go);
//        sentence_go = view.findViewById(R.id.img_sentence_go);
//        exam_go = view.findViewById(R.id.img_exam_go);
        mViewPager = view.findViewById(R.id.viewpager);
        txt_score = view.findViewById(R.id.score);
        ll_default = view.findViewById(R.id.ll_default);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "font/DINMedium_0.ttf");
        txt_score.setTypeface(tf);
        lesson.setTypeface(tf);
        initClickEvent();
        getLessonIdList();
        setScore(0);
        ToastUtil.showLongToast(mContext,"点击右上角的图标可以显示往期课程哦~~");
        return view;
    }

    private void initClickEvent() {
        if (isCet){
            ll_warmup.setVisibility(View.GONE);
        }
        microClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StarMicroEvent(GoldApp.getApp(mContext).productId));
            }
        });
        introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BuyIndicatorActivity.class);
                intent.putExtra("flag", false);
                startActivity(intent);
            }
        });
        ll_warmup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = MD5.getMD5ofStr(GoldApp.getApp(mContext).getLessonType() + "W" + TimeUtils.getCurTime());
                getTodayData(sign, "W");
            }
        });
        ll_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = MD5.getMD5ofStr(GoldApp.getApp(mContext).getLessonType() + "W" + TimeUtils.getCurTime());
                getTodayData(sign, "W");
            }
        });
        ll_sentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign = MD5.getMD5ofStr(GoldApp.getApp(mContext).getLessonType() + "S" + TimeUtils.getCurTime());

                getTodayData(sign, "S");
            }
        });
        ll_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sign = MD5.getMD5ofStr(GoldApp.getApp(mContext).getLessonType() + "L" + TimeUtils.getCurTime());
                getTodayData(sign, "L");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPagerContent(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.createSubClassWindow(mContext, mBeans);
                window.show(v);
            }
        });

    }

    private void initPagerItem() {
        int i1 = 0;
        GoldDateRecord record = mHelper.selectDataByDate(TimeUtils.getCurTime(),GoldApp.getApp(mContext).userId);
        for (int i = 0 ;i < mBeans.size() ; i++){
            Log.d("diao",mBeans.get(i).getLessonid());
            if (record!= null){
                if (mBeans.get(i).getLessonid().equals(record.getLessonid())){
                    i1 = i /3;
                }
            }else {
                i1 = 0 ;
            }
        }
        mViewPager.setCurrentItem(i1);
        setPagerContent(i1);
    }

    private void initLesson() {
        lesson.setText(mViewPager.getCurrentItem() + 1 + "/" + (mBeans.size() / 3));
        SpannableStringBuilder style = new SpannableStringBuilder(lesson.getText().toString());
        String s = String.valueOf(mViewPager.getCurrentItem() + 1);
        style.setSpan(new RelativeSizeSpan(1.5f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lesson.setText(style);
    }

    private void setScore(int score) {
        txt_score.setText("Score" + score);
        SpannableStringBuilder style = new SpannableStringBuilder(txt_score.getText().toString());
        style.setSpan(new RelativeSizeSpan(1.5f), 5, txt_score.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.trainingcamp_blue_score)), 5, txt_score.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt_score.setText(style);
    }


    public static GoldNewFragment newInstance(Context context) {
        GoldNewFragment fragment = new GoldNewFragment();
        mContext = context;
        return fragment;
    }

    private void getLessonIdList() {

        progrssDialog.show();
        LogUtils.d("getLessonIdList: ");
        String sign = MD5.getMD5ofStr(GoldApp.getApp(mContext).getLessonType() + GoldApp.getApp(mContext).getUserId() + DateUtils.getToday());
        retrofit2.Call<LessonIdBean> call = VipRequestFactory.getTestQuestionApi().getLessonIdApi("20005", GoldApp.getApp(mContext).getLessonType(), sign, "json", GoldApp.getApp(mContext).getUserId());
        call.enqueue(new Callback<LessonIdBean>() {
            @Override
            public void onResponse(Call<LessonIdBean> call, Response<LessonIdBean> res) {
                progrssDialog.dismiss();
                if (res.body() != null) {
                    if (res.body().getResult().equals("0")) {
                        ToastUtil.showToast(mContext, "失败");
                    } else {
                        mBeans = res.body().getLessonList();
                        SortBeans();
                        requestProgress();
//                        EventBus.getDefault().post(new FinishGetIDEvent());
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LessonIdBean> call, Throwable t) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progrssDialog.dismiss();
                        ToastUtil.showToast(mContext, getString(R.string.trainingcamp_data_request_fault));
                    }
                });
            }
        });
    }

    private void WriteBeans() {

        for (int i = 0; i < mBeans.size(); i += 3) {
            if (mHelper.writeDataToSchedule(GoldApp.getApp(mContext).getUserId(), mBeans.get(i).getLessonid(), TimeUtils.getFormateDate(Long.parseLong(s)))) {
                s = Long.parseLong(s) + 1000 * 60 * 60 * 24 + "";
            }
        }
        SP.put(mContext, "enter_gold", true);
    }



    private void requestVoaInfo() {
        String s = "";
        StringBuffer buffer = new StringBuffer(s);
        for (int i = 0; i < mBeans.size(); i += 3) {

            buffer.append(mBeans.get(i).getLessonid());
            if (i + 3 < mBeans.size()) {
                buffer.append(",");
            }
        }
        s = String.valueOf(buffer);

        if (!GoldApp.getApp(mContext).LessonType.contains("bbc")){
            retrofit2.Call<VoaInfoBean> call =
                    VipRequestFactory.getVoaInfoApi().getVoaInfo(String.valueOf(s));
            call.enqueue(new Callback<VoaInfoBean>() {
                @Override
                public void onResponse(Call<VoaInfoBean> call, Response<VoaInfoBean> response) {
                    if (response.body() == null){
                        ToastUtil.showToast(mContext,"数据加载失败");
                        return;
                    }
                    Log.d("diao", "onResponse: " + response.body().toString());
                    VoaInfoBean bean = response.body();
                    mHelper.saveVoaInfo(bean);
                }

                @Override
                public void onFailure(Call<VoaInfoBean> call, Throwable t) {
                }
            });
        }else {
            retrofit2.Call<BBCInfoBean> call =
                    VipRequestFactory.getVoaInfoApi().getBBCInfo(String.valueOf(s));
            call.enqueue(new Callback<BBCInfoBean>() {
                @Override
                public void onResponse(Call<BBCInfoBean> call, Response<BBCInfoBean> response) {
                    if (null == response.body()){
                        ToastUtil.showToast(mContext,"数据加载失败");
                        return;
                    }
                    Log.d("diao", "onResponse: " + response.body().toString());
                    BBCInfoBean bean = response.body();
                    mHelper.saveVoaInfo(bean);
                }

                @Override
                public void onFailure(Call<BBCInfoBean> call, Throwable t) {
                }
            });
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        if (mBeans.size()>0){
            setPagerContent(index);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setPagerContent(int position){
        index = position;
        initLesson();
        lessonId = Integer.parseInt(mBeans.get(position * 3).getLessonid());
        ACache.get(mContext).put("id", String.valueOf(lessonId));

        GoldDateRecord records = mHelper.selectDataById(GoldApp.getApp(mContext).userId, String.valueOf(lessonId));
        if (records != null) {
            if (records.getStep() == "1") {
                if (isCet){
//                    setClickStatus(2);
                    setClickStatus(4);
                }else {
//                    setClickStatus(1);
                    setClickStatus(4);
                }
            } else {
                int step = Integer.parseInt(records.getStep());
//                setClickStatus(step+1);
                setClickStatus(4);
            }
        } else {
            setClickStatus(4);
        }
        int scoresum = Integer.parseInt(records.getWord_score())+
                Integer.parseInt(records.getSentence_score())+
                Integer.parseInt(records.getExam_score());

        setRatingBarContent(records.getWord_score(),records.getSentence_score(),records.getExam_score());
        setScore(scoresum/3);
//        正式发版前添加
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            if (format.parse(TimeUtils.getCurTime()).getTime()<format.parse(records.getDate()).getTime()){
//                setClickStatus(1);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    private void setRatingBarContent(String wordScore,String sentenceScore , String ExamScore) {
        int word = Integer.parseInt(wordScore);
        int sentence = Integer.parseInt(sentenceScore);
        int exam = Integer.parseInt(ExamScore);
        mRatingBarWord.setCountSelected(word/20);
        mRatingBarSentence.setCountSelected(sentence/20);
        mRatingBarExam.setCountSelected(exam/20);
    }

    public void setClickStatus(int i) {
        setClickable(i);
        switch (i) {
            case 1:
                warmup.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_warmup));
                word.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_words_notyet));
                sentence.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_sentence_notyet));
                exam.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_exam_notyet));

                warmup_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                word_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_lock));
//                sentence_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_lock));
//                exam_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_lock));

                break;
            case 2:
                warmup.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_warmup));
                word.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_words));
                sentence.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_sentence_notyet));
                exam.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_exam_notyet));

                warmup_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                word_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                sentence_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_lock));
//                exam_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_lock));
                break;
            case 3:
                warmup.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_warmup));
                word.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_words));
                sentence.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_sentence));
                exam.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_exam_notyet));

                warmup_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                word_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                sentence_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                exam_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_lock));
                break;
            case 4:
                warmup.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_warmup));
                word.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_words));
                sentence.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_sentence));
                exam.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_exam));

                warmup_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                word_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                sentence_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
//                exam_go.setImageDrawable(getResources().getDrawable(R.drawable.trainingcamp_icon_go_new));
                break;
            default:
                break;

        }
    }

    private void setClickable(int i) {
        ll_warmup.setClickable((i - 1) >= 0 ? true : false);
        ll_words.setClickable((i - 2) >= 0 ? true : false);
        ll_sentence.setClickable((i - 3) >= 0 ? true : false);
        ll_exam.setClickable((i - 4) >= 0 ? true : false);
    }

    @Subscribe
    public void onEvent(LessonSelectEvent event) {
        mViewPager.setCurrentItem(event.getLessonid());
        window.dismiss();
    }

    @Subscribe
    public void onEvent(FinishGetIDEvent event) {
//        requestProgress();
        ToastUtil.showLongToast(mContext, "数据加载完成");
        ll_default.setVisibility(View.GONE);
        mAdapter = new LessonAdapter(mContext, mBeans);
        mViewPager.setAdapter(mAdapter);
        initPagerItem();
        requestVoaInfo();
    }

    private void SortBeans(){
        Collections.sort(mBeans, new Comparator<LessonIdBean.LessonListBean>() {
            @Override
            public int compare(LessonIdBean.LessonListBean o1, LessonIdBean.LessonListBean o2) {
                if (Integer.parseInt(o1.getLessonid()) > Integer.parseInt(o2.getLessonid())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    private void requestProgress() {
        sign = MD5.getMD5ofStr("80005class"+userid);
        Call<StudyProgress> progressCall = VipRequestFactory.getTestQuestionApi().getProgressApi("80005",GoldApp.getApp(mContext).productId,
                sign,"json",userid);
        progressCall.enqueue(new Callback<StudyProgress>() {
            @Override
            public void onResponse(Call<StudyProgress> call, Response<StudyProgress> response) {
                if (response.body().getResult() == 0){
                    WriteBeans();
                }else{
                    StudyProgress progress = response.body();
                    Collections.sort(progress.getData(), new Comparator<StudyProgress.DataBean>() {
                        @Override
                        public int compare(StudyProgress.DataBean o1, StudyProgress.DataBean o2) {
                            if (o1.getTitleid() > o2.getTitleid()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    writeDataToDB(response.body());
                    WriteBeans();
                }
//                EventBus.getDefault().post(new );
                EventBus.getDefault().post(new FinishGetIDEvent());
            }

            @Override
            public void onFailure(Call<StudyProgress> call, Throwable t) {

            }
        });

    }

    private void writeDataToDB(StudyProgress progress) {
        for(StudyProgress.DataBean bean : progress.getData())
        mHelper.writeDownloadDataToSchedule(userid,bean.getTitleid()+"",TimeUtils.getFormateDate((long) (bean.getPlanday()*DAY_MILLIS)));
    }

    //将获取的lessonId 上传,避免由于卸载或者更换手机导致的数据清除或异常 , 注意已经上传的数据不要再重传
    private void uploadStudyResult(List<LessonIdBean.LessonListBean> beans) {
        for(int i = 0 ; i<beans.size() ; i++){
            Call<SimpleResultBean> call = VipRequestFactory.getTestQuestionApi().
                    uploadStudyProgress("80004", GoldApp.getApp(mContext).productId + "",
                            MD5.getMD5ofStr("80004class" + GoldApp.getApp(mContext).getUserId() + ""),
                            "json", GoldApp.getApp(mContext).getUserId(), beans.get(i).getLessonid() + "",
                            "0", System.currentTimeMillis()/DAY_MILLIS+i+"",   "0", String.valueOf(System.currentTimeMillis()/DAY_MILLIS));

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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void getTodayData(String sign, final String cat) {
        progrssDialog.show();

        retrofit2.Call<AbilityQuestion> call = VipRequestFactory.getTestQuestionApi().testQuestionApi("20000", GoldApp.getApp(mContext).getLessonType(), cat, sign, "json", 3,
                GoldApp.getApp(mContext).getUserId(), lessonId);
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
                        ((Activity) mContext).runOnUiThread(new Runnable() {
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
                ((Activity) mContext).runOnUiThread(new Runnable() {
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
