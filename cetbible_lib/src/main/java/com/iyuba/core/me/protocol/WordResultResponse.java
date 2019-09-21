package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class WordResultResponse extends BaseJSONResponse {
    public String result;
    public String wordSum_0;
    public String wordSum_1;
    public String wordSum_2;
    public String wordSum_3;
    public String wordSum_4;
    public String message;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            JSONObject jsonBody = new JSONObject(bodyElement);
            result = jsonBody.getString("result");
            try {
                wordSum_0 = jsonBody.getString("wordSum_0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                wordSum_1 = jsonBody.getString("wordSum_1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                wordSum_2 = jsonBody.getString("wordSum_2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                wordSum_3 = jsonBody.getString("wordSum_3");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                wordSum_4 = jsonBody.getString("wordSum_4");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                message = jsonBody.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Log.e("jsonBody", ""+jsonBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

}
