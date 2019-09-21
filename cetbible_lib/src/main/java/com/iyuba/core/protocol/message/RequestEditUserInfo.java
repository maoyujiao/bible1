/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao 编辑用户信息
 */
public class RequestEditUserInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "20003";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestEditUserInfo(String userId, String key, String value) {
        super(protocolCode);
        setRequestParameter("id", userId);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + userId + "iyubaV2"));
        setRequestParameter("key", key);
        setRequestParameter("value", TextAttr.encode(TextAttr.encode(value)));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseEditUserInfo();
    }

}
