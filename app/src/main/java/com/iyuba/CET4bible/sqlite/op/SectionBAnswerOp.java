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
import com.iyuba.core.sqlite.mode.test.CetAnswer;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class SectionBAnswerOp extends DatabaseService {
    public static final String TABLE_NAME = "answerb" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "testtime";
    public static final String NUMBER = "number";
    public static final String QUESTION = "question";
    public static final String ANSWERA = "answera";
    public static final String ANSWERB = "answerb";
    public static final String ANSWERC = "answerc";
    public static final String ANSWERD = "answerd";
    public static final String SOUND = "sound";
    public static final String ANSWER = "answer";
    public static final String FLG = "flg";

    public SectionBAnswerOp(Context context) {
        super(context);
    }

    public ArrayList<CetAnswer> selectData(String year) {
        ArrayList<CetAnswer> texts = new ArrayList<CetAnswer>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + QUESTION + ","
                        + ANSWERA + "," + ANSWERB + "," + ANSWERC + ","
                        + ANSWERD + "," + SOUND + "," + ANSWER + "," + FLG
                        + " from " + getTableName() + " where " + TESTTIME + "= ?",
                new String[]{year});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            texts.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (texts.size() != 0) {
            return texts;
        }
        return null;
    }

    private String getTableName() {


            return "answerb"+ Constant.APP_CONSTANT.TYPE();

    }

    private CetAnswer fillIn(Cursor cursor) {
        CetAnswer answer = new CetAnswer();
        answer.id = cursor.getString(1);
        answer.question = cursor.getString(2);
        answer.a1 = cursor.getString(3);
        answer.a2 = cursor.getString(4);
        answer.a3 = cursor.getString(5);
        answer.a4 = cursor.getString(6);
        answer.sound = cursor.getString(7);
        answer.rightAnswer = cursor.getString(8);
        answer.flag = cursor.getString(9);
        answer.yourAnswer = "";
        answer.qsound = "Q" + answer.id + ".mp3";
        return answer;
    }
}
