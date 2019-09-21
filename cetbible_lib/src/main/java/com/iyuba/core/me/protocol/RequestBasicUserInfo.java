/**
 *
 */
package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ct 显示用户的基本资料信息 protocolCode 20001
 */

public class RequestBasicUserInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "20001";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestBasicUserInfo(String userId, String myid) {
        super(protocolCode);
        setRequestParameter("id", userId);
        setRequestParameter("myid", myid);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + userId + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseBasicUserInfo();
    }

}
