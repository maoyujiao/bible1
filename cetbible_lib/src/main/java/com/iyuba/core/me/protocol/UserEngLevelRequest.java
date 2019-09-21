package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UserEngLevelRequest extends BaseJSONRequest {
    public static final String protocolCode = "200031";

    public UserEngLevelRequest(String uid, String plevel, String preadLevel,
                               String ptalkLevel, String glevel, String gtalkLevel,
                               String greadLevel) {


        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?platform=android&format=json&protocol="
                + protocolCode
                + "&id="
                + uid
                + "&sign="
                + MD5.getMD5ofStr(protocolCode + uid + "iyubaV2")
                + "&key="
                + "ptalklevel,preadlevel,plevel,glevel,gtalklevel,greadlevel"
                + "&value="
                + ptalkLevel
                + ","
                + preadLevel
                + ","
                + URLEncoder.encode(URLEncoder.encode("'" + plevel + "'"))
                + ","
                + glevel + "," + gtalkLevel + "," + greadLevel);

//		Log.e("url",
//				"http://api.iyuba.com.cn/v2/api.iyuba?platform=android&format=json&protocol="
//						+ protocolCode
//						+ "&id="
//						+ uid
//						+ "&sign="
//						+ MD5.getMD5ofStr(protocolCode + uid + "iyubaV2")
//						+ "&key="
//						+ "ptalklevel,preadlevel,plevel,glevel,gtalklevel,greadlevel"
//						+ "&value="
//						+ ptalkLevel
//						+ ","
//						+ preadLevel
//						+ ","
//						+ URLEncoder.encode(URLEncoder.encode("'" + plevel + "'"))
//						+","
//						+glevel+","+gtalkLevel+","+greadLevel);

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new UserEngLevelResponse();
    }

}
