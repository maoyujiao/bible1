/**
 *
 */
package com.iyuba.core.me.sqlite.mode;

/**
 * 编辑信息
 *
 * @author 陈彤
 */
public class EditUserInfo {
    private String edGender;// 只能为1或2
    private int edBirthDay;// 日，必须为数字
    private int edBirthYear;// 年，必须为数字
    private int edBirthMonth;// 月，必须为数字
    private String edResideProvince;// 现居省
    private String edResideCity;// 现居市
    private String edResideDistrict;// 现居区
    private String edZodiac;// 生肖
    private String edConstellation;// 星座
    private String birthday;
    private String university;

    private String edEducation;        //学历
    private String edOccupation;    //职业

    private String edAffectivestatus;//情感状态
    private String edLookingfor;//交友目的
    private String edIntro;//自我介绍

    private String plevel, ptag, glevel, gtag, ptalklevel, ptalktag,
            gtalklevel, gtalktag, preadlevel, preadtag, greadlevel, greadtag;
    private String edInterest;//兴趣爱好

    public String getEdEducation() {
        return edEducation;
    }

    public void setEdEducation(String edEducation) {
        this.edEducation = edEducation;
    }

    public String getEdOccupation() {
        return edOccupation;
    }

    public void setEdOccupation(String edOccupation) {
        this.edOccupation = edOccupation;
    }

    public String getEdLookingfor() {
        return edLookingfor;
    }

    public void setEdLookingfor(String edLookingfor) {
        this.edLookingfor = edLookingfor;
    }

    public String getEdIntro() {
        return edIntro;
    }

    public void setEdIntro(String edIntro) {
        this.edIntro = edIntro;
    }

    public String getEdInterest() {
        return edInterest;
    }

    public void setEdInterest(String edInterest) {
        this.edInterest = edInterest;
    }

    public String getEdAffectivestatus() {
        return edAffectivestatus;
    }

    public void setEdAffectivestatus(String edAffectivestatus) {
        this.edAffectivestatus = edAffectivestatus;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getEdGender() {
        return edGender;
    }

    public void setEdGender(String edGender) {
        this.edGender = edGender;
    }

    public int getEdBirthDay() {
        return edBirthDay;
    }

    public void setEdBirthDay(int edBirthDay) {
        this.edBirthDay = edBirthDay;
    }

    public int getEdBirthYear() {
        return edBirthYear;
    }

    public void setEdBirthYear(int edBirthYear) {
        this.edBirthYear = edBirthYear;
    }

    public int getEdBirthMonth() {
        return edBirthMonth;
    }

    public void setEdBirthMonth(int edBirthMonth) {
        this.edBirthMonth = edBirthMonth;
    }

    public String getEdResideCity() {
        return edResideCity;
    }

    public void setEdResideCity(String edResideCity) {
        this.edResideCity = edResideCity;
    }

    public String getEdResideProvince() {
        return edResideProvince;
    }

    public void setEdResideProvince(String edResideProvince) {
        this.edResideProvince = edResideProvince;
    }

    public String getEdResideDistrict() {
        return edResideDistrict;
    }

    public void setEdResideDistrict(String edResideDistrict) {
        this.edResideDistrict = edResideDistrict;
    }

    public String getEdZodiac() {
        return edZodiac;
    }

    public void setEdZodiac(String edZodiac) {
        this.edZodiac = edZodiac;
    }

    public String getEdConstellation() {
        return edConstellation;
    }

    public void setEdConstellation(String edConstellation) {
        this.edConstellation = edConstellation;
    }

    public String getGreadtag() {
        return greadtag;
    }

    public void setGreadtag(String greadtag) {
        this.greadtag = greadtag;
    }

    public String getPlevel() {
        return plevel;
    }

    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    public String getPtag() {
        return ptag;
    }

    public void setPtag(String ptag) {
        this.ptag = ptag;
    }

    public String getGlevel() {
        return glevel;
    }

    public void setGlevel(String glevel) {
        this.glevel = glevel;
    }

    public String getGtag() {
        return gtag;
    }

    public void setGtag(String gtag) {
        this.gtag = gtag;
    }

    public String getPtalklevel() {
        return ptalklevel;
    }

    public void setPtalklevel(String ptalklevel) {
        this.ptalklevel = ptalklevel;
    }

    public String getPtalktag() {
        return ptalktag;
    }

    public void setPtalktag(String ptalktag) {
        this.ptalktag = ptalktag;
    }

    public String getGtalklevel() {
        return gtalklevel;
    }

    public void setGtalklevel(String gtalklevel) {
        this.gtalklevel = gtalklevel;
    }

    public String getGtalktag() {
        return gtalktag;
    }

    public void setGtalktag(String gtalktag) {
        this.gtalktag = gtalktag;
    }

    public String getPreadlevel() {
        return preadlevel;
    }

    public void setPreadlevel(String preadlevel) {
        this.preadlevel = preadlevel;
    }

    public String getPreadtag() {
        return preadtag;
    }

    public void setPreadtag(String preadtag) {
        this.preadtag = preadtag;
    }

    public String getGreadlevel() {
        return greadlevel;
    }

    public void setGreadlevel(String greadlevel) {
        this.greadlevel = greadlevel;
    }

}
