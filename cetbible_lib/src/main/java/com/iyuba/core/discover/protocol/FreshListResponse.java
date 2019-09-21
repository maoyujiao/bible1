/**
 *
 */
package com.iyuba.core.discover.protocol;

import android.util.Log;

import com.iyuba.core.discover.sqlite.mode.FreshContent;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author lmy
 */
public class FreshListResponse extends BaseJSONResponse {

    public String responseString;
    public JSONObject jsonBody;
    public String result;// 返回代码
    public String message;// 返回信息
    public JSONArray data;
    public ArrayList<FreshContent> freshList;
    public String firstPage;
    public String prevPage;
    public String nextPage;
    public String lastPage;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        responseString = bodyElement.toString().trim();
        freshList = new ArrayList<FreshContent>();
        try {
            jsonBody = new JSONObject(responseString.substring(
                    responseString.indexOf("{"), responseString.lastIndexOf("}") + 1));
//			Log.e("jsonBody", ""+jsonBody);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            result = jsonBody.getString("result");
        } catch (JSONException e) {

            e.printStackTrace();
        }


        if (result.equals("391")) {

            try {
                data = jsonBody.getJSONArray("data");

            } catch (JSONException e) {

                e.printStackTrace();
            }
            if (data != null && data.length() != 0) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                for (int i = 0; i < data.length(); i++) {
                    try {
                        FreshContent freshContent = new FreshContent();
                        JSONObject jsonObject = ((JSONObject) data.opt(i));
                        freshContent.body = jsonObject.getString("body");
                        freshContent.dateline = dateFormat.format(new Date(Long.parseLong(jsonObject.getString("dateline")) * 1000l));
                        freshContent.feedid = jsonObject.getString("feedid");
                        freshContent.hot = jsonObject.getString("hot");
                        freshContent.id = jsonObject.getString("id");
                        freshContent.idtype = jsonObject.getString("idtype");
                        freshContent.image = jsonObject.getString("image");
                        //	freshContent.latitude=jsonObject.getString("latitude");
                        freshContent.title = jsonObject.getString("title");
                        freshContent.uid = jsonObject.getString("uid");
                        freshContent.username = jsonObject.getString("username");

                        freshList.add(freshContent);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

                Log.e("IYUBA", freshList.size() + "-------------");

            }


        }
        return true;
    }
}

