package com.iyuba.trainingcamp.utils;

import android.content.Context;

import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.RecordWithType;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.utils
 * @class describe
 * @time 2018/8/28 09:55
 * @change
 * @chang time
 * @class describe
 */
public class UploadUtils {


    /***
     * 上传测试结果到大数据 该方法用于上传失败数据的上传
     *
     * @param uid      用户的id
     * @param ability  测试类型id 0写作 1单词  2语法  3听力 4.口语 5阅读
     * @param testMode 测试类型id X写作 W单词  G语法  L听力 S口语 R阅读
     * @param mode     1测评 2练习
     */

//    buildJsonForTestRecord
// {"uid":"5016675","appId":"246","lesson":"cet4",
// "sign":"983ba88718d8f7a99f29a4c64275435f","format":"json",
// "DeviceId":"ONEPLUS A3010","mode":1,"testList":
// [{"uid":"5016675","LessonId":"33105","TestNumber":0,
// "BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21",
// "TestId":0,"TestMode":"W","RightAnswer":"A","UserAnswer":"A",
// "AnswerResut":0,"index":0},{"uid":"5016675","LessonId":"33106",
// "TestNumber":1,"BeginTime":"2018-09-03 16:31:21",
// "TestTime":"2018-09-03 16:31:21","TestId":1,"TestMode":"W",
// "RightAnswer":"B","UserAnswer":"B","AnswerResut":0,"index":1},{"uid":"5016675","LessonId":"33107","TestNumber":2,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":2,"TestMode":"W","RightAnswer":"D","UserAnswer":"C","AnswerResut":0,"index":2},{"uid":"5016675","LessonId":"33108","TestNumber":3,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":3,"TestMode":"W","RightAnswer":"B","UserAnswer":"A","AnswerResut":0,"index":3},{"uid":"5016675","LessonId":"33109","TestNumber":4,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":4,"TestMode":"W","RightAnswer":"C",
// "UserAnswer":"B","AnswerResut":0,"index":4},{"uid":"5016675","LessonId":"33110","TestNumber":5,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":5,"TestMode":"W","RightAnswer":"A","UserAnswer":"C","AnswerResut":0,"index":5},{"uid":"5016675","LessonId":"33111","TestNumber":6,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":6,"TestMode":"W","RightAnswer":"D","UserAnswer":"A","AnswerResut":0,"index":6},{"uid":"5016675","LessonId":"33112","TestNumber":7,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":7,"TestMode":"W","RightAnswer":"C","UserAnswer":"D","AnswerResut":0,"index":7},{"uid":"5016675","LessonId":"33113","TestNumber":8,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":8,"TestMode":"W","RightAnswer":"A","UserAnswer":"B","AnswerResut":0,"index":8},{"uid":"5016675","LessonId":"33114","TestNumber":9,"BeginTime":"2018-09-03 16:31:21","TestTime":"2018-09-03 16:31:21","TestId":9,"TestMode":"W",
//            "RightAnswer":"B","UserAnswer":"C","AnswerResut":0,"index":9}],"scoreList":[]}
    public static void uploadTestRecordToNet(Context mContext, List<TestRecord> mTestRecordList, List<RecordWithType> mAbilityResultLists, String uid, int ability, String testMode, int mode) {
        String jsonForTestRecord = "";

        String strMd5 = uid + GoldApp.getApp(mContext).appId+ GoldApp.getApp(mContext).getLessonType() + "iyubaExam" + getCurTime();
        String sign = MD5.getMD5ofStr(strMd5);

        try {
            jsonForTestRecord = JsonUtil.buildJsonForTestRecord(mContext,mTestRecordList,mAbilityResultLists);
            LogUtils.e("appBaseActivity.java", "buildJsonForTestRecord" + jsonForTestRecord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e("appBaseActivity.java：", "获取将要上传的做题记录！！！！！！！");
        String url = "http://daxue.iyuba.com/ecollege/updateExamRecord.jsp";

        UploadTestRecordRequest up = new UploadTestRecordRequest(jsonForTestRecord, url);//上传

        String result = up.getResultByName("result");
        String jifen = up.getResultByName("jiFen");
        LogUtils.e("积分:" + jifen + "结果   " + result);
        if (Integer.parseInt(jifen) > 0) {
            ToastUtil.showToast(mContext, "测评数据成功同步到云端 +" + jifen + "积分");
        }
        TestRecord testRecords;
        if (!result.equals("-1") && !result.equals("-2")) {// 成功

            LogUtils.e("数据上传服务器");
        } else {
            LogUtils.e("没有数据上传服务器");
        }

    }

    private static String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }
}
