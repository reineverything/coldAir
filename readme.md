# 冷空气识别算法
## 1.算法描述
代码总共包括两个识别算法：单站冷空气强度识别，区域冷空气强度识别（基于国家站点）

## 2. 基本配置
详情见application.yml

| 设置                   | 值        |
| ---------------------- | --------- |
| 连接的数据库           | metadata  |
| 访问ip地址（需要修改） | localhost |
| 端口号（需要修改）     | 8082      |
## 3.算法1 ：单站冷空气强度识别

### 3.1 输入参数
| 参数名       | 参数说明             |
|-----------|------------------|
| stationId | 站号               |
| startTime | 开始时间（yyyy-MM-dd） |
| endTime   | 结束时间（yyyy-MM-dd） |

### 3.2 输出结果
输出结果为一段时间内的单站冷空气过程

| 参数名   | 参数说明         |
| -------- | ---------------- |
| iiiii    | 站号             |
| startDay | 开始时间         |
| endDay   | 结束时间         |
| n        | 该过程持续天数   |
| maxtd    | 降温最大值       |
| sumtd    | 全过程降温总量   |
| tmin     | 过程最低气温     |
| dert24   | 24小时降温最大值 |
| dert48   | 48小时降温最大值 |
| dert72   | 72小时降温最大值 |
| level    | 等级强度         |
| label    | 标签             |



### 3.3 输入样例(get)

Get:localhost:8082/cold/single/57249/2000-02-01/2000-02-15



### 3.4 输出样例

```json
[
    {
        "iiiii": "57249",
        "startDay": 20000211,
        "endDay": 20000210,
        "n": 2,
        "maxtd": 1.8,
        "sumtd": 1.7000000000000002,
        "tmin": -4.0,
        "dert24": -1.8,
        "dert48": -7.2,
        "dert72": -9.0,
        "level": 1,
        "label": "Medium"
    }
]
```



## 4.算法二：区域冷空气过程强度识别

### 4.1 输入参数
| 参数名       | 参数说明             |
|-----------|------------------|
| stationId | 站号               |
| startTime | 开始时间（yyyy-MM-dd） |
| endTime   | 结束时间（yyyy-MM-dd） |

### 4.2 输出结果

| 参数名             | 参数说明                                                 |
| ------------------ | -------------------------------------------------------- |
| iiiii              | 区域名称（目前仅支持湖北省，需要知道区域所有国家的站号） |
| startDay           | 开始时间                                                 |
| endDay             | 结束时间                                                 |
| mediumCount        | 中等强度站数                                             |
| severeCount        | 强冷空气站数                                             |
| hanchaoCount       | 寒潮站数                                                 |
| strongHanchaoCount | 强寒潮站数                                               |
| severeHanchaoCount | 强寒潮站数                                               |
| I                  | 过程强度指数                                             |
| M                  | 过程综合强度指数                                         |
| level              | 等级                                                     |



### 4.3 输入样例(post)

localhost:8082/cold/province/湖北省/1984-01-01/1985-01-01

### 4.4 输出样例

```json
[
    {
        "startDay": 20180114,
        "endDay": 20180115,
        "mediumCount": 32,
        "severeCount": 16,
        "hanchaoCount": 7,
        "strongHanchaoCount": 0,
        "severeHanchaoCount": 0,
        "level": "中等强度冷空气过程",
        "i": 1.5454545454545454,
        "m": 1.31471247789071,
        "iiiii": "湖北省"
    }
]
```

