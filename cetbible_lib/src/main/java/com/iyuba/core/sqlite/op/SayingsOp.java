/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.configation.Constant;
import com.iyuba.core.sqlite.db.DatabaseService;
import com.iyuba.core.sqlite.mode.Sayings;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class SayingsOp extends DatabaseService {
    public static final String TABLE_NAME = Constant.APP_CONSTANT.isEnglish() ? "sayings" : "sayings_jp";
    public static final String ID = "id";
    public static final String ENGLISH = "english";
    public static final String CHINESE = "chinese";

    public SayingsOp(Context context) {
        super(context);

        // CreateTabSql();
    }

    public void CreateTabSql() {

        closeDatabase(null);
    }

    /**
     * @return
     */
    public synchronized Sayings findDataById(int id) {
        Sayings sayings = new Sayings();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + ENGLISH + ", " + CHINESE + " from "
                        + TABLE_NAME + " where " + ID + "=? ",
                new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            sayings.id = cursor.getInt(0);
            sayings.english = cursor.getString(1);
            sayings.chinese = cursor.getString(2);
            closeDatabase(null);
            return sayings;
        }
        closeDatabase(null);
        return null;
    }
}
