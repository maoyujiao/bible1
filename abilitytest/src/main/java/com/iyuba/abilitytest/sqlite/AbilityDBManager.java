package com.iyuba.abilitytest.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.iyuba.abilitytest.R;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liuzhenli on 2017/9/13.
 */

public class AbilityDBManager {
    private final int BUFFERSIZE = 1024 * 10;
    public static final String DB_NAME = "abilitydb.sqlite";
    public static final String PACKAGENAME = Constant.PACKAGE_NAME;
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGENAME;


    private Context mContext;

    private SQLiteDatabase mDatabase;
    private int mCurrentVersion;

    public AbilityDBManager(Context context, int currentVersion) {
        this.mContext = context;
        this.mCurrentVersion = currentVersion;
    }


    public void openDatabase() {
        this.mDatabase = openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String path) {
        int versionPre = ConfigManager.Instance().loadInt("ability_db_database_version");
        File file = new File(path);
        if (versionPre < mCurrentVersion) {
            if (file.exists()) {
                file.delete();
                ConfigManager.Instance().putInt("ability_db_database_version", mCurrentVersion);
            }
            return loadDataBase(path);
        } else {
            return loadDataBase(path);
        }
    }

    private SQLiteDatabase loadDataBase(String path) {
        File file = new File(path);
        if (!file.exists()) {
            InputStream is = this.mContext.getResources().openRawResource(R.raw.abilitydb);
            try {
                FileOutputStream fos = new FileOutputStream(path);
                byte[] buffer = new byte[BUFFERSIZE];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file, null);
                return database;

            } catch (FileNotFoundException e) {
                Log.e("Database", "File not found");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Database", "IO exception");
                e.printStackTrace();
            }
        }
        return null;
    }
}
