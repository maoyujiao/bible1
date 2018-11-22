package com.iyuba.CET4bible.util.exam;

import com.google.gson.Gson;

import java.util.List;

/**
 * ExamListBean
 *
 * @author wayne
 * @date 2018/1/19
 */
public class ExamListBean {


    /**
     * size : 13
     * data : [{"content":"2017年12月四级真题卷1","id":"2017121601","name":"2017年12月四级真题卷1","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2017年06月四级真题卷2","id":"2017061702","name":"2017年06月四级真题卷2","picture":"1","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2017年06月四级真题卷1","id":"2017061701","name":"2017年06月四级真题卷1","picture":"1","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2016年12月四级真题卷2","id":"2016121702","name":"2016年12月四级真题卷2","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2016年12月四级真题卷1","id":"2016121701","name":"2016年12月四级真题卷1","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2016年06月四级真题卷2","id":"2016061802","name":"2016年06月四级真题卷2","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2016年06月四级真题卷1","id":"2016061801","name":"2016年06月四级真题卷1","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2015年12月四级真题卷3","id":"2015121903","name":"2015年12月四级真题卷3","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2015年12月四级真题卷2","id":"2015121902","name":"2015年12月四级真题卷2","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2015年12月四级真题卷1","id":"2015121901","name":"2015年12月四级真题卷1","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2015年06月四级真题卷3","id":"2015061303","name":"2015年06月四级真题卷3","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2015年06月四级真题卷2","id":"2015061302","name":"2015年06月四级真题卷2","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"},{"content":"2015年06月四级真题卷1","id":"2015061301","name":"2015年06月四级真题卷1","picture":"","typeA":"1","typeB":"1","typeC":"1","typeD":"0","version":"1"}]
     */

    private int size;
    private List<DataBean> data;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : 2017年12月四级真题卷1
         * id : 2017121601
         * name : 2017年12月四级真题卷1
         * picture :
         * typeA : 1
         * typeB : 1
         * typeC : 1
         * typeD : 0
         * version : 1
         */

        private String content;
        private String id;
        private String name;
        private String picture;
        private String typeA;
        private String typeB;
        private String typeC;
        private String typeD;
        private String version;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getTypeA() {
            return typeA;
        }

        public void setTypeA(String typeA) {
            this.typeA = typeA;
        }

        public String getTypeB() {
            return typeB;
        }

        public void setTypeB(String typeB) {
            this.typeB = typeB;
        }

        public String getTypeC() {
            return typeC;
        }

        public void setTypeC(String typeC) {
            this.typeC = typeC;
        }

        public String getTypeD() {
            return typeD;
        }

        public void setTypeD(String typeD) {
            this.typeD = typeD;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
