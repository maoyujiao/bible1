package com.iyuba.core.widget.subtitle;

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
    public List<Subtitle> lrcs = new ArrayList<Subtitle>(); // 原唱字幕

    public int getParagraph(double second, int type) {
        int step = 0;
        if (type == 1) {
            int size = subtitles.size();
            for (int i = 0; i < size; i++) {
                if (second >= subtitles.get(i).pointInTime) {
                    step = i + 1;
                } else {
                    break;
                }
            }
        } else if (type == 0) {
            int size = lrcs.size();
            for (int i = 0; i < size; i++) {
                if (second >= lrcs.get(i).pointInTime) {
                    step = i + 1;
                } else {
                    break;
                }
            }
        }
        return step;
    }
}
