/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.VOABaseJsonResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao 评论心情
 */
public class ResponseDoingSendComments extends VOABaseJsonResponse {

    public String result;
    public String message;

    public boolean isSendSuccess = false;

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
        isSendSuccess = result.equals("361");
        return true;
    }

}
