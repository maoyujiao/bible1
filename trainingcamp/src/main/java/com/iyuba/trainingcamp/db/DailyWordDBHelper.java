package com.iyuba.trainingcamp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.util.TimeUtils;

import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.BBCInfoBean;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.bean.VoaInfoBean;
import com.iyuba.trainingcamp.bean.WordHistory;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @class describe
 */
public class DailyWordDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    Context context;

    public final static String TB_SCHEDULE = "trainingschedule";
    public final static String TB_VOAINFO = "voainfo";
    public final static String TB_KEYWORDS = "keyword";
    public final static String TB_SENTENCE = "sentence";

    public final static String DB_NAME = "vipwords.sqlite";

    private SQLiteDatabase mDB;
    private Context mContext;

    //VoaInfoTable
    private String COL_DescCn = "DescCn";
    private String COL_CreatTime = "CreatTime";
    private String COL_Category = "Category";
    private String COL_Title_cn = "Title_cn";
    private String COL_Title = "Title";
    private String COL_Sound = "Sound";
    private String COL_PublishTime = "PublishTime";
    private String COL_HotFlg = "HotFlg";
    private String COL_Pic = "Pic";
    private String COL_VoaId = "VoaId";
    private String COL_Url = "Url";
    private String COL_ReadCount = "ReadCount";



    //SentenceTable
    private String SEN_en = "en";
    private String SEN_cn = "cn";
    private String SEN_id = "id";
    private String SEN_score = "score";

    // ColumnIndex
    // 0: lessonid  1: day  2:step  3:check_result
    String createDailyRecordsSql = "CREATE TABLE " + TB_SCHEDULE + " (userid varchar(255) , lessonid varchar(255),day varchar(255)," +
            "step varchar(255)," +
            "word_score varchar(255)," +
            "sentence_score varchar(255)," +
            "exam_score varchar(255))";

    String createWordsSql = "CREATE TABLE keyword (lessonid varchar(255),word varchar(255)," +
            "word_cn varchar(255)," +
            "check_result varchar(255))";

    String createVoaInfoSql = "CREATE TABLE voainfo (" + COL_DescCn + " varchar(255)," +
            COL_CreatTime + " varchar(255)," +
            COL_Category + " varchar(255)," +
            COL_Title_cn + " varchar(255)," +
            COL_Title + " varchar(255)," +
            COL_Sound + " varchar(255)," +
            COL_PublishTime + " varchar(255)," +
            COL_HotFlg + " varchar(255)," +
            COL_Pic + " varchar(255)," +
            COL_VoaId + " varchar(255)," +
            COL_Url + " varchar(255)," +
            COL_ReadCount + " varchar(255))";

    String createSentence = "CREATE TABLE sentence (" + SEN_id + " varchar(255)," +
            SEN_en + " varchar(255)," +
            SEN_cn + " varchar(255)," +
            SEN_score + " varchar(255))";


    public DailyWordDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DailyWordDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DailyWordDBHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

        /**数据库名**/

    }

    /**
     * @return 功能：打开数据库
     */
    public SQLiteDatabase openDatabase() {
        return SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/"
                + DB_NAME, null);
    }


    public void createtable(SQLiteDatabase mDB) {
        this.mDB = mDB;

        mDB.execSQL(createWordsSql);
        mDB.execSQL(createDailyRecordsSql);
        mDB.execSQL(createVoaInfoSql);
        mDB.execSQL(createSentence);

    }


    public List<String> getWordDetail(String word) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * from keyword where word = " + word;
        Cursor cursor = null;
        cursor = mDB.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int n = cursor.getColumnCount();
            for (int i = 0; i < n; i++) {
                list.add(cursor.getString(i));

            }
        }
        return list;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createtable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("diao", "upgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TB_VOAINFO);
//        db.execSQL("DROP TABLE IF EXISTS " + TB_SCHEDULE);
//        db.execSQL("DROP TABLE IF EXISTS " + TB_KEYWORDS);
//        db.execSQL(createWordsSql);
        db.execSQL(createVoaInfoSql);
//        db.execSQL(createDailyRecordsSql);

    }

    //    public void insertData(LearningContent content) {
//        String sql = "INSERT INTO keyword VALUES ("
//                + content.en+ ","
//                + content.pro+ ","
//                + content.cn+ ","
//                + content.phrase1+ ","
//                + content.phrase1_cn+ ","
//                + content.phrase2+ ","
//                + content.phrase2_cn+ ","
//                + content.sentence+ ","
//                + content.sentence_cn +")";
//        mDB.execSQL(sql);
//    }
    public void saveRecords(String lessonid, List<LearningContent> wordContent) {
        String sql = "delete from keyword where lessonid = " + lessonid;
        getWritableDatabase().execSQL(sql);
        String sql1 = "insert into  keyword (lessonid ,word,word_cn,check_result) values" +
                "(?,?,?,?)";
        for (int i = 0; i < wordContent.size(); i++) {
            getWritableDatabase().execSQL(sql1, new Object[]{lessonid, wordContent.get(i).en, wordContent.get(i).cn, wordContent.get(i).checkPassed + ""});
        }
    }

    public List<WordHistory> getWordHistory(String lessonid) {
        List<WordHistory> histories = new ArrayList<>();
        String sql = "select * from keyword where lessonid = " + lessonid;
        Cursor cursor;
        cursor = getWritableDatabase().rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            Log.d("diao", "getWordHistory: " + cursor.getCount());
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Log.d("diao", "WordHistory: " + i);

                WordHistory history = new WordHistory();
                history.setCn(cursor.getString(cursor.getColumnIndex("word_cn")));
                history.setEn(cursor.getString(cursor.getColumnIndex("word")));
                history.setPassed(cursor.getString(cursor.getColumnIndex("check_result")));
                histories.add(history);
                cursor.moveToNext();
            }
        }
        return histories;
    }

//    public static String getOldDate(int distanceDay, String day) {
//        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
//        Date beginDate = new Date(Long.parseLong(com.iyuba.trainingcamp.utils.TimeUtils.formatDateToMills(day)));
//        Calendar date = Calendar.getInstance();
//        date.setTime(beginDate);
//        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
//        Date endDate = null;
//        try {
//            endDate = dft.parse(dft.format(date.getTime()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return dft.format(endDate);
//    }

    //添加新的lessonid到数据库中
    public boolean writeDataToSchedule(String userid, String lessonid, String date ) {
        int i = 0;
        String sql1 = "select * from " + TB_SCHEDULE + " where userid = ?";
        String sql = "select * from " + TB_SCHEDULE + " where lessonid = ? and userid = ?";
        String insertsql = "insert into " + TB_SCHEDULE + "(userid,lessonid,day,step,word_score,sentence_score,exam_score) " +
                "values (?,?,?,?,?,?,?)";
        long day = 0L;
        long time = 0l;
        //获取最后一次的时间
        Cursor cursor1 = getWritableDatabase().rawQuery(sql1, new String[]{userid});
        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            while (cursor1.moveToNext()) {
                String s = com.iyuba.trainingcamp.utils.TimeUtils.formatDateToMills(cursor1.getString(2));
                if (Long.parseLong(s) > time) {
                    time = Long.parseLong(s);
                }
            }
            day =time;
        } else {
            //空的说明没有数据库没有数据
            String s = com.iyuba.trainingcamp.utils.TimeUtils.getCurTime();
            day = Long.parseLong(com.iyuba.trainingcamp.utils.TimeUtils.formatDateToMills(s));
//            day = com.iyuba.trainingcamp.utils.TimeUtils.getFormateDate(Long.valueOf(day));
        }
        while (selectDataByDate(com.iyuba.trainingcamp.utils.TimeUtils.getFormateDate(day), GoldApp.getApp(mContext).userId) != null) {
            day += 24*3600*1000;
        }
        Cursor cursor;
        cursor = getWritableDatabase().rawQuery(sql, new String[]{lessonid, userid});
        if (cursor.getCount() > 0) {
            Log.d("diao", "writeDataToSchedule: >0");
            return false;
        } else {
            getWritableDatabase().execSQL(insertsql, new String[]{userid, lessonid, com.iyuba.trainingcamp.utils.TimeUtils.getFormateDate(day), "1", "0", "0", "0"});
            Log.d("diao", "writeDataToSchedule: !>0"+ "values"+ userid + ": "+lessonid+"; "+
                    date);
            return true;
        }
    }

    public boolean writeDownloadDataToSchedule(String userid, String lessonid, String date ,int flg) {
        int i = 0;
        String sql1 = "select * from " + TB_SCHEDULE + " where userid = ?";
        String sql = "select * from " + TB_SCHEDULE + " where lessonid = ? and userid = ?";
        String insertsql = "insert into " + TB_SCHEDULE + "(userid,lessonid,day,step,word_score,sentence_score,exam_score) " +
                "values (?,?,?,?,?,?,?)";

        Cursor cursor;
        cursor = getWritableDatabase().rawQuery(sql, new String[]{lessonid, userid});
        if (cursor.getCount() > 0) {
            Log.d("diao", "writeDataToSchedule: >0");
            return false;
        } else {
            getWritableDatabase().execSQL(insertsql, new String[]{userid, lessonid, com.iyuba.trainingcamp.utils.TimeUtils.getCurTime(), flg+"", "0", "0", "0"});
            Log.d("diao", "writeDataToSchedule: !>0"+ "values"+ userid + ": "+lessonid+"; "+
                date);
            return true;
        }
    }

    public GoldDateRecord selectDataByDate(String date, String usrid) {

        String sql = "select * from " + TB_SCHEDULE + " where day = ? and userid = ?";

        Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{date, usrid});
        Log.d("diao", "selectDataByDate: " + sql);

        if (cursor != null) {
            Log.d("diao", "cursor: " + cursor.getCount());
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            GoldDateRecord record = new GoldDateRecord();
            record.setLessonid(cursor.getString(1));
            record.setUserId(cursor.getString(0));
            record.setDate(cursor.getString(2));
            record.setWord_score(cursor.getString(4));
            record.setSentence_score(cursor.getString(5));
            record.setExam_score(cursor.getString(6));
            record.setStep(cursor.getString(3));
            getWritableDatabase().close();

            return record;
        } else {
            Log.d("diao", "selectDataByDate: nulll");
            getWritableDatabase().close();

            return null;
        }
    }

    public List<String> getLessonIdList(String userid) {
        List<String> idList = new ArrayList<>();
        String sql = "select * from " + TB_SCHEDULE + " where  userid = ?";

        Log.d("diao", "selectDataByDate: " + sql);
        Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{userid});
        if (cursor != null) {
            Log.d("diao", "cursor: " + cursor.getCount());

        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                idList.add(cursor.getString(1));
            }
        }
        return idList;

    }

    public GoldDateRecord selectDataById(String userid, String lessonid) {

        String sql = "select * from " + TB_SCHEDULE + " where lessonid = ? and userid = ?";

        Log.d("diao", "selectDataByDate: " + sql);
        Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{lessonid, userid});
        if (cursor != null) {
            Log.d("diao", "cursor: " + cursor.getCount());

        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            GoldDateRecord record = new GoldDateRecord();
            record.setUserId(cursor.getString(0));
            record.setLessonid(cursor.getString(1));
            record.setDate(cursor.getString(2));
            record.setWord_score(cursor.getString(4));
            record.setSentence_score(cursor.getString(5));
            record.setExam_score(cursor.getString(6));
            record.setStep(cursor.getString(3));
            getWritableDatabase().close();
            return record;
        } else {
            Log.d("diao", "selectDataByDate: nulll");
            getWritableDatabase().close();

            return null;
        }
    }


    public void updateStudyProcess(String userid, String step, String lessonid) {

        String updateSql = "update " + TB_SCHEDULE + " set step = ? where lessonid = ? and userid = ?";
        getWritableDatabase().execSQL(updateSql, new String[]{step, lessonid, userid});

    }

    public void updateUpdateWordScore(String userid, String score, String lessonid) {

        String updateSql = "update " + TB_SCHEDULE + " set word_score = ? where lessonid = ? and userid = ?";
        getWritableDatabase().execSQL(updateSql, new String[]{score, lessonid, userid});
        getWritableDatabase().close();

    }

    public void updateUpdateExamScore(String userid, String score, String lessonid) {

        String updateSql = "update " + TB_SCHEDULE + " set exam_score = ? where lessonid = ? and userid = ?";
        getWritableDatabase().execSQL(updateSql, new String[]{score, lessonid, userid});
        getWritableDatabase().close();

    }

    public void updateUpdateSentenceScore(String userid, String score, String lessonid) {

        String updateSql = "update " + TB_SCHEDULE + " set sentence_score = ? where lessonid = ? and userid = ?";
        getWritableDatabase().execSQL(updateSql, new String[]{score, lessonid, userid});
    }

    public void saveVoaInfo(VoaInfoBean bean) {
        String sqlString = "insert or replace into " + TB_VOAINFO + " (" + COL_DescCn + ","
                + COL_CreatTime + "," + COL_Category + "," + COL_Title_cn + ","
                + COL_Title + "," + COL_Sound + "," + COL_PublishTime + ","
                + COL_HotFlg + "," + COL_Pic + "," + COL_VoaId + "," + COL_Url + "," + COL_ReadCount + ") " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        for (int i = 0; i < bean.getData().size(); i++) {
            VoaInfoBean.DataBean dataBean = bean.getData().get(i);
            Object[] objects = new Object[]{dataBean.getDescCn(), dataBean.getCreatTime(), dataBean.getCategory(),
                    dataBean.getTitle_cn(), dataBean.getTitle(), dataBean.getSound(), dataBean.getPublishTime(), dataBean.getHotFlg(),
                    dataBean.getPic(), dataBean.getVoaId(), dataBean.getUrl(), dataBean.getReadCount()};
            getWritableDatabase().execSQL(sqlString, objects);
        }

    }


    public VoaInfoBean.DataBean getVoaInfo(String voaid) {
        VoaInfoBean.DataBean infoBean = new VoaInfoBean.DataBean();
        String sql = "select * from " + TB_VOAINFO + " where " + COL_VoaId + " = ?";
        Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{voaid});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            infoBean.setTitle_cn(cursor.getString(3));
            infoBean.setTitle(cursor.getString(4));
        }
        return infoBean;
    }


    public void saveVoaInfo(BBCInfoBean bean) {
        String sqlString = "insert or replace into " + TB_VOAINFO + " (" + COL_DescCn + ","
                + COL_CreatTime + "," + COL_Category + "," + COL_Title_cn + ","
                + COL_Title + "," + COL_Sound + "," + COL_PublishTime + ","
                + COL_HotFlg + "," + COL_Pic + "," + COL_VoaId + "," + COL_Url + "," + COL_ReadCount + ") " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?)";
        for (int i = 0; i < bean.getData().size(); i++) {
            BBCInfoBean.DataBean dataBean = bean.getData().get(i);
            Object[] objects = new Object[]{dataBean.getDescCn(), dataBean.getCreatTime(), dataBean.getCategory(),
                    dataBean.getTitle_cn(), dataBean.getTitle(), dataBean.getSound(), dataBean.getPublishTime(), dataBean.getHotFlg(),
                    dataBean.getPic(), dataBean.getBbcId(), dataBean.getUrl(), dataBean.getReadCount()};
            getWritableDatabase().execSQL(sqlString, objects);
        }
    }
}
