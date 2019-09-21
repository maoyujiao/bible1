package com.iyuba.core.me.protocol;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class WordDetailRequest extends BaseJSONRequest {
    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    private String uid;
    private String sign;

    public WordDetailRequest(String uid, String mode) {
        this.uid = uid;
        this.sign = uid + dft.format(System.currentTimeMillis());
        // setAbsoluteURI("http://daxue.iyuba.cn/ecollege/getListenDetailRecord.jsp?format=json&uid="
        // + uid);
        setAbsoluteURI("http://daxue.iyuba.cn/ecollege/getWordsRecordDetail.jsp?format=json&uid="
                + uid
                + "&TestMode="
                + mode
                + "&sign="
                + MD5.getMD5ofStr(sign));
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new WordDetailResponse();
    }

}
