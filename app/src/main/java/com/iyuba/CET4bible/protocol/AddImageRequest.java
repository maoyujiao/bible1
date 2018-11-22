package com.iyuba.CET4bible.protocol;


import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AddImageRequest extends BaseJSONRequest {
    public AddImageRequest(String type) {

        String url = "http://dev.iyuba.com/getScrollPicApi.jsp?type=class." + type;
        setAbsoluteURI(url);
        Log.e("AddImageRequest", url);
    }


    @Override
    protected void fillBody(JSONObject jsonObject) {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new AddImageResponse();
    }
}
