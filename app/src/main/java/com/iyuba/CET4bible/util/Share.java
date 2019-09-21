/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.iyuba.configation.Constant;
import com.iyuba.core.util.GetLocation;
import com.iyuba.core.util.ScreenShot;
//import com.iyuba.headlinelibrary.manager.HeadlinesConstantManager;
//import com.iyuba.headlinelibrary.model.DownLoadJFResult;
//import com.iyuba.headlinelibrary.network.HeadlineNetwork;
//import com.iyuba.headlinelibrary.util.TimestampConverter;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 分享
 *
 * @author 陈彤
 */
public class Share {
    private Context mContext;
    private PlatformActionListener listener;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ScreenShot.savePic(mContext, Constant.screenShotAddr);
                    break;
            }
        }
    };

    public Share(Context context) {
        mContext = context;
    }

    public void shareMessage(String imagePath, String text, String url, String title) {

        OnekeyShare oks = new OnekeyShare();
        // 分享时Notification的图标和文字
        //oks.setNotification(R.drawable.ic_launcher, Constant.APPName);
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imagePath);
        oks.setImageUrl(url);
        oks.setUrl(url);
        oks.setComment("爱语吧的这款应用" + Constant.APPName + "真的很不错啊~推荐！");
        oks.setSite(Constant.APPName);
        oks.setSiteUrl(url);
        String location[] = GetLocation.getInstance(mContext).getLocation();
        if (!location[1].equals("0") && !location[0].equals("0")) {
            oks.setLatitude(Float.parseFloat(location[0]));
            oks.setLatitude(Float.parseFloat(location[1]));
        }
        oks.setSilent(true);
        if (listener != null) {
            oks.setCallback(listener);
        }
        oks.show(mContext);
    }

    public void prepareMessage(String title, String content, String url) {
//        handler.sendEmptyMessage(0);
        String imagePath = Constant.iconAddr;
        String text = title + url + content;
        shareMessage(imagePath, text, url, title);
    }

    public void setListener(Context context, String id) {
        listener = getListener(mContext, id);
    }

    public static PlatformActionListener getListener(final Context mContext, final String id) {
        return new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("share", "onComplete");

                String srid = "";
                if (platform.getName().equals("QQ")
                        || platform.getName().equals("Wechat")
                        || platform.getName().equals("WechatFavorite")) {
                    srid = "7";
                } else if (platform.getName().equals("QZone")
                        || platform.getName().equals("WechatMoments")
                        || platform.getName().equals("SinaWeibo")
                        || platform.getName().equals("TencentWeibo")) {
                    srid = "19";
                }
//                HeadlineNetwork.getNewDeductPointsForDownloadApi().DuctPointsForDownload(
//                        srid, "1", "1234567890" + TimestampConverter.getTime(),
//                        HeadlinesConstantManager.USERID, HeadlinesConstantManager.APP_ID, id)
//                        .enqueue(new Callback<DownLoadJFResult>() {
//                            @Override
//                            public void onResponse(Call<DownLoadJFResult> call, Response<DownLoadJFResult> response) {
//                                DownLoadJFResult result = response.body();
//                                if (result != null && "200".equals(result.getResult())) {
//                                    Toast.makeText(mContext, "分享成功" + result.getAddcredit() + "分，当前积分为："
//                                                    + result.getTotalcredit() + "分",
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(mContext, "分享成功",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<DownLoadJFResult> call, Throwable t) {
//                                Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
//                            }
//                        });
////                service.integral(srid, 1, getTime(), AccountManager.Instance(mContext).getId(), Constant.APPID, id)
////                        .subscribeOn(Schedulers.io())
////                        .observeOn(AndroidSchedulers.mainThread())
////                        .subscribe(new Subscriber<DownLoadJFResult>() {
////                            @Override
////                            public void onCompleted() {
////                            }
////
////                            @Override
////                            public void onError(Throwable e) {
////                                e.printStackTrace();
////                                Log.e("onError", "分享积分失败");
////                                Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
////                            }
////
////                            @Override
////                            public void onNext(DownLoadJFResult result) {
////                                Log.e("onNext", "分享积分成功---");
////                                if (result != null && "200".equals(result.getResult())) {
////                                    Toast.makeText(mContext, "分享成功" + result.getAddcredit() + "分，当前积分为："
////                                                    + result.getTotalcredit() + "分",
////                                            Toast.LENGTH_SHORT).show();
////                                } else {
////                                    Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
////                                }
////                            }
////                        });
//            }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("share", "onError");
                Toast.makeText(mContext, "分享失败",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("share", "onCancel");
                Toast.makeText(mContext, "分享已取消",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

}
