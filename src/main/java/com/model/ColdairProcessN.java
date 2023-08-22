package com.model;

import java.io.Serializable;

/**
 * 单站冷空气
 */
public class ColdairProcessN implements Serializable {

    private String iiiii;
    private Integer startDay;
    private Integer endDay;
    private Integer n;
    private Double maxtd;
    private Double sumtd;
    private Double tmin;
    private Double dert24;
    private Double dert48;
    private Double dert72;
    private Integer level;
    private String label;

    public String getIiiii() {
        return iiiii;
    }

    public void setIiiii(String iiiii) {
        this.iiiii = iiiii;
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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getMaxtd() {
        return maxtd;
    }

    public void setMaxtd(Double maxtd) {
        this.maxtd = maxtd;
    }

    public Double getSumtd() {
        return sumtd;
    }

    public void setSumtd(Double sumtd) {
        this.sumtd = sumtd;
    }

    public Double getTmin() {
        return tmin;
    }

    public void setTmin(Double tmin) {
        this.tmin = tmin;
    }

    public Double getDert24() {
        return dert24;
    }

    public void setDert24(Double dert24) {
        this.dert24 = dert24;
    }

    public Double getDert48() {
        return dert48;
    }

    public void setDert48(Double dert48) {
        this.dert48 = dert48;
    }

    public Double getDert72() {
        return dert72;
    }

    public void setDert72(Double dert72) {
        this.dert72 = dert72;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
