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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.adapter.WordHistoryAdapter;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class WordHistoryActivity extends BaseActivity {

    @BindView(R2.id.score)
    TextView mScore;
    @BindView(R2.id.ll)
    LinearLayout mLinearLayout;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    private String lessonId;
    private GoldDateRecord mRecords;
    Context mContext;

    DailyWordDBHelper mHelper;
    WordHistoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_word_history);
        ButterKnife.bind(this);
        mContext = this;
        mHelper = new DailyWordDBHelper(this);
        lessonId = getIntent().getStringExtra("lessonid");
        Log.d("diao", "onCreate: " + lessonId);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/DINMedium_0.ttf");
        mScore.setTypeface(tf);
        mRecords = mHelper.selectDataById(GoldApp.getApp(mContext).userId, lessonId);

        if (mRecords != null) {
            mLinearLayout.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            adapter = new WordHistoryAdapter(mContext, mHelper.getWordHistory(lessonId));
            mScore.setText(mHelper.selectDataById(GoldApp.getApp(mContext).userId, lessonId).getWord_score());
            if (TextUtils.isEmpty(mHelper.selectDataById(GoldApp.getApp(mContext).userId, lessonId).getWord_score())) {
                mLinearLayout.setVisibility(View.GONE);
            }
            recyclerView.setAdapter(adapter);
        } else {
            mLinearLayout.setVisibility(View.GONE);

        }

    }

    @OnClick(R2.id.back)
    public void onMBackClicked() {
        finish();
    }


}
