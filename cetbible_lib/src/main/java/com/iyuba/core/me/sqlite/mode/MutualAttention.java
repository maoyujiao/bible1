/**
 *
 */
package com.iyuba.core.me.sqlite.mode;

import android.graphics.Bitmap;

/**
 * 相互关注
 *
 * @author 陈彤
 */
public class MutualAttention {
    public String fusername;// 关注者用户名
    public String followuid;// 关注者uid
    public String bkname;// 关注者备注
    public String status;// 状态0正常关注 1特殊关注 -1不在关注
    public String dateline;// 添加关注的时间，系统秒数
    public Bitmap userBitmap;
    public boolean isvip;
}
