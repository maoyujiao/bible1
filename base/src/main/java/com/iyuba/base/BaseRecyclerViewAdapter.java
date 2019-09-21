package com.iyuba.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * BaseRecyclerViewAdapter
 *
 * @author wayne
 * @date 2017/12/28
 */
public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected Context mContext;
    private OnClickListener onClickListener;
    private OnClickListener onLongClickListener;

    public BaseRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.itemView, holder.getAdapterPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClickListener != null) {
                    onLongClickListener.onClick(holder.itemView, holder.getAdapterPosition());
                    return true;
                }
                return false;
            }
        });
    }

    public interface OnClickListener {
        void onClick(View v, int pos);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
