package com.iyuba.multithread;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author chenzefeng
 *         <p>
 *         封装出的管理类
 */
public class MultiThreadDownloadManager {

    private static final String DOWNLOAD_STATE_DOWNLOADING = "isdownloading";
    private static final String DOWNLOAD_STATE_PAUSING = "ispausing";
    private static final String DOWNLOAD_STATE_NO_DOWNLOADING = "nothingisdownloading";
    private static final String DOWNLOAD_STATE_APP_CLOSING = "appisclosing";

    private static String currentState = null;


    /**
     * 所有的下载任务
     */
    private static HashMap<Integer, FileDownloader> tasks = new HashMap<Integer, FileDownloader>();

    /**
     * 数据库操作
     */
    private static FileService fileService;


//	/**
//	 * 所有下载未完成的任务的下载信息，可根据id获取
//	 */
//	public static HashMap<Integer,DownloadTask> tasks = new HashMap<Integer, FileService.DownloadTask>();

    public static void enQueue(final Context context,
                               final int voaid,
                               final String downloadUrl,
                               final File fileSaveDir,
                               final int threadNum,
                               final DownloadProgressListener listener) {
        if (fileService == null) {
            fileService = new FileService(context);
        }
        DownloadProgressListener progressListener = new DownloadProgressListener() {
            @Override
            public void onProgressUpdate(int id, String downloadurl, int fileDownloadSize) {

                if (listener != null) {
                    listener.onProgressUpdate(id, downloadUrl, fileDownloadSize);
                }
            }

            @Override
            public void onDownloadStoped(int id) {

                if (tasks.isEmpty()) {
                    currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
                }
                if (listener != null) {
                    listener.onDownloadStoped(id);
                }
            }

            @Override
            public void onDownloadStart(FileDownloader fileDownloader, int id, int fileTotalSize) {

                if (!tasks.containsKey(id)) {
                    tasks.put(id, fileDownloader);
                    currentState = DOWNLOAD_STATE_DOWNLOADING;
                }
                if (listener != null) {
                    listener.onDownloadStart(fileDownloader, id, fileTotalSize);
                }
            }

            @Override
            public void onDownloadError(String errorMessage) {

                if (listener != null) {
                    listener.onDownloadError(errorMessage);
                }
            }

            @Override
            public void onDownloadComplete(int id, String savePathFullName) {

                tasks.remove(id);
                if (tasks.isEmpty()) {
                    currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
                }
                if (listener != null) {
                    listener.onDownloadComplete(id, savePathFullName);
                }
            }
        };
        if (tasks.containsKey(voaid)) {
            try {
                if (!tasks.get(voaid).isRunning()) {
                    tasks.get(voaid).download(voaid, progressListener);
                } else {
                    Log.e("enQueue", "the task+" + voaid + " is running");
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            final FileDownloader tempDownloader = new FileDownloader(
                    context,
                    downloadUrl,
                    fileSaveDir,
                    threadNum);
            try {
                tempDownloader.download(voaid, progressListener);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }


    }


    /**
     * 停止所有下载线程
     */
    public static void stopDownloads() {
        if (tasks != null) {
            Iterator iterator = tasks.keySet().iterator();
            while (iterator.hasNext()) {
                FileDownloader type = tasks.get(iterator.next());
                if (type.isRunning()) {
                    type.cancle();
                }
            }
        }
        currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
    }

    /**
     * 停止指定下载线程
     *
     * @param id 任务的id
     */
    public static void stopDownload(int id) {
        if (tasks != null) {
            FileDownloader type = tasks.get(id);
            type.cancle();
        }
        if (tasks.isEmpty()) {
            currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
        }
    }

    /**
     * 数据库中是否有未完成任务
     *
     * @param context
     * @return
     */
    public static boolean hasUnFinishTask(Context context) {
        if (fileService == null) {
            fileService = new FileService(context);
        }
        tasks = fileService.findDownload(context);
        return !tasks.isEmpty();
    }

    /**
     * 根据id返回百分比
     *
     * @param context
     * @return
     */
    public static int getTaskPercentage(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id).getDownloadPercentage();
        }
        return 0;
    }


    /**
     * 根据id彻底移除下载任务
     *
     * @param context
     * @return
     */
    public static void removeTask(int id) {

        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
        if (tasks.isEmpty()) {
            currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
        }
    }

    /**
     * 是否有此task
     *
     * @param context
     * @return
     */
    public static boolean hasTask(int id) {

        return tasks.containsKey(id);
    }

    /**
     * 彻底移除所有下载任务
     *
     * @param context
     * @return
     */
    public static void removeAllTask() {

        if (tasks != null) {
            Iterator iterator = tasks.keySet().iterator();
            while (iterator.hasNext()) {
                FileDownloader type = tasks.get(iterator.next());
                type.cancle();
                File file = new File(type.getDownloadUrl());
                if (file.exists()) {
                    file.delete();
                }
                fileService.delete(type.getDownloadUrl());
            }
        }
        currentState = DOWNLOAD_STATE_NO_DOWNLOADING;
    }

    /**
     * 有正在下载（暂停不算）的任务
     *
     * @return
     */
    public static boolean IsDowning() {
        return DOWNLOAD_STATE_DOWNLOADING.equals(currentState);
    }

    /**
     * 当前正在下载的任务总数
     *
     * @return
     */
    public static int DowningTaskNum() {
        return tasks.size();
    }
}
