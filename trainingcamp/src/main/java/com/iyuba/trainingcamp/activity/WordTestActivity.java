package com.iyuba.trainingcamp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.bean.TestResultBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecordHelper;
import com.iyuba.trainingcamp.http.HttpUrls;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.DateUtils;
import com.iyuba.trainingcamp.utils.LogUtils;
import com.iyuba.trainingcamp.utils.ParaConstants;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yq QQ:1032006226
 */
public class WordTestActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @BindView(R2.id.word)
    TextView mWord;
    @BindView(R2.id.word_pronounce)
    TextView mWordPronounce;
    @BindView(R2.id.pro)
    ImageView mPro;

    @BindView(R2.id.et)
    EditText et;
    private DailyWordDBHelper mHelper;

    private TextView mAnswerA_root, mAnswerB_root, mAnswerC_root, mAnswerD_root;
    private ImageView imgA, imgB, imgC, imgD;

    private LinearLayout imgLL1, imgLL2;
    @BindView(R2.id.position)
    TextView testIndex;

    private String lessonid;
    private String answer = "";
    private int currentWord = 0;
    private MediaPlayer player;
    private List<AbilityQuestion.TestListBean> list;
    private AbilityQuestion.TestListBean bean;
    private List<LearningContent> mLearningContents;
    private int right = 0;
    private String picPrefix;

    Button next_ques;
    private String userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_word_test);

        ButterKnife.bind(this);
        lessonid = getIntent().getStringExtra("lessonid");
        userid = GoldApp.getApp(this).userId;
        player = new MediaPlayer();
        mHelper = new DailyWordDBHelper(this);
        picPrefix = "http://static2.iyuba.com/" + GoldApp.getApp(this).getLessonType() + "/images/";
        getIntents();
        bindViews();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
        refreshUI(currentWord);
    }

    @Override
    protected void onPause() {
        Log.d("WordTestActivity", "onPause: ");
        saveToRecord(lessonid);
        super.onPause();
    }

    private void saveToRecord(String lessonid) {


        new DailyWordDBHelper(this).saveRecords(lessonid, mLearningContents);


    }

    private void refreshUI(int currentWord) {
        bean = list.get(currentWord);
        mWord.setText(bean.getQuestion());
        testIndex.setText(currentWord + 1 + "/" + mLearningContents.size());
        for (LearningContent content:mLearningContents){

            if (content.en.trim().substring(1).equals(bean.getQuestion())){
                mWordPronounce.setText(content.pro);

            }
        }

        LogUtils.d("diao", "refreshUI: " + mLearningContents.get(currentWord).toString());

        LogUtils.d("diao", "refreshUI: " + bean.toString());
        if (bean.getTags().contains("听音")) {
            et.setVisibility(View.GONE);
            mWord.setVisibility(View.GONE);
            mWordPronounce.setVisibility(View.GONE);
            mPro.setScaleType(ImageView.ScaleType.CENTER_CROP);
            getPronounce();
            next_ques.setVisibility(View.GONE);
        } else {
            et.setVisibility(View.GONE);
            mWord.setVisibility(View.VISIBLE);
            mWordPronounce.setVisibility(View.VISIBLE);
            mPro.setScaleType(ImageView.ScaleType.CENTER);
            next_ques.setVisibility(View.GONE);

        }
        if (!bean.getTags().contains("图片")) {
            et.setVisibility(View.GONE);
            imgLL1.setVisibility(View.GONE);
            imgLL2.setVisibility(View.GONE);
            mAnswerA_root.setVisibility(View.VISIBLE);
            mAnswerB_root.setVisibility(View.VISIBLE);
            mAnswerC_root.setVisibility(View.VISIBLE);
            mAnswerD_root.setVisibility(View.VISIBLE);
            TypedArray a = obtainStyledAttributes(R.styleable.TrainingTheme);
            @SuppressLint("ResourceAsColor")
            int d = a.getColor(0, R.color.trainingcamp_theme_color); //icoid 指的是所需要的drwable在Mytheme的实际index        // 回收

            mAnswerD_root.setTextColor(d);
            mAnswerC_root.setTextColor(d);
            mAnswerB_root.setTextColor(d);
            mAnswerA_root.setTextColor(d);
            a.recycle();

            mAnswerA_root.setText("A. " + bean.getAnswer1());
            mAnswerB_root.setText("B. " + bean.getAnswer2());
            mAnswerC_root.setText("C. " + bean.getAnswer3());
            mAnswerD_root.setText("D. " + bean.getAnswer4());
            mAnswerA_root.setBackgroundResource(R.drawable.trainingcamp_answers_bg_normal);
            mAnswerB_root.setBackgroundResource(R.drawable.trainingcamp_answers_bg_normal);
            mAnswerC_root.setBackgroundResource(R.drawable.trainingcamp_answers_bg_normal);
            mAnswerD_root.setBackgroundResource(R.drawable.trainingcamp_answers_bg_normal);
            setClickable(true);
            next_ques.setVisibility(View.GONE);

        } else {
            et.setVisibility(View.GONE);
            mAnswerA_root.setVisibility(View.GONE);
            mAnswerB_root.setVisibility(View.GONE);
            mAnswerC_root.setVisibility(View.GONE);
            mAnswerD_root.setVisibility(View.GONE);
            imgLL1.setVisibility(View.VISIBLE);
            imgLL2.setVisibility(View.VISIBLE);
            Glide.with(this).load(picPrefix + bean.getAnswer1()).placeholder(R.drawable.trainingcamp_oval_bg).into(imgA);
            Glide.with(this).load(picPrefix + bean.getAnswer2()).placeholder(R.drawable.trainingcamp_oval_bg).into(imgB);
            Glide.with(this).load(picPrefix + bean.getAnswer3()).placeholder(R.drawable.trainingcamp_oval_bg).into(imgC);
            Glide.with(this).load(picPrefix + bean.getAnswer4()).placeholder(R.drawable.trainingcamp_oval_bg).into(imgD);
            setClickable(true);
            next_ques.setVisibility(View.GONE);
            //ToDo set Drawables

        }
        if (bean.getTestType() == 8) {
            mAnswerA_root.setVisibility(View.GONE);
            mAnswerB_root.setVisibility(View.GONE);
            mAnswerC_root.setVisibility(View.GONE);
            mAnswerD_root.setVisibility(View.GONE);
            imgLL1.setVisibility(View.GONE);
            imgLL2.setVisibility(View.GONE);
            et.setText("");
            et.setVisibility(View.VISIBLE);
            et.addTextChangedListener(this);
            next_ques.setVisibility(View.VISIBLE);
            mWord.setVisibility(View.GONE);
            mWordPronounce.setVisibility(View.GONE);
            mPro.setScaleType(ImageView.ScaleType.CENTER_CROP);
            getPronounce();
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setClickable(Boolean clickable) {
        mAnswerD_root.setClickable(clickable);
        mAnswerC_root.setClickable(clickable);
        mAnswerB_root.setClickable(clickable);
        mAnswerA_root.setClickable(clickable);
        imgA.setClickable(clickable);
        imgB.setClickable(clickable);
        imgC.setClickable(clickable);
        imgD.setClickable(clickable);
    }


    private void bindViews() {
        next_ques = findViewById(R.id.next_button);
        mAnswerA_root = findViewById(R.id.answerA_root);
        mAnswerB_root = findViewById(R.id.answerB_root);
        mAnswerC_root = findViewById(R.id.answerC_root);
        mAnswerD_root = findViewById(R.id.answerD_root);
        imgA = findViewById(R.id.image1);
        imgB = findViewById(R.id.image2);
        imgC = findViewById(R.id.image3);
        imgD = findViewById(R.id.image4);
        imgLL1 = findViewById(R.id.ll_pic1);
        imgLL2 = findViewById(R.id.ll_pic2);
        mAnswerA_root.setOnClickListener(this);
        mAnswerB_root.setOnClickListener(this);
        mAnswerC_root.setOnClickListener(this);
        mAnswerD_root.setOnClickListener(this);
        imgA.setOnClickListener(this);
        imgB.setOnClickListener(this);
        imgC.setOnClickListener(this);
        imgD.setOnClickListener(this);
        next_ques.setOnClickListener(this);
        mPro.setOnClickListener(this);
    }

    public void getPronounce() {
        if (player.isPlaying()) {
            return;
        } else {
            player.reset();
        }
        String url = HttpUrls.GET_WORD_PRO + list.get(currentWord).getSounds();
        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.answerA_root || i == R.id.image1) {
            answer = "A";
            if (v instanceof TextView) {
                compareRight((TextView) v);
            } else {
                compareRight((ImageView) v);
            }
        } else if (i == R.id.answerB_root || i == R.id.image2) {
            answer = "B";
            if (v instanceof TextView) {
                compareRight((TextView) v);
            } else {
                compareRight((ImageView) v);
            }
        } else if (i == R.id.answerC_root || i == R.id.image3) {
            answer = "C";
            if (v instanceof TextView) {
                compareRight((TextView) v);
            } else {
                compareRight((ImageView) v);
            }
        } else if (i == R.id.answerD_root || i == R.id.image4) {
            answer = "D";
            if (v instanceof TextView) {
                compareRight((TextView) v);
            } else {
                compareRight((ImageView) v);
            }
        } else if (i == R.id.pro) {
            getPronounce();
        } else if (i == R.id.next_button) {
            if (et.getText().toString().equals(bean.getQuestion())) {
                setReult(true);
            } else {
               setReult(false);
            }
            currentWord++;
            if (currentWord == list.size()) {
                startScore();
                return;
            }
            refreshUI(currentWord);
        }
    }

    private void setReult(Boolean isPassed) {
        if (isPassed){
            mLearningContents.get(currentWord).checkPassed = true;
            right++ ;
            list.get(currentWord).setResult("1");
        }else {
            mLearningContents.get(currentWord).checkPassed = false;
            list.get(currentWord).setResult("0");
        }

    }

    private void compareRight(TextView view) {
        setClickable(false);
        if (answer.equals(getRight())) {
            setReult(true);
        } else {
            setReult(false);
        }
        bean.setUserAnswer(answer);

        view.setBackgroundResource(R.drawable.trainingcamp_answer_bg_pressed);
        view.setTextColor(Color.WHITE);
        currentWord++;
        if (currentWord == list.size()) {
            TestResultBean.getBean().wordScore = right * 100 / list.size();
            if (right*100/list.size()>=60){
                startScore();
            }else {
                showAlert();
            }
            return;
        }
        refreshNewWords();
    }

    private void compareRight(ImageView view) {
        setClickable(false);
        if (answer.equals(getRight())) {
            setReult(true);
        } else {
            setReult(false);
        }
        list.get(currentWord).setUserAnswer(answer);
        currentWord++;
        if (currentWord == list.size()) {
            TestResultBean.getBean().wordScore = right * 100 / list.size();
            if (right*100/list.size()>=60){
                startScore();
            }else {
                showAlert();
            }
            return;
        }
        refreshNewWords();
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            refreshUI(currentWord);
        }
    };

    private void refreshNewWords() {
        mWord.postDelayed(mRunnable, 500);
    }

    private void showConfirmDialog() {
        AlertDialog builder = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_sure_to_quit_study))
                .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), null)
                .setPositiveButton(getResources().getString(R.string.trainingcamp_exit_study), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    private void startScore() {
        ScoreActivity.start(this, lessonid, mLearningContents,list,0,
                TestResultBean.getBean().wordScore + "");
        setWordScore(TestResultBean.getBean().wordScore + "");
    }

    private void setWordScore(String s) {
        GoldDateRecord records = mHelper.selectDataById(userid,ACache.get(this).getAsString("id"));
        if (records != null) {
            mHelper.updateUpdateWordScore(userid,s, lessonid);
        }
    }

    private String getRight() {
        return list.get(currentWord).getAnswer();
    }

    @Override
    public void onBackPressed() {
        showConfirmDialog();
    }

    public void getIntents() {
        list = (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);
        mLearningContents = (List<LearningContent>) getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            next_ques.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > bean.getQuestion().length()) {
            Toast.makeText(this, "这个单词只有" + bean.getQuestion().length() + "个字母哟",
                    Toast.LENGTH_SHORT).show();
            s.delete(et.getSelectionStart() - 1, et.getSelectionEnd());
            int tempSelection = et.getSelectionEnd();
            et.setText(s);
            et.setSelection(tempSelection);
        }
    }

    public static void start(Context mContext, List<LearningContent> mLearningContents,
                             List<AbilityQuestion.TestListBean> list, String id) {
        Intent intent = new Intent(mContext, WordTestActivity.class);
        intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mLearningContents);
        intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);
        intent.putExtra("lessonid", id);
        mContext.startActivity(intent);
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog =builder.setMessage("您目前的得分为"+ TestResultBean.getBean().wordScore+",需要达到60分才能进入下一关~~").setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).setPositiveButton("重新闯关", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentWord = 0;
                refreshUI(currentWord);
                right = 0;

            }
        }).show();
        dialog.setCanceledOnTouchOutside(false);
    }
}
