/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.protocol.news;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class ReadCountAddRequest extends BaseJSONRequest {

    public ReadCountAddRequest(String id) {
        setAbsoluteURI("http://daxue.iyuba.cn/appApi/UnicomApi?protocol=70001&counts=1&format=json&appName=music&voaids="
                + id);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ReadCountAddResponse();
    }

}
