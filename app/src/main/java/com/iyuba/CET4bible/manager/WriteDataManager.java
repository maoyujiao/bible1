/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.manager;

import com.iyuba.CET4bible.sqlite.mode.Write;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class WriteDataManager {
    public static WriteDataManager dataManager;
    public Write write;
    public ArrayList<Write> writeList = new ArrayList<>();

    public WriteDataManager() {
    }

    public static synchronized WriteDataManager Instance() {
        if (dataManager == null) {
            dataManager = new WriteDataManager();
        }
        return dataManager;
    }
}
