package com.iyuba.core.protocol.news;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.sqlite.mode.CommonNewsDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimpleDetailResponse extends BaseJSONResponse {
    public List<CommonNewsDetail> voaDetailsTemps = new ArrayList<CommonNewsDetail>();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRoot = new JSONObject(bodyElement);
            JSONArray JsonArrayData = jsonObjectRoot.getJSONArray("data");
            if (JsonArrayData != null) {
                int size = JsonArrayData.length();
                JSONObject jsonObjectData;
                CommonNewsDetail voaDetailTemp;
                for (int i = 0; i < size; i++) {
                    jsonObjectData = JsonArrayData.getJSONObject(i);
                    voaDetailTemp = new CommonNewsDetail();
                    try {
                        voaDetailTemp.startTime = Double
                                .parseDouble(jsonObjectData.getString("Timing"));
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    try {
                        voaDetailTemp.endTime = Double
                                .parseDouble(jsonObjectData
                                        .getString("EndTiming"));
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    try {
                        voaDetailTemp.sentence_cn = jsonObjectData
                                .getString("sentence_cn");
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    try {
                        voaDetailTemp.paraid = jsonObjectData
                                .getString("ParaId");
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    try {
                        voaDetailTemp.idindex = jsonObjectData
                                .getString("IdIndex");
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    try {
                        voaDetailTemp.sentence = jsonObjectData
                                .getString("Sentence");
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    voaDetailsTemps.add(voaDetailTemp);
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }
}
