package com.iyuba.core.protocol;

import android.util.Log;

import com.iyuba.configation.Constant;

public abstract class VOABaseJsonRequest extends BaseJSONRequest {
    protected String platform = "android";
    protected String format = "json";
    protected String protocolCode = "";

    public VOABaseJsonRequest(String protocolCode) {
        this.protocolCode = protocolCode;
        setAbsoluteURI(getCurrAbsoluteURL());
    }

    /**
     * 获取当前的路径
     *
     * @return
     */
    private String getCurrAbsoluteURL() {
        StringBuffer absoluteURL = new StringBuffer("");

        if (protocolCode.equals("0")) {
            // 升级vip协议
            absoluteURL.append("http://app.iyuba.cn/pay/payVipApi.jsp")
                    .append("?").append("platform=").append(platform)
                    .append("&").append("format=").append("xml");
        } else if (protocolCode.equals("1")) {
            // 获取短信验证码
            absoluteURL.append("http://m.iyuba.com.cn/sendMessage.jsp")
                    .append("?").append("format=").append("json");
        } else if (protocolCode.equals("2")) {
            // 验证短信验证码是否正确
            absoluteURL.append("http://m.iyuba.com.cn/checkCode.jsp")
                    .append("?").append("format=").append("json");
        } else {
            absoluteURL.append("http://api.iyuba.com.cn/v2/api.iyuba")
                    .append("?").append("platform=").append(platform)
                    .append("&").append("format=").append(format)
                    .append("&appid=").append(Constant.APPID)
                    .append("&")
                    .append("protocol=").append(this.protocolCode);
            Log.e("VoaBaseJsonRequest", "absoluteURL>>>>>" + absoluteURL);
        }
        return absoluteURL.toString();
    }

    /**
     * 设置参数
     *
     * @param key
     * @param value
     */
    protected void setRequestParameter(String key, String value) {
        StringBuffer requestURLTemp = new StringBuffer(getAbsoluteURI());
        requestURLTemp.append("&").append(key).append("=").append(value);
        Log.e("VoaBaseJsonRequest", "requestURLTemp>>>>>" + requestURLTemp);
        setAbsoluteURI(requestURLTemp.toString());
    }

}
