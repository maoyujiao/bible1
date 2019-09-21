package com.iyuba.core.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.core.bean.QuestionListBean;
import com.iyuba.core.sqlite.db.DatabaseService;

import java.util.ArrayList;
import java.util.List;


public class QuestionOp extends DatabaseService {

    //Question表
    public static final String TABLE_NAME_QUESTION = "Question";

    public static final String QUESTIONID = "questionid";
    public static final String QUESTION = "question";
    public static final String IMG = "img";
    public static final String AUDIO = "audio";
    public static final String UID = "uid";
    public static final String USERNAME = "username";
    public static final String IMGSRC = "imgsrc";
    public static final String ANSWERCOUNT = "answercount";
    public static final String COMMENTCOUNT = "commentcount";
    public static final String AGREECOUNT = "agreecount";
    public static final String CATEGORY1 = "category1";
    public static final String CATEGORY2 = "category2";
    public static final String LOCATION = "location";
    public static final String FLG = "flg";
    public static final String APP = "app";
    public static final String CREATETIME = "createtime";

    public QuestionOp(Context context) {
        super(context);

    }

    public synchronized void insertQuestions(List<QuestionListBean.QuestionDataBean> questions) {
        if (questions != null && questions.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_NAME_QUESTION + " (" + QUESTIONID + ","
                    + QUESTION + "," + IMG + "," + AUDIO + ","
                    + UID + "," + USERNAME + "," + IMGSRC + ","
                    + ANSWERCOUNT + "," + COMMENTCOUNT + ","
                    + AGREECOUNT + "," + CATEGORY1 + "," + CATEGORY2 + "," + FLG + ","
                    + LOCATION + "," + APP + "," + CREATETIME + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            for (int i = 0; i < questions.size(); i++) {
                QuestionListBean.QuestionDataBean question = questions.get(i);
                Object[] objects = new Object[]{question.getQuestionid(), question.getQuestion(), question.getImg(),
                        question.getAudio(), question.getUid(), question.getUsername(), question.getImgsrc(),
                        question.getAnswercount(), question.getCommentcount(), question.getAgreecount(), question.getCategory1(),
                        question.getCategory2(), question.getFlg(), question.getLocation(), question.getApp(), question.getCreatetime()};
                importDatabase.openDatabase().execSQL(sqlString, objects);

                closeDatabase(null);
            }
        }
    }

    /**
     * 删除  数据库里面的名师堂问题
     */

    public synchronized boolean deleteQuestionData() {
        String sqlString = "delete from Question";
        try {
            importDatabase.openDatabase().execSQL(sqlString);
            closeDatabase(null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 查找所有的名师堂列表问题的信息
     *
     * @return
     */
    public synchronized ArrayList<QuestionListBean.QuestionDataBean> findDataByAll() {
        ArrayList<QuestionListBean.QuestionDataBean> questions = new ArrayList<QuestionListBean.QuestionDataBean>();

        Cursor cursor = null;
        try {
            cursor = importDatabase.openDatabase().rawQuery(
                    "select *" + " from " + TABLE_NAME_QUESTION
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                QuestionListBean.QuestionDataBean question = new QuestionListBean.QuestionDataBean();
                question.setQuestionid(cursor.getInt(0));
                question.setQuestion(cursor.getString(1));
                question.setImg(cursor.getString(2));
                question.setAudio(cursor.getString(3));
                question.setUid(cursor.getString(4));
                question.setUsername(cursor.getString(5));
                question.setImgsrc(cursor.getString(6));
                question.setAnswercount(cursor.getInt(7));
                question.setCommentcount(cursor.getInt(8));
                question.setAgreecount(cursor.getInt(9));
                question.setCategory1(cursor.getString(10));
                question.setCategory2(cursor.getString(11));
                question.setLocation(cursor.getString(12));
                question.setFlg(cursor.getInt(13));
                question.setApp(cursor.getString(14));
                question.setCreatetime(cursor.getString(15));
                questions.add(question);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return questions;
    }


    /**
     * 查找所有的名师堂列表问题的信息
     *
     * @return
     */
    public synchronized ArrayList<QuestionListBean.QuestionDataBean> findDataLastTwenty() {
        ArrayList<QuestionListBean.QuestionDataBean> questions = new ArrayList<QuestionListBean.QuestionDataBean>();

        Cursor cursor = null;
        try {
            cursor = importDatabase.openDatabase().rawQuery(
                    "select *" + " from " + TABLE_NAME_QUESTION + " LIMIT 20"
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                QuestionListBean.QuestionDataBean question = new QuestionListBean.QuestionDataBean();
                question.setQuestionid(cursor.getInt(0));
                question.setQuestion(cursor.getString(1));
                question.setImg(cursor.getString(2));
                question.setAudio(cursor.getString(3));
                question.setUid(cursor.getString(4));
                question.setUsername(cursor.getString(5));
                question.setImgsrc(cursor.getString(6));
                question.setAnswercount(cursor.getInt(7));
                question.setCommentcount(cursor.getInt(8));
                question.setAgreecount(cursor.getInt(9));
                question.setCategory1(cursor.getString(10));
                question.setCategory2(cursor.getString(11));
                question.setLocation(cursor.getString(12));
                question.setFlg(cursor.getInt(13));
                question.setApp(cursor.getString(14));
                question.setCreatetime(cursor.getString(15));
                questions.add(question);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return questions;
    }
}
