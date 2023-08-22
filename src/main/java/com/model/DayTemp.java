package com.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName(value = "SURF_CHN_MUL_DAY", schema = "public")
public class DayTemp implements Serializable {

    @TableField("Station_Id_C")
    private String stationIdC;
    @TableField("Datetime")
    private LocalDate datetime;
    @TableField("TEM_Avg")
    private Double temAvg;
    @TableField("TEM_Min")
    private Double temMin;
    @TableField("TEM_Max")
    private Double temMax;
}
