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
 * @author
 */
public class RequestMessageLetterContentList extends BaseJSONRequest {
    public static final String protocolCode = "60004";

    public RequestMessageLetterContentList(String uid, String friendid, int page) {
        // super(protocolCode);

        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol="
                + protocolCode + "&uid=" + uid + "&friendid=" + friendid
                + "&pageNumber=" + page + "&pageCounts=20&asc=0&sign="
                + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseMessageLetterContentList();
    }

}
