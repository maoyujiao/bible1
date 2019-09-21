/**
 *
 */
package com.iyuba.core.protocol.friends;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao 猜你认识好友列表
 */
public class RequestGuessFriendsList extends VOABaseJsonRequest {
    public static final String protocolCode = "52003";

    public RequestGuessFriendsList(String uid) {
        super(protocolCode);

        setRequestParameter("uid", uid);

        setRequestParameter("count", "20");
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseGuessFriendsList();
    }

}
