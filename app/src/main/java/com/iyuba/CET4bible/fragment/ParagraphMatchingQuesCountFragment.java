package com.iyuba.CET4bible.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ParagraphQuesCountAdapter;
import com.iyuba.CET4bible.event.ParagraphEvent;
import com.iyuba.CET4bible.sqlite.mode.ParagraphMatchingBean;
import com.iyuba.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * ParagraphMatchingQuesFragment
 *
 * @author wayne
 * @date 2017/12/22
 */
public class ParagraphMatchingQuesCountFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ParagraphQuesCountAdapter adapter;
    private TextView tvSubmit;
    private TextView tvTitle;

    private int fragmentCount;
    private boolean isShowAnswer = false;
    private SparseIntArray selectedArray;
    private ParagraphMatchingBean data;
    private String[] answerArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paragraph_matching_question_count, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        data = (ParagraphMatchingBean) getArguments().getSerializable("data");
        fragmentCount = getArguments().getInt("count");
        isShowAnswer = getArguments().getBoolean("showAnswer");
        answerArray = data.answer.split(",|，");

        tvSubmit = view.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        tvTitle = view.findViewById(R.id.tv_title);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5, GridLayoutManager.VERTICAL, false));
        adapter = new ParagraphQuesCountAdapter(mContext, fragmentCount);
        recyclerView.setAdapter(adapter);

        EventBus.getDefault().post(new ParagraphEvent.ParagraphRequestSelectedArray());


        if (isShowAnswer) {
            tvTitle.setText("您已经完成题目");
            tvSubmit.setOnClickListener(null);
        }
    }

    private boolean check() {
        int count = 0;
        for (int i = 0; i < fragmentCount; i++) {
            if (selectedArray.indexOfValue(i) != -1) {
                count += 1;
            }
        }
        return count == fragmentCount;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphAnswerSubmit(ParagraphEvent.ParagraphAnswerSubmitEvent event) {
        isShowAnswer = true;
        tvTitle.setText("您已经完成题目");
        tvSubmit.setVisibility(View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphAnswerRefresh(ParagraphEvent.ParagraphAnswerRefreshEvent event) {
        adapter.refreshData(event.array);
        selectedArray = event.array;

        if (check()) {
            tvTitle.setText("完成了，提交吧～");
        } else {
            tvTitle.setText("还没有完成，确定提交吗？");
        }
        if (isShowAnswer) {
            tvTitle.setText("您已经完成题目");
            tvSubmit.setVisibility(View.INVISIBLE);
            adapter.setShowAnswer(answerArray);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                EventBus.getDefault().post(new ParagraphEvent.ParagraphAnswerSubmitEvent());
                break;
            default:
                break;
        }
    }
}
