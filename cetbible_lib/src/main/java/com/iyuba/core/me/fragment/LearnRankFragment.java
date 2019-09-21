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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.bean.StudyRankingInfoBean;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.LearnRankListAdapter;
import com.iyuba.core.me.pay.MD5;
import com.iyuba.core.me.sqlite.mode.LearnRankUser;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.StudyTimeTransformUtil;
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
 * 作者：renzhy on 17/1/9 10:59
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LearnRankFragment extends Fragment {

    CircularImageView userImage;
    TextView userImageText;
    TextView userName;
    TextView myUsername;
    TextView myRank;
    TextView myRankTime;
    TextView myRankEssay;
    TextView myRankWord;
    ListView rankListView;
    private Context mContext;
    private String uid;
    private String type;
    private String total;
    private String start;
    private String myTotalWord = "";
    private String myTotalEssay = "";
    private String myRanking = "";
    private String myTotalTime = "";
    private String myName = "";
    private List<LearnRankUser> rankUsers = new ArrayList<>();
    private LearnRankUser champion;
    private View rootView;
    private LearnRankListAdapter rankListAdapter;
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
                        rankListAdapter = new LearnRankListAdapter(mContext, rankUsers);
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
                            userName.setText(champion.getName());
                            myRank.setText(myRanking);
                            myRankTime.setText(StudyTimeTransformUtil.getFormat(myTotalTime));
                            myRankEssay.setText(myTotalEssay);
                            myRankWord.setText(myTotalWord);

                        } else {
                            userImageText.setBackgroundResource(R.drawable.rank_green);
                            userImageText.setText(firstChar);
                            myUsername.setText(myName);
                            userName.setText(champion.getName());
                            myRank.setText(myRanking);
                            myRankTime.setText(StudyTimeTransformUtil.getFormat(myTotalTime));
                            myRankEssay.setText(myTotalEssay);
                            myRankWord.setText(myTotalWord);
                        }
                    } else {
                        userImageText.setVisibility(View.INVISIBLE);
                        userImage.setVisibility(View.VISIBLE);
                        GitHubImageLoader.Instace(mContext).setRawPic(champion.getImgSrc(), userImage,
                                R.drawable.noavatar_small);
                        myUsername.setText(myName);
                        userName.setText(champion.getName());
                        myRank.setText(myRanking);
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(myTotalTime));
                        myRankEssay.setText(myTotalEssay);
                        myRankWord.setText(myTotalWord);
                    }
                    break;
                case 3:
//					rankListAdapter.clearList();
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

        myRank = root.findViewById(R.id.tv_user_rank);
        myRankTime = root.findViewById(R.id.tv_learn_ranking_total_time);
        myRankEssay = root.findViewById(R.id.tv_learn_ranking_total_essay);
        myRankWord = root.findViewById(R.id.tv_learn_ranking_total_words);

        rankListView = root.findViewById(R.id.learn_rank_list);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_learn_rank, container, false);
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

    public void updateLearnRank(int timeType) {
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
        String url = "http://daxue.iyuba.cn/ecollege/getStudyRanking.jsp?uid=" + uid +
                "&type=" + type +
                "&start=" + start +
                "&total=" + total +
                "&sign=" + MD5.getMD5ofStr(uid + type + start + total + Http.date());
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                StudyRankingInfoBean bean = null;
                try {
                    bean = new Gson().fromJson(response, StudyRankingInfoBean.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    return;
                }

                myTotalWord = bean.getTotalWord() + "";
                myTotalEssay = bean.getTotalEssay() + "";
                myRanking = bean.getMyranking() + "";
                myTotalTime = bean.getTotalTime() + "";
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
        String s = "http://m.iyuba.cn/i/getRanking.jsp?uid="+AccountManager.Instace(mContext).userId+"&appId="+ Constant.APPID+"&sign=" +
                com.iyuba.core.util.MD5.getMD5ofStr(uid + "ranking" + Constant.APPID)+"&topic=&rankingType=studying";
//                5085392&appId=148&sign=9ca1e2b36147a78f22049eefe0e2b2eb&topic=&rankingType=testing"
        ShareUtil.showShare(getContext(),"我在爱语吧学习排名"+myRanking,"","","","",s+"",s);

    }

}
