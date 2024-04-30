package cn.wnhyang.geoHelper.geo;

import lombok.Data;

/**
 * @author wnhyang
 * @date 2024/4/29
 **/
@Data
public class AreaWithGeo {

    /**
     * 地区编码
     */
    private String code;

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;
}
