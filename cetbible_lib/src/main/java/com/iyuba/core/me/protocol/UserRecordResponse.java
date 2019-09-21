package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRecordResponse extends BaseJSONResponse {
    public String result;
    public String wordSum_0;
    public String wordSum_1;
    public String wordSum_2;
    public String wordSum_3;
    public String wordSum_4;
    public String sentenceSum_0;
    public String sentenceSum_1;
    public String sentenceSum_2;
    public String sentenceSum_3;
    public String sentenceSum_4;
    public String articleSum_0;
    public String articleSum_1;
    public String articleSum_2;
    public String articleSum_3;
    public String articleSum_4;
    public String studyTime_0;
    public String studyTime_1;
    public String studyTime_2;
    public String studyTime_3;
    public String studyTime_4;
    public String message;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonBody = new JSONObject(bodyElement);
            result = jsonBody.getString("result");
            wordSum_0 = jsonBody.getString("wordSum_0");
            sentenceSum_0 = jsonBody.getString("sentenceSum_0");
            articleSum_0 = jsonBody.getString("articleSum_0");
            studyTime_0 = jsonBody.getString("studyTime_0");
            wordSum_1 = jsonBody.getString("wordSum_1");
            sentenceSum_1 = jsonBody.getString("sentenceSum_1");
            articleSum_1 = jsonBody.getString("articleSum_1");
            studyTime_1 = jsonBody.getString("studyTime_1");
            wordSum_2 = jsonBody.getString("wordSum_2");
            sentenceSum_2 = jsonBody.getString("sentenceSum_2");
            articleSum_2 = jsonBody.getString("articleSum_2");
            studyTime_2 = jsonBody.getString("studyTime_2");
            wordSum_3 = jsonBody.getString("wordSum_3");
            sentenceSum_3 = jsonBody.getString("sentenceSum_3");
            articleSum_3 = jsonBody.getString("articleSum_3");
            studyTime_3 = jsonBody.getString("studyTime_3");
            wordSum_4 = jsonBody.getString("wordSum_4");
            sentenceSum_4 = jsonBody.getString("sentenceSum_4");
            articleSum_4 = jsonBody.getString("articleSum_4");
            studyTime_4 = jsonBody.getString("studyTime_4");
            message = jsonBody.getString("message");
            // Log.e("jsonBody", ""+jsonBody);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}
