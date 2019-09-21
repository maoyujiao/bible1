package com.iyuba.core.me.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iyuba.biblelib.R;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.WdDetailAdapter;
import com.iyuba.core.me.protocol.WordDetailRequest;
import com.iyuba.core.me.protocol.WordDetailResponse;
import com.iyuba.core.me.sqlite.mode.WordDetail;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IErrorReceiver;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.ErrorResponse;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.List;


public class WordDetailActivity extends Activity {
    WdDetailAdapter WdDetailAdapter;
    private Context mContext;
    private ListView WdDetailListView;
    private List<WordDetail> mList = new ArrayList<WordDetail>();
    private CustomDialog waitDialog;
    private String mode = "1";
    private Button backBtn;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    WdDetailAdapter.addList((ArrayList<WordDetail>) mList);
                    WdDetailAdapter.notifyDataSetChanged();
                    waitDialog.dismiss();
                    if (mList.size() == 0) {
                        Toast.makeText(mContext, "没有数据记录哦~~", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 10:
                    waitDialog.dismiss();
                    break;
                case 11:
                    waitDialog.dismiss();
                    CustomToast.showToast(mContext, "加载失败");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intel_word_detail);
        mContext = this;
        waitDialog = WaittingDialog.showDialog(WordDetailActivity.this);
        Intent intent = getIntent();
        mode = intent.getStringExtra("testMode");
        WdDetailListView = findViewById(R.id.detail_list);
        WdDetailAdapter = new WdDetailAdapter(mContext);
        WdDetailListView.setAdapter(WdDetailAdapter);
        backBtn = findViewById(R.id.button_back);
        waitDialog.show();
        new UpdateWdDetailThread().start();

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    private class UpdateWdDetailThread extends Thread {

        @Override
        public void run() {

            String uid = AccountManager.Instace(mContext).getId();

            ClientSession.Instance().asynGetResponse(
                    new WordDetailRequest(uid, mode), new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {

                            WordDetailResponse tr = (WordDetailResponse) response;

                            if (tr != null && tr.result.equals("1")) {

                                mList.clear();
                                mList.addAll(tr.mList);
                                mHandler.sendEmptyMessage(1);

                            } else {
                                mHandler.sendEmptyMessage(10);
                            }
                        }
                    }, new IErrorReceiver() {
                        @Override
                        public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                            mHandler.sendEmptyMessage(10);
                        }
                    });

        }
    }
}
