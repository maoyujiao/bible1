package com.iyuba.core.teacher.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.iyuba.base.util.SimpleNightMode;
import com.iyuba.biblelib.R;
import com.iyuba.core.bean.QuestionListBean;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.QuestionManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.adapter.QuestionListAdapter;
import com.iyuba.core.teacher.protocol.DeleteAnswerQuesRequest;
import com.iyuba.core.teacher.protocol.DeleteAnswerQuesResponse;
import com.iyuba.core.teacher.protocol.GetQuesListResponse;
import com.iyuba.core.teacher.protocol.GetTheQuesListRequest;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.ExeRefreshTime;
import com.iyuba.core.widget.ContextMenu;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;

import java.util.ArrayList;

public class TheQuesListActivity extends ListActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
    public int pageNum = 1;
    boolean isLast = false;
    String type = "0";
    ContextMenu contextMenu;
    private Context mContext;
    private ImageView btnBack, tinsert;
    private PullToRefreshView refreshView;// 刷新列表
    private ListView quesListview;
    private QuestionListAdapter quesAdapter;
    private CustomDialog waitDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    quesAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    waitDialog.show();
                    break;
                case 3:
                    waitDialog.dismiss();
                    break;
                case 4:
                    refreshView.onHeaderRefreshComplete();
                    break;
                case 5:
                    refreshView.onFooterRefreshComplete();
                    break;
                case 6:
                    CustomToast.showToast(mContext, R.string.no_data);
                    break;

                case 7:
                    CustomToast.showToast(mContext, "已是最后一页");
                    break;
            }
        }
    };
    private ArrayList<QuestionListBean.QuestionDataBean> quesList = new ArrayList<>();
    private boolean insertClick = false;
    private SimpleNightMode simpleNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_myques_list);
        mContext = this;
        waitDialog = WaittingDialog.showDialog(mContext);
        simpleNightMode = new SimpleNightMode(this);
        simpleNightMode.onResume();
        //获取传过来的问题类型
        Intent intent = getIntent();
        type = intent.getStringExtra("utype");
        if (type == null) type = "0";


        initWidget();
    }

    @Override
    protected void onDestroy() {
        simpleNightMode.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (insertClick) {
            insertClick = false;
            getHeaderData();
        }
    }

    public void initWidget() {


        contextMenu = findViewById(R.id.context_menu);
        btnBack = findViewById(R.id.btn_back);
        tinsert = findViewById(R.id.tinsert);

        quesListview = getListView();//(ListView) findViewById(R.id.ques_listview);
        refreshView = findViewById(R.id.ll_queslist_content);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });


        tinsert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                insertClick = true;
                Intent intent = new Intent();
                intent.setClass(mContext, QuezActivity.class);
                startActivity(intent);
            }
        });


        quesListview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {


                QuestionManager.getInstance().question = quesList.get(arg2);

                if (quesList.get(arg2).getUid().equals(AccountManager.Instace(mContext).userId)) {

                    final int theqid = quesList.get(arg2).getQuestionid();
                    final int num = arg2;

                    contextMenu.setText(mContext.getResources().getStringArray(
                            R.array.choose_delete));
                    contextMenu.setCallback(new ResultIntCallBack() {

                        @Override
                        public void setResult(int result) {
                            switch (result) {
                                case 0:
                                    delAlertDialog(theqid + "", num);
                                    break;
                                case 1:
                                    Intent intent = new Intent();
                                    intent.setClass(mContext, QuesDetailActivity.class);
                                    intent.putExtra("qid", theqid + "");
                                    startActivity(intent);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    contextMenu.show();
                    return true;
                } else {
                    return false;

                }

            }
        });


        quesAdapter = new QuestionListAdapter(mContext, quesList);
        quesListview.setAdapter(quesAdapter);
        quesListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //	QuestionManager.getInstance().question.qid  = quesList.get(arg2).qid;

                Intent intent = new Intent();
                intent.setClass(mContext, QuesDetailActivity.class);
                intent.putExtra("qid", quesList.get(arg2).getQuestionid() + "");
                startActivity(intent);
            }
        });

        getHeaderData();
    }

    private void delAlertDialog(final String id, final int num) {
        AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle(R.string.alert_title);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setMessage(mContext.getString(R.string.alert_delete));
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.alert_btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        ExeProtocol.exe(new DeleteAnswerQuesRequest("0", id, AccountManager.Instace(mContext).userId), new ProtocolResponse() {

                            @Override
                            public void finish(BaseHttpResponse bhr) {

                                DeleteAnswerQuesResponse tr = (DeleteAnswerQuesResponse) bhr;
                                if (tr.result.equals("1")) {
                                    quesList.remove(num);

                                    handler.sendEmptyMessage(1);
                                    handler.sendEmptyMessage(8);
                                } else {
                                    quesList.remove(num);

                                    handler.sendEmptyMessage(1);
                                    handler.sendEmptyMessage(8);
                                }
                            }

                            @Override
                            public void error() {
                            }
                        });


                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                    }
                });
        alert.show();
    }

    public void getHeaderData() {
        handler.sendEmptyMessage(2);

        ExeProtocol.exe(new GetTheQuesListRequest(AccountManager.Instace(mContext).userId, type, 1), new ProtocolResponse() {

            @Override
            public void finish(BaseHttpResponse bhr) {
                GetQuesListResponse tr = (GetQuesListResponse) bhr;
                if (tr.list != null && tr.list.size() != 0) {
                    pageNum = 2;
                    quesList.clear();
                    quesList.addAll(tr.list);

                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(4);
                    isLast = tr.list.size() < 20;
                } else {
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(6);
                }
            }

            @Override
            public void error() {
                handler.sendEmptyMessage(3);
                handler.sendEmptyMessage(6);
            }
        });
    }

    public void getFooterData() {

        if (isLast) {
            handler.sendEmptyMessage(5);
            handler.sendEmptyMessage(7);
            return;
        }
        handler.sendEmptyMessage(2);
        ExeProtocol.exe(new GetTheQuesListRequest(AccountManager.Instace(mContext).userId, type, pageNum), new ProtocolResponse() {

            @Override
            public void finish(BaseHttpResponse bhr) {
                GetQuesListResponse tr = (GetQuesListResponse) bhr;
                if (tr.list != null && tr.list.size() != 0) {
                    pageNum++;
                    quesList.addAll(tr.list);
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(5);
                    isLast = tr.list.size() < 20;
                } else {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void error() {
                handler.sendEmptyMessage(1);
            }
        });
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        new GetFooterDataTask().execute();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        Log.e("onHeaderRefresh", "onHeaderRefresh");
        refreshView.setLastUpdated(ExeRefreshTime
                .lastRefreshTime("NewPostListUpdateTime"));
        new GetHeaderDataTask().execute();
    }

    private class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            ExeRefreshTime.lastRefreshTime("NewPostListUpdateTime");
            handler.sendEmptyMessage(2);
            getHeaderData();
            return null;
        }
    }

    private class GetFooterDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            ExeRefreshTime.lastRefreshTime("NewPostListUpdateTime");
            getFooterData();
            return null;
        }
    }

}
