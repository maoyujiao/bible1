/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.me.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.core.me.sqlite.mode.School;
import com.iyuba.core.sqlite.db.DatabaseService;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class SchoolOp extends DatabaseService {
    public static final String TABLE_NAME_SCHOOL = "university";
    public static final String ID = "id";
    public static final String UNI_ID = "uni_id";
    public static final String PROVINCE = "province";
    public static final String UNI_TYPE = "uni_type";
    public static final String UNI_NAME = "uni_name";

    public SchoolOp(Context context) {
        super(context);
    }

    /**
     * @return
     */
    public synchronized ArrayList<School> findDataByFuzzy(String school) {
        ArrayList<School> schools = new ArrayList<School>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + UNI_ID + "," + UNI_NAME + "," + UNI_TYPE
                        + "," + PROVINCE + " from " + TABLE_NAME_SCHOOL
                        + " WHERE " + UNI_NAME + " LIKE '%" + school
                        + "%' limit 0,60;", new String[]{});
        School temp;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            temp = new School();
            temp.id = cursor.getInt(0);
            temp.school_id = cursor.getInt(1);
            temp.school_name = cursor.getString(2);
            temp.school_type = cursor.getInt(3);
            temp.province = cursor.getString(4);
            schools.add(temp);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return schools;
    }

    /**
     * @return
     */
    public synchronized ArrayList<School> findDataByFuzzy(String school,
                                                          String province) {
        ArrayList<School> schools = new ArrayList<School>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + UNI_ID + "," + UNI_NAME + "," + UNI_TYPE
                        + "," + PROVINCE + " from " + TABLE_NAME_SCHOOL
                        + " WHERE " + UNI_NAME + " LIKE '%" + school
                        + "%' and " + PROVINCE + "='" + province
                        + "' limit 0,40;", new String[]{});
        School temp;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            temp = new School();
            temp.id = cursor.getInt(0);
            temp.school_id = cursor.getInt(1);
            temp.school_name = cursor.getString(2);
            temp.school_type = cursor.getInt(3);
            temp.province = cursor.getString(4);
            schools.add(temp);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return schools;
    }
}
