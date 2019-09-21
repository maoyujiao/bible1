package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AnswerQuesRequest extends BaseJSONRequest {
    private String format = "json";

    public AnswerQuesRequest(String uid, String username, int authorType, int qid, String answer) {

        //用户名转码
        try {
            username = new String(username.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        username = TextAttr.encode(TextAttr.encode(TextAttr.encode(username)));
        setAbsoluteURI("http://www.iyuba.cn/question/answerQuestion.jsp?"
                + "format=" + format
                + "&authorid=" + uid
                + "&username=" + username
                + "&questionid=" + qid
                + "&answer=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(answer)))
                + "&authortype=" + authorType);

        Log.e("iyuba", getAbsoluteURI());
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new AnswerQuesResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

}
