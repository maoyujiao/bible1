package com.iyuba.core.me.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class TestDetailRequest extends BaseJSONRequest {
    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    private String sign;

    public TestDetailRequest(String uid, String testMode) {
        this.sign = uid + dft.format(System.currentTimeMillis());
        setAbsoluteURI("http://daxue.iyuba.cn/ecollege/getTestRecordDetail.jsp?format=json&uid="
                + uid
                + "&TestMode="
                + testMode
                + "&sign="
                + MD5.getMD5ofStr(sign));
        Log.e("TestDetailRequest", "" + "http://daxue.iyuba.cn/ecollege/getTestRecordDetail.jsp?format=json&uid="
                + uid
                + "&TestMode="
                + testMode
                + "&sign="
                + MD5.getMD5ofStr(sign));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new TestDetailResponse();
    }

}