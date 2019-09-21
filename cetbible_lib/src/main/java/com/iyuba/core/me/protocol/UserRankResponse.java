package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRankResponse extends BaseJSONResponse {
    public String result, totalUser, totalTime, positionByTime, totalTest,
            positionByTest, totalRate, positionByRate, everyDayInfo;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);
            try {
                result = jsonObjectRoot.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                totalUser = jsonObjectRoot.getString("totalUser");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                totalTime = jsonObjectRoot.getString("totalTime");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                positionByTime = jsonObjectRoot.getString("positionByTime");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                totalTest = jsonObjectRoot.getString("totalTest");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                positionByTest = jsonObjectRoot.getString("positionByTest");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                totalRate = jsonObjectRoot.getString("totalRate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                positionByRate = jsonObjectRoot.getString("positionByRate");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                everyDayInfo = jsonObjectRoot.getString("everyDayInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

}
