/**
 *
 */
package com.iyuba.core.discover.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author yao 向服务器发送当前位置
 */
public class RequestSendLocation extends VOABaseJsonRequest {
    public static final String protocolCode = "70001";

    public RequestSendLocation(String uid, String x, String y) {
        super(protocolCode);

        setRequestParameter("uid", uid);
        setRequestParameter("x", x);// 经度
        setRequestParameter("y", y);// 纬度
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + uid + x + y + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseSendLocation();
    }

}
