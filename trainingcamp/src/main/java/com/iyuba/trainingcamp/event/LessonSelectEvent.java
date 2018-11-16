package com.iyuba.trainingcamp.event;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.event
 * @class describe
 * @time 2018/11/6 14:47
 * @change
 * @chang time
 * @class describe
 */
public class LessonSelectEvent {

    int lessonid;

    public LessonSelectEvent(int lessonid) {
        this.lessonid = lessonid;
    }

    public int getLessonid() {
        return lessonid;
    }

    public void setLessonid(int lessonid) {
        this.lessonid = lessonid;
    }

}
