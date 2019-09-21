package com.iyuba.core.discover.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.iyuba.base.util.SimpleNightMode;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.discover.adapter.FriendCircleFreshListAdapter;
import com.iyuba.core.discover.protocol.FreshListRequest;
import com.iyuba.core.discover.protocol.FreshListResponse;
import com.iyuba.core.discover.sqlite.mode.FreshContent;
import com.iyuba.core.listener.OnActivityGroupKeyDown;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.activity.ReplyDoing;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.activity.ShowLargePicActivity;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshListView;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

public class FriendCircFreshListActivity extends Activity implements OnScrollListener,
        OnActivityGroupKeyDown {

    int pageNumber = 1;
    boolean islast = false;
    private Context mContext;
    private FriendCircleFreshListAdapter freshListAdapter;
    private ProgressDialog wettingDialog;
    private ImageView iv_publish;
    private PullToRefreshListView mobClassListView;
    private ProgressBar mobClassListWaitBar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    islast = false;

                    wettingDialog.show();
                    ClientSession.Instance().asynGetResponse(
                            // APPID pageNumber pageCounts
                            // 获取首页新鲜事列表
                            new FreshListRequest(
                                    AccountManager.Instace(mContext).userId, "1"),
                            new IResponseReceiver() {

                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {
                                    FreshListResponse res = (FreshListResponse) response;

                                    if (res.freshList.size() > 0) {
                                        freshListAdapter.clearList();
                                        freshListAdapter.addList(res.freshList);
                                    }
                                    pageNumber = 2;
                                    handler.sendEmptyMessage(2);
                                    wettingDialog.dismiss();
                                }
                            }, null, null);
                    break;
                case 2:
                    freshListAdapter.notifyDataSetChanged();
                    mobClassListView.onRefreshComplete();
                    mobClassListWaitBar.setVisibility(View.GONE);
                    break;
                case 3:
                    wettingDialog.show();
                    ClientSession.Instance().asynGetResponse(
                            // APPID pageNumber pageCounts
                            // 获取首页新鲜事列表
                            new FreshListRequest(
                                    AccountManager.Instace(mContext).userId,
                                    pageNumber + ""), new IResponseReceiver() {

                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {
                                    FreshListResponse res = (FreshListResponse) response;

                                    if (res.freshList.size() > 0) {
                                        freshListAdapter.addList(res.freshList);

                                    } else {
                                        islast = true;
                                    }
                                    pageNumber = pageNumber + 1;
                                    handler.sendEmptyMessage(2);
                                    wettingDialog.dismiss();
                                }
                            }, null, null);
                    break;

            }
        }
    };
    private View backView, button_back;
    private OnItemClickListener oItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            if (((FreshContent) freshListAdapter.getItem(position - 1)).idtype.equals("doid")) {
                Intent intent = new Intent(mContext, ReplyDoing.class);
                intent.putExtra("doid",
                        ((FreshContent) freshListAdapter.getItem(position - 1)).id);
                startActivity(intent);
            } else if (((FreshContent) freshListAdapter.getItem(position - 1)).idtype.equals("picid")) {
                String pic = "";

                pic = Constant.PIC_ABLUM__ADD + ((FreshContent) freshListAdapter.getItem(position - 1)).image;

                Intent intent = new Intent(mContext, ShowLargePicActivity.class);
                intent.putExtra("pic", pic.replace("-s.jpg", ".jpg"));
                mContext.startActivity(intent);

            }

        }
    };
    private OnRefreshListener orfl = new OnRefreshListener() {
        @Override
        public void onRefresh() {

            if (NetWorkState.isConnectingToInternet()) {// 开始刷新

                handler.sendEmptyMessage(1);

            }
        }
    };
    private SimpleNightMode simpleNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_circle_list);
        simpleNightMode = new SimpleNightMode(this);
        simpleNightMode.onResume();
        mContext = this;
        handler.sendEmptyMessage(1);
        wettingDialog = new ProgressDialog(mContext);
        initView();
    }

    @Override
    protected void onDestroy() {
        simpleNightMode.close();
        super.onDestroy();
    }

    public void initView() {
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

        backView = findViewById(R.id.backlayout);
        backView.setBackgroundColor(Color.WHITE);
        iv_publish = findViewById(R.id.iv_publish_fresh);
        OnClickListener on = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("action", "1");
                intent.setClass(mContext, PublishMood.class);
                startActivity(intent);
            }
        };

        OnLongClickListener ol = new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {

                Intent intent = new Intent();
                intent.putExtra("action", "2");
                intent.setClass(mContext, PublishMood.class);
                startActivity(intent);
                return false;
            }
        };
        iv_publish.setOnClickListener(on);
        iv_publish.setOnLongClickListener(ol);
        mobClassListWaitBar = findViewById(R.id.courselist_waitbar);
        freshListAdapter = new FriendCircleFreshListAdapter(mContext);
        mobClassListView = findViewById(R.id.course_list);
        mobClassListView.setOnScrollListener(this);
        mobClassListView.setonRefreshListener(orfl);
        mobClassListView.setOnItemClickListener(oItemClickListener);

        if (freshListAdapter != null) {
            mobClassListView.setAdapter(freshListAdapter);
        }
        mobClassListWaitBar.setVisibility(View.GONE);

    }

    @Override
    public boolean onSubActivityKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                // 判断滚动到底部
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    Log.d("请求列表的页码222", "判断滚动到底部时");
                    if (islast) {
                        CustomToast.showToast(mContext, "已经加载全部");
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {


    }

}
