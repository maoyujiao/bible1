package com.iyuba.CET4bible.protocol.info;

import android.util.Log;

import com.iyuba.CET4bible.bean.JpBlogContentBean;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONObject;

public class JpBlogContentResponse extends BaseJSONResponse {

    public JpBlogContentBean bean;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            String result = bodyElement.charAt(10) + "";
            if (result.equals("1")) {
                Log.d("获取内容成功", "成功");
            } else {
                Log.d("获取内容成功", "失败");
                return false;
            }

            String response = bodyElement.trim();


            JSONObject jsonObjectRootRoot = new JSONObject(response.substring(
                    response.indexOf("{", 2), response.lastIndexOf("}") + 1));
            //JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);

            bean = new JpBlogContentBean();

            String message = jsonObjectRootRoot.getString("message");
            message = message.replaceAll("'", "''");

            int blogid = jsonObjectRootRoot.getInt("blogid");
            String viewnum = jsonObjectRootRoot.getString("viewnum");
            String subject = jsonObjectRootRoot.getString("subject");
            String replynum = jsonObjectRootRoot.getString("replynum");

            bean.setMessage(message);
            bean.setBlogid(blogid);
            bean.setViewnum(viewnum);
            bean.setReplynum(replynum);
            bean.setSubject(subject);

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }
}
