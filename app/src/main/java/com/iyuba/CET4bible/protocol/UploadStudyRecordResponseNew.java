package com.iyuba.CET4bible.protocol;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadStudyRecordResponseNew extends com.iyuba.core.protocol.BaseJSONResponse {
    public String result;
    public String message;
    private String responseString;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {

        try {
            responseString = bodyElement.toString().trim();
            JSONObject jsonObjectRootRoot = new JSONObject(responseString.substring(
                    responseString.indexOf("{", 2), responseString.lastIndexOf("}") + 1));
            //JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            result = bodyElement.charAt(10) + "";
            message = jsonObjectRootRoot.getString("message");
            //Log.d("result", result+"");
            //Log.d("message", message+"");
            if (result.equals("1")) {
                Log.d("��ȡ���ݳɹ�", "�ɹ�");
            } else {
                Log.d("��ȡ���ݳɹ�", "ʧ��");
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return true;
    }

}
