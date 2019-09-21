package com.iyuba.core.discover.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.sqlite.mode.Sayings;
import com.iyuba.core.sqlite.op.SayingsOp;

import java.util.Random;

/**
 * 谚语界面
 *
 * @author chentong
 * @version 1.1 修改内容 增添自动模式
 */
public class Saying extends BasisActivity {
    private Context mContext;
    private Button backBtn, nextMode;
    private TextView english, chinese;
    private int id;
    private Sayings sayings;
    private SayingsOp sayingsOp;
    private Random rnd;
    private Button next;
    private boolean mode;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    rnd = new Random();
                    id = rnd.nextInt(10000) % 150 + 1;
                    sayings = sayingsOp.findDataById(id);
                    setData();
                    if (!mode) {
                        handler.sendEmptyMessageDelayed(0, 4000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saying);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        mode = ConfigManager.Instance().loadBoolean("saying");
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        nextMode = findViewById(R.id.next_mode);
        nextMode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mode = !mode;
                ConfigManager.Instance().putBoolean("saying", mode);
                setButtonText();
            }
        });
        next = findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                handler.sendEmptyMessage(0);
            }
        });
        chinese = findViewById(R.id.chinese);
        english = findViewById(R.id.english);
        sayingsOp = new SayingsOp(mContext);
        setButtonText();
    }

    private void setButtonText() {
        if (mode) {
            nextMode.setText(R.string.saying_auto);
            next.setVisibility(View.VISIBLE);
            handler.removeMessages(0);
            handler.sendEmptyMessage(0);
        } else {
            nextMode.setText(R.string.saying_manul);
            next.setVisibility(View.INVISIBLE);
            handler.sendEmptyMessage(0);
        }
    }

    private void setData() {
        chinese.setText(sayings.chinese);
        english.setText(sayings.english);
    }

    @Override
    public void finish() {
        super.finish();
        if (!mode) {
            handler.removeMessages(0);
        }
    }
}
