package com.iyuba.trainingcamp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecordHelper;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.DateUtils;
import com.iyuba.trainingcamp.widget.ScoreCircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.activity
 * @class describe
 * @time 2018/8/8 18:27
 * @change
 * @chang time
 * @class describe
 */
public class TestTestTest extends BaseActivity {
    ScoreCircleView mCircleView;
    List<Integer> list ;
    TextView scoreView ;
    private DailyWordDBHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_teteteet);
        mHelper = new DailyWordDBHelper(this);
        mCircleView = findViewById(R.id.circle);
        scoreView = findViewById(R.id.score_skill);
        list = new ArrayList<>();
        GoldDateRecord goldDateRecords = mHelper.selectDataById(GoldApp.getApp(this).userId,
                ACache.get(this).getAsString("id"));
        list.add (Integer.parseInt(goldDateRecords.getWord_score()));
        list.add (Integer.parseInt(goldDateRecords.getSentence_score()));
        list.add (Integer.parseInt(goldDateRecords.getExam_score()));
        setScoreView();

        mCircleView.setScores(list);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setScoreView() {
        int sum = 0 ;
        for (int i = 0 ; i< list.size() ; i++){
            sum += list.get(i);
        }
        scoreView.setText(""+sum/list.size());
    }
}
