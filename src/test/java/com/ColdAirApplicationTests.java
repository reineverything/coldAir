package com;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
class ColdAirApplicationTests {

    @Test
    void contextLoads() {
        double value = 5.9999999;
        double v = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
//        bd = bd.setScale(2, RoundingMode.HALF_UP); // 将浮点数舍入到2位小数，使用ROUND_HALF_UP方式进行舍入
        System.out.println(v);
    }



}
