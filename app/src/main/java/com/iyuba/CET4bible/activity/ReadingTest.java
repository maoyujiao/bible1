package com.iyuba.CET4bible.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ReadingFragmentAdapter;
import com.iyuba.CET4bible.protocol.StudyRecordInfo;
import com.iyuba.CET4bible.protocol.UpdateStudyRecordRequestNew;
import com.iyuba.CET4bible.sqlite.mode.PackInfo;
import com.iyuba.CET4bible.sqlite.op.ReadingInfoOp;
import com.iyuba.CET4bible.util.FavoriteUtil;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.util.TouristUtil;

import java.util.List;

import okhttp3.Call;

public class ReadingTest extends BasisActivity implements OnClickListener {
    private RelativeLayout rl_section1, rl_section2;
    private ImageView iv_section1, iv_section2;
    private TextView tv1, tv2;
    private ViewPager viewpager_reading;
    private ReadingFragmentAdapter adapter;
    private String packName;
    private Context mContext;
    private TextView title_info;

    //学习纪录
    private StudyRecordInfo studyRecordInfo;
    private GetDeviceInfo deviceInfo;
    private long startTime;


    private FavoriteUtil favoriteUtil;
    private String favoritePrefix = "reading";
    private PackInfo packInfo;
    private String pName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading_test);

        mContext = this;
        Bundle bundle = getIntent().getExtras();
        packName = bundle.getString("PackName");
        pName = new String(packName.getBytes());

        deviceInfo = new GetDeviceInfo(mContext);
        studyRecordInfo = new StudyRecordInfo();
        studyRecordInfo.uid = AccountManager.Instace(mContext).getId();
        studyRecordInfo.IP = deviceInfo.getLocalIPAddress();
        studyRecordInfo.DeviceId = deviceInfo.getLocalMACAddress();
        studyRecordInfo.Device = deviceInfo.getLocalDeviceType();
        studyRecordInfo.updateTime = "   ";
        studyRecordInfo.EndFlg = "1";
        studyRecordInfo.Lesson = Constant.APPName;
        studyRecordInfo.LessonId = getLessonId(packName);
        studyRecordInfo.BeginTime = deviceInfo.getCurrentTime();
        studyRecordInfo.TestNumber = getTestNumber(packName);
        startTime = System.currentTimeMillis();
        initView();

    }

    private String getTestNumber(String packName) {
        try {
            ReadingInfoOp readingInfoOp = new ReadingInfoOp(mContext);
            List<PackInfo> packInfos = readingInfoOp.findDataByPackName(packName);
            packInfo = packInfos.get(0);
            return packInfos.get(0).TitleNum + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0000";
    }

    private String getLessonId(String packName) {
        try {
            String year = packName.substring(0, 4);
            String month = "06";
            String number = "01";
            if (packName.contains("年12月")) {
                month = "12";
            }
            if (packName.contains("第二套")) {
                number = "02";
            } else if (packName.contains("第三套")) {
                number = "03";
            }
            return year + month + number;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        rl_section1.setOnClickListener(this);
    }

    private void initView() {
        title_info = findViewById(R.id.title_info);
        //处理一下packName
        packName = packName.replace("四级", "");
        packName = packName.replace("六级", "");
        if (packName != null && !packName.equals("")) {
            title_info.setText(packName);
        }
        findViewById(R.id.button_back).setOnClickListener(this);
        rl_section1 = findViewById(R.id.rl_section1);
        rl_section2 = findViewById(R.id.rl_section2);
        iv_section1 = findViewById(R.id.iv_section1);
        iv_section2 = findViewById(R.id.iv_section2);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        rl_section1.setOnClickListener(this);
        rl_section2.setOnClickListener(this);
        viewpager_reading = findViewById(R.id.viewpager_reading);
        adapter = new ReadingFragmentAdapter(mContext, getSupportFragmentManager());
        viewpager_reading.setAdapter(adapter);
        viewpager_reading.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewpager_reading.setCurrentItem(0);
        setTab(0);

        favoriteUtil = new FavoriteUtil(FavoriteUtil.Type.reading);

//        if (packInfo != null) {
//            cbFavorite1.setChecked(favoriteUtil.isFavorite(favoritePrefix + pName + "_1"));
//            cbFavorite2.setChecked(favoriteUtil.isFavorite(favoritePrefix + pName + "_2"));
//
//            cbFavorite1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    favoriteUtil.setFavorite(isChecked, favoritePrefix + pName + "_1");
//                }
//            });
//            cbFavorite2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    favoriteUtil.setFavorite(isChecked, favoritePrefix + pName + "_2");
//                }
//            });
//        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_section1) {
            viewpager_reading.setCurrentItem(0, true);
        }
        if (v.getId() == R.id.rl_section2) {
            viewpager_reading.setCurrentItem(1, true);
        }
        if (v.getId() == R.id.button_back) {
            finish();
        }
    }

    private void setTab(int position) {
        switch (position) {
            case 0:
                iv_section1.setImageResource(R.drawable.section1_light);
                iv_section2.setImageResource(R.drawable.section2);
                tv1.setTextColor(Color.parseColor("#7fc635"));
                tv2.setTextColor(Color.parseColor("#BABABA"));
                break;
            case 1:
                iv_section2.setImageResource(R.drawable.section2_light);
                iv_section1.setImageResource(R.drawable.section1);
                tv2.setTextColor(Color.parseColor("#7fc635"));
                tv1.setTextColor(Color.parseColor("#BABABA"));
                break;
            default:
                break;
        }

    }


    @Override
    protected void onDestroy() {
        if (System.currentTimeMillis() - startTime < 1000 * 15) {
            e("--- 时间不够15秒 ---");
            super.onDestroy();
            return;
        }
        studyRecordInfo.EndTime = deviceInfo.getCurrentTime();
        if (!TextUtils.isEmpty(studyRecordInfo.uid) && !"0".equals(studyRecordInfo.uid)) {
            try {
                if (AccountManager.Instace(mContext).checkUserLogin() && !TouristUtil.isTourist()) {
                    Http.get(UpdateStudyRecordRequestNew.getUrl(studyRecordInfo, "3", "0"), new HttpCallback() {
                        @Override
                        public void onSucceed(Call call, String response) {
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
