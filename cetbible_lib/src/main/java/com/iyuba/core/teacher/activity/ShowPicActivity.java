package com.iyuba.core.teacher.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.iyuba.biblelib.R;

public class ShowPicActivity extends Activity {

    private ImageView btnBack;
    private ImageView discPic;

    private String tempFilePath = Environment.getExternalStorageDirectory() + "/ques_temp.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_show_disc_pic);

        initWidget();
    }

    public void initWidget() {
        btnBack = findViewById(R.id.btn_back);
        discPic = findViewById(R.id.disc_pic);

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        Bitmap discBit = BitmapFactory.decodeFile(tempFilePath, null);
        discPic.setImageBitmap(discBit);
    }
}
