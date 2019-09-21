package com.iyuba.core.manager;

import com.iyuba.core.discover.sqlite.mode.BlogContent;
import com.iyuba.core.sqlite.User;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * 个人习惯在进入下一个activity的时候  先初始化数据
 * 用于各个页面之间的数据共享与传递
 *
 * @author 魏申鸿
 */
public class DataManager {
    private static DataManager instance;
    public ArrayList<Text> textList = null;
    public User user = null;
    public BlogContent blogContent;
    public int currentType;

    public static DataManager Instance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

}

