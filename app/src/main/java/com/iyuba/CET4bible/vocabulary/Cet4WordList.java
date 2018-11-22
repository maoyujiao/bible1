/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.vocabulary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.Cet4WordListAdapter;
import com.iyuba.CET4bible.manager.WordDataManager;
import com.iyuba.CET4bible.sqlite.mode.Cet4RootWord;
import com.iyuba.CET4bible.sqlite.mode.Cet4Word;
import com.iyuba.CET4bible.sqlite.op.Cet4WordOp;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class Cet4WordList extends BasisActivity {
    private Context mContext;
    private Button sortBtn;
    private ArrayList<Cet4Word> wordList;
    private ArrayList<Cet4RootWord> rootWordList;
    private ArrayList<Cet4Word> rootWords;
    private Cet4WordListAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.setList(wordList);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    adapter.setRootList(rootWordList);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    private ListView list;
    private Cet4WordOp wordOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        sortBtn = findViewById(R.id.sort);
        sortBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, WordSortSet.class);
                startActivity(intent);
            }
        });
        wordOp = new Cet4WordOp(mContext);
        list = findViewById(R.id.list);
        adapter = new Cet4WordListAdapter(mContext);

        String[] wordSort = ConfigManager.Instance().loadString("wordsort")
                .split("-");
        WordDataManager.Instance().cate = wordSort[0];
        WordDataManager.Instance().number = Integer.parseInt(wordSort[1]);

    }

    private void getDataList() {
        if (WordDataManager.Instance().cate.equals("0")) {
            wordList = wordOp.findDataByAll();
        } else if (WordDataManager.Instance().cate.equals("1")) {
            wordList = wordOp.findDataByRandom();
        } else if (WordDataManager.Instance().cate.equals("2")) {
            rootWordList = wordOp.findDataByRoot();
        } else if (WordDataManager.Instance().cate.equals("10")) {
            wordList = wordOp.findDataByStar("10");
        } else if (WordDataManager.Instance().cate.equals("13")) {
            wordList = wordOp.findDataByStar("13");
        } else if (WordDataManager.Instance().cate.equals("16")) {
            wordList = wordOp.findDataByStar("16");
        } else if (WordDataManager.Instance().cate.equals("20")) {
            wordList = wordOp.findDataByStar("20");
        } else {
            if (BuildConfig.isEnglish) {
                wordList = wordOp.findDataByLike(String.valueOf(WordDataManager.Instance().cate));
            } else {//日语根据发音查找
                wordList = wordOp.findJPDataByLikePron(String.valueOf(WordDataManager.Instance().cate));
            }
        }
        if (wordList == null) {
            wordList = new ArrayList<>();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConfigManager.Instance().putString(
                "wordsort",
                WordDataManager.Instance().cate + "-"
                        + WordDataManager.Instance().number);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataList();
        if (WordDataManager.Instance().cate.equals("2")) {
            adapter.setRootList(rootWordList);
            list.setAdapter(adapter);
            handler.sendEmptyMessage(1);
        } else {
            adapter.setList(wordList);
            list.setAdapter(adapter);
            list.setSelection(WordDataManager.Instance().number);
            handler.sendEmptyMessage(0);
        }

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (WordDataManager.Instance().cate.equals("2")) {
                    Cet4RootWord cet4RootWord = rootWordList.get(arg2);
                    Intent intent = new Intent(mContext, Cet4WordRoot.class);
                    intent.putExtra("rootWord", cet4RootWord.groupflg);
                    startActivity(intent);
//                    rootWords=wordOp.findWordByRoot(cet4RootWord.groupflg);
//                    adapter.setWordList(rootWords);
//                    adapter.setMode("rootWord");
//                    list.setAdapter(adapter);
//                    handler.sendEmptyMessage(2);
                } else {
                    Intent intent = new Intent(mContext, Cet4WordContent.class);
                    WordDataManager.Instance().pos = arg2;
                    WordDataManager.Instance().words = wordList;
                    startActivity(intent);
                }

            }
        });


    }
}
