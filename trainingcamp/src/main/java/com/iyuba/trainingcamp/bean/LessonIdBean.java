package com.iyuba.trainingcamp.bean;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.bean
 * @class describe
 * @time 2018/9/8 14:17
 * @change
 * @chang time
 * @class describe
 */
public class LessonIdBean {

    /**
     * result : 1
     * Total : 6
     * LessonList : [{"TestCategory":"L","Lessonid":"4085","id":"33895"},{"TestCategory":"S","Lessonid":"4085","id":"33889"},{"TestCategory":"W","Lessonid":"4085","id":"33879"},{"TestCategory":"L","Lessonid":"4086","id":"33371"},{"TestCategory":"S","Lessonid":"4086","id":"33365"},{"TestCategory":"W","Lessonid":"4086","id":"33355"}]
     */

    private String result;
    private int Total;
    private List<LessonListBean> LessonList;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<LessonListBean> getLessonList() {
        return LessonList;
    }

    public void setLessonList(List<LessonListBean> LessonList) {
        this.LessonList = LessonList;
    }

    public static class LessonListBean {
        /**
         * TestCategory : L
         * Lessonid : 4085
         * id : 33895
         */

        private String TestCategory;
        private String Lessonid;
        private String id;

        public LessonListBean(String testCategory, String lessonid, String id) {
            TestCategory = testCategory;
            Lessonid = lessonid;
            this.id = id;
        }

        public String getTestCategory() {
            return TestCategory;
        }

        public void setTestCategory(String TestCategory) {
            this.TestCategory = TestCategory;
        }

        public String getLessonid() {
            return Lessonid;
        }

        public void setLessonid(String Lessonid) {
            this.Lessonid = Lessonid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
