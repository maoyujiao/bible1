package com.iyuba.CET4bible.bean;

public class IyubaADBean {

    /**
     * result : 1
     * data : {"id":"676","adId":"VOA慢速英语华尔街banneriOSLearn-Word8-30","startuppic_StartDate":"2017-08-30","startuppic_EndDate":"2017-09-05","startuppic":"upload/1504064808441.jpg","type":"web","startuppic_Url":"http://dev.iyuba.cn/ad.jsp?adId=676&uid=0&appId=104","classNum":"0"}
     */

    private String result;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 676
         * adId : VOA慢速英语华尔街banneriOSLearn-Word8-30
         * startuppic_StartDate : 2017-08-30
         * startuppic_EndDate : 2017-09-05
         * startuppic : upload/1504064808441.jpg
         * type : web
         * startuppic_Url : http://dev.iyuba.cn/ad.jsp?adId=676&uid=0&appId=104
         * classNum : 0
         */

        private String id;
        private String adId;
        private String startuppic_StartDate;
        private String startuppic_EndDate;
        private String startuppic;
        private String type;
        private String startuppic_Url;
        private String classNum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAdId() {
            return adId;
        }

        public void setAdId(String adId) {
            this.adId = adId;
        }

        public String getStartuppic_StartDate() {
            return startuppic_StartDate;
        }

        public void setStartuppic_StartDate(String startuppic_StartDate) {
            this.startuppic_StartDate = startuppic_StartDate;
        }

        public String getStartuppic_EndDate() {
            return startuppic_EndDate;
        }

        public void setStartuppic_EndDate(String startuppic_EndDate) {
            this.startuppic_EndDate = startuppic_EndDate;
        }

        public String getStartuppic() {
            return startuppic;
        }

        public void setStartuppic(String startuppic) {
            this.startuppic = startuppic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStartuppic_Url() {
            return startuppic_Url;
        }

        public void setStartuppic_Url(String startuppic_Url) {
            this.startuppic_Url = startuppic_Url;
        }

        public String getClassNum() {
            return classNum;
        }

        public void setClassNum(String classNum) {
            this.classNum = classNum;
        }
    }
}
