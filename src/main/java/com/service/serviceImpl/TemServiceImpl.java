package com.service.serviceImpl;

import com.mapper.TemMapper;
import com.model.ColdairProcessN;
import com.model.ColdairProcessNCity;
import com.model.ColdairProcessProvince;
import com.model.DayTemp;
import com.service.TemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class TemServiceImpl implements TemService {

    @Autowired
    private TemMapper temMapper;


    @Override
    public List<ColdairProcessN> getTemStation(String stationId, LocalDate startTime, LocalDate endTime) {
        List<DayTemp> dayTemps = temMapper.selectByStation(stationId, startTime, endTime);
        //对列表中每个数据进行转换
        for (DayTemp dayTemp : dayTemps) {
            double temMin = dayTemp.getTemMin();
            BigDecimal bd = new BigDecimal(Double.toString(temMin));  //数据库中读取java精度丢失
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            dayTemp.setTemMin(bd.doubleValue());
        }
//        System.out.println(dayTemps);
        //对于每一天都要计算dert24,dert48,dert72

        List<ColdairProcessN> result = new ArrayList<>();//经过最终结果过程，起止和持续天数
        int i = 1, j;
        while (i < dayTemps.size()) {
            ColdairProcessN coldairProcessN1 = judgeProcess(dayTemps, i);
            if (coldairProcessN1.getLevel() == 0) { //第一天没有达到降温效果
                i++;
            } else {
                int length = 1;//记录降温过程的天数，只有>=3天才会被保存
                double temperature = dayTemps.get(i).getTemMin();
                j = i + 1;
                while (j < dayTemps.size()) {
                    if (dayTemps.get(j).getTemMin() < temperature) {//降温了
                        temperature = dayTemps.get(j).getTemMin();
                        length++;
                        j++;
                    } else {
                        //当温度上升，判断长度是否满足，保存
                        if (length >= 2) {
                            //i到j-1(i+length-1)满足条件
                            ColdairProcessN coldairProcessN2 = new ColdairProcessN();
                            coldairProcessN2.setIiiii(stationId);

                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd"); //localDate->int
                            coldairProcessN2.setStartDay(Integer.valueOf(dtf.format(dayTemps.get(i).getDatetime())));
                            coldairProcessN2.setEndDay(Integer.valueOf(dtf.format(dayTemps.get(j - 1).getDatetime())));
                            coldairProcessN2.setN(length);

                            /**
                             * 计算整个过程的dert24，dert48,dert72
                             */
                            double[] dert24s = new double[length];
                            double[] dert48s = new double[length];
                            double[] dert72s = new double[length];
                            double[] temMins = new double[length];
                            double dert24, dert48, dert72, temMin, maxtd, sumtd;
                            int level;
                            String label = null;
                            for (int k = 0; k < length; k++) {
                                dert24s[k] = judgeProcess(dayTemps, i + k).getDert24();
                                dert48s[k] = judgeProcess(dayTemps, i + k).getDert48();
                                dert72s[k] = judgeProcess(dayTemps, i + k).getDert72();
                                temMins[k] = judgeProcess(dayTemps, i + k).getTmin();
                            }

                            Arrays.sort(dert24s);
                            Arrays.sort(dert48s);
                            Arrays.sort(dert72s);
                            Arrays.sort(temMins);
                            dert24 = dert24s[0];
                            dert48 = dert48s[0];
                            dert72 = dert72s[0];
                            temMin = temMins[0];
                            sumtd = dayTemps.get(i - 1).getTemMin() - dayTemps.get(j - 1).getTemMin();
                            maxtd = -dert24;
                            level = judgeLevel(dert24, dert48, dert72, temMin);
                            if (level >= 3) {
                                label = "hanchao";
                            } else if (level == 2) {
                                label = "servere";
                            } else if (level == 1) {
                                label = "Medium";
                            }


                            coldairProcessN2.setDert24(dert24);
                            coldairProcessN2.setDert48(dert48);
                            coldairProcessN2.setDert72(dert72);
                            coldairProcessN2.setTmin(temMin);
                            coldairProcessN2.setMaxtd(maxtd);
                            coldairProcessN2.setSumtd(sumtd);

                            coldairProcessN2.setLevel(Math.min(level, 3));
                            coldairProcessN2.setLabel(label);
                            result.add(coldairProcessN2);
                        }
                        break;
                    }
                }
                i = j;
            }
        }


        return result;
    }

    /**
     * 判断第n天是否满足条件
     * @param list 降温过程
     * @param n    第n天
     * @return 这一天的等级
     */
    @Override
    public ColdairProcessN judgeProcess(List<DayTemp> list, int n) {

        if (list.size() <= n) {
            ColdairProcessN coldairProcessN = new ColdairProcessN();
            coldairProcessN.setLevel(0);
            return coldairProcessN;
        }


        int level;
        double temMin;
        double[] dert = getDert(list, n);

        temMin = list.get(n).getTemMin();
        //进行类型判断
        level = judgeLevel(dert[0], dert[1], dert[2], temMin);

        ColdairProcessN coldairProcessN = new ColdairProcessN();
        coldairProcessN.setTmin(temMin);
        coldairProcessN.setDert24(dert[0]);
        coldairProcessN.setDert48(dert[1]);
        coldairProcessN.setDert72(dert[2]);
        coldairProcessN.setLevel(level);
        return coldairProcessN;
    }

    @Override
    public int judgeLevel(double dert24, double dert48, double dert72, double temMin) {
        int level;

        if ((dert24 <= -12 || dert48 <= -14 || dert72 <= -16) && temMin <= 0) {
            level = 5;
        } else if ((dert24 <= -10 || dert48 <= -12 || dert72 <= -14) && temMin <= 2) {
            level = 4;
        } else if ((dert24 <= -8 || dert48 <= -10 || dert72 <= -12) && temMin <= 4) {
            level = 3;
        } else if (dert48 <= -8) {
            level = 2;
        } else if (dert48 <= -6) {
            level = 1;
        } else {
            level = 0;
        }
        System.out.println(dert24 + " " + dert48 + " " + dert72 + " " + level);
        return level;
    }


    /**
     * 计算下标从start到end中最大的最低温度
     *
     * @param list  降温过程
     * @param start 开始时间
     * @param end   结束时间
     * @return 最大温度
     */
    @Override
    public double processMax(List<DayTemp> list, int start, int end) {


        int length = end - start + 1;
        double[] temMin = new double[length];
        for (int i = 0; i < length; i++) {
            temMin[i] = list.get(start + i).getTemMin();
        }
        Arrays.sort(temMin);
        return temMin[length - 1];
    }

    /**
     * 根据一个省（区域）计算所有的区域冷空气过程强度
     *
     * @param provinceName 省名
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 降温过程
     */
    @Override
    public List<ColdairProcessNCity> getTemCity(String provinceName, LocalDate startTime, LocalDate endTime) {

        //根据省名得到省内所有市区的名称
        List<String> cities = temMapper.selectCitiesInProvince(provinceName);
        int day = (int) ChronoUnit.DAYS.between(startTime, endTime);//两个日期之间相隔天数
        List<ColdairProcessNCity> coldairProcessNCities = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd"); //localDate->int

        for (String city : cities) {//city:武汉市
            //计算武汉市所有的站号
            List<String> stations = temMapper.selectStationInCity(city);

            int[][] levels = new int[stations.size()][day];//存放每个站每天降温等级
            double[] rate = new double[day];//比例

            //对于这么所有的站，计算相应的每一天的强度等级
            //i表示整个区域所有的气象站，j表示每一天
            for (int i = 0; i < stations.size(); i++) {
//                List<ColdairProcessN> temStation = getTemStation(stations.get(i), startTime, endTime);//每个站点的降温过程
                List<DayTemp> dayTemps = temMapper.selectByStation(stations.get(i), startTime, endTime);//站点整体的信息
                for (int j = 1; j < day; j++) {
                    //计算每天的冷空气强度
                    ColdairProcessN coldairProcessN = judgeProcess(dayTemps, j);
                    levels[i][j] = coldairProcessN.getLevel();
                }
            }

            //判断每一列计算N1,N2,N3，i表示列，j表示行
            for (int i = 0; i < levels[0].length; i++) {
                int N1 = 0, N2 = 0, N3 = 0;
                for (int[] level : levels) {
                    if (level[i] == 1) {
                        N1++;
                    } else if (level[i] == 2) {
                        N2++;
                    } else if (level[i] >= 3) {
                        N3++;
                    }
                }
                double sum = N1 + N2 + N3;
                rate[i] = sum / levels.length;
            }


            //接下来，根据rate计算至少连续两天rate>=0.2的过程
            int i = 0, j;
            while (i < rate.length) {
                if (rate[i] < 0.2) {
                    i++;
                } else {
                    int length = 1;
                    j = i + 1;
                    while (j < rate.length) {
                        if (rate[j] >= 0.2) {
                            j++;
                            length++;
                        } else {//冷空气过程结束
                            if (length >= 2) {
                                //这就是一次区域冷空气

                                //判断单站强度的最大值
                                int n1 = 0, n2 = 0, n3 = 0;
                                double processSum = n1 + n2 + n3;
                                double I;//区域过程强度指数
                                I = (3 * n3 + 2 * n2 + n1) / processSum;
                                if (I >= 1.95) {

                                }

                                ColdairProcessNCity coldairProcessNCity = new ColdairProcessNCity();
                                coldairProcessNCity.setIiiii(city);
                                coldairProcessNCity.setStartDay(Integer.valueOf(dtf.format(startTime.plusDays(i))));
                                coldairProcessNCity.setEndDay(Integer.valueOf(dtf.format(startTime.plusDays(j - 1))));
                                coldairProcessNCity.setN(length);
                                coldairProcessNCities.add(coldairProcessNCity);
//                                System.out.println(coldairProcessNCity);
                            }
                            break;
                        }
                    }
                    i = j;
                }
            }
        }
        return coldairProcessNCities;
    }

    @Override
    public int getLevelOneDay(List<ColdairProcessN> list, LocalDate date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd"); //localDate->int
        int dateTime = Integer.parseInt(dtf.format(date));

        //判断dateTime是否在startTime和endTime之间，二分查找判断
        int i = 0, j = list.size() - 1;
        while (i <= j) {
            int mid = (i + j) / 2;
            ColdairProcessN coldairProcessN = list.get(mid);
            Integer startTime = coldairProcessN.getStartDay();
            Integer endTime = coldairProcessN.getEndDay();

            if (startTime > dateTime) {
                j = mid - 1;
            } else if (endTime >= dateTime) {
                //在该范围内
                return coldairProcessN.getLevel();
            } else {
                i = mid + 1;
            }
        }

        return 0;//没找到
    }

    /**
     * 计算站点第n天的dert值
     *
     * @param list 站点过程信息
     * @param n    第n天
     * @return dert数组
     */
    @Override
    public double[] getDert(List<DayTemp> list, int n) {
        double dert24, dert48, dert72;
        double[] dert = new double[3];
        dert24 = list.get(n).getTemMin() - list.get(n - 1).getTemMin();

        if (n >= 3) {
            dert48 = list.get(n).getTemMin() - processMax(list, n - 2, n - 1);
            dert72 = list.get(n).getTemMin() - processMax(list, n - 3, n - 1);
        } else if (n == 2) {
            dert48 = list.get(n).getTemMin() - processMax(list, 0, n - 1);
            dert72 = dert48;
        } else {
            dert48 = dert24;
            dert72 = dert24;
        }
        dert[0] = new BigDecimal(dert24).setScale(2, RoundingMode.HALF_UP).doubleValue();
        dert[1] = new BigDecimal(dert48).setScale(2, RoundingMode.HALF_UP).doubleValue();
        dert[2] = new BigDecimal(dert72).setScale(2, RoundingMode.HALF_UP).doubleValue();

        return dert;
    }

    @Override
    public List<ColdairProcessProvince> getTemProvince(String provinceName, LocalDate startTime, LocalDate endTime) {

        //根据省份查找全省的站点信息
        String[] stations = {"57249", "57251", "57253", "57256", "57257", "57259", "57260", "57265", "57268", "57278", "57279", "57355", "57358",
                "57359", "57361", "57362", "57363", "57368", "57370", "57377", "57378", "57381", "57385", "57387", "57388", "57389", "57395", "57398",
                "57399", "57439", "57445", "57447", "57458", "57460", "57461", "57464", "57465", "57466", "57469", "57475", "57476", "57477", "57481",
                "57482", "57483", "57485", "57486", "57489", "57491", "57492", "57493", "57494", "57496", "57498", "57499", "57540", "57541", "57543",
                "57545", "57571", "57573", "57581", "57582", "57583", "57586", "57589", "57590", "57595", "58401", "58402", "58404", "58407", "58408",
                "58409", "58500", "58501"
        };
        List<ColdairProcessProvince> res = new ArrayList<>();

        int day = (int) ChronoUnit.DAYS.between(startTime, endTime);//两个日期之间相隔天数
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd"); //localDate->int


        int[][] levels = new int[stations.length][day];//存放每个站每天降温等级
        double[] rate = new double[day];//比例


        for (int i = 0; i < stations.length; i++) {
            List<DayTemp> dayTemps = temMapper.selectByStation(stations[i], startTime, endTime);//站点整体的信息
            for (int j = 1; j < day; j++) {
                //计算每天的冷空气强度
                ColdairProcessN coldairProcessN = judgeProcess(dayTemps, j);
                levels[i][j] = coldairProcessN.getLevel();//级别分成1，2，3，4，5
            }
        }


        //判断每一列计算N1,N2,N3，i表示列，j表示行
        for (int i = 0; i < levels[0].length; i++) {
            int sum = 0;
            for (int[] level : levels) {
                if (level[i] != 0) {
                    sum++;
                }
            }

            rate[i] = (double) sum / levels.length;
        }


        //接下来，根据rate计算至少连续两天rate>=0.2的过程
        int i = 0, j;
        while (i < rate.length) {
            if (rate[i] < 0.2) {
                i++;
            } else {
                int length = 1;
                j = i + 1;
                while (j < rate.length) {
                    if (rate[j] >= 0.2) {
                        j++;
                        length++;
                    } else {//冷空气过程结束
                        if (length >= 2) {
                            //这就是一次区域冷空气
                            //接下来计算N1,N2,N3
                            int N1 = 0, N2 = 0, N3 = 0, N4 = 0, N5 = 0;
                            int[] maxValues = new int[stations.length];//存放每一行的level的最大值

                            for (int k = 0; k < stations.length; k++) {
                                int max = -1;
                                for (int l = i; l < j; l++) {
                                    if (levels[k][l] > max) {
                                        max = levels[k][l];
                                    }
                                }

                                maxValues[k] = max;
                            }

                            for (int maxValue : maxValues) {
                                if (maxValue == 1) {
                                    N1++;
                                } else if (maxValue == 2) {
                                    N2++;
                                } else if (maxValue == 3) {
                                    N3++;
                                } else if (maxValue == 4) {
                                    N4++;
                                } else if (maxValue == 5) {
                                    N5++;
                                }
                            }


                            //计算过程强度指数和区域强度指数
                            double I = (double) (3 * (N3 + N4 + N5) + 2 * N2 + N1) / (N1 + N2 + N3 + N4 + N5);
                            String level = null;
                            if (I >= 1.95) {
                                level = "寒潮过程";
                            } else if (I >= 1.7) {
                                level = "强冷空气过程";
                            } else if (I >= 1.0) {
                                level = "中等强度冷空气过程";
                            }
                            double M = I * Math.sqrt((double) (N1 + N2 + N3 + N4 + N5) / stations.length);
                            LocalDate start = startTime.plusDays(i);
                            LocalDate end = startTime.plusDays(j - 1);

                            ColdairProcessProvince coldairProcessProvince = new ColdairProcessProvince();
                            coldairProcessProvince.setIiiii(provinceName);
                            coldairProcessProvince.setStartDay(Integer.valueOf(dtf.format(start)));
                            coldairProcessProvince.setEndDay(Integer.valueOf(dtf.format(end)));
                            coldairProcessProvince.setMediumCount(N1);
                            coldairProcessProvince.setSevereCount(N2);
                            coldairProcessProvince.setHanchaoCount(N3 + N4 + N5);
                            coldairProcessProvince.setStrongHanchaoCount(N4);
                            coldairProcessProvince.setSevereHanchaoCount(N5);
                            coldairProcessProvince.setI(I);
                            coldairProcessProvince.setM(M);
                            coldairProcessProvince.setLevel(level);
                            res.add(coldairProcessProvince);
                        }
                        break;
                    }
                }
                i = j + 1;
            }
        }
        return res;
    }
}
