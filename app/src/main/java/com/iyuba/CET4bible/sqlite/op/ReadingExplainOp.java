package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.sqlite.mode.ReadingExplain;

import java.util.ArrayList;
import java.util.List;

public class ReadingExplainOp extends DatabaseService {
    private static final String TABLE_NAME = "reading_explain";
    private static final String PARTTYPE = "PartType";
    private static final String TITLENUM = "TitleNum";
    private static final String EXPLAIN = "Explain";
    private static final String QUESINDEX = "QuesIndex";
    private Context mContent;

    public ReadingExplainOp(Context context) {
        super(context);
        this.mContent = context;

    }

    public List<ReadingExplain> findDataByTitleNum(int titleNum) {
        List<ReadingExplain> readingExplains = new ArrayList<ReadingExplain>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(" select " + PARTTYPE + "," + TITLENUM +
                "," + EXPLAIN + "," + QUESINDEX + " from " + TABLE_NAME + " where " + TITLENUM + "=" + titleNum
                + " order by " + QUESINDEX, new String[]{});
        while (cursor.moveToNext()) {
            ReadingExplain readingExplain = new ReadingExplain();
            readingExplain.PartType = cursor.getInt(0);
            readingExplain.TitleNum = cursor.getInt(1);
            readingExplain.Explain = cursor.getString(2);
            readingExplain.QuesIndex = cursor.getInt(3);
            readingExplains.add(readingExplain);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return readingExplains;
    }

}
