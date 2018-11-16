package com.iyuba.trainingcamp.utils;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.utils
 * @class describe
 * @time 2018/10/9 15:19
 * @change
 * @chang time
 * @class describe
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * SP
 *
 * @author wayne
 * @date 2017/11/24
 */
public class SP {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "training_sharedPreference_data";

    private static SharedPreferences getSharedPreferences(Context context, String fileName) {
        return context.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
    }

    /**
     * String Integer Boolean Float Long
     */
    public static void put(Context context, String key, Object object) {
        put(context, FILE_NAME, key, object);
    }

    public static void put(Context context, String fileName, String key, Object object) {
        SharedPreferences sp = getSharedPreferences(context, fileName);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * String Integer Boolean Float Long
     */
    public static Object get(Context context, String key, Object defaultObject) {
        return get(context, FILE_NAME, key, defaultObject);
    }

    public static Object get(Context context, String fileName, String key, Object defaultObject) {
        SharedPreferences sp = getSharedPreferences(context, fileName);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        remove(context, FILE_NAME, key);
    }

    public static void remove(Context context, String fileName, String key) {
        SharedPreferences sp = getSharedPreferences(context, fileName);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        clear(context, FILE_NAME);
    }

    public static void clear(Context context, String fileName) {
        SharedPreferences sp = getSharedPreferences(context, fileName);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        return contains(context, FILE_NAME, key);
    }

    public static boolean contains(Context context, String fileName, String key) {
        SharedPreferences sp = getSharedPreferences(context, fileName);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        return getAll(context, FILE_NAME);
    }

    public static Map<String, ?> getAll(Context context, String fileName) {
        SharedPreferences sp = getSharedPreferences(context, fileName);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }
}