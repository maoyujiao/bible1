package com.iyuba.CET4bible.write;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.protocol.StudyRecordInfo;
import com.iyuba.CET4bible.protocol.UpdateStudyRecordRequestNew;
import com.iyuba.CET4bible.sqlite.mode.Write;
import com.iyuba.CET4bible.util.FavoriteUtil;
import com.iyuba.CET4bible.viewpager.WriteFragmentAdapter;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.util.TouristUtil;

import okhttp3.Call;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class WriteActivity extends BasisActivity implements OnClickListener {
    private static String[] CONTENT;
    private Context mContext;
    private TextView title;
    private ViewPager viewPager;
    private WriteFragmentAdapter adapter;
    private String titleName;

    //学习纪录
    private StudyRecordInfo studyRecordInfo;
    private GetDeviceInfo deviceInfo;
    private long startTime;

    private Write write;

    private FavoriteUtil favoriteUtil;
    private String prefix;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_content);

        mContext = this;
        if (getIntent().getStringExtra("type").equals("write")) {
            CONTENT = mContext.getResources().getStringArray(
                    R.array.write_title);
            prefix = "write";
        } else {
            CONTENT = mContext.getResources().getStringArray(
                    R.array.trans_title);
            prefix = "translate";
        }

        write = (Write) getIntent().getSerializableExtra("write");
        text = TextAttr.ToDBC(write.text);

        deviceInfo = new GetDeviceInfo(mContext);
        studyRecordInfo = new StudyRecordInfo();
        studyRecordInfo.uid = AccountManager.Instace(mContext).getId();
        studyRecordInfo.IP = deviceInfo.getLocalIPAddress();
        studyRecordInfo.DeviceId = deviceInfo.getLocalMACAddress();
        studyRecordInfo.Device = deviceInfo.getLocalDeviceType();
        studyRecordInfo.updateTime = "   ";
        studyRecordInfo.EndFlg = "1";
        studyRecordInfo.Lesson = Constant.APPName;
        studyRecordInfo.LessonId = getLessonId(write.name);
        studyRecordInfo.BeginTime = deviceInfo.getCurrentTime();
        studyRecordInfo.TestNumber = getTestNumber(write.name);
        startTime = System.currentTimeMillis();

        init();

        CheckBox checkBox = findView(R.id.cb_favorite);
        favoriteUtil = new FavoriteUtil(getIntent().getStringExtra("type").equals("write") ?
                FavoriteUtil.Type.write : FavoriteUtil.Type.translate);
        boolean isFavorite = favoriteUtil.isFavorite(prefix + write.num + write.index);
        checkBox.setChecked(isFavorite);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favoriteUtil.setFavorite(isChecked, prefix + write.num + write.index);
            }
        });
    }

    private String getTestNumber(String packName) {
//        String lessonId = getLessonId(packName);
        String type = "100";
        if (packName.contains("写作") || packName.contains("范文")) {
            type = "200";
        }
        return type + write.index + "00";
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

    private void init() {
        title = findViewById(R.id.title_info);
        //处理一下title
        if (write.name.contains("四级")) {
            titleName = write.name.replace("四级", "");
        } else if (write.name.contains("六级")) {
            titleName = write.name.replace("六级", "");
        }
        titleName = titleName.replace("真题", "");
        title.setText(titleName);
        findViewById(R.id.button_back).setOnClickListener(this);
        viewPager = findViewById(R.id.viewpager);
        adapter = new WriteFragmentAdapter(mContext, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findView(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.button_back:
                finish();
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
        String mode = prefix.equals("write") ? "4" : "3";
        try {
            if (AccountManager.Instace(mContext).checkUserLogin() && !TouristUtil.isTourist()) {
                if (!TextUtils.isEmpty(studyRecordInfo.uid) && !"0".equals(studyRecordInfo.uid)) {
                    Http.get(UpdateStudyRecordRequestNew.getUrl(studyRecordInfo, mode,
                            TextUtils.isEmpty(text) ? "0" : text.split(" ").length + ""), new HttpCallback() {
                        @Override
                        public void onSucceed(Call call, String response) {
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
