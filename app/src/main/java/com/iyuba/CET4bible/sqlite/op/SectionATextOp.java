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
import com.iyuba.core.sqlite.mode.test.CetText;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class SectionATextOp extends DatabaseService {
    public static final String TABLE_NAME = "texta" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "testtime";
    public static final String NUMBER = "number";
    public static final String NUMBERINDEX = "numberindex";
    public static final String TIMING = "timing";
    public static final String SENTENCE = "sentence";
    public static final String SEX = "sex";
    public static final String SOUND = "sound";

    public SectionATextOp(Context context) {
        super(context);
    }

    public ArrayList<CetText> selectData(String year) {
        ArrayList<CetText> texts = new ArrayList<CetText>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + NUMBERINDEX + ","
                        + TIMING + "," + SENTENCE + "," + SEX + "," + SOUND
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

    public ArrayList<CetText> selectData(String year, String id) {
        ArrayList<CetText> texts = new ArrayList<CetText>();
        Cursor cursor = importDatabase.openDatabase()
                .rawQuery(
                        "select " + TESTTIME + "," + NUMBER + "," + NUMBERINDEX
                                + "," + TIMING + "," + SENTENCE + "," + SEX
                                + "," + SOUND + " from " + getTableName()
                                + " where " + TESTTIME + "= ? and " + NUMBER
                                + "= ?", new String[]{year, id});
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
            return "texta" + Constant.APP_CONSTANT.TYPE();
    }

    private CetText fillIn(Cursor cursor) {
        CetText text = new CetText();
        text.id = cursor.getString(1);
        text.index = cursor.getString(2);
        text.time = cursor.getString(3);
        text.sentence = cursor.getString(4);
        text.sex = cursor.getString(5);
        return text;
    }
}
