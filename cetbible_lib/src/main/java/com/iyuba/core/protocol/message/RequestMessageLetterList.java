/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao 私信列表
 */
public class RequestMessageLetterList extends BaseJSONRequest {
    public static final String protocolCode = "60001";

    public RequestMessageLetterList(String uid, int page) {
        // super(protocolCode);

        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&asc=" + 0 + "&pageNumber="
                + page + "&pageCounts=" + 20 + "&sign="
                + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseMessageLetterList();
    }

}
