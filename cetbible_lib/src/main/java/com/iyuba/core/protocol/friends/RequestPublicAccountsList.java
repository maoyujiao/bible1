/**
 *
 */
package com.iyuba.core.protocol.friends;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class RequestPublicAccountsList extends VOABaseJsonRequest {
    public static final String protocolCode = "10008";

    public RequestPublicAccountsList(String uid, int pageNumber) {
        super(protocolCode);

        setRequestParameter("uid", uid);
        setRequestParameter("pageCounts", "50");
        setRequestParameter("pagenum", String.valueOf(pageNumber));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponsePublicAccountsList();
    }

}
