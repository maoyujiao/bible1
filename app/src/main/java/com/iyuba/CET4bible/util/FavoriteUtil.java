package com.iyuba.CET4bible.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyuba.base.util.L;
import com.iyuba.configation.ConfigManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * FavoriteUtil
 *
 * @author wayne
 * @date 2017/12/12
 */
public class FavoriteUtil {
    public interface Type {
        int listening = 0;
        int translate = 1;
        int write = 2;
        int fillInBlack = 3;
        int paragraph = 4;
        int reading = 5;
        int paragraph_question = 5;
    }

    private List<String> favoriteList;
    private String[] spKeys = {
            "sp_favorite_key_0", "sp_favorite_key_1", "sp_favorite_key_2", "sp_favorite_key_3",
            "sp_favorite_key_4", "sp_favorite_key_5", "sp_favorite_key_6", "sp_favorite_key_7"
    };
    private int type;

    private Gson gson;

    public FavoriteUtil(int type) {
        this.type = type;
        gson = new Gson();
        String data = ConfigManager.Instance().loadString(spKeys[type], "");
        if (TextUtils.isEmpty(data)) {
            favoriteList = new ArrayList<>();
        } else {
            favoriteList = gson.fromJson(data, new TypeToken<List<Object>>() {
            }.getType());
        }
    }

    public synchronized void setFavorite(boolean favorite, String id) {
        L.e("---  setFavorite  ---  " + id);
        if (favorite) {
            if (!hasBlog(id)) {
                favoriteList.add(id);
                ConfigManager.Instance().putString(spKeys[type], gson.toJson(favoriteList));
            }
        } else {
            if (hasBlog(id)) {
                favoriteList.remove(id);
                ConfigManager.Instance().putString(spKeys[type], gson.toJson(favoriteList));
            }
        }
    }

    public synchronized boolean isFavorite(String id) {
        L.e("---  isFavorite  ---  " + id);
        return hasBlog(id);
    }

    public List getData() {
        for (int i = 0; i < favoriteList.size(); i++) {
            L.e("-- getData --  " + favoriteList.get(i));
        }
        return favoriteList;
    }


    private boolean hasBlog(String id) {
        for (String s : favoriteList) {
            if (s.equals(id)) {
                return true;
            }
        }
        return false;
    }


    class KeyComparator implements Comparator<Object> {

        @Override
        public int compare(Object b111, Object b2222) {
            return b2222.toString().compareTo(b111.toString());
        }
    }
}
