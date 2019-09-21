package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.teacher.sqlite.mode.Teacher;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateBasicRequest extends BaseJSONRequest {
    private String format = "json";

    public UpdateBasicRequest(Teacher teacher) {
        setAbsoluteURI("http://www.iyuba.cn/question/teacher/api/updateBasic.jsp?format=json&uid="
                + teacher.uid + "&username=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.username)))
                + "&name=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.tname))) + "&sex=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.tsex)))
                + "&highestEducation=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.topedu)))
                + "&workYear=" + TextAttr.encode(TextAttr.encode(TextAttr.encode("老师"))) + "&email=" + teacher.temail
                + "&phone=" + teacher.tphone + "&weixin1="
                + teacher.tweixin + "&jianjie=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.tonedesc)))
                + "&image=" + teacher.timg
        );
        Log.e("iyuba", getAbsoluteURI());
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new UpdateBasicResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {

    }

}
