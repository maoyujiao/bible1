/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.About;
import com.iyuba.CET4bible.activity.HelpUse;
import com.iyuba.CET4bible.util.ClearBuffer;
import com.iyuba.CET4bible.widget.SleepDialog;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.activity.Login;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.FileSize;
import com.iyuba.core.widget.dialog.CustomToast;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;

//import com.iyuba.core.activity.FileBrowserActivity;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class SetFragment extends Fragment {
    private static int hour, minute, totaltime;// total用于计算时间，volume用于调整音量,睡眠模式用到的
    private static boolean isSleep = false;// 睡眠模式是否开启
    private Context mContext;
    private View root;
    Handler sleepHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int count = 0;
            AudioManager am = (AudioManager) mContext
                    .getSystemService(Context.AUDIO_SERVICE);
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (hour + minute != 0) {// 时间没结束
                        count++;
                        if (count % 10 == 0) {
                            if (am.getStreamVolume(AudioManager.STREAM_MUSIC) > 2) {
                                am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                                        AudioManager.ADJUST_LOWER, 0);// 第三参数为0代表不弹出提示。
                            }
                        }
                        totaltime--;
                        ((TextView) root.findViewById(R.id.sleep_state))
                                .setText(String.format("%02d:%02d", hour, minute));
                        hour = totaltime / 60;
                        minute = totaltime % 60;
                        sleepHandler.sendEmptyMessageDelayed(0, 60000);
                    } else {// 到结束时间
                        isSleep = false;
                        ((TextView) root.findViewById(R.id.sleep_state))
                                .setText(R.string.setting_sleep_state_off);
                        Intent intent = new Intent();
                        intent.setAction("gotosleep");
                        mContext.sendBroadcast(intent);
                    }
                    break;
                default:
                    break;
            }
        }

    };
    private CheckBox checkBox_Download, checkBox_Push, checkBox_night;
    private View aboutBtn, btn_download, btn_push, btn_night, btn_clear_pic,
            btn_help_use, btn_clear_video, recommendButton, sleepButton,
            language, savePathBtn;
    private Button logout;
    //	private void setNight() {
//		if (checkBox_night.isChecked()) {
//			SettingConfig.Instance().setNight(true);
//			((BasisActivity) mContext).night();
//		} else {
//			SettingConfig.Instance().setNight(false);
//			((BasisActivity) mContext).day();
//		}
//	}
    OnClickListener ocl = new OnClickListener() {
        @Override
        public void onClick(View arg0) {

            Intent intent;
            Dialog dialog;
            switch (arg0.getId()) {
                case R.id.btn_download:
                    setDownload();
                    break;
                case R.id.btn_push:
                    setPush();
                    break;
//			case R.id.night_mod:
//				setNight();
//				break;
            /*case R.id.play_set_btn:
				intent = new Intent(mContext, PlaySet.class);
				startActivity(intent);
				break;*/
                case R.id.clear_pic:
                    CustomToast
                            .showToast(mContext, R.string.setting_deleting, 2000);// 这里可以改为引用资源文件
                    GitHubImageLoader.Instace(mContext).clearCache();
                    handler.sendEmptyMessage(4);
                    break;
                case R.id.clear_video:
                    dialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(
                                    getResources().getString(R.string.alert_title))
                            .setMessage(
                                    getResources()
                                            .getString(R.string.setting_alert))
                            .setPositiveButton(
                                    getResources().getString(R.string.alert_btn_ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {

                                            CustomToast
                                                    .showToast(
                                                            mContext,
                                                            R.string.setting_deleting,
                                                            2000);// 这里可以改为引用资源文件
                                            new CleanBufferAsyncTask().execute();
                                        }
                                    })
                            .setNeutralButton(
                                    getResources().getString(
                                            R.string.alert_btn_cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                        }
                                    }).create();
                    dialog.show();
                    break;
                case R.id.help_use_btn:
                    intent = new Intent(mContext, HelpUse.class);
                    intent.putExtra("source", "set");
                    startActivity(intent);
                    break;
                case R.id.logout:
                    if (logout.getText().equals(mContext.getText(R.string.exit))) {
                        dialog = new AlertDialog.Builder(mContext)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(
                                        getResources().getString(
                                                R.string.alert_title))
                                .setMessage(
                                        getResources().getString(
                                                R.string.setting_logout_alert))
                                .setPositiveButton(
                                        getResources().getString(
                                                R.string.alert_btn_ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int whichButton) {

                                                handler.sendEmptyMessage(9);
                                            }
                                        })
                                .setNeutralButton(
                                        getResources().getString(
                                                R.string.alert_btn_cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                            }
                                        }).create();
                        dialog.show();
                    } else if (logout.getText().equals(
                            mContext.getText(R.string.no_login))) {
                        intent = new Intent();
                        intent.setClass(mContext, Login.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.sleep_mod:
                    intent = new Intent(mContext, SleepDialog.class);
                    startActivityForResult(intent, 23);
                    break;
                case R.id.recommend_btn:
                    prepareMessage();
                    break;
                case R.id.save_path_btn:
				/*mContext.startActivity(new Intent(mContext,
						FileBrowserActivity.class));*/
                    break;
                case R.id.about_btn:
                    intent = new Intent();
                    intent.setClass(mContext, About.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
    private TextView picSize, soundSize, savePath;
    private int appLanguage;
    private TextView languageText;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    ((TextView) root.findViewById(R.id.sleep_state))
                            .setText(R.string.setting_sleep_state_off);
                    break;
                case 3:
                    break;
                case 4:
                    picSize.setText("0B");
                    break;
                case 5:
                    initLanguage();
                    break;
                case 7:
                    initText();
                    initCheckBox();
                    break;
                case 8:
                    initSleep();
                    break;
                case 9:
                    AccountManager.Instace(mContext).loginOut();
                    CustomToast.showToast(mContext,
                            R.string.setting_loginout_success);
                    SettingConfig.Instance().setHighSpeed(false);
                    checkBox_Download.setChecked(false);
                    logout.setText(R.string.no_login);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.set, container, false);
        initWidget();
        handler.sendEmptyMessage(5);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    private void initCheckBox() {

        if (!AccountManager.Instace(mContext).checkUserLogin()) {
            SettingConfig.Instance().setHighSpeed(false);
        }
        checkBox_Download = root
                .findViewById(R.id.CheckBox_Download);
        checkBox_Download.setChecked(SettingConfig.Instance().isHighSpeed());
        checkBox_Download
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        setDownload();
                    }
                });
        checkBox_Push = root.findViewById(R.id.CheckBox_Push);
        checkBox_Push.setChecked(SettingConfig.Instance().isPush());
        checkBox_Push.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                setPush();
            }
        });
//		checkBox_night = (CheckBox) root.findViewById(R.id.CheckBox_night);
//		checkBox_night.setChecked(SettingConfig.Instance().isNight());
//		checkBox_night
//				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//
//						setNight();
//					}
//				});
    }

    /**
     *
     */
    private void initWidget() {

        btn_download = root.findViewById(R.id.btn_download);
        btn_push = root.findViewById(R.id.btn_push);
//		btn_night = root.findViewById(R.id.night_mod);
        //btn_play_set = root.findViewById(R.id.play_set_btn);
        btn_clear_pic = root.findViewById(R.id.clear_pic);
        btn_clear_video = root.findViewById(R.id.clear_video);
        btn_help_use = root.findViewById(R.id.help_use_btn);
        savePathBtn = root.findViewById(R.id.save_path_btn);
        savePath = root.findViewById(R.id.save_path);
        picSize = root.findViewById(R.id.picSize);
        soundSize = root.findViewById(R.id.soundSize);
        logout = root.findViewById(R.id.logout);
        sleepButton = root.findViewById(R.id.sleep_mod);
        aboutBtn = root.findViewById(R.id.about_btn);
        recommendButton = root.findViewById(R.id.recommend_btn);
        initListener();
    }

    private void initText() {
        if (AccountManager.Instace(mContext).checkUserLogin()) {
            logout.setText(R.string.exit);
        } else {
            logout.setText(R.string.no_login);
        }
        savePath.setText(Constant.envir);
        picSize.setText(getSize(0));
        soundSize.setText(getSize(1));
    }

    /**
     *
     */
    private void initListener() {

        //btn_play_set.setOnClickListener(ocl);
        btn_download.setOnClickListener(ocl);
        btn_push.setOnClickListener(ocl);
        btn_night.setOnClickListener(ocl);
        logout.setOnClickListener(ocl);
        btn_clear_pic.setOnClickListener(ocl);
        btn_clear_video.setOnClickListener(ocl);
        btn_help_use.setOnClickListener(ocl);
        sleepButton.setOnClickListener(ocl);
        recommendButton.setOnClickListener(ocl);
        aboutBtn.setOnClickListener(ocl);
        savePathBtn.setOnClickListener(ocl);
    }

    /**
     *
     */
    private void initSleep() {

        if (!isSleep) {
            ((TextView) root.findViewById(R.id.sleep_state))
                    .setText(R.string.setting_sleep_state_off);
        } else {
            ((TextView) root.findViewById(R.id.sleep_state)).setText(String
                    .format("%02d:%02d", hour, minute));
        }
    }

    private void initLanguage() {

        language = root.findViewById(R.id.set_language);
        language.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                setLanguage();
            }
        });
        languageText = root.findViewById(R.id.curr_language);
        // 根据设置初始化
        appLanguage = ConfigManager.Instance().loadInt("applanguage");
        String[] languages = mContext.getResources().getStringArray(
                R.array.language);
        languageText.setText(languages[appLanguage]);
    }

    private void setLanguage() {
        String[] languages = mContext.getResources().getStringArray(
                R.array.language);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.alert_title);
        builder.setSingleChoiceItems(languages, appLanguage,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        switch (index) {
                            case 0:// 系统默认语言
                                appLanguage = 0;
                                break;
                            case 1:// 简体中文
                                appLanguage = 1;
                                break;
                            case 2:// 英语
                                appLanguage = 2;
                                break;
                            case 3:// 后续
                                break;
                            default:
                                appLanguage = 0;
                                break;
                        }
                        ConfigManager.Instance().putInt("applanguage",
                                appLanguage);
                        Intent intent = new Intent();
                        intent.setAction("changeLanguage");
                        mContext.sendBroadcast(intent);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.alert_btn_cancel, null);
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == 1) {// 睡眠模式设置的返回结果
            hour = data.getExtras().getInt("hour");
            minute = data.getExtras().getInt("minute");
            if (hour + minute == 0) {
                isSleep = false;
                hour = 0;
                minute = 0;
                totaltime = 0;
                sleepHandler.removeMessages(0);
                handler.sendEmptyMessage(2);
            } else {
                sleepHandler.removeMessages(0);
                isSleep = true;
                totaltime = hour * 60 + minute;
                sleepHandler.sendEmptyMessage(0);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sleepHandler.removeMessages(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessage(5);
        handler.sendEmptyMessage(7);
        handler.sendEmptyMessage(8);
        MobclickAgent.onResume(mContext);
    }

    private void prepareMessage() {
        String text = getResources().getString(R.string.setting_share1)
                + Constant.APPName
                + getResources().getString(R.string.setting_share2)
                + "：http://app.iyuba.com/android/androidDetail.jsp?id="
                + Constant.APPID;
        Intent shareInt = new Intent(Intent.ACTION_SEND);
        shareInt.setType("text/*");
        shareInt.putExtra(Intent.EXTRA_TEXT, text);
        shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareInt.putExtra("sms_body", text);
        startActivity(Intent.createChooser(shareInt,
                getResources().getString(R.string.setting_share_ways)));
    }

    private String getSize(int type) {
        if (type == 0) {
            return FileSize.getInstance().getFormatFolderSize(
                    new File(RuntimeManager.getContext()
                            .getExternalCacheDir().getAbsolutePath()));
        } else {
            return FileSize.getInstance().getFormatFolderSize(
                    new File(Constant.videoAddr));
        }
    }

    private void setDownload() {
        if (AccountManager.Instace(mContext).checkUserLogin()
                && ConfigManager.Instance().loadInt("isvip") > 0) {
            if (checkBox_Download.isChecked()) {
                SettingConfig.Instance().setHighSpeed(true);
            } else {
                SettingConfig.Instance().setHighSpeed(false);
            }
        } else {
            AlertDialog alert = new AlertDialog.Builder(mContext).create();
            alert.setTitle(R.string.alert_title);
            alert.setMessage(mContext.getText(R.string.setting_vip_download));
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, getResources()
                            .getString(R.string.alert_btn_buy),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, VipCenter.class);
                            startActivity(intent);
                        }
                    });
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, getResources()
                            .getString(R.string.alert_btn_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            alert.show();
        }
        checkBox_Download.setChecked(SettingConfig.Instance().isHighSpeed());
    }

    private void setPush() {
        if (checkBox_Push.isChecked()) {
            SettingConfig.Instance().setPush(true);
            PushAgent.getInstance(mContext).enable();
        } else {
            SettingConfig.Instance().setPush(false);
            PushAgent.getInstance(mContext).disable();
        }
    }

    class CleanBufferAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ClearBuffer clear = new ClearBuffer(mContext);
            if (clear.Delete()) {
                clear.updateDB();
                soundSize.post(new Runnable() {
                    @Override
                    public void run() {

                        soundSize.setText("0B");
                    }
                });
            }
            return null;
        }
    }
}
