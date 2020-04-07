package com.iyuba.wordtest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.iyuba.wordtest.R;
import com.iyuba.wordtest.R2;
import com.iyuba.wordtest.adapter.StepAdapter;
import com.iyuba.wordtest.db.CetDataBase;
import com.iyuba.wordtest.entity.CetRootWord;
import com.iyuba.wordtest.manager.WordConfigManager;
import com.iyuba.wordtest.widget.MyGridView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordStepActivity extends AppCompatActivity {
    @BindView(R2.id.gridview)
    MyGridView gridview;
    @BindView(R2.id.all_words)
    TextView words_all;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    StepAdapter adapter;
    CetRootWord word;
    CetDataBase db;
    int dbSize;
    int wpd;
    int step;

    List<CetRootWord> wordList;
    private int checkedItem;
    public static int WORD_COUNT = 30 ;
    private ProgressDialog dialog ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_step);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary) , 0);
        ButterKnife.bind(this);
        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            WordConfigManager.Instance(getApplicationContext()).putBoolean("dbChange", false);
            step = WordConfigManager.Instance(getApplicationContext()).loadInt("stage", 1);
            words_all.setText(String.format("单词总数:%s   闯关单词数:%s", db.getCetRootWordDao().getAllRootWord().size()
                    , db.getCetRootWordDao().getWordsByStage(step).size()));
            adapter = new StepAdapter(3, step, dbSize / wpd + step);
            gridview.setAdapter(adapter);
            super.handleMessage(msg);
        }
    };

    @OnClick(R2.id.words_all)
    public void startAllWords() {
        WordListActivity.startIntnent(this, 0,true);
    }


    @OnClick(R2.id.set)
    public void set() {
        showAlert();
    }

    private void showAlert() {
        final String[] wpd = {"30", "50", "70", "100"};
        final String select = String.valueOf(WordConfigManager.Instance(this).loadInt("wpd", 30));
        for (int i = 0; i < wpd.length; i++) {
            if (wpd[i].equals(select)) {
                checkedItem = i;
            }
        }
        new AlertDialog.Builder(this)
                .setTitle("请选择每关单词数")
                .setSingleChoiceItems(wpd, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WORD_COUNT = Integer.parseInt(wpd[which]);
                    }
                })
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WordConfigManager.Instance(getApplicationContext()).putInt("wpd", WORD_COUNT);
                        initData();
                        dialog.dismiss();
                    }
                }).create().show();
        WordConfigManager.Instance(this).putBoolean("isWordNumberSelected", true);
        WordConfigManager.Instance(this).putBoolean("dbChange", true);

    }

    private void initData(){
        step = WordConfigManager.Instance(this).loadInt("stage", 1);
        db = CetDataBase.getInstance(this);
        if (db.getCetRootWordDao().getWordsByStage(0).size() > 0) {
            wordList = db.getCetRootWordDao().getAllRootWord();
        } else {
            wordList = db.getCetRootWordDao().getAllRootWord(step);
        }
        dbSize = wordList.size();
        dialog.setMessage("正在加载单词");
        dialog.show();
        wpd = WordConfigManager.Instance(this).loadInt("wpd", 30);
        if (WordConfigManager.Instance(this).loadBoolean("dbChange", true)) {
            new Thread(runnable).start();
        } else {
            mHanler.sendEmptyMessage(1);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<CetRootWord> list = new ArrayList<>();
            for (int i = 0; i < dbSize; i++) {
                word = wordList.get(i);
                word.stage = i / wpd + step;
                list.add(word);
            }
            db.getCetRootWordDao().updateWordSetStage(list);
            mHanler.sendEmptyMessage(1);
        }
    };
}
