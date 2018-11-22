package com.iyuba.CET4bible.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listener.AppUpdateCallBack;
import com.iyuba.CET4bible.manager.DownloadManager;
import com.iyuba.CET4bible.manager.VersionManager;
import com.iyuba.CET4bible.sqlite.mode.DownloadFile;
import com.iyuba.CET4bible.thread.AppUpdateThread;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.util.FileOpera;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.core.widget.dialog.CustomToast;

/**
 * 关于界面
 *
 * @author chentong
 */
public class About extends BasisActivity implements AppUpdateCallBack {
    private Context mContext;
    private Button backBtn;
    private TextView version;
    private View appUpdate, praise, feedback, announcer;
    private View appNewImg;
    private String version_code;
    private String appUpdateUrl;// 版本号
    private RoundProgressBar progressBar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    appUpdate.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    appNewImg.setVisibility(View.GONE);
                    initDownload(Constant.appfile + "_" + version_code);
                    break;
                case 1:
                    appNewImg.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    CustomToast.showToast(mContext, "当前应用已是最新版本");
                    break;
                case 2:
                    DownloadFile file = (DownloadFile) msg.obj;
                    if (file.downloadState.equals("start")) {
                        progressBar.setCricleProgressColor(0xff87c973);
                        progressBar.setMax(file.fileSize);
                        progressBar.setProgress(file.downloadSize);
                        Log.e("ABOUT", file.fileSize + "");
                        Message message = new Message();
                        message.what = 2;
                        message.obj = file;
                        handler.sendMessageDelayed(message, 1500);
                    } else if (file.downloadState.equals("finish")) {
                        appUpdate.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        appNewImg.setVisibility(View.GONE);
                        DownloadManager.Instance().fileList.remove(file);
                        new FileOpera(mContext).openFile(file.filePath
                                + file.fileName + file.fileAppend);
                    }
                    break;
                case 3:
                    appNewImg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_app_name)).setText(Constant.APP);

        praise = findViewById(R.id.praise);
        praise.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Uri uri = Uri.parse("market://details?id="
                            + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {

                    Dialog dialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getResources().getString(
                                            R.string.alert_title))
                            .setMessage(getResources().getString(
                                            R.string.about_market_error))
                            .setNeutralButton(getResources().getString(
                                            R.string.alert_btn_ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create();
                    dialog.show();
                }
            }
        });
        feedback = findViewById(R.id.feedback);
        feedback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Feedback.class);
                startActivity(intent);
            }
        });
        announcer = findViewById(R.id.announcer);
        announcer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getResources().getString(R.string.alert_title))
                        .setMessage(getResources().getString(
                                        R.string.about_announcer_alert))
                        .setNeutralButton(getResources().getString(R.string.alert_btn_ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create();
                dialog.show();
            }
        });
        appUpdate = findViewById(R.id.update);
        appUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAppUpdate();
            }
        });
        appNewImg = findViewById(R.id.newApp);
        version = findViewById(R.id.version);
        version.setText("当前版本： " + VersionManager.VERSION_CODE);
        progressBar = findViewById(R.id.roundProgressBar);
        update = getIntent().getBooleanExtra("update", false);
        boolean has = false;
        int size = DownloadManager.Instance().fileList.size();
        DownloadFile file;
        for (int i = 0; i < size; i++) {
            file = DownloadManager.Instance().fileList.get(i);
            if (file.id.equals("-1")) {
                Message message = new Message();
                message.what = 2;
                message.obj = file;
                handler.sendMessage(message);
                has = true;
            }
        }
        if (!has) {
            if (update) {
                this.appUpdateUrl = getIntent().getStringExtra("url");
                this.version_code = getIntent().getStringExtra("version");
                handler.sendEmptyMessage(0);
            } else {
                checkAppUpdate();
            }
        } else {
            appUpdate.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 检查新版本
     */
    public void checkAppUpdate() {
        VersionManager.Instace(this).checkNewVersion(VersionManager.version,
                this);
    }

    @Override
    public void appUpdateSave(String version_code, String newAppNetworkUrl) {

        this.version_code = version_code;
        this.appUpdateUrl = newAppNetworkUrl;
        Looper.prepare();
        AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle(R.string.alert_title);
        alert.setMessage(getResources()
                .getString(R.string.about_update_alert_1)
                + version_code
                + getResources().getString(R.string.about_update_alert_2));
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.alert_btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        handler.sendEmptyMessage(0);
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.sendEmptyMessage(3);
                    }
                });
        alert.show();
        Looper.loop();
    }

    @Override
    public void appUpdateFaild() {

        handler.sendEmptyMessage(1);
    }

    private void initDownload(String path) {

        DownloadFile downloadFile = new DownloadFile();
        downloadFile.id = "-1";
        downloadFile.downloadState = "start";
        downloadFile.fileAppend = ".apk";
//		downloadFile.downLoadAddress = appUpdateUrl;
        downloadFile.downLoadAddress = "http://static3.iyuba.com/android/apk/Valuablecet4/Valuablecet4.apk";
        downloadFile.filePath = Constant.envir + "/appUpdate/";
        downloadFile.fileName = path;
        DownloadManager.Instance().fileList.add(downloadFile);
        AppUpdateThread appUpdateThread = new AppUpdateThread(downloadFile);
        appUpdateThread.start();
        Message message = new Message();
        message.what = 2;
        message.obj = downloadFile;
        handler.sendMessage(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
