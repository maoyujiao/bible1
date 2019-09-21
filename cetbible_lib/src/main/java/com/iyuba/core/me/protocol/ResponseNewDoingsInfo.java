/**
 *
 */
package com.iyuba.core.me.protocol;

import com.iyuba.core.me.sqlite.mode.NewDoingsInfo;
import com.iyuba.core.protocol.VOABaseJsonResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author yao
 */
public class ResponseNewDoingsInfo extends VOABaseJsonResponse {
    public String result;// 返回代码
    public String message;// 返回信息
    //	public String uid;// 用户id
//	public String username;// 用户名
    public String counts;// 当前页总共的评论数
    public JSONArray data;
    public ArrayList<NewDoingsInfo> newDoingslist = new ArrayList<NewDoingsInfo>();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }

        // blogContent=new BlogContent();
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
        if (result.equals("391")) {
            try {
                System.out.println("blogInfo.blogCounts"
                        + jsonBody.getString("counts"));
                counts = jsonBody.getString("counts");

            } catch (JSONException e) {

                e.printStackTrace();
            }
//			try {
//				uid = jsonBody.getString("uid");
//			} catch (JSONException e) {
//
//				e.printStackTrace();
//			}
//			try {
//				username = jsonBody.getString("username");
//			} catch (JSONException e) {
//
//				e.printStackTrace();
//			}
            try {
                data = jsonBody.getJSONArray("data");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            if (data != null && data.length() != 0) {
                int size = data.length();
                NewDoingsInfo doingsInfo;
                JSONObject jsonObject;
                for (int i = 0; i < size; i++) {
                    try {
                        doingsInfo = new NewDoingsInfo();
                        jsonObject = ((JSONObject) data.opt(i));

                        doingsInfo.id = jsonObject.getString("id");
                        doingsInfo.uid = jsonObject.getString("uid");
                        doingsInfo.body = jsonObject.getString("body");
                        doingsInfo.feedid = jsonObject.getString("feedid");
                        doingsInfo.title = jsonObject.getString("title");
                        doingsInfo.username = jsonObject.getString("username");
//						doingsInfo.audio =  jsonObject.getString("audio");
                        doingsInfo.idtype = jsonObject.getString("idtype");
                        doingsInfo.replynum = jsonObject.getString("replynum");
                        doingsInfo.image = jsonObject.getString("image");
                        doingsInfo.hot = jsonObject.getString("hot");
                        doingsInfo.dateline = jsonObject.getString("dateline");

                        newDoingslist.add(doingsInfo);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }
}
