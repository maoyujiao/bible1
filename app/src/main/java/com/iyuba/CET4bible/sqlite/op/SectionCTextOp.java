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
public class SectionCTextOp extends DatabaseService {
    public static final String TABLE_NAME = "textc" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "testtime";
    public static final String NUMBER = "number";
    public static final String NUMBERINDEX = "numberindex";
    public static final String TIMING1 = "timing1";
    public static final String TIMING2 = "timing2";
    public static final String TIMING3 = "timing3";
    public static final String SENTENCE = "sentence";
    public static final String QWORD = "qwords";


    public SectionCTextOp(Context context) {
        super(context);
    }

    public ArrayList<CetText> selectData(String year) {
        ArrayList<CetText> texts = new ArrayList<CetText>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + NUMBERINDEX + ","
                        + TIMING1 + "," + TIMING2 + "," + TIMING3 + ","
                        + SENTENCE + "," + QWORD

                        + " from " + getTableName()
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

    private String getTableName() {

            return "textc" + Constant.APP_CONSTANT.TYPE();
    }

    private CetText fillIn(Cursor cursor) {
        CetText text = new CetText();
        text.id = cursor.getString(1);
        text.index = cursor.getString(2);
        text.time = cursor.getString(3);
        text.time2 = cursor.getString(4);
        text.time3 = cursor.getString(5);
        text.sentence = cursor.getString(6);
        text.qwords = cursor.getString(7);

        return text;
    }
}
