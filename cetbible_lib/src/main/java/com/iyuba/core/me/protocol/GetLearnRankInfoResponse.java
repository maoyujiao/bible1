package com.iyuba.core.me.protocol;

import android.util.Log;

import com.iyuba.core.me.sqlite.mode.LearnRankUser;
import com.iyuba.core.util.GsonUtils;
import com.iyuba.http.toolbox.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public class GetLearnRankInfoResponse extends BaseJSONResponse {

    public String result = "";
    public String message = "";

    public String myImgSrc = "";
    public String myId = "";
    public String myRanking = "";
    public String myName = "";
    public String totalEssay = "";
    public String totalWord = "";
    public String totalTime = "";
    public List<LearnRankUser> rankUsers = new ArrayList<LearnRankUser>();

    @Override
    protected boolean extractBody(JSONObject headerElement, String bodyElement) {
        Log.e("LearnGetRank", "====================");
        try {
            JSONObject jsonRoot = new JSONObject(bodyElement);
            message = jsonRoot.getString("message");

            if (message.equals("Success")) {
                result = jsonRoot.getString("result");

                totalWord = jsonRoot.getString("totalWord");
                totalTime = jsonRoot.getString("totalTime");
                totalEssay = jsonRoot.getString("totalEssay");
                myImgSrc = jsonRoot.getString("myimgSrc");
                myId = jsonRoot.getString("myid");
                myRanking = jsonRoot.getString("myranking");
                myName = jsonRoot.getString("myname");

                rankUsers = GsonUtils.toObjectList(jsonRoot.getString("data"), LearnRankUser.class);
//                for (Iterator itr = comments.iterator(); itr.hasNext(); ) {
//                    Comment ct = (Comment) itr.next();
//                    ct.produceBackList();
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
