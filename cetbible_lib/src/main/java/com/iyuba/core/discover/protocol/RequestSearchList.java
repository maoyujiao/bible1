/**
 *
 */
package com.iyuba.core.discover.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;


/**
 * @author 搜索好友
 */
public class RequestSearchList extends BaseJSONRequest {
    public static final String protocolCode = "52001";

    public RequestSearchList(String uid, String search, String type,
                             String pageNumber) {
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&search="
                + URLEncoder.encode(search) + "&pageNumber=" + pageNumber
                + "&type=" + type + "&pageNumber=" + pageNumber + "&sign="
                + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
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
