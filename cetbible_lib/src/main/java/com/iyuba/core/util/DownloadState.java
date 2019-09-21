package com.iyuba.core.util;

public class DownloadState {

    /**
     * 等待下载
     */
    public final static int STATUS_PENDING = 1 << 0;

    /**
     * 正在下载
     */
    public final static int STATUS_RUNNING = 1 << 1;

    /**
     * 暂停下载
     */
    public final static int STATUS_PAUSED = 1 << 2;

    /**
     * 下载成功
     */
    public final static int STATUS_SUCCESSFUL = 1 << 3;

    /**
     * 下载失败
     */
    public final static int STATUS_FAILED = 1 << 4;
}
