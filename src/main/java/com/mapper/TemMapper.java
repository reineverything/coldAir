package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.model.DayTemp;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TemMapper extends BaseMapper<DayTemp> {
    List<DayTemp> selectByStation(String stationId, LocalDate startTime, LocalDate endTime);

    List<String> selectCitiesInProvince(String provinceName);

    List<String> selectStationByProvince(String provinceName);

    List<String> selectStationInCity(String city);
}
