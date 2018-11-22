/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.configation.Constant;
import com.iyuba.core.sqlite.mode.test.CetFillInBlank;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class SectionCAnswerOp extends DatabaseService {
    public static final String TABLE_NAME = "answerC" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "testtime";
    public static final String NUMBER = "number";
    public static final String QUESTION = "question";
    public static final String KEYWORD1 = "keyword1";
    public static final String KEYWORD2 = "keyword2";
    public static final String KEYWORD3 = "keyword3";
    public static final String SOUND = "sound";
    public static final String ANSWER = "answer";

    public SectionCAnswerOp(Context context) {
        super(context);
    }

    public ArrayList<CetFillInBlank> selectData(String year) {
        ArrayList<CetFillInBlank> texts = new ArrayList<CetFillInBlank>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + QUESTION + ","
                        + KEYWORD1 + "," + KEYWORD2 + "," + KEYWORD3 + ","
                        + SOUND + "," + ANSWER + " from " + getTableName()
                        + " where " + TESTTIME + "= ?", new String[]{year});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            texts.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (texts.size() != 0) {
            return texts;
        }
        return null;
    }

    public void update(CetFillInBlank blank, String year) {
        importDatabase.openDatabase().execSQL(
                "update " + getTableName() + " set " + QUESTION
                        + "='" + blank.question + "' where " + NUMBER + "='" + blank.id + "' AND "
                        + TESTTIME + "='" + year + "'");
        closeDatabase(null);
    }

    private String getTableName() {

            return "answerC" + Constant.APP_CONSTANT.TYPE();
    }

    private CetFillInBlank fillIn(Cursor cursor) {
        CetFillInBlank answer = new CetFillInBlank();
        answer.id = cursor.getString(1);
        answer.question = cursor.getString(2);
        answer.keyword1 = cursor.getString(3);
        answer.keyword2 = cursor.getString(4);
        answer.keyword3 = cursor.getString(5);
        answer.sound = "Q" + cursor.getString(6);
        answer.answer = cursor.getString(7);
        answer.yourAnswer = "";
        return answer;
    }
}
