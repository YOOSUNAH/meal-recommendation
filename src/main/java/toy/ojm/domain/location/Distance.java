package toy.ojm.domain.location;

public class Distance {

    //  Haversine 공식 사용
    public static Double distance(double lat1, double lon1, double lat2, double lon2) {
        Double theta = lon1 - lon2; // 경도 차이
        Double dist =
            Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344; // *1609.344 면 meter, *1.609344면 kilometer

        return dist; // 단위 meter
    }

    // 10진수를 radian으로 변환
    private static Double deg2rad(Double deg) {
        return deg * Math.PI / 180.0;
    }

    // radian을 10진수로 변환
    private static Double rad2deg(Double rad) {
        return rad * 180 / Math.PI;
    }
}
