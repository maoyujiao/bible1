package com.iyuba.activity.sign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.configation.ConstantManager;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;


@SuppressLint({"SimpleDateFormat", "SetTextI18n"})
public class SignActivity extends Activity {

    private ImageView imageView;
    private ImageView qrImage;
    private TextView tv1, tv2, tv3;
    private Context mContext;
    private TextView sign;
    private ImageView userIcon;
    private TextView tvShareMsg;
    private int signStudyTime = 2 * 60;
    private String loadFiledHint = "打卡加载失败";
    private int totalDays = 0;

    String shareTxt;
    String getTimeUrl;
    LinearLayout ll;
    ProgressDialog dialog;
    String addCredit = "";//Integer.parseInt(bean.getAddcredit());
    String days = "";//Integer.parseInt(bean.getDays());
    String totalCredit = "";//bean.getTotalcredit();
    String money = "";

    private TextView tvUserName;
    private TextView tvAppName;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mContext = this;


        setContentView(R.layout.activity_sign);

        initView();
        initData();
        initBackGround();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initData() {

        String uid = AccountManager.Instace(mContext).userId;

        final String url = String.format(Locale.CHINA,
                "http://daxue.iyuba.cn/ecollege/getMyTime.jsp?uid=%s&day=%s&flg=1", uid, getDays());
        Log.d("dddd", url);
        getTimeUrl = url;
        dialog.setMessage("正在加载学习时间,请稍等...");
        dialog.show();
        Http.get(url, new HttpCallback() {

            @Override
            public void onSucceed(Call call, String response) {
                try {
                    if (null != dialog) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    final StudyTimeBeanNew bean = new Gson().fromJson(response, StudyTimeBeanNew.class);
                    Log.d("dddd", response);
                    if ("1".equals(bean.getResult())) {
                        final int time = Integer.parseInt(bean.getTotalTime());
                        int totaltime = Integer.parseInt(bean.getTotalDaysTime());
                        totalDays = Integer.parseInt(bean.getTotalDays());
                        String url = "http://static.iyuba.cn/images/mobile/" + (totalDays%30) + ".jpg";

                        Glide.with(mContext).load(url).placeholder(R.drawable.place).dontAnimate().into(imageView);
                        tv1.setText("学习天数:\n" + bean.getTotalDays() + "");
                        tv2.setText("单词数:\n" + bean.getTotalWords() + "");
                        int rankRate = 100 - Integer.parseInt(bean.getRanking()) * 100 / Integer.parseInt(bean.getTotalUser());
                        tv3.setText("超越了：\n" + rankRate + "%同学");
                        shareTxt = bean.getSentence() + "我在爱语吧坚持学习了" + bean.getTotalDays() + "天,积累了" + bean.getTotalWords()
                                + "单词如下";

                        if (time < signStudyTime) {
                            sign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    toast(String.format(Locale.CHINA, "打卡失败，当前已学习%d秒，\n满%d分钟可打卡", time, signStudyTime / 60));

                                }
                            });
                        } else {
                            sign.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onClick(View v) {

                                    qrImage.setVisibility(View.VISIBLE);
                                    sign.setVisibility(View.GONE);
                                    tvShareMsg.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_close).setVisibility(View.INVISIBLE);
                                    tvShareMsg.setText("长按图片识别二维码");
                                    tvShareMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                                    findViewById(R.id.tv_desc).setVisibility(View.VISIBLE);
                                    tvShareMsg.setBackground(getResources().getDrawable(R.drawable.sign_bg_yellow));

                                    writeBitmapToFile();


                                    findViewById(R.id.tv_desc).setVisibility(View.INVISIBLE);

                                    showShareOnMoment(mContext, AccountManager.Instace(mContext).userId + "", Constant.APPID + "");
                                }
                            });
                        }
                    } else {
                        toast(loadFiledHint + bean.getResult());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(loadFiledHint + "！！");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dialog.dismiss();
            }
        });
    }


    private void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    private long getDays() {
        //东八区;
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.set(1970, 0, 1, 0, 0, 0);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        long intervalMilli = now.getTimeInMillis() - cal.getTimeInMillis();
        long xcts = intervalMilli / (24 * 60 * 60 * 1000);
        return xcts;
    }



    private void initView() {

        imageView = findViewById(R.id.iv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        sign = findViewById(R.id.tv_sign);
        ll = findViewById(R.id.ll);
        qrImage = findViewById(R.id.tv_qrcode);
        userIcon = findViewById(R.id.iv_userimg);
        tvUserName = findViewById(R.id.tv_username);
        tvAppName = findViewById(R.id.tv_appname);
        tvShareMsg = findViewById(R.id.tv_sharemsg);


        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseAlert();
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);//循环滚动
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);//false不能取消显示，true可以取消显示

    }

    private void showCloseAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("温馨提示");
                dialog.setMessage("点击下面的打卡按钮,成功分享至微信朋友圈,可以领取红包哦!您确定要退出吗?");
                dialog.setPositiveButton("留下打卡", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("去意已决", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initBackGround() {




        String userIconUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                + AccountManager.Instace(mContext).userId + "&size=middle";
        Glide.with(mContext).load(userIconUrl).placeholder(R.drawable.noavatar_small).dontAnimate().transform(new CircleTransform(mContext)).into(userIcon);
//        Log.d("bible", "initBackGround: " + AccountManager.Instance(mContext).userId + ":" + AccountManager.Instance(mContext).userName);
        if (TextUtils.isEmpty(AccountManager.Instace(mContext).userName)) {
            tvUserName.setText(AccountManager.Instace(mContext).userId);
        } else {
            tvUserName.setText(AccountManager.Instace(mContext).userName);
        }
        if (Constant.APP_CONSTANT.TYPE() .equals("4") ){
            tvAppName.setText(Constant.APPName + "--四级考试必备的好帮手!");
        }else {
            tvAppName.setText(Constant.APPName + "--六级考试必备的好帮手!");
        }
        Glide.with(mContext).load("").placeholder(R.drawable.qrcode).into(qrImage);

    }


    @SuppressLint("NewApi")
    public void writeBitmapToFile() {
        View view = findViewById(R.id.rr);
//        View view1 = LayoutInflater.from(mContext).inflate(R.layout.activity_sign,null);
        ((TextView)findViewById(R.id.tv_desc)).setText("在 "+ Constant.APPName+" 上完成了打卡");
//        int[] location = new  int[2];

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        if (bitmap == null) {
//             Log.d("bible", "writeBitmapToFile: ");
            return;
        }
        Log.d("bible", "writeBitmapToFile: ");


        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();

        File newpngfile = new File(Environment.getExternalStorageDirectory(), "iyuba/aaa.png");
        if (newpngfile.exists()) {
            newpngfile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(newpngfile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * @author
     * @time
     * @describe 启动获取积分(红包的接口)
     */

    private void startInterfaceADDScore(String userID, String appid) {

        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
//        final String time = Base64Coder.encode(dateString);

        final String time = new String(Base64.encodeBase64(dateString.getBytes()));

        String url = "http://api.iyuba.cn/credits/updateScore.jsp?srid=81&mobile=1&flag="+ time + "&uid=" + userID
                + "&appid=" + appid;
        Http.get(url, new HttpCallback() {

            @Override
            public void onSucceed(Call call, String response) {
                final SignBean bean = new Gson().fromJson(response, SignBean.class);
                if (bean.getResult().equals("200")) {
                    money = bean.getMoney();
                    addCredit = bean.getAddcredit();
                    days = bean.getDays();
                    totalCredit = bean.getTotalcredit();

                    //打卡成功,您已连续打卡xx天,获得xx元红包,关注[爱语课吧]微信公众号即可提现!
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            float moneyThisTime = Float.parseFloat(money);
                            MobclickAgent.onEvent(mContext, "dailybonus");
                            if (moneyThisTime > 0) {
                                float moneyTotal = Float.parseFloat(totalCredit);
                                DecimalFormat format = new DecimalFormat("##0.00");
                                String money = format.format(((moneyThisTime) * 0.01));
                                String moneyTotalS = format.format(((moneyTotal) * 0.01));
                                Toast.makeText(mContext, "打卡成功," + "您已连续打卡" + days + "天,  获得" + money + "元,总计: " + moneyTotalS  + "元," + "满10元可在\"爱语课吧\"公众号提现", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(mContext, "打卡成功，连续打卡" + days + "天,获得" + addCredit + "积分，总积分: " + totalCredit, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "您今日已打卡", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }


            @Override
            public void onError(Call call, Exception e) {

            }
        });


    }

    public void showShareOnMoment(Context context, final String userID, final String AppId) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setPlatform(WechatMoments.NAME);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setImagePath(Environment.getExternalStorageDirectory() + "/iyuba/aaa.png");

        oks.setSilent(true);

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                startInterfaceADDScore(userID, AppId);
                finish();
                ToastUtil.showToast(mContext, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享失败===", throwable.toString());
                finish();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("okCallbackonError", "onCancel");
                Log.e("--分享取消===", "....");
                finish();
            }
        });
        // 启动分享GUI
        oks.show(context);
    }

}



