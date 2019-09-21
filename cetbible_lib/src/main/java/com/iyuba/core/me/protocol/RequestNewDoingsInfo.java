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
 * @author yao 查看用户状态-doings
 */
public class RequestNewDoingsInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "31001";
    public static final String pageCounts = "10";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestNewDoingsInfo(String userId, int pageNumber) {
        super(protocolCode);

        setRequestParameter("uid", userId);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + userId + "iyubaV2"));
        setRequestParameter("pageNumber", String.valueOf(pageNumber));
        setRequestParameter("pageCounts", pageCounts);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseNewDoingsInfo();
    }

}
