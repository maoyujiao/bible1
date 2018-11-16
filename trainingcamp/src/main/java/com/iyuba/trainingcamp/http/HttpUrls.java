package com.iyuba.trainingcamp.http;

import android.content.Context;

import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.utils.Constants;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.http
 * @class describe
 * @time 2018/7/28 15:01
 * @change
 * @chang time
 * @class describe
 */
public class HttpUrls {
    /**获取发音的接口*/
    public static String GET_WORD_PRO = "http://static2.iyuba.com/"+ Constants.TYPE+"/sounds/";
    /**获取解析txt文件的接口*/
    public static String getAttach(Context context) {
        String s = "http://static2.iyuba.com/"+ GoldApp.getApp(context).LessonType+"/attach/";
        return s;
    }
    /**
     * 上传能力测评使用的url
     */
    public static final String url_updateExamRecord = "http://daxue.iyuba.com/ecollege/updateExamRecord.jsp";
/**
 * 获取句子音频的网址  "http://static2.iyuba.com/"cet4/sounds/1-S-1.mp3"
 */

//    public static final String URL_SENTENCE_PRONOUCE = "http://static2.iyuba.com/"cet4/sounds/1-S-1.mp3"


}
