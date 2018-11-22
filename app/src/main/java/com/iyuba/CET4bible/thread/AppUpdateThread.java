package com.iyuba.CET4bible.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.iyuba.CET4bible.sqlite.mode.DownloadFile;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.ActionFinishCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class AppUpdateThread {
    private DownloadFile downloadFile;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    downloadFile.downloadState = "finish";
                    String old = downloadFile.filePath + downloadFile.fileName;
                    reNameFile(old, old + downloadFile.fileAppend);
                    break;
            }
        }
    };

    public AppUpdateThread(DownloadFile file) {
        downloadFile = file;

    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                downloadFile.downloadSize = 0;
                downFile(new ActionFinishCallBack() {

                    @Override
                    public void onReceived() {

                        handler.sendEmptyMessage(2);
                    }
                });
                Looper.loop();
            }
        });
        thread.start();
    }

    private void downFile(ActionFinishCallBack downloadFinish) {
        Log.e("URL", downloadFile.downLoadAddress);
        String fileFolder = downloadFile.filePath;
        String fileAbsolutePath = fileFolder + downloadFile.fileName;
        String fileFullPath = fileAbsolutePath + downloadFile.fileAppend;
        File fileTemp = new File(fileFullPath);
        if (fileTemp.exists()) {
            downloadFinish.onReceived();
        } else {
            File file = new File(fileFolder);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(fileAbsolutePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            try {
                URL url = new URL(downloadFile.downLoadAddress);
                Log.e("URL", downloadFile.downLoadAddress);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(file);
                downloadFile.fileSize = connection.getContentLength();
                byte[] buffer = new byte[1024 * 8];
                int length;
                Log.e("downloadFile.fileSize", downloadFile.fileSize + "");
                while (downloadFile.downloadSize < downloadFile.fileSize) {
                    length = inputStream.read(buffer);
                    downloadFile.downloadSize += length;
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                connection.disconnect();
                reNameFile(downloadFile.filePath, downloadFile.filePath
                        + Constant.append);
                downloadFinish.onReceived();
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (SocketTimeoutException e) {

                handler.sendEmptyMessage(4);
                e.printStackTrace();
            } catch (IOException e) {

                handler.sendEmptyMessage(4);
                e.printStackTrace();
            }
        }
    }

    private boolean reNameFile(String oldFilePath, String newFilePath) {
        File source = new File(oldFilePath);
        File dest = new File(newFilePath);
        return source.renameTo(dest);
    }
}