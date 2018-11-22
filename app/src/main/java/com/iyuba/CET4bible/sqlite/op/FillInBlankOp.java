package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.sqlite.mode.FillInBlankBean;

import java.util.ArrayList;

/**
 * FillInBlankOp
 *
 * @author wayne
 * @date 2017/12/20
 */
public class FillInBlankOp extends DatabaseService {
    public static final String TABLE_NAME = "fillinblank";
    public static final String YEAR = "year";
    public static final String INDEX = "_index";
    public static final String TITLE = "title";
    public static final String ORIGINAL = "original";
    public static final String WORD = "word";
    public static final String EXPLAIN = "explanation";
    public static final String TRANSLATION = "translation";

    public FillInBlankOp(Context context) {
        super(context);
    }

    public ArrayList<FillInBlankBean> selectData() {
        ArrayList<FillInBlankBean> writes = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select * from " + TABLE_NAME
                        + " GROUP BY " + YEAR + " ORDER BY " + INDEX + " DESC",
                new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            writes.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (writes.size() != 0) {
            return writes;
        }
        return writes;
    }

    public ArrayList<FillInBlankBean> selectData(String number) {
        ArrayList<FillInBlankBean> writes = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select * from " + TABLE_NAME + " where " + YEAR
                        + "=? ORDER BY " + INDEX + " DESC",
                new String[]{number});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            writes.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (writes.size() != 0) {
            return writes;
        }
        return writes;
    }

    private FillInBlankBean fillIn(Cursor cursor) {
        FillInBlankBean bean = new FillInBlankBean();
        bean.year = cursor.getString(cursor.getColumnIndex(YEAR));
        bean.index = cursor.getInt(cursor.getColumnIndex(INDEX));
        bean.title = cursor.getString(cursor.getColumnIndex(TITLE));
        bean.original = cursor.getString(cursor.getColumnIndex(ORIGINAL));
        bean.word = cursor.getString(cursor.getColumnIndex(WORD));
        bean.explanation = cursor.getString(cursor.getColumnIndex(EXPLAIN));
        bean.chinese = cursor.getString(cursor.getColumnIndex(TRANSLATION));
        return bean;
    }
}
