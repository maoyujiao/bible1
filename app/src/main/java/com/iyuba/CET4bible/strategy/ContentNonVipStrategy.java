package com.iyuba.CET4bible.strategy;

import android.support.v7.widget.RecyclerView;


public abstract class ContentNonVipStrategy extends ContentStrategy {

    public abstract void loadAd(RecyclerView.Adapter adapter);

    public abstract void refreshAd(RecyclerView recyclerView, RecyclerView.Adapter adapter);

}
