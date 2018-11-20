package com.iyuba.trainingcamp.utils;

import android.content.Context;
import android.util.Log;

import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.RecordWithType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

//import com.iyuba.abilitytest.entity.AbilityResult;
//import com.iyuba.abilitytest.entity.TestRecord;
//import com.iyuba.abilitytest.utils.CommonUtils;


public class JsonUtil {
    public static final String TAG = "JsonUtil";

    public static String buildJsonForTestRecord(Context context, List<TestRecord> tRecords, List<RecordWithType> tResults) throws JSONException {
        String jsondata;
        String strMd5 = tRecords.get(0).uid + GoldApp.getApp(context).appId + GoldApp.getApp(context).getLessonType() + "iyubaExam" + getCurTime();
        String sign = MD5.getMD5ofStr(strMd5);

        JSONObject jsonRoot = new JSONObject();
        jsonRoot.put("uid", tRecords.get(0).uid);
        jsonRoot.put("appId", GoldApp.getApp(context).appId); //应用ID
        jsonRoot.put("lesson", GoldApp.getApp(context).getLessonType()); //课程类型
        jsonRoot.put("sign", sign);
        jsonRoot.put("format", "json"); //返回格式
        jsonRoot.put("DeviceId", android.os.Build.MODEL); //设备ID
        jsonRoot.put("mode", 3); //区分是练习还是测试, 1测评 , 2练习
        JSONArray json = new JSONArray();
        for (int i = 0; i < tRecords.size(); i++) {
            JSONObject jsonObj = new JSONObject();
            TestRecord tRecord = tRecords.get(i);
//            jsonObj.put("uid", tRecord.uid);
//            jsonObj.put("LessonId", tRecord.Id);
//            jsonObj.put("TestNumber", tRecord.TestNumber);
//            jsonObj.put("BeginTime", tRecord.BeginTime);
//            jsonObj.put("TestTime", tRecord.TestTime);
//            jsonObj.put("TestId", tRecord.TestNumber);
//
//            jsonObj.put("TestMode", "W");
//            jsonObj.put("RightAnswer", tRecord.RightAnswer);
//            jsonObj.put("UserAnswer", tRecord.UserAnswer);
//            jsonObj.put("AnswerResut", tRecord.AnswerResult);
//            jsonObj.put("index", tRecord.index);
            jsonObj.put("AnswerResut", tRecord.AnswerResult);
            jsonObj.put("BeginTime", tRecord.BeginTime);
            jsonObj.put("Category", tRecord.Categroy);
            jsonObj.put("LessonId", tRecord.Id);
            jsonObj.put("RightAnswer", tRecord.RightAnswer);
            jsonObj.put("TestId", tRecord.TestId);
            jsonObj.put("TestMode", tRecord.TestMode);
            jsonObj.put("TestTime", tRecord.TestTime);
            jsonObj.put("UserAnswer", tRecord.UserAnswer);
            //把每个数据当作一对象添加到数组里
            json.put(jsonObj);
        }
        jsonRoot.put("testList", json);
        JSONArray array = new JSONArray();
        //Score里存储的数据格式  题目总数++正确数量++Category(能力)
//        String[] res = s.split("\\+\\+");
        for (int i = 0; i < tResults.size(); i++) {
            JSONObject itemObject = new JSONObject();

            itemObject.put("Score", tResults.get(i).getScore());
            itemObject.put("lessontype", tResults.get(i).getType());
            itemObject.put("category", tResults.get(i).getCategory());
            itemObject.put("testCnt", tResults.get(i).getTestCount());
            array.put(itemObject);
        }
        //每一个模块试题总数
//            int score = Integer.parseInt(res[0]) == 0 ? 0 : Integer.parseInt(res[1]) * 100 / Integer.parseInt(res[0]);
//            itemObject.put("Score", tRecords + "");//得分转化为百分制
//            LogUtils.e(TAG, CommonUtils.getLessonType(typeid) + "       " + res[2] + "     " + score);
//        return itemObject;
        jsonRoot.put("scoreList", array);

        jsondata = jsonRoot.toString();
        Log.e(TAG, jsondata);
        return jsondata;
        //调用解析JSON方法
        //parserJson(jsondata);  
    }

    /**
     * 将答题记录和用户的成绩一起传递大数据
     * --
     * Warn:这里有一个问题,账户A的数据上传失败,同一设备登录账户B,上传数据的时候,账户A的数据可能上传B数据呢.
     * Solve:上传数据时比对账号是否相同
     */
//    public static String buildJsonForTestRecordDouble(List<TestRecord> tRecords, List<AbilityResult> tResults, String uid, int mode) throws JSONException {
//        String jsondata;
//        JSONObject jsonRoot = new JSONObject();
//        JSONArray array = new JSONArray();
//
//        String strMd5 = uid + Constant.APPID + Constant.mListen + "iyubaExam" + getCurTime();
//        String sign = MD5.getMD5ofStr(strMd5);
//
//        //基本信息
//        jsonRoot.put("uid", uid);
//        jsonRoot.put("appId", Constant.APPID); //应用ID
//        jsonRoot.put("lesson", Constant.mListen); //课程类型
//        jsonRoot.put("sign", sign);
//        jsonRoot.put("format", "json"); //返回格式
//        jsonRoot.put("DeviceId", android.os.Build.MODEL); //设备ID
//        jsonRoot.put("mode", mode); //区分是练习还是测试, 1测评 , 2练习
//
//        //测试记录
//        for (int i = 0; i < tRecords.size(); i++) {
//            JSONObject jsonObj = new JSONObject();
//            TestRecord tRecord = tRecords.get(i);
//            if (tRecord.uid.equals(uid)) {//登录的账户uid与数据库里面的uid相同才可以上传
//                jsonObj.put("BeginTime", tRecord.BeginTime);
//                tRecord.TestTime = (tRecord.TestTime == null || tRecord.TestTime.equals("")) ? tRecord.BeginTime : tRecord.TestTime;
//                jsonObj.put("TestTime", tRecord.TestTime);
//                jsonObj.put("TestMode", tRecord.TestCategory);
//                jsonObj.put("TestId", tRecord.TestNumber);
//                jsonObj.put("LessonId", tRecord.Id);
//                tRecord.RightAnswer = tRecord.RightAnswer.length() > 50 ? tRecord.RightAnswer.substring(0, 50) : tRecord.RightAnswer;
//                jsonObj.put("RightAnswer", tRecord.RightAnswer);
//                tRecord.UserAnswer = tRecord.UserAnswer.length() > 50 ? tRecord.UserAnswer.substring(0, 50) : tRecord.UserAnswer;
//                jsonObj.put("UserAnswer", tRecord.UserAnswer);
//                jsonObj.put("AnswerResut", tRecord.AnswerResult);
//                jsonObj.put("Category", tRecord.Categroy);//2016.10.26  add by Liuzhenli
//                //把每个数据当作一对象添加到数组里
//                array.put(jsonObj);
//            }
//        }
//        jsonRoot.put("testList", array);
//
//        //成绩分析记录
//        array = new JSONArray();
//        for (int i = 0; i < tResults.size(); i++) {
//            AbilityResult tResult = tResults.get(i);
//            if (tResult.uid.equals(uid)) {
//                //把每个数据当作一对象添加到数组里
//                if (!tResult.Score1.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score1));
//                if (!tResult.Score2.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score2));
//                if (!tResult.Score3.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score3));
//                if (!tResult.Score4.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score4));
//                if (!tResult.Score5.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score5));
//                if (!tResult.Score6.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score6));
//                if (!tResult.Score7.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score7));
//                if (!tResult.Score8.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score8));
//                if (!tResult.Score9.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score9));
//                if (!tResult.Score10.equals("-1"))
//                    array.put(getScoreDetail(tResult.TypeId, tResult.Score10));
//            }
//
//        }
//        jsonRoot.put("scoreList", array);
//        jsondata = jsonRoot.toString();
//        Log.e(TAG, jsondata);
//        return jsondata;
//    }

    /**
     * 将答题记录和用户的成绩一起传递大数据  答完题目立刻上传
     * --
     * Warn:这里有一个问题,账户A的数据上传失败,同一设备登录账户B,上传数据的时候,账户A的数据可能上传B数据呢.
     * Solve:上传数据时比对账号是否相同
     */
//    public static String buildJsonForTestRecordDouble2(List<TestRecord> tRecords, AbilityResult tResult, String uid, int mode) throws JSONException {
//        String jsondata;
//        JSONObject jsonRoot = new JSONObject();
//        JSONArray array = new JSONArray();
//
//        String strMd5 = uid + Constant.APPID + Constant.mListen + "iyubaExam" + getCurTime();
//        String sign = MD5.getMD5ofStr(strMd5);
//
//        //基本信息
//        jsonRoot.put("uid", uid);
//        jsonRoot.put("appId", Constant.APPID); //应用ID
//        jsonRoot.put("lesson", Constant.mListen); //课程类型
//        jsonRoot.put("sign", sign);
//        jsonRoot.put("format", "json"); //返回格式
//        jsonRoot.put("DeviceId", android.os.Build.MODEL); //设备ID
//        jsonRoot.put("mode", mode); //区分是练习还是测试, 1测评 , 2练习
//
//        //测试记录
//        for (int i = 0; i < tRecords.size(); i++) {
//            JSONObject jsonObj = new JSONObject();
//            TestRecord tRecord = tRecords.get(i);
//            if (tRecord.uid.equals(uid)) {//登录的账户uid与数据库里面的uid相同才可以上传
//                jsonObj.put("BeginTime", tRecord.BeginTime);
//                tRecord.TestTime = (tRecord.TestTime == null || tRecord.TestTime.equals("")) ? tRecord.BeginTime : tRecord.TestTime;
//                jsonObj.put("TestTime", tRecord.TestTime);
//                jsonObj.put("TestMode", tRecord.TestCategory);
//                jsonObj.put("TestId", tRecord.TestNumber);
//                jsonObj.put("LessonId", tRecord.Id);//这里上传的id不是单元的编号lessonid  而是题目的唯一编号id
//                if (tRecord.RightAnswer == null) {
//                    tRecord.RightAnswer = null;
//                } else {
//                    tRecord.RightAnswer = tRecord.RightAnswer.length() > 50 ? tRecord.RightAnswer.substring(0, 50) : tRecord.RightAnswer;
//                }
//                jsonObj.put("RightAnswer", tRecord.RightAnswer);//less than 50
//                tRecord.UserAnswer = tRecord.UserAnswer.length() > 50 ? tRecord.UserAnswer.substring(0, 50) : tRecord.UserAnswer;
//                jsonObj.put("UserAnswer", tRecord.UserAnswer);//less than 50
//                jsonObj.put("AnswerResut", tRecord.AnswerResult);
//                jsonObj.put("Category", tRecord.Categroy);//2016.10.26  add by Liuzhenli
//                //把每个数据当作一对象添加到数组里
//                array.put(jsonObj);
//            }
//        }
//        jsonRoot.put("testList", array);
//
//        //成绩分析记录
//        array = new JSONArray();
//
//        if (mode == 1 && tResult.uid.equals(uid)) {//测评有成绩分析  练习没有
//            //把每个数据当作一对象添加到数组里
//            if (!tResult.Score1.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score1));
//            if (!tResult.Score2.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score2));
//            if (!tResult.Score3.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score3));
//            if (!tResult.Score4.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score4));
//            if (!tResult.Score5.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score5));
//            if (!tResult.Score6.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score6));
//            if (!tResult.Score7.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score7));
//            if (!tResult.Score8.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score8));
//            if (!tResult.Score9.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score9));
//            if (!tResult.Score10.equals("-1"))
//                array.put(getScoreDetail(tResult.TypeId, tResult.Score10));
//        }
//        jsonRoot.put("scoreList", array);
//        jsondata = jsonRoot.toString();
//        Log.e(TAG, jsondata);
//        return jsondata;
//    }

//    public static JSONObject getScoreDetail(int typeid, String s) {
//        JSONObject itemObject = new JSONObject();
//        //Score里存储的数据格式  题目总数++正确数量++Category(能力)
//        String[] res = s.split("\\+\\+");
//        try {
//            itemObject.put("lessontype", CommonUtils.getLessonType(typeid));
//            itemObject.put("category", res[2]);
//            itemObject.put("testCnt", res[0]);//每一个模块试题总数
//            int score = Integer.parseInt(res[0]) == 0 ? 0 : Integer.parseInt(res[1]) * 100 / Integer.parseInt(res[0]);
//            itemObject.put("Score", score + "");//得分转化为百分制
//            LogUtils.e(TAG, CommonUtils.getLessonType(typeid) + "       " + res[2] + "     " + score);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return itemObject;
//    }
    public static String buildJsonForTestRecordSingle(TestRecord tRecord) throws JSONException {
        String jsondata;
        JSONObject jsonRoot = new JSONObject();
        JSONArray json = new JSONArray();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("uid", tRecord.uid);
        jsonObj.put("LessonId", tRecord.Id);//这里上传的lessonId是题目的唯一id  不是单元区分的id
        jsonObj.put("TestNumber", tRecord.TestNumber);
        jsonObj.put("BeginTime", tRecord.BeginTime);
        jsonObj.put("TestTime", tRecord.TestTime);
        jsonObj.put("RightAnswer", tRecord.RightAnswer);
        jsonObj.put("UserAnswer", tRecord.UserAnswer);
        jsonObj.put("AnswerResut", tRecord.AnswerResult);
        //把每个数据当作一对象添加到数组里   
        json.put(jsonObj);
        jsonRoot.put("datalist", json);
        jsondata = jsonRoot.toString();
        Log.e("JSON", jsondata);
        return jsondata;
    }

    private static String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }

}
