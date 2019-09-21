/**
 *
 */
package com.iyuba.core.me.sqlite.mode;

import android.graphics.Bitmap;

/**
 * 关注
 *
 * @author 陈彤
 */
public class Attention {
    public String fusername;// 关注者用户名
    public String followuid;// 关注者uid
    public String bkname;// 关注者备注
    public String status;// 状态0正常关注 1特殊关注 -1不在关注
    public String dateline;// 添加关注的时间，系统秒数
    public String mutual;// 是否互相关注，1为是
    public Bitmap userBitmap;
    public String doing;
}
