package com.iyuba.core.teacher.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class AgreeAgainstResponse extends BaseJSONResponse {

    public String result;
    public String message;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRoot = new JSONObject(bodyElement);
            result = jsonObjectRoot.getString("result");
            message = jsonObjectRoot.getString("message");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }

        return true;
    }

}
