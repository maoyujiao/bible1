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
 * @author 相互关注列表
 */
public class ResponseMutualAttentionList extends BaseJSONResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public JSONArray data;
    public ArrayList<FindFriends> list;
    public int num;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        list = new ArrayList<FindFriends>();
        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = jsonObjectRootRoot.getString("result");
            message = jsonObjectRootRoot.getString("message");
            if (result.equals("570")) {
                num = jsonObjectRootRoot.getInt("num");
                data = jsonObjectRootRoot.getJSONArray("data");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    FindFriends item;
                    JSONObject jsonObject;
                    for (int i = 0; i < size; i++) {
                        item = new FindFriends();
                        jsonObject = ((JSONObject) data.opt(i));
                        item.userName = jsonObject.getString("fusername");
                        item.userid = jsonObject.getString("followuid");
                        item.doing = jsonObject.getString("doing");
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
