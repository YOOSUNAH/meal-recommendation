package toy.ojm.service;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import toy.ojm.entity.Restaurant;
import toy.ojm.location.TransCoordination;

import java.util.ArrayList;
import java.util.List;
import toy.ojm.location.Coordinates;

@Slf4j
@Service
public class NearbyRestaurantService {
    // 사용자 근처의 음식점을 데이터베이스에서 가져오는 메서드 (100m 이내의 음식점을 검색하는 쿼리)
    private final JdbcTemplate jdbcTemplate;
    private final TransCoordination transCoordination;

    public NearbyRestaurantService(JdbcTemplate jdbcTemplate, TransCoordination transCoordination) {
        this.jdbcTemplate = jdbcTemplate;
        this.transCoordination = transCoordination;
    }

    public List<Restaurant> getNearbyRestaurants(Coordinates coordinates) {
        List<Restaurant> nearbyRestaurants = new ArrayList<>();
        try {  // 사용자의 현재 위치를 데이터베이스의 TM 좌표계에서 Geolocation의 GCS 좌표계로 변환
            ProjCoordinate transformedCoordinates = transCoordination.transformToGCS(
                coordinates.getLongitude(), coordinates.getLatitude()
            );
            coordinates.setLongitude(transformedCoordinates.x);
            coordinates.setLatitude(transformedCoordinates.y);

            // 100m 이내의 음식점을 데이터베이스에서 쿼리 // test를 위해 거리 400000으로 조정함.
            String query = "SELECT * FROM restaurantTable WHERE ST_DISTANCE_SPHERE(POINT(longitude, latitude), POINT(?, ?)) <= 400000";
            Object[] params = {coordinates.getLongitude(), coordinates.getLatitude()};

            // 쿼리 결과를 RestaurantEntity 리스트로 변환하여 반환
            // ResultSet에서 각 행의 데이터를 읽어와서 rowMapper를 통해 RestaurantEntity 객체로 매핑하고, 그 결과를 리스트에 추가하여 반환
            nearbyRestaurants = jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
              // 데이터베이스 쿼리를 실행하고 그 결과를 RowMapper를 이용하여 객체로 매핑하는 기능을 수행
             // 여기서 query는 실행할 SQL 쿼리문이고, params는 쿼리에 전달될 파라미터들을 의미하고, rowMapper는 각 행(row)의 결과를 객체로 변환하는데 사용한다.
                Restaurant restaurant = new Restaurant(); // RestaurantEntity클래스의 객체(인스턴스)인 restaurant 생성
                restaurant.setBusinessStatus(resultSet.getString("businessStatus")); // restaurant에 resultSet으로 가져온 businessStatus 저장
                restaurant.setStreetNumberAddress(resultSet.getString("streetNumberAddress"));
                restaurant.setRestaurantName(resultSet.getString("restaurantName"));
                restaurant.setCategory(resultSet.getString("category"));
                restaurant.setLongitude(Double.valueOf(resultSet.getString("longitude")));
                restaurant.setLatitude(Double.valueOf(resultSet.getString("latitude")));
                return restaurant;
            });
        } catch (Exception e) {
            log.error("Error fetching nearby restaurants: {}", e.getMessage());
        }
        return nearbyRestaurants; // 근처 음식점 리스트 반환
    }
}
