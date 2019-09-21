package com.iyuba.abilitytest.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyuba.base.util.L;
import com.iyuba.configation.ConfigManager;
import com.iyuba.configation.ConstantManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录已经做过的题目年份，做过的字体颜色改变
 */
public class TestedUtil {
    private static volatile TestedUtil instance;
    private static final String TAG = "TestedUtil:  ";
    private static final String KEY = "ability-test-lessonId";
    private Gson gson;
    private List<String> mList;
    private boolean isN123;

    public static TestedUtil getInstance() {
        if (instance == null) {
            synchronized (TestedUtil.class) {
                if (instance == null) {
                    instance = new TestedUtil();
                }
            }
        }
        return instance;
    }

    private TestedUtil() {
        int level = ConstantManager.getAppType();
        isN123 = level > 10;

        gson = new Gson();

        String data = ConfigManager.Instance().loadString(KEY);
        if (TextUtils.isEmpty(data)) {
            mList = new ArrayList<>();
        } else {
            mList = gson.fromJson(data, new TypeToken<List<String>>() {
            }.getType());
        }
    }

    public void saveLessonId(String lessonId) {
        L.e(TAG + "saveLessonId : " + lessonId);

        if (mList.contains(lessonId)) {
            return;
        }
        mList.add(lessonId);
        ConfigManager.Instance().putString(KEY, gson.toJson(mList));
    }

    public boolean isTested(String lessonId) {
        if (isN123) {
            return false;
        }
        L.e(TAG + "isTested : " + lessonId);

        return mList.contains(lessonId);
    }


    public void removeLessonId(String lessonId) {
        L.e(TAG + "removeLessonId : " + lessonId);

        if (mList.contains(lessonId)) {
            mList.remove(lessonId);
            ConfigManager.Instance().putString(KEY, gson.toJson(mList));
        }
    }

}
