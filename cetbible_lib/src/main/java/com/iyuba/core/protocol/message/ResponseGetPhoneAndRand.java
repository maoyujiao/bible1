package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseGetPhoneAndRand extends BaseJSONResponse {

    public String result;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);
            result = jsonObjectRoot.getString("result");
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return false;
    }

}
