package cn.wnhyang.geoHelper;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.wnhyang.geoHelper.ip.Ip2Region;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 * ip工具类
 *
 * @author wnhyang
 * @date 2024/4/29
 **/
@Slf4j
public class IpUtil {

    private static Searcher searcher;

    private IpUtil() {
    }

    /**
     * 初始化 ip2region数据到内存
     */
    public static void init() {
        long start = System.currentTimeMillis();
        try {
            byte[] bytes = ResourceUtil.readBytes("ip2region.xdb");
            searcher = Searcher.newWithBuffer(bytes);
        } catch (Exception e) {
            log.error("初始化ip2region失败", e);
        }
        log.info("init success, cost:{}ms", System.currentTimeMillis() - start);
    }

    /**
     * 查询ip
     *
     * @param ip ip
     * @return 结果数据
     */
    public static String search(String ip) {
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            log.error("ip2region search error", e);
            return null;
        }
    }

    /**
     * 查询ip
     * @param ip ip
     * @return 结果数据
     */
    public static Ip2Region getIp2Region(String ip) {
        String search = search(ip);
        if (StrUtil.isNotBlank(search)) {
            String[] split = search.split("\\|");
            if (split.length == 5) {
                return new Ip2Region(ip, split[0], split[1], split[2], split[3], split[4]);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        init();
        String search = search("114.114.114.114");
        log.info("search:{}", search);
    }
}
