/**
 *
 */
package com.iyuba.core.protocol.message;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 搜索好友
 */
public class RequestSearchList extends BaseJSONRequest {
    public static final String protocolCode = "52001";

    public RequestSearchList(String uid, String search, int pageNumber) {
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=" + protocolCode
                + "&uid=" + uid
                + "&search=" + TextAttr.encode(search)
                + "&pageNumber=" + pageNumber
                + "&type=0"
                + "&pageCounts=20"
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
        Log.e("RequestSearchList", "http://api.iyuba.com.cn/v2/api.iyuba?protocol=" + protocolCode
                + "&uid=" + uid
                + "&search=" + TextAttr.encode(search)
                + "&pageNumber=" + pageNumber
                + "&type=0"
                + "&pageCounts=20"
                + "&sign=" + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseSearchList();
    }

}
