package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseNewInfo extends BaseJSONResponse {
    // {"system":0,"letter":2,"notice":1,"follow":0}
    public int system = 0;
    public int letter = 0;
    public int notice = 0;
    public int follow = 0;
    public int allNotice;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {


        try {
            JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            system = jsonObjectRootRoot.getInt("system");
            letter = jsonObjectRootRoot.getInt("letter");
            notice = jsonObjectRootRoot.getInt("notice");
            follow = jsonObjectRootRoot.getInt("follow");
            allNotice = jsonObjectRootRoot.getInt("allsystem")
                    + jsonObjectRootRoot.getInt("allfollow");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}
