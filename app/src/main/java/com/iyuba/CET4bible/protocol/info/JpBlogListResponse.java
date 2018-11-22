package com.iyuba.CET4bible.protocol.info;

import com.google.gson.Gson;
import com.iyuba.CET4bible.bean.JpBlogListBean;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONObject;

public class JpBlogListResponse extends BaseJSONResponse {
    public JpBlogListBean bean;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            bean = new Gson().fromJson(bodyElement, JpBlogListBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            bean = null;
            return false;
        }


        return true;
    }
}
