/**
 *
 */
package com.iyuba.core.teacher.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class ReadNoticeResponse extends BaseJSONResponse {

    public String result;// 返回代码
    public String message;// 返回信息

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = jsonObjectRootRoot.getString("result");
            message = jsonObjectRootRoot.getString("message");
            if (result.equals("1")) {
                System.out.println("设置成已读");
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
        // return false;
    }

}
