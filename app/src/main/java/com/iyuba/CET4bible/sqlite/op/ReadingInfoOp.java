package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.sqlite.mode.PackInfo;

import java.util.ArrayList;
import java.util.List;

public class ReadingInfoOp extends DatabaseService {
    public final static String TABLE_NAME = "TitleInfo";
    public final static String PARTTYPE = "PartType";
    public final static String TITLENUM = "TitleNum";
    public final static String QUESNUM = "QuesNum";
    public final static String RIGHTNUM = "RightNum";
    public final static String STUDYTIME = "StudyTime";
    public final static String PACKNAME = "PackName";
    public final static String TITLENAME = "TitleName";
    private Context mContext;

    public ReadingInfoOp(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 查询包名
     *
     * @return
     */
    public List<String> findPackName() {
        List<String> packNames = new ArrayList<String>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(" select  distinct " + PACKNAME +
                " from " + TABLE_NAME + " order by " + TITLENUM + " DESC", new String[]{});
        while (cursor.moveToNext()) {
            packNames.add(cursor.getString(0));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return packNames;
    }

    public List<PackInfo> findDataByPackName(String packName) {
        List<PackInfo> packInfos = new ArrayList<PackInfo>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(" select " +
                PARTTYPE + "," + TITLENUM + "," + QUESNUM + "," + TITLENAME + "," + PACKNAME + " from " + TABLE_NAME +
                " where " + PACKNAME + "=" + "'" + packName + "'" + " order by " + PARTTYPE, new String[]{});
        while (cursor.moveToNext()) {
            PackInfo packInfo = new PackInfo();
            packInfo.PartType = cursor.getInt(0);
            packInfo.TitleNum = cursor.getInt(1);
            packInfo.QuesNum = cursor.getInt(2);
            packInfo.TitleName = cursor.getString(3);
            packInfo.PackName = cursor.getString(4);
            packInfos.add(packInfo);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return packInfos;
    }

    public List<PackInfo> findAll() {
        List<PackInfo> packInfos = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(" select " +
                PARTTYPE + "," + TITLENUM + "," + QUESNUM + "," + TITLENAME + "," + PACKNAME + " from " + TABLE_NAME +
                " order by " + PARTTYPE, new String[]{});
        while (cursor.moveToNext()) {
            PackInfo packInfo = new PackInfo();
            packInfo.PartType = cursor.getInt(0);
            packInfo.TitleNum = cursor.getInt(1);
            packInfo.QuesNum = cursor.getInt(2);
            packInfo.TitleName = cursor.getString(3);
            packInfo.PackName = cursor.getString(4);
            packInfos.add(packInfo);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return packInfos;
    }
}
