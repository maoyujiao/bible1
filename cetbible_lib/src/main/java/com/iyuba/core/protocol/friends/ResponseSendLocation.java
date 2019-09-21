/**
 *
 */
package com.iyuba.core.protocol.friends;

import com.iyuba.core.protocol.VOABaseJsonResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class ResponseSendLocation extends VOABaseJsonResponse {
    public String result;// 返回代码
    public String message;// 返回信息

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
