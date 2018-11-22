package com.iyuba.CET4bible.sqlite.db;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.ImportDatabase;


/**
 * 鏁版嵁搴撴湇鍔�
 *
 * @author chentong
 */
public class DatabaseService {
    protected static ImportDatabase importDatabase;

    protected DatabaseService(Context context) {
        importDatabase = new ImportDatabase(context);
    }

    /**
     * 鍒犻櫎琛�? *
     *
     * @param tableName
     */
    public void dropTable(String tableName) {
        importDatabase.openDatabase().execSQL(
                "DROP TABLE IF EXISTS " + tableName);

    }

    /**
     * 鍏抽棴鏁版嵁搴�
     *
     * @param DatabaseName
     */
    public void closeDatabase(String DatabaseName) {
    }

    /**
     * 鍒犻櫎鏁版嵁搴撹〃鏁版嵁
     *
     * @param tableName
     * @param id
     */
    public void deleteItemData(String tableName, Integer id) {
        importDatabase.openDatabase().execSQL(
                "delete from " + tableName + " where _id=?",
                new Object[]{id});
    }

    /**
     * 鍒犻櫎鏁版嵁搴撹〃鏁版嵁
     *
     * @param tableName
     * @param ids       ids鏍煎紡涓�?,"","",""
     */
    public void deleteItemsData(String tableName, String ids) {
        importDatabase.openDatabase().execSQL(
                "delete from " + tableName + " where voaid in (" + ids + ")",
                new Object[]{});
    }

    /**
     * 鑾峰彇鏁版嵁搴撹〃椤规暟
     *
     * @param tableName
     * @return
     */
    public long getDataCount(String tableName) {
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select count(*) from " + tableName, null);
        cursor.moveToFirst();
        return cursor.getLong(0);
    }

    /**
     * 鍏抽棴鏁版嵁搴撴湇鍔�?
     */
    public void close() {
    }

}
