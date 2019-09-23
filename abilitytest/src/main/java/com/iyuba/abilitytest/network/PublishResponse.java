package com.iyuba.abilitytest.network;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.network
 * @class describe
 * @time 2019/1/19 18:58
 * @change
 * @chang time
 * @class describe
 */
public class PublishResponse {

    /**
     * FilePath : shuoshuo/2019/0/22/16/57/4/3441353a-bee2-4a07-b842-2b8dfc459ac3.amr
     * AddScore : 5
     * Message : OK
     * ShuoShuoId : 8385477
     * ResultCode : 1
     */

    private String FilePath;
    private int AddScore;
    private String Message;
    private int ShuoShuoId;
    private int ResultCode;

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

    public int getAddScore() {
        return AddScore;
    }

    public void setAddScore(int AddScore) {
        this.AddScore = AddScore;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getShuoShuoId() {
        return ShuoShuoId;
    }

    public void setShuoShuoId(int ShuoShuoId) {
        this.ShuoShuoId = ShuoShuoId;
    }

    public int getResultCode() {
        return ResultCode;
    }

    public void setResultCode(int ResultCode) {
        this.ResultCode = ResultCode;
    }
}
