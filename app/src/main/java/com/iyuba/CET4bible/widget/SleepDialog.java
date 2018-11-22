package com.iyuba.CET4bible.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.iyuba.CET4bible.R;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.widget.dialog.CustomToast;

/**
 * 睡眠设置用布局
 *
 * @author 陈彤
 */
public class SleepDialog extends Activity {
    private Context mContext;
    private LinearLayout layout;
    private Button deButton, inButton, comfiButton, cancelButton;
    private EditText editTextHour, editTextMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_dialog);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        deButton = findViewById(R.id.sleeptime_de);
        inButton = findViewById(R.id.sleeptime_in);
        comfiButton = findViewById(R.id.exitBtn0);
        cancelButton = findViewById(R.id.exitBtn1);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
        deButton.setText("-");
        inButton.setText("+");
        // 增加和删除按钮的逻辑
        deButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (editTextHour.isFocused()) {
                    int i = Integer.valueOf(editTextHour.getText().toString()) - 1;
                    if (i < 0) {
                        i = 0;
                    }
                    editTextHour.setText(String.format("%02d", i));
                } else {
                    int i = Integer
                            .valueOf(editTextMinute.getText().toString()) - 10;
                    if (i < 0) {
                        if (Integer.valueOf(editTextHour.getText().toString()) > 0) {
                            editTextHour.setText(String.format("%02d",
                                    Integer.valueOf(editTextHour.getText()
                                            .toString()) - 1));
                            editTextMinute.setText(String
                                    .format("%02d", 60 + i));
                        } else {
                            i = 0;
                            editTextMinute.setText(String.format("%02d", i));
                        }

                    } else {
                        editTextMinute.setText(String.format("%02d", i));
                    }

                }
            }
        });
        inButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (editTextHour.isFocused()) {
                    int i = Integer.valueOf(editTextHour.getText().toString()) + 1;
                    editTextHour.setText(String.format("%02d", i));
                } else {
                    int i = Integer
                            .valueOf(editTextMinute.getText().toString()) + 10;
                    if (i >= 60) {

                        editTextHour.setText(String.format("%02d", Integer
                                .valueOf(editTextHour.getText().toString()) + 1));
                        editTextMinute.setText(String.format("%02d", i - 60));
                    } else {
                        editTextMinute.setText(String.format("%02d", i));
                    }

                }
            }
        });
        editTextHour = findViewById(R.id.sleeptime_hour);
        editTextMinute = findViewById(R.id.sleeptime_minute);
        editTextHour.setText("00");
        editTextMinute.setText("00");
        comfiButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("hour",
                        Integer.valueOf(editTextHour.getText().toString()));
                intent.putExtra("minute",
                        Integer.valueOf(editTextMinute.getText().toString()));
                setResult(1, intent);// 第一个参数是自定义的结果编号，可以自己定义static，这里就省了。
                finish();
            }
        });

        editTextHour.setText("00");
        editTextMinute.setText("00");
        layout = findViewById(R.id.exit_layout);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomToast.showToast(mContext, R.string.setting_sleep);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public void exitbutton0(View v) {
        this.finish();
    }

}
