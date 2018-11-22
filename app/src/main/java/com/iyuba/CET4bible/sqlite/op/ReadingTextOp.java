package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.sqlite.mode.ReadingText;

import java.util.ArrayList;
import java.util.List;

public class ReadingTextOp extends DatabaseService {
    public static final String TABLE_NAME = "reading_text";
    public static final String PARTTYPE = "PartType";
    public static final String TITLENUM = "TitleNum";
    public static final String TITLENAME = "TitleName";
    public static final String SENINDEX = "SenIndex";
    public static final String SENTENCE = "Sentence";
    public static final String Timing = "Timing";
    private Context mContext;

    public ReadingTextOp(Context context) {
        super(context);
        this.mContext = context;
    }

    public List<ReadingText> findDataByTitleNum(int titleNum) {
        List<ReadingText> readingTexts = new ArrayList<ReadingText>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(" select " + PARTTYPE + "," +
                TITLENUM + "," + TITLENAME + "," + SENINDEX + "," + SENTENCE + "," +
                Timing + " from " + TABLE_NAME + " where " + TITLENUM + "=" + titleNum +
                " order by " + SENINDEX, new String[]{});
        while (cursor.moveToNext()) {
            ReadingText readingText = new ReadingText();
            readingText.PartType = cursor.getInt(0);
            readingText.TitleNum = cursor.getInt(1);
            readingText.TitleName = cursor.getString(2);
            readingText.SenIndex = cursor.getInt(3);
            readingText.Sentence = cursor.getString(4);
            readingText.Timing = cursor.getInt(5);
            readingTexts.add(readingText);
        }
        cursor.close();
        closeDatabase(null);
        return readingTexts;
    }

}
