package com.iyuba.core.me.protocol;

import com.google.gson.Gson;
import com.iyuba.core.me.sqlite.mode.ListenWordDetail;
import com.iyuba.core.protocol.BaseJSONResponse;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListenDetailResponse extends BaseJSONResponse {
    public List<ListenWordDetail> mList = new ArrayList<>();
    public String result;

    SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            ListenDetailResponseBean responseBean = new Gson().fromJson(bodyElement, ListenDetailResponseBean.class);
            result = responseBean.getResult();

            List<ListenDetailResponseBean.DataBean> list = responseBean.getData();

            if (list == null || list.size() < 1) {
                return true;
            }

            for (int i = 0; i < list.size(); i++) {
                ListenWordDetail lwd = new ListenWordDetail();
                ListenDetailResponseBean.DataBean bean = list.get(i);

                lwd.lesson = bean.getLesson();
                lwd.lessonId = bean.getLessonId();
                lwd.testNum = bean.getTestNumber();
                lwd.testWd = bean.getTestWords();
                try {
                    Date beginTime = dateformat.parse(bean.getBeginTime());
                    Date endTime = dateformat.parse(bean.getEndTime());
                    long diff = (endTime.getTime() - beginTime.getTime()) / 1000;
                    lwd.time = String.valueOf(diff);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                mList.add(lwd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }

}
