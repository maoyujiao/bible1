package com.iyuba.core.me.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.bean.TestRankingInfoBean;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.TestRankListAdapter;
import com.iyuba.core.me.sqlite.mode.TestRankUser;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.circularimageview.CircularImageView;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * Created by Ivot on 2016/12/29.
 */

public class TestRankFragment extends Fragment {
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
    private List<TestRankUser> rankUsers = new ArrayList<>();
    private TestRankUser champion;
    private View rootView;
    private TestRankListAdapter rankListAdapter;
    private Pattern p;
    private Matcher m;
    private CustomDialog waitDialog;
    private View listFooter;
    private boolean scorllable = true;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
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
                        rankListAdapter = new TestRankListAdapter(mContext, rankUsers);
                        rankListView.setAdapter(rankListAdapter);
                    } else if (champion.getRanking().equals("1")) {
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
                            userName.setText(champion.getName()==""?champion.getUid():champion.getName());
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
                        userName.setText(champion.getName()==""?champion.getUid():champion.getName());
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
        String url = "http://daxue.iyuba.cn/ecollege/getTestRanking.jsp?" +
                "uid=" + uid +
                "&type=" + type +
                "&start=" + start +
                "&total=" + total +
                "&sign=" + MD5.getMD5ofStr(uid + type + start + total + Http.date());

        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                TestRankingInfoBean bean = null;
                try {
                    bean = new Gson().fromJson(response, TestRankingInfoBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    return;
                }
                myTotalTest = bean.getTotalTest() + "";
                myRanking = bean.getMyranking() + "";
                myTotalRight = bean.getTotalRight() + "";
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

                if (champion.getRanking().equals("1"))
                    handler.sendEmptyMessage(2);
            }

            @Override
            public void onError(Call call, Exception e) {
                waitDialog.dismiss();
            }
        });
    }

    public void share(){
        String s = "http://m.iyuba.cn/i/getRanking.jsp?uid="+ AccountManager.Instace(mContext).userId+"&appId="+Constant.APPID+"&sign=" +
                MD5.getMD5ofStr(uid + "ranking" + Constant.APPID)+"&topic=&rankingType=testing";
//                5085392&appId=148&sign=9ca1e2b36147a78f22049eefe0e2b2eb&topic=&rankingType=testing"
        ShareUtil.showShare(getContext(),"我在爱语吧测试排名"+myRanking,"","","","",s,s);

    }

    public Bitmap shotActivityNoBar(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }


}
