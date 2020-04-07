package com.iyuba.CET4bible.viewpager;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.MainActivity;
import com.iyuba.CET4bible.adapter.ListeningTestListAdapter;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.strategy.ContentMixStrategy;
import com.iyuba.CET4bible.strategy.ContentNonVipStrategy;
import com.iyuba.CET4bible.strategy.ContentStrategy;
import com.iyuba.CET4bible.strategy.ContentVipStrategy;
import com.iyuba.CET4bible.strategy.HolderType;
import com.iyuba.CET4bible.util.exam.DbExamListBean;
import com.iyuba.CET4bible.util.exam.ExamDataUtil;
import com.iyuba.CET4bible.util.exam.ExamListOp;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.base.BaseFragment;
import com.iyuba.configation.Constant;
import com.iyuba.headlinelibrary.data.model.StreamType;
import com.iyuba.headlinelibrary.data.model.StreamTypeInfo;
import com.iyuba.module.toolbox.RxUtil;
import com.iyuba.module.user.IyuUserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

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

//    AdInfoFlowUtil adInfoFlowUtil;
    SwipeRefreshLayout swipeRefreshLayout;

    private Disposable mTypeDisposable;

    ContentStrategy mContentStrategy;
    private RecyclerView.Adapter mWorkAdapter;
    int[] mStreamTypes;
    private int mStrategyCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listening_cet, container, false);
        if (containerVp != null) {
            containerVp.setObjectForPosition(view, 0);
        }
        getStreamType();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshExamList());

        mList = new ArrayList<>();
        adapter = new ListeningTestListAdapter(mContext, getActivity() instanceof MainActivity);
        adapter.setList(mList);
        recyclerView.setAdapter(adapter);

//        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
//            @Override
//            public void onADLoad(List ads) {
//                AdInfoFlowUtil.insertAD(mList, ads, adInfoFlowUtil);
//                adapter.notifyDataSetChanged();
//            }
//        });
//        adInfoFlowUtil.setSupportVideo(true);

        refreshListAdapter();

        if (ExamDataUtil.isFirstRequestData(mContext)) {
            swipeRefreshLayout.setRefreshing(true);
            refreshExamList();
        }
    }

    private void refreshExamList() {
        ExamDataUtil.requestList(Constant.APP_CONSTANT.TYPE(), list -> {
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

//        adInfoFlowUtil.setAdRequestSize(5).resetLastPosition().refreshAd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        adInfoFlowUtil.destroy();
    }

    private void checkContentStrategy(@Nullable Runnable noChangeAction) {

        new Handler().postDelayed(() -> {

            if (!isAdded()) {
                return;
            }
            int code = getStrategyCode();
//        if (mStrategyCode != code) {
            mStrategyCode = code;
            mContentStrategy = switchStrategy(mStrategyCode);

            mWorkAdapter = mContentStrategy.buildWorkAdapter(getActivity(), adapter);
            mContentStrategy.init(recyclerView, mWorkAdapter);
            if (mContentStrategy instanceof ContentNonVipStrategy) {
                ((ContentNonVipStrategy) mContentStrategy).loadAd(mWorkAdapter);
            }
        }, 500);

    }

    private int getStrategyCode() {
        if (IyuUserManager.getInstance().isVip()) {
            return ContentStrategy.Strategy.VIP;
        } else {
            if (mStreamTypes != null) {
                return AdBlocker.getInstance().shouldBlockAd() ? ContentStrategy.Strategy.VIP : ContentStrategy.Strategy.MIX;
            } else {
                return ContentStrategy.Strategy.VIP;
            }
        }
    }

    private ContentStrategy switchStrategy(int strategyCode) {
        switch (strategyCode) {
            case ContentStrategy.Strategy.VIP: {
                return new ContentVipStrategy();
            }
            case ContentStrategy.Strategy.MIX:
            default: {
                return new ContentMixStrategy(2, 6, mStreamTypes, HolderType.HOME);
            }
        }
    }

    private void getStreamType() {
        RxUtil.dispose(mTypeDisposable);
        mTypeDisposable = com.iyuba.headlinelibrary.data.DataManager.getInstance().getStreamType(Constant.APPID)
                .compose(RxUtil.<StreamTypeInfo>applySingleIoScheduler())
                .subscribe(streamTypeInfo -> {
                    mStreamTypes = streamTypeInfo.getTypes();
                    checkContentStrategy(() -> adapter.notifyDataSetChanged());

                }, throwable -> {
                    throwable.printStackTrace();
                    mStreamTypes = new int[]{StreamType.YOUDAO, StreamType.YOUDAO, StreamType.YOUDAO};
                    checkContentStrategy(null);

                });

    }
}
