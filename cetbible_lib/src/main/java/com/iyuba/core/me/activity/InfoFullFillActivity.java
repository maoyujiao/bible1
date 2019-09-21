package com.iyuba.core.me.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.biblelib.R;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.adapter.SchoolListAdapter;
import com.iyuba.core.me.sqlite.mode.School;
import com.iyuba.core.me.sqlite.op.SchoolOp;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestEditUserInfo;
import com.iyuba.core.protocol.message.RequestUserDetailInfo;
import com.iyuba.core.protocol.message.ResponseEditUserInfo;
import com.iyuba.core.protocol.message.ResponseUserDetailInfo;
import com.iyuba.core.sqlite.mode.UserInfo;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class InfoFullFillActivity extends Activity {
    private final static int DATE_DIALOG = 0;
    private final static int SCHOOL_DIALOG = 3;// 学校选择
    int index = 0;
    Dialog dialog = null;
    private Context mContext = this;
    private View chsSex;
    private View chsBirth;
    private View chsProvince;
    private View chsIdentity;
    private View chsEducation;
    private View chsSchool;
    private View schoolDialog;
    private TextView chsnSex;
    private TextView chsnBirth;
    private TextView chsnProvince;
    private TextView chsnIdentity;
    private TextView chsnEducation;
    private TextView chsnSchool;
    private EditText ev;
    // private ImageView wc;
    // private ImageView lv;
    private ListView schoolList;
    private ArrayList<School> schools = new ArrayList<School>();
    private SchoolListAdapter schoolListAdapter;
    private StringBuffer tempSchool;
    private EditText searchText;
    private Button sure;
    private View backBtn;
    private View clear;
    private Button commit;
    private Calendar cld;
    private CustomDialog waitting;
    private String currentuid;
    private String yearn, monthn, dayn;
    private UserInfo userInfo;
    private ResponseUserDetailInfo userDetailInfo;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitting.show();
                    handler.sendEmptyMessage(1);
                    break;
                case 1:
                    ExeProtocol.exe(new RequestUserDetailInfo(currentuid),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseUserDetailInfo responseUserDetailInfo = (ResponseUserDetailInfo) bhr;
                                    if (responseUserDetailInfo.result.equals("211")) {
                                        userDetailInfo = responseUserDetailInfo;
                                    }
                                    handler.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {


                                }
                            });
                    break;
                case 2:
                    waitting.dismiss();
                    setText();
                    break;
                case 3:
                    Toast.makeText(mContext, "信息修改成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    schools = new SchoolOp(mContext).findDataByFuzzy(tempSchool
                            .toString());
                    schoolListAdapter = new SchoolListAdapter(mContext, schools);
                    schoolList.setAdapter(schoolListAdapter);
                    schoolListAdapter.notifyDataSetChanged();
                    break;
                case 9:
                    schools = new SchoolOp(mContext).findDataByFuzzy(tempSchool
                            .toString());
                    schoolListAdapter.setData(schools);
                    schoolListAdapter.notifyDataSetChanged();
                    break;

            }

        }

    };
    private Calendar c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_full_userinfo);
        mContext = this;

        backBtn = findViewById(R.id.button_back);
        chsSex = findViewById(R.id.choose_sex);
        chsBirth = findViewById(R.id.choose_birth);
        chsProvince = findViewById(R.id.choose_province);
        chsIdentity = findViewById(R.id.choose_identity);
        chsEducation = findViewById(R.id.choose_education);
        chsSchool = findViewById(R.id.choose_school);
        chsnSex = findViewById(R.id.chosen_sex);
        chsnBirth = findViewById(R.id.chosen_birth);
        chsnProvince = findViewById(R.id.chosen_province);
        chsnIdentity = findViewById(R.id.chosen_identity);
        chsnEducation = findViewById(R.id.chosen_education);
        chsnSchool = findViewById(R.id.chosen_school);
        commit = findViewById(R.id.userinfo_commit);
        ev = findViewById(R.id.editText_username);
        cld = Calendar.getInstance();
        currentuid = AccountManager.Instace(mContext).userId;
        userInfo = AccountManager.Instace(mContext).userInfo;
        waitting = WaittingDialog.showDialog(mContext);


        commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // String city = location.getText().toString();
                // city = city.trim();
                if (chsnBirth.getText() == "" || chsnEducation.getText() == ""
                        || chsnIdentity.getText() == "" || chsnProvince.getText() == ""
                        || chsnSchool.getText() == "" || chsnSex.getText() == "") {
                    Toast.makeText(mContext, "请完善信息！", Toast.LENGTH_SHORT).show();
                } else {
                    String value = "", key = "";
                    if (yearn != null) {
                        key = "gender,birthyear,birthmonth,birthday,resideprovince,occupation,education,graduateschool";
                        value = userDetailInfo.gender + "," + yearn + "," + monthn
                                + "," + dayn + "," + chsnProvince.getText() + ","
                                + chsnIdentity.getText() + ","
                                + chsnEducation.getText() + ","
                                + chsnSchool.getText();
                    } else {
                        key = "gender,resideprovince,occupation,education,graduateschool";
                        value = userDetailInfo.gender + ","
                                + chsnProvince.getText() + ","
                                + chsnIdentity.getText() + ","
                                + chsnEducation.getText() + ","
                                + chsnSchool.getText();
                    }

                    ExeProtocol.exe(
                            new RequestEditUserInfo(AccountManager
                                    .Instace(mContext).userId, key, value),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseEditUserInfo responseEditUserInfo = (ResponseEditUserInfo) bhr;
                                    if (responseEditUserInfo.result.equals("221")) {
                                        handler.sendEmptyMessage(3);
                                    } else {

                                    }
                                }

                                @Override
                                public void error() {


                                }
                            });
                }
            }
        });

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

        chsSex.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Builder builder = new Builder(mContext);
                // builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择性别");
                final String[] sex = {"男", "女"};
                // 设置一个单项选择下拉框
                /**
                 * 第一个参数指定我们要显示的一组下拉单选框的数据集合 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女'
                 * 会被勾选上 第三个参数给每一个单选项绑定一个监听器
                 */
                builder.setSingleChoiceItems(sex, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Toast.makeText(mContext, "性别为：" + sex[which],
                                // Toast.LENGTH_SHORT).show();
                                index = which;
                            }
                        });
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (userDetailInfo != null) {
                                    chsnSex.setText(sex[index]);
                                    userDetailInfo.gender = String
                                            .valueOf(index + 1);
                                    index = 0;
                                }

                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        });
                builder.show();

            }
        });

        chsBirth.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                showDialog(DATE_DIALOG);
            }
        });

        chsProvince.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Builder builder = new Builder(mContext);
                builder.setTitle("选择一个城市");
                // 指定下拉列表的显示数据
                final String[] cities = {"北京", "天津", "上海", "重庆", "河北", "河南",
                        "云南", "辽宁", "黑龙江", "湖南", "安徽", "山东", "新疆", "江苏", "浙江",
                        "江西", "湖北", "广西", "甘肃", "山西", "内蒙古", "陕西", "吉林", "福建",
                        "贵州", "广东", "青海", "西藏", "四川", "宁夏", "海南", "台湾", "香港",
                        "澳门"};
                // 设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(mContext, "选择的城市为：" + cities[which],
                        // Toast.LENGTH_SHORT).show();
                        chsnProvince.setText(cities[which]);
                        // userDetailInfo.resideLocation = cities[which];
                    }
                });
                builder.show();
            }
        });

        chsIdentity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Builder builder = new Builder(mContext);
                builder.setTitle("选择一个身份");
                // 指定下拉列表的显示数据
                final String[] identities = {"学生", "上班族"};
                // 设置一个下拉的列表选择项
                builder.setItems(identities,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Toast.makeText(mContext, "选择的城市为：" +
                                // cities[which], Toast.LENGTH_SHORT).show();
                                chsnIdentity.setText(identities[which]);
                                // userDetailInfo.occupation =
                                // identities[which];
                            }
                        });
                builder.show();
            }
        });

        chsEducation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Builder builder = new Builder(mContext);
                builder.setTitle("选择学历");
                // 指定下拉列表的显示数据
                final String[] educations = {"小学", "初中", "高中", "中等专业学校",
                        "大学专科", "本科", "硕士", "博士"};
                // 设置一个下拉的列表选择项
                builder.setItems(educations,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Toast.makeText(mContext, "选择的城市为：" +
                                // cities[which], Toast.LENGTH_SHORT).show();
                                chsnEducation.setText(educations[which]);
                                // userDetailInfo.education = educations[which];
                            }
                        });
                builder.show();
            }
        });

        chsSchool.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                createDialog(SCHOOL_DIALOG);
            }
        });

        handler.sendEmptyMessage(0);

    }

    /**
     * 创建日期及时间选择对话框
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        c = Calendar.getInstance();
        dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month,
                                          int dayOfMonth) {
                        Calendar cl = Calendar.getInstance();
                        cl.set(Calendar.YEAR, year);
                        cl.set(Calendar.MONTH, month);
                        cl.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (cl.after(cld)) {
                            Toast ta = Toast.makeText(mContext,
                                    "亲,您来自未来嘛,穿越过来哒o(≧v≦)o~~", Toast.LENGTH_SHORT);
                            ta.show();
                        } else {
                            int age = cld.get(Calendar.YEAR)
                                    - cl.get(Calendar.YEAR);
                            if (age > 80) {
                                Toast ta = Toast.makeText(mContext, "亲，你都"
                                        + String.valueOf(age)
                                        + "岁了，还学啥英语啊ヾ(≧▽≦*)o", Toast.LENGTH_SHORT);
                                ta.show();
                            } else
                                chsnBirth.setText(year + "-" + (month + 1)
                                        + "-" + dayOfMonth);
                            yearn = String.valueOf(year);
                            monthn = String.valueOf(month);
                            dayn = String.valueOf(dayOfMonth);
                        }
                    }
                }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );

        return dialog;
    }

    private void createDialog(int id) {
        Dialog dialog = null;
        Builder builder = new Builder(mContext);
        switch (id) {
            case SCHOOL_DIALOG:
                builder.setTitle(R.string.person_info_school);
                LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                schoolDialog = vi.inflate(R.layout.school_dialog, null);
                builder.setView(schoolDialog);
                builder.setNegativeButton(R.string.alert_btn_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                dialog = builder.create();
                dialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {

                        schoolListAdapter = null;
                    }
                });
                initSchoolDialog(dialog);
                break;
            default:
                break;
        }
        dialog.show();
    }

    private void initSchoolDialog(final Dialog dialog) {
        searchText = schoolDialog.findViewById(R.id.search_text);
        sure = schoolDialog.findViewById(R.id.search);
        clear = schoolDialog.findViewById(R.id.clear);
        schoolList = schoolDialog.findViewById(R.id.school_list);
        searchText.requestFocus();
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {


            }

            @Override
            public void afterTextChanged(Editable arg0) {

                tempSchool = new StringBuffer("");
                int size = arg0.length();
                for (int i = 0; i < size; i++) {
                    tempSchool.append(arg0.charAt(i));
                    tempSchool.append('%');
                }
                handler.sendEmptyMessage(9);
            }
        });
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                searchText.setText("");
                schools.clear();
                tempSchool = new StringBuffer("");
                handler.sendEmptyMessage(8);
            }
        });
        sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                chsnSchool.setText(searchText.getText().toString());
                dialog.dismiss();
            }
        });
        schoolList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                chsnSchool.setText(schools.get(arg2).school_name);
                dialog.dismiss();
            }
        });
        tempSchool = new StringBuffer("");
        handler.sendEmptyMessage(8);
    }

    public void setText() {
        if (userInfo.username != null) {
            ev.setText(userInfo.username);
        }
        if (userDetailInfo.gender != null && userDetailInfo.gender.equals("1")) {
            chsnSex.setText("男");
        } else if (userDetailInfo.gender != null && userDetailInfo.gender.equals("2")) {
            chsnSex.setText("女");
        } else if (userDetailInfo.gender != null && userDetailInfo.gender.equals("0")) {
            chsnSex.setText("保密");
        }
        chsnBirth.setText(userDetailInfo.birthday);
        chsnProvince.setText(userDetailInfo.resideLocation.substring(0, userDetailInfo.resideLocation.indexOf(" ")));
        chsnIdentity.setText(userDetailInfo.occupation);
        chsnEducation.setText(userDetailInfo.education);
        chsnSchool.setText(userDetailInfo.graduateschool);
    }

}
