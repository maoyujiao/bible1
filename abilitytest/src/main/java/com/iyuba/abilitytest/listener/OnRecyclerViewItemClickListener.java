package com.iyuba.abilitytest.listener;

import android.view.View;

/**
 * Created by liuzhenli on 2017/4/12.
 */

public interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}
