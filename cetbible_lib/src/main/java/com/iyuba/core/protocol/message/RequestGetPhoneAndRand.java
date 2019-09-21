package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestGetPhoneAndRand extends BaseJSONRequest {

    public RequestGetPhoneAndRand() {
        setAbsoluteURI("http://api.iyuba.com.cn/getPhoneAndRand.jsp?format=json");
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return null;
    }

}
