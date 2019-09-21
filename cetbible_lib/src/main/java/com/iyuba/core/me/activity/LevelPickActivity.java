package com.iyuba.core.me.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iyuba.biblelib.R;
import com.iyuba.core.me.widget.PickerView;

import java.util.ArrayList;
import java.util.List;

public class LevelPickActivity extends Activity {
    Context mContext = this;
    PickerView pv;
    int RESULT_OK = 1;
    int RESULT_CANCEL = -1;
    Button lpcancel;
    Button lpsure;
    String leveltext;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_picker);
        mContext = this;
        String flag = getIntent().getStringExtra("levels");

        pv = findViewById(R.id.level_pk);
        lpcancel = findViewById(R.id.lp_cancel);
        lpsure = findViewById(R.id.lp_sure);
        List<String> data = new ArrayList<String>();
        if (flag.equals("all")) {
            data.add("专业四级");
            data.add("专业八级");
            data.add("托福");
            data.add("雅思");
            data.add("初中");
            data.add("高中");
            data.add("三级");
            data.add("四级");
            data.add("六级");
        } else {
            data.add("熟练");
            data.add("精通");
            data.add("专家");
            data.add("0级");
            data.add("生疏");
            data.add("一般");
        }
        pv.setData(data);

        lpcancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent();
                LevelPickActivity.this.setResult(RESULT_CANCEL, intent);
                finish();
            }
        });
        lpsure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent();
                leveltext = pv.getData();
                intent.putExtra("result", leveltext);
                LevelPickActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
//		pv.setOnSelectListener(new onSelectListener() {
//
//			@Override
//			public void onSelect(String text) {
//				leveltext = text;
//			}
//		});
    }

}
