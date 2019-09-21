package com.iyuba.core.downloadprovider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.iyuba.configation.Constant;
import com.iyuba.core.downloadprovider.providers.DownloadManager;
import com.iyuba.core.downloadprovider.providers.DownloadManager.Request;

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
        if (cursor != null) {
            mIdColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
            mTitleColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE);
            mTotalBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            mCurrentBytesColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            mStatusColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                long downloadId = cursor.getLong(mIdColumnId);
                String title = cursor.getString(mTitleColumnId);
                if (title.length() > 4) {
                    try {
                        int voaId = Integer.valueOf(title.substring(0,
                                title.length() - 4));
                        idMap.put(voaId, downloadId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            request.setDestinationInExternalPublicDir("/" + "/iYuba/" + Constant.APPName + "/res", "/");
            request.setDescription(Constant.APPName);
            long id = mDownloadManager.enqueue(request);
            idMap.put(voaId, id);
            return true;
        }
    }

    public boolean addDownloadAll(int voaId, String urlZip, String urlVideo) {
        if (idMap.get(voaId) != null) {
            return false;
        } else {
            Uri srcUriZip = Uri.parse(urlZip);
            Request requestZip = new Request(srcUriZip);
            requestZip.setDestinationInExternalPublicDir("/" + "/iYuba/" + Constant.APPName + "/res", "/");
            requestZip.setDescription(Constant.APPName);
            long idZip = mDownloadManager.enqueue(requestZip);
            idMap.put(voaId, idZip);

            Uri srcUriVideo = Uri.parse(urlVideo);
            Request requestVideo = new Request(srcUriVideo);
            requestVideo.setDestinationInExternalPublicDir("/" + "/iYuba/" + Constant.APPName + "/res", "/");
            requestVideo.setDescription(Constant.APPName);
            long idVideo = mDownloadManager.enqueue(requestVideo);
            idMap.put(voaId, idVideo);

            return true;
        }
    }

    public void pauseDownload(int voaId) {
        mDownloadManager.pauseDownload(idMap.get(voaId));
    }

    public void resumeDownload(int voaId) {
        mDownloadManager.resumeDownload(idMap.get(voaId));
    }

    public void removeDownload(int voaId) {
        mDownloadManager.remove(idMap.get(voaId));
    }

    public DownloadInfoSimp query(int voaId) {
        if (idMap.get(voaId) != null) {
            DownloadInfoSimp info = new DownloadInfoSimp();
            DownloadManager.Query baseQuery = new DownloadManager.Query().setFilterById(idMap.get(voaId));
            Cursor cursor = mDownloadManager.query(baseQuery);
            if (cursor != null) {
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
                cursor.close();
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

    public int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{-1, -1, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = mDownloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return bytesAndStatus;
    }

    private static class DownloadManagerProxyHolder {
        private static DownloadManagerProxy instance = new DownloadManagerProxy(
                mContext);
    }
}
