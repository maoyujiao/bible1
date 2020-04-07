package com.iyuba.CET4bible.strategy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;


public final class ContentVipStrategy extends ContentStrategy {

    @Override
    public RecyclerView.Adapter buildWorkAdapter(Context context, RecyclerView.Adapter originalAdapter) {
        return originalAdapter;
    }

    @Override
    public void init(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getOriginalAdapterPosition(RecyclerView.Adapter workAdapter, int position) {
        return position;
    }
}
