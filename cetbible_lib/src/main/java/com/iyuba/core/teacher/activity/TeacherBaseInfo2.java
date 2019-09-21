package com.iyuba.core.teacher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.iyuba.base.BaseActivity;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.QuestionManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.teacher.protocol.UpdateClassRequest;
import com.iyuba.core.teacher.protocol.UpdateClassResponse;
import com.iyuba.core.teacher.sqlite.mode.Teacher;
import com.iyuba.core.thread.UploadFile;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class TeacherBaseInfo2 extends BaseActivity {
    public String size;
    Teacher teacher = new Teacher();
    Spinner tcity, endegree, jpdegree;
    CheckBox c1, c2, c3, c4, c5, c6, c7, c8;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14;
    private TextView quesCancel, quesComplete, next, pre;
    private String tempFilePath = Environment.getExternalStorageDirectory()
            + "/ques_temp.jpg";
    private CustomDialog cd;
    private TextView next2, category1, category2;
    private ArrayAdapter<String> adapter;

    private List<String> list;

    private String[] items = {"北京", "上海", "重庆", "西安", "南京", "武汉", "成都", "沈阳", "大连", "杭州", "宁波", "青岛", "济南", "厦门", "福州", "长沙", "哈尔滨", "长春", "大庆", "无锡", "苏州", "昆明", "合肥", "郑州", "佛山", "南昌", "贵阳", "南宁", "石家庄", "太原", "温州", "烟台", "珠海", "常州", "南通", "扬州", "徐州", "东莞", "威海", "淮安", "呼和浩特", "镇江", "潍坊", "中山", "临沂", "咸阳", "包头", "嘉兴", "惠州", "泉州", "秦皇岛", "洛阳", "海口", "兰州", "西宁", "银川", "乌鲁木齐", "齐齐哈尔", "鞍山", "昆山", "三亚", "廊坊", "芜湖", "抚顺", "德阳", "鄂尔多斯", "金华", "江阴", "营口", "唐山", "保定", "邢台", "桂林", "吉林", "九江", "锦州", "安庆", "邯郸", "赣州", "泰安", "柳州", "榆林", "新乡", "舟山", "慈溪", "南阳", "聊城", "东营", "淄博", "漳州", "沧州", "丹东", "宜兴", "绍兴", "湖州", "衡阳", "郴州", "泰州", "普宁", "义乌", "汕头", "揭阳", "宜昌", "大同", "湘潭", "盐城", "马鞍山", "介休", "襄樊", "长治", "日照", "常熟", "肇庆", "滨州", "台州", "株洲", "绵阳", "双流", "平顶山", "龙岩", "晋江", "连云港", "张家港", "岳阳", "济宁", "江门", "运城"};
    private ArrayAdapter<String> adapter2;
    private List<String> list2;
    private String[] items2 = {"英语四级", "英语六级", "英语专业四级", "英语专业八级", "非英语", "其他"};
    private ArrayAdapter<String> adapter3;
    private List<String> list3;
    private String[] items3 = {"日语N1", "日语N2", "日语N3", "非日语", "其他"};
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

                case 6:
                    CustomToast.showToast(mContext, "请选择问题类型");
                    break;

                case 10:
                    CustomToast.showToast(mContext, size);
                    break;
            }
        }
    };

    public void intiSpiner(Spinner spinner) {

        list = new ArrayList<String>();

        for (int i = 0; i < items.length; i++)

        {

            list.add(items[i]);

        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }

    public void intiSpiner2(Spinner spinner) {

        list2 = new ArrayList<String>();

        for (int i = 0; i < items2.length; i++)

        {

            list2.add(items2[i]);

        }

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter2);

    }

    public void intiSpiner3(Spinner spinner) {

        list3 = new ArrayList<String>();

        for (int i = 0; i < items3.length; i++)

        {

            list3.add(items3[i]);

        }

        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list3);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter3);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_teacherbaseinfo2);
        teacher = QuestionManager.getInstance().mTeacher;
        initWidget();
    }

    public void initData() {

    }

    public void initWidget() {

        tcity = findViewById(R.id.tcity);
        endegree = findViewById(R.id.endegree);
        jpdegree = findViewById(R.id.jpdegree);
        intiSpiner(tcity);
        intiSpiner2(endegree);
        intiSpiner3(jpdegree);


        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        cb7 = findViewById(R.id.cb7);
        cb8 = findViewById(R.id.cb8);
        cb9 = findViewById(R.id.cb9);
        cb10 = findViewById(R.id.cb10);
        cb11 = findViewById(R.id.cb11);
        cb12 = findViewById(R.id.cb12);
        cb13 = findViewById(R.id.cb13);
        cb14 = findViewById(R.id.cb14);

        OnCheckedChangeListener onc2 = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                String c2String = "";
                int num = 0;
                if (cb1.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb1.getText().toString().trim();
                }
                if (cb2.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb2.getText().toString().trim();
                }
                if (cb3.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb3.getText().toString().trim();
                }
                if (cb4.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb4.getText().toString().trim();
                }
                if (cb5.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb5.getText().toString().trim();
                }
                if (cb6.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb6.getText().toString().trim();
                }
                if (cb7.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb7.getText().toString().trim();
                }
                if (cb8.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb8.getText().toString().trim();
                }
                if (cb9.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb9.getText().toString().trim();
                }
                if (cb10.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb10.getText().toString().trim();
                }
                if (cb11.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb11.getText().toString().trim();
                }
                if (cb12.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb12.getText().toString().trim();
                }
                if (cb13.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb13.getText().toString().trim();
                }
                if (cb14.isChecked()) {
                    num++;
                    c2String = c2String + "," + cb14.getText().toString().trim();
                }

                if (num > 3) {
                    arg0.setChecked(false);
                    c2String = c2String.replace("," + arg0.getText(), "");
                    CustomToast.showToast(mContext, "最多只能选3项!");
                }
                if (!c2String.equals("")) c2String = c2String.substring(1, c2String.length());
                category2.setText(c2String);

            }
        };

        cb1.setOnCheckedChangeListener(onc2);
        cb2.setOnCheckedChangeListener(onc2);
        cb3.setOnCheckedChangeListener(onc2);
        cb4.setOnCheckedChangeListener(onc2);
        cb5.setOnCheckedChangeListener(onc2);
        cb6.setOnCheckedChangeListener(onc2);
        cb7.setOnCheckedChangeListener(onc2);
        cb8.setOnCheckedChangeListener(onc2);
        cb9.setOnCheckedChangeListener(onc2);
        cb10.setOnCheckedChangeListener(onc2);
        cb11.setOnCheckedChangeListener(onc2);
        cb12.setOnCheckedChangeListener(onc2);
        cb13.setOnCheckedChangeListener(onc2);
        cb14.setOnCheckedChangeListener(onc2);


        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);
        OnCheckedChangeListener onc = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                String c1String = "";
                int num = 0;
                if (c1.isChecked()) {
                    num++;
                    c1String = c1String + "," + c1.getText().toString().trim();
                }
                if (c2.isChecked()) {
                    num++;
                    c1String = c1String + "," + c2.getText().toString().trim();
                }
                if (c3.isChecked()) {
                    num++;
                    c1String = c1String + "," + c3.getText().toString().trim();
                }
                if (c4.isChecked()) {
                    num++;
                    c1String = c1String + "," + c4.getText().toString().trim();
                }
                if (c5.isChecked()) {
                    num++;
                    c1String = c1String + "," + c5.getText().toString().trim();
                }
                if (c6.isChecked()) {
                    num++;
                    c1String = c1String + "," + c6.getText().toString().trim();
                }
                if (c7.isChecked()) {
                    num++;
                    c1String = c1String + "," + c7.getText().toString().trim();
                }
                if (c8.isChecked()) {
                    num++;
                    c1String = c1String + "," + c8.getText().toString().trim();
                }
                if (num > 3) {
                    arg0.setChecked(false);
                    c1String = c1String.replace("," + arg0.getText(), "");
                    CustomToast.showToast(mContext, "最多只能选3项!");
                }
                if (!c1String.equals("")) c1String = c1String.substring(1, c1String.length());
                category1.setText(c1String);

            }
        };
        c1.setOnCheckedChangeListener(onc);
        c2.setOnCheckedChangeListener(onc);
        c3.setOnCheckedChangeListener(onc);
        c4.setOnCheckedChangeListener(onc);
        c5.setOnCheckedChangeListener(onc);
        c6.setOnCheckedChangeListener(onc);
        c7.setOnCheckedChangeListener(onc);
        c8.setOnCheckedChangeListener(onc);

        quesCancel = findViewById(R.id.tbutton_back2);
        next2 = findViewById(R.id.next2);
        category1 = findViewById(R.id.category1);
        category2 = findViewById(R.id.category2);
        next2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //获取课程类别信息
                teacher.uid = AccountManager.Instace(mContext).getId();
                teacher.username = AccountManager.Instace(mContext).getUserName();
                teacher.tcity = tcity.getSelectedItem().toString();

                teacher.endegree = endegree.getSelectedItem().toString();

                teacher.jpdegree = jpdegree.getSelectedItem().toString();

                teacher.category1 = category1.getText().toString().trim();

                teacher.category2 = category2.getText().toString().trim();

                UpdateClass();
                Intent intent = new Intent(mContext, TeacherBaseInfo3.class);
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

    public void UpdateClass() {

        ExeProtocol.exe(new UpdateClassRequest(teacher), new ProtocolResponse() {

            @Override
            public void finish(BaseHttpResponse bhr) {
                UpdateClassResponse tr = (UpdateClassResponse) bhr;
                if (tr.result.equals("1")) handler.sendEmptyMessage(11);
            }

            @Override
            public void error() {

            }
        });
    }

    class UploadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                String uri = "http://www.iyuba.cn/question/askQuestion.jsp?"
                        + "&format=json" + "&uid=" + AccountManager.Instace(mContext).getId() + "&username="
                        + AccountManager.Instace(mContext).getUserName() + "&question=" + 1 + "&category1="
                        + 3;

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
