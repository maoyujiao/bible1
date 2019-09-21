/**
 *
 */
package com.iyuba.core.protocol.friends;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.sqlite.mode.me.FindFriends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 */
public class ResponseSameAppFriendsList extends BaseJSONResponse {
    public String result;// 返回代码
    public String message;// 返回信息
    public JSONArray data;
    public ArrayList<FindFriends> list;
    public int friendCounts;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        list = new ArrayList<FindFriends>();
        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = jsonObjectRootRoot.getString("result");
            if (jsonObjectRootRoot.has("friendCounts")) {
                friendCounts = jsonObjectRootRoot.getInt("friendCounts");
            }
            if (result.equals("261")) {
                data = jsonObjectRootRoot.getJSONArray("data");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    FindFriends item;
                    JSONObject jsonObject;
                    for (int i = 0; i < size; i++) {
                        item = new FindFriends();
                        jsonObject = ((JSONObject) data.opt(i));
                        item.userid = jsonObject.getString("uid");
                        item.userName = jsonObject.getString("username");
                        item.appName = jsonObject.getString("appname");
                        item.gender = jsonObject.getString("gender");
                        list.add(item);
                    }
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }
}
