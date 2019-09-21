package com.iyuba.multithread;

public interface DownloadProgressListener {
    void onDownloadStart(FileDownloader fileDownloader, int id, int fileTotalSize);

    void onDownloadStoped(int id);

    void onProgressUpdate(int id, String downloadurl, int fileDownloadSize);

    void onDownloadComplete(int id, String savePathFullName);

    void onDownloadError(String errorMessage);
}
