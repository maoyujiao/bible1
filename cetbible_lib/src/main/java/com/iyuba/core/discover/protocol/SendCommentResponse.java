package com.iyuba.core.discover.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取评论的Response
 */

public class SendCommentResponse extends BaseJSONResponse {
    public String result;
    public String message;

    public boolean isSendSuccess = false;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
        try {
            result = jsonBody.getString("result");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            message = jsonBody.getString("message");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        isSendSuccess = result.equals("341");
        return true;
    }


}
