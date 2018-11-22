package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.iyuba.core.sqlite.db.DatabaseService;

public abstract class BaseOP extends DatabaseService {
    public BaseOP(Context context) {
        super(context);

        createConfigTable();
    }

    /**
     * 鍒涘缓琛�?
     */
    public void createConfigTable() {
        importDatabase.openDatabase().execSQL(setCreateTabSql());
    }

    public abstract String setCreateTabSql();

    public SQLiteDatabase getDatabase() {
        return importDatabase.openDatabase();
    }

}
