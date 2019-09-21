/**
 *
 */
package com.iyuba.core.me.sqlite.mode;


/**
 * 动态
 *
 * @author renzhy
 */
public class NewDoingsInfo {

    public String id;        // 动态id,有可能是问题ID，日志ID等等
    public String uid;        //用户ID
    public String body;        //获取用户动态的内容
    public String feedid;    //对应的唯一ID
    public String title;    //标题
    public String username;    //用户名
    public String audio;    //如果有音频文件，对应的地址
    public String idtype;    //ID对应的类型
    public String image;    //如果有图片对应的标签
    public String hot;        //是否热门
    public String dateline;    //时间戳
    public String replynum;    //回复数

}
