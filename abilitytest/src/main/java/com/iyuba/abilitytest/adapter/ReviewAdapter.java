package com.iyuba.abilitytest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.listener.OnRecyclerViewItemClickListener;

/**
 * Created by liuzhenli on 2017/8/22.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context mContext;
    private int mTotal;
    private int mCurrent;
    private OnRecyclerViewItemClickListener listener;

    public ReviewAdapter(Context context, int count) {
        this.mContext = context;
        this.mTotal = count;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_ability_review, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_title_num.setText(position + 1 + "");
        if (mCurrent < position) {
            holder.tv_title_num.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.tv_title_num.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.tv_title_num, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mTotal;
    }

    public void setCurrentPosition(int index) {
        mCurrent = index;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title_num;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title_num = itemView.findViewById(R.id.tv_title_num);
        }
    }
}
