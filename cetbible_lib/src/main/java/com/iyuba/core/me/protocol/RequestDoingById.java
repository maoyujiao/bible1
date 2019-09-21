/**
 *
 */
package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.VOABaseJsonRequest;
import com.iyuba.core.protocol.message.ResponseDoingById;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class RequestDoingById extends VOABaseJsonRequest {
    public static final String protocolCode = "39001";

    public RequestDoingById(String doid) {
        super(protocolCode);

        setRequestParameter("doid", doid);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + doid + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseDoingById();
    }

}
