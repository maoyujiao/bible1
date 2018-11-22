package com.iyuba.CET4bible.protocol;

import android.util.Log;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 反馈请求
 *
 * @author chentong
 */
public class FeedBackJsonRequest extends BaseJSONRequest {

    public FeedBackJsonRequest(String content, String email, String uid) {
        setAbsoluteURI(Constant.feedBackUrl + uid + "&content=" + content
                + "&email=" + email);


        Log.e("-----", getAbsoluteURI());
    }

    @Override
    protected void fillBody(JSONObject jsonObject) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new FeedBackJsonResponse();
    }

}
