/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.manager;

import com.iyuba.CET4bible.sqlite.mode.Cet4Word;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class WordDataManager {
    public static WordDataManager dataManager;
    public String cate;
    public int number;
    public ArrayList<Cet4Word> words;
    public int pos;

    public WordDataManager() {
    }

    public static synchronized WordDataManager Instance() {
        if (dataManager == null) {
            dataManager = new WordDataManager();
        }
        return dataManager;
    }
}
