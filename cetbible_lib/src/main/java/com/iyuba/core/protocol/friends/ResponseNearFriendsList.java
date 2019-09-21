package com.iyuba.core.protocol.friends;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.sqlite.mode.me.FindFriends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author 附近的人
 */
public class ResponseNearFriendsList extends BaseJSONResponse {
    public String result;// 返回代码
    public String message;// 返回信息
    public JSONArray data;
    public ArrayList<FindFriends> list;
    public Boolean isLastPage = false;
    public int total;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        list = new ArrayList<FindFriends>();
        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = jsonObjectRootRoot.getString("result");
            total = jsonObjectRootRoot.getInt("total");
            if (result.equals("711")) {
                data = jsonObjectRootRoot.getJSONArray("data");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    FindFriends item;
                    JSONObject jsonObjectRoot;
                    for (int i = 0; i < size; i++) {
                        item = new FindFriends();
                        jsonObjectRoot = ((JSONObject) data.opt(i));
                        item.userName = jsonObjectRoot.getString("username");
                        item.userid = jsonObjectRoot.getString("uid");
                        item.distance = jsonObjectRoot.getDouble("distance");
                        item.gender = jsonObjectRoot.getString("gender");
                        list.add(item);
                    }
                }
            } else if (result.equals("392")) {
                isLastPage = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}
