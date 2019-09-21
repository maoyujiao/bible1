package com.iyuba.core.discover.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.listener.OnPlayStateChangedListener;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.base.DictRequest;
import com.iyuba.core.protocol.base.DictResponse;
import com.iyuba.core.protocol.news.WordUpdateRequest;
import com.iyuba.core.sqlite.mode.Word;
import com.iyuba.core.sqlite.op.EGDBOp;
import com.iyuba.core.sqlite.op.WordDBOp;
import com.iyuba.core.sqlite.op.WordOp;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.Player;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

/**
 * 单词内容界面
 *
 * @author chentong
 * @version 1.0
 * @para "word"传入单词值
 */
public class WordContent extends BasisActivity {
    private boolean isEnglish;
    ImageView saveBtn;
    private Context mContext;
    private String appointWord;
    private TextView key, pron, def, example;
    private Word curWord;
    private ImageView speaker;
    private boolean fromHtml;
    Player player ;
    private CustomDialog waittingDialog;
    private boolean isFromSearch = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waittingDialog.show();
                    break;
                case 1:
                    waittingDialog.dismiss();
                    break;
                case 2:
                    showWordDefInfo();
                    break;
                case 3:
                    new WordOp(mContext).saveData(curWord);
                    CustomToast.showToast(mContext,
                            R.string.play_ins_new_word_success);
                    break;
                case 4:
                    CustomToast.showToast(mContext,
                            R.string.play_please_take_the_word);
                    break;
                case 5:
                    handler.sendEmptyMessage(1);
                    CustomToast.showToast(mContext,
                            mContext.getString(R.string.action_fail) + "\n"
                                    + mContext.getString(R.string.check_network));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);
        isEnglish = Constant.APP_CONSTANT.isEnglish();
        isFromSearch = getIntent().getBooleanExtra("search", false);


        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        waittingDialog = WaittingDialog.showDialog(mContext);
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        saveBtn = findViewById(R.id.word_add);
        if (!isFromSearch) {
            saveBtn.setVisibility(View.GONE);
        }
        saveBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isEnglish) {
                    saveNewWords();
                } else {
                    curWord.userid = AccountManager.Instace(mContext).getId();
                    new WordOp(mContext).saveData(curWord);
                    CustomToast.showToast(mContext,
                            R.string.play_ins_new_word_success);
                    saveBtn.setClickable(false);
                    saveBtn.setBackgroundResource(R.drawable.word_add_ok);
                }
            }
        });
        appointWord = this.getIntent().getStringExtra("word");
        initGetWordMenu();
        if (isEnglish) {
            handler.sendEmptyMessage(0);
        }
    }

    private void initGetWordMenu() {
        key = findViewById(R.id.word_key);
        pron = findViewById(R.id.word_pron);
        def = findViewById(R.id.word_def);
        example = findViewById(R.id.example);
        speaker = findViewById(R.id.word_speaker);
        if (isEnglish || isFromSearch) {

            curWord = new WordDBOp(mContext).findDataByKey(appointWord);
            if (curWord != null) {
                if (isEnglish) {
                    curWord.examples = new EGDBOp(mContext).findData(curWord.key);
                    handler.sendEmptyMessage(2);
                } else {
                    curWord.examples = new EGDBOp(mContext).findJPData(curWord.key);
                    setInfo(curWord);
                }
            }
            if (isEnglish) {
                getNetworkInterpretation();
            }
        } else {
            Word word = new WordOp(mContext).findDataByName(appointWord);
            setInfo(word);
        }
    }

    private void setInfo(final Word word) {
        key.setText(word.key);
        pron.setText("[" + word.pron + "]");
        if (TextUtils.isEmpty(word.examples)) {
            example.setText(R.string.no_word_example);
        } else {
            example.setText(word.examples);
        }
        def.setText(word.def);
        speaker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String url = word.audioUrl;
                if (player == null){
                    player = new Player(mContext,null);

                }
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                if (player.isPlaying()){
                    return;
                }
                player.playUrl(url);
            }
        });
    }

    /**
     * 获取单词释义
     */
    private void getNetworkInterpretation() {
        if (appointWord != null && appointWord.length() != 0) {
            ExeProtocol.exe(new DictRequest(appointWord),
                    new ProtocolResponse() {

                        @Override
                        public void finish(BaseHttpResponse bhr) {

                            DictResponse dictResponse = (DictResponse) bhr;
                            fromHtml = true;
                            curWord = dictResponse.word;
                            handler.sendEmptyMessage(2);
                        }

                        @Override
                        public void error() {

                            handler.sendEmptyMessage(5);
                        }
                    });
        } else {
            handler.sendEmptyMessage(4);
        }
    }

    /**
     * 显示单词释义
     */
    private void showWordDefInfo() {
        key.setText(curWord.key);
        if (curWord.pron != null && curWord.pron.length() != 0) {
            if (fromHtml) {
                pron.setText(Html.fromHtml("[" + curWord.pron + "]"));
            } else {
                pron.setText(TextAttr.decode("[" + curWord.pron + "]"));
            }
        }
        def.setText(curWord.def);
        if (curWord.examples != null && curWord.examples.length() != 0) {
            example.setText(Html.fromHtml(curWord.examples));
        } else {
            example.setText(R.string.no_word_example);
        }
        if (curWord.audioUrl != null && curWord.audioUrl.length() != 0) {
            speaker.setVisibility(View.VISIBLE);
        } else {
            speaker.setVisibility(View.GONE);
        }
        speaker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String url = curWord.audioUrl;
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                if(player == null){
                    player = new Player(mContext, null);
                }

                player.playUrl(url);
            }
        });
        handler.sendEmptyMessage(1);
    }

    /**
     * 保存单词值单词本
     */
    private void saveNewWords() {
        if (!AccountManager.Instace(mContext).checkUserLogin()) {
            Intent intent = new Intent();
            intent.setClass(mContext, Login.class);
            mContext.startActivity(intent);
        } else {
            curWord.userid = AccountManager.Instace(mContext).userId;
            handler.sendEmptyMessage(3);
            addNetwordWord(curWord.key);
            saveBtn.setClickable(false);
            saveBtn.setBackgroundResource(R.drawable.word_add_ok);
        }
    }

    /**
     * 同步网络生词库
     */
    private void addNetwordWord(String wordTemp) {
        ExeProtocol.exe(new WordUpdateRequest(
                        AccountManager.Instace(mContext).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {

                        onBackPressed();
                    }

                    @Override
                    public void error() {

                        handler.sendEmptyMessage(5);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fromHtml = false;
    }
}
