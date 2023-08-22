package com.model;

public class ColdairProcessProvince {

    //省名
    private String Iiiii;
    private Integer startDay;
    private Integer endDay;
    private Integer mediumCount;
    private Integer severeCount;
    private Integer hanchaoCount;
    private Integer strongHanchaoCount;//强寒潮站
    private Integer severeHanchaoCount;//超强寒潮
    private double I;//过程强度指数
    private double M;//过程综合强度指数
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIiiii() {
        return Iiiii;
    }

    public void setIiiii(String iiiii) {
        Iiiii = iiiii;
    }

    public Integer getStartDay() {
        return startDay;
    }

    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    public Integer getEndDay() {
        return endDay;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }

    public Integer getMediumCount() {
        return mediumCount;
    }

    public void setMediumCount(Integer mediumCount) {
        this.mediumCount = mediumCount;
    }

    public Integer getSevereCount() {
        return severeCount;
    }

    public void setSevereCount(Integer severeCount) {
        this.severeCount = severeCount;
    }

    public Integer getHanchaoCount() {
        return hanchaoCount;
    }

    public void setHanchaoCount(Integer hanchaoCount) {
        this.hanchaoCount = hanchaoCount;
    }

    public Integer getStrongHanchaoCount() {
        return strongHanchaoCount;
    }

    public void setStrongHanchaoCount(Integer strongHanchaoCount) {
        this.strongHanchaoCount = strongHanchaoCount;
    }

    public Integer getSevereHanchaoCount() {
        return severeHanchaoCount;
    }

    public void setSevereHanchaoCount(Integer severeHanchaoCount) {
        this.severeHanchaoCount = severeHanchaoCount;
    }
    public double getI() {
        return I;
    }

    public void setI(double i) {
        I = i;
    }

    public double getM() {
        return M;
    }

    public void setM(double m) {
        M = m;
    }
}
