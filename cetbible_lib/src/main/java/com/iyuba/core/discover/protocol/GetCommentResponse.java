package com.iyuba.core.discover.protocol;

import com.iyuba.core.discover.sqlite.mode.BlogComment;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取评论的Response
 */

public class GetCommentResponse extends BaseJSONResponse {
    public String result;
    public String message;
    public int commentCounts;//日志的评论数
    public String userid;//日志的作者id

    public int pageNumber;
    public int totalPage;
    public int firstPage;
    public int prevPage;
    public int nextPage;
    public int lastPage;


    public List<BlogComment> blogComments;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
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
        if (result.equals("261")) {
            try {
                pageNumber = jsonBody.getInt("PageNumber");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                totalPage = jsonBody.getInt("TotalPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                firstPage = jsonBody.getInt("FirstPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                prevPage = jsonBody.getInt("PrevPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                nextPage = jsonBody.getInt("NextPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                lastPage = jsonBody.getInt("LastPage");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            try {
                commentCounts = jsonBody.getInt("Counts");
            } catch (JSONException e) {

                e.printStackTrace();
            }

            JSONArray jsonArrayData = null;
            try {
                jsonArrayData = jsonBody.getJSONArray("data");
            } catch (JSONException e1) {

                e1.printStackTrace();
            }
            if (jsonArrayData.length() != 0) {
                blogComments = new ArrayList<BlogComment>();
                for (int i = 0; i < jsonArrayData.length(); i++) {
                    JSONObject jsonElement = null;
                    try {
                        jsonElement = jsonArrayData.getJSONObject(i);
                        BlogComment BlogComment = new BlogComment();
                        BlogComment.authorid = jsonElement.getString("authorid");
                        BlogComment.author = jsonElement.getString("author");
                        BlogComment.message = jsonElement.getString("message");
                        BlogComment.dateline = jsonElement.getString("dateline");
                        blogComments.add(BlogComment);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }

        } else if (result.equals("262")) {
            //无评论
        }
        return true;
    }

}
