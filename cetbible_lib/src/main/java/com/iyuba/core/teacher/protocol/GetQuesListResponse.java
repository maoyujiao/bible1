package com.iyuba.core.teacher.protocol;

import com.iyuba.core.bean.QuestionListBean;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetQuesListResponse extends BaseJSONResponse {

    public String result;
    public String total;
    public String message;
    public ArrayList<QuestionListBean.QuestionDataBean> list = new ArrayList<QuestionListBean.QuestionDataBean>();
    public HashMap<String, String> abilityTypeCatalog = new HashMap<String, String>();
    public HashMap<String, String> appTypeCatalog = new HashMap<String, String>();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        setAbilityTypeCatalog();
        setAppTypeCatalog();
        try {
            jsonBody = new JSONObject(bodyElement);
            result = jsonBody.getString("result");
            total = jsonBody.getString("total");
            if (result.equals("1")) {
                JSONArray data = jsonBody.getJSONArray("data");
                if (data != null && data.length() != 0) {
                    int size = data.length();
                    QuestionListBean.QuestionDataBean item;
                    JSONObject jsonObject;
                    for (int i = 0; i < size; i++) {
                        try {
                            item = new QuestionListBean.QuestionDataBean();
                            jsonObject = ((JSONObject) data.opt(i));
                            item.setQuestionid(jsonObject.getInt("questionid"));
                            item.setUid(jsonObject.getString("uid"));
                            item.setUsername(jsonObject.getString("username"));
                            item.setImgsrc(jsonObject.getString("imgsrc"));
                            item.setQuestion(jsonObject.getString("question"));
                            item.setImg(jsonObject.getString("img"));
                            item.setAudio(jsonObject.getString("audio"));
                            item.setCommentcount(jsonObject.getInt("commentcount"));
                            item.setAnswercount(jsonObject.getInt("answercount"));
                            item.setCreatetime(jsonObject.getString("createtime"));
                            item.setLocation(jsonObject.getString("location"));
                            item.setApp(jsonObject.getString("app"));
                            item.setAgreecount(jsonObject.getInt("agreecount"));

                            item.setCategory1(jsonObject.getString("category1"));
                            item.setCategory2(jsonObject.getString("category2"));


//							item.setCategory1(abilityTypeCatalog.get(jsonObject.getString("category1")));
//							item.setCategory2(appTypeCatalog.get(jsonObject.get("category2")));
//
//							if(abilityTypeCatalog.get(jsonObject.getString("category1"))==null)
//								item.setCategory1(jsonObject.getString("category1"));
//							if(appTypeCatalog.get(jsonObject.getString("category2"))==null)
//								item.setCategory2(jsonObject.getString("category2"));

                            list.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void setAbilityTypeCatalog() {
        abilityTypeCatalog = new HashMap<String, String>();
        abilityTypeCatalog.put("0", "其他");
        abilityTypeCatalog.put("1", "口语");
        abilityTypeCatalog.put("2", "听力");
        abilityTypeCatalog.put("3", "阅读");
        abilityTypeCatalog.put("4", "写作");
        abilityTypeCatalog.put("5", "翻译");
        abilityTypeCatalog.put("6", "单词");
        abilityTypeCatalog.put("7", "语法");
        abilityTypeCatalog.put("8", "其他");
    }

    public void setAppTypeCatalog() {
        appTypeCatalog = new HashMap<String, String>();
        appTypeCatalog.put("0", "其他");
        appTypeCatalog.put("101", "VOA");
        appTypeCatalog.put("102", "BBC");
        appTypeCatalog.put("103", "听歌");
        appTypeCatalog.put("104", "CET4");
        appTypeCatalog.put("105", "CET6");
        appTypeCatalog.put("106", "托福");
        appTypeCatalog.put("107", "N1");
        appTypeCatalog.put("108", "N2");
        appTypeCatalog.put("109", "N3");
        appTypeCatalog.put("110", "微课");
        appTypeCatalog.put("111", "雅思");
        appTypeCatalog.put("112", "初中");
        appTypeCatalog.put("113", "高中");
        appTypeCatalog.put("114", "考研");
        appTypeCatalog.put("115", "新概念");
        appTypeCatalog.put("116", "走遍美国");
    }

}
