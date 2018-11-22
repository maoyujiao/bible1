/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.listening;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.ListenCTestFragment.OnBackPressListener;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.viewpager.ListenCFragmentAdapter;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.widget.BackPlayer;
import com.iyuba.core.widget.TabText;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.play.ExtendedPlayer;

import java.io.File;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class SectionC extends BasisActivity implements OnBackPressListener {
    private static String[] CONTENT;
    private Context mContext;
    private Button pause;
    private TabText original, test, single_test;
    private ViewPager viewPager;
    private TextView title;
    private BackPlayer mPlayer;
    private SeekBar seekbar;
    private TextView curTime, allTime;
    private ListenCFragmentAdapter adapter;
    private int playTimes;
    private boolean backPressed;
    private AdView adView;
    private ImageView favor;
    private ExtendedPlayer extendedPlayer;
    OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(View arg0) {

            switch (arg0.getId()) {
                case R.id.original:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.test:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.comment:
                    viewPager.setCurrentItem(2, true);
                    break;
                case R.id.pause:
                    if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
                        if (extendedPlayer.isPlaying()) {
                            extendedPlayer.pause();
                        } else {
                            extendedPlayer.start();
                        }
                    } else {
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                        } else {
                            mPlayer.start();
                        }
                    }
                    setPauseBackground();
                    break;
//                case R.id.favor:
//                    CustomToast.showToast(mContext, "即将推出功能，请稍候使用");
//                    break;
                default:
                    break;
            }
        }
    };
    Handler videoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
                        int i = extendedPlayer.getCurrentPosition();
                        seekbar.setProgress(i);
                        curTime.setText(formatTime(i));
                        videoHandler.sendEmptyMessageDelayed(0, 1000);
                    } else {
                        int i = mPlayer.getCurrentPosition();
                        seekbar.setProgress(i);
                        curTime.setText(formatTime(i));
                        videoHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
                case 1:
                    seekbar.setSecondaryProgress(msg.arg1 * seekbar.getMax() / 100);
                    break;
                case 2:
                    playTimes++;
                    if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
                        if (playTimes <= 3) {
                            extendedPlayer.startToPlay(formatVideoPath());
                            adapter.setTimes(playTimes);
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.setSubmit(true);
                            adapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(2, true);
                            CustomToast.showToast(mContext, "Section C到此结束");
                            extendedPlayer.pause();
                        }
                    } else {
                        if (playTimes <= 3) {
                            mPlayer.setVideoPath(formatVideoPath());
                            adapter.setTimes(playTimes);
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.setSubmit(true);
                            adapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(2, true);
                            CustomToast.showToast(mContext, "Section C到此结束");
                            mPlayer.pause();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private ImageView iv_more;
    private PopupWindow popupWindow;
    private TextView tv_speed;
    private float speed = 1.0f;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_c);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title = findViewById(R.id.play_title_info);
        title.setText(ListenDataManager.Instance().year);
        CONTENT = mContext.getResources()
                .getStringArray(R.array.listen_c_title);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                setTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        adapter = new ListenCFragmentAdapter(mContext,
                getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        mPlayer = BackgroundManager.Instace().bindService.getPlayer();
        extendedPlayer = new ExtendedPlayer(mContext);

        initView();
        initTabText();
        initAD();
        viewPager.setCurrentItem(1, true);
    }

    private void initView() {

        playTimes = 1;
        tv_speed = findViewById(R.id.tv_speed);
        curTime = findViewById(R.id.cur_time);
        allTime = findViewById(R.id.all_time);
        pause = findViewById(R.id.pause);
        pause.setOnClickListener(ocl);
//        favor = (ImageView) findViewById(R.id.favor);
//        favor.setOnClickListener(ocl);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow(v);
            }
        });
        seekbar = findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
                        if (extendedPlayer.isPlaying()) {
                            extendedPlayer.seekTo(progress);
                        }
                    } else {
                        if (mPlayer.isPlaying()) {
                            mPlayer.seekTo(progress);
                        }
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
        if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
            extendedPlayer.startToPlay(formatVideoPath());
        } else {
            mPlayer.setVideoPath(formatVideoPath());
        }

        if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
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
                                speed = 1.1f;
                                count = 2;
                                break;
                            case 2:
                                speed = 1.2f;
                                count = 3;
                                break;
                            case 3:
                                speed = 1.3f;
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
                        }
                        extendedPlayer.setPlaySpeed(speed);
                        tv_speed.setText("x" + String.valueOf(speed).substring(0, 3));
                        Toast.makeText(mContext, String.valueOf(speed).substring(0, 3) + "倍速播放", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(mContext, Login.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void initPopupWindow(View v) {
        View view = getLayoutInflater().inflate(R.layout.sectionc_popup_window, null);

        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        popupWindow.showAsDropDown(v);

        LinearLayout choose = view.findViewById(R.id.lv_vip_testlib);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    createDialog();
                } else {
                    startActivity(new Intent(mContext, Login.class));
                }

            }
        });

    }

    private void createDialog() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.choose_player);
        int item;
        if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
            item = 1;
        } else {
            item = 0;
        }
        builder.setSingleChoiceItems(R.array.choose_player, item,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 1) {   //调速播放器
                            if (AccountManager.Instace(mContext).userInfo.vipStatus.equals("0")) {
                                showVipDialog();
                            } else {
                                ConfigManager.Instance().putBoolean("isChangePlayer_SectionC", true);
                                tv_speed.setVisibility(View.VISIBLE);
                                if (mPlayer != null && extendedPlayer != null) {
                                    if (mPlayer.isPlaying())
                                        mPlayer.pause();
                                    extendedPlayer.startToPlay(formatVideoPath());
                                }
                            }
                        } else if (which == 0) {
                            ConfigManager.Instance().putBoolean("isChangePlayer_SectionC", false);
                            tv_speed.setVisibility(View.GONE);
                            if (mPlayer != null && extendedPlayer != null) {
                                if (extendedPlayer.isPlaying())
                                    extendedPlayer.pause();
                                mPlayer.setVideoPath(formatVideoPath());
                            }

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

    private void initAD() {
        int isvip = ConfigManager.Instance().loadInt("isvip");
        if (isvip == 0) {
            // Create the adView
            adView = new AdView(this, AdSize.BANNER, "a150c0277858794");
            LinearLayout layout = findViewById(R.id.play_control);
            layout.addView(adView);
            adView.loadAd(new AdRequest());
        }
    }

    private void initTabText() {

        original = findViewById(R.id.original);
        test = findViewById(R.id.test);
        single_test = findViewById(R.id.comment);

        original.setText(CONTENT[0]);
        test.setText(CONTENT[1]);
        single_test.setText(CONTENT[2]);

        original.setOnClickListener(ocl);
        test.setOnClickListener(ocl);
        single_test.setOnClickListener(ocl);

    }

    private void setTab(int arg0) {

        original.dismiss();
        test.dismiss();
        single_test.dismiss();
        switch (arg0) {
            case 0:
                original.getFocus();
                break;
            case 1:
                test.getFocus();
                break;
            case 2:
                single_test.getFocus();
                break;
            default:
                break;
        }
    }

    private void setPauseBackground() {
        if (ConfigManager.Instance().loadBoolean("isChangePlayer_SectionC")) {
            if (extendedPlayer.isPlaying()) {
                pause.setBackgroundResource(R.drawable.pause);
            } else {
                pause.setBackgroundResource(R.drawable.play);
            }
        } else {
            if (mPlayer.isPlaying()) {
                pause.setBackgroundResource(R.drawable.pause);
            } else {
                pause.setBackgroundResource(R.drawable.play);
            }
        }
    }

    private void controlVideo() {
        extendedPlayer.setOnPreparedListener(new net.protyposis.android.mediaplayer.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(net.protyposis.android.mediaplayer.MediaPlayer mediaPlayer) {
                extendedPlayer.start();
                setPauseBackground();
                int i = extendedPlayer.getDuration();
                seekbar.setMax(i);
                allTime.setText(formatTime(i));
                videoHandler.sendEmptyMessage(0);
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
        mPlayer.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                mPlayer.start();
                setPauseBackground();
                int i = mPlayer.getDuration();
                seekbar.setMax(i);
                allTime.setText(formatTime(i));
                videoHandler.sendEmptyMessage(0);
            }
        });
        mPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

                Message msg = Message.obtain();
                msg.arg1 = percent;
                msg.what = 1;
                videoHandler.sendMessage(msg);
            }
        });
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                videoHandler.sendEmptyMessageDelayed(2, 1000);
            }
        });
    }

    private String formatVideoPath() {
        StringBuffer sb = new StringBuffer();
        sb.append(Constant.videoAddr).append(ListenDataManager.Instance().year)
                .append(File.separator).append('C').append(playTimes)
                .append(".mp3");
        return sb.toString();
    }

    private String formatTime(int time) {
        int i = time / 1000;
        int minute = i / 60;
        int second = i % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer.isPlaying())
            mPlayer.pause();

        if (extendedPlayer.isPlaying())
            extendedPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoHandler.removeMessages(0);
        videoHandler.removeMessages(1);
        if (adView != null) {
            adView.stopLoading();
            adView.destroy();
            adView.destroyDrawingCache();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {

        } else {
            AlertDialog alert = new AlertDialog.Builder(mContext).create();
            alert.setTitle(R.string.alert_title);
            alert.setMessage(mContext.getString(R.string.exit_test));
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, getResources()
                            .getString(R.string.alert_btn_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                            int which) {
                            SectionC.this.finish();
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
    }

    @Override
    public void onPresseded(boolean press) {

        backPressed = press;
    }

    @Override
    public void onSubmit(boolean submit) {

        if (submit) {
            adapter.setSubmit(true);
            adapter.notifyDataSetChanged();
        }
    }
}
