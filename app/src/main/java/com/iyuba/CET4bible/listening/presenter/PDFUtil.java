package com.iyuba.CET4bible.listening.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.iyuba.base.http.Http;
import com.iyuba.base.http.HttpCallback;
import com.iyuba.base.util.L;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;

/**
 * PDFUtil
 *
 * @author wayne
 * @date 2018/2/23
 */
public class PDFUtil {

    public String getPDFId(String time) {
        String result = time;
        if (time.length() == 10) {
            result = time.substring(0, 6) + time.substring(8, 10);
        } else if (time.length() == 9) {
            result = time.substring(0, 6) + "0" + time.substring(8, 9);
        }
        L.e("pdf-time   " + time);
        return result;
    }

    public interface Callback {
        void result(boolean result, String message);
    }

    public void pdfIntegral(final Context mContext, String idIndex, final String url, final Callback callback) {
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String flag = "";
        try {
            flag = Base64.encodeToString(
                    URLEncoder.encode(df.format(new Date(System.currentTimeMillis())), "UTF-8").getBytes(), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String integralPath = String.format("http://api.iyuba.com/credits/updateScore.jsp?srid=40&mobile=1" +
                "&flag=%s=&uid=%s&appid=%s&idindex=%s", flag, AccountManager.Instace(mContext).getId(), Constant.APPID, idIndex);

        Log.d("diaodebug",integralPath+"");
        final CustomDialog waitingDialog = WaittingDialog.showDialog(mContext);
        waitingDialog.show();
        Http.get(integralPath, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                if (mContext == null) {
                    return;
                }
                waitingDialog.dismiss();
                IntegralBean bean = new Gson().fromJson(response, IntegralBean.class);
                if ("200".equals(bean.result)) {
                    callback.result(true, url);
                } else {
                    callback.result(false, "Code：" + bean.result + " 积分扣取失败！");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (mContext == null) {
                    return;
                }
                waitingDialog.dismiss();
                callback.result(false, "网络异常");
            }
        });
    }

    public void getPDF(final Context mContext, final String idIndex, boolean englishOnly , final Callback callback) {
        int integerEnglish = 0 ;
        if (englishOnly){
            integerEnglish = 1 ;
        }else {
            integerEnglish = 0;
        }
        String type = Constant.APP_CONSTANT.TYPE().equals("4") ? "cet4" : "cet6";
        final CustomDialog waitingDialog = WaittingDialog.showDialog(mContext);
        waitingDialog.show();

        Http.get("http://class.iyuba.com/getCetPdfFile_new.jsp?lessonType=" +
                type + "&lessonId=" + idIndex + "&isEnglish=" + integerEnglish, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                if (mContext == null) {
                    return;
                }
                waitingDialog.dismiss();
                PDFBean bean = new Gson().fromJson(response, PDFBean.class);

                if ("true".equals(bean.message)) {
                    if (TextUtils.isEmpty(bean.path)) {
                        callback.result(false, "PDF获取失败");
                    } else {
                        String path = "http://class.iyuba.com" + bean.path;
                        if (AccountManager.isVip()) {
                            callback.result(true, path);
                        } else {
                            pdfIntegral(mContext, idIndex, path, callback);
                        }
                    }
                } else {
                    callback.result(false, "PDF获取失败，文件不存在");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (mContext == null) {
                    return;
                }
                waitingDialog.dismiss();
                callback.result(false, "网络异常");
            }
        });
    }


    public void download(String mExamTime, String url, Context mContext) {
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //指定下载路径和下载文件名
        request.setTitle(com.iyuba.configation.Constant.APPName + "-" + mExamTime + "真题PDF");
        request.setDestinationInExternalPublicDir("/iyuba/" + Constant.AppName, mExamTime + ".pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //获取下载管理器
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
    }

    class PDFBean {
        /**
         * exists : true
         * path : /pdf/1620.pdf
         */

        public String message;
        public String path;
    }

    class IntegralBean {

        /**
         * result : 200
         * addcredit : -20
         * totalcredit : 1465
         */

        public String result;
        public String addcredit;
        public String totalcredit;
    }
}
