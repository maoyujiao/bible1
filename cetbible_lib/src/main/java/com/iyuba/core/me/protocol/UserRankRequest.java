package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRankRequest extends BaseJSONRequest {

    public UserRankRequest(String uid) {
        setAbsoluteURI("http://daxue.iyuba.cn/ecollege/getPaiming.jsp?format=json&uid="
                + uid);

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new UserRankResponse();
    }

}
