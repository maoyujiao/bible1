package com.iyuba.CET4bible.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.iyuba.CET4bible.R;


public abstract class BaseDialog extends AlertDialog implements android.view.View.OnClickListener {

    protected BaseDialog(Context context) {
        super(context, R.style.BaseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    protected abstract void processClick(View v);

    @Override
    public void onClick(View v) {
        processClick(v);
    }


}
