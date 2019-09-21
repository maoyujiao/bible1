/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.setting.SettingConfig;

/**
 * 播放设置 对音频播放的常见属性进行设置 属性值：原文同步、耳机（插入播放暂停）、屏幕常亮、播放模式、听歌播放模式（特化，根据ID判断是否出现）
 *
 * @author 陈彤
 * @version 1.0
 */
public class PlaySet extends BasisActivity {
    private Context mContext;
    private Button backButton;
    private CheckBox checkBox_syncho, checkBox_autoPlay, checkBox_autoStop,
            checkBox_light;
    private View autoStopButton, autoPlayButton, btn_music_type, btn_light,
            btn_music_mode, btn_syncho;
    private TextView music_mode, music_type;
    OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            int id = arg0.getId();
            if (id == R.id.button_back) {
                finish();
            } else if (id == R.id.btn_auto_syncho) {
                if (SettingConfig.Instance().isSyncho()) {
                    SettingConfig.Instance().setSyncho(false);
                } else {
                    SettingConfig.Instance().setSyncho(true);
                }
                checkBox_syncho.setChecked(SettingConfig.Instance().isSyncho());
            } else if (id == R.id.btn_auto_stop) {
                if (SettingConfig.Instance().isAutoStop()) {
                    SettingConfig.Instance().setAutoStop(false);
                } else {
                    SettingConfig.Instance().setAutoStop(true);
                }
                checkBox_autoStop.setChecked(SettingConfig.Instance()
                        .isAutoStop());
            } else if (id == R.id.btn_auto_play) {
                if (SettingConfig.Instance().isAutoPlay()) {
                    SettingConfig.Instance().setAutoPlay(false);
                } else {
                    SettingConfig.Instance().setAutoPlay(true);
                }
                checkBox_autoPlay.setChecked(SettingConfig.Instance()
                        .isAutoPlay());
            } else if (id == R.id.btn_light) {
                if (SettingConfig.Instance().isLight()) {
                    SettingConfig.Instance().setLight(false);
                } else {
                    SettingConfig.Instance().setLight(true);
                }
                checkBox_autoPlay
                        .setChecked(SettingConfig.Instance().isLight());
            } else if (id == R.id.btn_music_type) {
                createDialog(R.string.setting_base_music_type, R.array.type,
                        Constant.type, "type");
            } else if (id == R.id.btn_music_mode) {
                createDialog(R.string.setting_base_music_mode, R.array.mode,
                        Constant.mode, "mode");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_set);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        init();
    }

    private void init() {

        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(ocl);
        btn_syncho = findViewById(R.id.btn_auto_syncho);
        btn_syncho.setOnClickListener(ocl);
        btn_light = findViewById(R.id.btn_light);
        btn_light.setOnClickListener(ocl);
        autoStopButton = findViewById(R.id.btn_auto_stop);
        autoStopButton.setOnClickListener(ocl);
        autoPlayButton = findViewById(R.id.btn_auto_play);
        autoPlayButton.setOnClickListener(ocl);
        btn_music_type = findViewById(R.id.btn_music_type);
        btn_music_type.setOnClickListener(ocl);
        if (!Constant.APPID.equals("209")) {
            btn_music_type.setVisibility(View.GONE);
        }
        btn_music_mode = findViewById(R.id.btn_music_mode);
        btn_music_mode.setOnClickListener(ocl);
        music_type = findViewById(R.id.music_type);
        music_type
                .setText(mContext.getResources().getStringArray(R.array.type)[Constant.type]);
        music_mode = findViewById(R.id.music_mode);
        music_mode
                .setText(mContext.getResources().getStringArray(R.array.mode)[Constant.mode]);
        initCheckBox();
    }

    private void initCheckBox() {

        checkBox_syncho = findViewById(R.id.CheckBox_auto_syncho);
        checkBox_syncho.setChecked(SettingConfig.Instance().isSyncho());
        checkBox_syncho
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        SettingConfig.Instance().setSyncho(isChecked);
                    }
                });

        checkBox_autoPlay = findViewById(R.id.CheckBox_auto_play);
        checkBox_autoPlay.setChecked(SettingConfig.Instance().isAutoPlay());
        checkBox_autoPlay
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        SettingConfig.Instance().setAutoPlay(isChecked);
                    }
                });

        checkBox_autoStop = findViewById(R.id.CheckBox_auto_stop);
        checkBox_autoStop.setChecked(SettingConfig.Instance().isAutoStop());
        checkBox_autoStop
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        SettingConfig.Instance().setAutoStop(isChecked);
                    }
                });
        checkBox_light = findViewById(R.id.CheckBox_light);
        checkBox_light.setChecked(SettingConfig.Instance().isLight());
        checkBox_light
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        SettingConfig.Instance().setLight(isChecked);
                    }
                });
    }

    /**
     * 对话框选择属性
     *
     * @param
     * @return
     */
    private void createDialog(int title, int array, int select,
                              final String sign) {
        Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setSingleChoiceItems(array, select,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (sign.equals("type")) {
                            Constant.type = which;
                            music_type.setText(mContext.getResources()
                                    .getStringArray(R.array.type)[which]);
                            ConfigManager.Instance().putInt("type",
                                    Constant.type);
                        } else {
                            Constant.mode = which;
                            music_mode.setText(mContext.getResources()
                                    .getStringArray(R.array.mode)[which]);
                            ConfigManager.Instance().putInt("mode",
                                    Constant.mode);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.alert_btn_cancel, null);
        builder.create().show();
    }

}
