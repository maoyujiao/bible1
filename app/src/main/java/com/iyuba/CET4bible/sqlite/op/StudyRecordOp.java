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

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.core.sqlite.mode.StudyRecord;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class StudyRecordOp extends DatabaseService {
    public static final String TABLE_NAME = "studyrecord";
    public static final String VOAID = "voaid";
    public static final String STARTTIME = "starttime";
    public static final String ENDTIME = "endtime";
    public static final String FLAG = "flag";
    public static final String LESSON = "lesson";

    public StudyRecordOp(Context context) {
        super(context);
        // CreateTabSql();
    }

    /**
     * 插入数据
     */
    public void saveData(StudyRecord studyRecord) {
        importDatabase.openDatabase().execSQL(
                "insert or replace into " + TABLE_NAME + " (" + VOAID + ","
                        + STARTTIME + "," + ENDTIME + "," + FLAG + "," + LESSON
                        + ") values(?,?,?,?,?)",
                new String[]{studyRecord.voaid, studyRecord.starttime,
                        studyRecord.endtime, studyRecord.flag,
                        studyRecord.lesson});
        closeDatabase(null);
    }

    /**
     * 选择数据
     */
    public void deleteData(StudyRecord studyRecord) {
        importDatabase.openDatabase().execSQL(
                "delete from " + TABLE_NAME + " where " + VOAID + "=? and "
                        + STARTTIME + "=?",
                new String[]{studyRecord.voaid, studyRecord.starttime});
        closeDatabase(null);
    }

    /**
     * 选择数据
     */
    public void delete() {
        importDatabase.openDatabase().execSQL("delete from " + TABLE_NAME);
        closeDatabase(null);
    }

    public ArrayList<StudyRecord> selectData() {
        ArrayList<StudyRecord> records = new ArrayList<StudyRecord>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + VOAID + "," + STARTTIME + "," + ENDTIME + ","
                        + FLAG + "," + LESSON + " from " + TABLE_NAME,
                new String[]{});
        StudyRecord studyRecord;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            studyRecord = new StudyRecord();
            studyRecord.voaid = cursor.getString(0);
            studyRecord.starttime = cursor.getString(1);
            studyRecord.endtime = cursor.getString(2);
            studyRecord.flag = cursor.getString(3);
            studyRecord.lesson = cursor.getString(4);
            records.add(studyRecord);
        }
        closeDatabase(null);
        if (records.size() != 0) {
            return records;
        }
        return null;
    }

    public boolean hasData() {
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + VOAID + " from " + TABLE_NAME, new String[]{});
        if (cursor.getCount() != 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
}
