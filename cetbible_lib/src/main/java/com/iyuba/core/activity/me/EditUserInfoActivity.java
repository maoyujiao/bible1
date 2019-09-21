package com.iyuba.core.activity.me;

/**
 * 编辑个人信息界面
 *
 * @author chentong
 * @version 1.0
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.adapter.me.SchoolListAdapter;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.sqlite.mode.EditUserInfo;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.base.LocationRequest;
import com.iyuba.core.protocol.base.LocationResponse;
import com.iyuba.core.protocol.message.RequestEditUserInfo;
import com.iyuba.core.protocol.message.RequestUserDetailInfo;
import com.iyuba.core.protocol.message.ResponseEditUserInfo;
import com.iyuba.core.protocol.message.ResponseUserDetailInfo;
import com.iyuba.core.sqlite.mode.me.School;
import com.iyuba.core.sqlite.op.SchoolOp;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.GetLocation;
import com.iyuba.core.util.JudgeZodicaAndConstellation;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditUserInfoActivity extends BasisActivity {
    private final static int GENDER_DIALOG = 1;// 性别选择
    private final static int DATE_DIALOG = 2;// 日期选择
    private final static int SCHOOL_DIALOG = 3;// 学校选择
    private Context mContext;
    private TextView gender, birthday, zodiac, constellation, school;
    private EditText location;
    private LinearLayout changeImageLayout;
    private ImageView userImage;
    private Button back, save;
    private Calendar calendar = null;
    private EditUserInfo editUserInfo = new EditUserInfo();
    private CustomDialog waitingDialog;
    private String cityName;
    // school
    private View schoolDialog;
    private EditText searchText;
    private Button sure;
    private View clear;
    private ListView schoolList;
    private ArrayList<School> schools = new ArrayList<School>();
    private SchoolListAdapter schoolListAdapter;
    private StringBuffer tempSchool;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitingDialog.show();
                    GetAddr();
                    handler.sendEmptyMessage(1);
                    break;
                case 1:
                    ExeProtocol.exe(
                            new RequestUserDetailInfo(AccountManager
                                    .Instace(mContext).userId),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    ResponseUserDetailInfo responseUserDetailInfo = (ResponseUserDetailInfo) bhr;
                                    if (responseUserDetailInfo.result.equals("211")) {
                                        editUserInfo = responseUserDetailInfo.editUserInfo;
                                    }
                                    handler.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {

                                    handler.sendEmptyMessage(2);
                                    handler.sendEmptyMessage(6);
                                }
                            });
                    break;
                case 2:
                    waitingDialog.dismiss();
                    setText();
                    break;
                case 3:
//				save.setClickable(true);
                    //TODO
                    finish();
                    CustomToast.showToast(mContext, R.string.person_info_success);
                    break;
                case 4:
                    save.setClickable(true);
                    CustomToast.showToast(mContext, R.string.person_info_fail);
                    break;
                case 5:
                    setText();
                    break;
                case 6:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 7:
                    CustomToast.showToast(mContext, R.string.check_gps);
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
                case 10:
                    location.setText(cityName);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edituserinfo);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        waitingDialog = WaittingDialog.showDialog(mContext);
        initWidget();
        LoadInfo();
        setListener();
    }

    private void initWidget() {

        userImage = findViewById(R.id.iveditPortrait);
        gender = findViewById(R.id.editGender);
        birthday = findViewById(R.id.editBirthday);
        location = findViewById(R.id.editResideLocation);
        zodiac = findViewById(R.id.editZodiac);
        constellation = findViewById(R.id.editConstellation);
        changeImageLayout = findViewById(R.id.editPortrait);
        back = findViewById(R.id.button_back);
        save = findViewById(R.id.editinfo_save_btn);
        school = findViewById(R.id.editSchool);
    }

    private void setText() {

        if (editUserInfo.getEdGender() != null) {
            if (editUserInfo.getEdGender().equals("1")) {
                gender.setText(getResources().getStringArray(R.array.gender)[0]);
            } else if (editUserInfo.getEdGender().equals("2")) {
                gender.setText(getResources().getStringArray(R.array.gender)[1]);
            }
        } else {
            gender.setText(getResources().getStringArray(R.array.gender)[0]);
        }
        birthday.setText(editUserInfo.getBirthday());
        zodiac.setText(editUserInfo.getEdZodiac());
        constellation.setText(editUserInfo.getEdConstellation());
        location.setText(editUserInfo.getEdResideCity());
        school.setText(editUserInfo.getUniversity());
        GitHubImageLoader.Instace(mContext).setCirclePic(
                AccountManager.Instace(mContext).userId, userImage);
    }

    private void LoadInfo() {

        handler.sendEmptyMessage(0);
    }

    private void setListener() {

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        changeImageLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UpLoadImage.class);
                startActivity(intent);
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createDialog(GENDER_DIALOG);
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createDialog(DATE_DIALOG);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                save.setClickable(false);
                String city = location.getText().toString();
                city = city.trim();
                String value = "", key = "";
                StringBuffer sb = new StringBuffer("");
                int i;
                if (city.contains(" ")) {
                    String[] area = city.split(" ");
                    sb.append(editUserInfo.getEdGender()).append(",");
                    sb.append(editUserInfo.getEdBirthYear()).append(",");
                    sb.append(editUserInfo.getEdBirthMonth()).append(",");
                    sb.append(editUserInfo.getEdBirthDay()).append(",");
                    sb.append(editUserInfo.getEdConstellation()).append(",");
                    sb.append(editUserInfo.getEdZodiac()).append(",");
                    sb.append(school.getText()).append(",");
                    for (i = 0; i < area.length; i++) {
                        sb.append(area[i]).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    value = sb.toString();
                    if (i == 3) {
                        key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,resideprovince,residecity,residedist";
                    } else {
                        key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,resideprovince,residecity";
                    }
                } else {
                    sb.append(editUserInfo.getEdGender()).append(",");
                    sb.append(editUserInfo.getEdBirthYear()).append(",");
                    sb.append(editUserInfo.getEdBirthMonth()).append(",");
                    sb.append(editUserInfo.getEdBirthDay()).append(",");
                    sb.append(editUserInfo.getEdConstellation()).append(",");
                    sb.append(editUserInfo.getEdZodiac()).append(",");
                    sb.append(school.getText()).append(",");
                    sb.append(city);
                    value = sb.toString();
                    key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,residecity";
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
                                    handler.sendEmptyMessage(4);
                                }
                            }

                            @Override
                            public void error() {

                                handler.sendEmptyMessage(6);
                            }
                        });
            }
        });
        school.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createDialog(SCHOOL_DIALOG);
            }
        });
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
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                searchText.setText("");
                schools.clear();
                tempSchool = new StringBuffer("");
                handler.sendEmptyMessage(8);
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                school.setText(searchText.getText().toString());
                dialog.dismiss();
            }
        });
        schoolList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                school.setText(schools.get(arg2).school_name);
                dialog.dismiss();
            }
        });
        tempSchool = new StringBuffer("");
        handler.sendEmptyMessage(8);
    }

    private void GetAddr() {
        String locationPos[] = GetLocation.getInstance(mContext).getLocation();
        String latitude = locationPos[0];
        String longitude = locationPos[1];
        if (latitude.equals("0.0") && longitude.equals("0.0")) {
            handler.sendEmptyMessage(7);
        }
        ExeProtocol.exe(new LocationRequest(latitude, longitude),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {


                        LocationResponse lcr = (LocationResponse) bhr;
                        StringBuffer builder = new StringBuffer();
                        builder.append(lcr.province).append(" ");
                        builder.append(lcr.locality).append(" ");
                        builder.append(lcr.subLocality);
                        cityName = builder.toString();
                        handler.sendEmptyMessage(10);
                    }

                    @Override
                    public void error() {


                    }
                });
    }

    private void createDialog(int id) {
        Dialog dialog = null;
        Builder builder = new AlertDialog.Builder(mContext);
        switch (id) {
            case GENDER_DIALOG:
                builder.setTitle(R.string.person_info_gender);
                builder.setSingleChoiceItems(R.array.gender, 0,
                        new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 1) {
                                    editUserInfo.setEdGender("2");
                                } else if (which == 0) {
                                    editUserInfo.setEdGender("1");
                                }
                                handler.sendEmptyMessage(5);
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton(R.string.alert_btn_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                dialog = builder.create();
                break;
            case DATE_DIALOG:
                calendar = Calendar.getInstance();
                dialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                editUserInfo.setEdBirthDay(dayOfMonth);
                                editUserInfo.setEdBirthYear(year);
                                editUserInfo.setEdBirthMonth(month + 1);
                                Calendar cal = new GregorianCalendar(year, month,
                                        dayOfMonth);
                                String constellation = JudgeZodicaAndConstellation
                                        .date2Constellation(cal);
                                String zodiac = JudgeZodicaAndConstellation
                                        .date2Zodica(cal);
                                editUserInfo.setEdZodiac(zodiac);
                                editUserInfo.setEdConstellation(constellation);
                                editUserInfo.setBirthday(year + "-" + (month + 1)
                                        + "-" + dayOfMonth);
                                handler.sendEmptyMessage(5);
                            }
                        }, calendar.get(Calendar.YEAR), // 传入年份
                        calendar.get(Calendar.MONTH), // 传入月份
                        calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                dialog.setTitle(R.string.person_info_birth);
                break;
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

}
