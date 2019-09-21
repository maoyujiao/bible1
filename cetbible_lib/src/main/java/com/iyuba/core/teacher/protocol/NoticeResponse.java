package com.iyuba.core.teacher.protocol;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.teacher.sqlite.mode.Notice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoticeResponse extends BaseJSONResponse {

    public String result;
    public String message;
    public String total;


    public List<Notice> list = new ArrayList<Notice>();

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
                    Notice item;
                    JSONObject jsonObject;
                    for (int i = 0; i < size; i++) {
                        try {
                            item = new Notice();
                            jsonObject = ((JSONObject) data.opt(i));
                            item.author = jsonObject.getString("author");
                            item.authorid = jsonObject.getString("authorid");
                            item.from_id = jsonObject.getInt("from_id");
                            item.from_idtype = jsonObject.getString("from_idtype");
                            item.id = jsonObject.getString("id");
                            item.isnew = jsonObject.getString("new");
                            item.note = jsonObject.getString("note");
                            item.uid = jsonObject.getString("uid");
                            item.time = jsonObject.getString("time");

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
