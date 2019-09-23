package com.iyuba.CET4bible.listening;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.holybible.widget.recycler.EndlessListRecyclerView;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.widget.subtitle.Subtitle;
import com.iyuba.abilitytest.network.AbilityTestRequestFactory;
import com.iyuba.abilitytest.network.GetSpeakRank;
import com.iyuba.abilitytest.network.SpeakRank;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.MD5;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankFragment extends Fragment {
    private static final String TAG = RankFragment.class.getSimpleName();
    private static final int PAGE_SIZE = 20;

    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout mSwipeContainer;
    @BindView(R.id.relative_container)
    RelativeLayout mNoDataContainer;
    @BindView(R.id.recycler)
    EndlessListRecyclerView mRankingRecyclerView;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    RankingAdapter mAdapter;
    List<SpeakRank> ranks;
    SpeakRank rank ;
    Subtitle mCurrentVoa;
    private SpeakRank mMyRank;
    private String mVoaId;

    public static RankFragment newInstance() {
        return new RankFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RankingAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, root);
        SectionA sectionA  = (SectionA) container.getContext();
        setVoaId(sectionA.mExamTime);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeContainer.setOnRefreshListener(mRefreshListener);
        mSwipeContainer.setColorSchemeResources(R.color.blue, R.color.purple, R.color.orange, R.color.red);
        mRankingRecyclerView.setEndless(true);
        mRankingRecyclerView.setAdapter(mAdapter);
        mRankingRecyclerView.setOnEndlessListener(mEndlessListener);

        refreshData();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCurrentVoa = null;
    }

    public void setSwipeRefreshing(boolean isRefreshing) {
        mSwipeContainer.setRefreshing(isRefreshing);
    }





    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshData();

        }
    };



    private void refreshData() {
        int userId = Integer.parseInt(AccountManager.Instace(getActivity()).getId());
        int last = mAdapter.getLastRank();

        String sign = buildReadRankSign(userId, Constant.mListen, 0, start, last == 0? 20 : last);
        Map<String, String> map = new HashMap<>();

        if(null == mVoaId){
            mVoaId = "0" ;
        }
        Call<GetSpeakRank> call = AbilityTestRequestFactory.getExamDetailApi().getSpeakRank(RangeType.TODAY, userId, Constant.mListen,
                Integer.parseInt(mVoaId), 0, last == 0? 20 : last, sign, map);
        call.enqueue(new Callback<GetSpeakRank>() {
            @Override
            public void onResponse(Call<GetSpeakRank> call, Response<GetSpeakRank> response) {
                if (response.body()!=null){
                    ranks = response.body().data;
                    rank= getMyRank(response.body());
                    mAdapter.setData(ranks,rank);
                    mAdapter.notifyDataSetChanged();
                    setSwipeRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<GetSpeakRank> call, Throwable t) {
                setSwipeRefreshing(false);
            }
        });
    }

    private int start = 0;
    private EndlessListRecyclerView.OnEndlessListener mEndlessListener = new EndlessListRecyclerView.OnEndlessListener() {
        @Override
        public void onEndless() {
            int userId = Integer.parseInt(AccountManager.Instace(getActivity()).getId());
            int last = mAdapter.getLastRank();
            String sign = buildReadRankSign(userId, Constant.mListen, 0, 0, last +20);
            Map<String, String> map = new HashMap<>();
            Call<GetSpeakRank> call = AbilityTestRequestFactory.getExamDetailApi().getSpeakRank2(RangeType.TODAY, userId, Constant.mListen,
                    Integer.parseInt(mVoaId), 0, last + 20, map);

            call.enqueue(new Callback<GetSpeakRank>() {
                @Override
                public void onResponse(Call<GetSpeakRank> call, Response<GetSpeakRank> response) {
                    if (response.body()!=null){
                        ranks= response.body().data;
                        Log.d("diao",ranks.size()+"");

                        rank= getMyRank(response.body());
                        mAdapter.setData(ranks,rank);
                        mAdapter.notifyDataSetChanged();
                        setSwipeRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<GetSpeakRank> call, Throwable t) {
                    setSwipeRefreshing(false);
                }
            });
//                mPresenter.loadMore(userId, mCurrentVoa.voaid, last, PAGE_SIZE);

        }
    };

    public SpeakRank getMyRank(GetSpeakRank rank) {
        SpeakRank speakRank = new SpeakRank() ;
        speakRank.count = rank.myCount;
        speakRank.imgSrc = rank.myImgSrc;
        speakRank.name = rank.myName;
        speakRank.ranking = rank.myRanking;
        speakRank.score  = rank.myScore;
        speakRank.uid = rank.myId;
        speakRank.vip = rank.vip;
        return speakRank;
    }

    public void setMyRank(SpeakRank myRank) {
        mMyRank = myRank;
    }

    public void setVoaId(String voaId) {
        mVoaId = voaId;
    }

    public interface IRankInfo {
        Pair<String, String> getDescriptionInfo();
    }

    private String buildReadRankSign(int uid, String topic, int topicId, int start, int total) {
        StringBuilder sb = new StringBuilder();
        String now = SDF.format(new Date());
        sb.append(uid).append(topic).append(topicId).append(start).append(total).append(now);
        return MD5.getMD5ofStr(sb.toString());
    }

}
