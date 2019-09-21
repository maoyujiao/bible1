package com.iyuba.core.discover.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.util.MD5;

import org.json.JSONException;
import org.json.JSONObject;

public class BlogRequest extends BaseJSONRequest {

    {
        requestId = 0;
    }


    public BlogRequest(String blogid) {

        MD5 m = new MD5();

        //根据ID和Type取包的信息http://api.iyuba.com.cn/v2/api.iyuba?protocol=31001&uid=928&sign=911d31a89b57ee074898720e819c4c6az
        setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=20008&blogid=" + blogid +
                "&sign=" + MD5.getMD5ofStr("20008" + blogid + "iyubaV2"));

        Log.d("BlogRequest", "http://api.iyuba.com.cn/v2/api.iyuba?protocol=20008&blogid=" + blogid +
                "&sign=" + MD5.getMD5ofStr("20008" + blogid + "iyubaV2"));

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public BaseHttpResponse createResponse() {

        return new BlogResponse();
    }

}

