package com.iyuba.trainingcamp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

    DailyWordDBHelper mHelper ;
    WordHistoryAdapter adapter ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_word_history);
        mHelper = new DailyWordDBHelper(this);
        mContext = this ;
        recyclerView = findViewById(R.id.recyclerView);
        lessonId = getIntent().getStringExtra("lessonid");
        Log.d("diao", "onCreate: "+lessonId);
        mRecords =  mHelper.selectDataById(GoldApp.getApp(mContext).userId,lessonId);

        if (mRecords !=null ){
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new WordHistoryAdapter(mContext,mHelper.getWordHistory(lessonId));
            recyclerView.setAdapter(adapter);
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
