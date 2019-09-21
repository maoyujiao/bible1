package com.iyuba.core.me.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestUserDetailInfo;
import com.iyuba.core.protocol.message.ResponseUserDetailInfo;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;

/**
 * 详细信息
 *
 * @author ct
 * @version 1.0
 * @para "currentuid" "currentname"
 */
public class UserDetailInfo extends BasisActivity {
    private TextView tvUserName, tvGender, tvResideLocation, tvBirthday,
            tvConstellation, tvZodiac, tvGraduatesSchool, tvCompany,
            tvAffectivestatus, tvLookingfor, tvIntro, tvInterest;
    private ResponseUserDetailInfo userDetailInfo;
    private Button back;
    private String currentuid, currentname;
    private CustomDialog waitting;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
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
                default:
                    break;
            }
        }
    };
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetailinfo);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        waitting = WaittingDialog.showDialog(mContext);
        Intent intent = getIntent();
        currentuid = intent.getStringExtra("currentuid");
        currentname = intent.getStringExtra("currentname");
        initWidget();
        handler.sendEmptyMessage(0);
    }

    private void setText() {
        tvUserName.setText(currentname);
        if (userDetailInfo.gender.equals("1")) {
            tvGender.setText("男");
        } else if (userDetailInfo.gender.equals("2")) {
            tvGender.setText("女");
        } else if (userDetailInfo.gender.equals("0")) {
            tvGender.setText("保密");
        }
        tvResideLocation.setText(userDetailInfo.resideLocation);
        tvBirthday.setText(userDetailInfo.birthday);
        tvConstellation.setText(userDetailInfo.constellation);
        tvZodiac.setText(userDetailInfo.zodiac);
        tvGraduatesSchool.setText(userDetailInfo.graduateschool);
        tvCompany.setText(userDetailInfo.company);
        tvAffectivestatus.setText(userDetailInfo.affectivestatus);
        tvLookingfor.setText(userDetailInfo.lookingfor);
        tvIntro.setText(userDetailInfo.bio);
        tvInterest.setText(userDetailInfo.interest);
    }

    private void initWidget() {
        tvUserName = findViewById(R.id.tvUserName);
        tvGender = findViewById(R.id.tvGender);
        tvResideLocation = findViewById(R.id.tvResideLocation);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvConstellation = findViewById(R.id.tvConstellation);
        tvZodiac = findViewById(R.id.tvZodiac);
        tvGraduatesSchool = findViewById(R.id.tvGraduatesSchool);
        tvCompany = findViewById(R.id.tvCompany);
        tvAffectivestatus = findViewById(R.id.tvAffectivestatus);
        tvLookingfor = findViewById(R.id.tvLookingfor);
        tvIntro = findViewById(R.id.tvBio);
        tvInterest = findViewById(R.id.tvInterest);
        back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
