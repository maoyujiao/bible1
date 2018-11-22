package com.iyuba.CET4bible.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.util.ReadBitmap;
import com.iyuba.core.util.SaveImage;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownLoadAd extends AsyncTask<String, String, Void> {

    public DownLoadAd() {

    }

    /**
     * 这里的String参数对应AsyncTask中的第一个参数 这里的Bitmap返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     *
     * @return
     */
    @Override
    protected Void doInBackground(String... urls) {

        String url = urls[0];
        String type = urls[1];
        Bitmap bitmap = null;
        Context mContext = RuntimeManager.getContext();
        if (NetWorkState.getAPNType() != 2) {
            return null;
        } else {
            try {
                InputStream is = new java.net.URL(url).openStream();
                bitmap = ReadBitmap.readBitmap(mContext, is);
                SaveImage.saveImage(Constant.envir + "/ad", bitmap, type
                        + ".jpg");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                ConfigManager.Instance().putString("updateAD",
                        df.format(new Date()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    @Override
    protected void onPostExecute(Void a) {

    }

    // 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    @Override
    protected void onPreExecute() {
    }

    /**
     * 这里的Intege参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     *
     * @Override protected void onProgressUpdate(Integer... values) { int vlaue
     *           = values[0]; progressBar.setProgress(vlaue); }
     */
}
