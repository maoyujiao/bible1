package com.iyuba.core.me.protocol;

import com.google.gson.Gson;
import com.iyuba.core.me.sqlite.mode.TestResultDetail;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestDetailResponse extends BaseJSONResponse {
    public List<TestResultDetail> mList = new ArrayList<>();
    public String result;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            TestDetailResponseBean responseBean = new Gson().fromJson(bodyElement, TestDetailResponseBean.class);
            result = responseBean.getResult();

            List<TestDetailResponseBean.DataBean> list = responseBean.getData();
            if (list == null || list.size() < 1) {
                return true;
            }
            for (int i = 0; i < list.size(); i++) {
                TestResultDetail lwd = new TestResultDetail();
                TestDetailResponseBean.DataBean bean = list.get(i);

                lwd.testTime = bean.getTestTime();
                lwd.lessonId = bean.getLessonId();
                lwd.testNum = bean.getTestNumber();
                lwd.testWords = bean.getTestWords();//temp.getString("TestWords");
                lwd.beginTime = bean.getBeginTime();//temp.getString("BeginTime");
                lwd.testindex = bean.getTestindex();//temp.getString("testindex");
                lwd.userAnswer = bean.getUserAnswer();//temp.getString("UserAnswer");
                lwd.rightAnswer = bean.getRightAnswer();//temp.getString("RightAnswer");
                lwd.score = bean.getScore();//temp.getString("Score");
                lwd.updateTime = bean.getUpdateTime();// temp.getString("UpdateTime");

                mList.add(lwd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
