package cn.wnhyang.geoHelper;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.Attribution;
import me.ihxq.projects.pna.ISP;
import me.ihxq.projects.pna.PhoneNumberInfo;
import me.ihxq.projects.pna.PhoneNumberLookup;

/**
 * 手机号工具类
 *
 * @author wnhyang
 * @date 2024/4/26
 **/
@Slf4j
public class PhoneNumberUtil {

    private static final PhoneNumberLookup LOOKUP = new PhoneNumberLookup();

    private static final String DONT_KNOW = "未知";

    private PhoneNumberUtil() {
    }

    /**
     * 查找手机号
     *
     * @param phoneNumber 手机号
     * @return 手机号信息
     */
    public static PhoneNumberInfo lookup(String phoneNumber) {
        return LOOKUP.lookup(phoneNumber).orElseGet(() -> getNotFound(phoneNumber));
    }

    public static PhoneNumberInfo getNotFound(String phoneNumber) {
        return new PhoneNumberInfo(phoneNumber, new Attribution(DONT_KNOW, DONT_KNOW, DONT_KNOW, DONT_KNOW), ISP.UNKNOWN);
    }

    public static void main(String[] args) {
        log.info("15558167723:{}", lookup("15558167723"));
    }
}
