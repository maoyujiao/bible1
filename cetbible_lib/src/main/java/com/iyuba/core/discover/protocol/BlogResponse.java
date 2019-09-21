/**
 *
 */
package com.iyuba.core.discover.protocol;

import com.iyuba.core.discover.sqlite.mode.BlogContent;
import com.iyuba.core.discover.sqlite.mode.BlogInfo;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author lmy
 */
public class BlogResponse extends BaseJSONResponse {

    public String result;// 返回代码
    public String message;// 返回信息
    public BlogInfo blogInfo = new BlogInfo();
    public JSONArray data;
    public BlogContent blogContent;


    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }

        blogContent = new BlogContent();
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

        if (result.equals("251")) {
            try {
                blogContent.replynum = jsonBody.getString("replynum");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.viewnum = jsonBody.getString("viewnum");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.blogid = jsonBody.getString("blogid");
            } catch (JSONException e2) {

                e2.printStackTrace();
            }
            try {
                blogContent.dateline = jsonBody.getString("dateline");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.favtimes = jsonBody.getString("favtimes");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.friend = jsonBody.getString("friend");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.ids = jsonBody.getString("ids");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.message = jsonBody.getString("message");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.noreply = jsonBody.getString("noreply");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.password = jsonBody.getString("password");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.sharetimes = jsonBody.getString("sharetimes");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.subject = jsonBody.getString("subject");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.uid = jsonBody.getString("uid");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.mp3flag = jsonBody.getString("mp3flag");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.mp3path = jsonBody.getString("mp3path");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            try {
                blogContent.fromid = jsonBody.getString("fromid");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
        }
        return true;
    }


}

