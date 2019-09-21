package com.iyuba.multithread;

import android.content.Context;
import android.util.Log;

import com.iyuba.multithread.util.NetStatusUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author http://www.cnblogs.com/hanyonglu/archive/2012/02/20/2358801.html
 */
public class FileDownloader {
    private static final int DOWNLOAD_STATE_DOWNLOADING = 1;
    private static final int DOWNLOAD_STATE_STOP = 2;
    private static final String TAG = "FileDownloader";
    /* 任务id */
    public int taskid;
    private int DOWNLOAD_STATE = -1;
    private Context context;
    private FileService fileService;
    /* 已下载文件长度 */
    private int downloadSize = 0;
    /* 线程数量 */
    private int threadNum = 1;
    /* 原始文件长度 */
    private int fileSize = 0;
    /* 线程数 */
    private DownloadThread[] threads;
    /* 本地保存文件 */
    private File saveFile;
    /* 缓存各线程下载的长度 */
    private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
    /* 每条线程下载的长度 */
    private int block;
    /* 下载路径 */
    private String downloadUrl;


    /**
     * @param context
     * @param downloadUrl
     * @param fileSaveDir
     * @param threadNum
     */
    public FileDownloader(Context context, String downloadUrl,
                          File fileSaveDir, int threadNum) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        fileService = new FileService(this.context);
        /* if(!fileSaveDir.exists()) fileSaveDir.mkdirs(); */
        this.saveFile = fileSaveDir;//
        this.threadNum = threadNum;
    }

    /**
     * 获取Http响应头字段
     *
     * @param http
     * @return
     */
    public static Map<String, String> getHttpResponseHeader(
            HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap<String, String>();

        for (int i = 0; ; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null)
                break;
            header.put(http.getHeaderFieldKey(i), mine);
        }

        return header;
    }

    /**
     * 打印Http头字段
     *
     * @param http
     */
    public static void printResponseHeader(HttpURLConnection http) {
        Map<String, String> header = getHttpResponseHeader(http);

        for (Map.Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey() + ":" : "";
            print(key + entry.getValue());
        }
    }

    /**
     * 打印日志信息
     *
     * @param msg
     */
    private static void print(String msg) {
        Log.i(TAG, msg);
    }

    /**
     * 构建文件下载器
     *
     * @param downloadUrl
     *            下载路径
     * @param fileSaveDir
     *            文件保存完整路径
     * @param threadNum
     *            下载线程数
     */

    /**
     * 获取线程数
     */
    public int getThreadSize() {
        return threads.length;
    }

    /**
     * 获取文件大小
     *
     * @return
     */
    public int getFileSize() {
        return fileSize;
    }

    protected void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 累计已下载大小
     *
     * @param size
     */
    protected synchronized void append(int size) {
        downloadSize += size;
    }

    /**
     * 更新指定线程最后下载的位置
     *
     * @param threadId 线程id
     * @param pos      最后下载的位置
     */
    protected synchronized void update(int threadId, int pos) {
        this.data.put(threadId, pos);
        this.fileService.update(this.downloadUrl, this.data);
    }

    /**
     * 设置http头等等
     */
    public void init() {
        try {
            URL url = new URL(this.downloadUrl);
            threads = new DownloadThread[threadNum];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", downloadUrl);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            printResponseHeader(conn);

            if (conn.getResponseCode() == 200) {
                this.fileSize = conn.getContentLength();// 根据响应获取文件大小
                if (this.fileSize <= 0)
                    throw new RuntimeException("Unkown file size ");

				/*
				 * String filename = getFileName(conn);//获取文件名称 this.saveFile =
				 * new File(fileSaveDir, filename);//构建保存文件
				 */
                Map<Integer, Integer> logdata = fileService
                        .getData(downloadUrl);// 获取下载记录

                if (logdata.size() > 0) {// 如果存在下载记录
                    for (Map.Entry<Integer, Integer> entry : logdata.entrySet())
                        data.put(entry.getKey(), entry.getValue());// 把各条线程已经下载的数据长度放入data中
                }

				/*
				 *
				 * 所有线程已经下载的数据长度改在从数据库获取数据时完成，
				 *
				 *
				 * */
//				if (this.data.size() == this.threads.length) {// 下面计算所有线程已经下载的数据长度
//					for (int i = 0; i < this.threads.length; i++) {
//						print("已经下载的长度 thread " +(i+1) +" : "+ this.data.get(i + 1));
//						this.downloadSize += this.data.get(i + 1);
//					}
//
//					print("已经下载的长度" + this.downloadSize);
//				}


                // 计算每条线程下载的数据长度
                this.block = (this.fileSize % this.threads.length) == 0 ? (this.fileSize / this.threads.length) : (this.fileSize / this.threads.length + 1);
            } else {
                throw new RuntimeException("server no response ");
            }
        } catch (Exception e) {
            print(e.toString());
            throw new RuntimeException("don't connection this url");
        }
    }

    public void cancle() {
        for (int i = 0; i < threads.length; i++) {
            DownloadThread array_element = threads[i];
            if (array_element != null) {
                array_element.interrupt();
            }
        }
        DOWNLOAD_STATE = DOWNLOAD_STATE_STOP;
    }

    public boolean isRunning() {
        return DOWNLOAD_STATE == DOWNLOAD_STATE_DOWNLOADING;
    }

    /**
     * 获取下载的文件在服务器上原始的文件名
     *
     * @param conn
     * @return
     */
    private String getFileName(HttpURLConnection conn) {
        String filename = this.downloadUrl.substring(this.downloadUrl
                .lastIndexOf('/') + 1);

        if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
            for (int i = 0; ; i++) {
                String mine = conn.getHeaderField(i);

                if (mine == null)
                    break;

                if ("content-disposition".equals(conn.getHeaderFieldKey(i)
                        .toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(
                            mine.toLowerCase());
                    if (m.find())
                        return m.group(1);
                }
            }

            filename = UUID.randomUUID() + ".tmp";// 默认取一个文件名
        }

        return filename;
    }

    /**
     * 开始下载文件
     *
     * @param listener 监听下载数量的变化,如果不需要了解实时下载的数量,可以设置为null
     * @return 已下载文件大小
     * @throws Exception
     */
    public void download(final int id, final DownloadProgressListener listener) {

        //init();初始化需要网络操作，所以开线程执行
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    int bufferSize = 0;
                    if (NetStatusUtil.isWifi(context) || NetStatusUtil.is4G(context)) {
                        bufferSize = 8 * 1024;
                    } else if (NetStatusUtil.is3G(context)) {
                        bufferSize = 3 * 1024;
                    } else {
                        bufferSize = 1024;
                    }
                    init();
                    RandomAccessFile randOut = new RandomAccessFile(
                            FileDownloader.this.saveFile, "rw");
                    if (fileSize > 0)
                        randOut.setLength(fileSize);
                    randOut.close();
                    URL url = new URL(downloadUrl);
                    if (data.size() != threads.length) {
                        data.clear();
                        print("data.size() != threads.length");
                        for (int i = 0; i < threads.length; i++) {
                            data.put(i + 1, 0);// 初始化每条线程已经下载的数据长度为0
                        }
                    }
                    DOWNLOAD_STATE = DOWNLOAD_STATE_DOWNLOADING;
                    if (listener != null) {
                        listener.onDownloadStart(FileDownloader.this, id, getFileSize());
                    }
                    for (int i = 0; i < threads.length; i++) {// 开启线程进行下载
                        int downLength = data.get(i + 1);
                        if (downLength < block
                                && downloadSize < fileSize) {// 判断线程是否已经完成下载,否则继续下载
                            threads[i] = new DownloadThread(
                                    FileDownloader.this, url,
                                    saveFile,
                                    block,
                                    data.get(i + 1), i + 1,
                                    bufferSize);
                            threads[i].setPriority(7);
                            threads[i].start();
                        } else {
                            print("threads  :" + i);
                            print("downLength < block   :" + (downLength < block));
                            print("downloadSize < fileSize   :" + (downloadSize < fileSize));
                            threads[i] = null;
                        }
                    }
                    taskid = id;
                    fileService.save(downloadUrl, data);
                    fileService.saveFileSize(downloadUrl, getFileSize());
                    fileService.saveFileId(downloadUrl, id);
                    fileService.saveFilePath(saveFile.getAbsolutePath(), downloadUrl);
                    boolean notFinish = true;// 下载未完成
                    while (notFinish) {// 循环判断所有线程是否完成下载
                        Thread.sleep(900);
                        notFinish = false;// 假定全部线程下载完成

                        for (int i = 0; i < threads.length; i++) {
                            if (threads[i] != null
                                    && !threads[i].isFinish()) {// 如果发现线程未完成下载
                                notFinish = true;// 设置标志为下载没有完成

                                if (threads[i]
                                        .getDownLength() == -1) {// 如果下载失败,再重新下载
                                    threads[i] = new DownloadThread(
                                            FileDownloader.this,
                                            url,
                                            saveFile,
                                            block,
                                            data.get(i + 1),
                                            i + 1,
                                            bufferSize);
                                    threads[i]
                                            .setPriority(7);
                                    threads[i].start();
                                }
                            }
                        }

                        if (listener != null)
                            listener.onProgressUpdate(id, downloadUrl, downloadSize);// 通知目前已经下载完成的数据长度

                        if (DOWNLOAD_STATE == DOWNLOAD_STATE_STOP) {
                            break;
                        }
                    }
                    if (DOWNLOAD_STATE == DOWNLOAD_STATE_STOP) {
                        if (listener != null) {
                            listener.onDownloadStoped(id);
                        }
                    } else {
                        fileService.delete(downloadUrl);
                        if (listener != null) {
                            listener.onDownloadComplete(id,
                                    saveFile.getAbsolutePath());
                        }
                    }
                } catch (Exception e) {
                    print(e.toString());
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onDownloadError(e.getMessage());
                    }
                }
            }
        });
        thread.start();
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getSaveFullPath() {
        return saveFile.getAbsolutePath();
    }

    public int getDownloadSize() {
        return downloadSize;
    }

    protected void setDownloadSize(int downloadSize) {
        this.downloadSize = downloadSize;
    }

    public int getDownloadPercentage() {
        return (downloadSize * 100 / fileSize);
    }

    public int getTaskid() {
        return taskid;
    }

    protected void setTaskid(int taskid) {
        this.taskid = taskid;
    }
}
