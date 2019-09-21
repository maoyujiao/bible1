package com.iyuba.core.teacher.activity;

/**
 * 私信界面
 *
 * @author chentong
 * @version 1.0
 */

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
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.adapter.QNoticeListAdapter;
import com.iyuba.core.teacher.protocol.NoticeRequest;
import com.iyuba.core.teacher.protocol.NoticeResponse;
import com.iyuba.core.teacher.protocol.ReadNoticeRequest;
import com.iyuba.core.teacher.sqlite.mode.Notice;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.ExeRefreshTime;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class QuestionNotice extends BasisActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {
    public String noticeId = "0";
    private TextView title;
    private Button backButton;
    private Context mContext;
    private CustomDialog waitingDialog;
    private Boolean isLastPage = true;
    private Boolean isTopRefresh = false;
    private Boolean isFootRefresh = false;
    private int page = 1;
    private ListView messageList;// 新闻列表
    private PullToRefreshView refreshView;// 刷新列表
    private QNoticeListAdapter adapter;
    private ArrayList<Notice> letterList = new ArrayList<Notice>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    page = 1;
                    letterList.clear();
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    ExeProtocol.exe(
                            new NoticeRequest(AccountManager.Instace(mContext).userId, 0, page),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    NoticeResponse res = (NoticeResponse) bhr;
                                    if (res.result.equals("1")) {
                                        letterList.addAll(res.list);
                                        adapter.addList(letterList);
                                        if (res.list == null || res.list.size() == 0) {
                                            isLastPage = true;
                                        } else {
                                            page++;
                                            isLastPage = false;
                                        }
                                    } else {
                                    }
                                    handler.sendEmptyMessage(4);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                    handler.sendEmptyMessage(7);
                                }
                            });
                    break;
                case 2:
                    waitingDialog.show();
                    break;
                case 3:
                    waitingDialog.dismiss();
                    break;
                case 4:
                    handler.sendEmptyMessage(3);
                    adapter.notifyDataSetChanged();
                    if (isTopRefresh) {
                        isTopRefresh = false;
                        refreshView.onHeaderRefreshComplete();
                    } else if (isFootRefresh) {
                        isFootRefresh = false;
                        refreshView.onFooterRefreshComplete();
                    }
                    setListener();
                    break;
                case 6:
                    adapter.notifyDataSetChanged();
                    // 设置成已读
                    ClientSession.Instance().asynGetResponse(
                            new ReadNoticeRequest(noticeId),
                            new IResponseReceiver() {
                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {

                                }
                            });
                    break;
                case 7:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 8:
                    CustomToast.showToast(mContext, R.string.message_add_all);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_notice);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        initWidget();
        waitingDialog = WaittingDialog.showDialog(mContext);
    }

    private void initWidget() {

        title = findViewById(R.id.title);
        title.setText(R.string.question_title);
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        initLetterView();
    }

    private void initLetterView() {

        messageList = findViewById(R.id.list);
        refreshView = findViewById(R.id.listview);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        adapter = new QNoticeListAdapter(mContext);
        messageList.setAdapter(adapter);
        handler.sendEmptyMessage(0);
    }

    private void setListener() {
        messageList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                //SocialDataManager.Instance().letterlist = letterList;

                letterList.get(position).isnew = "0";
                //ViewHolder viewholder = (ViewHolder) arg1.getTag();
                //viewholder.isNew.setVisibility(View.GONE);
                handler.sendEmptyMessage(6);// 进入问题详情
                noticeId = letterList.get(position).id;
                Intent intent = new Intent();
                intent.putExtra("qid", letterList.get(position).from_id + "");
                intent.setClass(mContext, QuesDetailActivity.class);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

        if (!isLastPage) {
            handler.sendEmptyMessage(1);
            isFootRefresh = true;
        } else {
            handler.sendEmptyMessage(8);
            refreshView.onFooterRefreshComplete();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

        handler.sendEmptyMessage(0);
        refreshView.setLastUpdated(ExeRefreshTime
                .lastRefreshTime("MessageCenter"));
        isTopRefresh = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
