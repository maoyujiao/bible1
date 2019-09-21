package com.iyuba.core.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.util.SmsContent;
import com.iyuba.core.util.TelNumMatch;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;
import com.mob.MobSDK;

import java.util.Timer;
import java.util.TimerTask;


import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

/**
 * 手机注册界面
 *
 * @author czf
 * @version 1.0
 */


public class RegistByPhoneActivity extends BasisActivity {
    private Context mContext;
    private EditText phoneNum, messageCode;
    private Button getCodeButton;
    private TextView toEmailButton;
    private String phoneNumString = "", messageCodeString = "";
    @SuppressLint("HandlerLeak")
    Handler handlerSms = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                    CustomToast.showToast(mContext, "验证成功");
                    Intent intent = new Intent();
                    intent.setClass(mContext, RegistSubmitActivity.class);
                    intent.putExtra("phoneNumb", phoneNumString);
                    startActivity(intent);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    boolean smart = (Boolean) msg.obj;
                    if (smart) {
                        //通过智能验证
                        CustomToast.showToast(mContext, "已通过短信智能验证，请完成注册");
                        Intent intent = new Intent();
                        intent.setClass(mContext, RegistSubmitActivity.class);
                        intent.putExtra("phoneNumb", phoneNumString);
                        startActivity(intent);
                        finish();
                    } else {
                        //依然走短信验证
                        CustomToast.showToast(mContext, "验证码已经发送，请等待接收");
                    }
                }
            } else {
                Log.e("RegistByPhoneActivity", "" + result);
                //CustomToast.showToast(mContext, "验证失败，请输入正确的验证码！");
                getCodeButton.setText("获取验证码");
                getCodeButton.setEnabled(true);
            }
        }
    };
    private Timer timer;
    @SuppressLint("HandlerLeak")
    Handler handler_time = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Handler处理消息
            if (msg.what > 0) {
                getCodeButton.setText("重新发送(" + msg.what + "s)");
            } else {
                timer.cancel();
                getCodeButton.setEnabled(true);
                getCodeButton.setText("获取验证码");
            }
        }
    };
    private TextView protocol;
    private EventHandler eh;
    private TimerTask timerTask;
    private SmsContent smsContent;
    private CustomDialog waittingDialog;
    @SuppressLint("HandlerLeak")
    Handler handler_waitting = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    waittingDialog.show();
                    break;
                case 2:
                    waittingDialog.dismiss();
                    break;
                case 3:
                    CustomToast.showToast(mContext, "手机号已注册，请换一个号码试试~", 2000);
                    break;
                case 4:
                    CustomToast.showToast(mContext, "电话不能为空");
                    break;
            }
        }
    };
    private EditTextWatch editTextWatch;
    private Button nextstep_unfocus;
    private Button nextstep_focus;
    @SuppressLint("HandlerLeak")
    Handler handler_verify = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Handler处理消息
            if (msg.what == 0) {
                timer.cancel();
                /*
				 * getCodeButton.setText("下一步"); getCodeButton.setEnabled(true);
				 */
                String verifyCode = (String) msg.obj;
                messageCode.setText(verifyCode);
                nextstep_focus.setVisibility(View.VISIBLE);
                nextstep_focus.setEnabled(true);
            } else if (msg.what == 1) {
                SMSSDK.getVerificationCode("86", phoneNum.getText().toString());
                timer = new Timer();
                timerTask = new TimerTask() {
                    int i = 60;

                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = i--;
                        handler_time.sendMessage(msg);
                    }
                };
                timer.schedule(timerTask, 1000, 1000);
                getCodeButton.setTextColor(Color.WHITE);
				/*getCodeButton.setEnabled(false);*/
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        editTextWatch = new EditTextWatch();
        setContentView(R.layout.regist_layout_phone);
        CrashApplication.getInstance().addActivity(this);
        waittingDialog = WaittingDialog.showDialog(mContext);
        messageCode = findViewById(R.id.regist_phone_code);
        messageCode.addTextChangedListener(editTextWatch);
        phoneNum = findViewById(R.id.regist_phone_numb);
        phoneNum.addTextChangedListener(editTextWatch);
        getCodeButton = findViewById(R.id.regist_getcode);
        nextstep_unfocus = findViewById(R.id.nextstep_unfocus);
        nextstep_unfocus.setEnabled(false);
        nextstep_focus = findViewById(R.id.nextstep_focus);
        MobSDK.init(getApplicationContext(), Constant.APP_CONSTANT.MOB_APPKEY(), Constant.APP_CONSTANT.MOB_APP_SECRET());
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handlerSms.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
        smsContent = new SmsContent(RegistByPhoneActivity.this, handler_verify);
        protocol = findViewById(R.id.protocol);
        protocol.setText(Html
                .fromHtml("我已阅读并同意<a href=\"https://ai.iyuba.cn/api/protocol.jsp?apptype="+
                        Constant.APP_CONSTANT.APPName()+"\">使用条款和隐私政策</a>"));
        protocol.setMovementMethod(LinkMovementMethod.getInstance());
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toEmailButton = findViewById(R.id.regist_email);
        toEmailButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
        toEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, RegistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        getCodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (verificationNum()) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    handler_waitting.sendEmptyMessage(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    phoneNumString = phoneNum.getText().toString();

                    String path = "http://api.iyuba.com.cn/sendMessage3.jsp?format=json"
                            + "&userphone=" + phoneNumString;
                    Http.get(path, new HttpCallback() {
                        @Override
                        public void onSucceed(Call call, String response) {
                            Log.e("===okHttp==", response);
                            RegisterMessageBean bean;
                            try {
                                bean = new Gson().fromJson(response, RegisterMessageBean.class);
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                                handler_waitting.sendEmptyMessage(2);
                                return;
                            }
                            if ("1".equals(bean.getResult())) {
                                handler_verify.sendEmptyMessage(1);
                                RegistByPhoneActivity.this
                                        .getContentResolver()
                                        .registerContentObserver(
                                                Uri.parse("content://sms/"),
                                                true, smsContent);
                            } else if ("-1".equals(bean.getResult())) {
                                handler_waitting.sendEmptyMessage(3);
                            }
                            handler_waitting.sendEmptyMessage(2);
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            Log.e("===okHttp==error==", e.getMessage());
                            handler_waitting.sendEmptyMessage(2);
                        }
                    });
//                    ExeProtocol.exe(new RequestSubmitMessageCode(
//                            phoneNumString), new ProtocolResponse() {
//
//                        @Override
//                        public void finish(BaseHttpResponse bhr) {
//                            ResponseSubmitMessageCode res = (ResponseSubmitMessageCode) bhr;
//                            if (res != null) {
//                                if (res.result.equals("1")) {
//                                    handler_verify.sendEmptyMessage(1);
//                                    RegistByPhoneActivity.this
//                                            .getContentResolver()
//                                            .registerContentObserver(
//                                                    Uri.parse("content://sms/"),
//                                                    true, smsContent);
//                                } else if (res.result.equals("-1")) {
//                                    handler_waitting.sendEmptyMessage(3);
//                                }
//                                handler_waitting.sendEmptyMessage(2);
//                            }
//
//                        }
//
//                        @Override
//                        public void error() {
//                            handler_waitting.sendEmptyMessage(2);
//                        }
//                    });
                } else {
                    handler_waitting.sendEmptyMessage(4);
                }

            }
        });
        nextstep_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verification()) {
                    SMSSDK.submitVerificationCode("86", phoneNumString,
                            messageCode.getText().toString());
                } else {
                    CustomToast.showToast(mContext, "验证码不能为空");
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    public boolean verification() {
        phoneNumString = phoneNum.getText().toString();
        messageCodeString = messageCode.getText().toString();
        if (phoneNumString.length() == 0) {
            phoneNum.setError("手机号不能为空");
            return false;
        }
        if (!checkPhoneNum(phoneNumString)) {
            phoneNum.setError("手机号输入错误");
            return false;
        }
        if (messageCodeString.length() == 0) {
            messageCode.setError("验证码不能为空");
            return false;
        }
        return true;
    }

    /**
     * 验证
     */
    public boolean verificationNum() {
        phoneNumString = phoneNum.getText().toString();
        messageCodeString = messageCode.getText().toString();
        if (phoneNumString.length() == 0) {
            phoneNum.setError("手机号不能为空");
            return false;
        }
        if (!checkPhoneNum(phoneNumString)) {
            phoneNum.setError("手机号输入错误");
            return false;
        }

        return true;
    }

    public boolean checkPhoneNum(String userId) {
        if (userId.length() < 2)
            return false;
        TelNumMatch match = new TelNumMatch(userId);
        int flag = match.matchNum();
        /*不check 号码的正确性，只check 号码的长度*/
        /*if (flag == 1 || flag == 2 || flag == 3) {
            return true;
		} else {
			return false;
		}*/
        return flag != 5;
    }

    public class EditTextWatch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if ((s.toString().length() == 4 && verificationNum()) || (verificationNum() && messageCode.getText().toString().length() == 4)) {
                if (timer != null) {
                    timer.cancel();
                }
                nextstep_focus.setVisibility(View.VISIBLE);
                nextstep_focus.setEnabled(true);
            } else {
                nextstep_focus.setVisibility(View.GONE);
                nextstep_focus.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }
}
