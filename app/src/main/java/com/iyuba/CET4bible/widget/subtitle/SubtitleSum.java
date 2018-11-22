package com.iyuba.CET4bible.widget.subtitle;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章准备内容
 *
 * @author ct
 */
public class SubtitleSum {
    public int voaid;
    public List<Subtitle> subtitles = new ArrayList<Subtitle>(); // 解说字幕

    public int getParagraph(double second) {
        int step = 0;
        int size = subtitles.size();
        for (int i = 0; i < size; i++) {
            if (second >= subtitles.get(i).pointInTime) {
                step = i + 1;
            } else {
                break;
            }
        }
        return step;
    }
}
