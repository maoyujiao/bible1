package com.iyuba.core.teacher.activity;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.iyuba.base.BaseActivity;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.protocol.GetTeacherInfoRequest;
import com.iyuba.core.teacher.protocol.GetTeacherInfoResponse;
import com.iyuba.core.teacher.protocol.UpdateBasicRequest;
import com.iyuba.core.teacher.protocol.UpdateBasicResponse;
import com.iyuba.core.teacher.sqlite.mode.Teacher;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.thread.UploadFile;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.SaveImage;
import com.iyuba.core.util.SelectPicUtils;
import com.iyuba.core.widget.ContextMenu;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeacherBaseInfo extends BaseActivity {
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public String size;
    RadioButton RadioButton1, RadioButton2;
    Spinner spteachercert;
    private TextView quesCancel;
    private ContextMenu contextMenu;
    private String tempFilePath = Environment.getExternalStorageDirectory()
            + "/teacher.jpg";
    private File tempFile = new File(tempFilePath);
    private CustomDialog cd;
    private ImageView teaacherhead;
    private String attachment = "";
    private TextView next2, editteachername, editteachermobile, editteacheremail, editteacherweixin, editteacherdesc;
    private Teacher teacher = new Teacher();
    private ArrayAdapter<String> adapter;

    private List<String> list;

    private String[] items = {"专科", "本科", "硕士", "博士", "其他"};

    @SuppressLint("HandlerLeak")
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
                    getTeacherData();
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.ask_question_fail);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.ask_question_success);
                    finish();
                    break;
                case 4:
                    CustomToast.showToast(mContext, R.string.question_tip);
                    break;

                case 6:
                    CustomToast.showToast(mContext, "请选择问题类型");
                    break;

                case 10:
                    CustomToast.showToast(mContext, size);
                    break;
                case 12:
                    GitHubImageLoader.Instace(mContext).setPic("http://www.iyuba.cn/question/" + teacher.timg,
                            teaacherhead, R.drawable.photo_pic, 0);
                    break;
                case 13:
                    CustomToast.showToast(mContext, attachment);
                    break;
                case 16:
                    CustomToast.showToast(mContext, "请上传您的头像!");
                    break;
                case 17:
                    CustomToast.showToast(mContext, "请填写必填信息！");
                    break;
                case 18:
                    initData();
                    break;
            }

        }
    };
    private Uri contentUri;

    public void intiSpiner(Spinner spinner) {

        list = new ArrayList<String>();

        for (int i = 0; i < items.length; i++) {

            list.add(items[i]);

        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_teacherbaseinfo);
        cd = WaittingDialog.showDialog(mContext);

        initWidget();
        handler.sendEmptyMessage(1);
    }

    public void initData() {
        cd.dismiss();
        if (!teacher.timg.equals("")) handler.sendEmptyMessage(12);
        editteachername.setText(teacher.tname);
        editteachermobile.setText(teacher.tphone);
        editteacherdesc.setText(teacher.tonedesc);
        editteacherweixin.setText(teacher.tweixin);
        editteacheremail.setText(teacher.temail);
        if (teacher.tsex.trim().equals("女")) RadioButton2.setChecked(true);

        if (!teacher.topedu.equals("")) {
            int theNum = 0;
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(teacher.topedu)) {
                    theNum = i;
                    break;
                }

            }


            spteachercert.setSelection(theNum, true);


        }


    }

    public void initWidget() {


        spteachercert = findViewById(R.id.spteachercert);
        intiSpiner(spteachercert);


        contextMenu = findViewById(R.id.context_menu1);
        teaacherhead = findViewById(R.id.teaacherhead);
        teaacherhead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                setContextMenu();
            }
        });


        editteacheremail = findViewById(R.id.editteacheremail);
        editteacherweixin = findViewById(R.id.editteacherweixin);
        editteacherdesc = findViewById(R.id.editteacherdesc);

        editteachermobile = findViewById(R.id.editteachermobile);
        RadioButton1 = findViewById(R.id.RadioButton1);
        RadioButton2 = findViewById(R.id.RadioButton2);


        editteachername = findViewById(R.id.editteachername);
        quesCancel = findViewById(R.id.tbutton_back);

        next2 = findViewById(R.id.next2);

        next2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //获取老师的基本信息
                teacher.uid = AccountManager.Instace(mContext).getId();
                teacher.username = AccountManager.Instace(mContext).getUserName();
                teacher.tname = editteachername.getText().toString().trim();
                teacher.tphone = editteachermobile.getText().toString().trim();
                teacher.temail = editteacheremail.getText().toString().trim();
                teacher.tweixin = editteacherweixin.getText().toString().trim();
                teacher.tonedesc = editteacherdesc.getText().toString().trim();
                if (!attachment.equals("")) teacher.timg = attachment;
                if (RadioButton2.isChecked()) {
                    teacher.tsex = "女";
                } else {
                    teacher.tsex = "男";
                }
                teacher.topedu = spteachercert.getSelectedItem().toString();
                //判断是否上传老师头像
                if (teacher.timg.equals("")) {
                    handler.sendEmptyMessage(16);
                    return;
                }
                //判断是否有必填项没有填
                if (editteachername.getText().toString().trim().equals("") || editteachermobile.getText().toString().trim().equals("")
                        || editteacheremail.getText().toString().trim().equals("") || editteacherweixin.getText().toString().trim().equals("")
                        || editteacherdesc.getText().toString().trim().equals("")
                ) {
                    handler.sendEmptyMessage(17);
                    return;
                }


                updateBasic();
                Intent intent = new Intent(mContext, TeacherBaseInfo2.class);
                startActivity(intent);
                finish();
            }
        });

        quesCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });


    }

    //设置菜单
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
                        }
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME, new File(tempFilePath));
                } else {
                    contentUri = Uri.fromFile(new File(tempFilePath));
                }
                SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT, "teacher.jpg");

                break;

            case PHOTO_REQUEST_GALLERY:
                String path = SelectPicUtils.getPath(this, data.getData());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME , new File(path));
                } else {
                    contentUri = Uri.fromFile(new File(path));
                }
                SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT, "teacher.jpg");
                break;

            case PHOTO_REQUEST_CUT:
                setPicToView();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView() {
        Bitmap bitmap = BitmapFactory.decodeFile(tempFilePath);
        teaacherhead.setImageBitmap(bitmap);
        SaveImage.saveImage(tempFilePath, bitmap);
        new UploadThread().start();
    }

    //获取老师的基本信息
    public void getTeacherData() {

        ExeProtocol.exe(new GetTeacherInfoRequest(AccountManager.Instace(mContext).userInfo.uid), new ProtocolResponse() {

            @Override
            public void finish(BaseHttpResponse bhr) {
                GetTeacherInfoResponse tr = (GetTeacherInfoResponse) bhr;
                teacher = tr.item;
                handler.sendEmptyMessage(18);
                //initData();
            }

            @Override
            public void error() {

            }
        });
    }

    //提交或更新老师基本信息
    public void updateBasic() {
        ExeProtocol.exe(new UpdateBasicRequest(teacher), new ProtocolResponse() {

            @Override
            public void finish(BaseHttpResponse bhr) {
                UpdateBasicResponse tr = (UpdateBasicResponse) bhr;
                if (tr.result.equals("1")) ;
            }

            @Override
            public void error() {

            }
        });
    }

    //上传老师的头像
    class UploadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                String uri = "http://www.iyuba.cn/question/teacher/api/upLoad.jsp?format=json";
                Log.e("iyuba", uri);
                UploadFile.postHead(tempFilePath, uri, new OperateCallBack() {

                    @Override
                    public void success(String message) {
                        attachment = message;
                    }

                    @Override
                    public void fail(String message) {
                        attachment = message;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
