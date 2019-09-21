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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.base.BaseActivity;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.Login;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.adapter.QuestionAppTypeListAdapter;
import com.iyuba.core.teacher.adapter.QuestionTypeListAdapter;
import com.iyuba.core.teacher.protocol.AskQuesRequest;
import com.iyuba.core.teacher.protocol.AskQuesResponse;
import com.iyuba.core.teacher.sqlite.mode.QuestionAppType;
import com.iyuba.core.teacher.sqlite.mode.QuestionType;
import com.iyuba.core.thread.UploadFile;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.NetWorkState;
import com.iyuba.core.util.SaveImage;
import com.iyuba.core.util.SelectPicUtils;
import com.iyuba.core.util.TakePictureUtil;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.ContextMenu;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class QuezActivity extends BaseActivity {
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public String size;
    View edit, sub, sub2;
    int qtype = 0;//问题的类型
    int qAppType = 0;
    List<QuestionType> qtList;
    List<QuestionAppType> qatList;
    String askuid = "";
    private TextView quesDescCancel, quesComplete,
            nextToAppType, preToAppType,
            nextToAbilityType, preToQuesDesc;
    private EditText quesDescText;
    private ImageView photoPic;

    private TextView quesDescWordsNumTip;
    private View centerPart;
    private ContextMenu contextMenu;
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = quesDescText.getSelectionStart();
            editEnd = quesDescText.getSelectionEnd();
            if (temp.length() > 140) {
                Toast.makeText(mContext, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                quesDescText.setText(s);
                quesDescText.setSelection(tempSelection);
            } else {
                int remainLen = 140 - temp.length();
                quesDescWordsNumTip.setText("剩余输入字数" + remainLen);
            }
        }
    };
    private QuestionTypeListAdapter qtAdapter;
    private QuestionAppTypeListAdapter qatAdapter;
    private boolean hasDiscPic = false;
    private CustomDialog cd;
    private String question;
    private ListView listview;
    private ListView listview2;
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

                case 5:
                    //设置问题类型的数据源
                    setAbilityTypeData();//初始化问题类型

                    break;
                case 6:
                    CustomToast.showToast(mContext, "请选择问题来源");
                    break;
                case 7:
                    //设置问题类型的数据源
                    setAppTypeData();//初始化问题类型
                    break;
                case 8:
                    CustomToast.showToast(mContext, "请选择问题类型");
                    break;
                case 10:
                    CustomToast.showToast(mContext, size);
                    break;
            }
        }
    };
    private Uri contentUri;
    private Uri finalUri;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_questioning);
        cd = WaittingDialog.showDialog(mContext);
        Intent intent = getIntent();
        askuid = intent.getStringExtra("askuid");
        if (askuid == null) askuid = "";
        initWidget();
        handler.sendEmptyMessage(5);
    }

    public void initWidget() {

        quesDescCancel = findViewById(R.id.btn_back);
        nextToAppType = findViewById(R.id.next_to_app_type);

        preToQuesDesc = findViewById(R.id.part2btn_back);
        nextToAbilityType = findViewById(R.id.part2btn_next);

        preToAppType = findViewById(R.id.part3btn_back);
        quesComplete = findViewById(R.id.part3btn_complete);

        quesDescText = findViewById(R.id.ques_text);
        centerPart = findViewById(R.id.center_part);
        quesDescWordsNumTip = findViewById(R.id.ques_words_tip);
        photoPic = findViewById(R.id.photo_pic);
        contextMenu = findViewById(R.id.context_menu);

        listview = findViewById(R.id.part2_list);
        listview2 = findViewById(R.id.part3_list);

        edit = findViewById(R.id.part1);
        sub = findViewById(R.id.part2);
        sub2 = findViewById(R.id.part3);

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                QuestionAppType qat = qatList.get(arg2);
                qAppType = qat.id;
                handler.sendEmptyMessage(7);
            }
        });

        listview2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                QuestionType qt = qtList.get(arg2);
                qtype = qt.id;
                handler.sendEmptyMessage(5);
            }
        });

        quesDescCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        //问题提交
        quesComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!NetWorkState.isConnectingToInternet()) {
                    handler.sendEmptyMessage(2);
                } else {

                    //如果没登录则跳转登录
                    if (!AccountManager.Instace(mContext).checkUserLogin()) {

                        Intent intent = new Intent();
                        intent.setClass(mContext, Login.class);
                        startActivity(intent);
                        return;
                    }
                    //如果选择了类型则可以提交，没有则提示没有选择类型
                    if (qtype != 0) {
                        askQuestion();
                    } else {
                        handler.sendEmptyMessage(8);
                    }
                }
            }
        });

        //设置下一步按钮事件，选择问题来自的应用
        nextToAppType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                question = quesDescText.getText().toString();
                //提示字符串不为空
                if (question.trim().equals("")) {
                    handler.sendEmptyMessage(4);
                } else {
                    handler.sendEmptyMessage(7);
                    qAppType = 0;//问题提类型设置为0
                    //关闭软键盘
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(quesDescText.getWindowToken(), 0);
                    edit.setVisibility(View.GONE);
                    sub.setVisibility(View.VISIBLE);
                    sub2.setVisibility(View.GONE);

                }
            }
        });

        //设置下一步按钮事件，选择问题考察的能力
        nextToAbilityType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //如果选择了类型则可以提交，没有则提示没有选择类型
                if (qAppType != 0) {
                    handler.sendEmptyMessage(5);
                    qtype = 0;//问题提类型设置为0
                    //关闭软键盘
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(quesDescText.getWindowToken(), 0);
                    edit.setVisibility(View.GONE);
                    sub.setVisibility(View.GONE);
                    sub2.setVisibility(View.VISIBLE);
                } else {
                    handler.sendEmptyMessage(6);
                }
            }
        });


        //设置上一步按钮事件,回到选择问题来源界面
        preToAppType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sub.setVisibility(View.VISIBLE);
                sub2.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
        });

        //设置上一步按钮事件，回到问题描述界面
        preToQuesDesc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sub.setVisibility(View.GONE);
                sub2.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
            }
        });

        quesDescText.addTextChangedListener(mTextWatcher);

        centerPart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setContextMenu();
            }
        });

        photoPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
//				if (hasDiscPic) {
//					Intent intent = new Intent();
//					intent.setClass(mContext, ShowPicActivity.class);
//					startActivity(intent);
//				} else {
                setContextMenu();
//				}
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
                        photoFile =
                                TakePictureUtil.getPhotoFile(mContext) ;
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME + ".fileprovider",photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    photoFile);
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
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME + ".fileprovider", photoFile);
                } else {
                    contentUri = Uri.fromFile(photoFile);
                }
                finalUri = SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT,photoFile );
                break;
            case PHOTO_REQUEST_GALLERY:
                String path = SelectPicUtils.getPath(this, data.getData());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, Constant.PACKAGE_NAME + ".fileprovider", new File(path));
                } else {
                    contentUri = Uri.fromFile(new File(path));
                }
                finalUri = SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT,new File(path));
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    hasDiscPic = true;
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
        photoPic.setImageBitmap(bitmap);
        SaveImage.saveImage(Environment.getExternalStorageDirectory()+"/iyuba/ques.jpg", bitmap);
    }

    public void askQuestion() {
        question = quesDescText.getText().toString();
        if (question.trim().equals("")) {
            handler.sendEmptyMessage(4);
        } else {
            question = question.replaceAll("\'", "’");
            question = TextAttr.encode(TextAttr.encode(TextAttr.encode(question)));
            if (!hasDiscPic) {
                askQuestionWithoutFile();
            } else {
                askQuestionWithFile();
            }
        }
    }

    public void askQuestionWithoutFile() {
        new AskQuestionTask().execute();
    }

    public void askQuestionWithFile() {
        handler.sendEmptyMessage(1);
        new UploadThread().start();
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 40, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 40;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private Bitmap getImageZoomed(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    //组装问题能力类型
    public void setAbilityTypeData() {
        qtList = new ArrayList<>();
        String[] types = {"口语", "听力", "阅读", "写作", "翻译", "单词", "语法", "其他"};
        for (int i = 1; i <= types.length; i++) {
            QuestionType q = new QuestionType();
            q.id = i;
            q.type = types[i - 1];
            if (qtype == q.id) {
                q.isSelect = true;
            }
            qtList.add(q);

        }
        qtAdapter = new QuestionTypeListAdapter(mContext, qtList);
        listview2.setAdapter(qtAdapter);
        qtAdapter.notifyDataSetChanged();
    }

    //组装问题应用类型
    public void setAppTypeData() {
        qatList = new ArrayList<>();
        String[] types = {"VOA", "BBC", "听歌", "CET4", "CET6", "托福", "N1", "N2",
                "N3", "微课", "雅思", "初中", "高中", "考研", "新概念", "走遍美国"};
        for (int i = 1; i <= types.length; i++) {
            QuestionAppType q = new QuestionAppType();
            q.id = i;
            q.type = types[i - 1];
            if (qAppType == q.id) {
                q.isSelect = true;
            }
            qatList.add(q);

        }
        qatAdapter = new QuestionAppTypeListAdapter(mContext, qatList);
        listview.setAdapter(qatAdapter);
        qatAdapter.notifyDataSetChanged();
    }

    class UploadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                if (qAppType != 0) {
                    qAppType += 100;
                }
                String id = AccountManager.Instace(mContext).getId();
                String name = AccountManager.Instace(mContext).getUserName();

                String uri = "http://www.iyuba.cn/question/askQuestion.jsp?"
                        + "&format=json" + "&uid=" + id +
                        "&username=" + (TouristUtil.isTourist() ? id : name) + "&question=" + question + "&category1="
                        + qtype + "&category2=" + qAppType;

                if (!askuid.equals("")) {
                    uri = uri + "&tuid=" + askuid;
                }
                Log.e("iyuba", uri);

                Bitmap bt = getImageZoomed(Environment.getExternalStorageDirectory()+"/iyuba/ques.jpg");

//				Bitmap bt=BitmapFactory.decodeFile(TakePictureUtil.photoPath);
                File f = new File(Environment.getExternalStorageDirectory()+"/iyuba/ques.jpg");
                FileInputStream fis = new FileInputStream(f);
                FileChannel fc = fis.getChannel();

//				Log.e("iyuba",fc.size()/1024+"------------"+100);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int percent = 100;

                bt.compress(Bitmap.CompressFormat.JPEG, percent, stream);

                size = stream.size() / 1024 + "+++1++" + fc.size() / 1024;
                Log.e("iyuba", stream.size() / 1024 + "------------" + percent);

                //讲压缩后的文件保存在temp2下
//				String temp2=Constant.envir+"/temp2.jpg";
                String temp2 = f.getAbsolutePath();
                FileOutputStream os = new FileOutputStream(temp2);
                os.write(stream.toByteArray());
                os.close();

                UploadFile.post(temp2, uri, new OperateCallBack() {

                    @Override
                    public void success(String message) {
                        handler.sendEmptyMessage(0);
                        handler.sendEmptyMessage(3);
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

    private class AskQuestionTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            handler.sendEmptyMessage(1);
            if (qAppType != 0) {
                qAppType += 100;
            }
            String id = AccountManager.Instace(mContext).getId();
            String userName;
            if (TouristUtil.isTourist()) {
                userName = id;
            } else {
                userName = AccountManager.Instace(mContext).getUserName();
            }
            ExeProtocol.exe(
                    new AskQuesRequest(id, userName, question, qtype, qAppType, askuid),
                    new ProtocolResponse() {

                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            AskQuesResponse tr = (AskQuesResponse) bhr;
                            handler.sendEmptyMessage(0);
                            if (!"0".equals(tr.result)) {
                                handler.sendEmptyMessage(3);
                            } else {
                                handler.sendEmptyMessage(2);
                            }
                        }

                        @Override
                        public void error() {
                            handler.sendEmptyMessage(0);
                            handler.sendEmptyMessage(2);
                        }
                    });
            return null;
        }
    }


}
