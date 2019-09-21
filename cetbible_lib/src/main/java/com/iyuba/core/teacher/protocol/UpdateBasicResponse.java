package com.iyuba.core.teacher.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateBasicResponse extends BaseJSONResponse {

    public String result;
    public String message;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
            result = jsonBody.getString("result");
            message = jsonBody.getString("message");
            if (result.equals("1")) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}
