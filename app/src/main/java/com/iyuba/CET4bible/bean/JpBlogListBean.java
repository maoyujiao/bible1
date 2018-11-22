package com.iyuba.CET4bible.bean;

import java.io.Serializable;
import java.util.List;

public class JpBlogListBean {

    /**
     * result : 251
     * message : OK
     * blogCounts : 426
     * pageNumber : 2
     * firstPage : 1
     * prevPage : 1
     * nextPage : 3
     * lastPage : 22
     * data : [{"viewnum":"2896","password":"","dateline":"1466042268","noreply":"0","subject":"N3文法每日一句（67）","sharetimes":"0","replynum":"1","friend":"0","pic":"","blogid":"15692","favtimes":"0"},{"viewnum":"2246","password":"","dateline":"1465897627","noreply":"0","subject":"N3文法每日一句（66）","sharetimes":"0","replynum":"2","friend":"0","pic":"","blogid":"15668","favtimes":"0"},{"viewnum":"1697","password":"","dateline":"1465812137","noreply":"0","subject":"N3文法每日一句（65）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15633","favtimes":"0"},{"viewnum":"1388","password":"","dateline":"1465726602","noreply":"0","subject":"N3文法每日一句（64）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15615","favtimes":"0"},{"viewnum":"1360","password":"","dateline":"1465365619","noreply":"0","subject":"N3文法每日一句（63）","sharetimes":"0","replynum":"4","friend":"0","pic":"","blogid":"15553","favtimes":"0"},{"viewnum":"1272","password":"","dateline":"1465286957","noreply":"0","subject":"N3文法每日一句（62）","sharetimes":"0","replynum":"2","friend":"0","pic":"","blogid":"15534","favtimes":"0"},{"viewnum":"1105","password":"","dateline":"1465205040","noreply":"0","subject":"N3文法每日一句（61）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15514","favtimes":"0"},{"viewnum":"1083","password":"","dateline":"1464852784","noreply":"0","subject":"N3文法每日一句（60）","sharetimes":"0","replynum":"2","friend":"0","pic":"","blogid":"15437","favtimes":"0"},{"viewnum":"962","password":"","dateline":"1464750393","noreply":"0","subject":"N3文法每日一句（59）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15415","favtimes":"0"},{"viewnum":"805","password":"","dateline":"1464661163","noreply":"0","subject":"N3文法每日一句（58）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15384","favtimes":"0"},{"viewnum":"758","password":"","dateline":"1464605273","noreply":"0","subject":"N3文法每日一句（57）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15380","favtimes":"0"},{"viewnum":"699","password":"","dateline":"1464338411","noreply":"0","subject":"N3文法每日一句（56）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15336","favtimes":"0"},{"viewnum":"668","password":"","dateline":"1464243196","noreply":"0","subject":"N3文法每日一句（55）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15305","favtimes":"0"},{"viewnum":"714","password":"","dateline":"1464170641","noreply":"0","subject":"N3文法每日一句（54）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15289","favtimes":"0"},{"viewnum":"620","password":"","dateline":"1463999356","noreply":"0","subject":"N3文法每日一句（53）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15260","favtimes":"0"},{"viewnum":"704","password":"","dateline":"1463724896","noreply":"0","subject":"N3文法每日一句（52）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15192","favtimes":"0"},{"viewnum":"630","password":"","dateline":"1463651101","noreply":"0","subject":"N3文法每日一句（51）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15185","favtimes":"0"},{"viewnum":"668","password":"","dateline":"1463563548","noreply":"0","subject":"N3文法每日一句（50）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15167","favtimes":"0"},{"viewnum":"1113","password":"","dateline":"1463393229","noreply":"0","subject":"N3文法每日一句（49）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15135","favtimes":"0"},{"viewnum":"447","password":"","dateline":"1463132850","noreply":"0","subject":"N3文法每日一句（48）","sharetimes":"0","replynum":"0","friend":"0","pic":"","blogid":"15077","favtimes":"0"}]
     */

    private int result;
    private String message;
    private int blogCounts;
    private int pageNumber;
    private int firstPage;
    private int prevPage;
    private int nextPage;
    private int lastPage;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBlogCounts() {
        return blogCounts;
    }

    public void setBlogCounts(int blogCounts) {
        this.blogCounts = blogCounts;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * viewnum : 2896
         * password :
         * dateline : 1466042268
         * noreply : 0
         * subject : N3文法每日一句（67）
         * sharetimes : 0
         * replynum : 1
         * friend : 0
         * pic :
         * blogid : 15692
         * favtimes : 0
         */

        private String viewnum;
        private String password;
        private String dateline;
        private String noreply;
        private String subject;
        private String sharetimes;
        private String replynum;
        private String friend;
        private String pic;
        private String blogid;
        private String favtimes;

        public String getViewnum() {
            return viewnum;
        }

        public void setViewnum(String viewnum) {
            this.viewnum = viewnum;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getNoreply() {
            return noreply;
        }

        public void setNoreply(String noreply) {
            this.noreply = noreply;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSharetimes() {
            return sharetimes;
        }

        public void setSharetimes(String sharetimes) {
            this.sharetimes = sharetimes;
        }

        public String getReplynum() {
            return replynum;
        }

        public void setReplynum(String replynum) {
            this.replynum = replynum;
        }

        public String getFriend() {
            return friend;
        }

        public void setFriend(String friend) {
            this.friend = friend;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getBlogid() {
            return blogid;
        }

        public void setBlogid(String blogid) {
            this.blogid = blogid;
        }

        public String getFavtimes() {
            return favtimes;
        }

        public void setFavtimes(String favtimes) {
            this.favtimes = favtimes;
        }
    }
}
