package com.iyuba.core.teacher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.base.BaseActivity;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.protocol.SubmitRequest;
import com.iyuba.core.teacher.protocol.SubmitResponse;
import com.iyuba.core.thread.UploadFile;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.SaveImage;
import com.iyuba.core.util.SelectPicUtils;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.ContextMenu;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TeacherBaseInfo3 extends BaseActivity {
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public String size;
    ImageView fj1, fj2, fj3;
    Button button1;
    Uri contentUri;
    int tu = 1;
    private TextView quesCancel;
    private ContextMenu contextMenu;
    private String tempFilePath = Environment.getExternalStorageDirectory()
            + "/fj.jpg";
    private File tempFile = new File(tempFilePath);
    private boolean hasPic = false;
    private CustomDialog cd;
    private TextView next2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    cd.dismiss();
                    break;
                case 1:
                    cd.show();
                    break;
                case 6:
                    CustomToast.showToast(mContext, "请选择问题类型");
                    break;

                case 13:
                    CustomToast.showToast(mContext, "请添加图片!");
                    break;
                case 11:
                    submit();
                    break;
                case 12:
                    CustomToast.showToast(mContext, "图片上传成功，点击完成，结束认证!");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_teacherbaseinfo3);
        cd = WaittingDialog.showDialog(mContext);
        initWidget();
    }

    public void initWidget() {

        next2 = findViewById(R.id.next2);
        next2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();


            }
        });


        button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!hasPic) {
                    handler.sendEmptyMessage(13);
                    return;
                }
                button1.setClickable(false);
                handler.sendEmptyMessage(1);
                new UploadThread().start();
                next2.setVisibility(View.VISIBLE);

            }
        });
        contextMenu = findViewById(R.id.context_menu3);
        quesCancel = findViewById(R.id.tbutton_back3);
        fj1 = findViewById(R.id.fj1);


        fj1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setContextMenu();
            }
        });

        quesCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME, new File(tempFilePath));
                } else {
                    contentUri = Uri.fromFile(new File(tempFilePath));
                }
                SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT, "fj.jpg");
                break;

            case PHOTO_REQUEST_GALLERY:
                String path = SelectPicUtils.getPath(this, data.getData());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME, new File(path));
                } else {
                    contentUri = Uri.fromFile(new File(path));
                }
                SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT, "fj.jpg");
                break;

            case PHOTO_REQUEST_CUT:
                setPicToView();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView() {
        hasPic = true;
        Bitmap bitmap = BitmapFactory.decodeFile(tempFilePath);
        SaveImage.saveImage(tempFilePath, bitmap);
        fj1.setImageBitmap(bitmap);
    }

    public void submit() {
        ExeProtocol.exe(
                new SubmitRequest(AccountManager.Instace(mContext).userId + ""),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        SubmitResponse tr = (SubmitResponse) bhr;
                        handler.sendEmptyMessage(0);
                        if ("1".equals(tr.result)) {
                            handler.sendEmptyMessage(12);
                        }
                    }

                    @Override
                    public void error() {
                    }
                });

    }

    public void setContextMenu() {
        contextMenu.setText(mContext.getResources().getStringArray(
                R.array.choose_pic));
        contextMenu.setCallback(new ResultIntCallBack() {

            @Override
            public void setResult(int result) {
                Intent intent;
                switch (result) {
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME ,
                                    tempFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(tempFile));
                        }
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                        } else {
                            intent.setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                        }
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                        break;
                    default:
                        break;
                }
                contextMenu.dismiss();
            }
        });
        contextMenu.show();
    }

    class UploadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                String uri = "http://www.iyuba.cn/question/teacher/api/upLoad.jsp?from=attachment&uid="
                        + AccountManager.Instace(mContext).userInfo.uid + "&username=" +
                        TextAttr.encode(TextAttr.encode(TextAttr.encode(AccountManager.Instace(mContext).getUserName())));
                Log.e("iyuba", uri);
                Log.e("iyuba", "------图片");
                Bitmap bt = BitmapFactory.decodeFile(tempFilePath);
                File f = new File(tempFilePath);
                FileInputStream fis = new FileInputStream(f);
                FileChannel fc = fis.getChannel();

                Log.e("iyuba", fc.size() / 1024 + "------------" + 100);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int percent = 20;
                bt.compress(Bitmap.CompressFormat.JPEG, percent, stream);
                size = stream.size() / 1024 + "+++1++" + fc.size() / 1024;
                Log.e("iyuba", stream.size() / 1024 + "------------" + percent);


                //讲压缩后的文件保存在temp2下
                String temp2 = Constant.envir + "/temp2.jpg";
                FileOutputStream os = new FileOutputStream(temp2);
                os.write(stream.toByteArray());
                os.close();

                UploadFile.post(temp2, uri, new OperateCallBack() {

                    @Override
                    public void success(String message) {
                        handler.sendEmptyMessage(0);
                        handler.sendEmptyMessage(3);
                        handler.sendEmptyMessage(11);
                    }

                    @Override
                    public void fail(String message) {
                        handler.sendEmptyMessage(0);
                        handler.sendEmptyMessage(2);
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
