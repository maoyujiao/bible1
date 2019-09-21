package com.iyuba.core.me.protocol;

import com.iyuba.core.me.sqlite.mode.WordDetail;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WordDetailResponse extends BaseJSONResponse {
    public List<WordDetail> mList = new ArrayList<WordDetail>();
    public String result;
    public int num;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);

            result = jsonObjectRoot.getString("result");

            num = Integer.parseInt(jsonObjectRoot.getString("num"));
            for (int i = 0; i < num; i++) {
                WordDetail wd = new WordDetail();
                wd.word = jsonObjectRoot.getString("word_" + i);
                wd.frequent = jsonObjectRoot.getString("updatetimes_" + i);
                mList.add(wd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}
