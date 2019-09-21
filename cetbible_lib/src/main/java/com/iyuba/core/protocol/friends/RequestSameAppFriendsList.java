/**
 *
 */
package com.iyuba.core.protocol.friends;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class RequestSameAppFriendsList extends BaseJSONRequest {
    public static final String protocolCode = "90003";

    public RequestSameAppFriendsList(String uid, int pageNumber) {
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&pagesize=20"
                + "&pagenum=" + pageNumber);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseSameAppFriendsList();
    }

}
