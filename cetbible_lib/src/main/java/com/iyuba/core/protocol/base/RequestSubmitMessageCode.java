/**
 *
 */
package com.iyuba.core.protocol.base;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao 验证短信验证码
 */
public class RequestSubmitMessageCode extends BaseJSONRequest {

    public RequestSubmitMessageCode(String userphone) {
        setAbsoluteURI("http://api.iyuba.com.cn/sendMessage3.jsp?format=json"
                + "&userphone=" + userphone);
        Log.e("RequestSubmitMessageCode", "http://api.iyuba.com.cn/sendMessage3.jsp?format=json"
                + "&userphone=" + userphone);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponseSubmitMessageCode();
    }

}
