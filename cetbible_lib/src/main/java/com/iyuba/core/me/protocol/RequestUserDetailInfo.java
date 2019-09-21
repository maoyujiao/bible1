package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author yao 用户的详细资料
 */
public class RequestUserDetailInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "20002";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestUserDetailInfo(String userId) {
        super(protocolCode);

        setRequestParameter("id", userId);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + userId + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseUserDetailInfo();
    }

}
