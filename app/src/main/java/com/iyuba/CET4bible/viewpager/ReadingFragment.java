package com.iyuba.CET4bible.viewpager;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.MainActivity;
import com.iyuba.CET4bible.adapter.FavoriteReadingAdapter;
import com.iyuba.CET4bible.sqlite.op.ReadingInfoOp;
import com.iyuba.CET4bible.strategy.ContentMixStrategy;
import com.iyuba.CET4bible.strategy.ContentNonVipStrategy;
import com.iyuba.CET4bible.strategy.ContentStrategy;
import com.iyuba.CET4bible.strategy.ContentVipStrategy;
import com.iyuba.CET4bible.strategy.HolderType;
import com.iyuba.CET4bible.util.AdInfoFlowUtil;
import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.base.BaseFragment;
import com.iyuba.base.util.L;
import com.iyuba.base.util.SimpleLineDividerDecoration;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.DataManager;
import com.iyuba.headlinelibrary.data.model.StreamType;
import com.iyuba.headlinelibrary.data.model.StreamTypeInfo;
import com.iyuba.module.toolbox.RxUtil;
import com.iyuba.module.user.IyuUserManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * ReadingFragment
 *
 * @author wayne
 * @date 2010/01/03
 */
public class ReadingFragment extends BaseFragment {
    RecyclerView recyclerView;
    FavoriteReadingAdapter adapter;
    ArrayList mList;

    AdInfoFlowUtil adInfoFlowUtil;

    private Disposable mTypeDisposable;

    ContentStrategy mContentStrategy;
    private RecyclerView.Adapter mWorkAdapter;
    int[] mStreamTypes;
    private int mStrategyCode;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fillinblank, container, false);
        if (containerVp!=null){
            containerVp.setObjectForPosition(view, 1);
        }
        getStreamType();
        return view ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext)

        );

        recyclerView.addItemDecoration(new SimpleLineDividerDecoration(mContext).setColor(R.color.darkgray));

        mList = new ArrayList<>();
        adapter = new FavoriteReadingAdapter(mContext, mList, getActivity() instanceof MainActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        recyclerView.setFocusable(false);
        mList.addAll(sort(new ReadingInfoOp(mContext).findPackName()));
        adapter.notifyDataSetChanged();

        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
            @Override
            public void onADLoad(List ads) {
                AdInfoFlowUtil.insertAD(mList, ads, adInfoFlowUtil);
                adapter.notifyDataSetChanged();
            }
        });
        adInfoFlowUtil.setAdRequestSize(10).refreshAd();
    }

    private List sort(List<String> readingList) {
        List tempList = new ArrayList();
        for (String s : readingList) {
            String year = s.substring(0, 7);
            year = year.replace("年", "");
            if (year.contains("6")) {
                year = year.replace("年", "").replace("6月", "06");
            }

            int number = Integer.parseInt(year);
            if (number > 201300) {
                tempList.add(s);
            }
        }

        readingList.removeAll(tempList);

        readingList.addAll(0, sort2(tempList));
        return readingList;
    }

    private List sort2(List tempList) {
        if (tempList.size() % 3 == 0) {
            List list = new ArrayList();

            for (int i = 0; i < tempList.size() / 3; i++) {
                String w = (String) tempList.get(i * 3);
                String w2 = (String) tempList.get(i * 3 + 1);
                String w3 = (String) tempList.get(i * 3 + 2);

                list.add(w3);
                list.add(w2);
                list.add(w);
            }
            tempList.clear();
            tempList.addAll(list);
        }
        return tempList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adInfoFlowUtil.destroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            DataManager.Instance().currentType = 5;
        }
    }

    private void checkContentStrategy(@Nullable Runnable noChangeAction) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

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
                .subscribe(new Consumer<StreamTypeInfo>() {
                    @Override
                    public void accept(StreamTypeInfo streamTypeInfo) throws Exception {
                        mStreamTypes = streamTypeInfo.getTypes();
                        checkContentStrategy(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        mStreamTypes = new int[]{StreamType.YOUDAO, StreamType.YOUDAO, StreamType.YOUDAO};
                        checkContentStrategy(null);

                    }
                });

    }
}
