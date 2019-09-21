package com.iyuba.core.teacher.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.teacher.sqlite.mode.IyuTeacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetTeacherListResponse extends BaseJSONResponse {

    public String result;
    public String message;
    public String total;


    public List<IyuTeacher> list = new ArrayList<IyuTeacher>();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
            result = jsonBody.getString("result");
            total = jsonBody.getString("total");
            if (result.equals("1")) {
                JSONArray data = jsonBody.getJSONArray("data");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    IyuTeacher item;
                    JSONObject jsonObject;
                    for (int i = 0; i < size; i++) {
                        try {
                            item = new IyuTeacher();
                            jsonObject = ((JSONObject) data.opt(i));
                            item.uid = jsonObject.getString("uid");
                            item.username = jsonObject.getString("username");
                            item.imgsrc = jsonObject.getString("imgsrc");
                            item.tid = jsonObject.getString("tid");
                            item.tname = jsonObject.getString("tname");
                            item.timg = jsonObject.getString("timg");
                            item.tonedesc = jsonObject.getString("tonedesc");
                            item.tcity = jsonObject.getString("tcity");
                            item.category1 = jsonObject.getString("category1");
                            item.category2 = jsonObject.getString("category2");
                            item.topedu = jsonObject.getString("topedu");
                            item.tlevel = jsonObject.getString("tlevel");
                            list.add(item);
                        } catch (Exception e) {


                        }
                    }


                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}
