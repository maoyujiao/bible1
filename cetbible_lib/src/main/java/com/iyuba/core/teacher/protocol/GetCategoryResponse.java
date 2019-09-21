package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.manager.QuestionManager;
import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.teacher.sqlite.mode.AnswerInfo;
import com.iyuba.core.teacher.sqlite.mode.Chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetCategoryResponse extends BaseJSONResponse {

    public String result;
    public String total;
    public String message;
    public List<AnswerInfo> infoList = new ArrayList<AnswerInfo>();
    public List<List<Chat>> chatList = new ArrayList<List<Chat>>();
    HashMap<String, String> cat1 = new HashMap<String, String>();
    HashMap<String, String> cat2 = new HashMap<String, String>();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
            Log.e("GetChatListResponse", jsonBody.toString());
            result = jsonBody.getString("result");
            total = jsonBody.getString("total");
            if (result.equals("1")) {
                JSONArray data = jsonBody.getJSONArray("cat1");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    JSONObject jsonObject = null;
                    for (int i = 0; i < size; i++) {
                        jsonObject = ((JSONObject) data.opt(i));
                        String key = jsonObject.getString("");
                        String value = jsonObject.getString("");
                        cat1.put(key, value);
                    }
                }

                data = jsonBody.getJSONArray("cat2");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    JSONObject jsonObject = null;
                    for (int i = 0; i < size; i++) {
                        jsonObject = ((JSONObject) data.opt(i));
                        String key = jsonObject.getString("");
                        String value = jsonObject.getString("");
                        cat2.put(key, value);
                    }
                }
                QuestionManager.getInstance().cat1 = cat1;
                QuestionManager.getInstance().cat2 = cat2;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}
