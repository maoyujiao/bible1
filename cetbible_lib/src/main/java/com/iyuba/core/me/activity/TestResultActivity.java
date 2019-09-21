package com.iyuba.core.me.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.protocol.TestResultRequest;
import com.iyuba.core.me.protocol.TestResultResponse;
import com.iyuba.core.me.protocol.UserRankRequest;
import com.iyuba.core.me.protocol.UserRankResponse;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IErrorReceiver;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.ErrorResponse;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;


public class TestResultActivity extends Activity {
    private Context mContext;
    private TextView tv, lsTotQues, lsAvgScore, skTotQues, skAvgScore,
            rdTotQues, rdAvgScore, wrTotQues, wrAvgScore, othrTotQues,
            othrAvgScore;
    private String testSum_0, testSum_1, testSum_2, testSum_3, testSum_4,
            score_0, score_1, score_2, score_3, score_4;
    private String totalTest = "", positionByTest = "", totalRate = "",
            positionByRate = "";
    private Button othrDetail, listenDetail, speakDetail, readDetail, wrDetail,
            backBtn;
    private CustomDialog waitDialog;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // waitDialog.show();
                    if (totalTest == "" || positionByTest == "" || totalRate == ""
                            || positionByRate == "") {
                        handler.sendEmptyMessageDelayed(1, 500);
                    } else {
                        othrTotQues.setText(testSum_0);
                        othrAvgScore.setText(score_0);
                        lsTotQues.setText(testSum_1);
                        lsAvgScore.setText(score_1);
                        skTotQues.setText(testSum_2);
                        skAvgScore.setText(score_2);
                        rdTotQues.setText(testSum_3);
                        rdAvgScore.setText(score_3);
                        wrTotQues.setText(testSum_4);
                        wrAvgScore.setText(score_4);

                        SpannableStringBuilder style1, style2, style3, style4, style5, style6;
                        String r3 = "已做题" + totalTest, r4 = "道，按做题量排名："
                                + positionByTest, r5 = "\n做题正确率：" + totalRate, r6 = "，按正确率全站排名："
                                + positionByRate;
                        style3 = new SpannableStringBuilder(r3);
                        style4 = new SpannableStringBuilder(r4);
                        style5 = new SpannableStringBuilder(r5);
                        style6 = new SpannableStringBuilder(r6);

                        style3.setSpan(new ForegroundColorSpan(Color.YELLOW),
                                r3.indexOf(totalTest), r3.indexOf(totalTest)
                                        + totalTest.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        style4.setSpan(
                                new ForegroundColorSpan(Color.YELLOW),
                                r4.indexOf(positionByTest),
                                r4.indexOf(positionByTest)
                                        + positionByTest.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        style5.setSpan(new ForegroundColorSpan(Color.YELLOW),
                                r5.indexOf(totalRate), r5.indexOf(totalRate)
                                        + totalRate.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        style6.setSpan(
                                new ForegroundColorSpan(Color.YELLOW),
                                r6.indexOf(positionByRate),
                                r6.indexOf(positionByRate)
                                        + positionByRate.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        style3 = style3
                                .append(style4.append(style5.append(style6)));
                        tv.setText(style3);

                        waitDialog.dismiss();
                    }
                    break;
                case 10:
                    waitDialog.dismiss();
                    CustomToast.showToast(mContext, "加载失败");
                    break;
                case 11:
                    waitDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intel_test_result);
        mContext = this;
        waitDialog = WaittingDialog.showDialog(TestResultActivity.this);

        tv = findViewById(R.id.intel_conclusion);
        lsTotQues = findViewById(R.id.ls_ques_num);
        lsAvgScore = findViewById(R.id.ls_avg_score);
        skTotQues = findViewById(R.id.sk_ques_num);
        skAvgScore = findViewById(R.id.sk_avg_score);
        rdTotQues = findViewById(R.id.rd_ques_num);
        rdAvgScore = findViewById(R.id.rd_avg_score);
        wrTotQues = findViewById(R.id.wr_ques_num);
        wrAvgScore = findViewById(R.id.wr_avg_score);
        othrTotQues = findViewById(R.id.othr_ques_num);
        othrAvgScore = findViewById(R.id.othr_avg_score);

        othrDetail = findViewById(R.id.othrd);
        speakDetail = findViewById(R.id.sd);
        listenDetail = findViewById(R.id.ld);
        readDetail = findViewById(R.id.rd);
        wrDetail = findViewById(R.id.wrd);
        backBtn = findViewById(R.id.button_back);

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        othrDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "0");
                intent.setClass(mContext, TestDetailActivity.class);
                startActivity(intent);

            }
        });

        listenDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "1");
                intent.setClass(mContext, TestDetailActivity.class);
                startActivity(intent);
            }
        });

        speakDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "2");
                intent.setClass(mContext, TestDetailActivity.class);
                startActivity(intent);
            }
        });

        readDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "3");
                intent.setClass(mContext, TestDetailActivity.class);
                startActivity(intent);
            }
        });

        wrDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "4");
                intent.setClass(mContext, TestDetailActivity.class);
                startActivity(intent);
            }
        });

        waitDialog.show();
        new DataThread().start();
    }

    private class DataThread extends Thread {

        @Override
        public void run() {
            // waitDialog.show();
            String uid = AccountManager.Instace(mContext).userId;

            ClientSession.Instance().asynGetResponse(new UserRankRequest(uid),
                    new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {
                            UserRankResponse tr = (UserRankResponse) response;

                            if (tr != null && tr.result.equals("1")) {
                                totalTest = tr.totalTest;
                                positionByTest = tr.positionByTest;
                                totalRate = tr.totalRate;
                                positionByRate = tr.positionByRate;
                            } else {
                                handler.sendEmptyMessage(11);
                            }
                            // waitDialog.dismiss();
                        }
                    }, new IErrorReceiver() {
                        @Override
                        public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                            handler.sendEmptyMessage(11);
                        }
                    });

            ClientSession.Instance().asynGetResponse(new TestResultRequest(uid),
                    new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {
                            TestResultResponse tr = (TestResultResponse) response;

                            if (tr != null && tr.result.equals("1")) {
                                testSum_0 = tr.testSum_0;
                                score_0 = tr.score_0;
                                testSum_1 = tr.testSum_1;
                                score_1 = tr.score_1;
                                testSum_2 = tr.testSum_2;
                                score_2 = tr.score_2;
                                testSum_3 = tr.testSum_3;
                                score_3 = tr.score_3;
                                testSum_4 = tr.testSum_4;
                                score_4 = tr.score_4;

                                handler.sendEmptyMessage(1);
                            } else {
                                handler.sendEmptyMessage(11);
                            }
                            // waitDialog.dismiss();
                        }
                    }, new IErrorReceiver() {
                        @Override
                        public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                            handler.sendEmptyMessage(11);
                        }
                    });

        }
    }

}
