package com.iyuba.trainingcamp.event;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.event
 * @class describe
 * @time 2018/11/9 15:50
 * @change
 * @chang time
 * @class describe
 */
public class StarMicroEvent {
    int productid ;


    public StarMicroEvent(String id) {
        this.productid = Integer.parseInt(id);
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

}
