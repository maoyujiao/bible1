package com.iyuba.core.protocol.base;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class GradeResponse extends BaseJSONResponse {
    public String totalTime;
    public String positionByTime;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);
            totalTime = jsonObjectRoot.getString("totalTime");
            positionByTime = jsonObjectRoot.getString("positionByTime");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}
