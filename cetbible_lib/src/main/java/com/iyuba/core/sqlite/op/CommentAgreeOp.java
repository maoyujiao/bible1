package com.iyuba.core.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.core.sqlite.db.DatabaseService;


public class CommentAgreeOp extends DatabaseService {

    public static final String TABLE_NAME = "commentonagree";
    public static final String UID = "uid";
    public static final String COMMENTID = "commentid";
    public static final String AGREE = "agree";
    public CommentAgreeOp(Context context) {
        super(context);
    }

    public synchronized int findDataByAll(String commentid, String uid) {

        if (uid == null) return 0;
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + COMMENTID + " , " + UID + "," + AGREE + " from "
                        + TABLE_NAME + " where " + COMMENTID + " = ? AND "
                        + UID + " = ?", new String[]{commentid, uid});
        if (cursor != null && cursor.getCount() == 0) {
            cursor.close();
            return 0;
        } else {
            int temp = 0;
            cursor.moveToFirst();
            if (cursor.getString(2).equals("against")) {
                temp = 2;
            } else {
                temp = 1;
            }
            cursor.close();
            return temp;
        }
    }

    public synchronized void saveData(String commentid, String uid, String agree) {
        if (commentid != null && uid != null) {
            importDatabase.openDatabase().execSQL(
                    "insert or replace into " + TABLE_NAME + " (" + COMMENTID
                            + "," + UID + "," + AGREE + ") values(?,?,?)",
                    new Object[]{commentid, uid, agree});
            closeDatabase(null);
        }
    }

}
