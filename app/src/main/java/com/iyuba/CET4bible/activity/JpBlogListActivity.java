package com.iyuba.CET4bible.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.JpBlogListAdapter;
import com.iyuba.CET4bible.bean.JpBlogListBean;
import com.iyuba.CET4bible.protocol.info.JpBlogListRequest;
import com.iyuba.CET4bible.protocol.info.JpBlogListResponse;
import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.iyuba.CET4bible.sqlite.op.BlogOp;
import com.iyuba.CET4bible.util.AdInfoFlowUtil;
import com.iyuba.base.BaseActivity;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.ArrayList;
import java.util.List;

public class JpBlogListActivity extends BaseActivity implements AbsListView.OnScrollListener {
    private ListView listView;
    private ArrayList mDataList = new ArrayList<>();
    private JpBlogListAdapter listAdapter;
    private CustomDialog waitting;
    private int page = 1;

    private AdInfoFlowUtil adInfoFlowUtil;
    private BlogOp blogOp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jp_blog_list);
        listView = findViewById(R.id.listview);

        blogOp = new BlogOp(mContext);
        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
            @Override
            public void onADLoad(List ads) {
                AdInfoFlowUtil.insertAD(mDataList, ads, adInfoFlowUtil);
                listAdapter.notifyDataSetChanged();
            }
        }).setSupportVideo(true);

        listAdapter = new JpBlogListAdapter(this, mDataList);
        listView.setAdapter(listAdapter);
        waitting = WaittingDialog.showDialog(this);

        handler.sendEmptyMessage(0);


        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDataList.get(position) instanceof Blog) {
                    Intent intent = new Intent(mContext, JpBlogActivity.class);
                    intent.putExtra("blog", (Blog) listAdapter.getItem(position));
                    startActivity(intent);
                } else if (mDataList.get(position) instanceof NativeResponse) {
                    NativeResponse bean = (NativeResponse) listAdapter.getItem(position);
                    bean.handleClick(view);
                }
            }
        });
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    handler.sendEmptyMessage(1);
                    ExeProtocol.exe(new JpBlogListRequest(Constant.APP_CONSTANT.BLOG_ACCOUNT_ID(), page + ""), new ProtocolResponse() {
                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            handler.sendEmptyMessage(2);
                            JpBlogListResponse response = (JpBlogListResponse) bhr;
                            if (response == null || response.bean == null || response.bean.getData() == null) {
                                return;
                            }
                            List<JpBlogListBean.DataBean> list = response.bean.getData();
                            blogOp.saveData(toBlog(list));
                            handler.sendEmptyMessage(5);
                        }

                        @Override
                        public void error() {
                            handler.sendEmptyMessage(2);
                            handler.sendEmptyMessage(5);
                        }
                    });
                case 1:
                    waitting.show();
                    break;
                case 2:
                    waitting.dismiss();
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 4:
                    adInfoFlowUtil.refreshAd();
                    listAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    getDataFromDatabase();
                    break;
                default:
                    break;
            }
        }
    };

    private ArrayList<Blog> toBlog(List<JpBlogListBean.DataBean> list) {
        ArrayList<Blog> blogArrayList = new ArrayList<>();
        for (JpBlogListBean.DataBean dataBean : list) {
            Blog blog = new Blog();
            blog.createtime = DateFormat.format("yyyy-MM-dd",
                    Long.parseLong(dataBean.getDateline()) * 1000).toString();
            blog.readcount = dataBean.getViewnum();
            blog.title = dataBean.getSubject();
            blog.id = Integer.parseInt(dataBean.getBlogid());
            blog.url = dataBean.getPic();
            blogArrayList.add(blog);
        }
        return blogArrayList;
    }

    private void getDataFromDatabase() {
        mDataList.addAll(blogOp.selectData(15, page * 15 - 15));
        //广告
        handler.sendEmptyMessage(4);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    page += 1;
                    handler.sendEmptyMessage(0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adInfoFlowUtil.destroy();
    }
}
