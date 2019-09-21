package com.iyuba.core.protocol;

import com.iyuba.configation.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户登录
 *
 * @author chentong
 */
public class appUpdateRequest extends BaseJSONRequest {

    private int version;

    public appUpdateRequest(int version) {
        this.version = version;
        setAbsoluteURI(Constant.appUpdateUrl + this.version);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new appUpdateResponse();
    }

}
