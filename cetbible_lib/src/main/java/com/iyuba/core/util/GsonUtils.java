package com.iyuba.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class GsonUtils {
    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    public static <T> T toObject(String jsonstr, Class<T> clazz) {
        return gson.fromJson(jsonstr, clazz);
    }

    public static <T> List<T> toObjectList(String jsonstr, Class<T> clazz) {
        JsonArray jarray = parser.parse(jsonstr).getAsJsonArray();
        List<T> objList = new ArrayList<T>();
        for (JsonElement jobj : jarray) {
            T obj = gson.fromJson(jobj, clazz);
            objList.add(obj);
        }
        return objList;
    }

    public static <T> T toObject(byte[] bytes, Class<T> clazz) {
        return gson.fromJson(new String(bytes), clazz);
    }

    public static <T> String toJson(T obj, Class<T> clazz) {
        return gson.toJson(obj, clazz);
    }
}
