package com.iyuba.CET4bible.strategy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public abstract class ContentStrategy {

    public abstract RecyclerView.Adapter buildWorkAdapter(Context context, RecyclerView.Adapter originalAdapter);

    public abstract void init(RecyclerView recyclerView, RecyclerView.Adapter adapter);

    public abstract int getOriginalAdapterPosition(RecyclerView.Adapter workAdapter, int position);

    public interface Strategy {
        int NONE = -1;
        int VIP = 0;
        int YOUDAO = 1;
        int MIX = 9;
    }

}
