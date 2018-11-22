package com.iyuba.CET4bible.event;

import android.util.SparseIntArray;

/**
 * ParagraphEvent
 *
 * @author wayne
 * @date 2017/12/23
 */
public class ParagraphEvent {
    /**
     * 上一页，下一页
     */
    public static class ParagraphPageClickEvent {
        public int pos;

        public ParagraphPageClickEvent(int pos) {
            this.pos = pos;
        }
    }

    /**
     * 提交答案
     */
    public static class ParagraphAnswerSubmitEvent {

        public ParagraphAnswerSubmitEvent() {
        }
    }

    /**
     * 点击选项
     */
    public static class ParagraphAnswerClickEvent {
        public int fragmentPos;
        public int questionPos;
        public boolean selected;

        /**
         * @param fragmentPos 第几个题目
         * @param questionPos 哪一个选项
         * @param selected    选中还是取消
         */
        public ParagraphAnswerClickEvent(int fragmentPos, int questionPos, boolean selected) {
            this.fragmentPos = fragmentPos;
            this.questionPos = questionPos;
            this.selected = selected;
        }
    }

    /**
     * 刷新答案显示
     */
    public static class ParagraphAnswerRefreshEvent {
        public SparseIntArray array;

        public ParagraphAnswerRefreshEvent(SparseIntArray array) {
            this.array = array;
        }
    }

    /**
     * 切换到指定的fragment
     */
    public static class ParagraphQuesFragmentChangeEvent {
        public int pos;

        public ParagraphQuesFragmentChangeEvent(int pos) {
            this.pos = pos;
        }
    }

    /**
     * 获取答题数据
     */
    public static class ParagraphRequestSelectedArray {

    }

}
