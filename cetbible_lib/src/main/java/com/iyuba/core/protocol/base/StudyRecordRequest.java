package com.iyuba.core.protocol.base;

import com.iyuba.configation.Constant;
import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;
import com.iyuba.core.sqlite.mode.StudyRecord;
import com.iyuba.core.util.GetMAC;
import com.iyuba.core.util.TextAttr;

import java.io.IOException;

/**
 * 获取网页单词本
 *
 * @author Administrator
 */
public class StudyRecordRequest extends BaseXMLRequest {
    private StringBuffer sb = new StringBuffer();
    private String device = android.os.Build.BRAND + android.os.Build.MODEL
            + android.os.Build.DEVICE;

    public StudyRecordRequest(String uid, StudyRecord studyRecord) {
        sb.append("http://daxue.iyuba.cn/ecollege/updateStudyRecord.jsp?format=xml&appId=");
        sb.append(Constant.APPID);
        sb.append("&appName=").append(Constant.AppName);
        sb.append("&Lesson=").append(TextAttr.encode(Constant.APPName));
        sb.append("&LessonId=").append(studyRecord.voaid);
        sb.append("&uid=").append(uid);
        sb.append("&Device=").append(TextAttr.encode(TextAttr.encode(device)));
        sb.append("&DeviceId=").append(TextAttr.encode(GetMAC.getMAC()));
        sb.append("&BeginTime=").append(TextAttr.encode(studyRecord.starttime));
        sb.append("&EndTime=").append(TextAttr.encode(studyRecord.endtime));
        sb.append("&EndFlg=").append(studyRecord.flag);
        sb.append("&testNumber=").append(Constant.type);

        setAbsoluteURI(sb.toString());
    }

    @Override
    public BaseHttpResponse createResponse() {

        return new StudyRecordResponse();
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }
}
