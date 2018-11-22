package com.iyuba.CET4bible.adapter;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.event.ParagraphEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * ParagraphQuestionItemAdapter
 *
 * @author wayne
 * @date 2017/12/22
 */
public class ParagraphQuesCountAdapter extends RecyclerView.Adapter<ParagraphQuesCountAdapter.Holder> {
    private Context context;
    private int fragmentCount;
    private SparseIntArray selectedArray;
    private boolean isShowAnswer = false;
    private String[] answerArray;
    private List<String> sortedAnswerList;

    public ParagraphQuesCountAdapter(Context context, int fragmentCount) {
        this.context = context;
        this.fragmentCount = fragmentCount;
        selectedArray = new SparseIntArray();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_paragraph_question, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.text.setText(position + 1 + "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator = AnimatorInflater.loadStateListAnimator(context, R.animator.button_state_list_animator);
            holder.text.setStateListAnimator(animator);
        }

        if (isShowAnswer) {
            int myAnswer = selectedArray.get(position, -1);
            if (myAnswer == -1) {
                // 错
                holder.text.setTextColor(Color.WHITE);
                holder.text.setBackgroundResource(R.drawable.select_bg_circle_orange);
            } else {
                if (answerArray[position].equals(sortedAnswerList.get(myAnswer))) {
                    // 对
                    holder.text.setTextColor(Color.WHITE);
                    holder.text.setBackgroundResource(R.drawable.select_bg_circle_green);
                } else {
                    // 错
                    holder.text.setTextColor(Color.WHITE);
                    holder.text.setBackgroundResource(R.drawable.select_bg_circle_orange);
                }
            }
        } else {

            int myAnswer = selectedArray.get(position, -1);
            if (myAnswer == -1) {
                holder.text.setTextColor(Color.parseColor("#4a4f5e"));
                holder.text.setBackgroundResource(R.drawable.select_bg_cirle_grey);
            } else {
                holder.text.setTextColor(Color.WHITE);
                holder.text.setBackgroundResource(R.drawable.select_bg_circle_orange);
            }
        }

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = !holder.text.isSelected();
                holder.text.setSelected(isSelected);

                EventBus.getDefault().post(new ParagraphEvent.ParagraphQuesFragmentChangeEvent(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return fragmentCount;
    }

    public void refreshData(SparseIntArray array) {
        selectedArray = array;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 150);
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView text;


        public Holder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public void setShowAnswer(String[] answerArray) {
        isShowAnswer = true;
        this.answerArray = answerArray;

        HashSet<String> set = new HashSet<>();
        set.addAll(Arrays.asList(answerArray));

        sortedAnswerList = new ArrayList<>();
        sortedAnswerList.addAll(set);
        Collections.sort(sortedAnswerList);

    }
}
