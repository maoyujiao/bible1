package com.iyuba.abilitytest.entity;

import java.io.Serializable;

/**
 * 分享加分
 * Created by Administrator on 2016/8/18.
 */
public class AddCreditModule implements Serializable {
//    {
//        "result": "200",
//            "addcredit": "5",
//            "totalcredit": "1145"
//    }
//
//    {
//        "result": "201",
//         "message": "本篇分享成功！分享新文章可获得积分呦！"
//    }
    public int result;
    public int addcredit;
    public int totalcredit;
    public String message;

}
