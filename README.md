# geoHelper

# 简介

`geoHelper`整合了身份证、手机号、`IP`、`GPS`解析方法，基于内存提供离线高性能解析服务。

[身份证/手机号解析服务](https://www.yuque.com/wnh/share/vw03pm5x88atva56)

[IP/GPS解析服务，ip2region，逆地理编码](https://www.yuque.com/wnh/share/yusumn415tgh6uwn)

# 数据来源

| 数据     | 来源                                                         |
| -------- | ------------------------------------------------------------ |
| 行政区划 | [GitHub - modood/Administrative-divisions-of-China](https://github.com/modood/Administrative-divisions-of-China) |
| 手机号   | [GitHub - EeeMt/phone-number-geo](https://github.com/EeeMt/phone-number-geo) |
| IP       | [狮子的魂/ip2region](https://gitee.com/lionsoul/ip2region)   |
| geo      | [中国城市坐标（最全最完整）](https://www.cnblogs.com/henuyuxiang/p/12981201.html) |

# 身份证解析

适用国内身份证解析，作为`Hutool.IdcardUtil`的扩展，加入`AdocUtil`，意为中国行政区划工具。

方法包括：

-   `getProvince` 获取省份
-   `getProvinceName` 获取省份名称
-   `getCity` 获取城市
-   `getCityName` 获取城市名称
-   `getArea` 获取区县
-   `getAreaName` 获取区县名称
-   `getPca` 获取省市区

当然基于[GitHub - modood/Administrative-divisions-of-China](https://github.com/modood/Administrative-divisions-of-China)可以扩展到乡级（乡镇街道）、 村级（村委会居委会），那么数据量会大一些，可以将数据存储在数据库中使用。

## 示例

```java
public static void main(String[] args) {
    init();
    log.info("provinceCode:11, provinceName:{}", getProvinceName("11"));
    log.info("cityCode:1101, cityName:{}", getCityName("1101"));
    log.info("areaCode:110101, areaName:{}", getAreaName("110101"));
    log.info("areaCode:110101, pca:{}", getPca("110101"));
}
```

> init success, cost:97ms
>
> provinceCode:11, provinceName:北京市
>
> cityCode:1101, cityName:市辖区
>
> areaCode:110101, areaName:东城区
>
> provinceCode:11, cityCode:1101, areaCode:110101
>
> areaCode:110101, pca:Pca(province=北京市, city=市辖区, area=东城区)

# 手机号解析

适用国内手机号解析，使用[GitHub - EeeMt/phone-number-geo](https://github.com/EeeMt/phone-number-geo)工具。

## 示例

```java
public static void main(String[] args) {
    log.info("15558167723:{}", lookup("15558167723"));
}
```

> 15558167723:PhoneNumberInfo(number=15558167723, attribution=Attribution(province=浙江, city=杭州, zipCode=310000, areaCode=0571), isp=CHINA_UNICOM)
>



# IP解析

使用[狮子的魂/ip2region](https://gitee.com/lionsoul/ip2region)项目，因为此项目本身很灵活，数据可扩展，可自行参考源项目学习。

## 示例

```java
public static void main(String[] args) {
    init();
    String search = search("114.114.114.114");
    log.info("search:{}", search);
}
```

> init success, cost:28ms
>
> search:中国|0|江苏省|南京市|0

# GPS解析

[IP/GPS解析服务，ip2region，逆地理编码](https://www.yuque.com/wnh/share/yusumn415tgh6uwn)，实现的方法已经在这篇文章说明了，所以不多介绍啦。

## 示例

```java
public static void main(String[] args) {
    AdocUtil.init();
    init();
    // 116.867584,39.542294
    Pca pcaByGeo = getPcaByGeo("116.867584,39.542294");
    log.info("pcaByGeo:{}", pcaByGeo);
}
```

> pcaByGeo:Pca(province=河北省, city=廊坊市, area=广阳区)

## 解析不准确的原因

之前文章已经说明此方法的优势与缺陷，这里再说明一下，如果需要增加精度要怎么做。

[行政区浏览](https://webapi.amap.com/ui/1.0/ui/geo/DistrictExplorer/examples/index.html?guide=1)

[坐标拾取器 | 高德地图API](https://lbs.amap.com/tools/picker)

[GeoHUB](https://geohub.amap.com/mapstyle/index)

[天地图·在线地图](https://map.tianditu.gov.cn/)

总所周知行政区划都是不规则的多边形，而且这个多边形的中心点不是数学上的重心，而是行政上的中心。如下是河北省-廊坊市-广阳区的行政区划，这是一个不规则的多边形。

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240429/image.9nzle33yha.webp)

通过高德地图搜索确认其中心经纬度是：`116.710667,39.52343`，已标注在上图，大概是红色圆圈⭕️的位置。

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240429/image.1lbmlan12z.webp)

现有坐标：`116.867584,39.542294`，其应该归属天津市-武清区，但是因为计算方法或者说是数据的缺陷，因此点离廊坊市-广阳区更近，所以结果是`pcaByGeo:Pca(province=河北省, city=廊坊市, area=广阳区)`，并不正确。如下图所示。

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240430/image.4g4ar8iwlv.webp)
![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240429/image.3k7tait1eh.webp)

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240429/image.51dyc9zrrr.webp)

## 如何提高准确性呢？

那就是要切割了，如下是天津市-武清区，如果希望对于武清区解析的更准确就要增加武清区数据，数据不能是随意添加，需要如下图这样的画圈，将武清区的边界画出，增加这些圆圈中心坐标数据。如果希望更加精细就要画出更多直径更小的圈，不过那工作量将非常庞大。

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240430/image.2krpymd1kf.webp)

重要‼️

所以一旦要画圈提高精度，必须连带画出边界外相邻的圈。因为，如果只是画出下面这样天津市-武清区的圈，不管边界外廊坊市-广阳区、北京市-通州区，那么原来被识别为通州、广阳的点会因为武清新增加的点的影响都被识别为武清区，而且新增的边界圆心坐标向边界方向画圆与其他地区圆近乎相切时的直径必须统一，这样才能保证边界点距离的准确。

如下图在北京通州、天津武清、廊坊广阳邻接的位置画了三个圈，其中天津武清区的圆心坐标为：`116.88,39.58`，北京通州圆心坐标为：`116.91,39.59`，廊坊广阳圆心坐标为：`116.70,39.54`。

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240430/image.7i06si9afz.webp)

## 补充数据

1、首先从`areas.csv`文件中找到武清区的`areaCode`为`120114`。

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240430/image.58h690d9jp.webp)

2、在`areas_with_geo.csv`文件中补充`120114`经纬度`120114,116.88,39.58`。

![image](https://jsd.cdn.zzko.cn/gh/wnhyang/picx-images-hosting@master/20240430/image.4ckotkn8wd.webp)

3、重新运行项目就可以了。

再次测试`116.867584,39.542294`，果然被正确解析到天津武清区！

## 总结

以上已经说明了解析不准确，补充数据提高准确性的方法，但这个方法其实是很苛刻的，对于数据的要求非常高，甚至说如何数据达标甚至可以比拟商业的的经纬度解析标准。
