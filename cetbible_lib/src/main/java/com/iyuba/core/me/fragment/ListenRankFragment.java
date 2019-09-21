package com.iyuba.core.me.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.bean.ListenRankingInfoBean;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.ListenRankListAdapter;
import com.iyuba.core.me.sqlite.mode.ListenRankUser;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.circularimageview.CircularImageView;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.Platform;
import okhttp3.Call;

/**
 * Created by 15730 on 2018/4/17.
 */

public class ListenRankFragment extends Fragment {
    CircularImageView userImage;
    TextView userImageText;
    TextView userName;
    TextView myUsername;
    TextView tvRank;
    TextView tvTotalTest;
    TextView tvTotalRight;
    TextView tvRightRate;
    ListView rankListView;
    private Context mContext;
    private String uid;
    private String type;
    private String total;
    private String start;
    private String myTotalTest = "";
    private String myRanking = "";
    private String myTotalRight = "";
    private String myName = "";
    private List<ListenRankUser> rankUsers = new ArrayList<>();
    private ListenRankUser champion;
    private View rootView;
    private ListenRankListAdapter rankListAdapter;
    private Pattern p;
    private Matcher m;
    private CustomDialog waitDialog;
    private View listFooter;
    private boolean scorllable = true;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitDialog.show();
                    if (rankUsers.size() == 0) {
                        start = "0";
                    } else {
                        start = String.valueOf(rankListAdapter.getCount());
                    }

                    requestRankData();
                    break;
                case 1:
                    if (rankListAdapter == null) {
                        rankListAdapter = new ListenRankListAdapter(mContext, rankUsers);
                        rankListView.setAdapter(rankListAdapter);
                    } else if ((champion.getRanking()+"").equals("1")) {
                        rankListAdapter.resetList(rankUsers);
                    } else {
                        rankListAdapter.addList(rankUsers);
                    }
                    waitDialog.dismiss();
                    break;
                case 2:
                    String firstChar = getFirstChar(champion.getName());
                    if (champion.getImgSrc().equals("http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg")) {
                        userImage.setVisibility(View.INVISIBLE);
                        userImageText.setVisibility(View.VISIBLE);
                        p = Pattern.compile("[a-zA-Z]");
                        m = p.matcher(firstChar);
                        if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                            userImageText.setBackgroundResource(R.drawable.rank_blue);
                            userImageText.setText(firstChar);
                            myUsername.setText(myName);
                            userName.setText(champion.getName());
                            tvRank.setText(myRanking);
                            tvTotalTest.setText(myTotalTest);
                            tvTotalRight.setText(myTotalRight);
                            if (Integer.parseInt(myTotalTest) == 0) {
                                tvRightRate.setText("0%");
                            } else {
                                tvRightRate.setText(Integer.parseInt(myTotalRight) * 100 / Integer.parseInt(myTotalTest) + "%");
                            }
                        } else {
                            userImageText.setBackgroundResource(R.drawable.rank_green);
                            userImageText.setText(firstChar);
                            myUsername.setText(myName);
                            userName.setText(champion.getName());
                            tvRank.setText(myRanking);
                            tvTotalTest.setText(myTotalTest);
                            tvTotalRight.setText(myTotalRight);
                            if (Integer.parseInt(myTotalTest) == 0) {
                                tvRightRate.setText("0%");
                            } else {
                                tvRightRate.setText(Integer.parseInt(myTotalRight) * 100 / Integer.parseInt(myTotalTest) + "%");
                            }
                        }
                    } else {
                        userImageText.setVisibility(View.INVISIBLE);
                        userImage.setVisibility(View.VISIBLE);
                        GitHubImageLoader.Instace(mContext).setRawPic(champion.getImgSrc(), userImage,
                                R.drawable.noavatar_small);
                        myUsername.setText(myName);
                        userName.setText(champion.getName());
                        tvRank.setText(myRanking);
                        tvTotalTest.setText(myTotalTest);
                        tvTotalRight.setText(myTotalRight);
                        if (Integer.parseInt(myTotalTest) == 0) {
                            tvRightRate.setText("0%");
                        } else {
                            tvRightRate.setText(Integer.parseInt(myTotalRight) * 100 / Integer.parseInt(myTotalTest) + "%");
                        }
                    }
                    break;
                case 3:
//                    rankListAdapter.clearList();
                    if (rankUsers != null) {
                        rankUsers.clear();
                    }
                    break;
            }
        }
    };

    public void share(){
        String s = "http://m.iyuba.cn/i/getRanking.jsp?uid="+ AccountManager.Instace(mContext).userId+"&appId="+ Constant.APPID+"&sign=" +
                MD5.getMD5ofStr(uid + "ranking" + Constant.APPID)+"&topic=&rankingType=listening";
//                5085392&appId=148&sign=9ca1e2b36147a78f22049eefe0e2b2eb&topic=&rankingType=testing"
        ShareUtil.showShare(getContext(),"我在爱语吧听力排名"+myRanking,"","","","",s,s);

    }
    public void findViews(View root) {
        userImage = root.findViewById(R.id.rank_user_image);
        userImageText = root.findViewById(R.id.rank_user_image_text);
        userName = root.findViewById(R.id.rank_user_name);
        myUsername = root.findViewById(R.id.username);

        tvRank = root.findViewById(R.id.tv_user_rank);
        tvTotalTest = root.findViewById(R.id.tv_test_total_num);
        tvTotalRight = root.findViewById(R.id.tv_test_right_num);
        tvRightRate = root.findViewById(R.id.tv_test_right_rate);

        rankListView = root.findViewById(R.id.test_rank_list);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_test_rank, container, false);
        findViews(rootView);

        waitDialog = WaittingDialog.showDialog(getContext());


        uid = AccountManager.Instace(mContext).userId;
        type = "D";
        total = "10";
        start = "0";

        listFooter = inflater.inflate(R.layout.comment_footer, null);
        rankListView.addFooterView(listFooter);

        handler.sendEmptyMessage(0);
        ShareUtil.setOnShareStateListener(new ShareUtil.OnShareStateListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(mContext,"分享失败。",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(mContext,"分享失败。",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(mContext,"分享已取消",
                        Toast.LENGTH_SHORT).show();
            }
        });

        rankListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            // 当comment不为空且comment.size()不为0且没有完全加载
                            if (scorllable)
                                handler.sendEmptyMessage(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        return rootView;
    }

    public void updateTestRank(int timeType) {
        handler.sendEmptyMessage(3);
        switch (timeType) {
            case 0:
                type = "D";
                total = "10";
                start = "0";
                handler.sendEmptyMessage(0);
                break;
            case 1:
                type = "W";
                total = "10";
                start = "0";
                handler.sendEmptyMessage(0);
                break;
            case 2:
                type = "M";
                total = "10";
                start = "0";
                handler.sendEmptyMessage(0);
                break;
            default:
                break;
        }
    }

    private String getFirstChar(String name) {
        String subString;
        for (int i = 0; i < name.length(); i++) {
            subString = name.substring(i, i + 1);

            p = Pattern.compile("[0-9]*");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是数字", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[a-zA-Z]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[\u4e00-\u9fa5]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是汉字", Toast.LENGTH_SHORT).show();
                return subString;
            }
        }

        return "A";
    }

    private void requestRankData() {
        String url = "http://daxue.iyuba.cn/ecollege/getStudyRanking.jsp?" +
                "uid=" + uid +
                "&type=" + type +
                "&total=" + total +
                "&sign=" + MD5.getMD5ofStr(uid + type + start + total + Http.date()) +
                "&start=" + start +
                "&mode=listening";

        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                ListenRankingInfoBean bean = null;
                try {
                    bean = new Gson().fromJson(response, ListenRankingInfoBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    return;
                }
                myTotalTest = bean.getResult() + "";
                myRanking = bean.getMyranking() + "";
                myTotalRight = bean.getMyid() + "";
                myName = TouristUtil.isTourist() ? AccountManager.Instace(mContext).getId() :
                        AccountManager.Instace(mContext).getUserName();
                rankUsers = bean.getData();
                champion = rankUsers.get(0);

                if (rankUsers.size() < 10) {
                    listFooter.setVisibility(View.GONE);
                    scorllable = false;
                } else {
                    listFooter.setVisibility(View.VISIBLE);
                }

                handler.sendEmptyMessage(1);

                if (champion.getRanking()==1)
                    handler.sendEmptyMessage(2);
            }

            @Override
            public void onError(Call call, Exception e) {
                waitDialog.dismiss();
            }
        });
    }



}
