package com.iyuba.core.discover.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.discover.adapter.WordListAdapter;
import com.iyuba.core.discover.protocol.WordUpdateRequest;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.news.WordSynRequest;
import com.iyuba.core.protocol.news.WordSynResponse;
import com.iyuba.core.sqlite.mode.Word;
import com.iyuba.core.sqlite.op.WordOp;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.ExeRefreshTime;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 单词本界面
 *
 * @author chentong
 * @version 1.0
 */

public class WordCollection extends BasisActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {
    private Context mContext;
    private ArrayList<Word> words;
    private ArrayList<Word> tryToDeleteWords = new ArrayList<Word>();
    private WordOp wo;
    private PullToRefreshView refreshView;// 刷新列表
    private View no_login;
    private ListView wordList;
    private WordListAdapter nla;
    private boolean isDelStart = false;
    private TextView delButton;
    private String userId;
    private int page = 1;
    private Boolean isLastPage = false;
    private Boolean isTopRefresh = false;
    private Boolean isFootRefresh = false;
    private CustomDialog wettingDialog;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    nla.notifyDataSetChanged();
                    break;
                case 1:
                    wettingDialog.show();
                    break;
                case 2:
                    wettingDialog.dismiss();
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 4:
                    page = 1;
                    if (words != null) {
                        words.clear();
                    }
                    handler.sendEmptyMessage(5);
                    handler.sendEmptyMessage(1);
                    break;
                case 5:
                    ExeProtocol.exe(new WordSynRequest(userId, userId, page),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    WordSynResponse wsr = (WordSynResponse) bhr;
                                    words.addAll(wsr.wordList);
                                    if (words != null && words.size() > 0) {
                                        if (wsr.firstPage == wsr.nextPage) {
                                            isLastPage = true;
                                        } else {
                                            page++;
                                            isLastPage = false;
                                        }
                                        nla.setData(words);
                                        wo.saveData(wsr.wordList);
                                        handler.sendEmptyMessage(0);
                                        handler.sendEmptyMessage(6);

                                        handler.sendEmptyMessage(11);
                                    } else {
                                        handler.sendEmptyMessage(2);
                                        handler.sendEmptyMessage(7);
                                    }
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                    handler.sendEmptyMessage(6);
                                }
                            });
                    break;
                case 6:
                    handler.sendEmptyMessage(2);
                    if (isTopRefresh) {
                        isTopRefresh = false;
                        refreshView.onHeaderRefreshComplete();
                    } else if (isFootRefresh) {
                        isFootRefresh = false;
                        refreshView.onFooterRefreshComplete();
                    }
                    break;
                case 7:
                    CustomToast.showToast(mContext, R.string.word_no_data);
                    break;
                case 8:
                    CustomToast.showToast(mContext, R.string.word_add_all);
                    break;
                case 9:
                    ClientSession.Instance().asynGetResponse(
                            new WordUpdateRequest(userId,
                                    WordUpdateRequest.MODE_DELETE,
                                    msg.obj.toString()), new IResponseReceiver() {

                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {
                                    wo.deleteItemWord(userId);
                                }
                            });
                    break;
                case 11:
                    onResume0();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_collection_list);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        wettingDialog = WaittingDialog.showDialog(mContext);
        refreshView = findViewById(R.id.listview);
        if (Constant.APP_CONSTANT.isEnglish()) {
            refreshView.setOnHeaderRefreshListener(this);
            refreshView.setOnFooterRefreshListener(this);
        } else {
            refreshView.setRefreshable(false);
        }
        no_login = findViewById(R.id.noLogin);
        // ----------------------------------删除按钮
        delButton = findViewById(R.id.button_delete);
        delButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDelStart) {
                    delButton.setText("编辑");
                    StringBuffer ids = new StringBuffer("");
                    Iterator<Word> iteratorVoa = null;
                    try {
                        iteratorVoa = words.iterator();
                        ArrayList<Word> delWordTemp = new ArrayList<Word>();
                        while (iteratorVoa.hasNext()) {
                            Word word = iteratorVoa.next();
                            if (word.isDelete) {
                                delWordTemp.add(word);
                                ids.append(",").append("\'" + word.key + "\'");
                                iteratorVoa.remove();
                            }
                        }
                        if (ids.toString() != null
                                && ids.toString().length() != 0) {
                            delNetwordWord(delWordTemp);
                            wo.tryToDeleteItemWord(ids.toString().substring(1),
                                    userId);
                        } else {
                            CustomToast.showToast(mContext,
                                    R.string.newword_please_select_word, 1000);
                        }
                    } catch (Exception e) {// 当初始化单词表中尚无单词是出现异常处理
                        CustomToast.showToast(mContext, R.string.no_new_word,
                                1000);
                    }
                    cancelDelete();
                    isDelStart = false;
                } else {
                    delButton.setText("删除");
                    isDelStart = true;
                }
                changeItemDeleteStart();
            }
        });
        wordList = findViewById(R.id.list);
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                onBackPressed();
            }
        });
    }

    public void changeItemDeleteStart() {
        if (nla != null) {
            nla.modeDelete = isDelStart;
            handler.sendEmptyMessage(0);
        }
    }

    public void cancelDelete() {
        if (words != null && words.size() != 0) {
            int size = words.size();
            for (int i = 0; i < size; i++) {
                words.get(i).isDelete = false;
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        onResume0();
    }

    private void onResume0() {
        if (AccountManager.Instace(mContext).checkUserLogin()) {
            no_login.setVisibility(View.GONE);
            refreshView.setVisibility(View.VISIBLE);
            userId = AccountManager.Instace(mContext).userId;
        } else {
            no_login.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.GONE);
            findViewById(R.id.button_to_login).setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent();
                            intent.setClass(mContext, Login.class);
                            startActivity(intent);
                        }
                    });
        }
        if (wordList != null) {
            isDelStart = false;
            if (nla != null) {
                cancelDelete();
                isDelStart = false;
                changeItemDeleteStart();
            }
            wo = new WordOp(this);
            words = (ArrayList<Word>) wo.findDataByAll(userId);
            tryToDeleteWords = (ArrayList<Word>) wo.findDataByDelete(userId);
            if (tryToDeleteWords != null
                    && NetWorkState.isConnectingToInternet()) {
                delNetwordWord(tryToDeleteWords);
            }
            nla = new WordListAdapter(this);
            wordList.setAdapter(nla);
            if (words != null) {
                nla.setData(words);
                handler.sendEmptyMessage(0);
            } else {
                words = new ArrayList<>();
                onHeaderRefresh(refreshView);
            }
            wordList.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {

                    if (isDelStart) {
                        words.get(arg2).isDelete = !words.get(arg2).isDelete;
                        handler.sendEmptyMessage(0);
                    } else {
                        Intent intent = new Intent(mContext, WordContent.class);
                        intent.putExtra("word", words.get(arg2).key);
                        intent.putExtra("search", false);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void delNetwordWord(ArrayList<Word> wordss) {
        int size = wordss.size();
        Message message;
        for (int i = 0; i < size; i++) {
            message = new Message();
            message.what = 9;
            message.obj = wordss.get(i).key;
            handler.sendMessageDelayed(message, 1500);
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

        if (!isLastPage) {
            handler.sendEmptyMessage(5);
            isFootRefresh = true;
        } else {
            handler.sendEmptyMessage(8);
            refreshView.onFooterRefreshComplete();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

        if (AccountManager.Instace(mContext).checkUserLogin()) {
            handler.sendEmptyMessage(4);
            refreshView.setLastUpdated(ExeRefreshTime.lastRefreshTime("Word"));
            isTopRefresh = true;
        }
    }
}
