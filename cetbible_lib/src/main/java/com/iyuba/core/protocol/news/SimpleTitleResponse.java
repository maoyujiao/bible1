package com.iyuba.core.protocol.news;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.sqlite.mode.CommonNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SimpleTitleResponse extends BaseJSONResponse {
    public ArrayList<CommonNews> voasTemps = new ArrayList<CommonNews>();
    public int total;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRoot = new JSONObject(bodyElement);
            total = Integer.parseInt(jsonObjectRoot.getString("total"));
            if (total == 0) {
            } else {
                JSONArray JsonArrayData = jsonObjectRoot.getJSONArray("data");
                if (JsonArrayData != null) {
                    int size = JsonArrayData.length();
                    JSONObject jsonObjectData;
                    CommonNews voa;
                    for (int i = 0; i < size; i++) {
                        jsonObjectData = JsonArrayData.getJSONObject(i);
                        voa = new CommonNews();
                        try {
                            voa.id = Integer.parseInt(jsonObjectData
                                    .getString("VoaId"));
                        } catch (Exception e) {

                        }
                        try {
                            voa.id = Integer.parseInt(jsonObjectData
                                    .getString("BbcId"));
                        } catch (Exception e) {

                        }
                        try {
                            voa.id = Integer.parseInt(jsonObjectData
                                    .getString("SongId"));
                        } catch (Exception e) {

                        }
                        try {
                            voa.title = jsonObjectData.getString("Title_cn");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        if (voa.title == null) {
                            try {
                                voa.title = jsonObjectData.getString("Title");
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                        try {
                            voa.content = jsonObjectData.getString("DescCn");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        try {
                            voa.musicUrl = jsonObjectData.getString("Sound");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        try {
                            voa.picUrl = jsonObjectData.getString("Pic");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        try {
                            voa.time = jsonObjectData.getString("CreatTime");
                            voa.time = voa.time.split(" ")[0];
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        voasTemps.add(voa);
                    }
                }
            }
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        return true;
    }

}
