package cn.wnhyang.geoHelper.ip;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 国家|区域|省份|城市|ISP
 *
 * @author wnhyang
 * @date 2024/4/30
 **/
@Data
@AllArgsConstructor
public class Ip2Region {

    /**
     * ip
     */
    private String ip;

    /**
     * 国家
     */
    private String country;

    /**
     * 地区
     */
    private String area;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * ISP
     */
    private String isp;
}
