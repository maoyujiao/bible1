package com.iyuba.core.protocol.base;

import android.text.TextUtils;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户注册
 *
 * @author chentong
 */
public class RegistRequest extends BaseJSONRequest {

    private String userName, email;

    public RegistRequest(String userName,String tuid, String password, String email) {
        this.userName = userName;
        this.email = email;
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=10002&email="
                + this.email
                + "&username="
                + this.userName
                + (TextUtils.isEmpty(tuid) ? "" : "&tuid=" + tuid)
                + "&password="
                + MD5.getMD5ofStr(password)
                + "&platform=android&app="
                + Constant.AppName
                + "&format=xml&sign="
                + MD5.getMD5ofStr("10002" + userName
                + MD5.getMD5ofStr(password) + email + "iyubaV2"));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new RegistResponse();
    }

}
