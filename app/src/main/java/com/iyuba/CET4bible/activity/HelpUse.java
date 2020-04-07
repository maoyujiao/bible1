package com.iyuba.CET4bible.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.viewpager.HelpFragmentAdapter;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.widget.viewpagerindicator.CirclePageIndicator;

/**
 * 使用说明Activity
 *
 * @author chentong
 */
public class HelpUse extends BasisActivity {
    private ViewPager viewPager;
    private CirclePageIndicator pi;
    private String source;
    private TextView close;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:
                    close.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    close.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.help_use);
        CrashApplication.getInstance().addActivity(this);
        source = getIntent().getStringExtra("source");
        pi = findViewById(R.id.pageIndicator);
        viewPager = findViewById(R.id.viewpager);
        close = findViewById(R.id.close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (source.equals("welcome")) {
                    Intent intent = new Intent();
                    intent.setClass(HelpUse.this, MainActivity.class);
                    intent.putExtra("showDialog",true);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                pi.setCurrentItem(arg0);
                switch (arg0) {
                    case 3: // 停止变更
                        if (viewPager.getCurrentItem() == 3 ) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                        break;
                    default:break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        viewPager.setAdapter(new HelpFragmentAdapter(
                getSupportFragmentManager()));
        pi.setPageColor(getResources().getColor(R.color.red));
        pi.setViewPager(viewPager, false);
        pi.setCentered(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (source.equals("welcome")) {
            Intent intent = new Intent();
            intent.setClass(HelpUse.this, MainActivity.class);
            intent.putExtra("showDialog",true);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }
}