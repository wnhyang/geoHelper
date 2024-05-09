package cn.wnhyang.geoHelper;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.wnhyang.geoHelper.ad.Area;
import cn.wnhyang.geoHelper.ad.City;
import cn.wnhyang.geoHelper.ad.Pca;
import cn.wnhyang.geoHelper.ad.Province;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://github.com/modood/Administrative-divisions-of-China">...</a>
 * 中国行政区划工具类
 *
 * @author wnhyang
 * @date 2024/4/24
 **/
@Slf4j
public class AdocUtil {

    /**
     * 省份集合
     */
    private static final Map<String, Province> PROVINCE_MAP = new HashMap<>(32);

    /**
     * 城市集合
     */
    private static final Map<String, City> CITY_MAP = new HashMap<>(512);

    /**
     * 区/县集合
     */
    private static final Map<String, Area> AREA_MAP = new HashMap<>(4096);

    private static final String DONT_KNOW = "未知";

    private static final int AREA_CODE_LENGTH = 6;

    private AdocUtil() {
    }

    /**
     * 初始化省市县数据
     */
    public static void init() {
        long start = System.currentTimeMillis();
        try {
            List<Province> provinceList = CsvUtil.getReader().read(
                    ResourceUtil.getUtf8Reader("provinces.csv"), Province.class);
            provinceList.forEach(province -> PROVINCE_MAP.put(province.getCode(), province));

            List<City> cityList = CsvUtil.getReader().read(
                    ResourceUtil.getUtf8Reader("cities.csv"), City.class);
            cityList.forEach(city -> CITY_MAP.put(city.getCode(), city));

            List<Area> areaList = CsvUtil.getReader().read(
                    ResourceUtil.getUtf8Reader("areas.csv"), Area.class);
            areaList.forEach(area -> AREA_MAP.put(area.getCode(), area));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("init success, cost:{}ms", System.currentTimeMillis() - start);
    }


    /**
     * 获取省份
     *
     * @param provinceCode 省份编码
     * @return 省份 {@link Province}
     */
    public static Province getProvince(String provinceCode) {
        if (PROVINCE_MAP.containsKey(provinceCode)) {
            return PROVINCE_MAP.get(provinceCode);
        }
        return null;
    }

    /**
     * 获取省份名称
     *
     * @param provinceCode 省份编码
     * @return 省份名称
     */
    public static String getProvinceName(String provinceCode) {
        if (ObjectUtil.isNotNull(getProvince(provinceCode))) {
            return getProvince(provinceCode).getName();
        }
        return DONT_KNOW;
    }

    /**
     * 获取城市
     *
     * @param cityCode 城市编码
     * @return 城市 {@link City}
     */
    public static City getCity(String cityCode) {
        if (CITY_MAP.containsKey(cityCode)) {
            return CITY_MAP.get(cityCode);
        }
        return null;
    }

    /**
     * 获取城市名称
     *
     * @param cityCode 城市编码
     * @return 城市名称
     */
    public static String getCityName(String cityCode) {
        if (ObjectUtil.isNotNull(getCity(cityCode))) {
            return getCity(cityCode).getName();
        }
        return DONT_KNOW;
    }

    /**
     * 获取区/县
     *
     * @param areaCode 区/县编码
     * @return 区/县 {@link Area}
     */
    public static Area getArea(String areaCode) {
        if (AREA_MAP.containsKey(areaCode)) {
            return AREA_MAP.get(areaCode);
        }
        return null;
    }

    /**
     * 获取区/县名称
     *
     * @param areaCode 区/县编码
     * @return 区/县名称
     */
    public static String getAreaName(String areaCode) {
        if (ObjectUtil.isNotNull(getArea(areaCode))) {
            return getArea(areaCode).getName();
        }
        return DONT_KNOW;
    }

    /**
     * 获取省市区
     *
     * @param areaCode 区/县编码
     * @return 省市区 {@link Pca}
     */
    public static Pca getPca(String areaCode) {
        int len = areaCode.length();
        if (len == AREA_CODE_LENGTH) {
            String provinceCode = StrUtil.sub(areaCode, 0, 2);
            String cityCode = StrUtil.sub(areaCode, 0, 4);
            Pca pca = new Pca();
            pca.setProvince(getProvinceName(provinceCode));
            pca.setCity(getCityName(cityCode));
            pca.setArea(getAreaName(areaCode));
            return pca;
        }
        return null;
    }

    public static void main(String[] args) {
        init();
        log.info("provinceCode:11, provinceName:{}", getProvinceName("11"));
        log.info("cityCode:1101, cityName:{}", getCityName("1101"));
        log.info("areaCode:110101, areaName:{}", getAreaName("110101"));
        log.info("areaCode:110101, pca:{}", getPca("110101"));
    }

}
