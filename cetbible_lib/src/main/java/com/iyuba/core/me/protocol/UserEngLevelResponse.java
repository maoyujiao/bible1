package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class UserEngLevelResponse extends BaseJSONResponse {
    public int result;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRoot = new JSONObject(bodyElement);
            result = jsonObjectRoot.getInt("result");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return true;
    }

}
