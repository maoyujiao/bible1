/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.me.sqlite.mode.DoingsCommentInfo;
import com.iyuba.core.protocol.VOABaseJsonResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author yao
 */
public class ResponseDoingsCommentInfo extends VOABaseJsonResponse {
    public String result;// 返回代码
    public String message;// 返回信息
    public String doid;// 心情状态id
    public String counts;// 当前页总共的评论数
    public JSONArray data;
    public ArrayList<DoingsCommentInfo> doingsCommentlist;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
        doingsCommentlist = new ArrayList<DoingsCommentInfo>();
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
        if (result.equals("311")) {
            try {
                counts = jsonBody.getString("counts");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                doid = jsonBody.getString("doid");
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
                DoingsCommentInfo doingsCommentInfo;
                JSONObject jsonObject;
                for (int i = 0; i < size; i++) {
                    try {
                        doingsCommentInfo = new DoingsCommentInfo();
                        jsonObject = ((JSONObject) data.opt(i));
                        doingsCommentInfo.message = jsonObject
                                .getString("message");
                        doingsCommentInfo.dateline = jsonObject
                                .getString("dateline");
                        doingsCommentInfo.grade = jsonObject.getString("grade");
                        doingsCommentInfo.ip = jsonObject.getString("ip");
                        doingsCommentInfo.id = jsonObject.getString("id");
                        doingsCommentInfo.uid = jsonObject.getString("uid");
                        doingsCommentInfo.upid = jsonObject.getString("upid");
                        doingsCommentInfo.username = jsonObject
                                .getString("username");
                        doingsCommentlist.add(doingsCommentInfo);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

            }

        }
        return true;
    }
}
