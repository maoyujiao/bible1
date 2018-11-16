package com.iyuba.trainingcamp.db.dbclass;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.db.dbclass
 * @class describe
 * @time 2018/8/17 11:55
 * @change
 * @chang time
 * @class describe
 */
public class GoldDateRecordHelper {

    public static List<GoldDateRecord> queryData(){
        return DataSupport.findAll(GoldDateRecord.class);
    }

    public static List<GoldDateRecord> queryDate(String date){
        return DataSupport.where("date1 = ?" , date).find(GoldDateRecord.class);
    }

    public static List<GoldDateRecord> querybyid(String id){
        return DataSupport.where("lessonid = ?" , id).find(GoldDateRecord.class);
    }
}
