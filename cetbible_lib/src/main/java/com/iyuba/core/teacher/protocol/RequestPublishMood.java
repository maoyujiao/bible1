/**
 *
 */
package com.iyuba.core.teacher.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ct 显示用户的基本资料信息 protocolCode 20001
 */

public class RequestPublishMood extends VOABaseJsonRequest {
    public static final String protocolCode = "30006";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestPublishMood(String userId, String userName, String message) {
        super(protocolCode);
        setRequestParameter("uid", userId);
        setRequestParameter("username", userName);
        setRequestParameter("from", "android");
        setRequestParameter("message", TextAttr.encode(message));
        setRequestParameter(
                "sign",
                MD5.getMD5ofStr("30006" + userId + userName + message
                        + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponsePublishMood();
    }

}
