package com.iyuba.CET4bible.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.db.DBOpenHelper;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 瀵煎叆鏁版嵁搴�
 *
 * @author chentong
 */
public class ImportDatabase {
    private static final String DB_NAME = "bible_database.sqlite"; // 保存的数据库文件名
    public static DBOpenHelper mdbhelper;
    private static SQLiteDatabase database = null;
    private static Context mContext;
    private final int BUFFER_SIZE = 400000;
    private String PACKAGE_NAME;
    private String DB_PATH;
    private int lastVersion, currentVersion;

    public ImportDatabase(Context context) {
        mContext = context;
        mdbhelper = new DBOpenHelper(context);
    }

    public String getDBPath() {
        return DB_PATH + "/" + DB_NAME;
    }

    public void setPackageName(String packageName) {
        PACKAGE_NAME = packageName;
        DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + "/" + PACKAGE_NAME + "/" + "databases";
    }

    public void setVersion(int lastVeision, int curVersion) {
        this.lastVersion = lastVeision;
        this.currentVersion = curVersion;
    }

    public synchronized SQLiteDatabase openDatabase() {
        database = mdbhelper.getWritableDatabase();
        return database;
    }

    /**
     * 淇敼鍚庯紝姝ゅ嚱鏁颁綔涓虹涓�杩愯鏃剁殑鍒涘缓鏁版嵁搴撳嚱鏁�?
     *
     * @param dbfile
     */
    public synchronized void openDatabase(String dbfile) {
        lastVersion = ConfigManager.Instance().loadInt("database_version");
        File database = new File(dbfile);
        if (currentVersion > lastVersion) {
            if (database.exists()) {
                database.delete();
            }
            loadDataBase(dbfile);
            ConfigManager.Instance().putInt("database_version", currentVersion);
        }
    }

    public void closeDatabase() {

    }

    /**
     * 灏嗘暟鎹簱鏂囦欢鎷疯礉鍒伴渶瑕佺殑浣嶇�?
     *
     * @param dbfile
     */
    private void loadDataBase(String dbfile) {
        try {
            InputStream is = mContext.getResources().openRawResource(
                    BuildConfig.isEnglish ? (BuildConfig.isCET4 ? R.raw.cet4 : R.raw.cet6) : R.raw.n123);
            BufferedInputStream bis = new BufferedInputStream(is);
            if (!(new File(DB_PATH).exists())) {
                new File(DB_PATH).mkdir();
            }
            FileOutputStream fos = new FileOutputStream(dbfile);
            BufferedOutputStream bfos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = bis.read(buffer)) > 0) {
                bfos.write(buffer, 0, count);
            }
            bfos.close();
            fos.close();
            bis.close();
            is.close();
            LogUtils.e("ImportDatabase", "ImportDatabase数据库拷贝成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mdbhelperClose() {
        if (mdbhelper != null) {
            mdbhelper.close();
        }
    }
}
