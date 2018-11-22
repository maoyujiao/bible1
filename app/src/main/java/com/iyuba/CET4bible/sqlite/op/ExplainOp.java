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
import com.iyuba.core.sqlite.mode.test.CetExplain;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class ExplainOp extends DatabaseService {
    public static final String TABLE_NAME = "explain" + Constant.APP_CONSTANT.TYPE();
    public static final String TESTTIME = "testtime";
    public static final String NUMBER = "number";
    public static final String KEY = "keyss";
    public static final String EXPLAIN = "explains";
    public static final String KNOWLEDGE = "knowledge";

    public ExplainOp(Context context) {
        super(context);
    }

    public ArrayList<CetExplain> selectData(String year) {
        ArrayList<CetExplain> texts = new ArrayList<CetExplain>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + TESTTIME + "," + NUMBER + "," + KEY + "," + EXPLAIN
                        + "," + KNOWLEDGE + " from " + getTableName() + " where "
                        + TESTTIME + "= ?", new String[]{year});
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

        return "explain" + Constant.APP_CONSTANT.TYPE();
    }

    private CetExplain fillIn(Cursor cursor) {
        CetExplain explain = new CetExplain();
        explain.id = cursor.getString(1);
        explain.keys = cursor.getString(2);
        explain.explain = cursor.getString(3);
        explain.knowledge = cursor.getString(4);
        return explain;
    }
}
