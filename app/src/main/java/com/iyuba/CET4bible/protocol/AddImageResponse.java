package com.iyuba.CET4bible.protocol;

import com.iyuba.CET4bible.sqlite.mode.ImageData;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 获取评论的Response
 */

public class AddImageResponse extends BaseJSONResponse {
    public ArrayList<ImageData> imageDatas = new ArrayList<>();
    public String result;
    private ImageData imageData;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);
            result = jsonObjectRoot.getString("result");
            if (result != null && result.equals("1")) {
                JSONArray JsonArrayData = jsonObjectRoot.getJSONArray("data");
                JSONObject jsonObjectData;
                int size = JsonArrayData.length();
                for (int i = 0; i < size; i++) {
                    imageData = new ImageData();
                    jsonObjectData = JsonArrayData.getJSONObject(i);
                    imageData.name = jsonObjectData.getString("name");
                    imageData.pic = jsonObjectData.getString("pic");
                    imageData.desc1 = jsonObjectData.getString("desc1");
                    imageData.course_id = jsonObjectData.getString("id");
                    imageDatas.add(imageData);
                }
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }
}
