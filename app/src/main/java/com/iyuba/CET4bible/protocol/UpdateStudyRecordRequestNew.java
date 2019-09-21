package com.iyuba.CET4bible.protocol;

import android.util.Log;

import com.iyuba.configation.Constant;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.util.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UpdateStudyRecordRequestNew extends BaseHttpRequest {

    /**
     * updateStudyRecordNew.jsp参数
     * format：	要求返回的格式，json或xml、默认为json
     * uid:（非空） 爱语吧用户id，没有登录用户：0
     * DeviceId(非空)  应用的设备ID或网卡ID
     * appId: 应用的ID， 在http://bigdata.iyuba.com 中存在
     * BeginTime:（非空） 开始时间 格式：2014-02-19 15:33:00
     * EndTime:	结束时间 格式：2014-02-19 15:33:00
     * Lesson:（非空,encode一次）课程名称：voa,BBC,听歌学英语，英语四级，英语六级
     * LessonId:（非空） 课程id 默认值为0（songid,voaid,bbcid,四级题的年月id等）
     * TestNumber:（可空）	测试题号 默认值为0，
     * TestWords:（可空）	测试单词数 默认值为0，一般是本次学习内容中的单词总数
     * TestMode:（可空）	测试模试 1：听力 2：口语 3：阅读 4：写作 0：未确定（VOA，BBC，听歌学学习整篇内容可以设置为0）
     * UserAnswer:（可空）		用户的答案，一般是指本次听力中答题内容。（A，B，C，D）或者听说练习中听不懂的单词或说不准的单词，多个单词用逗号分开
     * Score:（可空）	 默认值为0（对应的做题的成绩，）
     * EndFlg:	完成标志：0：只开始听，点暂停提交；1：听力完成；2：做题完成,
     * Device:	做题设备：android手机，iphone手机，firefox,ie等
     * platform:	ios,android,air等
     * sign: Md5(uid+BeginTime+"YYYY-MM-DD"); YYYY-MM-DD是系统日期
     */
    public UpdateStudyRecordRequestNew(StudyRecordInfo studyRecordInfo) {
        setAbsoluteURI(getUrl(studyRecordInfo, "1", "0"));


        Log.e("UpdateStudyRecordNew", getAbsoluteURI());

    }

    public static String getUrl(StudyRecordInfo studyRecordInfo, String testMode, String testWords) {
        try {
            return "http://daxue.iyuba.cn/ecollege/updateStudyRecordNew.jsp?format=json"
                    + "&uid=" + studyRecordInfo.getUid()
                    + "&BeginTime=" + URLEncoder.encode(studyRecordInfo.getBeginTime(), "UTF-8")
                    + "&EndTime=" + URLEncoder.encode(studyRecordInfo.getEndTime(), "UTF-8")
                    + "&Lesson=" + URLEncoder.encode(URLEncoder.encode(studyRecordInfo.getLesson(), "UTF-8"), "UTF-8")
                    + "&TestMode=" + testMode
                    + "&TestWords=" + testWords
                    + "&LessonId=" + studyRecordInfo.getLessonId()
                    + "&EndFlg=" + studyRecordInfo.getEndFlg()
                    + "&platform=" + URLEncoder.encode(studyRecordInfo.Device, "UTF-8")
                    + "&appName=" + Constant.APPName
                    + "&appId=" + Constant.APPID
                    + "&DeviceId=" + studyRecordInfo.DeviceId
                    + "&TestNumber=" + studyRecordInfo.TestNumber
                    + "&sign=" + MD5.getMD5ofStr(studyRecordInfo.uid + studyRecordInfo.BeginTime + getCurTime());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public com.iyuba.core.protocol.BaseHttpResponse createResponse() {
        return new UploadStudyRecordResponseNew();
    }

    public static String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return df.format(System.currentTimeMillis());
    }

}
