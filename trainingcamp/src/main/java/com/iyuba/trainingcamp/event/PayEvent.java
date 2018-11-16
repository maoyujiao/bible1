package com.iyuba.trainingcamp.event;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.event
 * @class describe
 * @time 2018/10/12 16:32
 * @change
 * @chang time
 * @class describe
 */
public class PayEvent {
    String price;
    String subject;
    int amount;
    String body;
    String out_trade_no ;

    public String getPrice() {
        return price;
    }

    public String getSubject() {
        return subject;
    }

    public int getAmount() {
        return amount;
    }

    public String getBody() {
        return body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public PayEvent(String price, String subject, int amount, String body , String out_trade_no) {
        this.price = price;
        this.subject = subject;
        this.amount = amount;
        this.body = body;
        this.out_trade_no = out_trade_no;
    }
}
