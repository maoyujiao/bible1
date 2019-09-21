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
import com.iyuba.core.me.protocol.UserRankRequest;
import com.iyuba.core.me.protocol.UserRankResponse;
import com.iyuba.core.me.protocol.UserRecordRequest;
import com.iyuba.core.me.protocol.UserRecordResponse;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IErrorReceiver;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.ErrorResponse;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.text.DecimalFormat;

public class LearnResultActivity extends Activity {
    private Context mContext;
    private TextView tv;
    private TextView lsTime, lsWord, lsSentence, lsArticle, skTime, skWord,
            skSentence, skArticle, rdTime, rdWord, rdSentence, rdArticle,
            wrTime, wrWord, wrSentence, wrArticle, othrTime, othrWord,
            othrSentence, othrArticle;
    private String studyTime_0, wordSum_0, sentenceSum_0, articleSum_0,
            studyTime_1, wordSum_1, sentenceSum_1, articleSum_1, studyTime_2,
            wordSum_2, sentenceSum_2, articleSum_2, studyTime_3, wordSum_3,
            sentenceSum_3, articleSum_3, studyTime_4, wordSum_4, sentenceSum_4,
            articleSum_4;
    private String totalTime = "", positionByTime = "", totalTest = "",
            positionByTest = "", totalRate = "", positionByRate = "";
    private Button listenDetail, speakDetail, readDetail, writeDetail,
            othrDetail, backBtn;
    private CustomDialog waitDialog;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // waitDialog.show();
                    if (totalTime == "" || positionByTime == "" || totalTest == ""
                            || positionByTest == "" || totalRate == ""
                            || positionByRate == "") {
                        handler.sendEmptyMessageDelayed(1, 500);
                    } else {
                        othrTime.setText(studyTime_0);
                        othrWord.setText(wordSum_0);
                        othrSentence.setText(sentenceSum_0);
                        othrArticle.setText(articleSum_0);
                        lsTime.setText(studyTime_1);
                        lsWord.setText(wordSum_1);
                        lsSentence.setText(sentenceSum_1);
                        lsArticle.setText(articleSum_1);
                        skTime.setText(studyTime_2);
                        skWord.setText(wordSum_2);
                        skSentence.setText(sentenceSum_2);
                        skArticle.setText(articleSum_2);
                        rdTime.setText(studyTime_3);
                        rdWord.setText(wordSum_3);
                        rdSentence.setText(sentenceSum_3);
                        rdArticle.setText(articleSum_3);
                        wrTime.setText(studyTime_4);
                        wrWord.setText(wordSum_4);
                        wrSentence.setText(sentenceSum_4);
                        wrArticle.setText(articleSum_4);

                        Integer tt = Integer.valueOf(totalTime);
                        double ttt = (long) tt / 3600.00;
                        DecimalFormat df = new DecimalFormat("0.00");
                        String tttt = df.format(ttt);
                        totalTime = String.valueOf(tttt);


                        SpannableStringBuilder style1, style2, style3, style4, style5, style6;
                        String r1 = "恭喜您！\n已学习" + totalTime, r2 = "小时\n按时间全站排名："
                                + positionByTime;
                        // , r3 = "\n已做题" + totalTest, r4 = "道，按做题量排名："
                        // + positionByTest, r5 = "\n做题正确率：" + totalRate, r6 =
                        // "，按正确率全站排名："
                        // + positionByRate;
                        style1 = new SpannableStringBuilder(r1);
                        style2 = new SpannableStringBuilder(r2);
                        // style3 = new SpannableStringBuilder(r3);
                        // style4 = new SpannableStringBuilder(r4);
                        // style5 = new SpannableStringBuilder(r5);
                        // style6 = new SpannableStringBuilder(r6);
                        style1.setSpan(new ForegroundColorSpan(Color.YELLOW),
                                r1.indexOf(totalTime), r1.indexOf(totalTime)
                                        + totalTime.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        style2.setSpan(
                                new ForegroundColorSpan(Color.YELLOW),
                                r2.indexOf(positionByTime),
                                r2.indexOf(positionByTime)
                                        + positionByTime.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // style3.setSpan(new ForegroundColorSpan(Color.YELLOW),
                        // r3.indexOf(totalTest), r3.indexOf(totalTest)
                        // + totalTest.length(),
                        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // style4.setSpan(
                        // new ForegroundColorSpan(Color.YELLOW),
                        // r4.indexOf(positionByTest),
                        // r4.indexOf(positionByTest)
                        // + positionByTest.length(),
                        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // style5.setSpan(new ForegroundColorSpan(Color.YELLOW),
                        // r5.indexOf(totalRate), r5.indexOf(totalRate)
                        // + totalRate.length(),
                        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // style6.setSpan(
                        // new ForegroundColorSpan(Color.YELLOW),
                        // r6.indexOf(positionByRate),
                        // r6.indexOf(positionByRate)
                        // + positionByRate.length(),
                        // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        style1 = style1.append(style2);
                        tv.setText(style1);

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
        setContentView(R.layout.intel_learn_result);
        mContext = this;
        waitDialog = WaittingDialog.showDialog(LearnResultActivity.this);

        tv = findViewById(R.id.intel_conclusion);
        lsTime = findViewById(R.id.ls_time);
        lsWord = findViewById(R.id.ls_word);
        lsSentence = findViewById(R.id.ls_sentence);
        lsArticle = findViewById(R.id.ls_article);
        skTime = findViewById(R.id.sk_time);
        skWord = findViewById(R.id.sk_word);
        skSentence = findViewById(R.id.sk_sentence);
        skArticle = findViewById(R.id.sk_article);
        rdTime = findViewById(R.id.rd_time);
        rdWord = findViewById(R.id.rd_word);
        rdSentence = findViewById(R.id.rd_sentence);
        rdArticle = findViewById(R.id.rd_article);
        wrTime = findViewById(R.id.wr_time);
        wrWord = findViewById(R.id.wr_word);
        wrSentence = findViewById(R.id.wr_sentence);
        wrArticle = findViewById(R.id.wr_article);
        othrTime = findViewById(R.id.othr_time);
        othrWord = findViewById(R.id.othr_word);
        othrSentence = findViewById(R.id.othr_sentence);
        othrArticle = findViewById(R.id.othr_article);
        listenDetail = findViewById(R.id.ld);
        speakDetail = findViewById(R.id.sd);
        readDetail = findViewById(R.id.rd);
        writeDetail = findViewById(R.id.wd);
        othrDetail = findViewById(R.id.othrd);
        backBtn = findViewById(R.id.button_back);

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        listenDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "1");
                intent.setClass(mContext, ResultDetailActivity.class);
                startActivity(intent);
            }
        });

        speakDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "2");
                intent.setClass(mContext, ResultDetailActivity.class);
                startActivity(intent);
            }
        });

        readDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "3");
                intent.setClass(mContext, ResultDetailActivity.class);
                startActivity(intent);
            }
        });

        writeDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "4");
                intent.setClass(mContext, ResultDetailActivity.class);
                startActivity(intent);
            }
        });

        othrDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "0");
                intent.setClass(mContext, ResultDetailActivity.class);
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
            String uid = AccountManager.Instace(mContext).getId();

            ClientSession.Instance().asynGetResponse(new UserRankRequest(uid),
                    new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {
                            UserRankResponse tr = (UserRankResponse) response;

                            if (tr != null && tr.result.equals("1")) {
                                totalTime = tr.totalTime;
                                positionByTime = tr.positionByTime;
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

            ClientSession.Instance().asynGetResponse(new UserRecordRequest(uid),
                    new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {
                            UserRecordResponse tr = (UserRecordResponse) response;

                            if (tr != null && tr.result.equals("1")) {
                                studyTime_0 = tr.studyTime_0;
                                wordSum_0 = tr.wordSum_0;
                                sentenceSum_0 = tr.sentenceSum_0;
                                articleSum_0 = tr.articleSum_0;
                                studyTime_1 = tr.studyTime_1;
                                wordSum_1 = tr.wordSum_1;
                                sentenceSum_1 = tr.sentenceSum_1;
                                articleSum_1 = tr.articleSum_1;
                                studyTime_2 = tr.studyTime_2;
                                wordSum_2 = tr.wordSum_2;
                                sentenceSum_2 = tr.sentenceSum_2;
                                articleSum_2 = tr.articleSum_2;
                                studyTime_3 = tr.studyTime_3;
                                wordSum_3 = tr.wordSum_3;
                                sentenceSum_3 = tr.sentenceSum_3;
                                articleSum_3 = tr.articleSum_3;
                                studyTime_4 = tr.studyTime_4;
                                wordSum_4 = tr.wordSum_4;
                                sentenceSum_4 = tr.sentenceSum_4;
                                articleSum_4 = tr.articleSum_4;

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
