package com.iyuba.CET4bible.protocol.info;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

public class JpBlogContentRequest extends BaseJSONRequest {

    public JpBlogContentRequest(String blogid) {

        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=200062"
                + "&blogId=" + blogid
                + "&format=json&sign=" + MD5.getMD5ofStr("20006" + blogid + "iyubaV2"));
        Log.d("BlogContentRequest url", getAbsoluteURI());
    }

    @Override
    protected void fillBody(JSONObject jsonObject) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new JpBlogContentResponse();
    }

}

