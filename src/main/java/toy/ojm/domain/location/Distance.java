package toy.ojm.domain.location;

public class Distance {

    //  Haversine 공식 사용
    public Double distance(double lat1, double lon1, double lat2, double lon2){
        Double theta = lon1 - lon2; // 경도 차이
        Double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))* Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344; // *1609.344 면 meter, *1.609344면 kilometer

        return dist; // 단위 meter
    }

    // 10진수를 radian으로 변환
    private Double deg2rad(Double deg){
        return deg * Math.PI/180.0;
    }

    // radian을 10진수로 변환
    private Double rad2deg(Double rad){
        return rad * 180 / Math.PI;
    }

    //  피타고라스의 정리 사용
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2){
        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }

    //    private static RestaurantRepository restaurantRepository;
//    static Double EARTH_RADIUS = 6378.135;
//    // 두 지점 간의 거리 계산
//    public static double getDistance(double lat1, double lon1, double lat2, double lon2){
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//
//        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))*
//                Math.sin(dLon/2) * Math.sin(dLon/2);
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double d = EARTH_RADIUS * C * 1000; // Distance in m
//        return d;
//    }
//
//    public List<Restaurant>aroundRestaurantList(double nowLatitude, double nowLongitude, double distance){
//        // m당 y좌표 이동 값
//        double mForLatitude = (1 / (EARTH_RADIUS * 1 * (Math.PI / 180))) / 1000;
//        // m당 x좌표 이동 값
//        double mForLongitude = (1 / (EARTH_RADIUS * 1 * (Math.PI /  180)* Math.cos(Math.toRadians(nowLatitude)))) / 1000;
//
//        // 현재 위치 기준 검색 거리 좌표
//        double maxY = nowLatitude + (distance * mForLatitude);
//        double minY = nowLatitude - (distance * mForLatitude);
//        double maxX = nowLongitude - (distance * mForLongitude);
//        double minX = nowLongitude - (distance * mForLongitude);
//
//        // 해당되는 좌표의 범위 안에 있는 가맹점
//        // AroundRestaurant Repository가 별도로 필요할까?
//        List<RestaurantResponseDto> tempAroundRestaurantList = restaurantRepository.getAroundRestaurantList(maxY, maxX, minY, minX);
//        List<RestaurantResponseDto> resultAroundRestaurantList = new ArrayList<>();
//
//        // 정확한 거리 측정
//        for(RestaurantResponseDto aroundRestaurant : tempAroundRestaurantList ){
//            double distance = Helper.getDistance(nowLatitude, nowLongitude, aroundRestaurant.getLatitude(), aroundRestaurant.getLongitude());
//            if (distance < distance){
//                resultAroundRestaurantList.add(aroundRestaurant);
//            }
//        }
//        return resultAroundRestaurantList;
//    }
}
