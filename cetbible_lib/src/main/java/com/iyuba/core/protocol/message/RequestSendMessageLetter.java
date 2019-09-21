/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * @author 发私信
 */
public class RequestSendMessageLetter extends BaseJSONRequest {
    public static final String protocolCode = "60002";

    public RequestSendMessageLetter(String uid, String username, String context) {
        // super(protocolCode);

        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&username="
                + URLEncoder.encode(username) + "&context="
                + URLEncoder.encode(context) + "&sign="
                + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseSendMessageLetter();
    }

}
