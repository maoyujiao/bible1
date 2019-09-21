/**
 *
 */
package com.iyuba.core.discover.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author 取消关注 50002
 */
public class RequestCancelAttention extends BaseJSONRequest {
    public static final String protocolCode = "50002";

    public RequestCancelAttention(String uid, String followid) {
        // super(protocolCode);

        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&followid=" + followid
                + "&sign="
                + MD5.getMD5ofStr(protocolCode + uid + followid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseCancelAttention();
    }

}
