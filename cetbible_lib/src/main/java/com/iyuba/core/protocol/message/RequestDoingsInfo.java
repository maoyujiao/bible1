/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao 查看用户状态-doings
 */
public class RequestDoingsInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "30001";
    public static final String pageCounts = "10";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestDoingsInfo(String userId, int pageNumber) {
        super(protocolCode);

        setRequestParameter("userId", userId);
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

        return new ResponseDoingsInfo();
    }

}
