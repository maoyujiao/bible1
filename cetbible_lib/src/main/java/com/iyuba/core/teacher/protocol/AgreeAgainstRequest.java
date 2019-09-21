package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AgreeAgainstRequest extends BaseJSONRequest {

    public AgreeAgainstRequest(String uid, String uname, String type, String typeid) {
        super();

        //用户名转码
        try {
            uname = new String(uname.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        uname = TextAttr.encode(TextAttr.encode(TextAttr.encode(uname)));

        setAbsoluteURI("http://www.iyuba.cn/question/updateAgree.jsp?uid=" + uid
                + "&username=" + uname + "&type=" + type + "&typeid=" + typeid
        );


        Log.e("iyuba", "http://www.iyuba.cn/question/updateAgree.jsp?uid=" + uid
                + "&username=" + uname + "&type=" + type + "&typeid=" + typeid);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new AgreeAgainstResponse();
    }

}
