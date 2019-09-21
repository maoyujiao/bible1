package com.iyuba.core.protocol.message;

import android.text.TextUtils;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册协议
 *
 * @author yaoyao
 * @protocolCode 10002
 */
public class RequestPhoneNumRegister extends BaseJSONRequest {
    public static final String protocolCode = "11002";
    public String md5Status = "1"; // 0=未加密,1=加密
    public String emailStatus = "0";

    /**
     * @param wordKey
     */

    public RequestPhoneNumRegister(String userName, String password, String tuid,
                                   String mobile) {

        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?platform=android&app="
                + Constant.AppName
                + "&format=json&protocol=11002"
                + (TextUtils.isEmpty(tuid) ? "" : "&tuid=" + tuid)
                + "&username="
                + TextAttr.encode(userName)
                + "&password="
                + MD5.getMD5ofStr(password)
                + "&sign="
                + MD5.getMD5ofStr(protocolCode + userName
                + MD5.getMD5ofStr(password) + "iyubaV2")
                + "&mobile="
                + mobile);

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ResponsePhoneNumRegister();
    }

}
