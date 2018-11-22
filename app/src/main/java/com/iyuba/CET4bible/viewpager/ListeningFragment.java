package com.iyuba.CET4bible.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ListeningTestListAdapter;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.util.AdInfoFlowUtil;
import com.iyuba.CET4bible.util.exam.DbExamListBean;
import com.iyuba.CET4bible.util.exam.ExamDataUtil;
import com.iyuba.CET4bible.util.exam.ExamListBean;
import com.iyuba.CET4bible.util.exam.ExamListOp;
import com.iyuba.base.BaseFragment;
import com.iyuba.base.util.SimpleLineDividerDecoration;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ListeningFragment
 *
 * @author wayne
 * @date 2010/01/03
 */
public class ListeningFragment extends BaseFragment {
    RecyclerView recyclerView;
    ListeningTestListAdapter adapter;
    ArrayList mList;

    AdInfoFlowUtil adInfoFlowUtil;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listening_cet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new SimpleLineDividerDecoration(mContext));
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshExamList();
            }
        });
        mList = new ArrayList<>();
        adapter = new ListeningTestListAdapter(mContext);
        adapter.setList(mList);
        recyclerView.setAdapter(adapter);

        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
            @Override
            public void onADLoad(List ads) {
                AdInfoFlowUtil.insertAD(mList, ads, adInfoFlowUtil);
                adapter.notifyDataSetChanged();
            }
        });
        adInfoFlowUtil.setSupportVideo(true);

        refreshListAdapter();

        if (ExamDataUtil.isFirstRequestData(mContext)) {
            swipeRefreshLayout.setRefreshing(true);
            refreshExamList();
        }
    }

    private void refreshExamList() {
        ExamDataUtil.requestList(Constant.APP_CONSTANT.TYPE(), new ExamDataUtil.ListCallback() {
            @Override
            public void onLoadData(List<ExamListBean.DataBean> list) {
                swipeRefreshLayout.setRefreshing(false);
                if (list != null && list.size() > 0) {
                    try {
                        ExamDataUtil.writeListData2DB(mContext, list);
                        ExamDataUtil.setFirstRequestData(mContext, false);
                        refreshListAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showLong("题库加载失败...");
                    }
                } else {
                    showLong("题目加载失败");
                }
            }
        });
    }

    private void refreshListAdapter() {
        ExamListOp listOp = new ExamListOp(mContext);
        List<DbExamListBean> examListBeans = listOp.findAll();
        mList.clear();
        Map<String, String> images = new HashMap<>();
        for (DbExamListBean examListBean : examListBeans) {
            mList.add(examListBean.year);
            images.put(examListBean.year, examListBean.image);
        }
        if (mList.size() == 0) {
            mList.addAll(Arrays.asList(ListenDataManager.getTestNewType()));
            adapter.setImageMap();
        } else {
            adapter.setImageUrls(images);
        }

        adapter.notifyDataSetChanged();

        adInfoFlowUtil.setAdRequestSize(5).resetLastPosition().refreshAd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adInfoFlowUtil.destroy();
    }
}
