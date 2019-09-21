package com.iyuba.core.me.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.iyuba.biblelib.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * http://www.cnblogs.com/xiaowenji/archive/2011/01/03/1925014.html
 */
public class ZDBManager {
    public static final String DB_NAME = "zzaidb.sqlite"; // 数据库名字
    // public static final String PACKAGE_NAME =
    // "com.iyuba.toelflistening";//托福听力
    public static final String PACKAGE_NAME = Constant.PACKAGE_NAME;// 四级听力
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;
    private final int BUFFER_SIZE = 3000000;
    private SQLiteDatabase database;
    private Context context;

    private int lastVersion, currentVersion;

    public ZDBManager(Context context) {
        this.context = context;
    }

    public void setVersion(int lastVeision, int curVersion) {
        this.lastVersion = lastVeision;
        this.currentVersion = curVersion;
    }

    /**
     * 打开数据库
     */
    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    /**
     * 打开数据库 根据版本判断是否需要更新
     *
     * @param dbfile
     */
    public SQLiteDatabase openDatabase(String dbfile) {
        lastVersion = ConfigManager.Instance().loadInt("zdb_database_version");
        File database = new File(dbfile);
        if (currentVersion > lastVersion) {
            if (database.exists()) {
                database.delete();
            }

            ConfigManager.Instance().putInt("zdb_database_version",
                    currentVersion);

            return loadDataBase(dbfile);
        } else {
            return loadDataBase(dbfile);
        }
    }

    /**
     * @param dbfile
     * @return
     */
    private SQLiteDatabase loadDataBase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) {
                InputStream is = this.context.getResources().openRawResource(R.raw.zzaidb);
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        this.database.close();
    }
}