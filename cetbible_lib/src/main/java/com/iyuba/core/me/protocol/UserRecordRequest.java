package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRecordRequest extends BaseJSONRequest {

    public UserRecordRequest(String uid) {


        setAbsoluteURI("http://daxue.iyuba.cn/ecollege/getStudyRecord.jsp?uid="
                + uid);

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new UserRecordResponse();
    }

}
