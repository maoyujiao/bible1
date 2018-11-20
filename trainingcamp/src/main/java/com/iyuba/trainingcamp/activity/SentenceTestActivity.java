package com.iyuba.trainingcamp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.bean.TestResultBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecordHelper;
import com.iyuba.trainingcamp.manager.IseManager;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.utils.TimeUtils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author yq QQ:1032006226
 */
public class SentenceTestActivity extends BaseActivity implements View.OnClickListener {

    SpannableStringBuilder style ;
    DailyWordDBHelper mHelper;
    @BindView(R2.id.en)
    TextView mEn;
    @BindView(R2.id.cn)
    TextView mCn;
    @BindView(R2.id.next)
    TextView next ;
    @BindView(R2.id.follow)
    ImageView mFollow;
    private AnimationDrawable animationDrawable;
    List<Integer> indexList = new ArrayList<>();

    private Context mContext ;
    private IseManager mManager;
    private List<LearningContent> mLearningContents ;
    private List<AbilityQuestion.TestListBean> list ;
    private int scoreSum ;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    mFollow.setImageResource(R.drawable.trainingcamp_icon_follow0);
                    if (animationDrawable != null && animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    mLearningContents.get(position).score = msg.arg1+"";
                    indexList = (List<Integer>) msg.obj ;


                    mLearningContents.get(position).checkPassed = msg.arg1 > 60;
                    next.setVisibility(View.VISIBLE);
                    if (position == list.size()-1){
                        next.setText("完成");
                    }else {
                        next.setText("下一题");
                    }
                    list.get(position).setResult(String.valueOf(msg.arg1));
                    setColorSpan();

                    break;
                case 1000:
                    animationDrawable.stop();
                    mFollow.setImageResource(R.drawable.trainingcamp_icon_follow0);
                    next.setVisibility(View.VISIBLE);
                    if (position == list.size()-1){
                        next.setText("完成");
                    }else {
                        next.setText("下一题");
                    }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void setColorSpan() {
        if (indexList.size() == 0){
            return;
        }
        String unColoredString = mEn.getText().toString();
        String [] strings = unColoredString.split(" ");
        style = new SpannableStringBuilder(unColoredString);
        for (int i : indexList){
            Log.d("diao",i+"");
            if (i>=strings.length){
                break;
            }
            int beginPosition = unColoredString.indexOf(strings[i]);
            style.setSpan(new ForegroundColorSpan(Color.RED), beginPosition, beginPosition+strings[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        }
        mEn.setText(style);
        StringBuffer buffer  = new StringBuffer("");
        for (int i1 = 0 ;i1< indexList.size();i1++){
            buffer.append(strings[i1]);
            if (i1!=indexList.size()-1){
                buffer.append(",");
            }
        }
        String s = String.valueOf(buffer);
        list.get(position).setUserAnswer(s);
    }

    private int position;
    private TextView testIndex;

    private void bindViews() {
        ButterKnife.bind(this);
        mContext = this;
        testIndex = findViewById(R.id.position);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFollow.setOnClickListener(this);
        mEn.setOnClickListener(this);
        mCn.setOnClickListener(this);
        next.setOnClickListener(this);
        mHelper = new DailyWordDBHelper(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_sentence_test);
        bindViews();
        mManager  = IseManager.getInstance(mContext,mHandler);
        mLearningContents=  (List<LearningContent>)getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);
        list=  (List<AbilityQuestion.TestListBean>)getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);

        refreshUI(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mManager  = IseManager.getInstance(mContext,mHandler);
    }

    @Override
    protected void onPause() {
        mManager.stopEvaluating();
        super.onPause();
    }

    private void refreshUI(int position) {
        if (position == mLearningContents.size() || position>mLearningContents.size()){

            for (LearningContent content:mLearningContents){
                scoreSum += Integer.parseInt(content.score);
            }
            TestResultBean.getBean().sentenceScore = scoreSum/mLearningContents.size();
            if (TestResultBean.getBean().sentenceScore<60){
                showAlert();
                return;
            }
            setSentenceScore(TestResultBean.getBean().sentenceScore+"");
            ACache.get(mContext).put("sentence", (Serializable) mLearningContents);
            ScoreActivity.start(mContext,ACache.get(mContext).getAsString("id"),mLearningContents,list,1,
                    TestResultBean.getBean().sentenceScore+"");

            finish();
            return;
        }
        next.setVisibility(View.INVISIBLE);
        testIndex.setText(position+1+"/"+mLearningContents.size());

        mEn.setText(mLearningContents.get(position).en);
        mEn.setTextColor(getResources().getColor(R.color.trainingcamp_white));
        mCn.setText(mLearningContents.get(position).cn);
        mFollow.setImageResource(R.drawable.trainingcamp_icon_follow0);
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您目前的得分为"+scoreSum/mLearningContents.size()+",需要达到60分才能进入下一关~~").setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).setPositiveButton("重新闯关", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                position = 0;
                refreshUI(position);
                scoreSum = 0 ;
            }
        }).show();
    }

    private void setSentenceScore(String s) {
        GoldDateRecord goldDateRecord = mHelper.selectDataById(GoldApp.getApp(mContext).userId,ACache.get(this).getAsString("id"));
        if (goldDateRecord == null ){
            Log.d("diao", "setSentenceScore: null");
        }else {
            mHelper.updateUpdateSentenceScore(GoldApp.getApp(mContext).userId,s, ACache.get(this).getAsString("id"));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.follow ){
            if (!mManager.isEvaluating()){
                mManager.startEvaluate(mLearningContents.get(position).en,mLearningContents.get(position).getId());
                animationDrawable = (AnimationDrawable) getResources().getDrawable(
                        R.drawable.trainingcamp_speaking_anim);
                mFollow.setImageDrawable(animationDrawable);
                animationDrawable.start();
                next.setVisibility(View.INVISIBLE);
            }else {
                mManager.stopEvaluating();
            }

        } else if (v.getId() == R.id.back){
            finish();
        } else if (v.getId() == R.id.next){
            position ++ ;
            refreshUI(position);
        }
    }

    public  static  void start(Context context , List<LearningContent> mLearningContents , List<AbilityQuestion.TestListBean> list){
        Intent intent = new Intent(context,SentenceTestActivity.class);
        intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mLearningContents);
        intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);

        context.startActivity(intent);
    }
}
