/**
 *
 */
package com.iyuba.core.activity.me;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.thread.UploadFile;
import com.iyuba.core.util.FileOpera;
import com.iyuba.core.util.ReadBitmap;
import com.iyuba.core.util.SaveImage;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 修改头像
 *
 * @author ct
 * @version 1.0
 * @para "regist" 是否来自注册 是则下一步补充详细信息
 */
public class UpLoadImage extends BasisActivity {
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int NONE = 0;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private ImageView image;
    private Button upLoad, photo, local, skip, back;
    private boolean fromRegist = false;
    private Context mContext;
    private CustomDialog waitingDialog;
    private boolean isSend = false;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitingDialog.show();
                    break;
                case 1:
                    waitingDialog.dismiss();
                    break;
                case 2:
                    isSend = false;
                    String picUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                            + AccountManager.Instace(mContext).userId
                            + "&size=middle";
                    String fileUrl = RuntimeManager.getContext()
                            .getExternalCacheDir().getAbsolutePath() + "/" + picUrl.hashCode();
                    new FileOpera(mContext).deleteFile(fileUrl);
                    GitHubImageLoader.Instace(mContext).clearMemoryCache();
                    showDialog(
                            getResources().getString(R.string.uploadimage_success),
                            new OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (fromRegist) {
                                        Intent intent = new Intent(mContext,
                                                EditUserInfoActivity.class);
                                        intent.putExtra("regist", fromRegist);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        onBackPressed();
                                    }
                                }
                            });
                    break;
                case 3:
                    isSend = false;
                    showDialog(
                            getResources().getString(
                                    R.string.uploadimage_failupload),
                            new OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };
    private String tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
    private File tempFile = new File(tempFilePath);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        fromRegist = getIntent().getBooleanExtra("regist", fromRegist);
        waitingDialog = WaittingDialog.showDialog(mContext);
        initWidget();
    }

    private void initWidget() {

        image = findViewById(R.id.userImage);
        back = findViewById(R.id.upload_back_btn);
        upLoad = findViewById(R.id.upLoad);
        photo = findViewById(R.id.photo);
        local = findViewById(R.id.local);
        skip = findViewById(R.id.skip);
        try {
            File userPic = new File(RuntimeManager.getContext()
                    .getExternalFilesDir(null).toString()
                    + "/user.jpg");
            if (!userPic.exists()) {
                image.setImageBitmap(ReadBitmap.readBitmap(mContext,
                        R.drawable.defaultavatar));
            } else {
                image.setImageBitmap(ReadBitmap.readBitmap(mContext,
                        new FileInputStream(userPic)));
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(tempFile));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
            }
        });
        local.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // // 手机相册中选择

                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });
        upLoad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isSend) {
                    isSend = !isSend;
                    handler.sendEmptyMessage(0);
                    CustomToast.showToast(mContext,
                            R.string.uploadimage_uploading);
                    new uploadThread().start();
                } else {
                    CustomToast.showToast(mContext, R.string.submitting);
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (fromRegist) {
                    Intent intent = new Intent(mContext,
                            EditUserInfoActivity.class);
                    intent.putExtra("regist", fromRegist);
                    startActivity(intent);
                } else {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                    startPhotoZoom(data.getData(), 150);
                break;

            case PHOTO_REQUEST_CUT:
                setPicToView(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
//        if (bundle != null) {
//            Bitmap photo = bundle.getParcelable("data");
//            Drawable drawable = new BitmapDrawable(getResources(),photo);
//            SaveImage.saveImage(tempFilePath, photo);
//            image.setBackground(drawable);
//        }
        Drawable drawable = null;
        try {
            drawable = new BitmapDrawable(new FileInputStream(new File(tempFilePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image.setBackground(drawable);
        Bitmap bitmap = BitmapFactory.decodeFile(tempFilePath);
        SaveImage.saveImage(tempFilePath, bitmap);
    }

    private void showDialog(String mess, OnClickListener ocl) {
        new AlertDialog.Builder(UpLoadImage.this)
                .setTitle(R.string.alert_title).setMessage(mess)
                .setNegativeButton(R.string.alert_btn_ok, ocl).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    class uploadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                UploadFile.post("http://api.iyuba.com.cn/v2/avatar?uid="
                                + AccountManager.Instace(mContext).userId,
                        new OperateCallBack() {

                            @Override
                            public void success(String message) {
                                handler.sendEmptyMessage(1);
                                handler.sendEmptyMessage(2);
                            }

                            @Override
                            public void fail(String message) {
                                handler.sendEmptyMessage(1);
                                handler.sendEmptyMessage(3);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
