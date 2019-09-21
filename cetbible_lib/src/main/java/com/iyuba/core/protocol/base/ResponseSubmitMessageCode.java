/**
 *
 */
package com.iyuba.core.protocol.base;

import android.util.Log;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao result: 验证的结果，1为验证成功，0为验证失败。 checkResultCode:
 *         验证结果分类，1为验证成功，0为验证码错误，-1为验证码已过期 message: 返回验证结果提示信息
 */
public class ResponseSubmitMessageCode extends BaseJSONResponse {

    public String result;
    public int res_code;
    public String userphone;
    public String identifier;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRoot = new JSONObject(bodyElement);
            result = jsonObjectRoot.getString("result");
            if (result.equals("1")) {
                res_code = jsonObjectRoot.getInt("res_code");
                userphone = jsonObjectRoot.getString("userphone");
                identifier = jsonObjectRoot.getString("identifier");

                Log.e("res_code", res_code + "");
                Log.e("userphone", userphone + "");
                Log.e("identifier", identifier + "");
            } else {
                res_code = -1;
                userphone = "";
                identifier = "";
            }
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        return true;
    }

}
