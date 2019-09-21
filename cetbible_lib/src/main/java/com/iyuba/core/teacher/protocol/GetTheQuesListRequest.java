package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.configation.ConfigManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class GetTheQuesListRequest extends BaseJSONRequest {
    private String format = "json";

    public GetTheQuesListRequest(String uid, String type, int pageNum) {
        int qtype1 = ConfigManager.Instance().loadInt("qtype1");
        int qtype2 = ConfigManager.Instance().loadInt("qtype2");
        qtype2 = qtype2 - 1;
        setAbsoluteURI("http://www.iyuba.cn/question/getQuestionList.jsp?format=json&type=" + type + "&category1=" + qtype1 + "&pageNum="
                + pageNum + "&uid=" + uid
        );

        Log.e("iyuba", "http://www.iyuba.cn/question/getQuestionList.jsp?format=json&type=" + type + "&category1=" + qtype1 + "&pageNum="
                + pageNum + "&uid=" + uid);
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new GetQuesListResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

}
