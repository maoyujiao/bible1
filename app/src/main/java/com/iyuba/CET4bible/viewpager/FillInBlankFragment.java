package com.iyuba.CET4bible.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.MainActivity;
import com.iyuba.CET4bible.adapter.FavoriteFillInBlankAdapter;
import com.iyuba.CET4bible.sqlite.mode.FillInBlankBean;
import com.iyuba.CET4bible.sqlite.op.FillInBlankOp;
import com.iyuba.CET4bible.util.AdInfoFlowUtil;
import com.iyuba.base.BaseFragment;
import com.iyuba.base.util.SimpleLineDividerDecoration;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * FillInBlankFragment
 *
 * @author wayne
 * @date 2017/12/20
 */
public class FillInBlankFragment extends BaseFragment {
    RecyclerView recyclerView;
    FavoriteFillInBlankAdapter adapter;
    ArrayList mList;

    AdInfoFlowUtil adInfoFlowUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fillinblank, container, false);
        if (containerVp!=null){
            containerVp.setObjectForPosition(view, 1);
        }
        return view ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new SimpleLineDividerDecoration(mContext).setColor(R.color.darkgray));

        mList = new ArrayList<>();
        adapter = new FavoriteFillInBlankAdapter(mContext, mList , getActivity() instanceof MainActivity);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        FillInBlankOp op = new FillInBlankOp(mContext);
        mList.addAll(op.selectData());
        adapter.notifyDataSetChanged();

        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
            @Override
            public void onADLoad(List ads) {
                AdInfoFlowUtil.insertAD(mList, ads, adInfoFlowUtil);
                adapter.notifyDataSetChanged();
            }
        });
        adInfoFlowUtil.setAdRequestSize(5).refreshAd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adInfoFlowUtil.destroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            DataManager.Instance().currentType = 3;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
