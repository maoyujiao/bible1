/**
 *
 */
package com.iyuba.core.me.protocol;

import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.protocol.VOABaseJsonResponse;
import com.iyuba.core.sqlite.mode.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author yao
 */
public class ResponseBasicUserInfo extends VOABaseJsonResponse {
    public String message;// 返回信息
    public String result;// 返回代码
    public UserInfo userInfo = AccountManager.Instace(RuntimeManager
            .getContext()).userInfo;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            result = jsonBody.getString("result");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            message = jsonBody.getString("message");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.relation = jsonBody.getString("relation");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.username = jsonBody.getString("username");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.icoins = jsonBody.getString("icoins");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.money = jsonBody.getString("money");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            userInfo.doings = jsonBody.getString("doings");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.views = jsonBody.getString("views");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.gender = jsonBody.getString("gender");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.text = jsonBody.getString("text");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.distance = jsonBody.getString("distance");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.follower = jsonBody.getString("follower");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            userInfo.following = jsonBody.getString("following");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            if (jsonBody.has("notification")) {
                userInfo.notification = jsonBody.getString("notification");
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}