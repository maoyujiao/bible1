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

import com.iyuba.core.sqlite.db.DatabaseService;
import com.iyuba.core.sqlite.mode.UserInfo;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class UserInfoOp extends DatabaseService {
    public static final String TABLE_NAME = "userinfo";
    public static final String USERID = "userid";
    public static final String GENDER = "gender";
    public static final String FANS = "fans";
    public static final String ATTENTIONS = "attentions";
    public static final String NOTIFICATIONS = "notifications";
    public static final String SEETIMES = "seetimes";
    public static final String STATE = "state";
    public static final String VIP = "vip";
    public static final String IYUBI = "iyubi";
    public static final String DEADLINE = "deadline";
    public static final String STUDYTIME = "studytime";
    public static final String POSITION = "position";
    public static final String ICOIN = "icoin";

    public UserInfoOp(Context context) {
        super(context);
        // CreateTabSql();
    }

    /**
     * 插入数据
     */
    public void saveData(UserInfo userInfo) {
        importDatabase.openDatabase()
                .execSQL(
                        "insert or replace into " + TABLE_NAME + " (" + USERID
                                + "," + GENDER + "," + FANS + "," + ATTENTIONS
                                + "," + NOTIFICATIONS + "," + SEETIMES + ","
                                + STATE + "," + VIP + "," + IYUBI + ","
                                + DEADLINE + "," + STUDYTIME + "," + POSITION
                                + "," + ICOIN
                                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        new Object[]{userInfo.uid, userInfo.gender,
                                userInfo.follower, userInfo.following,
                                userInfo.notification, userInfo.views,
                                userInfo.text, userInfo.vipStatus,
                                userInfo.iyubi, userInfo.deadline,
                                userInfo.studytime, userInfo.position,
                                userInfo.icoins});
        closeDatabase(null);
    }

    /**
     * 选择数据
     */
    public UserInfo selectData(String userId) {
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + USERID + "," + GENDER + "," + FANS + ","
                        + ATTENTIONS + "," + NOTIFICATIONS + "," + SEETIMES
                        + "," + STATE + "," + VIP + "," + IYUBI + ","
                        + DEADLINE + "," + STUDYTIME + "," + POSITION + ","
                        + ICOIN + " from " + TABLE_NAME + " where " + USERID
                        + "=?", new String[]{userId});
        if (cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();
            userInfo.uid = cursor.getString(0);
            userInfo.gender = cursor.getString(1);
            userInfo.follower = cursor.getString(2);
            userInfo.following = cursor.getString(3);
            userInfo.notification = cursor.getString(4);
            userInfo.views = cursor.getString(5);
            userInfo.text = cursor.getString(6);
            userInfo.vipStatus = cursor.getString(7);
            userInfo.iyubi = cursor.getString(8);
            userInfo.deadline = cursor.getString(9);
            userInfo.studytime = cursor.getInt(10);
            userInfo.position = cursor.getString(11);
            userInfo.icoins = cursor.getString(12);
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase(null);
            return userInfo;
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return null;
    }

    public int getAccountSize() {
        int size = 0;
        Cursor cursor = importDatabase.openDatabase().query(TABLE_NAME, null, null, null, null, null, null, null);
        if (cursor != null) {
            size = cursor.getCount();
            cursor.close();
        }
        closeDatabase(null);
        return size;
    }


    public int selectStudyTime(String userId) {
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + STUDYTIME + " from " + TABLE_NAME + " where "
                        + USERID + "=?", new String[]{userId});
        if (cursor.moveToNext()) {
            int temp = cursor.getInt(0);
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase(null);
            return temp;
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return 0;
    }

    public synchronized void updataByStudyTime(String userid, int time) {
        importDatabase.openDatabase().execSQL(
                "update " + TABLE_NAME + " set " + STUDYTIME + "=" + time
                        + " where " + USERID + "=?", new String[]{userid});
        closeDatabase(null);
    }

    public synchronized void delete(String userid) {
        importDatabase.openDatabase().execSQL(
                "delete from " + TABLE_NAME + " where " + USERID + "=?",
                new String[]{userid});
        closeDatabase(null);
    }
}
