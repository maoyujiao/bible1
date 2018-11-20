package com.iyuba.trainingcamp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.adapter.WordListAdapter;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.utils.ParaConstants;

import java.io.Serializable;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.activity
 * @class describe
 * @time 2018/7/28 13:04
 * @change
 * @chang time
 * @class describe
 */
public class LearnResultActivity extends BaseActivity  {


    Context context ;
    private TextView mTip_cn;
    private TextView mTip_en;
    private android.support.v7.widget.RecyclerView mLearn_result_list;
    private TextView mGo_test;
    List<LearningContent> mLearningContents;
    List<AbilityQuestion.TestListBean> lists ;
    private WordListAdapter adapter;

    private void bindViews() {

        mTip_cn = findViewById(R.id.tip_cn);
        mTip_en = findViewById(R.id.tip_en);
        mLearn_result_list = findViewById(R.id.learn_result_list);
        mGo_test = findViewById(R.id.go_test);
        mGo_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WordTestActivity.class);
                intent.putExtra(ParaConstants.QUESTION_LIST_LABEL,(Serializable) lists);
                intent.putExtra(ParaConstants.LEARNINGS_LABEL,(Serializable) mLearningContents);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(1);
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_learn_result_activity);
        context = this;
        bindViews();
        mLearningContents = (List<LearningContent>) getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);  // 单词的学习数据
        lists =  (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);  //单词测试的数据
        initList();
        if (!getIntent().getExtras().getBoolean("show",true)){
            mGo_test.setVisibility(View.INVISIBLE);
            adapter.setShowIcon(false);
        }

    }

    private void initList() {
        adapter = new WordListAdapter(context, mLearningContents);
        mLearn_result_list.setLayoutManager(new LinearLayoutManager(context));
        mLearn_result_list.setAdapter(adapter);
    }

    private void showConfirmDialog(int i) {
        if (i == 1) {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_sure_to_quit_study))
                    .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), null)
                    .setPositiveButton(getResources().getString(R.string.trainingcamp_exit_study), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

        } else {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_is_study_sentence_continue))
                    .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.trainingcamp_exit_study, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void onBackPressed() {
        showConfirmDialog(1);
    }

}
