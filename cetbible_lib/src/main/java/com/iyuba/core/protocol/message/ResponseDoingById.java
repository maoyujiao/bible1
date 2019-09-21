/**
 *
 */
package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseJSONResponse;
import com.iyuba.core.sqlite.mode.me.DoingsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class ResponseDoingById extends BaseJSONResponse {

    public DoingsInfo doingInfo = new DoingsInfo();
    public JSONArray data;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {

            e3.printStackTrace();
        }
        try {
            doingInfo.message = jsonBody.getString("message");
        } catch (JSONException e) {

            e.printStackTrace();
        }

        try {
            doingInfo.replynum = jsonBody.getString("replynum");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        try {
            doingInfo.uid = jsonBody.getString("uid");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        try {
            doingInfo.username = jsonBody.getString("username");
        } catch (JSONException e2) {

            e2.printStackTrace();
        }
        try {
            doingInfo.dateline = jsonBody.getString("dateline");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        try {
            doingInfo.from = jsonBody.getString("from");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        try {
            doingInfo.ip = jsonBody.getString("ip");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        /*
		 * try { if(jsonBody.isNull("locationDesc")){ doingInfo.locationDesc="";
		 * doingInfo.isReport=false; }else{
		 * doingInfo.locationDesc=jsonBody.getString("locationDesc");
		 * doingInfo.isReport=true; }
		 * 
		 * } catch (JSONException e1) {
		 * e1.printStackTrace(); } try { if(jsonBody.isNull("latitude")){
		 * doingInfo.latitude="";
		 * 
		 * }else{ doingInfo.latitude=jsonBody.getString("latitude");
		 * 
		 * }
		 * 
		 * } catch (JSONException e1) {
		 * e1.printStackTrace(); } try { if(jsonBody.isNull("longitude")){
		 * doingInfo.longitude=""; }else{
		 * doingInfo.longitude=jsonBody.getString("longitude"); }
		 * 
		 * } catch (JSONException e1) {
		 * e1.printStackTrace(); }
		 */
        return true;
    }

}
