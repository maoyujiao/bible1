package com.iyuba.core.teacher.protocol;

import android.util.Log;

import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.teacher.sqlite.mode.Teacher;
import com.iyuba.core.util.TextAttr;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateClassRequest extends BaseJSONRequest {
    private String format = "json";

    public UpdateClassRequest(Teacher teacher) {
        setAbsoluteURI("http://www.iyuba.cn/question/teacher/api/updateClass.jsp?format=json&uid="
                + teacher.uid + "&username=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.username)))
                + "&city=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.tcity)))
                + "&endegree=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.endegree)))
                + "&jpdegree=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.jpdegree)))
                + "&part=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.category1)))
                + "&positionName=" + TextAttr.encode(TextAttr.encode(TextAttr.encode(teacher.category2)))
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
