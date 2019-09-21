package com.iyuba.core.util;

import android.content.Context;

import com.iyuba.configation.Constant;

import java.io.File;

/**
 * 清缓存
 *
 * @author chentong
 */
public class ClearBuffer {
    private String filepath;
    private Context mContext;

    public ClearBuffer(Context context) {
        filepath = Constant.videoAddr;
        mContext = context;
    }

    public void setPath(String path) {
        filepath = Constant.envir + "/" + path;
        File file = new File(filepath);
        file.delete();
    }

    public boolean Delete() {
        File file = new File(filepath);
        if (file.isFile()) {
            file.delete();
            return true;
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            if (files.length == 0) {
                return false;
            } else {
                for (int i = 0; i < files.length; i++) {
                    file = files[i];
                    if (file.isDirectory()) {
                        File files1[] = file.listFiles();
                        if (files1.length == 0) {
                        } else {
                            for (int j = 0; j < files1.length; j++) {
                                files1[j].delete();
                            }
                            file.delete();
                        }
                    } else {
                        file.delete();
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public void updateDB() {
    }
}
