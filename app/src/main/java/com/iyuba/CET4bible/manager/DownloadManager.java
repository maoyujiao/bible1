package com.iyuba.CET4bible.manager;

import android.content.Context;

import com.iyuba.CET4bible.sqlite.mode.DownloadFile;
import com.iyuba.configation.RuntimeManager;

import java.util.ArrayList;

public class DownloadManager {
    public static DownloadManager downloadManager;
    public ArrayList<DownloadFile> fileList = new ArrayList<>();
    Context mContext;

    public DownloadManager() {
    }

    public static synchronized DownloadManager Instance() {
        if (downloadManager == null) {
            downloadManager = new DownloadManager();
            downloadManager.mContext = RuntimeManager.getContext();
        }
        return downloadManager;
    }

}
