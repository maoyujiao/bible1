/**
 *
 */
package com.iyuba.core.discover.sqlite.mode;


/**
 * @author yao
 *         日志内容。回复数等详细信息
 */
public class BlogContent {
    public String blogid = null;//	日志id
    public String subject = null;//	日志标题
    public String viewnum = null;//	日志查看数
    public String replynum = null;//	回复数
    public String dateline = null;//	发布时间，此为系统秒数
    public String noreply = null;//	是否允许评论
    public String friend = null;//	隐私设置
    public String password = null;//	日志密码
    public String favtimes = null;//	收藏次数
    public String sharetimes = null;//		分享次数
    public String message = null;//	日志内容
    public String ids = null;//	允许查看日志的id
    public String username = null;//日志作者
    public String uid = null;//日志作者id
    public String mp3flag = null;//1为音频，2为视频，0啥都没有
    public String mp3path = null;//音频或者视频的地址
    public String fromid = null;//视频图片的地址
}

