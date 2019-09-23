package com.iyuba.abilitytest.network;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.network
 * @class describe
 * @time 2019/1/26 10:04
 * @change
 * @chang time
 * @class describe
 */
public class SendGoodResponse {

    /**
     * ResultCode : 10
     * Message : id
     */

    private String ResultCode;
    private String Message;

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String ResultCode) {
        this.ResultCode = ResultCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
