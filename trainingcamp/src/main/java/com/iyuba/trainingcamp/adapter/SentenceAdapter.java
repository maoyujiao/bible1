package com.iyuba.trainingcamp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.activity.SentenceDetailActivity;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.utils.ParaConstants;

import java.io.Serializable;
import java.util.List;


/**
 * @author yq QQ:1032006226
 */
public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.ViewHolder> {

    private List<LearningContent> sentences;
    private Context mContext;

    public SentenceAdapter(Context context, List<LearningContent> list) {
        this.sentences = list;
        mContext = context;
    }

    @NonNull
    @Override
    public SentenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trainingcamp_sentence, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceAdapter.ViewHolder holder, final int position) {

        holder.SentenceView.setText(sentences.get(position).en);
        holder.SentenceCn.setText(sentences.get(position).cn);
        float points  = 0f;
        if (sentences.get(position).score == null){
            points  = 0f;
        }else {
            points  = Float.parseFloat(sentences.get(position).score);
        }
        if (points > 79) {
            holder.score.setBackgroundResource(R.drawable.trainingcamp_icon_80_100);
        } else if (points > 59) {
            holder.score.setBackgroundResource(R.drawable.trainingcamp_icon_60_79);
        } else if (points > 1) {
            holder.score.setBackgroundResource(R.drawable.trainingcamp_icon_1_59);
        } else {
            holder.score.setBackgroundResource(R.drawable.trainingcamp_icon_1_59);
        }
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"font/DINMedium_0.ttf");
        holder.score.setTypeface(tf);
        holder.score.setText(String.valueOf((int)points));
        holder.SentenceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SentenceDetailActivity.class);
                intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) sentences);
                intent.putExtra("index",position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == sentences){
            return  0 ;
        }
        return sentences.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView SentenceView;
        TextView SentenceCn;
        TextView score,detail;

        public ViewHolder(View itemView) {
            super(itemView);
            SentenceView = itemView.findViewById(R.id.sentnence_read);
            SentenceCn = itemView.findViewById(R.id.sentnence_cn);
            score = itemView.findViewById(R.id.score);
            detail = itemView.findViewById(R.id.detail);

        }
    }
}
