package com.iyuba.core.me.activity;

/**
 * 关注界面
 *
 * @author chentong
 * @version 1.0
 * @para "userid" 当前用户userid（本人或其他人个人主页）
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
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.adapter.AttentionListAdapter;
import com.iyuba.core.me.sqlite.mode.Attention;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestAttentionList;
import com.iyuba.core.protocol.message.ResponseAttentionList;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.ExeRefreshTime;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView;

import java.util.ArrayList;

import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;


public class AttentionCenter extends BasisActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    private String currUserid;
    private Button backButton;
    private Context mContext;
    private CustomDialog waitingDialog;
    private Boolean isLastPage = false;
    private Boolean isTopRefresh = false;
    private Boolean isFootRefresh = false;
    private ListView fansList;// 新闻列表
    private PullToRefreshView refreshView;// 刷新列表
    private AttentionListAdapter adapter;
    private ArrayList<Attention> attentionArrayList = new ArrayList<Attention>();
    private int curPage = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    attentionArrayList.clear();
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    ExeProtocol.exe(
                            new RequestAttentionList(currUserid, String
                                    .valueOf(curPage)), new ProtocolResponse() {

//							@Override
//							public void finish(BaseHttpResponse bhr) {
//
//								ResponseAttentionList res = (ResponseAttentionList) bhr;
//								if (res.result.equals("550")) {
//									attentionArrayList.addAll(res.fansList);
//									adapter.setData(attentionArrayList);
//									if (attentionArrayList.size() >= res.num) {
//										isLastPage = true;
//									}
//								} else {
//								}
//								curPage += 1;
//								handler.sendEmptyMessage(4);
//							}

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseAttentionList res = (ResponseAttentionList) bhr;
                                    if (res.result.equals("550")) {
                                        attentionArrayList.addAll(res.fansList);
                                        adapter.setData(attentionArrayList);
                                        if (attentionArrayList.size() >= res.num) {
                                            isLastPage = true;
                                        }
                                    } else {
                                    }
                                    curPage += 1;
                                    handler.sendEmptyMessage(4);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                    handler.sendEmptyMessage(5);
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
                        refreshView.onHeaderRefreshComplete();
                    } else if (isFootRefresh) {
                        refreshView.onFooterRefreshComplete();
                    }
                    setListener();
                    break;
                case 5:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                default:
                    break;
            }
        }
    };
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fanslist);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        currUserid = this.getIntent().getStringExtra("userid");
        initWidget();
        waitingDialog = WaittingDialog.showDialog(mContext);
    }

    private void initWidget() {

        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        title = findViewById(R.id.title);
        title.setText(R.string.me_attention_text);
        initFansView();
    }

    private void initFansView() {

        fansList = findViewById(R.id.list);
        refreshView = findViewById(R.id.listview);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        adapter = new AttentionListAdapter(mContext);
        fansList.setAdapter(adapter);
        handler.sendEmptyMessage(0);
    }

    private void setListener() {
        fansList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                SocialDataManager.Instance().attention = attentionArrayList
                        .get(position);
                Attention attention = attentionArrayList.get(position);
                String username = attention.fusername;
                String uid = attention.followuid;
                startActivity(PersonalHomeActivity.buildIntent (getContext(), Integer.parseInt(uid), username, 0));

            }
        });
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

        if (!isLastPage) {
            handler.sendEmptyMessage(1);
            isFootRefresh = true;
        } else {
            refreshView.onFooterRefreshComplete();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

        handler.sendEmptyMessage(0);
        refreshView.setLastUpdated(ExeRefreshTime
                .lastRefreshTime("AttentionCenter"));
        isTopRefresh = true;
    }
}
