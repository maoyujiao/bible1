package com.iyuba.CET4bible.protocol;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户登录
 *
 * @author chentong
 */
public class AdRequest extends BaseJSONRequest {

    public AdRequest() {
//		setAbsoluteURI("http://app.iyuba.com/dev/getAdEntryAll.jsp?appId=201&flag=1");
        setAbsoluteURI("http://dev.iyuba.com/getAdEntryAll.jsp?appId=" + Constant.APPID + "&flag=1");
    }

    @Override
    protected void fillBody(JSONObject jsonObject) {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new AdResponse();
    }

}
