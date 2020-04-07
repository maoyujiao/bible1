package com.iyuba.core.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.me.UpLoadImage;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.base.RegistRequest;
import com.iyuba.core.protocol.base.RegistResponse;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

/**
 * 注册界面
 *
 * @author chentong
 * @version 1.1 修改内容 更新API
 */
public class RegistActivity extends BasisActivity {
    private Context mContext;
    private EditText userName, userPwd, reUserPwd, email;
    private Button regBtn;
    private String userNameString;
    private String userPwdString;
    private String reUserPwdString;
    private String emailString;
    private boolean send = false;
    private CustomDialog wettingDialog;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    finish();
                    break;
                case 1: // 弹出错误信息
                    CustomToast.showToast(mContext, R.string.regist_email_used);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.regist_userid_exist);
                    break;
                case 4:
                    CustomToast.showToast(mContext, msg.obj.toString());
                    break;
                case 5:
                    wettingDialog.show();
                    break;
                case 6:
                    wettingDialog.dismiss();
                    break;
                case 7:
                    CustomToast.showToast(mContext, R.string.regist_operating);
                    break;
            }
        }
    };
    private TextView protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.regist_layout);
        CrashApplication.getInstance().addActivity(this);
        wettingDialog = WaittingDialog.showDialog(mContext);
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userName = findViewById(R.id.editText_userId);
        userPwd = findViewById(R.id.editText_userPwd);
        reUserPwd = findViewById(R.id.editText_reUserPwd);
        email = findViewById(R.id.editText_email);
        regBtn = findViewById(R.id.button_regist);
        regBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (verification()) { // 验证通过
                    // 开始注册
                    if (!send) {
                        send = true;
                        handler.sendEmptyMessage(5);
                        regist();
                    } else {
                        handler.sendEmptyMessage(7);
                    }
                }
            }
        });
        String remindString ="我已阅读并同意使用条款和隐私政策";

        protocol = findViewById(R.id.protocol);
        SpannableStringBuilder spannableStringBuilder  = new SpannableStringBuilder(remindString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Web.start(mContext, Constant.PROTOCOL_URL_HEADER+Constant.APPName,"用户隐私协议");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
            }
        } ;
        spannableStringBuilder.setSpan(clickableSpan, remindString.indexOf("使用条款"), remindString.indexOf("使用条款")+9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocol.setText(spannableStringBuilder);
        protocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 验证
     */
    public boolean verification() {
        userNameString = userName.getText().toString();
        userPwdString = userPwd.getText().toString();
        reUserPwdString = reUserPwd.getText().toString();
        emailString = email.getText().toString().trim();
        if (userNameString.length() == 0) {
            userName.setError(mContext
                    .getString(R.string.regist_check_username_1));
            return false;
        }
        if (!checkUserId(userNameString)) {
            userName.setError(mContext
                    .getString(R.string.regist_check_username_1));
            return false;
        }
        if (!checkUserName(userNameString)) {
            userName.setError(mContext
                    .getString(R.string.regist_check_username_2));
            return false;
        }
        if (userPwdString.length() == 0) {
            userPwd.setError(mContext
                    .getString(R.string.regist_check_userpwd_1));
            return false;
        }
        if (!checkUserPwd(userPwdString)) {
            userPwd.setError(mContext
                    .getString(R.string.regist_check_userpwd_1));
            return false;
        }
        if (!reUserPwdString.equals(userPwdString)) {
            reUserPwd.setError(mContext
                    .getString(R.string.regist_check_reuserpwd));
            return false;
        }
        if (emailString.length() == 0) {
            email.setError(getResources().getString(
                    R.string.regist_check_email_1));
            return false;
        }
        if (!emailCheck(emailString)) {
            email.setError(mContext.getString(R.string.regist_check_email_2));
            return false;
        }
        return true;
    }

    /**
     * 匹配用户名
     *
     * @param userId
     * @return
     */
    public boolean checkUserId(String userId) {
        return userId.length() >= 3 && userId.length() <= 20;
    }

    /**
     * 匹配用户名2 验证非手机号 邮箱号
     *
     * @param userId
     * @return
     */
    public boolean checkUserName(String userId) {
        if (userId
                .matches("^([a-z0-ArrayA-Z]+[-_|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
            return false;
        }
        return !userId.matches("^(1)\\d{10}$");
    }

    /**
     * 匹配密码
     *
     * @param userPwd
     * @return
     */
    public boolean checkUserPwd(String userPwd) {
        return userPwd.length() >= 6 && userPwd.length() <= 20;
    }

    /**
     * email格式匹配
     *
     * @param email
     * @return
     */
    public boolean emailCheck(String email) {
        return email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
//        return email
//                .matches("^([a-z0-ArrayA-Z]+[-_|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    private void regist() {
        ExeProtocol.exe(new RegistRequest(userName.getText().toString(), AccountManager.Instace(mContext).getId(),
                        userPwd.getText().toString(), email.getText().toString().trim()),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        RegistResponse rr = (RegistResponse) bhr;
                        send = false;
                        handler.sendEmptyMessage(6);
                        if (rr.result.equals("111")) {
                            Looper.prepare();
                            AccountManager.Instace(mContext).login(
                                    userName.getText().toString(),
                                    userPwd.getText().toString(),
                                    new OperateCallBack() {
                                        @Override
                                        public void success(String result) {
                                            if (SettingConfig.Instance().isAutoLogin()) {// 保存账户密码
                                                AccountManager.Instace(mContext)
                                                        .saveUserNameAndPwd(
                                                                userName.getText().toString(),
                                                                userPwd.getText().toString());
                                            } else {
                                                AccountManager.Instace(mContext)
                                                        .saveUserNameAndPwd("", "");
                                            }
                                            Intent intent = new Intent(
                                                    mContext, UpLoadImage.class);
                                            intent.putExtra("regist", true);
                                            startActivity(intent);
                                            handler.sendEmptyMessage(0);

                                            TouristUtil.clearTouristInfo();
                                        }

                                        @Override
                                        public void fail(String message) {

                                        }
                                    });
                            Looper.loop();
                        } else if (rr.result.equals("112")) {
                            handler.sendEmptyMessage(3);
                        } else if (rr.result.equals("114")) {
                            handler.obtainMessage(4, rr.message).sendToTarget();
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void error() {
                        send = false;
                        handler.sendEmptyMessage(2);
                        handler.sendEmptyMessage(6);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.button_regist_phone).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext,
                                RegistByPhoneActivity.class));
                        finish();
                    }
                });
    }
}
