package com.iyuba.core.protocol.base;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户登录
 *
 * @author chentong
 */
public class LoginRequest extends BaseJSONRequest {
    private String userName, password;

    public LoginRequest(String userName, String password, String latitude,
                        String longitude) {
        this.userName = userName;
        this.password = password;
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=11001&username="
                + this.userName
                + "&password="
                + MD5.getMD5ofStr(password)
                + "&x="
                + longitude
                + "&y="
                + latitude
                + "&appid="
                + Constant.APPID
                + "&sign="
                + MD5.getMD5ofStr("11001" + userName
                + MD5.getMD5ofStr(this.password) + "iyubaV2")
                + "&format=xml");
//		String a = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=11001&username="
//				+ this.userName
//				+ "&password="
//				+ MD5.getMD5ofStr(password)
//				+ "&x="
//				+ longitude
//				+ "&y="
//				+ latitude
//				+ "&appid="
//				+ Constant.APPID
//				+ "&sign="
//				+ MD5.getMD5ofStr("11001" + userName
//				+ MD5.getMD5ofStr(this.password) + "iyubaV2")
//				+ "&format=xml";
//		System.out.println(a);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new LoginResponse();
    }

}
