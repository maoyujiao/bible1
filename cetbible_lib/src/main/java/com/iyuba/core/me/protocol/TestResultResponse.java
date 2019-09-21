package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class TestResultResponse extends BaseJSONResponse {
    public String result;
    public String testSum_0;
    public String testSum_1;
    public String testSum_2;
    public String testSum_3;
    public String testSum_4;
    public String score_0;
    public String score_1;
    public String score_2;
    public String score_3;
    public String score_4;
    public String message;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonBody = new JSONObject(bodyElement);
            result = jsonBody.getString("result");
            testSum_0 = jsonBody.getString("testSum_0");
            score_0 = jsonBody.getString("score_0");
            testSum_1 = jsonBody.getString("testSum_1");
            score_1 = jsonBody.getString("score_1");
            testSum_2 = jsonBody.getString("testSum_2");
            score_2 = jsonBody.getString("score_2");
            testSum_3 = jsonBody.getString("testSum_3");
            score_3 = jsonBody.getString("score_3");
            testSum_4 = jsonBody.getString("testSum_4");
            score_4 = jsonBody.getString("score_4");
            message = jsonBody.getString("message");
            // Log.e("jsonBody", ""+jsonBody);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}
