package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.configation.Constant;
import com.iyuba.core.sqlite.mode.test.CetAnswer;

import org.litepal.util.Const;

import java.util.ArrayList;

public class NewTypeSectionAAnswerOp extends DatabaseService {

    public static final String TABLE_NAME = "newtype_answera" + Constant.APP_CONSTANT.TYPE();
    public static final String TABLEA_NAME = "newtype_answera" + Constant.APP_CONSTANT.TYPE();
    public static final String TABLEB_NAME = "newtype_answerb" + Constant.APP_CONSTANT.TYPE();
    public static final String TABLEC_NAME = "newtype_answerc" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "testtime";
    public static final String NUMBER = "number";
    public static final String QUESTION = "question";
    public static final String ANSWERA = "answera";
    public static final String ANSWERB = "answerb";
    public static final String ANSWERC = "answerc";
    public static final String ANSWERD = "answerd";
    public static final String SOUND = "sound";
    public static final String ANSWER = "answer";
    public static final String YANSWER = "your_answer";
    public static final String FLG = "flg";


    public NewTypeSectionAAnswerOp(Context context) {
        super(context);
    }

    public ArrayList<CetAnswer> selectData(String year) {
        ArrayList<CetAnswer> texts = new ArrayList<CetAnswer>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + QUESTION + ","
                        + ANSWERA + "," + ANSWERB + "," + ANSWERC + ","
                        + ANSWERD + "," + SOUND + "," + ANSWER + "," + FLG + ","
                        + YANSWER
                        + " from " + getTableName() + " where " + TESTTIME + "= ?",
                new String[]{year});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            texts.add(fillIn(cursor));
        }
        closeDatabase(null);
        cursor.close();
        if (texts.size() != 0) {
            return texts;
        }
        return null;
    }

    private String getTableName() {
        return "newtype_answera" + Constant.APP_CONSTANT.TYPE();
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
        answer.yourAnswer = cursor.getString(10);
        answer.qsound = "Q" + answer.id + ".mp3";
        return answer;
    }

    public int getRightSum(String year) {
        ArrayList<CetAnswer> texts = new ArrayList<CetAnswer>();
        int sizeA, sizeB, sizeC;
        Cursor cursorA = importDatabase.openDatabase().rawQuery(
                "select Count(*) "
                        + " from " + TABLEA_NAME + " where " + TESTTIME + "= ?" +
                        " and " + ANSWER + " = " + YANSWER,
                new String[]{year});
        Cursor cursorB = importDatabase.openDatabase().rawQuery(
                "select Count(*) "
                        + " from " + TABLEB_NAME + " where " + TESTTIME + "= ?" +
                        " and " + ANSWER + " = " + YANSWER,
                new String[]{year});
        Cursor cursorC = importDatabase.openDatabase().rawQuery(
                "select Count(*) "
                        + " from " + TABLEC_NAME + " where " + TESTTIME + "= ?" +
                        " and " + ANSWER + " = " + YANSWER,
                new String[]{year});
        cursorA.moveToFirst();
        cursorB.moveToFirst();
        cursorC.moveToFirst();
        if (cursorA.getCount() == 0) {
            sizeA = 0;
        } else {
            sizeA = cursorA.getInt(0);
        }
        if (cursorB.getCount() == 0) {
            sizeB = 0;
        } else {
            sizeB = cursorB.getInt(0);
        }
        if (cursorC.getCount() == 0) {
            sizeC = 0;
        } else {
            sizeC = cursorC.getInt(0);
        }

        return sizeA + sizeB + sizeC;
    }

    public void saveUserAnswers(String year, String questionId, String answer) {
        Log.d("diao", year);
        Log.d("diao", questionId);
        Log.d("diao", answer);
        SQLiteDatabase database = importDatabase.openDatabase();
        String sqlA = "UPDATE  " + TABLEA_NAME
                + " SET " + YANSWER + " =  ?" + " where " + TESTTIME + " = ?" +
                " and " + NUMBER + " = ?";
        database.execSQL(sqlA, new String[]{answer, year, questionId});


        String sqlB = "UPDATE  " + TABLEB_NAME
                + " SET " + YANSWER + " =  ?" + " where " + TESTTIME + " = ?" +
                " and " + NUMBER + " = ?";
        database.execSQL(sqlB, new String[]{answer, year, questionId});


        String sqlC = "UPDATE  " + TABLEC_NAME
                + " SET " + YANSWER + " =  ?" + " where " + TESTTIME + " = ?" +
                " and " + NUMBER + " = ?";
        Log.d("diao", "saveUserAnswers: "+sqlA);
        Log.d("diao", "saveUserAnswers: "+sqlB);
        Log.d("diao", "saveUserAnswers: "+sqlC);
        database.execSQL(sqlC, new String[]{answer, year, questionId});
    }


}
