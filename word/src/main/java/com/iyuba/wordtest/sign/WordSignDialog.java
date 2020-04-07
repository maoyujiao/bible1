package com.iyuba.wordtest.sign;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.iyuba.wordtest.R;
import com.iyuba.wordtest.viewmodel.UserSignViewModel;


public class WordSignDialog extends Dialog {

    Context context ;
    UserSignViewModel model ;

    public WordSignDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    public WordSignDialog(@NonNull Context context, UserSignViewModel model){
        super(context);
        this.context = context;
        this.model = model;
    }

    private void initDialog() {

        setContentView(R.layout.sign_dialog);
        setDialogStyle();
        TextView sign_btn = findViewById(R.id.sign_btn);
        ImageView close = findViewById(R.id.sign_close);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.sign_close){
                    dismiss();
                    ((Activity)context).finish();
                }else if (v.getId() == R.id.sign_btn){
                    getContext().startActivity(WordSignActivity.buildIntent(getContext(),model));
                    dismiss();
                    ((Activity)context).finish();
                }
            }
        };
        close.setOnClickListener(listener);
        sign_btn.setOnClickListener(listener);
    }

    private void setDialogStyle() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int)getContext().getResources().getDisplayMetrics().widthPixels; // 宽度
        params.height = (int)getContext().getResources().getDisplayMetrics().heightPixels; // 宽度
        //lp.width = 650;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}
