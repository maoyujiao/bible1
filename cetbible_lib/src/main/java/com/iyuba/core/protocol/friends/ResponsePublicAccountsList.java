/**
 *
 */
package com.iyuba.core.protocol.friends;

import com.iyuba.core.protocol.VOABaseJsonResponse;
import com.iyuba.core.sqlite.mode.me.FindFriends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 */
public class ResponsePublicAccountsList extends VOABaseJsonResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public JSONArray data;
    public ArrayList<FindFriends> list;
    public String pageNumber;
    public String totalPage;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
        list = new ArrayList<FindFriends>();
        try {
            result = jsonBody.getString("result");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            pageNumber = jsonBody.getString("pageNumber");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            totalPage = jsonBody.getString("totalPage");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        if (result.equals("141")) {
            try {
                data = jsonBody.getJSONArray("data");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            if (data != null && data.length() != 0) {
                int size = data.length();
                FindFriends item;
                JSONObject jsonObject;
                for (int i = 0; i < size; i++) {
                    try {
                        item = new FindFriends();
                        jsonObject = ((JSONObject) data.opt(i));
                        item.userid = jsonObject.getString("uid");
                        item.userName = jsonObject.getString("username");
                        item.doing = jsonObject.getString("ps");
                        item.followers = jsonObject.getString("followers");
                        item.gender = jsonObject.getString("gender");
                        list.add(item);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }
}
