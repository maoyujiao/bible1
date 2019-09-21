/**
 *
 */
package com.iyuba.core.discover.sqlite.mode;

/**
 * @author yao
 *         日志类,基本信息
 */
public class BlogInfo {

    public String result;//	返回代码
    public String message;//	返回信息
    public String blogCounts;//		总日志数
    public String pageNumber;//		当前页数
    public String firstPage;//	首页
    public String prevPage;//	上一页
    public String nextPage;//	下一页
    public String lastPage;//	最后一页
    public String data;//日志信息

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBlogCounts() {
        return blogCounts;
    }

    public void setBlogCounts(String blogCounts) {
        this.blogCounts = blogCounts;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }


}
