package cn.wnhyang.geoHelper;

/**
 * <a href="https://www.geodatasource.com/developers/java">...</a>
 * 经纬度距离计算
 *
 * @author wnhyang
 * @date 2024/4/28
 **/
public class DistanceCalculator {

    public static void main(String[] args) throws java.lang.Exception {
        System.out.println(distance(-96.80322, 32.9697, -98.53506, 29.46786, "M") + " Miles\n");
        System.out.println(distance(-96.80322, 32.9697, -98.53506, 29.46786, "K") + " Kilometers\n");
        System.out.println(distance(-96.80322, 32.9697, -98.53506, 29.46786, "N") + " Nautical Miles\n");
    }

    /**
     * 计算两点之间的距离
     *
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 纬度2
     * @return 距离 单位英里
     */
    public static double distance(double lon1, double lat1, double lon2, double lat2) {
        return distance(lon1, lat1, lon2, lat2, "M");
    }

    /**
     * 计算两点之间的距离
     *
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 纬度2
     * @param unit 单位
     * @return 距离
     */
    public static double distance(double lon1, double lat1, double lon2, double lat2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if ("K".equals(unit)) {
                dist = dist * 1.609344;
            } else if ("N".equals(unit)) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
