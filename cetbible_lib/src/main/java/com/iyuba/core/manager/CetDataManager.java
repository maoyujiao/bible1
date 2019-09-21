package com.iyuba.core.manager;

import com.iyuba.core.sqlite.mode.test.CetAnswer;
import com.iyuba.core.sqlite.mode.test.CetExplain;
import com.iyuba.core.sqlite.mode.test.CetFillInBlank;
import com.iyuba.core.sqlite.mode.test.CetText;

import java.util.ArrayList;

/**
 * cet数据管理
 *
 * @author chentong
 * @version 1.0
 */
public class CetDataManager {
    private static CetDataManager instance;
    public ArrayList<CetAnswer> answerList = new ArrayList<CetAnswer>();
    public ArrayList<CetExplain> explainList = new ArrayList<CetExplain>();
    public ArrayList<CetText> textList = new ArrayList<CetText>();
    public ArrayList<CetFillInBlank> blankList = new ArrayList<CetFillInBlank>();

    private CetDataManager() {
    }

    public static synchronized CetDataManager Instace() {
        if (instance == null) {
            instance = new CetDataManager();
        }
        return instance;
    }
}
