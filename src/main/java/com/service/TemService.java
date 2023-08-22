package com.service;

import com.model.ColdairProcessN;
import com.model.ColdairProcessNCity;
import com.model.ColdairProcessProvince;
import com.model.DayTemp;

import java.time.LocalDate;
import java.util.List;

public interface TemService {
    List<ColdairProcessN> getTemStation(String stationId, LocalDate startTime, LocalDate endTime);

    ColdairProcessN judgeProcess(List<DayTemp> list,int n);

    int judgeLevel(double dert24,double dert48,double dert72,double temMin);

    double processMax(List<DayTemp> list,int start,int end);

    List<ColdairProcessNCity> getTemCity(String provinceName, LocalDate startTime, LocalDate endTime);

    //根据时间判断当天的降温强度
    int getLevelOneDay(List<ColdairProcessN> list,LocalDate date);

    //计算第n天的dert24,dert48,dert72
    double[] getDert(List<DayTemp> list, int n);

    List<ColdairProcessProvince> getTemProvince(String provinceName, LocalDate startTime, LocalDate endTime);
}
