package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class NoticeRequest extends BaseJSONRequest {
    private String format = "json";

    public NoticeRequest(String uid, int isnew, int pageNum) {
        setAbsoluteURI("http://www.iyuba.cn/question/getNotice.jsp?"
                + "format=" + format
                + "&uid=" + uid
                + "&pageNum=" + pageNum + "&isNew=" + isnew
        );

        Log.e("iyuba", "http://www.iyuba.cn/question/getNotice.jsp?"
                + "format=" + format
                + "&uid=" + uid
                + "&pageNum=" + pageNum + "&isNew=" + isnew);
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new NoticeResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

}
