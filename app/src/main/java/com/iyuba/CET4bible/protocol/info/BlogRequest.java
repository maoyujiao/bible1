package com.iyuba.CET4bible.protocol.info;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户登录
 *
 * @author chentong
 */
public class BlogRequest extends BaseJSONRequest {

    public BlogRequest(String cate, int page) {
        setAbsoluteURI("http://cms.iyuba.cn/cmsApi/getEssay.jsp?format=json&pageCounts=15&catids="
                + cate + "&pageNumber=" + page);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new BlogResponse();
    }

}
