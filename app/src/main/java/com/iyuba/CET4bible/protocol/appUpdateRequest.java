package com.iyuba.CET4bible.protocol;

import android.util.Log;

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
public class appUpdateRequest extends BaseJSONRequest {

    private int version;

    public appUpdateRequest(int version) {
        this.version = version;
        setAbsoluteURI(Constant.appUpdateUrl + this.version);
        Log.e("--", getAbsoluteURI());
    }

    @Override
    protected void fillBody(JSONObject jsonObject) {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new appUpdateResponse();
    }

}
