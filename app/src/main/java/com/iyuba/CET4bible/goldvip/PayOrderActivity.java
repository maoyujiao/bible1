package com.iyuba.CET4bible.goldvip;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.view.NoScrollListView;
import com.iyuba.CET4bible.protocol.pay.OrderGenerateRequest;
import com.iyuba.CET4bible.protocol.pay.OrderGenerateWeiXinRequest;
import com.iyuba.CET4bible.protocol.pay.RequestCallBack;
import com.iyuba.configation.Constant;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.setting.SettingConfig;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomToast;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by howard9891 on 2016/10/28.
 */

public class PayOrderActivity extends Activity {
    private static final String TAG = PayOrderActivity.class.getSimpleName();
    private static final String Seller = "iyuba@sina.com";
    private TextView payorder_username;
    private TextView payorder_rmb_amount;
    private NoScrollListView methodList;
    private Toolbar mToolbar;
    private PayMethodAdapter methodAdapter;
    private Button payorder_submit_btn;
    private boolean confirmMutex = true;
    private Context mContext;
    private String price;
    private String subject;
    private String body;
    private String out_trade_no;
    private IWXAPI mWXAPI;
    private String mWeiXinKey;
    private int selectPosition = 0;
    private Button button;
    private String amount;
    private Response.ErrorListener mOrderErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            new AlertDialog.Builder(PayOrderActivity.this)
                    .setTitle("订单提交出现问题!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmMutex = true;
                            dialog.dismiss();
                            PayOrderActivity.this.finish();
                        }
                    })
                    .show();
        }
    };
    private Handler alipayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    confirmMutex = true;
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (SettingConfig.Instance().isAutoLogin()) {
                            //根据用户在注册时候获取到用户名利用sp保存起来.然后读取出来
                            String[] nameAndPwd = AccountManager.Instace(getApplicationContext())
                                    .getUserNameAndPwd();
                            String userName = nameAndPwd[0];
                            String userPwd = nameAndPwd[1];
                            //入去出来之后就获取到了用户的登录名和密码,然后进行登录
                            AccountManager.Instace(getApplicationContext()).login(userName, userPwd, null);
                        }
                        new AlertDialog.Builder(mContext).setTitle("提示")
                                .setMessage("支付成功! 若数据未刷新，请重新登录后查看")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        finish();
                                    }
                                }).show();
                       /* // TODO refresh user's iyubi information!!!
                        new AlertDialog.Builder(PayOrderActivity.this)
                                .setTitle("支付成功")
                                .setMessage("请填写详细的联系地址以便收到所赠礼品！")
                                .setNegativeButton("放弃", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        PayOrderActivity.this.finish();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        String url = "http://www.iyuba.cn/sendbook/indexmm.jsp?uid="
                                                + AccountManager.Instance(mContext).userId + "&platform=android";
                                       *//* Intent intent = WebActivity.buildIntent(PayOrderActivity
                                                .this, url, "赠送礼品");
                                        startActivity(intent);*//*
                                        PayOrderActivity.this.finish();
                                    }
                                })
                                .show();
                   */
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                        // 最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            CustomToast.showToast(PayOrderActivity.this, "支付结果确认中", 1500);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            CustomToast.showToast(PayOrderActivity.this, "您已取消支付", 1500);
                        } else if (TextUtils.equals(resultStatus, "6002")) {
                            CustomToast.showToast(PayOrderActivity.this, "网络连接出错", 1500);
                        } else {
                            // 其他值就可以判断为支付失败，或者系统返回的错误
                            CustomToast.showToast(PayOrderActivity.this, "支付失败", 1500);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_payorder);
        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        subject = intent.getStringExtra("subject");
        amount = intent.getStringExtra("amount");
        body = intent.getStringExtra("body");
        out_trade_no = intent.getStringExtra("out_trade_no");
        mWeiXinKey = mContext.getString(R.string.weixin_key);
        mWXAPI = WXAPIFactory.createWXAPI(this, mWeiXinKey, false);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(subject);
        findView();

        showGoldVipHintDialog();
    }

    private void showGoldVipHintDialog() {
        if (!AccountManager.isGoldVip()) {
            return;
        }
        int status = com.iyuba.configation.ConfigManager.Instance().loadInt("isvip");
        if (com.iyuba.configation.Constant.VIP_STATUS == status) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("VIP提示")
                .setMessage("您已开通黄金会员，若上次开通的为其他应用黄金会员，将无法继续享受对应应用免费微课等黄金vip特权")
                .setPositiveButton("确定", null)
                .show();
    }

    private void findView() {
        //   mToolbar = (Toolbar) findViewById(R.id.payorder_toolbar);
        button = findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        payorder_username = findViewById(R.id.payorder_username_tv);
        payorder_username.setText(AccountManager.Instace(mContext).getUserName());
        payorder_rmb_amount = findViewById(R.id.payorder_rmb_amount_tv);
        payorder_rmb_amount.setText(price + "元");
        methodList = findViewById(R.id.payorder_methods_lv);
        payorder_submit_btn = findViewById(R.id.payorder_submit_btn);
        methodList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                methodAdapter.changeSelectPosition(position);
                methodAdapter.notifyDataSetChanged();
            }
        });
        methodAdapter = new PayMethodAdapter(this);
        methodList.setAdapter(methodAdapter);
        payorder_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccountManager.Instace(mContext).checkUserLogin() || TouristUtil.isTourist()) {
                    TouristUtil.showTouristHint(mContext);
                    finish();
                    return;
                }

                if (confirmMutex) {
                    confirmMutex = false;
                    String newSubject;
                    String newbody;
                    try {
                        newSubject = URLEncoder.encode(subject, "UTF-8");
                        newbody = URLEncoder.encode(body, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        newSubject = "iyubi";
                        newbody = "iyubi";
                    }
                    switch (selectPosition) {
                        case PayMethodAdapter.PayMethod.ALIPAY:
                            payByAlipay(newbody, newSubject);
                            break;
                        case PayMethodAdapter.PayMethod.WEIXIN:
                            Log.e("PayOrderActivity", "weixin");
                            if (mWXAPI.isWXAppInstalled()) {
                                payByWeiXin();
                            } else {
                                confirmMutex = true;
                                Toast.makeText(mContext, "您还未安装微信客户端!", Toast.LENGTH_SHORT).show();
//                                new AlertDialog.Builder(PayOrderActivity.this)
//                                        .setTitle("提示")
//                                        .setMessage("微信未安装无法使用微信支付!")
//                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                confirmMutex = true;
//                                                dialog.cancel();
//                                            }
//                                        })
//                                        .show();
                            }
                            break;
                      /*  case PayMethodAdapter.PayMethod.BANKCARD:
                            payByWeb();
                            break;*/
                        default:
                            payByAlipay(newbody, newSubject);
                            break;
                    }
                }
            }
        });
    }

    private void payByAlipay(String body, String subject) {
        confirmMutex = true;
        RequestCallBack rc = new RequestCallBack() {
            @Override
            public void requestResult(Request result) {
                OrderGenerateRequest request = (OrderGenerateRequest) result;
                if (request.isRequestSuccessful()) {
                    // 完整的符合支付宝参数规范的订单信息
                    final String payInfo = request.orderInfo + "&sign=\"" + request.orderSign
                            + "\"&" + "sign_type=\"RSA\"";

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(PayOrderActivity.this);
                            // 调用支付接口，获取支付结果
                            String result = alipay.pay(payInfo, true);

                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = result;
                            alipayHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    validateOrderFail();
                }
            }
        };
        String productId;
        if (Constant.APP_CONSTANT.isEnglish()) {
            productId = "4".equals(Constant.APP_CONSTANT.TYPE()) ? "2" : "4";
        } else {
            if ("1".equals(Constant.APP_CONSTANT.TYPE())) {
                productId = "11";
            } else if ("2".equals(Constant.APP_CONSTANT.TYPE())) {
                productId = "5";
            } else {
                productId = "6";
            }
        }
        OrderGenerateRequest orderRequest = new OrderGenerateRequest(productId, Seller, out_trade_no,
                subject, price, body, "", Constant.APPID,
                String.valueOf(AccountManager.Instace(mContext).userId), amount,
                mOrderErrorListener, rc);
        CrashApplication.getInstance().getQueue().add(orderRequest);
    }

    private void payByWeiXin() {
        confirmMutex = true;
        RequestCallBack rc = new RequestCallBack() {
            @Override
            public void requestResult(Request result) {
                OrderGenerateWeiXinRequest first = (OrderGenerateWeiXinRequest) result;
                if (first.isRequestSuccessful()) {
                    Log.e(TAG, "OrderGenerateWeiXinRequest success!");
                    PayReq req = new PayReq();
                    req.appId = mWeiXinKey;
                    req.partnerId = first.partnerId;
                    req.prepayId = first.prepayId;
                    req.nonceStr = first.nonceStr;
                    req.timeStamp = first.timeStamp;
                    req.packageValue = "Sign=WXPay";
                    req.sign = buildWeixinSign(req, first.mchkey);
                    mWXAPI.sendReq(req);
                } else {
                    validateOrderFail();
                }
            }
        };
        String uid = String.valueOf(AccountManager.Instace(mContext).userId);
        OrderGenerateWeiXinRequest request = new OrderGenerateWeiXinRequest(mWeiXinKey, Constant.APPID, uid, price, amount, mOrderErrorListener, rc);
        CrashApplication.getInstance().getQueue().add(request);
    }

    /*private void payByWeb() {
        String url = "http://app.iyuba.cn/wap/servlet/paychannellist?";
        url += "out_user=" + AccountManager.Instance(mContext).userId;
        url += "&appid=" + Constant.APPID;
        url += "&amount=" + 0;
        Intent intent = WebActivity.buildIntent(this, url, "订单支付");
        startActivity(intent);
        confirmMutex = true;
        finish();
    }*/

    private String buildWeixinSign(PayReq payReq, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildWeixinStringA(payReq));
        sb.append("&key=").append(key);
        Log.i(TAG, sb.toString());
        return MD5.getMD5ofStr(sb.toString()).toUpperCase();
    }

    private String buildWeixinStringA(PayReq payReq) {
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(payReq.appId);
        sb.append("&noncestr=").append(payReq.nonceStr);
        sb.append("&package=").append(payReq.packageValue);
        sb.append("&partnerid=").append(payReq.partnerId);
        sb.append("&prepayid=").append(payReq.prepayId);
        sb.append("&timestamp=").append(payReq.timeStamp);
        return sb.toString();
    }

    private void validateOrderFail() {
        new AlertDialog.Builder(PayOrderActivity.this)
                .setTitle("订单异常!")
                .setMessage("订单验证失败!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PayOrderActivity.this.finish();
                    }
                })
                .show();
    }

}
