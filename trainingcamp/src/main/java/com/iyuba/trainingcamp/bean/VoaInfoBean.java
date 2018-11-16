package com.iyuba.trainingcamp.bean;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.bean
 * @class describe
 * @time 2018/11/6 18:29
 * @change
 * @chang time
 * @class describe
 */
public class VoaInfoBean {

    /**
     * total : 2
     * data : [{"DescCn":"罗尔德·达尔是一位让人喜爱的英国作家。","CreatTime":"2016-09-16 09:21:15.0","Category":"2","Title_cn":"英国作家罗尔德·达尔百年诞辰","Title":"Roald Dahl Day Celebrates British Writer\u2019s 100th Birthday ","Sound":"/201609/4088.mp3","PublishTime":"","HotFlg":"1","Pic":"http://static.iyuba.com/images/voa/4088.jpg","VoaId":"4088","Url":"http://learningenglish.voanews.com/a/world-celebrates-roald-dahl-day-trending-today/3505675.html","ReadCount":"103281"},{"DescCn":"商人Jeff Bezos公布了两个新型火箭，计划用来将卫星和人们送进太空。大多数人都知道Bezos为网上卖家亚马逊的创始人兼首席执行官。不过，火箭设计来自他的私人太空公司\u2014\u2014Blue Origin。","CreatTime":"2016-09-15 15:19:41.0","Category":"8","Title_cn":"新型火箭现身私人太空竞赛","Title":"New Rockets Announced in Private Space Race","Sound":"/201609/4086.mp3","PublishTime":"","HotFlg":"1","Pic":"http://static.iyuba.com/images/voa/4086.jpg","VoaId":"4086","Url":"http://learningenglish.voanews.com/a/jeff-bezos-announces-new-blue-origin-rockets-in-private-space-race-with-spacex/3505814.html","ReadCount":"100887"}]
     */

    private String total;
    private List<DataBean> data;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * DescCn : 罗尔德·达尔是一位让人喜爱的英国作家。
         * CreatTime : 2016-09-16 09:21:15.0
         * Category : 2
         * Title_cn : 英国作家罗尔德·达尔百年诞辰
         * Title : Roald Dahl Day Celebrates British Writer’s 100th Birthday
         * Sound : /201609/4088.mp3
         * PublishTime :
         * HotFlg : 1
         * Pic : http://static.iyuba.com/images/voa/4088.jpg
         * VoaId : 4088
         * Url : http://learningenglish.voanews.com/a/world-celebrates-roald-dahl-day-trending-today/3505675.html
         * ReadCount : 103281
         */

        private String DescCn;
        private String CreatTime;
        private String Category;
        private String Title_cn;
        private String Title;
        private String Sound;
        private String PublishTime;
        private String HotFlg;
        private String Pic;
        private String VoaId;
        private String Url;
        private String ReadCount;

        public String getDescCn() {
            return DescCn;
        }

        public void setDescCn(String DescCn) {
            this.DescCn = DescCn;
        }

        public String getCreatTime() {
            return CreatTime;
        }

        public void setCreatTime(String CreatTime) {
            this.CreatTime = CreatTime;
        }

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public String getTitle_cn() {
            return Title_cn;
        }

        public void setTitle_cn(String Title_cn) {
            this.Title_cn = Title_cn;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getSound() {
            return Sound;
        }

        public void setSound(String Sound) {
            this.Sound = Sound;
        }

        public String getPublishTime() {
            return PublishTime;
        }

        public void setPublishTime(String PublishTime) {
            this.PublishTime = PublishTime;
        }

        public String getHotFlg() {
            return HotFlg;
        }

        public void setHotFlg(String HotFlg) {
            this.HotFlg = HotFlg;
        }

        public String getPic() {
            return Pic;
        }

        public void setPic(String Pic) {
            this.Pic = Pic;
        }

        public String getVoaId() {
            return VoaId;
        }

        public void setVoaId(String VoaId) {
            this.VoaId = VoaId;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public String getReadCount() {
            return ReadCount;
        }

        public void setReadCount(String ReadCount) {
            this.ReadCount = ReadCount;
        }
    }
}
