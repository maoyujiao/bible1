package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.configation.Constant;
import com.iyuba.core.sqlite.mode.test.CetText;

import java.util.ArrayList;

public class NewTypeTextBOp extends DatabaseService {
    public static final String TABLE_NAME = "newtype_textb" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "TestTime";
    public static final String NUMBER = "Number";
    public static final String NUMBERINDEX = "NumberIndex";
    public static final String TIMING = "Timing";
    public static final String SENTENCE = "Sentence";
    public static final String SOUND = "Sound";
    public static final String SEX = "sex";
    public static final String GOOD = "good";
    public static final String BAD = "bad";
    public static final String SCORE = "score";
    public static final String URL = "record_url";

    public NewTypeTextBOp(Context context) {
        super(context);
    }

    public ArrayList<CetText> selectData(String year) {
        ArrayList<CetText> texts = new ArrayList<CetText>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + NUMBERINDEX + ","
                        + TIMING + "," + SENTENCE + "," + SOUND
                        +"," + GOOD + "," + BAD + "," + SCORE + "," + URL
                        + "," + SEX + " from "
                        + getTableName() + " where " + TESTTIME + "= ?",
                new String[]{year});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            texts.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (texts.size() != 0) {
            cursor.close();
            return texts;
        }
        cursor.close();
        return null;
    }

    private String getTableName() {
            return "newtype_textb" + Constant.APP_CONSTANT.TYPE();
    }

    public ArrayList<CetText> selectData(String year, String id) {
        ArrayList<CetText> texts = new ArrayList<CetText>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + NUMBERINDEX + ","
                        + TIMING + "," + SENTENCE + "," + SOUND + "," + SEX
                        +"," + GOOD + "," + BAD + "," + SCORE + "," + URL
                        + " from "
                        + getTableName() + " where " + TESTTIME + "= ? and "
                        + NUMBER + "= ?", new String[]{year, id});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            texts.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (texts.size() != 0) {
            return texts;
        }
        return null;
    }

    private CetText fillIn(Cursor cursor) {
        CetText text = new CetText();
        text.testTime = cursor.getString(0);
        text.id = cursor.getString(1);
        text.index = cursor.getString(2);
        text.time = cursor.getString(3);
        text.sentence = cursor.getString(4);
        text.sex = cursor.getString(6);
        text.good = cursor.getString(7);
        text.bad = cursor.getString(8);
        text.score = cursor.getString(9);
        text.record_url = cursor.getString(10);
        return text;
    }
}
