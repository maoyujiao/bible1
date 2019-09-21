/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.me.sqlite.mode.Fans;
import com.iyuba.core.protocol.VOABaseJsonResponse;
import com.iyuba.core.sqlite.mode.me.MessageLetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao 通知
 */
public class ResponseNotificationInfo extends VOABaseJsonResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public String uid;
    public MessageLetter letter = new MessageLetter();
    public JSONArray data;
    public ArrayList<Fans> list;
    public int firstPage;
    public int prevPage;
    public int nextPage;
    public int lastPage;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
        list = new ArrayList<Fans>();
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
        if (result.equals("632")) {

        } else {
            try {
                prevPage = jsonBody.getInt("prevPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                firstPage = jsonBody.getInt("firstPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                nextPage = jsonBody.getInt("nextPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                lastPage = jsonBody.getInt("lastPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                uid = jsonBody.getString("uid");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            if (result.equals("631")) {
                try {
                    data = jsonBody.getJSONArray("data");
                } catch (JSONException e) {

                    e.printStackTrace();
                }
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    Fans item;
                    JSONObject jsonObject;
                    for (int i = 0; i < size; i++) {
                        try {
                            jsonObject = ((JSONObject) data.opt(i));
                            item = new Fans();
                            item.uid = jsonObject.getString("authorid");
                            item.isnew = jsonObject.getString("new");
                            item.doing = jsonObject.getString("note");
                            item.username = jsonObject.getString("author");
                            item.dateline = jsonObject.getString("dateline");
                            list.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return true;
    }

}
