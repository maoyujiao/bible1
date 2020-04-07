package com.iyuba.core.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.iyuba.configation.Constant;

import java.io.File;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.core.util
 * @class describe
 * @time 2019/1/14 20:43
 * @change
 * @chang time
 * @class describe
 */
public class UriParseUtils {
    public static Uri getUri(File file, Context mContext){
        Uri uri ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME ,
                    file);
        } else {
            uri =   Uri.fromFile(file);
        }
        return uri ;
    }

}
