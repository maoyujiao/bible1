package com.iyuba.CET4bible.viewpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.BlogActivity;
import com.iyuba.CET4bible.adapter.BlogListAdapter;
import com.iyuba.CET4bible.manager.BlogDataManager;
import com.iyuba.CET4bible.protocol.info.BlogRequest;
import com.iyuba.CET4bible.protocol.info.BlogResponse;
import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.iyuba.CET4bible.sqlite.op.BlogOp;
import com.iyuba.CET4bible.util.AdInfoFlowUtil;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.umeng.analytics.MobclickAgent;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment implements OnScrollListener,
        OnItemClickListener {
    private String[] CATE_IDS;
    private static final String[] CATE_IDS_CET4 = {"13,5,25,31", "13", "5", "25", "31"};
    private static final String[] CATE_IDS_CET6 = {"65,69,84,87", "65", "69", "84", "87"};
    private static String[] CATE_STRINGS;
    private int mContent;
    private Context mContext;
    private ListView blogListView;
    private BlogListAdapter blogAdapter;
    private ArrayList blogs = new ArrayList<>();
    private BlogOp blogOp;
    private View root;
    private int page;
    private String category;
    private CustomDialog waitting;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    handler.sendEmptyMessage(1);
                    ExeProtocol.exe(new BlogRequest(CATE_IDS[mContent], page),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    BlogResponse response = (BlogResponse) bhr;
                                    if (response.blogs != null
                                            && response.blogs.size() != 0) {
                                        blogOp.saveData(response.blogs);
                                    }
                                    handler.sendEmptyMessage(2);
                                    getDataFromDataBase();
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(3);
                                    handler.sendEmptyMessage(2);
                                    getDataFromDataBase();
                                }
                            });
                    break;
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
                    blogAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    adInfoFlowUtil.refreshAd();
                    break;
                default:
                    break;
            }
        }
    };



    public static BlogFragment newInstance(Context context, int content) {
        BlogFragment fragment = new BlogFragment();
        fragment.mContent = content;
        fragment.mContext = context;
        return fragment;
    }

    private AdInfoFlowUtil adInfoFlowUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CATE_IDS = BuildConfig.isCET4 ? CATE_IDS_CET4 : CATE_IDS_CET6;

        root = inflater.inflate(R.layout.blog_fragment, container, false);
        waitting = WaittingDialog.showDialog(mContext);
        CATE_STRINGS = getResources().getStringArray(R.array.blog_title);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        blogOp = new BlogOp(mContext);
        blogListView = root.findViewById(R.id.list);
        blogAdapter = new BlogListAdapter(mContext, blogs);
        blogListView.setAdapter(blogAdapter);
        blogListView.setOnScrollListener(this);
        blogListView.setOnItemClickListener(this);

        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
            @Override
            public void onADLoad(List ads) {
                AdInfoFlowUtil.insertAD(blogs, ads, adInfoFlowUtil);
                handler.sendEmptyMessage(4);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adInfoFlowUtil.destroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getUserVisibleHint() && isVisibleToUser) {
            category = CATE_IDS[mContent];
            page = 1;
            blogs.clear();
            handler.sendEmptyMessage(4);
            handler.sendEmptyMessage(0);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    private void getDataFromDataBase() {
        if (category.equals(CATE_IDS[0])) {
            blogs.addAll(blogOp.selectData(15, page * 15 - 15));
        } else {
            blogs.addAll(blogOp.selectData(category, 15, page * 15 - 15));
        }
        page++;
        handler.sendEmptyMessage(4);
        //广告
        handler.sendEmptyMessage(5);
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_IDLE:
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    handler.sendEmptyMessage(0);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {

        if (blogs.get(pos) instanceof NativeResponse) {
            ((NativeResponse) blogs.get(pos)).handleClick(view);
        }
        if (blogs.get(pos) instanceof Blog) {
            Intent intent = new Intent(mContext, BlogActivity.class);
            intent.putExtra("title", CATE_STRINGS[mContent]);
            BlogDataManager.Instance().blog = (Blog) blogs.get(pos);
            startActivity(intent);
        }
    }
}
