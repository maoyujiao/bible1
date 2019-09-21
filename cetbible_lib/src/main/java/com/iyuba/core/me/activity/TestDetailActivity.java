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
import com.iyuba.core.me.adapter.TestDetailAdapter;
import com.iyuba.core.me.protocol.TestDetailRequest;
import com.iyuba.core.me.protocol.TestDetailResponse;
import com.iyuba.core.me.sqlite.mode.TestResultDetail;
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

public class TestDetailActivity extends Activity {

    private Context mContext;
    private ListView TestDetailListView;
    private TestDetailAdapter testDetailAdapter;
    private List<TestResultDetail> mList = new ArrayList<TestResultDetail>();
    private CustomDialog waitDialog;
    private String mode = "1";
    private Button backBtn;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    testDetailAdapter.addList((ArrayList<TestResultDetail>) mList);
                    testDetailAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.test_detail_record);
        mContext = this;
        Intent intent = getIntent();
        mode = intent.getStringExtra("testMode");
        waitDialog = WaittingDialog.showDialog(TestDetailActivity.this);
        TestDetailListView = findViewById(R.id.detail_list);
        testDetailAdapter = new TestDetailAdapter(mContext);
        TestDetailListView.setAdapter(testDetailAdapter);
        backBtn = findViewById(R.id.button_back);
        waitDialog.show();
        new UpdateTestDetailThread().start();

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    private class UpdateTestDetailThread extends Thread {

        @Override
        public void run() {

            String uid = AccountManager.Instace(mContext).getId();

            ClientSession.Instance().asynGetResponse(
                    new TestDetailRequest(uid, mode), new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {

                            TestDetailResponse tr = (TestDetailResponse) response;

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
