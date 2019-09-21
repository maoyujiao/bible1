package com.iyuba.core.teacher.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.teacher.sqlite.mode.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTeacherInfoResponse extends BaseJSONResponse {

    public String result;
    public String message;
    public Teacher item = new Teacher();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
            result = jsonBody.getString("result");
            if (result.equals("1")) {
                JSONArray data = jsonBody.getJSONArray("data");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    JSONObject jsonObject;
                    if (size > 0) {
                        jsonObject = ((JSONObject) data.opt(0));

                        item.uid = jsonObject.getString("uid");
                        item.username = jsonObject.getString("username");
                        item.imgsrc = jsonObject.getString("imgsrc");
                        item.tid = jsonObject.getString("tid");
                        item.tname = jsonObject.getString("tname");
                        item.timg = jsonObject.getString("timg");
                        item.tsex = jsonObject.getString("tsex");
                        item.tphone = jsonObject.getString("tphone");
                        item.temail = jsonObject.getString("temail");
                        item.tqq = jsonObject.getString("tqq");
                        item.tweixin = jsonObject.getString("tweixin");
                        item.topedu = jsonObject.getString("topedu");
                        item.tidentity = jsonObject.getString("tidentity");
                        item.tonedesc = jsonObject.getString("tonedesc");
                        item.tcity = jsonObject.getString("tcity");
                        item.endegree = jsonObject.getString("endegree");
                        item.jpdegree = jsonObject.getString("jpdegree");
                        item.category1 = jsonObject.getString("category1");
                        item.category2 = jsonObject.getString("category2");
                        item.attachment = jsonObject.getString("attachment");
                        item.tlevel = jsonObject.getString("tlevel");
                    }


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}
