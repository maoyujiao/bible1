package com.iyuba.core.util;


import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    public static final int BIT_MIN_WIDTH = 100;
    public static final int BIT_MIN_HEIGHT = 100;
    public static final int BIT_MAX_WIDTH = 400;
    public static final int BIT_MAX_HEIGHT = 600;
    private static final String TAG = "BitmapUtil";
    public static File compressPicDir;

    private static FileOutputStream out;

    /**
     * 由File文件生成bitmap
     *
     * @param pathName  文件路径
     * @param reqWidth  请求宽度
     * @param reqHeight 请求高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // Decode bitmap with inSampleSize set
        // options.inSampleSize = calculateInSampleSize(options, reqWidth,
        // reqHeight);
        options.inSampleSize = computeSampleSizeTay(options, -1, reqWidth
                * reqHeight);

        // options.inSampleSize = 10;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;

        // Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(pathName, options);
        /**
         * 解决照片旋转90度的bug
         */
        int degree = readPictureDegree(pathName);
        bitmap = rotaingImageView(degree, bitmap);
        return bitmap;
    }

    /**
     * 计算图片的压缩比
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    public static int computeSampleSizeTay(BitmapFactory.Options options,

                                           int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength,

                maxNumOfPixels);

        int roundedSize;

        if (initialSize <= 8) {

            roundedSize = 1;

            while (roundedSize < initialSize) {

                roundedSize <<= 1;

            }

        } else {

            roundedSize = (initialSize + 7) / 8 * 8;

        }

        return roundedSize;

    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,

                                                int minSideLength, int maxNumOfPixels) {

        double w = options.outWidth;

        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :

                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        int upperBound = (minSideLength == -1) ? 128 :

                (int) Math.min(Math.floor(w / minSideLength),

                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {

            // return the larger one when there is no overlapping zone.

            return lowerBound;

        }

        if ((maxNumOfPixels == -1) &&

                (minSideLength == -1)) {

            return 1;

        } else if (minSideLength == -1) {

            return lowerBound;

        } else {

            return upperBound;

        }

    }

    /**
     * 压缩文件
     *
     * @param file
     * @return
     */

    public static final File compressFile(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File f = null;

        /**
         * 解决照片旋转90度的bug
         */
        int degree = readPictureDegree(filePath);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = calculateInSampleSize(opts, BIT_MAX_WIDTH, BIT_MAX_HEIGHT);
        opts.inPreferredConfig = Config.ARGB_8888;
        opts.inPurgeable = true;// 同时设置才会有效
        opts.inInputShareable = true;//当系统内存不够时候图片自动被回收
        bitmap = BitmapFactory.decodeFile(filePath, opts);
        if (degree > 0) {
            bitmap = rotaingImageView(degree, bitmap);
        }
        if (compressPicDir == null) {
            compressPicDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "CompressPics");
        }

        if (!compressPicDir.exists()) {
            if (!compressPicDir.mkdirs()) {
                Log.d("bitmap", "failed to create directory");
            }
        }

        f = new File(compressPicDir, System.currentTimeMillis() + "_" + new File(filePath).getName());
        Log.d("bitmap", "压缩后图片路径：" + f.getAbsolutePath());
        try {
            out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (Exception e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    out = null;
                }
            }

        }
        bitmap = null;
        System.gc();

        return f;
    }

    public static File getCompressFileDir() {
        return compressPicDir;
    }

    public static Bitmap toCircleBitmap(Bitmap bitmap)

    {
        if (null == bitmap) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (!(width > 0 && height > 0)) {
            return null;
        }
        int ovalLen = Math.min(width, height);
        Rect src = new Rect((width - ovalLen) / 2, (height - ovalLen) / 2,
                (width - ovalLen) / 2, (height - ovalLen) / 2);
        Bitmap output = Bitmap.createBitmap(ovalLen, ovalLen, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        bitmap = Bitmap.createBitmap(bitmap, src.left, src.top, ovalLen,
                ovalLen, null, true);
        Path path = new Path();
        path.addOval(new RectF(0, 0, ovalLen, ovalLen), Path.Direction.CW);
        BitmapShader tempShader = new BitmapShader(bitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(tempShader);
        canvas.drawPath(path, paint);
        return output;
    }

    /**
     * 把传入的bitmap转换成为一个file文件
     *
     * @param bitmap
     */
    public static File bitmapToFile(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            boolean compress = bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (compress) {
                return file;
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 把一个图片缩放到指定大小
     *
     * @param requireWidth
     * @param requireHeight
     * @param file
     * @return
     */
    public static File scaleFile(int requireWidth, int requireHeight, File file) {

        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleHeight = (float) requireHeight / (float) height;
        float scaleWidht = (float) requireWidth / (float) width;
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return bitmapToFile(newbm, file.getAbsolutePath());

    }

    /**
     * bitmap旋转角度
     *
     * @param bm                要旋转的bitmap
     * @param orientationDegree 顺时针旋转角度
     * @return
     */
    public static Bitmap adjustPhotoRotation(Bitmap bm,
                                             final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
                (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(),
                Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);

        return bm1;
    }

    /*
     * 旋转图片
     *
     * @param angle 角度
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
