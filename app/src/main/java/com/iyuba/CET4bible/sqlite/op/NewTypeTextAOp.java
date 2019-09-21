package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.widget.subtitle.Subtitle;
import com.iyuba.configation.Constant;
import com.iyuba.core.sqlite.mode.test.CetText;

import java.util.ArrayList;

public class NewTypeTextAOp extends DatabaseService {

    public static final String TABLE_NAME = "newtype_texta" + Constant.APP_CONSTANT.TYPE();
    public static final String NEWTYPE_TABLE_TEXTA = "newtype_texta" + Constant.APP_CONSTANT.TYPE();
    public static final String NEWTYPE_TABLE_TEXTB = "newtype_textb" + Constant.APP_CONSTANT.TYPE();
    public static final String NEWTYPE_TABLE_TEXTC = "newtype_textc" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "TestTime";
    public static final String NUMBER = "Number";
    public static final String NUMBERINDEX = "NumberIndex";
    public static final String TIMING = "Timing";
    public static final String SENTENCE = "Sentence";
    public static final String SEX = "Sex";
    public static final String SOUND = "Sound";
    public static final String GOOD = "good";
    public static final String BAD = "bad";
    public static final String SCORE = "score";
    public static final String URL = "record_url";

    public NewTypeTextAOp(Context context) {
        super(context);
    }

    public ArrayList<CetText> selectData(String year) {
        ArrayList<CetText> texts = new ArrayList<CetText>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + NUMBERINDEX + ","
                        + TIMING + "," + SENTENCE + "," + SEX + "," + SOUND + ","
                        + GOOD + "," + BAD + "," + SCORE + "," + URL
                        + " from " + getTableName() + " where " + TESTTIME + "= ?",
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

    public ArrayList<CetText> selectData(String year, String id) {
        ArrayList<CetText> texts = new ArrayList<CetText>();
        Cursor cursor = importDatabase.openDatabase()
                .rawQuery(
                        "select " + TESTTIME + "," + NUMBER + "," + NUMBERINDEX
                                + "," + TIMING + "," + SENTENCE + "," + SEX
                                + "," + SOUND + ","
                                + GOOD + "," + BAD + "," + SCORE + "," + URL
                                + " from " + getTableName()
                                + " where " + TESTTIME + "= ? and " + NUMBER
                                + "= ?", new String[]{year, id});
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
        return "newtype_texta" + Constant.APP_CONSTANT.TYPE();
    }

    private CetText fillIn(Cursor cursor) {
        CetText text = new CetText();
        text.testTime  =  cursor.getString(0);
        text.id = cursor.getString(1);
        text.index = cursor.getString(2);
        text.time = cursor.getString(3);
        text.sentence = cursor.getString(4);
        text.sex = cursor.getString(5);
        text.good = cursor.getString(6);
        text.bad = cursor.getString(7);
        text.score = cursor.getString(8);
        text.record_url = cursor.getString(9);
        return text;
    }


    public Subtitle getRecordingResult(Subtitle subTextAB, String type) {
        SQLiteDatabase mDB;
        String tablename1 = NEWTYPE_TABLE_TEXTA;
        if ("A".equals(type)) {
            tablename1 = NEWTYPE_TABLE_TEXTA;
        } else if ("B".equals(type)) {
            tablename1 = NEWTYPE_TABLE_TEXTB;
        } else if ("C".equals(type)) {
            tablename1 = NEWTYPE_TABLE_TEXTC;
        }
        String[] goodsString = new String[]{};
        String[] badsString = new String[]{};
        mDB = importDatabase.openDatabase();
        String querySQL = "select * from " + tablename1 + " where " + NUMBERINDEX + " = " + subTextAB.NumberIndex +
                " and " + NUMBER + " = " + subTextAB.Number +
                " and " + TESTTIME + " = " + subTextAB.testTime;
        Cursor cur = mDB.rawQuery(querySQL, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            subTextAB.goodList = new ArrayList<>();
            subTextAB.badList = new ArrayList<>();
            subTextAB.record_url = cur.getString(cur.getColumnIndex(URL));
            if (!TextUtils.isEmpty(cur.getString(cur.getColumnIndex(SCORE)))) {
                subTextAB.readScore = Integer.parseInt(cur.getString(cur.getColumnIndex(SCORE)));
                subTextAB.isRead = true;
            }
            String goods = cur.getString(cur.getColumnIndex(GOOD));
            String bads = cur.getString(cur.getColumnIndex(BAD));
            // 转义字符
            if (!TextUtils.isEmpty(goods))
                goodsString = goods.split("\\+");
            if (!TextUtils.isEmpty(bads))
                badsString = bads.split("\\+");
            try {
                for (int i = 0; i < goodsString.length; i++) {
                    subTextAB.goodList.add(Integer.valueOf(goodsString[i]));
                }
                for (int i = 0; i < badsString.length; i++) {
                    subTextAB.badList.add(Integer.valueOf(badsString[i]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subTextAB;
    }


    public void writeRecordingData(Subtitle subTextAB, String type) {
        String tablename1 = NEWTYPE_TABLE_TEXTA;
        String tablename2 = NEWTYPE_TABLE_TEXTB;
        String tablename3 = NEWTYPE_TABLE_TEXTC;
        SQLiteDatabase mDB ;

        mDB = importDatabase.openDatabase();
        String goodString = "";
        for (int i = 0; i < subTextAB.goodList.size(); i++) {
            goodString += subTextAB.goodList.get(i);
            if (i + 1 != subTextAB.goodList.size()) {
                goodString += "+";
            }
        }
        String badString = "";
        for (int i = 0; i < subTextAB.badList.size(); i++) {
            badString += subTextAB.badList.get(i);
            if (i + 1 != subTextAB.badList.size()) {
                badString += "+";
            }
        }

        String tablename = tablename1;
        if (TextUtils.equals(type, "A")) {
            tablename = tablename1;
        } else if (TextUtils.equals(type, "B")) {
            tablename = tablename2;
        } else if (TextUtils.equals(type, "C")) {
            tablename = tablename3;
        }
        String sql = "UPDATE " + tablename + " SET " + BAD + " = \"" + badString + "\"," +
                GOOD + " = \"" + goodString + "\" ," +
                URL + " = \"" + subTextAB.record_url + "\" ," +
                SCORE + " = \"" + subTextAB.readScore + "\"" +
                " WHERE " + NUMBER + " = " + subTextAB.Number +
                " AND " + TESTTIME + " = " + subTextAB.testTime +
                " AND " + NUMBERINDEX + " = " + subTextAB.NumberIndex;
        Log.d("diao", sql);
        mDB.execSQL(sql);

    }

    public String getSentence(int year, String paraid,
                              String idIndex, String type) {
        SQLiteDatabase mDB ;
        String s = "";
        mDB = importDatabase.openDatabase();
//		ArrayList<Explain> explains = new ArrayList<Explain>();

        String tb = NEWTYPE_TABLE_TEXTA;
        if ("A".equals(type)) {
            tb = NEWTYPE_TABLE_TEXTA;
        } else if ("B".equals(type)) {
            tb = NEWTYPE_TABLE_TEXTB;
        } else {
            tb = NEWTYPE_TABLE_TEXTC;
        }
        Cursor cur = null;

        // String sql =
        // "select *  from "+tablename+" where TestTime = "+year+" order by " +
        // FIELD_NUMBER + " asc";
        String sql = "select *  from " + tb + " where TestTime = "
                + year + " and Number = " + paraid + " and NumberIndex = " + idIndex + " order by "
                + NUMBER + " asc";
        Log.d("diao",sql);

        try {
            cur = mDB.rawQuery(sql, null);

            int iC = cur.getCount();
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                s = cur.getString(cur.getColumnIndex("Sentence"));
            }

            cur.close();
            //mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                //mDB.close();
            }
        }
        return s;
    }


}
