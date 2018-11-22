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

import java.util.List;

/**
 * ParagraphQuestionItemAdapter
 *
 * @author wayne
 * @date 2017/12/22
 */
public class ParagraphQuestionItemAdapter extends RecyclerView.Adapter<ParagraphQuestionItemAdapter.Holder> {
    private Context context;
    private int fragmentPos;
    private SparseIntArray selectedArray;
    private boolean isShowAnswer;
    private List<String> sortedAnswerArray;
    private List<String> answerArray;

    public ParagraphQuestionItemAdapter(Context context, List<String> sortedAnswerArray,
                                        List<String> answerArray, int pos, boolean isShowAnswer) {
        this.context = context;
        this.fragmentPos = pos;
        this.sortedAnswerArray = sortedAnswerArray;
        this.answerArray = answerArray;
        this.isShowAnswer = isShowAnswer;
        selectedArray = new SparseIntArray();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_paragraph_question, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.text.setText(sortedAnswerArray.get(position));

        //  为显示答案时
        if (isShowAnswer) {
            int selectedAnswerPosition = selectedArray.get(fragmentPos, -1);
            //   未答题
            if (selectedAnswerPosition == -1) {
                if (sortedAnswerArray.get(position).equals(answerArray.get(fragmentPos))) {
                    // 正确答案
                    setGreenState(holder.text);
                } else {
                    // 未选择
                    setGreyState(holder.text);
                }
            } else {
                // 答对了
                if (sortedAnswerArray.get(selectedAnswerPosition).equals(answerArray.get(fragmentPos))) {
                    if (selectedAnswerPosition == position) {
                        //对了
                        setGreenState(holder.text);
                    } else {
                        // 未选择
                        setGreyState(holder.text);
                    }
                } else {
                    if (selectedAnswerPosition == position) {
                        // 错了
                        setOrangeState(holder.text);
                    } else if (isRight(position)) {
                        //对了
                        setGreenState(holder.text);
                    } else {
                        // 未选择
                        setGreyState(holder.text);
                    }
                }
            }


        } else {
            // 选择的答案
            int myAnswer = selectedArray.get(fragmentPos, -1);
            holder.text.getPaint().setFakeBoldText(false);

            if (myAnswer == position) {
                // 已经选中当前
                setOrangeState(holder.text);
            } else {
                if (selectedArray.indexOfValue(position) == -1) {
                    // 未选择
                    setGreyState(holder.text);
                } else {
                    // 选中，但非当前页面
                    setGreyAnsweredState(holder.text);
                    holder.text.getPaint().setFakeBoldText(true);
                }
            }


            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = holder.text.getTag();
                    boolean isSelected = false;
                    if (tag != null) {
                        isSelected = true;
                    }
                    isSelected = !isSelected;
                    holder.text.setTag(isSelected);

                    holder.text.animate()
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .setDuration(150);
                    holder.text.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.text.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(100);
                        }
                    }, 150);

                    EventBus.getDefault().post(new ParagraphEvent.ParagraphAnswerClickEvent(fragmentPos, holder.getAdapterPosition(), isSelected));
                }
            });
        }
    }

    private void setOrangeState(TextView textView) {
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.select_bg_circle_orange);
    }

    private void setGreenState(TextView textView) {
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.select_bg_circle_green);
    }

    private void setGreyState(TextView textView) {
        textView.setTextColor(Color.parseColor("#4a4f5e"));
        textView.setBackgroundResource(R.drawable.select_bg_cirle_grey);
    }

    private void setGreyAnsweredState(TextView textView) {
        textView.setTextColor(context.getResources().getColor(R.color.title_bar_color));
        textView.setBackgroundResource(R.drawable.select_bg_cirle_grey);
    }

    private boolean isRight(int position) {
        String answer = answerArray.get(fragmentPos);
        for (int i = 0; i < sortedAnswerArray.size(); i++) {
            if (sortedAnswerArray.get(i).equals(answer)) {
                if (position == i) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return sortedAnswerArray.size();
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

    public void setShowAnswer(boolean showAnswer) {
        isShowAnswer = showAnswer;
        notifyDataSetChanged();
    }
}
