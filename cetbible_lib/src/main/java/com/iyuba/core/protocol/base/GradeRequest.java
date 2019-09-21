package com.iyuba.core.protocol.base;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取网页单词本
 *
 * @author Administrator
 */
public class GradeRequest extends BaseJSONRequest {

    public GradeRequest(String uid) {
        setAbsoluteURI("http://daxue.iyuba.cn/ecollege/getPaiming.jsp?format=json&uid="
                + uid + "&appName=" + Constant.AppName);
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new GradeResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

}
