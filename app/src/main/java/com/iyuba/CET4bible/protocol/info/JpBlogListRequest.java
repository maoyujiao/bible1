package com.iyuba.CET4bible.protocol.info;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 四级资讯 id=242141
 *
 * @author yao
 *         http://api.iyuba.com.cn/v2/api.iyuba?platform=android&format=json&
 *         protocol=20006&
 *         id=242141&sign=80f18af1258564617afa4ca32c62d571&pageNumber
 *         =1&pageCounts=20
 */
public class JpBlogListRequest extends BaseJSONRequest {


    public JpBlogListRequest(String id, String pageNum) {

        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=200064" +
                "&sign=" + MD5.getMD5ofStr("20006" + id + "iyubaV2") +
                "&id=" + id +
                "&pageNumber=" + pageNum + "&pageCounts=15");

        Log.e("----- blog ----", getAbsoluteURI());
    }

    @Override
    protected void fillBody(JSONObject jsonObject) {
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new JpBlogListResponse();
    }
}
