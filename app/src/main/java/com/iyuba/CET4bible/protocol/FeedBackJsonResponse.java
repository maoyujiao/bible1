package com.iyuba.CET4bible.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONObject;

public class FeedBackJsonResponse extends BaseJSONResponse {
    public String status;
    public String msg;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        return true;
    }

}
