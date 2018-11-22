package com.iyuba.CET4bible.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.VersionManager;
import com.iyuba.CET4bible.protocol.FeedBackJsonRequest;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.activity.me.Chatting;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestSendMessageLetter;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomDialog;
import com.iyuba.core.widget.dialog.CustomToast;
import com.iyuba.core.widget.dialog.WaittingDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 意见反馈Activity
 *
 * @author chentong
 */

public class Feedback extends BasisActivity {
    private CustomDialog wettingDialog;
    private Button backBtn, developer;
    private View submit;
    private EditText context, email;
    private String content;
    private Context mContext;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CustomToast.showToast(mContext,
                            R.string.feedback_submit_success);
                    finish();
                    break;
                case 1:
                    CustomToast
                            .showToast(mContext, R.string.feedback_network_error);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.feedback_submitting);
                    break;
                case 3:
                    ClientSession.Instace().asynGetResponse(
                            new RequestSendMessageLetter(
                                    AccountManager.Instace(mContext).userId,
                                    "tunye", content), new IResponseReceiver() {
                                @Override
                                public void onResponse(BaseHttpResponse request,
                                                       BaseHttpRequest response, int rspCookie) {

                                }

                            });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        wettingDialog = WaittingDialog.showDialog(this);
        context = findViewById(R.id.editText_info);
        email = findViewById(R.id.editText_Contact);
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        developer = findViewById(R.id.developer);
        developer.setVisibility(View.GONE);
        developer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent;
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    intent = new Intent();
                    intent.putExtra("friendid", "46738");
                    intent.putExtra("currentname", "开发人员");
                    intent.setClass(mContext, Chatting.class);
                    mContext.startActivity(intent);
                } else {
                    intent = new Intent(mContext, Login.class);
                    mContext.startActivity(intent);
                }
            }
        });
        submit = findViewById(R.id.ImageView_submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!context.getText().toString().equals(content)) {
                    makeContent();
                    String uid = "";
                    if (verification()) {
                        if (AccountManager.Instace(mContext).checkUserLogin()) {
                            handler.sendEmptyMessage(3);
                            uid = AccountManager.Instace(mContext).getId();
                        }
                        wettingDialog.show();
                        ExeProtocol.exe(
                                new FeedBackJsonRequest(TextAttr
                                        .encode(content), email.getText().toString().trim(), uid),
                                new ProtocolResponse() {

                                    @Override
                                    public void finish(BaseHttpResponse bhr) {

                                        wettingDialog.dismiss();
                                        handler.sendEmptyMessage(0);
                                    }

                                    @Override
                                    public void error() {

                                        handler.sendEmptyMessage(1);
                                        wettingDialog.dismiss();
                                    }
                                });
                    }
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    /**
     * @return
     */
    private void makeContent() {

        content = context.getText().toString() + "\nappversion:["
                + VersionManager.VERSION_CODE + "]app:[" + Constant.APPName
                + "]\nphone:[" + android.os.Build.BRAND
                + android.os.Build.DEVICE + "]sysversion:["
                + android.os.Build.VERSION.RELEASE + "]";
    }

    /**
     * 验证
     */
    public boolean verification() {
        String contextString = context.getText().toString();
        String emailString = email.getText().toString().trim();

        if (contextString.length() == 0) {
            context.setError(getResources().getString(
                    R.string.feedback_info_null));
            return false;
        }

        if (emailString.length() != 0) {
            if (!emailCheck(emailString)) {
                email.setError(getResources().getString(
                        R.string.feedback_effective_email));
                return false;
            }
        } else {
            if (!AccountManager.Instace(mContext).checkUserLogin() || TouristUtil.isTourist()) {
                email.setError(getResources().getString(
                        R.string.feedback_email_null));
                return false;
            }
        }

        return true;
    }

    /**
     * email格式匹配
     *
     * @param email
     * @return
     */
    public boolean emailCheck(String email) {
        String check = "^([a-z0-ArrayA-Z]+[-_|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

}
