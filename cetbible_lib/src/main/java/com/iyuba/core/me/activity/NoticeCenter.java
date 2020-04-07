package com.iyuba.core.me.activity;

/**
 * 通知中心界面
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
import com.iyuba.core.me.adapter.FanListAdapter;
import com.iyuba.core.me.sqlite.mode.Fans;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestNotificationInfo;
import com.iyuba.core.protocol.message.ResponseNotificationInfo;
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

import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;

public class NoticeCenter extends BasisActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {
    private String currUserid;
    private Button backButton;
    private Context mContext;
    private CustomDialog waitingDialog;
    private Boolean isLastPage = false;
    private Boolean isTopRefresh = false;
    private Boolean isFootRefresh = false;
    private ListView fansList;// 新闻列表
    private PullToRefreshView refreshView;// 刷新列表
    private FanListAdapter adapter;
    private ArrayList<Fans> fansArrayList = new ArrayList<Fans>();
    private int curPage = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    fansArrayList.clear();
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    ExeProtocol.exe(
                            new RequestNotificationInfo(currUserid, String
                                    .valueOf(curPage), "system,app,follow"),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseNotificationInfo res = (ResponseNotificationInfo) bhr;
                                    if (res.result.equals("631")) {
                                        fansArrayList.addAll(res.list);
                                        adapter.setData(fansArrayList);
                                        if (res.firstPage == res.nextPage) {
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
        title.setText(R.string.me_notification_text);
        initFansView();
    }

    private void initFansView() {

        fansList = findViewById(R.id.list);
        refreshView = findViewById(R.id.listview);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        adapter = new FanListAdapter(mContext);
        fansList.setAdapter(adapter);
        handler.sendEmptyMessage(0);
    }

    private void setListener() {
        fansList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent();
                SocialDataManager.Instance().userid = fansArrayList
                        .get(position).uid;
                String uid = fansArrayList.get(position).uid;
                String username = fansArrayList.get(position).username;
                mContext.startActivity(PersonalHomeActivity.buildIntent(mContext,
                        Integer.parseInt(uid),
                        username,0));
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
            refreshView.onFooterRefreshComplete();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

        handler.sendEmptyMessage(0);
        refreshView.setLastUpdated(ExeRefreshTime
                .lastRefreshTime("NoticeCenter"));
        isTopRefresh = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
