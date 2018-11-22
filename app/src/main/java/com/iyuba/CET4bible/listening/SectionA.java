package com.iyuba.CET4bible.listening;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.presenter.PDFUtil;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.protocol.StudyRecordInfo;
import com.iyuba.CET4bible.protocol.UpdateStudyRecordRequestNew;
import com.iyuba.CET4bible.sqlite.op.NewTypeTextAOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeTextBOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeTextCOp;
import com.iyuba.CET4bible.sqlite.op.SectionATextOp;
import com.iyuba.CET4bible.sqlite.op.SectionBTextOp;
import com.iyuba.CET4bible.util.AdBannerUtil;
import com.iyuba.CET4bible.util.Share;
import com.iyuba.CET4bible.viewpager.ListenFragmentAdapter;
import com.iyuba.base.util.L;
import com.iyuba.base.util.T;
import com.iyuba.base.util.Util;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.sqlite.mode.test.CetAnswer;
import com.iyuba.core.sqlite.mode.test.CetText;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.play.ExtendedPlayer;
import com.iyuba.play.OnStateChangeListener;

import net.protyposis.android.mediaplayer.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 */
public class SectionA extends BasisActivity {
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    previous.setBackgroundResource(R.drawable.un_previous_question);
                    previous.setEnabled(false);
                    next.setBackgroundResource(R.drawable.next_question);
                    next.setEnabled(true);
                    submit.setVisibility(View.GONE);
                    break;
                case 2:
                    previous.setBackgroundResource(R.drawable.previous_question);
                    previous.setEnabled(true);
                    next.setBackgroundResource(R.drawable.next_question);
                    next.setEnabled(true);
                    submit.setVisibility(View.GONE);
                    break;
                case 3:
                    previous.setBackgroundResource(R.drawable.previous_question);
                    previous.setEnabled(true);
                    next.setBackgroundResource(R.drawable.trainingcamp_un_next_question);
                    next.setEnabled(false);
                    submit.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private Context mContext;
    private Button previous, next, pause, submit, sheet;
    private ViewPager viewPager;
    private TextView title;
    private int curPos;
    private String section;
    private ArrayList<CetAnswer> answerList;
    private ListenFragmentAdapter adapter;
    private SeekBar seekbar;
    private TextView curTime, allTime;
    private int minus;
    private boolean isNewType;
    private ImageView iv_more;
    private PopupWindow popupWindow;
    private ExtendedPlayer extendedPlayer;
    Handler videoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int i = extendedPlayer.getCurrentPosition();
                    seekbar.setProgress(i);
                    curTime.setText(formatTime(i));
                    videoHandler.sendEmptyMessageDelayed(0, 1000);
                    setPauseBackground();
                    break;
                case 1:
                    seekbar.setSecondaryProgress(msg.arg1 * seekbar.getMax() / 100);
                    break;
                case 2:
                    Log.e("onCompletion", "onCompletion");
                    viewPager.setCurrentItem(1, true);
                    setContent(1, true, false);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private TextView tv_speed;
    private AdBannerUtil adBannerUtil;
    //是否为解析返回的
    private boolean isExplain = false;
    OnClickListener ocl = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent intent;
            switch (arg0.getId()) {
                case R.id.button_back:
                    onBackPressed();
                    break;
                case R.id.pause:
                    try {
                        if (extendedPlayer.isPlaying()) {
                            extendedPlayer.pause();
                        } else {
                            extendedPlayer.start();
                        }
                        setPauseBackground();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                case R.id.preview:
                    if (isExplain) {
                        // 是否为查看解析
                        viewPager.setCurrentItem(2, true);
                    } else {
                        viewPager.setCurrentItem(1, true);
                    }
                    setContent(-1, false, false);
                    break;
                case R.id.next:
                    if (isExplain) {
                        // 是否为查看解析
                        viewPager.setCurrentItem(2, true);
                    } else {
                        viewPager.setCurrentItem(1, true);
                    }
                    setContent(1, false, false);
                    break;
                case R.id.submit:
                    intent = new Intent(mContext, ListenSubmit.class);
                    intent.putExtra("finish", true);
                    startActivityForResult(intent, 0);
                    break;
                default:
                    break;
            }
        }
    };
    private float speed = 1.0f;
    private int count = 1;
    //学习纪录
    private StudyRecordInfo studyRecordInfo;
    private GetDeviceInfo deviceInfo;
    private long startTime;
    private CustomDialog waitingDialog;

    private PDFUtil pdfUtil;
    private String mExamTime;
    private TextView tvAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_a);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);

        extendedPlayer = new ExtendedPlayer(mContext);

        section = getIntent().getStringExtra("section");
        isNewType = getIntent().getBooleanExtra("isNewType", false);

        answerList = ListenDataManager.Instance().answerList;

        pdfUtil = new PDFUtil();
        waitingDialog = WaittingDialog.showDialog(mContext);

        deviceInfo = new GetDeviceInfo(mContext);
        studyRecordInfo = new StudyRecordInfo();
        studyRecordInfo.uid = AccountManager.Instace(mContext).getId();
        studyRecordInfo.IP = deviceInfo.getLocalIPAddress();
        studyRecordInfo.DeviceId = deviceInfo.getLocalMACAddress();
        studyRecordInfo.Device = deviceInfo.getLocalDeviceType();
        studyRecordInfo.updateTime = "   ";
        studyRecordInfo.EndFlg = " ";
        studyRecordInfo.Lesson = Constant.APPName;
        studyRecordInfo.LessonId = ListenDataManager.Instance().year;
        studyRecordInfo.BeginTime = deviceInfo.getCurrentTime();
        startTime = System.currentTimeMillis();

        mExamTime = ListenDataManager.Instance().year;

        tvAB = findView(R.id.tv_ab);
        tvAB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                abRepeat();
            }
        });
        findViewById(R.id.button_back).setOnClickListener(ocl);
        title = findViewById(R.id.play_title_info);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow(v);
            }
        });

        if (isNewType) {
            String strTitle = ListenDataManager.Instance().year;
            StringBuilder sb = new StringBuilder();
            sb.append(strTitle.substring(0, 4)).append("年").append(strTitle.substring(4, 6)).append("月")
                    .append("(").append(strTitle.substring(strTitle.length() - 1, strTitle.length())).append(")");
            title.setText(sb.toString());
        } else {
            title.setText(ListenDataManager.Instance().year);
        }

        viewPager = findViewById(R.id.viewpager);
        adapter = new ListenFragmentAdapter(mContext, getSupportFragmentManager(), section,mExamTime);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findView(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        initView();
        adBannerUtil = new AdBannerUtil(mContext);
        adBannerUtil.setView(findViewById(R.id.youdao_ad), (ImageView) findViewById(R.id.photoImage));
        adBannerUtil.setAddamView(findViewById(R.id.ad_addam));
        adBannerUtil.setMiaozeView((ViewGroup) findById(R.id.adMiaozeParent));
        adBannerUtil.loadAd();
        viewPager.setCurrentItem(1, true);
    }

    int abRepeatStatus = 0;
    int abRepeatStart = 0;
    long abRepeatEnd = 0;
    Subscription abRepeatSub;

    private void abRepeat() {
        if (abRepeatStatus == 0) {
            if (!extendedPlayer.isPlaying()) {
                return;
            }
            abRepeatStart = extendedPlayer.getCurrentPosition();
            abRepeatStatus = 1;
            tvAB.setText("A-");
            showShort("开始记录A- ，再次点击即可区间播放");
            L.e("start ----  " + abRepeatStart + "    ");
        } else if (abRepeatStatus == 1) {
            abRepeatEnd = extendedPlayer.getCurrentPosition();
            if (abRepeatEnd < abRepeatStart) {
                resetABRepeat();
                return;
            }
            abRepeatStatus = 2;
            tvAB.setText("A-B");
            showShort("开始播放 A-B");
            L.e("end  ---  " + abRepeatEnd + "    ");
            extendedPlayer.seekTo(abRepeatStart);
            abRepeatSub = Observable.interval(abRepeatEnd - abRepeatStart, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            extendedPlayer.seekTo(abRepeatStart);
                        }
                    });

        } else if (abRepeatStatus == 2) {
            abRepeatStatus = 0;
            tvAB.setText("AB");
            stopABRepeat();
            showShort("已取消播放A-B");
        }
    }

    private void resetABRepeat() {
        tvAB.setText("AB");
        abRepeatStatus = 0;
        stopABRepeat();
    }

    private void stopABRepeat() {
        if (abRepeatSub != null && !abRepeatSub.isUnsubscribed()) {
            abRepeatSub.unsubscribe();
            abRepeatSub = null;
            L.e("--- ab --- repeat --- unsubscribe");
        }
    }

    private void initPopupWindow(View v) {
        View view = getLayoutInflater().inflate(R.layout.popup_window, null);

        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        popupWindow.showAsDropDown(v, 0, -20);

        View choose = view.findViewById(R.id.lv_vip_testlib);
        View share = view.findViewById(R.id.lv_server);
        View sheet = view.findViewById(R.id.lv_sheet);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    createDialog();
                } else {
                    startActivity(new Intent(mContext, Login.class));
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imagePath = Constant.iconAddr;
                popupWindow.dismiss();
                Share share = new Share(getApplicationContext());
                share.setListener(getApplicationContext(), mExamTime);
                share.shareMessage(imagePath, Constant.APPName,
                        "http://m.iyuba.com/ncet/t.jsp?l=" + Constant.APP_CONSTANT.TYPE()
                                + "&i=" + ListenDataManager.Instance().year + "&s=" + section,title.getText().toString()+"听力真题");
//                new Share(mContext).prepareMessage("", "英语六级宝典", "http://m.iyuba.com/ncet/t.jsp?l=6&i="+ListenDataManager.Instance().year+"&s="+section);
            }
        });

        sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Intent intent = new Intent(mContext, ListenSubmit.class);
                intent.putExtra("finish", false);
                startActivityForResult(intent, 0);
            }
        });
        view.findViewById(R.id.tv_pdf).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AccountManager.isVip()) {
                    showPdfConfirmDialog();
                } else {
                    showPdfDialog();
                }
            }
        });
        view.findViewById(R.id.tv_feedback).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ErrorFeedbackActivity.class));
            }
        });
    }

    private void showPDFDialog(final String url) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        final android.support.v7.app.AlertDialog dialog = builder.setTitle("PDF已生成 请妥善保存。")
                .setMessage("下载链接：" + url + "\n[已复制到剪贴板]\n")
                .setNegativeButton("下载", null)
                .setPositiveButton("关闭", null)
                .setNeutralButton("发送", null)
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        try {
            View v = dialog.getWindow().getDecorView().findViewById(android.R.id.message);
            if (v != null) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.copy2ClipBoard( mContext,url);
                        showLong("PDF下载链接已复制到剪贴板");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button negative = dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button positive = dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showLong("文件将会下载到" + "iyuba/" + Constant.AppName + "/ 目录下");
                    pdfUtil.download(mExamTime, url, mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button neutral = dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL);
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mExamTime + " PDF";
                new Share(mContext).shareMessage(Constant.iconAddr, "", url, title);
            }
        });
        Util.copy2ClipBoard(mContext, url);
        showLong("PDF下载链接已复制到剪贴板");
    }

    private void showPdfConfirmDialog() {
        new android.support.v7.app.AlertDialog.Builder(mContext).setTitle("提示")
                .setMessage("试题生成PDF需消耗20积分，请确认是否生成")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("生成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showPdfDialog();

                    }
                }).show();
    }

    private void showPdfDialog() {
        pdfUtil.getPDF(mContext, pdfUtil.getPDFId(mExamTime), true, new PDFUtil.Callback() {
                                @Override
                                public void result(boolean result, String message) {
                                    if (result) {
//                            showLong("积分扣取成功");
                                        showPDFDialog(message);
                                    } else {
                                        showLong(message);
                                    }
                                }
                            });
    }

//    private void showPdfClassDialog() {
//        final String[] items = new String[]{"英文", "中英双语"};
//        new AlertDialog.Builder(mContext).setTitle("请选择生成pdf的种类")
//                .setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            pdfUtil.getPDF(mContext, pdfUtil.getPDFId(mExamTime), true, new PDFUtil.Callback() {
//                                @Override
//                                public void result(boolean result, String message) {
//                                    if (result) {
////                            showLong("积分扣取成功");
//                                        showPDFDialog(message);
//                                    } else {
//                                        showLong(message);
//                                    }
//                                }
//                            });
//                            dialog.dismiss();
//                        } else {
//                            pdfUtil.getPDF(mContext, pdfUtil.getPDFId(mExamTime), false, new PDFUtil.Callback() {
//                                @Override
//                                public void result(boolean result, String message) {
//                                    if (result) {
////                            showLong("积分扣取成功");
//                                        showPDFDialog(message);
//                                    } else {
//                                        showLong(message);
//                                    }
//                                }
//                            });
//                            dialog.dismiss();
//                        }
//
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create().show();
//    }

    private void createDialog() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.choose_player);
        int item;
        if (ConfigManager.Instance().loadBoolean("isChangePlayer")) {
            item = 1;
        } else {
            item = 0;
        }
        builder.setSingleChoiceItems(R.array.choose_player, item,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        initSoundUrl(answerABs);
                        if (which == 1) {   //调速播放器
                            if (AccountManager.Instace(mContext).userInfo.vipStatus.equals("0")) {
                                showVipDialog();
                            } else {
                                ConfigManager.Instance().putBoolean("isChangePlayer", true);
                                tv_speed.setVisibility(View.VISIBLE);
                                speed = 1.0f;
                                tv_speed.setText("x" + String.valueOf(speed).substring(0, 3));
                                extendedPlayer.setPlaySpeed(speed);
                            }
                        } else if (which == 0) {
                            ConfigManager.Instance().putBoolean("isChangePlayer", false);
                            tv_speed.setVisibility(View.GONE);
                            speed = 1.0f;
                            extendedPlayer.setPlaySpeed(speed);
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.alert_btn_cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    private void showVipDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        //设置对话框标题
        builder.setTitle("提示");
        //设置对话框内的文本
        builder.setMessage("VIP用户才可以享受调节语速功能,是否要成为VIP用户?");
        //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, VipCenter.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 执行点击确定按钮的业务逻辑
                dialog.dismiss();
            }
        });
        //使用builder创建出对话框对象
        android.support.v7.app.AlertDialog dialog = builder.create();
        //显示对话框
        dialog.show();
    }

    private void initView() {
        curPos = -1;
        tv_speed = findViewById(R.id.tv_speed);
        curTime = findViewById(R.id.cur_time);
        allTime = findViewById(R.id.all_time);
        previous = findViewById(R.id.preview);
        next = findViewById(R.id.next);
        pause = findViewById(R.id.pause);
        submit = findViewById(R.id.submit);
        previous.setOnClickListener(ocl);
        next.setOnClickListener(ocl);
        pause.setOnClickListener(ocl);
        submit.setOnClickListener(ocl);

        seekbar = findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    if (extendedPlayer.isPlaying()) {
                        extendedPlayer.seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        controlVideo();
        setContent(1, false, false);

        if (ConfigManager.Instance().loadBoolean("isChangePlayer")) {
            tv_speed.setVisibility(View.VISIBLE);
        } else {
            tv_speed.setVisibility(View.GONE);
        }

        tv_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    if (AccountManager.Instace(mContext).userInfo.vipStatus.equals("0")) {
                        showVipDialog();
                    } else {  //调速功能使用
                        switch (count) {
                            case 0:
                                speed = 1.0f;
                                count = 1;
                                break;
                            case 1:
                                speed = 1.2f;
                                count = 2;
                                break;
                            case 2:
                                speed = 1.3f;
                                count = 3;
                                break;
                            case 3:
                                speed = 1.5f;
                                count = 4;
                                break;
                            case 4:
                                speed = 0.8f;
                                count = 5;
                                break;
                            case 5:
                                speed = 0.9f;
                                count = 0;
                                break;
                            default:
                                break;
                        }
                        extendedPlayer.setPlaySpeed(speed);
                        tv_speed.setText(String.format("x%s", String.valueOf(speed).substring(0, 3)));
                        Toast.makeText(mContext, String.valueOf(speed).substring(0, 3) + "倍速播放", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(mContext, Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setPauseBackground() {
        if (extendedPlayer.isPlaying()) {
            pause.setBackgroundResource(R.drawable.pause);
        } else {
            pause.setBackgroundResource(R.drawable.play);
        }
    }

    private void controlVideo() {
        extendedPlayer.setOnPreparedListener(new net.protyposis.android.mediaplayer.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(net.protyposis.android.mediaplayer.MediaPlayer mediaPlayer) {
                try {
                    waitingDialog.dismiss();
                    setPauseBackground();
                    int i = extendedPlayer.getDuration();
                    seekbar.setMax(i);
                    allTime.setText(formatTime(i));
                    videoHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        extendedPlayer.setOnBufferingUpdateListener(new net.protyposis.android.mediaplayer.MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(net.protyposis.android.mediaplayer.MediaPlayer mediaPlayer, int percent) {
                Message msg = Message.obtain();
                msg.arg1 = percent;
                msg.what = 1;
                videoHandler.sendMessage(msg);
            }
        });
        extendedPlayer.setOnCompletionListener(new net.protyposis.android.mediaplayer.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(net.protyposis.android.mediaplayer.MediaPlayer mediaPlayer) {
                videoHandler.sendEmptyMessageDelayed(2, 1000);
            }
        });
        extendedPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if (waitingDialog.isShowing()) {
                    waitingDialog.dismiss();
                }
                return false;
            }
        });
        extendedPlayer.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int status) {
                switch (status) {
                    case 4: //播放
                        if (waitingDialog.isShowing()) {
                            waitingDialog.dismiss();
                        }

                        break;
                    case 7: //完成
                        studyRecordInfo.EndFlg = "1";
                        uploadStudyRecord(studyRecordInfo);
                        break;

                    case 5: //暂停
                        studyRecordInfo.EndFlg = "0";
                        uploadStudyRecord(studyRecordInfo);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void uploadStudyRecord(StudyRecordInfo studyRecordInfo) {
        if (System.currentTimeMillis() - startTime < 1000 * 15) {
            e("--- 时间不够15秒 ---");
            return;
        }
        studyRecordInfo.EndTime = deviceInfo.getCurrentTime();
        if (TextUtils.isEmpty(studyRecordInfo.uid) || studyRecordInfo.uid.equals("0")) {
            return;
        }
        Http.get(UpdateStudyRecordRequestNew.getUrl(studyRecordInfo, "0", "0"), new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
            }

            @Override
            public void onError(Call call, Exception e) {
            }
        });
    }

    private String formatTime(int time) {
        int i = time / 1000;
        int minute = i / 60;
        int second = i % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    private void setContent(int to, boolean nextQuestion, boolean change) {
        if (nextQuestion) {
//            curPos = curPos + minus - 1;
            curPos = getNextQuestion(curPos);
        }
        if (to == 1) {
            curPos++;
        } else {
            curPos--;
        }
        boolean mutiQuestion = false, changeQuestion = true;
        // 结束的位置
        int tempPos = curPos + 1;
        // 起始的位置
        int tempPosMinus = curPos;

        if (answerList == null || answerList.size() == 0) {
            showLong("题库加载失败...");
            finish();
            return;
        }

        if (curPos >= answerList.size()) {
            return;
        }

        String questionId = null;
        try {
            questionId = answerList.get(curPos).id;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (tempPos >= answerList.size()) {
            tempPos--;
        }

        L.e("====  setContent  ==== curPos        : " + curPos);
        L.e("====  setContent  ==== tempPos       : " + tempPos);
        L.e("====  setContent  ==== tempPosMinus  : " + tempPosMinus);


        // 切换上一题和下一题的按钮图片
        if (curPos == 0) {
            handler.sendEmptyMessage(1);
        } else if (curPos == answerList.size() - 1) {
            handler.sendEmptyMessage(3);
        } else {
            handler.sendEmptyMessage(2);
        }

        String curPosFlag = answerList.get(curPos).flag;
        String tempPosFlag = answerList.get(tempPos).flag;
        L.e("====  setContent  ====  curPosFlag   :" + curPosFlag);
        L.e("====  setContent  ====  tempPosFlag  :" + tempPosFlag);
        L.e("====  setContent  ====  answerList   :" + answerList.toString());


        if (curPosFlag.equals("1") && tempPosFlag.equals("1")) {
            mutiQuestion = false;
            changeQuestion = true;
        } else {
            mutiQuestion = true;
            if (to == -1 && curPosFlag.equals("0") && tempPosFlag.equals("1")) {
                changeQuestion = true;
            } else changeQuestion = to == 1 && curPosFlag.equals("1") && tempPosFlag.equals("0");
        }
        if (mutiQuestion) {
            while (answerList.get(tempPos).flag.equals("0")) {
                if (tempPos < answerList.size() - 1) {
                    tempPos++;
                } else {
                    tempPos++;
                    break;
                }
            }
            L.e("====  setContent  ==22== tempPos  : " + tempPos);

            while (answerList.get(tempPosMinus).flag.equals("0")) {
                tempPosMinus--;
            }
            L.e("====  setContent  ==== tempPosMinus  : " + tempPosMinus);

            ListenDataManager.Instance().rowString = "第 "
                    + answerList.get(tempPosMinus).id + " - "
                    + answerList.get(tempPos - 1).id + " 题";
        } else {
            ListenDataManager.Instance().rowString = "第 " + questionId + " 题";
        }
        changeQuestion = changeQuestion || change;
        if (changeQuestion) {
            try {
                resetABRepeat();
                startToPlay();
            } catch (Exception e) {
                e.printStackTrace();
                T.showLong(mContext, "听力播放失败，可尝试在设置中清除音频缓存，重新下载");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
                return;
            }

            studyRecordInfo.TestNumber = answerList.get(curPos).id;
            studyRecordInfo.BeginTime = deviceInfo.getCurrentTime();

            if (section.equals("A") && !isNewType) {
                int id = Integer.parseInt(questionId);
                ArrayList<CetText> list = null;
                while (list == null) {
                    list = new SectionATextOp(mContext).selectData(ListenDataManager.Instance().year, id + "");
                    id--;
                }
                ListenDataManager.Instance().textList = list;
            } else if (section.equals("B") && !isNewType) {
                int id = Integer.parseInt(questionId);
                ArrayList<CetText> list = null;
                while (list == null) {
                    list = new SectionBTextOp(mContext).selectData(ListenDataManager.Instance().year, id + "");
                    id--;
                }
                ListenDataManager.Instance().textList = list;
            } else if (section.equals("A") && isNewType) {
                int id = Integer.parseInt(questionId);
                ArrayList<CetText> list = null;
                while (list == null) {
                    list = new NewTypeTextAOp(mContext).selectData(ListenDataManager.Instance().year, id + "");
                    id--;
                }
                ListenDataManager.Instance().textList = list;
            } else if (section.equals("B") && isNewType) {
                int id = Integer.parseInt(questionId);
                ArrayList<CetText> list = null;
                while (list == null) {
                    list = new NewTypeTextBOp(mContext).selectData(ListenDataManager.Instance().year, id + "");
                    id--;
                }
                ListenDataManager.Instance().textList = list;
            } else if (section.equals("C") && isNewType) {
                int id = Integer.parseInt(questionId);
                ArrayList<CetText> list = null;
                while (list == null) {
                    list = new NewTypeTextCOp(mContext).selectData(ListenDataManager.Instance().year, id + "");
                    id--;
                }
                ListenDataManager.Instance().textList = list;
            }
        }
        if (mutiQuestion) {
            minus = tempPos - tempPosMinus;
        } else {
            minus = 1;
        }
        ListenDataManager.curPos = curPos;
        adapter.notifyDataSetChanged();
    }

    private void startToPlay() {
        try {
            final String path = getSoundPath();
            Log.e("mp3 path: ", path);
            if (path.startsWith("http")) {
                waitingDialog.show();
            }

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        extendedPlayer.startToPlay(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
            T.showLong(mContext, "播放失败，请尝试在设置中清除音频缓存，重新下载");
            return;
        }

    }

    @NonNull
    private String getSoundPath() {
        if (checkLocalFiles()) {
            return Constant.videoAddr
                    + mExamTime + File.separator
                    + answerList.get(curPos).sound;
        } else {
            String type = Constant.APP_CONSTANT.TYPE();
            String path = "http://cetsounds.iyuba.com/" + type
                    + "/" + mExamTime + "/" + answerList.get(curPos).sound;
            return path;
        }
    }

    private boolean checkLocalFiles() {
        String year = mExamTime;
        String fileNoAppend = Constant.videoAddr + year + ".cet4";
        String folder = Constant.videoAddr + year;
        File file1 = new File(folder);
        File file2 = new File(fileNoAppend);
        if (file1.exists()) {
            if (file1.list().length <= 0) {
                file1.delete();
                return false;
            }
            // complete
            return true;
        } else if (file2.exists()) {
            // downloading
            return false;
        } else {
            // no down
            return false;
        }
    }

    private int getNextQuestion(int curPos) {
        String sound = answerList.get(curPos).sound;
        for (int i = curPos; i < answerList.size(); i++) {
            if (i >= answerList.size())
                break;
            if (!sound.equals(answerList.get(i).sound)) {
                return i - 1;
            }
        }
        return curPos;
    }

    @Override
    public void onBackPressed() {
        AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle(R.string.alert_title);
        alert.setMessage(mContext.getString(R.string.exit_test));
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.alert_btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        SectionA.this.finish();
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoHandler.removeCallbacksAndMessages(null);
        if (extendedPlayer != null) {
            extendedPlayer.stopAndRelease();
        }
        adBannerUtil.destroy();
        stopABRepeat();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (extendedPlayer.isPlaying())
//            extendedPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case 0:
                if (resultCode == 100) {

                } else {
                    if (intent != null) {
                        curPos = intent.getExtras().getInt("curPos", 0);
                        curPos--;
                    }
                    if (resultCode == 0) {
                        viewPager.setCurrentItem(1, true);
                    } else if (resultCode == 1) {
                        isExplain = true;
                        viewPager.setCurrentItem(2, true);
                    }
                    setContent(1, false, true);
                }
                break;
            default:
                break;
        }
    }

    public ExtendedPlayer getPlayer() {
        if (extendedPlayer == null) {

        }
        return extendedPlayer;
    }

}
