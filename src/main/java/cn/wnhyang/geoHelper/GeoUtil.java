package cn.wnhyang.geoHelper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import cn.wnhyang.geoHelper.ad.Pca;
import cn.wnhyang.geoHelper.geo.AreaWithGeo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区经纬度工具类
 *
 * @author wnhyang
 * @date 2024/4/29
 **/
@Slf4j
public class GeoUtil {

    private static final Map<String, String> GEO_MAP = new HashMap<>(4096);

    private static final Map<String, AreaWithGeo> AREA_WITH_GEO_MAP = new HashMap<>(4096);

    private GeoUtil() {
    }

    /**
     * 初始化 地区经纬度数据
     */
    public static void init() {
        long start = System.currentTimeMillis();
        List<AreaWithGeo> areaList = CsvUtil.getReader().read(
                ResourceUtil.getUtf8Reader("areas_with_geo.csv"), AreaWithGeo.class);
        areaList.forEach(area -> {
            // 四舍五入
            String key = Math.round(area.getLon()) + "," + Math.round(area.getLat());
            String value = GEO_MAP.get(key);
            if (StrUtil.isNotBlank(value)) {
                GEO_MAP.put(key, value + "," + area.getCode());
            } else {
                GEO_MAP.put(key, area.getCode());
            }
            AREA_WITH_GEO_MAP.put(area.getCode(), area);
        });
        log.info("init success, cost:{}ms", System.currentTimeMillis() - start);
    }

    /**
     * 根据经纬度获取相似地区
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 相似地区
     */
    public static List<AreaWithGeo> getSimilarPcaByGeo(double lon, double lat) {
        List<AreaWithGeo> list = new ArrayList<>();
        String key = Math.round(lon) + "," + Math.round(lat);
        String areaCodes = GEO_MAP.get(key);
        if (StrUtil.isNotBlank(areaCodes)) {
            for (String areaCode : areaCodes.split(",")) {
                AreaWithGeo areaWithGeo = AREA_WITH_GEO_MAP.get(areaCode);
                if (areaWithGeo != null) {
                    list.add(areaWithGeo);
                }
            }
        }
        return list;
    }

    /**
     * 根据经纬度获取地区
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 地区
     */
    public static Pca getPcaByGeo(double lon, double lat) {
        List<AreaWithGeo> areaWithGeos = getSimilarPcaByGeo(lon, lat);
        if (CollUtil.isEmpty(areaWithGeos)) {
            return null;
        }
        double distance = 10000.0;
        Pca result = new Pca();
        for (AreaWithGeo areaWithGeo : areaWithGeos) {
            double tmpDistance = DistanceCalculator.distance(areaWithGeo.getLon(), areaWithGeo.getLat(), lon, lat);
            if (tmpDistance < distance) {
                distance = tmpDistance;
                Pca pca = AdocUtil.getPca(areaWithGeo.getCode());
                if (pca != null) {
                    result.setProvince(pca.getProvince());
                    result.setCity(pca.getCity());
                    result.setArea(pca.getArea());
                }
            }
        }

        return result;
    }

    /**
     * 根据经纬度获取地区
     *
     * @param lonAndLat 经纬度字符串，格式必须是 "经度,纬度"
     * @return 地区
     */
    public static Pca getPcaByGeo(String lonAndLat) {
        try {
            String[] split = lonAndLat.split(",");
            double lon = Double.parseDouble(split[0]);
            double lat = Double.parseDouble(split[1]);
            return getPcaByGeo(lon, lat);
        } catch (Exception e) {
            log.error("经纬度格式错误");
            return null;
        }
    }

    public static void main(String[] args) {
        AdocUtil.init();
        init();
        // 116.867584,39.542294
        Pca pcaByGeo = getPcaByGeo("116.867584,39.542294");
        log.info("pcaByGeo:{}", pcaByGeo);
    }
}
