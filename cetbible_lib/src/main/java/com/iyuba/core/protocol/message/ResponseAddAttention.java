/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 加关注
 */
public class ResponseAddAttention extends BaseJSONResponse {
    public String result;// 返回代码
    public String message;// 返回信息

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = jsonObjectRootRoot.getString("result");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}
