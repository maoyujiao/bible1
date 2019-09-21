package com.iyuba.core.discover.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;


public class FreshListRequest extends BaseJSONRequest {

    {
        requestId = 0;
    }


    public FreshListRequest(String uid, String pageNumber) {

        MD5 m = new MD5();

        //根据ID和Type取包的信息http://api.iyuba.com.cn/v2/api.iyuba?protocol=31001&uid=928&sign=911d31a89b57ee074898720e819c4c6az
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=31001&uid=" + uid + "&&pageNumber=" + pageNumber + "&pageCounts=20" +
                "&sign=" + MD5.getMD5ofStr("31001" + uid + "iyubaV2") + "&find=2&feeds=blog,doing,album");

        Log.d("FreshListRequest", "http://api.iyuba.com.cn/v2/api.iyuba?protocol=31001&uid=" + uid + "&&pageNumber=" + pageNumber + "&pageCounts=20" +
                "&sign=" + MD5.getMD5ofStr("31001" + uid + "iyubaV2") + "&find=2&feeds=blog,doing,album");

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new FreshListResponse();
    }

}

