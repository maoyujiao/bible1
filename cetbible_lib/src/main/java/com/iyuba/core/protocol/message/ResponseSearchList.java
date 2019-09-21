/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.sqlite.mode.me.FindFriends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author
 */
public class ResponseSearchList extends BaseJSONResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public JSONArray data;
    public ArrayList<FindFriends> list;
    public int firstPage;
    public int prevPage;
    public int nextPage;
    public int lastPage;
    public int total;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        list = new ArrayList<FindFriends>();
        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = jsonObjectRootRoot.getString("result");
            if (result.equals("591")) {
                // JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
                // result = jsonObjectRootRoot.getString("result");
                total = jsonObjectRootRoot.getInt("total");
                prevPage = jsonObjectRootRoot.getInt("prevPage");
                nextPage = jsonObjectRootRoot.getInt("nextPage");
                lastPage = jsonObjectRootRoot.getInt("lastPage");
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
                        list.add(item);
                    }
                }
            } else if (result.endsWith("590")) {
                return false;

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }
}
