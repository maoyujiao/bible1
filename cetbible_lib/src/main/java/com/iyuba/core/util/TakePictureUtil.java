package com.iyuba.core.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TakePictureUtil {
    public static final int REQUEST_TAKE_PHOTO = 11;// 照相
    public static final int REQUEST_GALLERY = 22;// 相册
    public static String photoPath;
    public static String photoCutPath;
    private static String PHOTO_DIR;// 存储照片的位置

    public static File getPhotoFile(Context context) {
        File file = new File(getPhotoFilePath(context));
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String getPhotoFilePath(Context context) {
        if (TextUtils.isEmpty(PHOTO_DIR)) {
            createImgFolders(context);
        }

        photoPath = PHOTO_DIR + "/" + getPhotoName();
        photoCutPath = PHOTO_DIR + "/" + getCutPhotoName();

        return photoPath;
    }

    /**
     * 得到图片的名称
     */
    public static String getPhotoName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssSSS");
        String str = sdf.format(date);
        Random ran = new Random();
        str = str + ran.nextInt(100) + ".jpg";
        return str;
    }

    /**
     * 得到图片的名称
     */
    public static String getCutPhotoName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssSSS");
        String str = sdf.format(date);
        Random ran = new Random();
        str = str + ran.nextInt(100) + "cut.jpg";
        return str;
    }

    private static void createImgFolders(Context context) {
        File compressImgDir;
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            // 如果未加载SD卡，存放在内置卡中
            PHOTO_DIR = context.getCacheDir().getAbsolutePath()
                    + File.separator + "pics" + File.separator;
            compressImgDir = new File(PHOTO_DIR, "CompressPics");
        } else {
            PHOTO_DIR = Environment.getExternalStorageDirectory()
                    + "/DCIM/Camera";
            compressImgDir = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile().toString()
                    + "/iyubaclient/pictures/CompressedPictures");
        }
        BitmapUtil.compressPicDir = compressImgDir;

        File photoDir = new File(PHOTO_DIR);
        if (!photoDir.exists()) {
            photoDir.mkdirs();
            photoDir = null;
        }
    }

    public static String getImageFilePathName(Uri uri, Context context) {
        String[] proj = new String[]{MediaColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, proj, null, null, null);
        c.moveToFirst();
        c.getColumnNames();
        String filePathName = c.getString(0);
        c.close();
        if (filePathName == null) {
            return null;
        }
        if (filePathName.lastIndexOf(".jpg") == -1
                && filePathName.lastIndexOf(".png") == -1
                && filePathName.lastIndexOf(".jpeg") == -1
                && filePathName.lastIndexOf(".bmp") == -1) {
            return null;
        }
        return filePathName;
    }

}
