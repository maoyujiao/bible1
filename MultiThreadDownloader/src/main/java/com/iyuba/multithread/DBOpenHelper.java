package com.iyuba.multithread;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ݿ������
 *
 * @author Administrator
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "down.db";
    private static final int VERSION = 1;

    /**
     * ������
     *
     * @param context
     */
    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, fileid INTEGER, downurl varchar(100), downpath varchar(100), threadid INTEGER, downlength INTEGER, titallength INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS filedownlog");
        onCreate(db);
    }
}