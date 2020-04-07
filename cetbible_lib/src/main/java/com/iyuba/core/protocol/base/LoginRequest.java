package com.iyuba.core.protocol.base;

import android.util.Log;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

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
        String url = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=11001&format=xml&username="
                + this.userName
                + "&password="
                + MD5.getMD5ofStr(password)
                + "&appid="
                + Constant.APPID
                + "&app="
                + Constant.APPName
                + "&sign="
                + MD5.getMD5ofStr("11001" + userName
                + MD5.getMD5ofStr(this.password) + "iyubaV2");
//        Log.d("测试", "LoginRequest: 临时账户" + SettingConfig.Instance().getIstour());
        String[] split = url.split("&");
        String[] split1 = split[2].split("=");
        //ConfigManager.Instance().loadString("userId").length()!=0||
			/*String[] split = url.split("&");
			String[] split1 = split[2].split("=");*/
        StringBuilder sb = new StringBuilder();
        sb.append(split[0] + "&");
        sb.append(split[1] + "&");
        //用户名前端
        sb.append(split1[0] + "=");
        //用户名需要编码
        try {
            sb.append(URLEncoder.encode(split1[1], "utf-8") + "&");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append(split[3] + "&");
        sb.append(split[4] + "&");
        sb.append(split[5] + "&");
        sb.append(split[6]);
        String Eurl = sb.toString();
        setAbsoluteURI(Eurl);
        Log.d("测试", "LoginRequest:Eurl " + Eurl);

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
