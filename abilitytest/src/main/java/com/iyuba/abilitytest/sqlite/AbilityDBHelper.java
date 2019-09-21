package com.iyuba.abilitytest.sqlite;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iyuba.abilitytest.entity.AbilityLessonInfoEntity;

import java.util.ArrayList;

/**
 * Created by liuzhenli on 2017/9/13.
 */

public class AbilityDBHelper  {
    private static  AbilityDBHelper instance;
    private SQLiteDatabase mSQLDB;

    private final String BOOK_ID="bookid";
    private final String BOOK_NAME="bookname";
    private final String LESSON_ID = "lessonid";
    private final String LESSON_NAME="lessonname";
    private AbilityDBHelper(){
        mSQLDB=SQLiteDatabase.openOrCreateDatabase(AbilityDBManager.DB_PATH + "/" +AbilityDBManager. DB_NAME,null);
    }

    public static  AbilityDBHelper getInstance(){
        if (instance==null){
            synchronized (AbilityDBHelper.class){
                instance = new AbilityDBHelper();
            }
        }
        return instance;
    }


    public ArrayList<AbilityLessonInfoEntity> getLessonInfosByBookId(String bookId){
        ArrayList<AbilityLessonInfoEntity> lessonInfos=new ArrayList<>();
        String sql ="select * from lessoninfo where bookid = "+bookId+ " order by lessonid desc";
        Cursor cursor = mSQLDB.rawQuery(sql,null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                AbilityLessonInfoEntity lessonInfo = new AbilityLessonInfoEntity();
                lessonInfo.lessonId = cursor.getInt(cursor.getColumnIndex(LESSON_ID));
                lessonInfo.lessonName = cursor.getString(cursor.getColumnIndex(LESSON_NAME));
                lessonInfo.bookId = cursor.getInt(cursor.getColumnIndex(BOOK_ID));
                lessonInfo.bookName =cursor.getString(cursor.getColumnIndex(BOOK_NAME));
                lessonInfos.add(lessonInfo);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return lessonInfos;
    }

}
