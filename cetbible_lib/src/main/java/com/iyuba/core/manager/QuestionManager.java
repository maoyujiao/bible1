package com.iyuba.core.manager;

import com.iyuba.core.bean.QuestionListBean;
import com.iyuba.core.teacher.sqlite.mode.IyuTeacher;
import com.iyuba.core.teacher.sqlite.mode.Teacher;

import java.util.HashMap;

public class QuestionManager {
    private static QuestionManager instance;
    public QuestionListBean.QuestionDataBean question = new QuestionListBean.QuestionDataBean();//主列表中用到
    public IyuTeacher teacher = new IyuTeacher();//名师的信息

    public Teacher mTeacher = new Teacher();//老师验证的teacher信息


    //废弃不用
    public HashMap<String, String> cat1 = new HashMap<String, String>();
    public HashMap<String, String> cat2 = new HashMap<String, String>();

    private QuestionManager() {
    }

    public static QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }
}
