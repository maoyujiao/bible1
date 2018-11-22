package com.iyuba.CET4bible.protocol;

/**
 * StudyRecordInfo
 *
 * @author wayne
 * @date 2017/11/17
 */
public class StudyRecordInfo {
    public String uid;
    public String BeginTime;
    public String EndTime;
    public String Lesson;
    public String LessonId;
    public String TestNumber;
    public String EndFlg;
    public String Device;
    public String IP;
    public String updateTime;
    public String DeviceId;

    public boolean IsUpload = false;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getLesson() {
        return Lesson;
    }

    public void setLesson(String lesson) {
        Lesson = lesson;
    }

    public String getLessonId() {
        return LessonId;
    }

    public void setLessonId(String lessonId) {
        LessonId = lessonId;
    }

    public String getTestNumber() {
        return TestNumber;
    }

    public void setTestNumber(String testNumber) {
        TestNumber = testNumber;
    }

    public String getEndFlg() {
        return EndFlg;
    }

    public void setEndFlg(String endFlg) {
        EndFlg = endFlg;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public boolean isIsUpload() {
        return IsUpload;
    }

    public void setIsUpload(boolean isUpload) {
        IsUpload = isUpload;
    }


}

