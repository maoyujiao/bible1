/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.me.sqlite.mode.Fans;
import com.iyuba.core.protocol.VOABaseJsonResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao 粉丝列表
 */
public class ResponseFansList extends VOABaseJsonResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public Fans fan = new Fans();
    public JSONArray data;
    public ArrayList<Fans> fansList;
    public int num;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
        fansList = new ArrayList<Fans>();
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
        if (result.equals("560")) {
            try {
                num = jsonBody.getInt("num");

            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                data = jsonBody.getJSONArray("data");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            if (data != null && data.length() != 0) {
                int size = data.length();
                Fans fans;
                JSONObject jsonObject;
                for (int i = 0; i < size; i++) {
                    try {
                        fans = new Fans();
                        jsonObject = ((JSONObject) data.opt(i));
                        fans.mutual = jsonObject.getString("mutual");
                        fans.uid = jsonObject.getString("uid");
                        fans.dateline = jsonObject.getString("dateline");
                        fans.username = jsonObject.getString("username");
                        fans.doing = jsonObject.getString("doing");
                        fansList.add(fans);
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                }

            }

        }
        return true;
    }

}
