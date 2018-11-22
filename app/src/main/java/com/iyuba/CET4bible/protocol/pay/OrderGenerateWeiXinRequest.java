package com.iyuba.CET4bible.protocol.pay;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.iyuba.abilitytest.network.BaseJsonObjectRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class OrderGenerateWeiXinRequest extends BaseJsonObjectRequest {
    private static final String TAG = OrderGenerateWeiXinRequest.class.getSimpleName();
    private static final String URL = "http://vip.iyuba.com/weixinPay.jsp?";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    public int retcode = -233;
    public String retmsg = "";
    public String mchkey;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String sign;

    public OrderGenerateWeiXinRequest(String wxkey, String appid, String uid, String money,
                                      String amount, ErrorListener el, final RequestCallBack rc) {
        super(Method.POST, buildUrl(wxkey, appid, uid, money, amount), el);
        setResListener(new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "response content : \n" + response.toString());
                try {
                    retcode = response.getInt("retcode");
                    retmsg = response.getString("retmsg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isRequestSuccessful()) {
                    try {
                        timeStamp = response.getString("timestamp");
                        partnerId = response.getString("mch_id");
                        nonceStr = response.getString("noncestr");
                        prepayId = response.getString("prepayid");
                        sign = response.getString("sign");
                        mchkey = response.getString("mch_key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (rc != null) rc.requestResult(OrderGenerateWeiXinRequest.this);
            }
        });
    }

    private static String buildUrl(String wxkey, String appid, String uid, String money,
                                   String amount) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL).append("format=json");
        sb.append("&").append("wxkey=").append(wxkey);
        sb.append("&").append("appid=").append(appid);
        sb.append("&").append("uid=").append(uid);
        sb.append("&").append("money=").append(money);
        sb.append("&").append("amount=").append(amount);
        sb.append("&").append("productid=2");
        sb.append("&").append("sign=").append(generateSign(appid, uid, money, amount));
        Log.e(TAG, sb.toString());
        return sb.toString();
    }

    private static String generateSign(String appid, String uid, String money, String amount) {
        StringBuilder sb = new StringBuilder();
        sb.append(appid).append(uid).append(money).append(amount);
        sb.append(sdf.format(System.currentTimeMillis()));
        return MD5.getMD5ofStr(sb.toString());
    }

    @Override
    public boolean isRequestSuccessful() {
        return retcode == 0;
    }

}
