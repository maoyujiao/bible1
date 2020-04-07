package com.iyuba.core.discover.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.sqlite.mode.me.Emotion;
import com.iyuba.core.teacher.protocol.RequestPublishMood;
import com.iyuba.core.teacher.protocol.ResponsePublishMood;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.util.SaveImage;
import com.iyuba.core.util.SelectPicUtils;
import com.iyuba.core.util.TakePictureUtil;
import com.iyuba.core.widget.dialog.CustomToast;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 关于界面
 *
 * @author lmy
 */

public class PublishMood extends BasisActivity implements OnClickListener {

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public String size;
    String success;
    String failure;
    boolean doing = false;
    private Context mContext;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CustomToast.showToast(mContext, "写点什么在发表吧！");
                    break;
                case 1:
                    CustomToast.showToast(mContext, "发表成功");
                    finish();
                    break;
                case 2:
                    CustomToast.showToast(mContext, "网络原因发表失败，请稍后再试！");
                    break;
                case 3:
                    CustomToast.showToast(mContext, "请打开网络！");
                    break;

                case 6:
                    CustomToast.showToast(mContext, "正在发表....");
                    break;
            }
        }
    };
    private TextView button_back, publish_m;
    private RelativeLayout rlShow;
    private GridView emotion_GridView;
    private EditText content;
    private ImageView iface, ivAddPic;
    private int[] imageIds = new int[30];
    private String actionUrl = "http://api.iyuba.com.cn/v2/avatar/photo?uid=";
    private String newName;
    private Boolean isChangePor = false;
    private boolean hasDiscPic = false;
    private File file;
    private Uri contentUri;
    private Uri finalUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_fricircle_mood);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        initView();

//		String action = getIntent().getStringExtra("action");
//		if (action.equals("2"))
//			showListDia();

    }

    public void initView() {
        button_back = findViewById(R.id.button_back2);
        button_back.setOnClickListener(this);
        publish_m = findViewById(R.id.publish_m);
        publish_m.setOnClickListener(this);
        rlShow = findViewById(R.id.rl_show);
        emotion_GridView = rlShow.findViewById(R.id.grid_emotion);
        content = findViewById(R.id.edit_mood);
        iface = findViewById(R.id.iface);
        iface.setOnClickListener(this);
        ivAddPic = findViewById(R.id.iv_add_picture);
        ivAddPic.setOnClickListener(this);
    }

    public void showListDia() {

        final String[] mList = {"相机拍摄", "手机相册"};
        AlertDialog.Builder listDia = new AlertDialog.Builder(PublishMood.this);
        listDia.setTitle("选择图片");
        listDia.setItems(mList, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

				/* 下标是从0开始的 */
                showClickMessage(which);
            }

        });
        listDia.create().show();

    }

    private void showClickMessage(int which) {

        Intent intent;
        switch (which) {
            case 0:
                file = TakePictureUtil.getPhotoFile(mContext) ;
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME,file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            file);
                }

                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                break;
            case 1:
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME , file);
                } else {
                    contentUri = Uri.fromFile(file);
                }
                finalUri = SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT,file );
                break;

            case PHOTO_REQUEST_GALLERY:
                String path = SelectPicUtils.getPath(this, data.getData());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME , new File(path));
                } else {
                    contentUri = Uri.fromFile(new File(path));
                }
                finalUri = SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT,new File(path));
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    hasDiscPic = true;
                    isChangePor = true;
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(finalUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ivAddPic.setImageBitmap(bitmap);
        SaveImage.saveImage(Environment.getExternalStorageDirectory() + "/iyuba/mood.jpg", bitmap);
    }

    // 上传头像、文件到服务器上
    private void uploadFile() {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url;
            if (content.getText().toString() == null
                    || content.getText().toString().equals("")) {
                url = new URL(actionUrl
                        + AccountManager.Instace(mContext).userId);
            } else {
                url = new URL(actionUrl
                        + AccountManager.Instace(mContext).userId
                        + "&iyu_describe="
                        + URLEncoder.encode(URLEncoder.encode(content.getText()
                        .toString(), "UTF-8"), "UTF-8"));
                Log.e("userId", actionUrl
                        + AccountManager.Instace(mContext).userId
                        + "&iyu_describe=" + content.getText().toString());
            }
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设定传送的method=POST */
            con.setRequestMethod("POST");
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            // con.setRequestProperty("iyu_describe",
            // URLEncoder.encode(mood_content.getText().toString(),"utf-8"));
			/* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            String describe = "iyu_describe="
                    + URLEncoder.encode(content.getText().toString(), "utf-8");
            Log.e("describe", "----" + describe);
            // ds.writeBytes(describe);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);
			/* 取得文件的FileInputStream */

//			Bitmap bmp = BitmapFactory.decodeFile(uploadFile);
            Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/iyuba/mood.jpg");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0
            Log.e("iyuba", stream.toByteArray().length / 1024
                    + "stream------------------------");
//			String temp2 = Environment.getExternalStorageDirectory()
//					+ "/temp2.jpg";
            String temp2 = Environment.getExternalStorageDirectory()+"/iyuba/mood.jpg";
            FileOutputStream os = new FileOutputStream(temp2);
            os.write(stream.toByteArray());
            os.close();
            FileInputStream fStream = new FileInputStream(temp2);
			/* 设定每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* 从文件读取数据到缓冲区 */
            while ((length = fStream.read(buffer)) != -1) { /* 将数据写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(describe);
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
            fStream.close();
            ds.flush();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
			/* 将Response显示于Dialog */
            success = b.toString().trim();
            JSONObject jsonObject = new JSONObject(success.substring(
                    success.indexOf("{"), success.lastIndexOf("}") + 1));
            System.out.println("cc=====" + jsonObject.getString("status"));
            if (jsonObject.getString("status").equals("0")) {// status 为0则修改成功
                handler.sendEmptyMessage(1);
                finish();
            } else {
                handler.sendEmptyMessage(2);
            }
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
            failure = e.getMessage();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.publish_m) {
            if (!NetWorkState.isConnectingToInternet()) {// 开始刷新
                handler.sendEmptyMessage(3);
            } else {
                if (doing) {
                    handler.sendEmptyMessage(6);
                    return;
                }
                if (isChangePor) {
                    doing = true;
                    handler.sendEmptyMessage(6);
                    Thread upload = new Thread() {

                        @Override
                        public void run() {
                            super.run();
                            uploadFile();
                        }
                    };
                    upload.start();
                } else {
                    if (content.getText().toString().trim().equals("")) {
                        handler.sendEmptyMessage(0);
                        return;
                    }
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(PublishMood.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    ExeProtocol.exe(
                            new RequestPublishMood(AccountManager
                                    .Instace(mContext).getId(), AccountManager
                                    .Instace(mContext).getUserName(), content
                                    .getText().toString().trim()),
                            new ProtocolResponse() {
                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponsePublishMood responseUpdateState = (ResponsePublishMood) bhr;
                                    int code = Integer
                                            .parseInt(responseUpdateState.result);
                                    if (code == 351) {
                                        handler.sendEmptyMessage(1);

                                    } else {
                                        handler.sendEmptyMessage(2);
                                    }
                                    doing = false;
                                }

                                @Override
                                public void error() {

                                    // handler.sendEmptyMessage(3);
                                }
                            });
                }
            }
        }
        if (arg0.getId() == R.id.button_back2) {
            finish();
        }

        if (arg0.getId() == R.id.iv_add_picture) {
            showListDia();
        }

        if (arg0.getId() == R.id.iface) {
            if (rlShow.getVisibility() == View.GONE) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(PublishMood.this
                                        .getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                rlShow.setVisibility(View.VISIBLE);
                initEmotion();
                emotion_GridView.setVisibility(View.VISIBLE);
                emotion_GridView
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                Bitmap bitmap = null;
                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), imageIds[arg2
                                                % imageIds.length]);
                                ImageSpan imageSpan = new ImageSpan(mContext,
                                        bitmap);
                                String str = "image" + arg2;
                                SpannableString spannableString = new SpannableString(
                                        str);
                                String str1 = Emotion.express[arg2];
                                SpannableString spannableString1 = new SpannableString(
                                        str1);
                                if (str.length() == 6) {
                                    spannableString.setSpan(imageSpan, 0, 6,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else if (str.length() == 7) {
                                    spannableString.setSpan(imageSpan, 0, 7,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else {
                                    spannableString.setSpan(imageSpan, 0, 5,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                content.append(spannableString1);
                            }
                        });
            } else {
                rlShow.setVisibility(View.GONE);
            }
        }

    }

    private void initEmotion() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                Emotion.initEmotion(),
                R.layout.team_layout_single_expression_cell,
                new String[]{"image"}, new int[]{R.id.image});
        emotion_GridView.setAdapter(simpleAdapter);
        emotion_GridView.setNumColumns(7);
    }
}
