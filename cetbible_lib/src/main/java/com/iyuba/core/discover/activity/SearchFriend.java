/**
 *
 */
package com.iyuba.core.discover.activity;

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

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.adapter.discover.FindFriendsListAdapter;
import com.iyuba.core.discover.protocol.RequestSendLocation;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.activity.PersonalHome;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.friends.RequestGuessFriendsList;
import com.iyuba.core.protocol.friends.RequestNearFriendsList;
import com.iyuba.core.protocol.friends.RequestPublicAccountsList;
import com.iyuba.core.protocol.friends.RequestSameAppFriendsList;
import com.iyuba.core.protocol.friends.ResponseGuessFriendsList;
import com.iyuba.core.protocol.friends.ResponseNearFriendsList;
import com.iyuba.core.protocol.friends.ResponsePublicAccountsList;
import com.iyuba.core.protocol.friends.ResponseSameAppFriendsList;
import com.iyuba.core.sqlite.mode.me.FindFriends;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.ExeRefreshTime;
import com.iyuba.core.util.GetLocation;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;

import java.util.ArrayList;

/**
 * 找朋友 4种模式推送朋友
 *
 * @author chentong
 * @version 1.0
 */
public class SearchFriend extends BasisActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {
    private Context mContext;
    private Button near, guess, sameapp, back, accounts;
    private CustomDialog waitingDialog;
    private String x, y;// 经纬度
    private ListView friendList;
    private PullToRefreshView refreshView;// 刷新列表
    private boolean isAccountsLastPage = false;
    private boolean isSameAppLastPage = false;
    private boolean isNearLastPage = false;

    private ArrayList<FindFriends> findFriendsList = new ArrayList<FindFriends>();
    private FindFriendsListAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    CustomToast.showToast(mContext, R.string.action_fail);
                    break;
                case 1:
                    waitingDialog.show();
                    break;
                case 2:
                    waitingDialog.dismiss();
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 4:
                    CustomToast.showToast(mContext, R.string.action_fail);
                    break;
                case 5:
                    Intent intent = new Intent();
                    intent.setClass(mContext, PersonalHome.class);
                    startActivity(intent);
                    break;
                case 6:
                    CustomToast.showToast(mContext, R.string.social_add_all);
                    break;
                case 7:
                    handler.sendEmptyMessage(2);
                    handler.sendEmptyMessage(6);
                    refreshView.onHeaderRefreshComplete();
                    break;
                case 8:
                    handler.sendEmptyMessage(2);
                    handler.sendEmptyMessage(6);
                    refreshView.onFooterRefreshComplete();
                    break;
                case 9:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    Handler handler_guess = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    handler.sendEmptyMessage(1);
                    handler_guess.sendEmptyMessage(1);
                    break;
                case 1:
                    ExeProtocol.exe(
                            new RequestGuessFriendsList(AccountManager
                                    .Instace(mContext).userId),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseGuessFriendsList res = (ResponseGuessFriendsList) bhr;
                                    if (res.result.equals("591")) {
                                        findFriendsList.addAll(res.list);
                                        adapter.setData(findFriendsList, 3);
                                        handler.sendEmptyMessage(9);
                                    } else {
                                        handler.sendEmptyMessage(0);
                                    }
                                    handler_guess.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                }
                            });
                    break;
                case 2:
                    handler.sendEmptyMessage(2);
                    refreshView.onHeaderRefreshComplete();
                    refreshView.onFooterRefreshComplete();
                    break;
                default:
                    break;
            }
        }

    };
    private int currPages;
    Handler handler_sameApp = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    currPages = 1;
                    handler.sendEmptyMessage(1);
                    handler_sameApp.sendEmptyMessage(1);
                    break;
                case 1:
                    // 联网获取日志列表，滑到底部点击更多进行加载
                    ExeProtocol.exe(
                            new RequestSameAppFriendsList(AccountManager
                                    .Instace(mContext).userId, currPages),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseSameAppFriendsList res = (ResponseSameAppFriendsList) bhr;
                                    if (res.result.equals("261")) {
                                        findFriendsList.addAll(res.list);
                                        adapter.setData(findFriendsList, 2);
                                        handler.sendEmptyMessage(9);
                                        isSameAppLastPage = res.friendCounts <= findFriendsList
                                                .size();
                                    } else if (res.result.equals("262")) {
                                        handler.sendEmptyMessage(0);
                                    }
                                    currPages += 1;
                                    handler_sameApp.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                }
                            });
                    break;
                case 2:
                    handler.sendEmptyMessage(2);
                    refreshView.onHeaderRefreshComplete();
                    refreshView.onFooterRefreshComplete();
                    break;
                default:
                    break;
            }
        }
    };
    Handler handler_publicAccounts = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    currPages = 1;
                    handler.sendEmptyMessage(1);
                    handler_publicAccounts.sendEmptyMessage(1);
                    break;
                case 1:
                    // 联网获取日志列表，滑到底部点击更多进行加载
                    ExeProtocol.exe(
                            new RequestPublicAccountsList(AccountManager
                                    .Instace(mContext).userId, currPages),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponsePublicAccountsList res = (ResponsePublicAccountsList) bhr;
                                    if (res.result.equals("141")) {
                                        findFriendsList.addAll(res.list);
                                        adapter.setData(findFriendsList, 0);
                                        handler.sendEmptyMessage(9);
                                        isAccountsLastPage = res.pageNumber.equals(res.totalPage);
                                    } else if (res.result.equals("142")) {
                                        handler.sendEmptyMessage(0);
                                    }
                                    currPages += 1;
                                    handler_publicAccounts.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                }
                            });
                    break;
                case 2:
                    handler.sendEmptyMessage(2);
                    refreshView.onHeaderRefreshComplete();
                    refreshView.onFooterRefreshComplete();
                    break;
                default:
                    break;
            }
        }
    };
    Handler handler_near = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    currPages = 1;
                    handler.sendEmptyMessage(1);
                    handler_near.sendEmptyMessage(1);
                    break;
                case 1:
                    // 获取附近好友
                    ExeProtocol.exe(
                            new RequestNearFriendsList(AccountManager
                                    .Instace(mContext).userId, currPages, x, y),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseNearFriendsList res = (ResponseNearFriendsList) bhr;
                                    if (res.result.equals("711")) {
                                        findFriendsList.addAll(res.list);
                                        adapter.setData(findFriendsList, 1);
                                        handler.sendEmptyMessage(9);
                                        isNearLastPage = res.total <= findFriendsList.size();
                                    } else if (res.result.equals("710")) {
                                        handler.sendEmptyMessage(0);
                                    }
                                    currPages += 1;
                                    handler.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                }
                            });
                    break;
                case 2:
                    handler.sendEmptyMessage(2);
                    refreshView.onHeaderRefreshComplete();
                    refreshView.onFooterRefreshComplete();
                    break;
                case 3:
                    // 上传当前位置
                    ExeProtocol.exe(
                            new RequestSendLocation(AccountManager
                                    .Instace(mContext).userId, x, y),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };
    private int whichView = 0;
    private OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.button_back) {
                onBackPressed();
            } else if (id == R.id.publicaccounts) {
                whichView = 3;
                setButtonBackGround();
                onHeaderRefresh(refreshView);
            } else if (id == R.id.sameapp) {
                whichView = 2;
                setButtonBackGround();
                onHeaderRefresh(refreshView);
            } else if (id == R.id.near) {
                whichView = 1;
                setButtonBackGround();
                onHeaderRefresh(refreshView);
            } else if (id == R.id.guess) {
                whichView = 0;
                setButtonBackGround();
                onHeaderRefresh(refreshView);
            } else {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriends);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        waitingDialog = WaittingDialog.showDialog(mContext);
        initLocation();
        initWidget();
    }

    private void initLocation() {
        String locationPos[] = GetLocation.getInstance(mContext).getLocation();
        y = locationPos[0];
        x = locationPos[1];
    }

    private void initWidget() {

        back = findViewById(R.id.button_back);
        near = findViewById(R.id.near);
        guess = findViewById(R.id.guess);
        sameapp = findViewById(R.id.sameapp);
        accounts = findViewById(R.id.publicaccounts);
        refreshView = findViewById(R.id.listview);
        friendList = findViewById(R.id.friendlist);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        adapter = new FindFriendsListAdapter(mContext);
        friendList.setAdapter(adapter);
        setClickListener();
        whichView = 0;
        setButtonBackGround();
        onHeaderRefresh(refreshView);
    }

    private void setButtonBackGround() {
        near.setBackgroundResource(R.drawable.near);
        guess.setBackgroundResource(R.drawable.guess);
        accounts.setBackgroundResource(R.drawable.publicaccounts);
        sameapp.setBackgroundResource(R.drawable.sameapp);
        switch (whichView) {
            case 3:
                accounts.setBackgroundResource(R.drawable.publicaccounts_press);
                break;
            case 1:
                near.setBackgroundResource(R.drawable.near_press);
                break;
            case 2:
                sameapp.setBackgroundResource(R.drawable.sameapp_press);
                break;
            case 0:
                guess.setBackgroundResource(R.drawable.guess_press);
                break;
            default:
                break;
        }
        isAccountsLastPage = false;
        isNearLastPage = false;
        isSameAppLastPage = false;
        friendList.setSelection(0);
    }

    private void setClickListener() {
        back.setOnClickListener(ocl);
        accounts.setOnClickListener(ocl);
        near.setOnClickListener(ocl);
        guess.setOnClickListener(ocl);
        sameapp.setOnClickListener(ocl);
        friendList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                SocialDataManager.Instance().userid = findFriendsList.get(arg2).userid;
                handler.sendEmptyMessage(5);// 进入个人空间
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
        isAccountsLastPage = false;
        isSameAppLastPage = false;
        isNearLastPage = false;
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

        handler.sendEmptyMessage(1);
        if (whichView == 3) {
            if (!isAccountsLastPage) {
                handler_publicAccounts.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(8);
            }
        } else if (whichView == 1) {
            if (!isNearLastPage) {
                handler_near.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(8);
            }
        } else if (whichView == 2) {
            if (!isSameAppLastPage) {
                handler_sameApp.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(8);
            }
        } else if (whichView == 0) {
            handler.sendEmptyMessage(8);
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

        findFriendsList.clear();
        handler.sendEmptyMessage(1);
        refreshView.setLastUpdated(ExeRefreshTime
                .lastRefreshTime("SearchFriend"));
        if (whichView == 3) {
            if (!isAccountsLastPage) {
                handler_publicAccounts.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(7);
            }
        } else if (whichView == 1) {
            if (!isNearLastPage) {
                handler_near.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(7);
            }
        } else if (whichView == 2) {
            if (!isSameAppLastPage) {
                handler_sameApp.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(7);
            }
        } else if (whichView == 0) {
            handler_guess.sendEmptyMessage(0);
        }
    }
}
