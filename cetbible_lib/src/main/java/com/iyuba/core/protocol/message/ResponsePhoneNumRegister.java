package com.iyuba.core.protocol.message;

import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponsePhoneNumRegister extends BaseJSONResponse {
    public String resultCode;
    public String message;
    public boolean isRegSuccess;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            JSONObject jsonObjectRoot = new JSONObject(bodyElement);
            resultCode = jsonObjectRoot.getString("result");
            message = jsonObjectRoot.getString("message");
            isRegSuccess = resultCode.equals("111");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        return true;
    }

}
