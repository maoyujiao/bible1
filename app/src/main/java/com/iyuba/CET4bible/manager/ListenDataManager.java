package com.iyuba.CET4bible.manager;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.core.sqlite.mode.test.CetAnswer;
import com.iyuba.core.sqlite.mode.test.CetExplain;
import com.iyuba.core.sqlite.mode.test.CetFillInBlank;
import com.iyuba.core.sqlite.mode.test.CetText;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class ListenDataManager {

    private static final String[] test4 = {"2014_12_3", "2014_12_2", "2014_12_1", "2014_06_2",
            "2014_06_1", "2013_12_2", "2013_12_1", "2013_06", "2012_12",
            "2012_06", "2011_12", "2011_06", "2010_12", "2010_06", "2009_12",
            "2009_06", "2008_12", "2008_06", "2007_12", "2007_06", "2006_12",
            "2006_06",};
    private static final String[] test_newtype4 = {"2017_12_1601","2017_06_1702", "2017_06_1701", "2016_12_1702", "2016_12_1701",
            "2016_06_1802", "2016_06_1801", "2015_12_1903",
            "2015_12_1902", "2015_12_1901", "2015_06_1303", "2015_06_1302", "2015_06_1301"};
    private static final String[] test6 = {"2015_12_2", "2015_12_1", "2015_12_0", "2015_06_2",
            "2015_06_1", "2014_12_3", "2014_12_2", "2014_12_1", "2014_06_2",
            "2014_06_1", "2013_12_2", "2013_12_1", "2013_06", "2012_12",
            "2012_06", "2011_12", "2011_06", "2010_12", "2010_06", "2009_12",
            "2009_06", "2008_12", "2008_06", "2007_12", "2007_06", "2006_12",
            "2006_06",};
    private static final String[] test_newtype6 = {
            "2017_06_1702", "2017_06_1701",
            "2016_12_172", "2016_12_171", "2016_06_182", "2016_06_181", "2015_12_193",
            "2015_12_192", "2015_12_191", "2015_06_133", "2015_06_132", "2015_06_131"};
    public static int curPos;
    private static ListenDataManager dataManager;
    public String year = "";
    public String rowString ="";
    public ArrayList<CetAnswer> answerList = new ArrayList<>();
    public ArrayList<CetExplain> explainList = new ArrayList<>();
    public ArrayList<CetText> textList = new ArrayList<>();
    public ArrayList<CetFillInBlank> blankList = new ArrayList<>();
    public String section = "A";
    public String para ;

    public ListenDataManager() {
    }

    public static synchronized ListenDataManager Instance() {
        if (dataManager == null) {
            dataManager = new ListenDataManager();
        }
        return dataManager;
    }

    public static String[] getTestNewType() {
        if (BuildConfig.isCET4) {
            return test_newtype4;
        } else {
            return test_newtype6;
        }
    }

    public static String[] getTest() {
        if (BuildConfig.isCET4) {
            return test4;
        } else {
            return test6;
        }
    }

}
