package com.iyuba.core.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.iyuba.configation.Constant;
import com.iyuba.core.util.DownloadManager.Request;

import java.util.HashMap;
import java.util.Map;

public class DownloadManagerProxy {
    private static Context mContext;
    private DownloadManager mDownloadManager;
    private Map<Integer, Long> idMap;

    private int mIdColumnId;
    private int mTitleColumnId;
    private int mStatusColumnId;
    private int mTotalBytesColumnId;
    private int mCurrentBytesColumnId;

    private DownloadManagerProxy(Context mContext) {
        idMap = new HashMap<Integer, Long>();
        mDownloadManager = new DownloadManager(mContext.getContentResolver(), mContext.getPackageName());
        mDownloadManager.setAccessAllDownloads(true);
        DownloadManager.Query baseQuery = new DownloadManager.Query()
                .setOnlyIncludeVisibleInDownloadsUi(true);
        Cursor cursor = mDownloadManager.query(baseQuery.orderBy(
                DownloadManager.COLUMN_TOTAL_SIZE_BYTES,
                DownloadManager.Query.ORDER_DESCENDING));
        mIdColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
        mTitleColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE);
        mTotalBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
        mCurrentBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
        mStatusColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                long downloadId = cursor.getLong(mIdColumnId);
                String title = cursor.getString(mTitleColumnId);
                if (title.length() > 4) {
                    int voaId = Integer.valueOf(title.substring(0,
                            title.length() - 4));

                    idMap.put(voaId, downloadId);
                }
            }
            cursor.close();
        }
    }

    public static DownloadManagerProxy getInstance(Context mContext) {
        DownloadManagerProxy.mContext = mContext;
        return DownloadManagerProxyHolder.instance;
    }

    public boolean addDownload(int voaId, String url) {
        if (idMap.get(voaId) != null) {
            return false;
        } else {
            Uri srcUri = Uri.parse(url);
            Request request = new Request(srcUri);
            request.setDestinationInExternalPublicDir("/" + Constant.AppName, "/");
            request.setDescription(Constant.AppName);
            long id = mDownloadManager.enqueue(request);
            idMap.put(voaId, id);

            return true;
        }
    }

    public void pauseDownload(int voaId) {
        mDownloadManager.pauseDownload(idMap.get(voaId));
    }

    public void resumeDownload(int voaId) {
        mDownloadManager.resumeDownload(idMap.get(voaId));
    }

    public DownloadInfo query(int voaId) {
        if (idMap.get(voaId) != null) {
            DownloadInfo info = new DownloadInfo();
            DownloadManager.Query baseQuery = new DownloadManager.Query().setFilterById(idMap.get(voaId));
            Cursor cursor = mDownloadManager.query(baseQuery);

            cursor.moveToFirst();
            if (cursor != null && !cursor.isAfterLast()) {
                info.totalBytes = cursor.getLong(mTotalBytesColumnId);
                info.currentBytes = cursor.getLong(mCurrentBytesColumnId);
                info.state = cursor.getInt(mStatusColumnId);
                info.percent = getProgressValue(info.totalBytes, info.currentBytes);
                cursor.close();

                return info;
            } else {
                idMap.remove(voaId);
            }
        }

        return null;
    }

    private int getProgressValue(long totalBytes, long currentBytes) {
        if (totalBytes == -1) {
            return 0;
        }
        return (int) (currentBytes * 100 / totalBytes);
    }

    private static class DownloadManagerProxyHolder {
        private static DownloadManagerProxy instance = new DownloadManagerProxy(
                mContext);
    }
}
