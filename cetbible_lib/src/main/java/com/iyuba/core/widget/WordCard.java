package com.iyuba.core.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.Login;
import com.iyuba.core.discover.activity.WordContent;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.base.DictRequest;
import com.iyuba.core.protocol.base.DictResponse;
import com.iyuba.core.protocol.news.WordUpdateRequest;
import com.iyuba.core.sqlite.mode.Word;
import com.iyuba.core.sqlite.op.WordDBOp;
import com.iyuba.core.sqlite.op.WordOp;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.dialog.CustomToast;

/**
 * 单词卡
 *
 * @author 陈彤
 */
public class WordCard extends LinearLayout {
    LayoutInflater layoutInflater;
    private Context mContext;
    private Button add_word, close_word;
    private ProgressBar progressBar_translate;
    private String selectText;
    private WordOp op;
    private TextView key, def, pron;
    private ImageView collect ;
    private Word selectCurrWordTemp;
    private ImageView speaker;
    private View main;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showWordDefInfo();
                    break;
                case 2:
                    CustomToast.showToast(mContext,
                            R.string.play_no_word_interpretation, 1000);
                    WordCard.this.setVisibility(View.GONE);
                    break;
                case 3:
                    CustomToast.showToast(mContext,
                            R.string.play_please_take_the_word, 1000);
//                    WordCard.this.setVisibility(View.GONE);
                    break;
            }
        }
    };

    public WordCard(Context context) {
        super(context);
        mContext = context;
        ((Activity) mContext).getLayoutInflater().inflate(R.layout.wordcard,
                this);
        initGetWordMenu();
    }



    public WordCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        ((Activity) mContext).getLayoutInflater().inflate(R.layout.wordcard,
                this);
        initGetWordMenu();
    }

    private void initGetWordMenu() {
        main = findViewById(R.id.word);
        progressBar_translate = findViewById(R.id.progressBar_get_Interperatatior);
        key = findViewById(R.id.word_key);
        def = findViewById(R.id.word_def);
        pron = findViewById(R.id.word_pron);
        speaker = findViewById(R.id.word_speaker);
        add_word = findViewById(R.id.word_add);
        add_word.setOnClickListener(new OnClickListener() { // 添加到生词本

            @Override
            public void onClick(View v) {
                saveNewWords();
            }
        });
        close_word = findViewById(R.id.word_close);
        collect = findViewById(R.id.word_collect);
        close_word.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                WordCard.this.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 获取单词释义
     */
    private void getNetworkInterpretation() {
        if (selectText != null && selectText.length() != 0) {
            WordDBOp wordDBOp = new WordDBOp(mContext);
            WordOp wordOp = new WordOp(mContext);
            Word collectResult = wordOp.findDataByName(selectText);
            if (collectResult!=null){
                collect.setImageDrawable(getResources().getDrawable(R.drawable.collect_true));
            }else {
                collect.setImageDrawable(getResources().getDrawable(R.drawable.collect_false));
            }
            selectCurrWordTemp = wordDBOp.findDataByKey(selectText);
            wordDBOp.updateWord(selectText);
            if (selectCurrWordTemp != null) {
                if (selectCurrWordTemp.def != null
                        && selectCurrWordTemp.def.length() != 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            } else {

                ExeProtocol.exe(new DictRequest(selectText),
                        new ProtocolResponse() {
                            @Override
                            public void finish(BaseHttpResponse bhr) {

                                DictResponse dictResponse = (DictResponse) bhr;
                                selectCurrWordTemp = dictResponse.word;
                                if (selectCurrWordTemp != null) {
                                    if (selectCurrWordTemp.def != null
                                            && selectCurrWordTemp.def.length() != 0) {
                                        handler.sendEmptyMessage(1);
                                    } else {
                                        handler.sendEmptyMessage(2);
                                    }
                                }
                            }

                            @Override
                            public void error() {

                                handler.sendEmptyMessage(3);
                            }
                        });
            }
        }
    }

    private void showWordDefInfo() {
        if (selectCurrWordTemp != null) {
            key.setText(selectCurrWordTemp.key);
            def.setText(selectCurrWordTemp.def);
            if (selectCurrWordTemp.pron != null
                    && !selectCurrWordTemp.pron.equals("null")) {
                StringBuffer sb = new StringBuffer();
                sb.append('[').append(selectCurrWordTemp.pron).append(']');
                pron.setText(TextAttr.decode(sb.toString()));
            }
            if (selectCurrWordTemp.audioUrl != null
                    && selectCurrWordTemp.audioUrl.length() != 0) {
                speaker.setVisibility(View.VISIBLE);
            } else {
                speaker.setVisibility(View.GONE);
            }
            speaker.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Player player = new Player(mContext, null);
                    String url = selectCurrWordTemp.audioUrl;
                    player.playUrl(url);
                }
            });
            main.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, WordContent.class);
                    intent.putExtra("word", selectCurrWordTemp.key);
                    mContext.startActivity(intent);
                }
            });
            add_word.setVisibility(View.VISIBLE); // 选词的同事隐藏加入生词本功能
            progressBar_translate.setVisibility(View.GONE); // 显示等待
        }
    }

    public void saveNewWords() {
        if (!AccountManager.Instace(mContext).checkUserLogin()) {
            Intent intent = new Intent();
            intent.setClass(mContext, Login.class);
            mContext.startActivity(intent);
        } else {
            try {
                selectCurrWordTemp.userid = AccountManager.Instace(mContext).userId;
                WordOp wo = new WordOp(mContext);
                wo.saveData(selectCurrWordTemp);
                CustomToast.showToast(mContext,
                        R.string.play_ins_new_word_success, 1000);
                WordCard.this.setVisibility(View.GONE);
                addNetwordWord(selectCurrWordTemp.key);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    public void addNetwordWord(String wordTemp) {
        collect.setImageDrawable(getResources().getDrawable(R.drawable.collect_true));

        ClientSession.Instance().asynGetResponse(
                new WordUpdateRequest(AccountManager.Instace(mContext).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response,
                                           BaseHttpRequest request, int rspCookie) {
                    }
                }, null, null);
    }

    public void searchWord(String word) {
        selectText = word;
        getNetworkInterpretation();
    }
}
