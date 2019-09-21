/**
 *
 */
package com.iyuba.core.protocol.friends;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 请求周边的人
 */
public class RequestNearFriendsList extends BaseJSONRequest {
    public static final String protocolCode = "70002";

    public RequestNearFriendsList(String uid, int pageNumber, String x, String y) {
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&pageCounts=" + 50
                + "&pageNumber=" + pageNumber + "&x=" + x + "&y=" + y
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
        Log.e("RequestNearFriendsList", "http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&pageCounts=" + 50
                + "&pageNumber=" + pageNumber + "&x=" + x + "&y=" + y
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseNearFriendsList();
    }

}
