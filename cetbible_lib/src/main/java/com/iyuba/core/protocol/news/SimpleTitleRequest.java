package com.iyuba.core.protocol.news;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取最新新闻索引
 *
 * @author chentong
 */
public class SimpleTitleRequest extends BaseJSONRequest {
    String format = "json"; // 可选，默认为json格式
    String parentID; // 可选，用于获取分类下的索引

    public SimpleTitleRequest(String url) {
        setAbsoluteURI(url);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new SimpleTitleResponse();
    }

}
