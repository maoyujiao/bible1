package com.iyuba.CET4bible.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ParagraphQuestionItemAdapter;
import com.iyuba.CET4bible.event.ParagraphEvent;
import com.iyuba.CET4bible.sqlite.mode.ParagraphMatchingBean;
import com.iyuba.CET4bible.util.FavoriteUtil;
import com.iyuba.base.BaseFragment;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.activity.VipCenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static com.iyuba.base.util.L.e;

/**
 * ParagraphMatchingQuesFragment
 *
 * @author wayne
 * @date 2017/12/22
 */
public class ParagraphMatchingQuesFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ParagraphQuestionItemAdapter adapter;
    private TextView tvCurrentPos;
    private TextView tvQuestionCount;
    private TextView tvNext;
    private TextView tvPreview;
    private TextView tvSubmit;
    private TextView tvQuestion;
    private TextView tvAnswer;
    private TextView tvExplanation;

    private int currentPos;
    private int fragmentCount;
    private String questionStr;
    private boolean isShowAnswer = false;
    private ParagraphMatchingBean data;
    private List<String> answerArray = new ArrayList<>();
    private List<String> sortedAnswerArray = new ArrayList<>();
    private SparseIntArray selectedArray;

    private CheckBox cbFavorite;
    private FavoriteUtil favoriteUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paragraph_matching_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        data = (ParagraphMatchingBean) getArguments().getSerializable("data");

        String[] answer = data.answer.split(",|，");
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < answer.length; i++) {
            answerArray.add(answer[i]);
            set.add(answer[i]);
        }
        sortedAnswerArray.addAll(set);
        Collections.sort(sortedAnswerArray);

        questionStr = getArguments().getString("question");
        currentPos = getArguments().getInt("pos");
        fragmentCount = getArguments().getInt("count");
        isShowAnswer = getArguments().getBoolean("showAnswer");

        tvQuestion = view.findViewById(R.id.tv_question);
        tvAnswer = view.findViewById(R.id.tv_answer);
        tvExplanation = view.findViewById(R.id.tv_explain);

        tvCurrentPos = view.findViewById(R.id.tv_current_pos);
        tvQuestionCount = view.findViewById(R.id.tv_ques_count);
        tvNext = view.findViewById(R.id.tv_preview);
        tvNext.setOnClickListener(this);
        tvPreview = view.findViewById(R.id.tv_next);
        tvPreview.setOnClickListener(this);
        tvSubmit = view.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);

        String[] explanations = data.explanation.split("[+][+]");

        if (AccountManager.isVip()) {
            tvExplanation.setText(explanations[currentPos]);
        } else {
            tvExplanation.setText("您还不是会员，会员用户才可以查看解析，是否开通会员？ 点击开通");
            tvExplanation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, VipCenter.class);
                    mContext.startActivity(intent);
                }
            });
        }

        tvAnswer.setVisibility(View.INVISIBLE);
        tvExplanation.setVisibility(View.INVISIBLE);
        setViewVisibility();

        tvQuestion.setText(questionStr);
        tvCurrentPos.setText(currentPos + 1 + "");
        tvQuestionCount.setText("/" + fragmentCount);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5, GridLayoutManager.VERTICAL, false));
        adapter = new ParagraphQuestionItemAdapter(mContext, sortedAnswerArray, answerArray, currentPos, isShowAnswer);
        recyclerView.setAdapter(adapter);


        cbFavorite = view.findViewById(R.id.cb_favorite);
        favoriteUtil = new FavoriteUtil(FavoriteUtil.Type.paragraph_question);
        cbFavorite.setChecked(favoriteUtil.isFavorite(getFavoriteKey()));
        cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favoriteUtil.setFavorite(isChecked, getFavoriteKey());
            }
        });
    }

    private String getFavoriteKey() {
        return data.year + data.index + currentPos;
    }

    private void setViewVisibility() {
        if (isShowAnswer) {
            tvSubmit.setText("查看结果");

            tvAnswer.setVisibility(View.VISIBLE);
            tvExplanation.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 防止onStart和setUserVisibleHint 同时调用，多次请求数据
     */
    private boolean isOnResume = false;

    @Override
    public void onStart() {
        super.onStart();
        isOnResume = true;
        if (getUserVisibleHint()) {
            // 请求数据
            e("++++++++  请求刷新数据 ");
            // 打开页面，请求获取做题数据
            EventBus.getDefault().post(new ParagraphEvent.ParagraphRequestSelectedArray());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isOnResume) {
            e("++++++++  请求刷新数据 ");
            // 打开页面，请求获取做题数据
            EventBus.getDefault().post(new ParagraphEvent.ParagraphRequestSelectedArray());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnResume = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphAnswerSubmit(ParagraphEvent.ParagraphAnswerSubmitEvent event) {
        // 提交答案，显示解析和正确答案
        isShowAnswer = true;
        setViewVisibility();
        adapter.setShowAnswer(isShowAnswer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphAnswerRefresh(ParagraphEvent.ParagraphAnswerRefreshEvent event) {
        // 接受到答题数据，刷新选项
        if (!getUserVisibleHint()) {
            return;
        }
        adapter.refreshData(event.array);
        selectedArray = event.array;
        tvAnswer.setText(String.format(Locale.CHINA, "当前选择答案为%s，正确答案为%s", getAnswer(), answerArray.get(currentPos)));
    }

    private String getAnswer() {
        int ss = selectedArray.get(currentPos, -1);
        if (ss == -1) {
            return "空";
        } else {
            return sortedAnswerArray.get(selectedArray.get(currentPos));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_preview:
                previewPage();
                break;
            case R.id.tv_next:
                nextPage();
                break;
            case R.id.tvSubmit:
                EventBus.getDefault().post(new ParagraphEvent.ParagraphQuesFragmentChangeEvent(fragmentCount));
                break;
            default:
                break;
        }
    }

    private void previewPage() {
        if (currentPos == 0) {
            return;
        }
        EventBus.getDefault().post(new ParagraphEvent.ParagraphPageClickEvent(-1));
    }


    private void nextPage() {
        EventBus.getDefault().post(new ParagraphEvent.ParagraphPageClickEvent(1));
    }
}
