/**
 *
 */
package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yao
 */
public class ReadNoticeRequest extends BaseJSONRequest {

    public ReadNoticeRequest(String ids) {
        setAbsoluteURI("http://www.iyuba.cn/question/readNotice.jsp?ids=" + ids);

        Log.e("iyuba", "http://www.iyuba.cn/question/readNotice.jsp?ids=" + ids);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

        // return null;
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new ReadNoticeResponse();
    }

}
