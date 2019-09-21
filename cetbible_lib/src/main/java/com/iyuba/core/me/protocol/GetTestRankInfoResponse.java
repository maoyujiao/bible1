package com.iyuba.core.me.protocol;

import android.util.Log;

import com.iyuba.core.me.sqlite.mode.TestRankUser;
import com.iyuba.core.util.GsonUtils;
import com.iyuba.http.toolbox.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public class GetTestRankInfoResponse extends BaseJSONResponse {

    public String result = "";
    public String message = "";

    public String myImgSrc = "";
    public String myId = "";
    public String myRanking = "";
    public String myName = "";
    public String totalTest = "";
    public String totalRight = "";
    public List<TestRankUser> rankUsers = new ArrayList<TestRankUser>();

    @Override
    protected boolean extractBody(JSONObject headerElement, String bodyElement) {
        Log.e("TestGetRank", "====================");
        try {
            JSONObject jsonRoot = new JSONObject(bodyElement);
            message = jsonRoot.getString("message");

            if (message.equals("Success")) {
                result = jsonRoot.getString("result");

                totalTest = jsonRoot.getString("totalTest");
                totalRight = jsonRoot.getString("totalRight");
                myImgSrc = jsonRoot.getString("myimgSrc");
                myId = jsonRoot.getString("myid");
                myRanking = jsonRoot.getString("myranking");
                myName = jsonRoot.getString("myname");

                rankUsers = GsonUtils.toObjectList(jsonRoot.getString("data"), TestRankUser.class);
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
