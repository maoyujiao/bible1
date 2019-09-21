package com.iyuba.abilitytest.manager;

import com.iyuba.abilitytest.entity.AbilityQuestion;
import com.iyuba.abilitytest.entity.AbilityResult;
import com.iyuba.abilitytest.entity.ExamScore;

import java.util.ArrayList;

/**
 * Created by LiuZhenLi on 2016/10/10.
 */
public class DataManager {

    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<ExamScore.DataBean> abilityResList = new ArrayList<>();//测试结果
    public ArrayList<AbilityQuestion.TestListBean> practiceList = new ArrayList<>();//练习题
    public String lessonId = "-1/";
}
