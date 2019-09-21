package com.iyuba.core.sqlite.mode;

import com.iyuba.biblelib.R;

/**
 * ��ʾһ���ļ�ʵ�� **
 * <p>
 * 文件浏览器相关类
 */
public class FileInfo {
    public String Name;
    public String Path;
    public long Size;
    public String type;
    public boolean IsDirectory = false;
    public int FileCount = 0;
    public int FolderCount = 0;

    public int getIconResourceId() {
        if (IsDirectory) {
            return R.drawable.folder;
        } else {
            if (type.equals("application/vnd.android.package-archive/*")) {
                return R.drawable.apk;
            } else if (type.equals("video/*")) {
                return R.drawable.video;
            } else if (type.equals("audio/*")) {
                return R.drawable.audio;
            } else if (type.equals("image/*")) {
                return R.drawable.image;
            } else if (type.equals("text/*")) {
                return R.drawable.txt;
            } else {
                return R.drawable.doc;
            }
        }
    }
}