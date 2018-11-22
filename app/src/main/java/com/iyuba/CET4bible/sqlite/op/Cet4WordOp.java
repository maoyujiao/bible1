package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.sqlite.mode.Cet4RootWord;
import com.iyuba.CET4bible.sqlite.mode.Cet4Word;
import com.iyuba.configation.Constant;

import java.util.ArrayList;

/**
 * 获取单词表数据库
 *
 * @author ct
 * @time 12.9.27
 */
public class Cet4WordOp extends DatabaseService {
    public static final String TABLE_NAME_WORD = getTableName();
    public static final String WORD = "word";
    public static final String STAR = "star";
    public static final String SOUND = "sound";
    public static final String PRON = "pron";
    public static final String DEF = "def";
    public static final String RANDOM = "random";
    public static final String ROOTLIST = "rootWordsList";
    public static final String ROOT = "rootWord";

    public Cet4WordOp(Context context) {
        super(context);
    }

    public synchronized Cet4Word findDataByWord(String word) {
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + WORD + "," + STAR + "," + SOUND + "," + PRON + ","
                        + DEF + " from " + getTableName() + " where " + WORD
                        + "=? order by " + WORD, new String[]{word});
        if (!cursor.isAfterLast()) {
            Cet4Word cet4Word = fillIn(cursor);
            cursor.close();
            closeDatabase(null);
            return cet4Word;
        } else {
            closeDatabase(null);
            return null;
        }
    }

    public ArrayList<Cet4Word> findWordByRoot(int groupflg) {
        ArrayList<Cet4Word> words = new ArrayList<Cet4Word>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select * from " + ROOT + " where groupflg=" + groupflg, new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            words.add(fillRootIn(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public synchronized ArrayList<Cet4Word> findDataByAll() {
        ArrayList<Cet4Word> words = new ArrayList<Cet4Word>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + WORD + "," + STAR + "," + SOUND + "," + PRON + ","
                        + DEF + " from " + getTableName() + " order by "
                        + (BuildConfig.isEnglish ? WORD : PRON), new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            words.add(fillIn(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public synchronized ArrayList<Cet4Word> findDataByLike(String condition) {
        ArrayList<Cet4Word> words = new ArrayList<Cet4Word>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + WORD + "," + STAR + "," + SOUND + "," + PRON + ","
                        + DEF + " from " + getTableName() + " where " + WORD
                        + " LIKE '" + condition + "%'" + " order by " + WORD,

                new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            words.add(fillIn(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public synchronized ArrayList<Cet4Word> findJPDataByLikePron(String condition) {
        ArrayList<Cet4Word> words = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + WORD + "," + STAR + "," + SOUND + "," + PRON + ","
                        + DEF + " from " + getTableName() + " where " + PRON
                        + " LIKE '" + condition + "%'" + " order by " + WORD,

                new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            words.add(fillIn(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public synchronized ArrayList<Cet4Word> findDataByRandom() {
        ArrayList<Cet4Word> words = new ArrayList<Cet4Word>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + WORD + "," + STAR + "," + SOUND + "," + PRON + ","
                        + DEF + " from " + getTableName() + " order by "
                        + RANDOM, new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            words.add(fillIn(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public synchronized ArrayList<Cet4Word> findDataByStar(String condition) {
        ArrayList<Cet4Word> words = new ArrayList<Cet4Word>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + WORD + "," + STAR + "," + SOUND + "," + PRON + ","
                        + DEF + " from " + getTableName() + " where " + STAR
                        + " ='" + condition + "'" + " order by " + (BuildConfig.isEnglish ? WORD : PRON),
                new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            words.add(fillIn(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public ArrayList<Cet4RootWord> findDataByRoot() {
        ArrayList<Cet4RootWord> words = new ArrayList<Cet4RootWord>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select * from " + ROOTLIST, new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            words.add(fillRootListIn(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public synchronized void updateStar(Cet4Word word) {
        importDatabase.openDatabase().execSQL(
                "update " + getTableName() + " set " + STAR + "=? where "
                        + WORD + "=?", new String[]{word.star, word.word});
        closeDatabase(null);
    }

    private Cet4Word fillIn(Cursor cursor) {
        Cet4Word word = new Cet4Word();
        word.word = cursor.getString(0);
        word.star = cursor.getString(1);
        word.sound = cursor.getString(2);
        word.pron = cursor.getString(3);
        word.def = cursor.getString(4);
        return word;
    }

    private Cet4RootWord fillRootListIn(Cursor cursor) {
        Cet4RootWord wordRoot = new Cet4RootWord();
        wordRoot.groupflg = cursor.getInt(0);
        wordRoot.grouptitle = cursor.getString(1);
        return wordRoot;
    }

    private Cet4Word fillRootIn(Cursor cursor) {
        Cet4Word word = new Cet4Word();
        word.word = cursor.getString(1);
        word.pron = cursor.getString(3);
        word.def = cursor.getString(4);
        return word;
    }

    public static String getTableName() {
        if (BuildConfig.isEnglish) {
            return BuildConfig.isCET4 ? "Cet4word" : "cet6word";
        } else {
            return "n" + Constant.APP_CONSTANT.TYPE() + "word";
        }
    }
}
