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
import com.iyuba.CET4bible.adapter.FavoriteTranslateAdapter;
import com.iyuba.CET4bible.sqlite.mode.Write;
import com.iyuba.CET4bible.sqlite.op.TranslateOp;
import com.iyuba.CET4bible.sqlite.op.WriteOp;
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
 * WriteFragment
 *
 * @author wayne
 * @date 2010/01/03
 */
public class WriteFragment extends BaseFragment {
    RecyclerView recyclerView;
    FavoriteTranslateAdapter adapter;
    ArrayList mList;

    AdInfoFlowUtil adInfoFlowUtil;
    public static final int TRANSLATE = 1;
    public static final int WRITE = 2;

    int type  ;

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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new SimpleLineDividerDecoration(mContext).setColor(R.color.darkgray));

        type = getArguments().getInt("type", 1);

        mList = new ArrayList<>();
        //  翻译
        if (type == TRANSLATE) {
            mList.addAll(new TranslateOp(mContext).selectData());
        } else {
            // 写作
            mList.addAll(new WriteOp(mContext).selectData());
        }


        List<Write> writes = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            Write w = (Write) mList.get(i);
            int year = Integer.parseInt(w.num);

            if (type == TRANSLATE) {
                if (year > 20150000) {
                    writes.add(w);
                }
            } else {
                if (year > 20160000) {
                    writes.add(w);
                }
            }
        }
        mList.removeAll(writes);
        mList.addAll(0, sort(writes));

        adapter = new FavoriteTranslateAdapter(mContext, mList, getActivity() instanceof MainActivity);
        adapter.setWrite(type != 1);

        recyclerView.setAdapter(adapter);

//        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
//            @Override
//            public void onADLoad(List ads) {
//                AdInfoFlowUtil.insertAD(mList, ads, adInfoFlowUtil);
//                adapter.notifyDataSetChanged();
//            }
//        });
//        if (type == WRITE) {
//            adInfoFlowUtil.setSupportVideo(true);
//            adInfoFlowUtil.setAdRequestSize(14).setVideoAdRequestSize(1).refreshAd();
//        } else {
//            adInfoFlowUtil.setAdRequestSize(15).refreshAd();
//        }
    }

    private List sort(List<Write> writes) {
        if (writes.size() % 3 == 0) {
            List list = new ArrayList();

            for (int i = 0; i < writes.size() / 3; i++) {
                Write w = writes.get(i * 3);
                Write w2 = writes.get(i * 3 + 1);
                Write w3 = writes.get(i * 3 + 2);

                list.add(w3);
                list.add(w2);
                list.add(w);
            }
            writes.clear();
            writes.addAll(list);
        }
        return writes;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            DataManager.Instance().currentType = type;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        adInfoFlowUtil.destroy();
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
