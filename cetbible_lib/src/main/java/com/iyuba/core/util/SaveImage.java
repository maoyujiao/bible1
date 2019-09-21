/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 保存图片
 *
 * @author 陈彤
 */
public class SaveImage {
    public static void saveImage(String path, Bitmap bitmap) {
        File picture = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(picture);
            bitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(String path, Bitmap bitmap, String name) {
        File picturePackage = new File(path);
        if (!picturePackage.exists()) {
            picturePackage.mkdirs();
        }
        File picture = new File(path + "/" + name);
        try {
            FileOutputStream out = new FileOutputStream(picture);
            bitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static Bitmap resizeImage(Bitmap bitmap, double screenRatio) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        double ratio = (height * 1.0) / width;
        int newWidth, newHeight, resizeWidth, resizeHeight;
        if (screenRatio > ratio) {
            newWidth = (int) (width / screenRatio * ratio);
            newHeight = height;
            resizeWidth = (width - newWidth) / 2;
            resizeHeight = 0;
        } else {
            newWidth = width;
            newHeight = (int) (screenRatio * height / ratio);
            resizeWidth = 0;
            resizeHeight = (height - newHeight) / 2;
        }
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, resizeWidth,
                resizeHeight, newWidth, newHeight);
        if (BitmapOrg != null && !BitmapOrg.isRecycled()) {
            BitmapOrg.recycle();
            BitmapOrg = null;
        }
        System.gc();
        return resizedBitmap;

    }

}
