package com.iyuba.trainingcamp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.SignBean;
import com.iyuba.trainingcamp.event.ShareEvent;
import com.iyuba.trainingcamp.http.Http;
import com.iyuba.trainingcamp.http.HttpCallback;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.codec.binary.Base64;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;

//
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;

/**
 * @author yq QQ:1032006226
 */
public class GoldShareActivity extends BaseActivity {
    // Content View Elements
    public static GoldShareActivity activity;
    @BindView(R2.id.background)
    ImageView mBackground;
    @BindView(R2.id.close)
    TextView mClose;
    @BindView(R2.id.user_pic)
    ImageView mUser_pic;
    @BindView(R2.id.share_txt)
    TextView mShare_txt;
    @BindView(R2.id.introduce)
    TextView mIntroduce;
    @BindView(R2.id.qr_app)
    ImageView mQr_app;
    @BindView(R2.id.share_ll)
    LinearLayout shareLL;
    @BindView(R2.id.share_btn)
    TextView mShare_btn;
    @BindView(R2.id.root)
    RelativeLayout mRoot;

    Context context;
    Random mRandom;
    private int score;
    private String level;

    // End Of Content View Elements

    private void bindViews() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_gold_share);
        ButterKnife.bind(this);
        bindViews();
        activity = this;
        String userid = GoldApp.getApp(context).userId;
        context = this;

        score = Integer.parseInt(getIntent().getStringExtra("score"));
        level = getIntent().getStringExtra("level");
        Glide.with(context).load("http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                + userid + "&size=middle").asBitmap().centerCrop().into(new BitmapImageViewTarget(mUser_pic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mUser_pic.setImageDrawable(circularBitmapDrawable);
            }
        });

        mRandom = new Random();
        Glide.with(context).load("http://static.iyuba.com/images/mobile/" + mRandom.nextInt(31) + ".jpg")
                .placeholder(R.drawable.trainingcamp_place).into(mBackground);
        mIntroduce.setText("长按识别图中二维码\n下载爱语吧应用,学英语练听力\n天天学习,每日红包");
        shareLL.setVisibility(View.INVISIBLE);
        mShare_txt.setText(GoldApp.getApp(context).getUserName() + "\t\t\t" +
                TimeUtils.getCurTime() + "\n完成今日第"
                + level + "关, 得分 " + score + " 分 ");
//        SpannableStringBuilder style = new SpannableStringBuilder(mShare_txt.getText().toString());
//        int length = GoldApp.getApp(context).getUserName().length();
//        style.setSpan(new ForegroundColorSpan(Color.argb(255,128 ,0 ,128)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
//        mShare_txt.setText(style);

    }


    public void showShareOnMoment(Context context, final String userID, final String AppId) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setPlatform(WechatMoments.NAME);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setImagePath(FilePath.getSharePicPath() + "share.png");
        oks.setSilent(true);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                startInterfaceADDScore(userID, AppId);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享失败===", throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("okCallbackonError", "onError");
                Log.e("--分享取消===", "....");
            }
        });
        // 启动分享GUI
        oks.show(context);
    }

    public void writeBitmapToFile() {

        mRoot.setDrawingCacheEnabled(true);
        mRoot.buildDrawingCache();
        Bitmap bitmap = mRoot.getDrawingCache();
        if (bitmap == null) {
            return;
        }
        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();

        File newpngfile = new File(FilePath.getSharePicPath() + "share.png");
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

    private void showCloseAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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

    private void startInterfaceADDScore(String userID, String appid) {

        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
//        final String time = Base64Coder.encode(dateString);

        final String time = new String(Base64.encodeBase64(dateString.getBytes()));

        String url = "http:api.iyuba.com/credits/updateScore.jsp?srid=82&mobile=1&flag=" + time + "&uid=" + userID
                + "&appid=" + appid;
        Http.get(url, new HttpCallback() {

            @Override
            public void onSucceed(Call call, String response) {
                final SignBean bean = new Gson().fromJson(response, SignBean.class);
                if (bean.getResult().equals("200")) {
                    final String money = bean.getMoney();
                    final String addCredit = bean.getAddcredit();
                    final String days = bean.getDays();
                    final String totalCredit = bean.getTotalcredit();

                    //打卡成功,您已连续打卡xx天,获得xx元红包,关注[爱语课吧]微信公众号即可提现!
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (money != null) {
                                float moneyThisTime = Float.parseFloat(money);
                            }
                            MobclickAgent.onEvent(context, "dailybonus");
                            if (money == null) {
                                float moneyTotal = Float.parseFloat(totalCredit);
                                Toast.makeText(context, "分享成功," + "您已获得" + Integer.parseInt(addCredit) * 0.01 + "元,总计: " + Integer.parseInt(totalCredit) * 0.01 + "元," + "满10元可在\"爱语课吧\"公众号提现", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(context, "分享成功，您已获得" + Integer.parseInt(money) * 0.01 + "元，总计: " + Integer.parseInt(totalCredit) * 0.01 + "元", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "您今日已分享", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(Call call, Exception e) {

            }
        });

    }

    public static void start(Context context, String level, String score) {
        Intent intent = new Intent(context, GoldShareActivity.class);
        intent.putExtra("level", level);
        intent.putExtra("score", score);
        context.startActivity(intent);
    }

    @OnClick(R2.id.close)
    public void onMCloseClicked() {
        showCloseAlert();

    }

    @OnClick(R2.id.share_btn)
    public void onMShareBtnClicked() {
        mShare_btn.setVisibility(View.INVISIBLE);
        shareLL.setVisibility(View.VISIBLE);
        writeBitmapToFile();
        EventBus.getDefault().post(new ShareEvent(FilePath.getSharePicPath() + "share.png"));
    }
}
