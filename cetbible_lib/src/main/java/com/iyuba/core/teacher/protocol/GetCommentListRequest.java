package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class GetCommentListRequest extends BaseJSONRequest {
    private String format = "json";

    public GetCommentListRequest(int qid) {
        setAbsoluteURI("http://www.iyuba.cn/question/getQuestionDetail.jsp?format=json&authortype=2&questionid="
                + qid
        );
        Log.e("iyuba", "http://www.iyuba.cn/question/getQuestionDetail.jsp?format=json&authortype=2&questionid="
                + qid);
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new GetCommentListResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

}
