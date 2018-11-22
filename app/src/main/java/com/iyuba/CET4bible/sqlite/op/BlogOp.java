/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.sqlite.op;

import android.content.Context;
import android.database.Cursor;

import com.iyuba.CET4bible.sqlite.db.DatabaseService;
import com.iyuba.CET4bible.sqlite.mode.Blog;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class BlogOp extends DatabaseService {
    public static final String TABLE_NAME = "blog";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TITLE_EN = "title_en";
    public static final String SOURCE = "source";
    public static final String AUTHOR = "author";
    public static final String TAG = "tag";
    public static final String DESCCN = "desccn";
    public static final String CREATETIME = "createtime";
    public static final String URL = "url";
    public static final String ESSAY = "essay";
    public static final String CATEGORY = "category";
    public static final String READCOUNT = "readcount";

    public BlogOp(Context context) {
        super(context);
    }

    public void saveData(ArrayList<Blog> blogs) {
        for (Blog blog : blogs) {
            saveData(blog);
        }
    }

    /**
     * 插入数据
     */
    public void saveData(Blog blog) {
        importDatabase.openDatabase().execSQL(
                "insert or replace into " + TABLE_NAME + " (" + ID + ","
                        + TITLE + "," + TITLE_EN + "," + SOURCE + "," + AUTHOR
                        + "," + TAG + "," + DESCCN + "," + URL + ","
                        + CREATETIME + "," + ESSAY + "," + CATEGORY + ","
                        + READCOUNT + ") values(?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{blog.id, blog.title, blog.title_en, blog.source,
                        blog.author, blog.tag, blog.desccn, blog.url,
                        blog.createtime, blog.essay, blog.category,
                        blog.readcount});
        closeDatabase(null);
    }

    public Blog selectData(int id) {
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + TITLE + "," + TITLE_EN + "," + SOURCE
                        + "," + AUTHOR + "," + TAG + "," + DESCCN + "," + URL
                        + "," + CREATETIME + "," + ESSAY + "," + CATEGORY + ","
                        + READCOUNT + " from " + TABLE_NAME + " where id= ?",
                new String[]{String.valueOf(id)});
        closeDatabase(null);
        if (cursor.moveToFirst()) {
            Blog blog = fillIn(cursor);
            cursor.close();
            return blog;
        } else {
            cursor.close();
            return null;
        }

    }

    public void removeAllData() {
        importDatabase.openDatabase().execSQL("delete from " + TABLE_NAME);
        closeDatabase(null);
    }

    public ArrayList<Blog> selectData(int count, int offset) {
        ArrayList<Blog> blogs = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + TITLE + "," + TITLE_EN + "," + SOURCE
                        + "," + AUTHOR + "," + TAG + "," + DESCCN + "," + URL
                        + "," + CREATETIME + "," + ESSAY + "," + CATEGORY + ","
                        + READCOUNT + " from " + TABLE_NAME + " ORDER BY "
                        + CREATETIME + " DESC Limit ? Offset ?",
                new String[]{String.valueOf(count), String.valueOf(offset)});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            blogs.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (blogs.size() != 0) {
            return blogs;
        }
        return new ArrayList<>();
    }

    public ArrayList<Blog> selectData(String cate, int count, int offset) {
        ArrayList<Blog> blogs = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + TITLE + "," + TITLE_EN + "," + SOURCE
                        + "," + AUTHOR + "," + TAG + "," + DESCCN + "," + URL
                        + "," + CREATETIME + "," + ESSAY + "," + CATEGORY + ","
                        + READCOUNT + " from " + TABLE_NAME
                        + " where category= ? ORDER BY " + CREATETIME
                        + " DESC  Limit ? Offset ?",
                new String[]{cate, String.valueOf(count),
                        String.valueOf(offset)});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            blogs.add(fillIn(cursor));
        }
        closeDatabase(null);
        if (blogs.size() != 0) {
            return blogs;
        }
        return null;
    }

    public ArrayList<Blog> findDataByAll() {
        ArrayList<Blog> blogs = new ArrayList<>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select *" + " from " + TABLE_NAME + " ORDER BY " + ID
                        + " desc", new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            blogs.add(fillIn(cursor));
        }
        cursor.close();
        closeDatabase(null);
        if (blogs.size() != 0) {
            return blogs;
        }
        return null;
    }

    private Blog fillIn(Cursor cursor) {
        Blog blog = new Blog();
        blog.id = cursor.getInt(0);
        blog.title = cursor.getString(1);
        blog.title_en = cursor.getString(2);
        blog.source = cursor.getString(3);
        blog.author = cursor.getString(4);
        blog.tag = cursor.getString(5);
        blog.desccn = cursor.getString(6);
        blog.url = cursor.getString(7);
        blog.createtime = cursor.getString(8);
        blog.essay = cursor.getString(9);
        blog.category = cursor.getString(10);
        blog.readcount = cursor.getString(11);
        return blog;
    }
}
