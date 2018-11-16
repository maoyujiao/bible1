package com.iyuba.trainingcamp.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.adapter.SentenceAdapter;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.ExamDetail;
import com.iyuba.trainingcamp.bean.LearningContent;

import com.iyuba.trainingcamp.http.VipRequestFactory;
import com.iyuba.trainingcamp.utils.MD5;
import com.iyuba.trainingcamp.utils.ParaConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.activity
 * @class describe
 * @time 2018/7/24 12:18
 * @change
 * @chang time
 * @class describe
 */
public class SentenceResultActivity extends BaseActivity {

    List<LearningContent> list;

    Context context;

    private android.support.v7.widget.RecyclerView mRecyclerView;
    private TextView mStart;

    private void bindViews() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mStart = findViewById(R.id.start);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_sentence_learning);
        context = this;
        bindViews();
        list = (List<LearningContent>) getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);


        RecyclerView.LayoutManager Lmanager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(Lmanager);
        if (list == null) {

        } else {
            mRecyclerView.setAdapter(new SentenceAdapter(context, list));

        }
    }
}
