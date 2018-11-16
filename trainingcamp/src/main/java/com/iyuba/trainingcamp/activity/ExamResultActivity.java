package com.iyuba.trainingcamp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.adapter.ExamQuestionAdapter;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.http.DownloadUtil;
import com.iyuba.trainingcamp.http.HttpUrls;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.ParaConstants;

import java.io.File;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.activity
 * @class describe
 * @time 2018/8/7 18:45
 * @change
 * @chang time
 * @class describe
 */
public class ExamResultActivity extends BaseActivity {
    RecyclerView recyclerView;
    ExamQuestionAdapter mAdapter;
    List<AbilityQuestion.TestListBean> list;
    int index = 0;
    Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);

                    recyclerView.setLayoutManager(manager);
                    mAdapter = new ExamQuestionAdapter(mContext, list);
                    recyclerView.setAdapter(mAdapter);
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_exam_result);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mContext = this;
        recyclerView = findViewById(R.id.recyclerView);
        list = (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);
        for (int i = 0 ; i < list.size() ; i++){
            File file = new File(FilePath.getTxtPath()+ list.get(i).Explains);
            if (!file.exists()){
                downloadAttach();
                break;
            }
            if (i == list.size() - 1){
                mHandler.sendEmptyMessage(100);
                return;
            }
        }
    }

    private void downloadAttach() {

        DownloadUtil.get(mContext).download(HttpUrls.getAttach(this) + list.get(index).Explains,
                FilePath.getTxtPathSuffix(), new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        Log.d("diao", "onDownloadSuccess: " + index);
                        if (index == list.size() - 1) {
                            mHandler.sendEmptyMessage(100);
                            return;
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                index++;
                                downloadAttach();
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed() {

                    }
                });
    }
}
