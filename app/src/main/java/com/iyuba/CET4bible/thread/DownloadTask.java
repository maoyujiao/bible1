/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.thread;

import android.os.Handler;
import android.os.Message;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.mode.DownloadFile;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.listener.ActionFinishCallBack;
import com.iyuba.core.util.UpZipFile;
import com.iyuba.core.widget.dialog.CustomToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class DownloadTask {
    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                UpZipFile.upZipFile(fileWithAppend, Constant.videoAddr, new ActionFinishCallBack() {
                    @Override
                    public void onReceived() {
                        handler.sendEmptyMessage(0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    private DownloadFile downloadFile;
    private ActionFinishCallBack callBack;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    CustomToast.showToast(RuntimeManager.getContext(),
                            R.string.unzip);
                    File file = new File(fileNoAppend);
                    file.delete();
                    break;
                case 0:
                    downloadFile.downloadState = "finish";
                    if (callBack != null) {
                        callBack.onReceived();
                    }
                    break;
                case 1:
                    reNameFile(fileNoAppend, fileWithAppend);
                    CustomToast.showToast(RuntimeManager.getContext(),
                            R.string.unzip);
                    thread.start();
                    break;
                default:
                    break;
            }
        }
    };
    private String folder, fileWithAppend, fileNoAppend;

    public DownloadTask(DownloadFile file) {
        downloadFile = file;
    }

    public void setListener(ActionFinishCallBack callBack) {
        this.callBack = callBack;
    }

    public void start() {
        File file = new File(Constant.videoAddr);
        if (!file.exists()) {
            file.mkdirs();
        }
        folder = Constant.videoAddr + downloadFile.fileName;
        fileWithAppend = folder + downloadFile.fileAppend;
        fileNoAppend = folder + ".cet4";

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                downloadFile.downloadSize = 0;
                downFile();
            }
        });
        thread.start();
    }

    private void downFile() {
        File file = new File(Constant.videoAddr);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(folder);
        if (!file.exists()) {
            try {
                file = new File(fileNoAppend);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        try {
            URL url = new URL(downloadFile.downLoadAddress);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file);
            downloadFile.fileSize = connection.getContentLength();
            byte[] buffer = new byte[1024 * 8];
            int length;
            while (downloadFile.downloadSize < downloadFile.fileSize) {
                length = inputStream.read(buffer);
                downloadFile.downloadSize += length;
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            connection.disconnect();
            if (downloadFile.fileAppend.equals(".zip")) {
                handler.sendEmptyMessage(1);
            } else {
                reNameFile(fileNoAppend, fileWithAppend);
                handler.sendEmptyMessage(0);
            }
        } catch (MalformedURLException e) {

            handler.sendEmptyMessage(-1);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {

            handler.sendEmptyMessage(-1);
            e.printStackTrace();
        } catch (IOException e) {

            handler.sendEmptyMessage(-1);
            e.printStackTrace();
        }

    }

    private boolean reNameFile(String oldFilePath, String newFilePath) {
        File source = new File(oldFilePath);
        File dest = new File(newFilePath);
        return source.renameTo(dest);
    }
}
