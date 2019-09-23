package com.iyuba.abilitytest.network;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.network
 * @class describe
 * @time 2019/1/26 15:14
 * @change
 * @chang time
 * @class describe
 */
public class PublishVoiceResponse {
    private String FilePath;
    private int AddScore;
    private String Message;
    private int ShuoshuoId;
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
        return ShuoshuoId;
    }

    public void setShuoShuoId(int ShuoShuoId) {
        this.ShuoshuoId = ShuoShuoId;
    }

    public int getResultCode() {
        return ResultCode;
    }

    public void setResultCode(int ResultCode) {
        this.ResultCode = ResultCode;
    }
}
