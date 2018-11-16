package com.iyuba.trainingcamp.utils;

import android.os.Environment;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.utils
 * @class describe
 * @time 2018/10/20 17:41
 * @change
 * @chang time
 * @class describe
 */
public class FilePath {
    public static String getRecordPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getRecordPathSuffix();
    }
    public static String getTxtPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getTxtPathSuffix();
    }

    public static String getRecordPathSuffix(){
        return "iyuba/"+Constants.TYPE+"/records/";
    }
    public static String getTxtPathSuffix(){
        return "iyuba/"+Constants.TYPE+"/txt/";
    }

    public static String getSharePicPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/iyuba/"+Constants.TYPE+"/";
    }
}
