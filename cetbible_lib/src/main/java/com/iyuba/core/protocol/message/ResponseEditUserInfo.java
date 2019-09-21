/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.VOABaseJsonResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class ResponseEditUserInfo extends VOABaseJsonResponse {

    public String message;// 返回信息
    public String result;// 返回代码

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
        try {
            result = jsonBody.getString("result");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            message = jsonBody.getString("message");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}
