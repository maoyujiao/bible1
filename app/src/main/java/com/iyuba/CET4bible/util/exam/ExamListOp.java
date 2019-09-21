package com.iyuba.CET4bible.util.exam;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.ImportDatabase;
import com.iyuba.base.util.L;
import com.iyuba.configation.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * ExamListOp
 *
 * @author wayne
 * @date 2018/1/19
 */
public class ExamListOp {
    String table_exam_list = "exam_list";

    Context mContext;
    ImportDatabase importDatabase;

    public ExamListOp(Context context) {
        mContext = context;
        importDatabase = new ImportDatabase(mContext);
    }


    public List<DbExamListBean> findAll() {
        List<DbExamListBean> list = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery("select * from " + table_exam_list, new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            list.add(fillIn(cursor));
        }
        cursor.close();
        importDatabase.closeDatabase();
        return list;
    }

    public void deleteAllTable(String id) {
        if (Constant.APP_CONSTANT.TYPE().equals("4")) {
            String tables[] = new String[]{
                    "newtype_texta4",
                    "newtype_textb4",
                    "newtype_textc4",
                    "newtype_answera4",
                    "newtype_answerb4",
                    "newtype_answerc4",
                    "newtype_explain4"
            };

            for (int i = 0; i < tables.length; i++) {
                delete(tables[i], "TestTime", id);
            }
        } else if (Constant.APP_CONSTANT.TYPE().equals("6")) {
            String tables[] = new String[]{
                    "newtype_texta6",
                    "newtype_textb6",
                    "newtype_textc6",
                    "newtype_answera6",
                    "newtype_answerb6",
                    "newtype_answerc6",
                    "newtype_explain6"
            };

            for (int i = 0; i < tables.length; i++) {
                delete(tables[i], "TestTime", id);
            }
        }
    }

    public void delete(String table, String columnId, String id) {
        importDatabase.openDatabase().execSQL("delete from " + table + " where " + columnId + " = ?", new String[]{id});
        L.e("=== DB DELETE === TABLE :::  " + table + "   === ID :  " + id);
    }

    private DbExamListBean fillIn(Cursor cursor) {
        DbExamListBean bean = new DbExamListBean();
        bean.year = cursor.getString(cursor.getColumnIndex("year"));
        bean.title = cursor.getString(cursor.getColumnIndex("title"));
        bean.version = cursor.getString(cursor.getColumnIndex("version"));
        bean.image = cursor.getString(cursor.getColumnIndex("image"));
        return bean;
    }

    public void write(List<ExamListBean.DataBean> list) {
        for (int i = 0; i < list.size(); i++) {
            write(list.get(i));
        }
    }

    public void write(ExamListBean.DataBean dataBean) {
        importDatabase.openDatabase().execSQL("insert or replace into exam_list values(?,?,?,?);", new String[]{
                dataBean.getId(), dataBean.getName(), dataBean.getPicture(), dataBean.getVersion()});
        L.e("=== DB INSERT === TABLE :::  exam_list   === ID :  " + dataBean.getId());
    }

    public void insertListenData(String section, ExamDataBean.ItemListBean data) {
        insertAnswer(section, data.getAnswer());
        insertText(section, data.getTextList());
        insertExplain(data.getExplain());
    }

    private void insertExplain(List<ExamDataBean.ItemListBean.ExplainBean> explainList) {
        if (explainList == null) {
            return;
        }
        for (int i = 0; i < explainList.size(); i++) {
            ExamDataBean.ItemListBean.ExplainBean bean = explainList.get(i);
            importDatabase.openDatabase().execSQL("insert into " + "newtype_explain" + Constant.APP_CONSTANT.TYPE() + " values(?,?,?,?,?,?,?,?)",
                    new String[]{
                            bean.getTestTime(),
                            bean.getTestType() + "",
                            bean.getNumber() + "",
                            bean.getKeyss(),
                            bean.getExplains().replace("'", "’").replace("\\\"", "”"),
                            bean.getKnowledge().replace("'", "’").replace("\\\"", "”"),
                            bean.getDemo(),
                            bean.getFlg()
                    }
            );
            L.e("=== DB INSERT === TABLE :::  " + "newtype_explain" + Constant.APP_CONSTANT.TYPE() + "   === ID :  " + bean.getTestTime());
        }
    }

    private void insertText(String section, List<ExamDataBean.ItemListBean.TextListBean> textList) {
        if (textList == null) {
            return;
        }
        for (int i = 0; i < textList.size(); i++) {
            ExamDataBean.ItemListBean.TextListBean bean = textList.get(i);
            importDatabase.openDatabase().execSQL(
                    "insert into " + "newtype_text" + section + Constant.APP_CONSTANT.TYPE() + " values(?,?,?,?,?,?,?,?,?,?,?,?)",
                    new String[]{
                            bean.getTestTime(),
                            textList.get(0).getSound().split("-")[0],
                            //bean.getNumber() + "",
                            bean.getNumberIndex() + "",
                            bean.getTiming() + "",
                            bean.getSentence().replace("'", "’").replace("\\\"", "”"),
                            bean.getVipFlg(),
                            bean.getSex(),
                            bean.getSound(),
                            "",
                            "",
                            "",
                            ""
                    }
            );
            L.e("=== DB INSERT === TABLE :::  " + "newtype_text" + section.toLowerCase() + Constant.APP_CONSTANT.TYPE() + "   === ID :  " + bean.getTestTime());
        }
    }

    private void insertAnswer(String section, List<ExamDataBean.ItemListBean.AnswerBean> answerList) {
        if (answerList == null) {
            return;
        }
        for (int i = 0; i < answerList.size(); i++) {
            ExamDataBean.ItemListBean.AnswerBean answer = answerList.get(i);
            importDatabase.openDatabase().execSQL(
                    "insert into " + "newtype_answer" + section + Constant.APP_CONSTANT.TYPE() + " values(?,?,?,?,?,?,?,?,?,? ,?)",
                    new String[]{
                            answer.getTestTime(),
                            answer.getNumber() + "",
                            answer.getQuestion().replace("'", "’").replace("\\\"", "”"),
                            answer.getAnswerA().replace("'", "’").replace("\\\"", "”"),
                            answer.getAnswerB().replace("'", "’").replace("\\\"", "”"),
                            answer.getAnswerC().replace("'", "’").replace("\\\"", "”"),
                            answer.getAnswerD().replace("'", "’").replace("\\\"", "”"),
                            answer.getSound(),
                            answer.getAnswer(),
                            answer.getFlg(),
                            ""
                    }
            );
            L.e("=== DB INSERT === TABLE :::  " + "newtype_answer" + section.toLowerCase() + Constant.APP_CONSTANT.TYPE() + "   === ID :  " + answer.getTestTime());
        }
    }

    public String findImageByID(String test) {
        List<DbExamListBean> list = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery("select * from " + table_exam_list + " where year = ?", new String[]{test});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            list.add(fillIn(cursor));
        }
        cursor.close();
        importDatabase.closeDatabase();
        if (list.size() > 0) {
            return list.get(0).image;
        } else {
            return "";
        }
    }
}
