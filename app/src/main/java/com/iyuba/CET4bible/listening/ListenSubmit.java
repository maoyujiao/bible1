/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.listening;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ListenAnswerAdapter;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionAAnswerOp;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.sqlite.mode.test.CetAnswer;

public class ListenSubmit extends BasisActivity {
    private Context mContext;
    private Button backBtn;
    private ListView answer;
    private boolean finish;
    NewTypeSectionAAnswerOp op ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listen_answer);
        mContext = this;
        op = new NewTypeSectionAAnswerOp(mContext);
        CrashApplication.getInstance().addActivity(this);
        finish = getIntent().getBooleanExtra("finish", false);
        writeAnswerList();
        initWidget();
    }

    private void writeAnswerList() {
        for (int i = 0; i < ListenDataManager.Instance().answerList.size(); i++) {
            CetAnswer anser = ListenDataManager.Instance().answerList.get(i);
            op.saveUserAnswers(ListenDataManager.Instance().textList.get(0).testTime,anser.id,anser.yourAnswer);
        }
    }

    private void initWidget() {
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        answer = findViewById(R.id.list);
        answer.setAdapter(new ListenAnswerAdapter(mContext, finish,
                new ResultIntCallBack() {

                    @Override
                    public void setResult(int result) {
                        Intent intent = new Intent();
                        intent.putExtra("curPos", result);
                        ListenSubmit.this.setResult(1, intent);
                        ListenSubmit.this.finish();
                    }
                }));
        answer.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("curPos", arg2);
                setResult(0, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(100);
        super.onBackPressed();
    }
}
