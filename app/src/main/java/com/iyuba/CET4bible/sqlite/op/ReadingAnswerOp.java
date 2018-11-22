package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.sqlite.mode.ReadingAnswer;

import java.util.ArrayList;
import java.util.List;

public class ReadingAnswerOp extends DatabaseService {
    public static final String TABLE_NAME = "reading_answer";
    public static final String PARTTYPE = "PartType";
    public static final String TITLENUM = "TitleNum";
    public static final String QUESINDEX = "QuesIndex";
    public static final String QUESTEXT = "QuesText";
    public static final String ANSWERNUM = "AnswerNum";
    public static final String ANSWERTEXT = "AnswerText";
    public static final String ANSWER = "Answer";
    public static final String ISSINGLE = "isSingle";
    private Context mContext;

    public ReadingAnswerOp(Context context) {
        super(context);
        this.mContext = context;
    }

    public List<ReadingAnswer> findDataByTitleNum(int titleNum) {
        List<ReadingAnswer> readingAnswers = new ArrayList<ReadingAnswer>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(" select " + PARTTYPE + "," +
                TITLENUM + "," + QUESINDEX + "," + QUESTEXT + "," + ANSWERNUM + "," +
                ANSWERTEXT + "," + ANSWER + "," + ISSINGLE + " from " + TABLE_NAME + " where " + TITLENUM + "=" + titleNum +
                " order by " + QUESINDEX, new String[]{});
        while (cursor.moveToNext()) {
            ReadingAnswer readingAnswer = new ReadingAnswer();
            readingAnswer.PartType = cursor.getInt(0);
            readingAnswer.TitleNum = cursor.getInt(1);
            readingAnswer.QuesIndex = cursor.getInt(2);
            readingAnswer.QuesText = cursor.getString(3);
            readingAnswer.AnswerNum = cursor.getInt(4);
            readingAnswer.AnswerText = cursor.getString(5);
            readingAnswer.Answer = cursor.getInt(6);
            readingAnswer.isSingle = cursor.getInt(7);
            readingAnswers.add(readingAnswer);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return readingAnswers;
    }


}
