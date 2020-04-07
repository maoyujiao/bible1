package com.iyuba.wordtest.sign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.EncodeUtils;
import com.google.gson.Gson;
import com.iyuba.module.mvp.BasePresenter;
import com.iyuba.wordtest.bean.SignBean;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.network.HttpManager;
import com.iyuba.wordtest.sign.SignMVPView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

public class SignPresenter extends BasePresenter {

    public void writeBitmapToFile(View view ) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap == null) {
            return;
        }

        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();

        File newpngfile = new File(Environment.getExternalStorageDirectory(), "wordSign.png");
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
        ((SignMVPView)getMvpView()).saveImageFinished(newpngfile.getAbsolutePath());
    }

    public void showShareOnMoment(Context context, final String userID,
                                  final String AppId, String imagePath , final String level) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setPlatform(WechatMoments.NAME);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setImagePath(imagePath);
        oks.setSilent(true);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                startInterfaceADDScore(userID, AppId,level);
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


    @SuppressLint("CheckResult")
    private void startInterfaceADDScore(String userID, String appid, String level) {

        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        final String time = new String(EncodeUtils.base64Decode(dateString.getBytes()));
//                Base64Coder.encode(dateString);

        String url = "http://api.iyuba.cn/credits/updateScore.jsp?srid=82&mobile=1&flag=" + time + "&uid=" + userID
                + "&appid=" + appid+"&idindex="+level;
        Log.d("diao",url);
        HttpManager.getSignApi().getSign("82","1",
                time, WordManager.userid,WordManager.appid,level)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<SignBean>() {
                    @Override
                    public void accept(SignBean bean) throws Exception {
                        if (bean.getResult().equals("200")) {
                            String money = bean.getMoney();
                            String addCredit = bean.getAddcredit();
                            String days = bean.getDays();
                            String totalCredit = bean.getTotalcredit();
                            //打卡成功,您已连续打卡xx天,获得xx元红包,关注[爱语课吧]微信公众号即可提现!
                            float moneyThisTime = Float.parseFloat(money);
                            if (moneyThisTime > 0) {
                                float moneyTotal = Float.parseFloat(totalCredit);
                                ((SignMVPView)getMvpView()).showSucess("打卡成功,获得" + moneyThisTime * 0.01 + "元,总计: " + moneyTotal * 0.01 + "元," + "满10元可在\"爱语课吧\"公众号提现");
                            } else {
                                ((SignMVPView)getMvpView()).showSucess("打卡成功，连续打卡" + days + "天,获得" + addCredit + "积分，总积分: " + totalCredit);
                            }


                        } else {
                            ((SignMVPView)getMvpView()).showFail("打卡失败，您今天已经打过卡了");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });


    }

}
