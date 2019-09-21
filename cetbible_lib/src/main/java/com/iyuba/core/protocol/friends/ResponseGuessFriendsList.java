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
 * @author yao 猜你认识好友列表
 */
public class ResponseGuessFriendsList extends VOABaseJsonResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public JSONArray data;
    public ArrayList<FindFriends> list;
    public Boolean isLastPage = false;

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
        if (result.equals("591")) {
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
                        if (jsonObject.has("uid")) {
                            item.userid = jsonObject.getString("uid");
                        }
                        if (jsonObject.has("vip")) {
                            item.vip = jsonObject.getString("vip");
                        }
                        if (jsonObject.has("doing")) {
                            item.doing = jsonObject.getString("doing");
                        }
                        if (jsonObject.has("username")) {
                            item.userName = jsonObject.getString("username");
                        }
                        if (jsonObject.has("gender")) {
                            item.gender = jsonObject.getString("gender");
                        }
                        list.add(item);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        } else if (result.equals("592")) {
            isLastPage = true;
        }
        return true;
    }
}
