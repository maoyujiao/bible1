package com.iyuba.CET4bible.manager;

import com.iyuba.CET4bible.sqlite.mode.PackInfo;

import java.util.ArrayList;
import java.util.List;

public class ReadingDataManager {
    public static ReadingDataManager instance;
    public List<String> packNames = new ArrayList<>();
    public List<PackInfo> packInfos = new ArrayList<>();

    private ReadingDataManager() {
    }

    public static synchronized ReadingDataManager getInstance() {
        if (instance == null) {
            instance = new ReadingDataManager();
        }
        return instance;
    }
}
