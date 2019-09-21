package com.iyuba.core.me.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class ListenDetailRequest extends BaseJSONRequest {
    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    String sign;
    private String uid;
    private String page;
    private String numPerPage;
    private String testMode;

    public ListenDetailRequest(String uid, String page, String numPerPage,
                               String testMode) {
        this.uid = uid;
        this.page = page;
        this.numPerPage = numPerPage;
        this.testMode = testMode;
        this.sign = uid + dft.format(System.currentTimeMillis());


        Log.e("url", "http://daxue.iyuba.cn/ecollege/getStudyRecordByTestMode.jsp?format=json&uid="
                + uid
                + "&Pageth="
                + page
                + "&NumPerPage="
                + numPerPage
                + "&TestMode=" + testMode + "&sign=" + MD5.getMD5ofStr(sign));
        setAbsoluteURI("http://daxue.iyuba.cn/ecollege/getStudyRecordByTestMode.jsp?format=json&uid="
                + uid
                + "&Pageth="
                + page
                + "&NumPerPage="
                + numPerPage
                + "&TestMode=" + testMode + "&sign=" + MD5.getMD5ofStr(sign));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ListenDetailResponse();
    }

}
