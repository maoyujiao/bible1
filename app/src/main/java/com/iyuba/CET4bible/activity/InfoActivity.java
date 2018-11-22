package com.iyuba.CET4bible.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.viewpager.BlogFragmentAdapter;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.widget.TabText;
import com.umeng.analytics.MobclickAgent;

public class InfoActivity extends BasisActivity implements OnClickListener {
    private static String[] CONTENT;
    private Context mContext;
    private ViewPager viewPager;
    private TabText all, listen, read, write, translate;
    private BlogFragmentAdapter adapter;
    private View button_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        mContext = this;
        init();
    }

    private void init() {
        button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        CONTENT = getResources().getStringArray(R.array.blog_title);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                setTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        /*adapter = new BlogFragmentAdapter(mContext, getActivity()
                .getSupportFragmentManager());*/
        adapter = new BlogFragmentAdapter(mContext, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        initTabText();
        setTab(0);
    }

    private void initTabText() {

        all = findViewById(R.id.all);
        read = findViewById(R.id.read);
        listen = findViewById(R.id.listen);
        write = findViewById(R.id.write);
        translate = findViewById(R.id.translate);
        all.setText(CONTENT[0]);
        write.setText(CONTENT[1]);
        read.setText(CONTENT[2]);
        listen.setText(CONTENT[3]);
        translate.setText(CONTENT[4]);
        all.setOnClickListener(this);
        read.setOnClickListener(this);
        listen.setOnClickListener(this);
        write.setOnClickListener(this);
        translate.setOnClickListener(this);
    }

    private void setTab(int arg0) {

        switch (arg0) {
            case 0:
                all.getFocus();
                write.dismiss();
                read.dismiss();
                listen.dismiss();
                translate.dismiss();
                break;
            case 1:
                all.dismiss();
                write.getFocus();
                read.dismiss();
                listen.dismiss();
                translate.dismiss();
                break;
            case 2:
                all.dismiss();
                write.dismiss();
                read.getFocus();
                listen.dismiss();
                translate.dismiss();
                break;
            case 3:
                all.dismiss();
                write.dismiss();
                read.dismiss();
                listen.getFocus();
                translate.dismiss();
                break;
            case 4:
                all.dismiss();
                write.dismiss();
                read.dismiss();
                listen.dismiss();
                translate.getFocus();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.all:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.write:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.listen:
                viewPager.setCurrentItem(3, true);
                break;
            case R.id.read:
                viewPager.setCurrentItem(2, true);
                break;
            case R.id.translate:
                viewPager.setCurrentItem(4, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }
}
