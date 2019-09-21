package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class GetCategoryRequest extends BaseJSONRequest {


    public GetCategoryRequest() {
        setAbsoluteURI("http://www.iyuba.cn/question/getCategoryList.jsp");
        Log.e("iyuba", "http://www.iyuba.cn/question/getCategoryList.jsp");
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new GetCategoryResponse();
    }

}
