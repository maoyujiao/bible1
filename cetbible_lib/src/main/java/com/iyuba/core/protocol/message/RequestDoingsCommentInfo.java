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
 * @author yao
 */
public class RequestDoingsCommentInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "30002";
    public static final String pageCounts = "100";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestDoingsCommentInfo(String id, String pageNumber) {
        super(protocolCode);

        setRequestParameter("doing", id);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + id + "iyubaV2"));
        setRequestParameter("pageNumber", pageNumber);
        setRequestParameter("pageCounts", pageCounts);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseDoingsCommentInfo();
    }

}
