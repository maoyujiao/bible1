package com.iyuba.core.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.OperateCallBack;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.base.LoginRequest;
import com.iyuba.core.protocol.base.LoginResponse;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.sqlite.mode.UserInfo;
import com.iyuba.core.sqlite.op.UserInfoOp;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.GetLocation;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import personal.iyuba.personalhomelibrary.manager.account.PersonalManager;

/**
 * 用户管理 用于用户信息的保存及权限判断
 *
 * @author chentong
 * @version 1.1 更改内容 引入userinfo数据结构统一管理用户信息
 */
public class AccountManager {
    public static final int LOGIN_STATUS_UNLOGIN = 0;
    public static final int LOGIN_STATUS_LOGIN = 1;
    public final static String USERNAME = "userName";
    public final static String USERID = "userId";
    public final static String USERPASSWORD = "userPassword";
    public final static String IYUBAAMOUNT = "currUserAmount";
    public final static String VALIDITY = "validity";
    public final static String ISVIP = "isVip";
    private static AccountManager instance;
    private static Context mContext;
    public int loginStatus = LOGIN_STATUS_UNLOGIN; // 用户登录状态,默认为未登录状态
    public UserInfo userInfo;
    public String userId; // 用户ID
    public String userName; // 用户姓名
    public String userPwd; // 用户密码
    public String isteacher = "0";

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1: // 弹出错误信息
                    CustomToast.showToast(mContext, R.string.login_fail);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.login_faild);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.person_vip_limit);
                    break;
            }
        }
    };
    private boolean loginSuccess = false;

    private AccountManager() {
    }

    public static synchronized AccountManager Instace(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public static boolean isVip() {
        return ConfigManager.Instance().loadInt("isvip") != 0;
    }

    /**
     * 检查当前用户是否登录
     *
     * @return
     */
    public boolean checkUserLogin() {

        if (userInfo == null || userId == null) {
            return false;
        }
        return loginStatus == LOGIN_STATUS_LOGIN || TouristUtil.isTourist();
    }

    public void setLoginState(int state) {
        loginStatus = state;
        userId = ConfigManager.Instance().loadString("userId");
        String[] nameAndPwd = getUserNameAndPwd();
        userName = nameAndPwd[0];
        userPwd = nameAndPwd[1];
        userInfo = new UserInfoOp(mContext).selectData(userId);
        if (userInfo.vipStatus != null) {
            ConfigManager.Instance().putInt("isvip",
                    Integer.parseInt(userInfo.vipStatus));
        } else {
            userInfo.vipStatus = "0";
            ConfigManager.Instance().putInt("isvip", 0);
            ConfigManager.Instance().putBoolean("isvip_", false);
        }
        setUserStatus(userInfo);
    }

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public boolean login(final String userName, String userPwd,
                         final OperateCallBack rc) {
        this.userName = userName;
        this.userPwd = userPwd;
        String[] strings = GetLocation.getInstance(mContext).getLocation();
        if (strings[0] != null && strings[1] != null) {

        } else {
            strings[0] = "0";
            strings[1] = "0";
        }
        ExeProtocol.exe(new LoginRequest(this.userName, this.userPwd,
                strings[0], strings[1]), new ProtocolResponse() {

            @Override
            public void finish(BaseHttpResponse bhr) {
                LoginResponse rr = (LoginResponse) bhr;
                if (rr.result.equals("101")) {
                    // 登录成功
                    Refresh(rr);
                    if (rc != null) {
                        rc.success(null);
                    }
                } else {
                    // 登录失败
                    handler.sendEmptyMessage(1);
                    loginSuccess = false;
                    if (rc != null) {
                        rc.fail(null);
                    }
                }
            }

            @Override
            public void error() {
                handler.sendEmptyMessage(2);
                loginSuccess = false;
                if (rc != null) {
                    rc.fail(null);
                }
            }
        });
        return loginSuccess;
    }

    /**
     * 用户登出
     *
     * @return
     */
    public boolean loginOut() {
        EventBus.getDefault().post(new LogoutEvent(Integer.parseInt(userId)));

        new UserInfoOp(mContext).delete(userId);
        loginStatus = LOGIN_STATUS_UNLOGIN;
        userId = null; // 用户ID
        userName = null; // 用户姓名
        userPwd = null; // 用户密码
        userInfo = null;
        isteacher = "0";
        SettingConfig.Instance().setAutoLogin(false);
        saveUserNameAndPwd("", "");
        ConfigManager.Instance().putInt("isvip", 0);
        ConfigManager.Instance().putString("userId", null);
        TouristUtil.setTouristLogout(true);
        initCommonConstants();
        return true;
    }

    private void initCommonConstants() {
        User user = new User();
        user.vipStatus = getVipStatus() + "";
        user.uid = (AccountManager.Instace(mContext).userId) == null ? 0 : Integer.parseInt(AccountManager.Instace(mContext).userId);
        user.name = AccountManager.Instace(mContext).userName;
        IyuUserManager.getInstance().setCurrentUser(user);
        PersonalManager.getInstance().AppId = Constant.APPID;
        PersonalManager.getInstance().categoryType = Constant.APPName;
        PersonalManager.getInstance().setUserId(user.uid);


    }
    /**
     * 更换用户
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public boolean replaceUserLogin(String userName, String userPwd) {
        if (loginOut()) { // 登出
            return login(userName, userPwd, null);
        }
        return false;
    }

    /**
     * 保存账户密码
     *
     * @param userName
     * @param userPwd
     */
    public void saveUserNameAndPwd(String userName, String userPwd) {
        ConfigManager.Instance().putString("userName", userName);
        ConfigManager.Instance().putString("userPwd", userPwd);
    }

    /**
     * 获取用户名及密码
     *
     * @return string[2] [0]=userName,[1]=userPwd
     */
    public String[] getUserNameAndPwd() {
        String[] nameAndPwd = new String[]{
                ConfigManager.Instance().loadString("userName"),
                ConfigManager.Instance().loadString("userPwd")};
        return nameAndPwd;
    }


    /**
     * 处理userinfo数据写入
     */
    public void Refresh(LoginResponse rr) {
        userId = rr.uid; // 成功返回用户ID
        ConfigManager.Instance().putString("userId", userId);
        userInfo = new UserInfoOp(mContext).selectData(userId);
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        isteacher = rr.isteacher;
        userInfo.uid = userId;
        userInfo.username = userName;
        userInfo.iyubi = rr.amount;
        userInfo.vipStatus = rr.vip;
        userInfo.isteacher = rr.isteacher;
        ConfigManager.Instance().putString("config_is_teacher", isteacher);

        long time = Long.parseLong(rr.validity);
        if (time < 0) {
            userInfo.deadline = "终身VIP";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.CHINA);
            try {
                long allLife = sdf.parse("2099-01-01").getTime() / 1000;
                if (time > allLife) {
                    userInfo.deadline = "终身VIP";
                } else {
                    userInfo.deadline = sdf.format(new Date(time * 1000));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        loginSuccess = true;
        loginStatus = LOGIN_STATUS_LOGIN;
        if (!(userInfo.vipStatus.equals("0"))) {
            SettingConfig.Instance().setHighSpeed(true);
            ConfigManager.Instance().putBoolean("isvip_", true);
        }
        else {
            SettingConfig.Instance().setHighSpeed(false);
            ConfigManager.Instance().putBoolean("isvip_", false);
        }
        new UserInfoOp(mContext).saveData(userInfo);
        ConfigManager.Instance().putInt("isvip",
                Integer.parseInt(userInfo.vipStatus));

        setUserStatus(userInfo);

    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        if (TouristUtil.isTourist()) {
            TouristUtil.saveUserInfo(userInfo);
        }
        setUserStatus(userInfo);
    }

    private void setUserStatus(UserInfo userInfo) {
        isteacher = ConfigManager.Instance().loadString("config_is_teacher", "0");
        userId = userInfo.uid;
        userName = userInfo.username;
        this.userInfo.isteacher = isteacher;
        // 成功返回用户ID
        //用户登录成功之后就保存到用户的uid并保存是否是Teacher
        ConfigManager.Instance().putString("userId", userId);

        if (!(userInfo.vipStatus.equals("0"))) {
            SettingConfig.Instance().setHighSpeed(true);
            ConfigManager.Instance().putBoolean("isvip_", true);
        } else {
            SettingConfig.Instance().setHighSpeed(false);
            ConfigManager.Instance().putBoolean("isvip_", false);
        }
        ConfigManager.Instance().putInt("isvip", Integer.parseInt(userInfo.vipStatus));

    }

    public String getId() {
        if (TextUtils.isEmpty(userId)) {
            return "0";
        }
        return userId;
    }

    public String getUserName() {
        if (TextUtils.isEmpty(userName) || "null".equals(userName)) {
            return getId();
        }
        return userName;
    }

    public static boolean isGoldVip() {
        return ConfigManager.Instance().loadInt("isvip") > 1;
    }

    public int getVipStatus() {
        return ConfigManager.Instance().loadInt("isvip");
    }
}