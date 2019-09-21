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
 * @author yao 请求粉丝列表 51002
 */
public class RequestFansList extends VOABaseJsonRequest {
    public static final String protocolCode = "51002";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestFansList(String uid, String page) {
        super(protocolCode);
        setRequestParameter("uid", uid);
        setRequestParameter("pageCounts", "20");
        setRequestParameter("pageNumber", page);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseFansList();
    }

}
