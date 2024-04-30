package cn.wnhyang.geoHelper.ad;

import lombok.Data;

/**
 * @author wnhyang
 * @date 2024/4/24
 **/
@Data
public class Area {

    /**
     * 区/县编码
     */
    private String code;

    /**
     * 区/县名称
     */
    private String name;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属省份编码
     */
    private String provinceCode;
}
