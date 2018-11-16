package com.iyuba.trainingcamp.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.adapter.WordHistoryAdapter;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecordHelper;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.activity
 * @class describe
 * @time 2018/10/11 16:40
 * @change
 * @chang time
 * @class describe
 */
public class WordHistoryActivity extends BaseActivity{

    RecyclerView recyclerView ;
    private  String  lessonId ;
    private GoldDateRecord mRecords ;
    Context mContext ;

    private TextView mScore;
    private LinearLayout mLinearLayout;
    DailyWordDBHelper mHelper ;
    WordHistoryAdapter adapter ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_word_history);
        mContext = this ;
        mHelper = new DailyWordDBHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        mLinearLayout = findViewById(R.id.ll);
        mScore = findViewById(R.id.score);
        lessonId = getIntent().getStringExtra("lessonid");
        Log.d("diao", "onCreate: "+lessonId);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/DINMedium_0.ttf");
        mScore.setTypeface(tf);
        mRecords =  mHelper.selectDataById(GoldApp.getApp(mContext).userId,lessonId);

        if (mRecords !=null ){
            mLinearLayout.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new WordHistoryAdapter(mContext,mHelper.getWordHistory(lessonId));
            mScore.setText(mHelper.selectDataById(GoldApp.getApp(mContext).userId,lessonId).getWord_score());
            if(TextUtils.isEmpty(mHelper.selectDataById(GoldApp.getApp(mContext).userId,lessonId).getWord_score())){
                mLinearLayout.setVisibility(View.GONE);
            }
            recyclerView.setAdapter(adapter);
        }else {
            mLinearLayout.setVisibility(View.GONE);

        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
