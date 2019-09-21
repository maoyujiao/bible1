package com.iyuba.core.discover.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.me.Chatting;
import com.iyuba.core.discover.adapter.MutualAttentionAdapter;
import com.iyuba.core.discover.adapter.SearchResultAdapter;
import com.iyuba.core.discover.protocol.RequestMutualAttentionList;
import com.iyuba.core.discover.protocol.RequestSearchList;
import com.iyuba.core.discover.protocol.ResponseMutualAttentionList;
import com.iyuba.core.discover.protocol.ResponseSearchList;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.sqlite.mode.SearchItem;
import com.iyuba.core.sqlite.mode.me.MutualAttention;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;

/**
 * 找朋友界面 仅用于私信界面 后续会更改
 *
 * @author chentong
 * @version 1.0
 */
public class FindFriendListActivity extends BasisActivity implements
        OnScrollListener {
    private Context mContext;
    private ListView friendList, searchListView;
    private char letter = 'A';
    private Button backButton, button;
    private EditText editText;
    private CustomDialog waitingDialog;
    private String currPages = "1";
    private int curPage = 1;
    private int curSearchPage = 1;
    private String currSearchPages = "1";
    private MutualAttentionAdapter adapter;
    private String id;
    private ArrayList<MutualAttention> list = new ArrayList<MutualAttention>();
    private ArrayList<SearchItem> searchList = new ArrayList<SearchItem>();
    private String editContent;// 搜索内容
    private SearchResultAdapter adapterSearch;
    private LinearLayout layout_friend, layout_search, layout_nodata;
    private Boolean isSearchLastPage = false;
    private Boolean isFriendLastPage = false;
    private String currName;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curPage = 1;
                    currPages = String.valueOf(curPage);
                    adapter.clearList();
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    break;
                case 1:
                    // 联网获取日志列表，滑到底部点击更多进行加载
                    ClientSession.Instance().asynGetResponse(
                            new RequestMutualAttentionList(
                                    AccountManager.Instace(mContext).userId,
                                    currPages), new IResponseReceiver() {
                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {

                                    ResponseMutualAttentionList responseFansList = (ResponseMutualAttentionList) response;
                                    if (responseFansList.result.equals("570")) {
                                        list.addAll(responseFansList.list);
                                        adapter.addList(responseFansList.list);
                                        if (list.size() == responseFansList.num
                                                || list.size() > responseFansList.num) {
                                            isFriendLastPage = true;
                                        }
                                    } else {

                                    }
                                    curPage += 1;
                                    currPages = String.valueOf(curPage);
                                    handler.sendEmptyMessage(4);
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
                    // setListViewHeightBasedOnChildren(doings_commentlist);
                    break;
                case 7:
                    handler.sendEmptyMessage(3);
                    Intent intent = new Intent();
                    intent.putExtra("friendid", id);
                    intent.putExtra("currentname", currName);
                    intent.setClass(mContext, Chatting.class);
                    startActivity(intent);
                    break;
                case 10:
                    curSearchPage = 1;
                    currSearchPages = String.valueOf(curSearchPage);
                    // adapterSearch.clearList();
                    handler.sendEmptyMessage(2);
                    handler.sendEmptyMessage(11);
                    break;
                case 11:
                    // 在互相关注中搜索
                    // adapterSearch.clearList();
                    ClientSession.Instance().asynGetResponse(
                            new RequestSearchList(
                                    AccountManager.Instace(mContext).userId,
                                    editText.getText().toString(), "3",
                                    currSearchPages), new IResponseReceiver() {
                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {

                                    ResponseSearchList res = (ResponseSearchList) response;
                                    if (res.result.equals("591")) {
                                        searchList.clear();
                                        searchList.addAll(res.list);
                                        adapterSearch.addList(searchList);
                                        handler.sendEmptyMessage(14);
                                        if (res.firstPage == res.lastPage) {
                                            isSearchLastPage = true;
                                        }
                                    } else if (res.result.equals("590")) {
                                        // 无数据
                                        handler.sendEmptyMessage(12);
                                    }
                                    curSearchPage += 1;
                                    currSearchPages = String.valueOf(curSearchPage);
                                    handler.sendEmptyMessage(3);
                                    handler.sendEmptyMessage(13);
                                }

                            });
                    break;
                case 13:
                    adapterSearch.notifyDataSetChanged();
                    break;
                case 12:
                    layout_friend.setVisibility(View.GONE);
                    layout_search.setVisibility(View.GONE);
                    layout_nodata.setVisibility(View.VISIBLE);
                    break;
                case 14:
                    setOnClick();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_friendlist);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        initWidget();
        waitingDialog = WaittingDialog.showDialog(mContext);
        adapter = new MutualAttentionAdapter(mContext);
        friendList.setAdapter(adapter);
        friendList.setOnScrollListener(this);
        handler.sendEmptyMessage(0);
        setListener();
    }

    private void initWidget() {

        backButton = findViewById(R.id.friendlist_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        editText = findViewById(R.id.findfriend);
        friendList = findViewById(R.id.friend_list);
        button = findViewById(R.id.surebutton);// 确定按钮
        layout_friend = findViewById(R.id.layout_friend);
        layout_search = findViewById(R.id.layout_search);
        layout_nodata = findViewById(R.id.layout_nodata);
        searchListView = findViewById(R.id.friendsearch_list);
        layout_search.setVisibility(View.GONE);
        layout_nodata.setVisibility(View.GONE);
    }

    private void setListener() {

        friendList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long arg3) {

                SocialDataManager.Instance().mutualAttention = list
                        .get(position);
                id = list.get(position).followuid;
                currName = list.get(position).fusername;
                handler.sendEmptyMessage(7);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                editContent = editText.getText().toString();
                search();
            }

        });
    }

    private void search() {

        layout_friend.setVisibility(View.GONE);
        layout_search.setVisibility(View.VISIBLE);
        adapterSearch = new SearchResultAdapter(mContext);
        searchListView.setAdapter(adapterSearch);
        searchListView.setOnScrollListener(this);
        handler.sendEmptyMessage(10);
    }

    private void setOnClick() {
        searchListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                SocialDataManager.Instance().searchItem = searchList.get(arg2);
                id = searchList.get(arg2).uid;
                handler.sendEmptyMessage(3);
                Intent intent = new Intent();
                intent.putExtra("friendid", searchList.get(arg2).uid);
                intent.putExtra("search", "search");
                intent.setClass(mContext, Chatting.class);
                startActivity(intent);
                // handler.sendEmptyMessage(7);
            }
        });
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    if (layout_friend.getVisibility() == View.VISIBLE) {
                        if (!isFriendLastPage) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                    if (layout_search.getVisibility() == View.VISIBLE) {
                        if (!isSearchLastPage) {
                            handler.sendEmptyMessage(11);
                        }
                    }
                }
                break;

        }
    }

}
