package com.controller;

import com.model.ColdairProcessN;
import com.model.ColdairProcessNCity;
import com.model.ColdairProcessProvince;
import com.service.TemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cold")
public class TemController {

    @Autowired
    private TemService temService;

    //计算单站24小时，48小时，72小时气温降幅
    @GetMapping("/single/{stationId}/{startTime}/{endTime}")
    public List<ColdairProcessN> getSingleTem(@PathVariable String stationId, @PathVariable LocalDate startTime,@PathVariable LocalDate endTime){
        return temService.getTemStation(stationId,startTime,endTime);
    }


    /**
     * 计算一个城市的降温信息，用不到
     * @param areaName 名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 区域冷空气过程
     */
    @GetMapping("/province/{areaName}/{startTime}/{endTime}")
    public List<ColdairProcessNCity> getAreaTem(@PathVariable String areaName,@PathVariable LocalDate startTime,@PathVariable LocalDate endTime){
        return temService.getTemCity(areaName,startTime,endTime);
    }

    /**
     * 湖北省的区域降温，不考虑省内城市
     */
    @PostMapping ("/province/{provinceName}/{startTime}/{endTime}")
    public List<ColdairProcessProvince> getProvinceTem(@PathVariable String provinceName,@PathVariable LocalDate startTime,@PathVariable LocalDate endTime){
        return temService.getTemProvince(provinceName,startTime,endTime);
    }

}
