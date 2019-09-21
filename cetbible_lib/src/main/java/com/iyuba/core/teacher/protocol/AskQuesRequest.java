package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

public class AskQuesRequest extends BaseJSONRequest {
    private String format = "json";

    public AskQuesRequest(String uid, String username, String desc, int type, String askuid) {
        String uri;
        uri = "http://www.iyuba.cn/question/askQuestion.jsp?"
                + "&format=" + format
                + "&uid=" + uid
                + "&username=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(username)))
                + "&question=" + desc
                + "&category1=" + type;

        if (!askuid.equals(""))
            uri = uri + "&tuid=" + askuid;

        Log.e("iyuba", uri);
        setAbsoluteURI(uri);
    }

    public AskQuesRequest(String uid, String username, String desc, int type, int appType, String askuid) {
        String uri = null;
        uri = "http://www.iyuba.cn/question/askQuestion.jsp?"
                + "&format=" + format
                + "&uid=" + uid
                + "&username=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(username)))
                + "&question=" + desc
                + "&category1=" + type
                + "&category2=" + appType;

        if (!askuid.equals(""))
            uri = uri + "&tuid=" + askuid;


        Log.e("iyuba", uri);
        setAbsoluteURI(uri);
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new AskQuesResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

}
