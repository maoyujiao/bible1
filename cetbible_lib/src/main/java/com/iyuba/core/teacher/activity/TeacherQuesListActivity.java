package com.iyuba.core.teacher.activity;

import android.app.ListActivity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.base.util.SimpleNightMode;
import com.iyuba.biblelib.R;
import com.iyuba.core.bean.QuestionListBean;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.QuestionManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.adapter.QuestionListAdapter;
import com.iyuba.core.teacher.protocol.GetQuesListResponse;
import com.iyuba.core.teacher.protocol.GetTheQuesListRequest;
import com.iyuba.core.teacher.sqlite.mode.IyuTeacher;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.ExeRefreshTime;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;

import java.util.ArrayList;

public class TeacherQuesListActivity extends ListActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
    public int pageNum = 1;
    public IyuTeacher teacher = new IyuTeacher();
    boolean isLast = false;
    String uid = "928";
    ImageView teacher_img, teacher_star;
    TextView teacher_name;
    TextView teacher_desc, teacher_sc, teacher_lb, teacher_self;
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
                    //CustomToast.showToast(mContext, "没有问题");
                    break;

                case 7:
                    CustomToast.showToast(mContext, "已是最后一页");
                    break;
            }
        }
    };
    private ArrayList<QuestionListBean.QuestionDataBean> quesList = new ArrayList<QuestionListBean.QuestionDataBean>();
    private View sx;
    private SimpleNightMode simpleNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_teacherques_list);
        simpleNightMode = new SimpleNightMode(this);
        simpleNightMode.onResume();
        mContext = this;
        waitDialog = WaittingDialog.showDialog(mContext);
        //获取老师信息
        teacher = QuestionManager.getInstance().teacher;
        uid = teacher.uid;


        initWidget();
    }

    @Override
    protected void onDestroy() {
        simpleNightMode.close();
        super.onDestroy();
    }

    public void initWidget() {

        teacher_sc = findViewById(R.id.teacher_sc);
        teacher_lb = findViewById(R.id.teacher_lb);
        teacher_self = findViewById(R.id.teacher_self);
        teacher_sc.setText(" 擅长模块: " + teacher.category1);
        teacher_lb.setText(" 擅长课程类别: " + teacher.category2);
        teacher_self.setText(" 自我描述: " + teacher.tonedesc);

        teacher_img = findViewById(R.id.teacher_img);
        teacher_star = findViewById(R.id.teacher_star);
        teacher_name = findViewById(R.id.teacher_name);
        teacher_desc = findViewById(R.id.teacher_desc);
        teacher_name.setText(teacher.tname + " ");
        teacher_desc.setText(teacher.topedu + "  " + teacher.tcity);
        GitHubImageLoader.Instace(mContext).setPic(teacher.tlevel,
                teacher_star, 0);
        GitHubImageLoader.Instace(mContext).setCirclePic(teacher.timg,
                teacher_img, R.drawable.noavatar_small);


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

                Intent intent = new Intent();
                intent.setClass(mContext, QuezActivity.class);
                startActivity(intent);
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

    public void getHeaderData() {
        handler.sendEmptyMessage(2);

        ExeProtocol.exe(new GetTheQuesListRequest(uid, "4", 1), new ProtocolResponse() {

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
        ExeProtocol.exe(new GetTheQuesListRequest(uid, "4", pageNum), new ProtocolResponse() {

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
