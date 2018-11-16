//package com.iyuba.gold.db;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.iyuba.abilitytest.entity.AbilityQuestion;
//import com.iyuba.abilitytest.entity.AbilityResult;
//import com.iyuba.abilitytest.entity.ExamDetail;
//import com.iyuba.abilitytest.entity.TestCategory;
//import com.iyuba.abilitytest.entity.TestRecord;
//import com.iyuba.configation.Constant;
//import com.iyuba.core.util.LogUtils;
//import com.iyuba.http.LOGUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author LiuZhenli on 2016/10/12.
// */
//public class VipTestRecordHelper extends SQLiteOpenHelper {
//    private static final String TAG = "TestRecordHelper";
//
//    private static final String DB_NAME = "db_abilitytest";
//    private static final String TABLE_NAME_DATE = "db_date_learned";
//    private static final String TABLE_NAME_RECORD = "abilityTestRecord";//记录每道题目的答题记录
//    private static final String TABLE_NAME_PRACTICELIST = "practice_w";//练习题目
//    private String TABLE_NAME_RESULT = "ability_result";
//    /**
//     * 2016.10.26 因需要Categoty,Mode字段,更改版本号为2
//     * 2017.1.3 不再使用zzai 统一使用db_abilitytest 数据库
//     * 2017.6.30 新增LessonId字段 作为单元标记
//     * 2017.9.19 新增EXPLAIN字段 版本号改为6
//     */
//
//    private static final int VERSION = 6;
//
//    //abilityTestRecord 字段
//    private static final String TESTNUMBER = "TestNumber";//0
//    private static final String TESTINDEX = "testindex";//1
//    private static final String USERANSWER = "UserAnswer";//2
//    private static final String RIGHTANSWER = "RightAnswer";//3
//    private static final String ISUPLOAD = "IsUpload";//4
//    private static final String ID = "Id";//5 题目的唯一编号
//    private static final String UID = "uid";//6
//    private static final String ANSWERRESULT = "AnswerResult";//7
//    private static final String BEGINTIME = "BeginTime";//8
//    private static final String TESTTIME = "TestTime";//9
//    private static final String TESTMODE = "TestMode";//10
//    private static final String CATEGORY = "Category";//11
//    private static final String MODE = "Mode";//12  1代表测评 2代表练习
//
//    //practice 字段
//    private static final String TESTID = "TestId";
//    private static final String ANDWER = "Answer";
//    private static final String ANDWER1 = "Answer1";
//    private static final String ANDWER2 = "Answer2";
//    private static final String ANDWER3 = "Answer3";
//    private static final String ANDWER4 = "Answer4";
//    private static final String ANDWER5 = "Answer5";
//    private static final String TAGS = "Tags";
//    private static final String QUESTION = "Question";
//    private static final String TESTTYPE = "TestType";//* TestType  测试的类型  0  1(选择题） 2(填空题） 3(选择填空）  4(图片选择）  5(语音测评） 6（多选）  7（判断）  8（单词拼写）
//    private static final String TESTCATEGORY = "TestCategory";//测试题按照听说读写进行分类  W(单词) R(阅读) L(听力) X(写作) S(口语) G(语法)
//    private static final String PIC = "Pic";
//    private static final String SOUNDS = "Sounds";
//    private static final String ATTACH = "Attach";
//    private static final String REMAININDEX = "RemainIndex";
//    private static final String RESULT = "result";//题目是否做过 0 未做过 1 做错了2 做对了
//    private static final String LESSONID = "LessonId";//单元的编号  区分是第几个单元  20170629开始在高职英语中使用
//    private static final String LESSON = "lesson";//
//    private static final String EXPLAIN="Explains";//题目解析 20170919添加
//
//
//    private Context mContext;
//    private final SQLiteDatabase mDB;
//    private static VipTestRecordHelper instance;
//
//    private static final String DATE ="Date";
//    private static final String STEP = "Step";
//    private static final String SCORE_WORD  = "Score_W";
//    private static final String SCORE_SENTENCE  = "Score_S";
//    private static final String SCORE_EXAME  = "Score_E";
//
//
//    public VipTestRecordHelper(Context context) {
//        super(context, DB_NAME, null, VERSION);
//        this.mContext = context;
//        mDB = getWritableDatabase();
//    }
//
//    public static VipTestRecordHelper getInstance(Context context) {
//        if (instance == null) {
//            synchronized (VipTestRecordHelper.class) {
//                instance = new VipTestRecordHelper(context.getApplicationContext());
//            }
//        }
//        return instance;
//    }
//
//    private void createAbilityTestRecord(SQLiteDatabase db) {
//        String sqlString = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_RECORD + " ("
//                + TESTNUMBER + " integer  DEFAULT 0, "
//                + TESTINDEX + " integer  DEFAULT 0, "
//                + USERANSWER + " varchar  DEFAULT '', "
//                + RIGHTANSWER + " varchar  DEFAULT '', "
//                + ISUPLOAD + " boolean DEFAULT false, "
//                + LESSONID + " varchar DEFAULT '', "
//                + UID + " text NOT NULL DEFAULT '', "
//                + ANSWERRESULT + " integer  DEFAULT 0, "
//                + BEGINTIME + " datetime, "
//                + TESTTIME + " datetime, "
//                + TESTMODE + " varchar, "
//                + CATEGORY + " varchar, "
//                + MODE + " integer  DEFAULT 1)";//区分题目类型,1测评  2练习
//        db.execSQL(sqlString);
//    }
//
//    private void createDailyLearned(SQLiteDatabase db) {
//        String sqlString = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DATE + " ("
//                + DATE + " varchar  DEFAULT '0', "
//                + STEP + " varchar  DEFAULT '0', "
//                + SCORE_WORD + " varchar  DEFAULT '0', "
//                + SCORE_SENTENCE + " varchar  DEFAULT '0', "
//                + SCORE_EXAME + " varchar DEFAULT '0')";//区分题目类型,1测评  2练习
//        db.execSQL(sqlString);
//    }
//
////    public synchronized void  insertIntoDataBase(String [] datas){
////        String querySQL = "SELECT * FROM "
////        mDB.rawQuery()
////        String sqlString = "INSERT INTO " + TABLE_NAME_DATE +" VALUES (?,?,?,?,?)" ;
////        mDB.execSQL(sqlString,datas);
////    }
//
//
//    /*练习题目数据表*/
//    private void createPracticeTitle(SQLiteDatabase db) {
//        String slq_practice = " CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PRACTICELIST + " (" +
//                ID + " integer ," +//primary key
//                TESTID + " varchar," +
//                ANDWER + " varchar," +
//                ANDWER1 + " varchar," +
//                ANDWER2 + " varchar," +
//                ANDWER3 + " varchar," +
//                ANDWER4 + " varchar," +
//                ANDWER5 + " varchar," +
//                CATEGORY + " varchar," +
//                TAGS + " varchar," +
//                QUESTION + " varchar," +
//                TESTTYPE + " integer," +
//                TESTCATEGORY + " varchar," +
//                PIC + " varchar , " +
//                SOUNDS + " varchar , " +
//                ATTACH + " varchar , " +
//                RESULT + " integer DEFAULT 0 , " +//题目是否做过 0 做错 1 做对  2 未作
//                UID + " integer DEFAULT 0 , " +//   用户的id
//                USERANSWER + " varchar default ''," +
//                LESSONID + " integer DEFAULT 0 ," +//单元
//                EXPLAIN + " varchar default '' ," +//解析
//                LESSON + " varchar default ''" +//单元
//                ")";//用户答案
//        db.execSQL(slq_practice);
//    }
//
//    private void createAbilityResult(SQLiteDatabase db) {
//        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_RESULT + " (" +
//                "TestId integer PRIMARY KEY AUTOINCREMENT," +
//                "TypeId integer," +
//                "Score1 varchar," +
//                "Score2 varchar," +
//                "Score3 varchar," +
//                "Score4 varchar," +
//                "Score5 varchar, " +
//                "Score6 varchar, " +
//                "Score7 VARCHAR, " +
//                "Score8 VARCHAR, " +
//                "Score9 VARCHAR, " +
//                "Score10 VARCHAR, " +
//                "Total varchar, " +
//                "UndoNum varchar, " +
//                "DoRight varchar, " +
//                "beginTime varchar, " +
//                "endTime varchar, " +
//                "IsUpload INT DEFAULT (0), " +
//                "UserId TEXT)";
//        db.execSQL(sql);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        createAbilityTestRecord(db);
//        createAbilityResult(db);
//        createPracticeTitle(db);
//        createDailyLearned(db);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists " + TABLE_NAME_RECORD);
//        db.execSQL("drop table if exists " + TABLE_NAME_PRACTICELIST);
//        db.execSQL("drop table if exists " + TABLE_NAME_RESULT);
//        db.execSQL("drop table if exists " + TABLE_NAME_DATE);
//
//        createAbilityTestRecord(db);
//        createAbilityResult(db);
//        createPracticeTitle(db);
//        createDailyLearned(db);
//    }
//
//
//    /**
//     * 将练习题目插入数据表
//     *
//     * @param titleList 题目
//     */
//    public synchronized boolean insertPractices(List<AbilityQuestion.TestListBean> titleList, String testCategory, String uid) {
//        boolean flag = false;
//        mDB.beginTransaction(); // 手动设置开始事务
//
//        if (titleList != null && titleList.size() != 0) {
//            String sqlString = "insert into " + TABLE_NAME_PRACTICELIST + " (" + ID + ","
//                    + TESTID + "," + ANDWER + "," + ANDWER1 + ","
//                    + ANDWER2 + "," + ANDWER3 + "," + ANDWER4 + ","
//                    + ANDWER5 + "," + CATEGORY + "," + TAGS + ","
//                    + QUESTION + "," + TESTTYPE + "," + TESTCATEGORY + ","
//                    + PIC + "," + SOUNDS + "," + ATTACH + "," + RESULT + ","
//                    + UID + "," + USERANSWER + "," + LESSONID + "," +EXPLAIN + "," + LESSON + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            LogUtils.e(TAG, "practice sql " + sqlString);
//
//            for (int i = 0; i < titleList.size(); i++) {
//                AbilityQuestion.TestListBean ques = titleList.get(i);
//                Object[] objects = new Object[]{ques.getId(), ques.getTestId(), ques.getAnswer(), ques.getAnswer1(), ques.getAnswer2(), ques.getAnswer3(),
//                        ques.getAnswer4(), ques.getAnswer5(), ques.getCategory().trim(), ques.getTags(), ques.getQuestion(), ques.getTestType(), testCategory, ques.getImage(), ques.getSounds(),
//                        ques.getAttach(), 2, uid, 0, ques.getLessonId(), ques.Explains, Constant.mListen};
//                LogUtils.e(TAG, "保存数据库题目的id  " + ques.getId());
//                mDB.execSQL(sqlString, objects);
//            }
//            mDB.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
//            mDB.endTransaction(); // 处理完成
//            flag = true;
//        }
//        return flag;
//    }
//
//    /**
//     * 将练习题目插入数据表
//     *
//     * @param titleList 题目
//     */
//    public synchronized boolean insertPractices2(List<AbilityQuestion.TestListBean> titleList, String testCategory, String uid) {
//        boolean flag = false;
//        mDB.beginTransaction(); // 手动设置开始事务
//
//        if (titleList != null && titleList.size() != 0) {
//            String sqlString = "insert into " + TABLE_NAME_PRACTICELIST + " (" + ID + ","
//                    + TESTID + "," + ANDWER + "," + ANDWER1 + ","
//                    + ANDWER2 + "," + ANDWER3 + "," + ANDWER4 + ","
//                    + ANDWER5 + "," + CATEGORY + "," + TAGS + ","
//                    + QUESTION + "," + TESTTYPE + "," + TESTCATEGORY + ","
//                    + PIC + "," + SOUNDS + "," + ATTACH + "," + RESULT + ","
//                    + UID + "," + USERANSWER + "," + LESSONID + "," + EXPLAIN + "," + LESSON + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//            LogUtils.e(TAG, "practice sql    " + sqlString);
//
//            for (int i = 0; i < titleList.size(); i++) {
//                AbilityQuestion.TestListBean ques = titleList.get(i);
//                Object[] objects = new Object[]{ques.getId(), ques.getTestId(), ques.getAnswer(), ques.getAnswer1(), ques.getAnswer2(), ques.getAnswer3(),
//                        ques.getAnswer4(), ques.getAnswer5(), ques.getCategory(), ques.getTags(), ques.getQuestion(), ques.getTestType(), testCategory, ques.getImage(), ques.getSounds(),
//                        ques.getAttach(), 2, uid, 0, ques.getLessonId(), ques.Explains, Constant.mListen};
//                LogUtils.e(TAG, "保存数据库题目的id  " + ques.getId());
//                mDB.execSQL(sqlString, objects);
//            }
//            mDB.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
//            mDB.endTransaction(); // 处理完成
//            flag = true;
//        }
//        return flag;
//    }
//
//
//    public synchronized void updateDateLearnedDB(String date){
//        String sql = "";
//        sql = "insert into "+TABLE_NAME_DATE + " VALUES"+ " ("+date+",'0',0,0,0)";
//        mDB.execSQL(sql);
//    }
//
//    public synchronized int getDateLearned(String date){
//        String sql = "";
//        sql = "select * from "+TABLE_NAME_DATE + " where "+DATE +"= "+date;
//        Cursor  cur ;
//        cur = mDB.rawQuery(sql,null);
//        if (cur.getCount()>0){
//            return cur.getCount() ;
//        }else {
//            updateDateLearnedDB(date);
//            return  0 ;
//        }
//    }
//
//    /**
//     * @param testType W--单词
//     * @return CateGoryList
//     */
//    public ArrayList<TestCategory> getCategoryTypeAndCount(String uid, String testType, int result, int lessonId) {
//        String sql = "";
//        switch (result) {
//            case 0://答错题目的个数
//            case 1://答对题目的个数
//            case 2: //未答题目的个数
//                sql = "select count(Category) ,Category from " + TABLE_NAME_PRACTICELIST + " where "
//                        + UID + " = \'" + uid + "\'"
//                        + " and TestCategory = \'" + testType + "\' " +
//                        " and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId
//                        + " and result = " + result + " group by Category";
//                break;
//            case 3://分类获取所有题目
//                sql = "select count(Category) ,Category from " + TABLE_NAME_PRACTICELIST + " where "
//                        + UID + " = \'" + uid + "\' " +
//                        " and " + LESSON + " = \'" + Constant.mListen + "\'"
//                        + " and " + LESSONID + " = " + lessonId
//                        + " and TestCategory = \'" + testType + "\' group by Category";
//                break;
//        }
//        ArrayList<TestCategory> categoryList = new ArrayList<>();
//        Cursor cursor = null;
//        try {
//            cursor = mDB.rawQuery(sql, null);
//            cursor.moveToFirst();
//            if (cursor.getCount() > 0) {
//                for (int i = 0; i < cursor.getCount(); i++) {
//                    TestCategory category = new TestCategory();
//                    category.count = cursor.getInt(0);
//                    category.type = cursor.getString(1);
//                    categoryList.add(i, category);
//                    cursor.moveToNext();
//                }
//                cursor.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return categoryList;
//    }
//
//    /**
//     * @param uid          用户的id
//     * @param testCategory 测试的类型 W 单词 L 听力
//     * @param mode         是否答题 0打错题目 1答对题目 2未答题 3所有题目
//     * @param count        每次获取的数据条数
//     * @return 题目
//     */
//
//    public ArrayList<AbilityQuestion.TestListBean> getParacticeTitles1(String uid, String category, String testCategory, int mode, int count, int lessonId) {
//
//        String sql;
//        if (category.equals("all") && mode == 3) {//获取所有题目
//            sql = "select * from " + TABLE_NAME_PRACTICELIST + " where uid = " + uid +
//                    " and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId
//                    + " and " + TESTCATEGORY + " = \'" + testCategory + "\'  order by Id limit " + count;
//        } else if (!category.equals("all") && mode == 3) {//获取指定纬度的所有题目
//            sql = "select * from " + TABLE_NAME_PRACTICELIST + " where uid = " + uid +
//                    " and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId +
//                    " and " + TESTCATEGORY + " = \'" + testCategory + "\'  and " + CATEGORY + " = \'" + category + "\' and result <> 2 order by Id";
//        } else {//获取制定累型的题目
//            sql = "select * from " + TABLE_NAME_PRACTICELIST + " where uid = " + uid +
//                    " and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId +
//                    " and " + TESTCATEGORY + " = \'" + testCategory + "\' and " + RESULT + " = " + mode +
//                    " order by Id limit " + count;
//        }
//        LOGUtils.e(TAG, sql);
//        ArrayList<AbilityQuestion.TestListBean> item = new ArrayList<>();
//        Cursor cursor = mDB.rawQuery(sql, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            for (int i = 0; i < cursor.getCount(); i++) {
//                AbilityQuestion.TestListBean ques = new AbilityQuestion.TestListBean();
//                ques.setId(cursor.getInt(0));
//                ques.setTestId(cursor.getInt(1));
//                ques.setAnswer(cursor.getString(2));
//                ques.setAnswer1(cursor.getString(3));
//                ques.setAnswer2(cursor.getString(4));
//                ques.setAnswer3(cursor.getString(5));
//                ques.setAnswer4(cursor.getString(6));
//                ques.setAnswer5(cursor.getString(7));
//                ques.setCategory(cursor.getString(8));
//                ques.setTags(cursor.getString(9));
//                ques.setQuestion(cursor.getString(10));
//                ques.setTestType(cursor.getInt(11));
//                ques.setImage(cursor.getString(13));
//                ques.setSounds(cursor.getString(14));
//                ques.setAttach(cursor.getString(15));
//                ques.setResult(cursor.getString(16));
//                ques.Explains = cursor.getString(cursor.getColumnIndex(EXPLAIN));
//                //ques.uid = cursor.getString(17);
//                ques.setUserAnswer(cursor.getString(18));
//                item.add(i, ques);
//                cursor.moveToNext();
//            }
//        }
//        cursor.close();
//        return item;
//    }
//
//    public boolean saveTestRecords(ArrayList<TestRecord> testRecordLists) {
//        String sqlString = "insert into " + TABLE_NAME_RECORD + "(uid,LessonId,TestNumber,UserAnswer,RightAnswer,AnswerResult,BeginTime,"
//                + "TestTime,IsUpload,TestMode,Category,Mode)" + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
//        boolean flag;
//        mDB.beginTransaction();
//        try {
//            for (int i = 0; i < testRecordLists.size(); i++) {
//                TestRecord testRecord = testRecordLists.get(i);
//                Object[] objects = new Object[]{testRecord.uid, testRecord.Id,
//                        testRecord.TestNumber, testRecord.UserAnswer,
//                        testRecord.RightAnswer, testRecord.AnswerResult,
//                        testRecord.BeginTime, testRecord.TestTime, testRecord.IsUpload, testRecord.TestCategory, testRecord.Categroy, testRecord.mode};
//                mDB.execSQL(sqlString, objects);
//                LogUtils.e(TAG, "sqlString   " + sqlString + "   :::" + testRecord.TestNumber);
//            }
//            mDB.setTransactionSuccessful();// 设置事务标志为成功，当结束事务时就会提交事务
//            mDB.endTransaction();
//            flag = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            flag = false;
//        }
//        return flag;
//    }
//
//    /**
//     * @return List<StudyRecord>
//     */
//    public ArrayList<TestRecord> getWillUploadTestRecord(String testMode, String uid, int mode) {
//        ArrayList<TestRecord> testRecordList = new ArrayList<>();
//        String sqlSting = "select * from " + TABLE_NAME_RECORD + " where IsUpload = '0' and " + MODE + " = " + mode + " and " + UID + " = \'" + uid + "\' and " + TESTMODE + " = \'" + testMode + "\'";
//        Cursor cursor = null;
//        try {
//            cursor = mDB.rawQuery(sqlSting, null);
//            cursor.moveToFirst();
//            if (cursor.getCount() > 0) {
//                for (int i = 0; i < cursor.getCount(); i++) {
//                    TestRecord testRecord = new TestRecord();
//                    testRecord.uid = cursor.getString(cursor.getColumnIndex(UID));
//                    testRecord.Id = cursor.getString(cursor.getColumnIndex(ID));
//                    testRecord.TestNumber = cursor.getInt(cursor.getColumnIndex(TESTNUMBER));
//                    testRecord.BeginTime = cursor.getString(cursor.getColumnIndex(BEGINTIME));
//                    testRecord.TestTime = cursor.getString(cursor.getColumnIndex(TESTTIME));
//                    testRecord.UserAnswer = cursor.getString(cursor.getColumnIndex(USERANSWER));
//                    testRecord.RightAnswer = cursor.getString(cursor.getColumnIndex(RIGHTANSWER));
//                    testRecord.AnswerResult = cursor.getInt(cursor.getColumnIndex(ANSWERRESULT));
//                    testRecord.IsUpload = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ISUPLOAD)));
//                    testRecord.TestCategory = cursor.getString(cursor.getColumnIndex(TESTMODE));
//                    testRecord.Categroy = cursor.getString(cursor.getColumnIndex(CATEGORY));
//                    testRecordList.add(i, testRecord);
//                    cursor.moveToNext();
//                }
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return testRecordList;
//    }
//
//    /**
//     * @param TestNumber t
//     * @return Success or not
//     */
//    public boolean setTestRecordIsUpload(int TestNumber) {
//        String sqlString = "update " + TABLE_NAME_RECORD + " set IsUpload = '1' " + " where TestNumber = '" + TestNumber + "'";
//        return exeSql(sqlString);
//    }
//
//
//    private boolean exeSql(String sqlString) {
//        boolean flag;
//        try {
//            mDB.execSQL(sqlString);
//            flag = true;
//        } catch (Exception e) {
//            flag = false;
//        }
//        return flag;
//    }
//
//    private boolean exeSql(String sqlString, Object[] objects) {
//        boolean flag;
//        try {
//            mDB.execSQL(sqlString, objects);
//            flag = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            flag = false;
//        }
//        return flag;
//    }
//
//
//    public boolean updatePracticeDB(String uid, ArrayList<ExamDetail.DataBean> mExamDataList, String w) {
//        boolean flag;
//        mDB.beginTransaction(); // 设置开始事务
//        String sql;
//        try {
//            for (ExamDetail.DataBean data : mExamDataList) {
//                if (data.getScore() == 100) data.setScore(1);
//                sql = "update  " + TABLE_NAME_PRACTICELIST + " set " + RESULT + " = " + data.getScore() + ", " + USERANSWER + " = \'" + data.getUserAnswer()
//                        + "\' where " + ID + " = " + data.getId() + " and " + TESTCATEGORY + " = \'" + w + "\'" +
//                        " and  " + UID + " = \'" + uid + "\'";
//                LogUtils.e(TAG, "更新数据库 :" + sql);
//                mDB.execSQL(sql);
//            }
//            mDB.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
//            mDB.endTransaction(); // 处理完成
//            flag = true;
//        } catch (Exception e) {
//            flag = false;
//            mDB.endTransaction();
//            e.printStackTrace();
//        }
//        return flag;
//    }
//
//    /***
//     * 分类获取试题总数  答对的个数  这里根据题目类型划分  听力 词汇 阅读
//     * eg 单词 总题目500个
//     *
//     * @param uid    用户的id
//     * @param result 是否正确  0 错误,1 正确 2 未答
//     * @return
//     */
//    public ArrayList<TestCategory> getPracticeTestRecord(String uid, int result, int lessonId) {
//        String sql = "";
//        switch (result) {
//            case 0://答错题目的个数
//            case 1://答对题目的个数
//            case 2: //未答题目的个数
//                sql = "select count(TestCategory) ,TestCategory from " + TABLE_NAME_PRACTICELIST +
//                        " where " + UID + " = \'" + uid + "\'  and result = " + result + " and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId + " group by TestCategory";
//                break;
//            case 3://分类获取所有题目
//                sql = "select count(TestCategory) ,TestCategory from " + TABLE_NAME_PRACTICELIST +
//                        " where " + UID + " = \'" + uid + "\' " + " and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId + " group by TestCategory";
//                break;
//        }
//        ArrayList<TestCategory> categoryList = new ArrayList<TestCategory>();
//        Cursor cursor = mDB.rawQuery(sql, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            for (int i = 0; i < cursor.getCount(); i++) {
//                TestCategory category = new TestCategory();
//                category.count = cursor.getInt(0);//geshu
//                category.type = cursor.getString(1);//W 单词  L 听力
//                categoryList.add(i, category);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//        return categoryList;
//    }
//
//    /**
//     * @param uid
//     * @param result
//     * @param lessonId
//     * @param testCategory W L R X
//     */
//    public int getPracticeCountByType(String uid, int result, int lessonId, String testCategory) {
//        String sql = "";
//        if (result == 3) {
//            sql = "select count(TestCategory) ,TestCategory from " + TABLE_NAME_PRACTICELIST +
//                    " where " + UID + " = \'" + uid + "\'  and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId + " and " + TESTCATEGORY + " =\'" + testCategory + "\'  group by TestCategory";
//        } else {
//            sql = "select count(TestCategory) ,TestCategory from " + TABLE_NAME_PRACTICELIST +
//                    " where " + UID + " = \'" + uid + "\'  and result = " + result + " and " + LESSON + " = \'" + Constant.mListen + "\' and " + LESSONID + " = " + lessonId + " and " + TESTCATEGORY + " =\'" + testCategory + "\'  group by TestCategory";
//        }
//        ArrayList<TestCategory> categoryList = new ArrayList<TestCategory>();
//        Cursor cursor = mDB.rawQuery(sql, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            for (int i = 0; i < cursor.getCount(); i++) {
//                TestCategory c = new TestCategory();
//                c.count = cursor.getInt(0);//geshu
//                c.type = cursor.getString(1);//W 单词  L 听力
//                categoryList.add(i, c);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//        if (categoryList.size() > 0) {
//            return categoryList.get(0).count;
//        } else {
//            return 0;
//        }
//
//    }
//
//    /**
//     * 根据用户名和题目类型 删除数据库中的数据
//     *
//     * @param uid          用户的Id
//     * @param testCategory 题目类型 W(单词) L(听力) R(阅读) G (语法) S(口语) X(写作)
//     * @return Delete True or false
//     */
//    public boolean delPracticeDataByUser(String uid, String testCategory) {
//
//        String sql = "DELETE FROM " + TABLE_NAME_PRACTICELIST + " where " + UID + " = \'" + uid + "\' and " + TESTCATEGORY + " =  \'" + testCategory + "\' ";
//        LogUtils.e(TAG, "删除数据库中的数据  " + sql);
//        return exeSql(sql);
//
//    }
//
//    /**
//     * 保存能力测试结果  abilityResult
//     *
//     * @param res 测试结果
//     * @return 保存的测试结果是否成功
//     */
//    public boolean seveTestRecord(AbilityResult res) {
//        String sqlString = "insert into ability_result(TypeId,Score1,Score2,Score3,Score4,Score5,Score6,Score7,Score8,Score9,Score10,Total,UndoNum,DoRight,beginTime,endTime,IsUpload,UserId)"
//                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        Object[] objects = new Object[]{res.TypeId, res.Score1, res.Score2, res.Score3, res.Score4, res.Score5, res.Score6, res.Score7, res.Score8, res.Score9, res.Score10, res.Total, res.UndoNum, res.DoRight, res.beginTime, res.endTime, res.isUpload, res.uid};
//        return exeSql(sqlString, objects);
//    }
//
//    /**
//     * 标记能力分析上传成功
//     *
//     * @param testId testid 在数据库中自增
//     * @return 是否保存成功
//     */
//    public boolean setAbilityResultIsUpload(int testId) {
//        String sqlString = "update ability_result" + " set IsUpload = 1 " + " where TestId = '" + testId + "'";
//        return exeSql(sqlString);
//    }
//
//    /***
//     * 获取雅思能力测试结果
//     *
//     * @param typeId 本道题的类型 0写作 1单词 2 语法 3听力 4口语 5阅读
//     * @param uid    用户的id
//     * @param upload 获取的数据是否上传服务器
//     * @return 测试结果
//     */
//    public ArrayList<AbilityResult> getAbilityTestRecord(int typeId, String uid, boolean upload) {
//        ArrayList<AbilityResult> resultList = new ArrayList<>();
//        Cursor cursor = null;
//        String sqlString;
//
//        if (!uid.equals("") && upload) {//获取需要上传服务器的数据  根据用户名和是否已经上传进行选择
//            sqlString = "select * from ability_result where UserId =  " + uid + " and  IsUpload = 0";
//            LogUtils.e(TAG, "登录状态下上传服务器sql    " + sqlString);
//        } else if (!uid.equals("")) {//用户登录过 展示本地数据时使用
//            sqlString = "select * from ability_result where TypeId= " + typeId + " and UserId =  " + uid;
//            LogUtils.e(TAG, "登录状态下展示 sql    " + sqlString);
//        } else {
//            sqlString = "select * from ability_result where TypeId= " + typeId;//未登录状态下展示本地数据
//            LogUtils.e(TAG, "未登录状态下展示 sql    " + sqlString);
//        }
//        try {
//            cursor = mDB.rawQuery(sqlString, null);
//            cursor.moveToFirst();
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                for (int i = 0; i < cursor.getCount(); i++) {
//                    AbilityResult res = new AbilityResult();
//                    res.TestId = cursor.getInt(0);
//                    res.TypeId = cursor.getInt(1);
//                    res.Score1 = cursor.getString(2);
//                    res.Score2 = cursor.getString(3);
//                    res.Score3 = cursor.getString(4);
//                    res.Score4 = cursor.getString(5);
//                    res.Score5 = cursor.getString(6);
//                    res.Score6 = cursor.getString(7);
//                    res.Score7 = cursor.getString(8);
//                    res.Score8 = cursor.getString(9);
//                    res.Score9 = cursor.getString(10);
//                    res.Score10 = cursor.getString(11);
//                    res.Total = cursor.getInt(12);
//                    res.UndoNum = cursor.getInt(13);
//                    res.DoRight = cursor.getInt(14);
//                    res.beginTime = cursor.getString(15);
//                    res.endTime = cursor.getString(16);
//                    res.isUpload = cursor.getInt(17);
//                    res.uid = cursor.getString(18);
//                    resultList.add(res);
//                    cursor.moveToNext();
//                }
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return resultList;
//    }
//}
