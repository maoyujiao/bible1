package com.iyuba.core.protocol.base;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationResponse extends BaseJSONResponse {
    public String subLocality = "";
    public String locality = "";
    public String province = "";
    private String type = "";

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);
            JSONArray JsonArrayData = jsonObjectRoot.getJSONArray("results");
            if (JsonArrayData != null) {
                JSONObject jsonLocationData = JsonArrayData.getJSONObject(0);
                JSONArray JsonArrayDataInner = jsonLocationData
                        .getJSONArray("address_components");
                if (JsonArrayDataInner != null) {
                    int size = JsonArrayDataInner.length();
                    JSONObject jsonPositionData;
                    JSONArray JsonArrayDataType;
                    for (int i = 0; i < size; i++) {
                        jsonPositionData = JsonArrayDataInner.getJSONObject(i);
                        JsonArrayDataType = jsonPositionData
                                .getJSONArray("types");
                        type = JsonArrayDataType.get(0).toString();
                        if (type.equals("administrative_area_level_1")) {
                            province = jsonPositionData.getString("short_name");
                        }
                        if (type.equals("locality")) {
                            locality = jsonPositionData.getString("short_name");
                        }
                        if (type.equals("sublocality")) {
                            subLocality = jsonPositionData
                                    .getString("short_name");
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
