package com.iyuba.core.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.iyuba.imooclib.ImoocManager;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;

/**
 * 登录界面
 *
 * @author chentong
 * @version 1.2 修改内容 API更新; userinfo引入; VIP更新方式变化
 */
public class Login extends BasisActivity {
    private Button registBtn, loginBtn;
    private String userName, userPwd;
    private EditText userNameET, userPwdET;
    private CheckBox autoLogin;
    private CustomDialog cd;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    userNameET.setText(userName);
                    userPwdET.setText(userPwd);
                    break;
                case 1:
                    cd.show();
                    break;
                case 2:
                    cd.dismiss();
                    break;
                case 3:
                    finish();
                    break;
            }
        }
    };
    private Context mContext;
    private TextView findPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        cd = WaittingDialog.showDialog(mContext);
        userNameET = findViewById(R.id.editText_userId);
        userPwdET = findViewById(R.id.editText_userPwd);
        if (SettingConfig.Instance().isAutoLogin()) {// 保存账户密码
            String[] nameAndPwd = AccountManager.Instace(mContext)
                    .getUserNameAndPwd();
            userName = nameAndPwd[0];
            userPwd = nameAndPwd[1];
            handler.sendEmptyMessage(0);
        }

        autoLogin = findViewById(R.id.checkBox_autoLogin);
        autoLogin.setChecked(SettingConfig.Instance().isAutoLogin());
        autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingConfig.Instance().setAutoLogin(true);
            }
        });
        if (!autoLogin.isChecked()) {
            autoLogin.setChecked(true);
            SettingConfig.Instance().setAutoLogin(true);
        }

        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginBtn = findViewById(R.id.button_login);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登录操作
                if (verification()) {
                    handler.sendEmptyMessage(1);
                    AccountManager.Instace(mContext).login(userName, userPwd,
                            new OperateCallBack() {
                                @Override
                                public void success(String result) {
                                    if (SettingConfig.Instance().isAutoLogin()) {// 保存账户密码
                                        AccountManager.Instace(mContext)
                                                .saveUserNameAndPwd(userName,
                                                        userPwd);
                                    } else {
                                        AccountManager.Instace(mContext)
                                                .saveUserNameAndPwd("", "");
                                    }
                                    handler.sendEmptyMessage(2);
                                    handler.sendEmptyMessage(3);
                                    TouristUtil.clearTouristInfo();
                                    setImoocStatus();
                                }

                                @Override
                                public void fail(String message) {
                                    handler.sendEmptyMessage(2);
                                }
                            });
                }
            }
        });
        registBtn = findViewById(R.id.button_regist);
        registBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, RegistActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findPassword = findViewById(R.id.find_password);
        findPassword
                .setText(Html
                        .fromHtml("<a href=\"http://m.iyuba.cn/m_login/inputPhonefp.jsp?\">"
                                + getResources().getString(
                                R.string.login_find_password) + "</a>"));
        findPassword.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 验证
     */
    public boolean verification() {
        userName = userNameET.getText().toString();
        userPwd = userPwdET.getText().toString();
        if (userName.length() < 3) {
            userNameET.setError(getResources().getString(
                    R.string.login_check_effective_user_id));
            return false;
        }

        if (userPwd.length() == 0) {
            userPwdET.setError(getResources().getString(
                    R.string.login_check_user_pwd_null));
            return false;
        }
        if (!checkUserPwd(userPwd)) {
            userPwdET.setError(getResources().getString(
                    R.string.login_check_user_pwd_constraint));
            return false;
        }
        return true;
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

    @Override
    protected void onPause() {
        super.onPause();
        if (AccountManager.Instace(mContext).userName == null
                || AccountManager.Instace(mContext).userName.equals("")) {
            SettingConfig.Instance().setAutoLogin(true);
        }
    }


    private void setImoocStatus() {
        ImoocManager.appId = Constant.APPID;
        User user = new User();

        if (AccountManager.Instace(mContext.getApplicationContext())
                .checkUserLogin()) {
            user.vipStatus = ConfigManager.Instance().loadInt("isvip") + "";
            user.name = AccountManager.Instace(mContext).getUserName();
            user.uid = Integer.parseInt(ConfigManager.Instance().loadString("userId"));
            IyuUserManager.getInstance().setCurrentUser(user);
        } else {
            user.vipStatus = "0";
            user.name = "";
            user.uid = 0;
            IyuUserManager.getInstance().setCurrentUser(user);
        }
    }

}
