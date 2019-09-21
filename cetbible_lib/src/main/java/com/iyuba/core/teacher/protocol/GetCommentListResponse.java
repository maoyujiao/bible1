package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.teacher.sqlite.mode.AnswerInfo;
import com.iyuba.core.teacher.sqlite.mode.Chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetCommentListResponse extends BaseJSONResponse {

    public String result;
    public String total;
    public String message;
    public List<AnswerInfo> infoList = new ArrayList<AnswerInfo>();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
            Log.e("GetChatListResponse", jsonBody.toString());
            result = jsonBody.getString("result");
            total = jsonBody.getString("total");
            if (result.equals("1")) {
                JSONArray data = jsonBody.getJSONArray("answers");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    AnswerInfo item = null;

                    JSONObject jsonObject = null;
                    for (int i = 0; i < size; i++) {
                        List<Chat> list = new ArrayList<Chat>();
                        item = new AnswerInfo();
                        jsonObject = ((JSONObject) data.opt(i));
                        item.answerid = jsonObject.getInt("answerid");
                        item.qid = jsonObject.getInt("questionid");
                        item.uid = jsonObject.getInt("authorid");
                        item.username = jsonObject.getString("username");
                        item.userimg = jsonObject.getString("imgsrc");
                        item.time = jsonObject.getString("answertime");
                        item.answer = jsonObject.getString("answer");
                        item.agreeCount = jsonObject.getInt("agreecount");
                        item.audio = jsonObject.getString("audio");
                        item.type = jsonObject.getInt("type");
                        infoList.add(item);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("infoList", infoList.size() + "#####");

        return true;
    }

}
