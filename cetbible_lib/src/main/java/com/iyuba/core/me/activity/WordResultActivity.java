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
import com.iyuba.core.me.protocol.WordResultRequest;
import com.iyuba.core.me.protocol.WordResultResponse;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IErrorReceiver;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.ErrorResponse;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;


public class WordResultActivity extends Activity {
    private Context mContext;
    private TextView tv, lsWordSum, skWordSum, rdWordSum, wrWordSum,
            othrWordSum;
    private String wordSum_0 = "", wordSum_1 = "", wordSum_2 = "",
            wordSum_3 = "", wordSum_4 = "";
    private Button othrDetail, listenDetail, speakDetail, readDetail, wrDetail,
            backBtn;
    private CustomDialog waitDialog;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // waitDialog.show();
                    if (wordSum_1 == "" || wordSum_2 == "" || wordSum_0 == ""
                            || wordSum_3 == "" || wordSum_4 == "") {
                        handler.sendEmptyMessageDelayed(1, 500);
                    } else {
                        lsWordSum.setText(wordSum_1);
                        skWordSum.setText(wordSum_2);
                        othrWordSum.setText(wordSum_0);
                        rdWordSum.setText(wordSum_3);
                        wrWordSum.setText(wordSum_4);

                        int wordSum = Integer.valueOf(wordSum_0)
                                + Integer.valueOf(wordSum_1)
                                + Integer.valueOf(wordSum_2)
                                + Integer.valueOf(wordSum_3)
                                + Integer.valueOf(wordSum_4);

                        SpannableStringBuilder style3;
                        String r3 = "恭喜您！\n您的智慧化单词大数据中需要有"
                                + String.valueOf(wordSum) + "个单词强化记忆";
                        style3 = new SpannableStringBuilder(r3);
                        // style4 = new SpannableStringBuilder(r4);
                        // style5 = new SpannableStringBuilder(r5);
                        // style6 = new SpannableStringBuilder(r6);
                        //
                        style3.setSpan(new ForegroundColorSpan(Color.YELLOW),
                                r3.indexOf(String.valueOf(wordSum)), r3.indexOf(String.valueOf(wordSum))
                                        + String.valueOf(wordSum).length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                        // style3 = style3
                        // .append(style4.append(style5.append(style6)));
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
        setContentView(R.layout.intel_word_result);
        mContext = this;
        waitDialog = WaittingDialog.showDialog(WordResultActivity.this);

        tv = findViewById(R.id.intel_conclusion);
        lsWordSum = findViewById(R.id.ls_word_num);
        skWordSum = findViewById(R.id.sk_word_num);
        rdWordSum = findViewById(R.id.rd_word_num);
        wrWordSum = findViewById(R.id.wr_word_num);
        othrWordSum = findViewById(R.id.othr_word_num);

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
                intent.setClass(mContext, WordDetailActivity.class);
                startActivity(intent);

            }
        });

        listenDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "1");
                intent.setClass(mContext, WordDetailActivity.class);
                startActivity(intent);
            }
        });

        speakDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "2");
                intent.setClass(mContext, WordDetailActivity.class);
                startActivity(intent);
            }
        });

        readDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "3");
                intent.setClass(mContext, WordDetailActivity.class);
                startActivity(intent);
            }
        });

        wrDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("testMode", "4");
                intent.setClass(mContext, WordDetailActivity.class);
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

            ClientSession.Instance().asynGetResponse(new WordResultRequest(uid),
                    new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {
                            WordResultResponse tr = (WordResultResponse) response;

                            if (tr != null && tr.result.equals("1")) {
                                wordSum_0 = tr.wordSum_0;
                                wordSum_1 = tr.wordSum_1;
                                wordSum_2 = tr.wordSum_2;
                                wordSum_3 = tr.wordSum_3;
                                wordSum_4 = tr.wordSum_4;

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
