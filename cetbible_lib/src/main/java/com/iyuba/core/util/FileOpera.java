package com.iyuba.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 文件打开失败
 *
 * @author chentong
 */
public class FileOpera {
    private Context mContext;

    public FileOpera(Context context) {
        mContext = context;
    }

    public void openFile(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 或者
        intent.setDataAndType(Uri.fromFile(new File(filePath)),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }
}
