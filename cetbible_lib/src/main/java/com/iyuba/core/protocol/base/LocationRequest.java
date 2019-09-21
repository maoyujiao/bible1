package com.iyuba.core.protocol.base;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户登录
 *
 * @author chentong
 */
public class LocationRequest extends BaseJSONRequest {

    public LocationRequest(String latitude, String longitude) {
        setAbsoluteURI("http://maps.google.cn/maps/api/geocode/json?latlng="
                + latitude + "," + longitude + "&sensor=true&language=zh-CN");
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new LocationResponse();
    }

}
