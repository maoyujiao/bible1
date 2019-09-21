package com.iyuba.CET4bible.protocol.info;

import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlogResponse extends BaseJSONResponse {
    public String result = "";
    public ArrayList<Blog> blogs = new ArrayList<Blog>();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);
            result = jsonObjectRoot.getString("result");
            if (result.equals("1")) {
                JSONArray JsonArrayData = jsonObjectRoot.getJSONArray("data");
                if (JsonArrayData != null) {
                    int size = JsonArrayData.length();
                    Blog blog;
                    JSONObject jsonObjectData;

                    for (int i = 0; i < size; i++) {
                        jsonObjectData = JsonArrayData.getJSONObject(i);
                        blog = new Blog();
                        blog.id = jsonObjectData.getInt("essayid");
                        blog.title = jsonObjectData.getString("title");
                        try {
                            blog.title_en = jsonObjectData
                                    .getString("title_en");
                        } catch (Exception e) {

                        }
                        blog.desccn = jsonObjectData.getString("abstract");
                        blog.source = jsonObjectData.getString("source");
                        blog.author = jsonObjectData.getString("author");
                        blog.tag = jsonObjectData.getString("tag");

//                        StringBuilder  sb = new StringBuilder("http://cet4.iyuba.com");
//                        sb.append(jsonObjectData.getString("url"));
//                        blog.url = sb.toString();

//                        blog.url = "http://m.iyuba.cn/news.html?id=" + blog.id + "&type=cet" + Constant.APP_CONSTANT.TYPE();

                        blog.url = "http://blog.iyuba.cn/Blog_" + blog.id + ".html";

                        blog.createtime = jsonObjectData.getString("createtime");
                        blog.readcount = jsonObjectData.getString("readcount");
                        blog.essay = jsonObjectData.getString("essayfield");
                        blog.category = jsonObjectData.getString("catid");
                        blogs.add(blog);
                    }
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }
}
