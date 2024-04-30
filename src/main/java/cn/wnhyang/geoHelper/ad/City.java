package cn.wnhyang.geoHelper.ad;

import lombok.Data;

/**
 * @author wnhyang
 * @date 2024/4/24
 **/
@Data
public class City {

    /**
     * 城市编码
     */
    private String code;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 所属省份编码
     */
    private String provinceCode;
}
