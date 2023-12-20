package toy.ojm.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.domain.Coordinates;
import toy.ojm.domain.TransCoord;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.repository.JdbcTemplateMemberRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelToDatabaseService {

    private final ExcelReader excelReader;
    private final JdbcTemplate jdbcTemplate;
    private final TransCoord transCoord;

    public void readFromExcelAndSave(String filePath, MealRecommendationRequest request) {
        List<RestaurantDTO> excelRestaurants = excelReader.read(filePath);
        List<RestaurantEntity> restaurantEntities = convertRestaurantEntities(excelRestaurants);
        // 데이터베이스에 데이터 삽입
        for (RestaurantEntity entity : restaurantEntities) {
            // 데이터베이스의 TM 좌표계를 Geolocation의 GCS 좌표계로 변환하여 저장
            ProjCoordinate transformedCoordinates = transCoord.transformToGCS(Double.parseDouble(entity.getLongitude()), Double.parseDouble(entity.getLatitude()));
            entity.setLongitude(String.valueOf(transformedCoordinates.x));
            entity.setLatitude(String.valueOf(transformedCoordinates.y));
            insertData(entity);
        }
        // 사용자의 위치로부터 100m 이내의 음식점 데이터 가져오기
        Coordinates userCoordinates = request.getCoordinates();
        if (userCoordinates != null && isValidCoordinates(userCoordinates)) {
            ProjCoordinate transformedCoordinates = transCoord.transformToGCS(userCoordinates.getLongitude(), userCoordinates.getLatitude());
            userCoordinates.setLongitude(String.valueOf(transformedCoordinates.x)); // 변환된 경도 설정
            userCoordinates.setLatitude(String.valueOf(transformedCoordinates.y)); // 변환된 위도 설정

            List<RestaurantEntity> nearbyRestaurants = getNearbyRestaurants(userCoordinates);
        } else {
            log.warn("유효하지 않은 사용자 위치 정보입니다.");
        }
    }
    private List<RestaurantEntity> convertRestaurantEntities(List<RestaurantDTO> excelRestaurants) {
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for (RestaurantDTO excelRestaurant : excelRestaurants) {
            try {
                RestaurantEntity entity = new RestaurantEntity();
                entity.setBusinessStatus(excelRestaurant.getBusinessStatus());
                entity.setStreetNumberAddress(excelRestaurant.getStreetNumberAddress());
                entity.setStreetNameAddress(excelRestaurant.getStreetNameAddress());
                entity.setRestaurantName(excelRestaurant.getRestaurantName());
                entity.setCategory(excelRestaurant.getCategory());
                entity.setLongitude(excelRestaurant.getLongitude());
                entity.setLatitude(excelRestaurant.getLatitude());
                restaurantEntities.add(entity);
            } catch (Exception e) {
                log.error("Error creating RestaurantEntity: {}", e.getMessage());
            }
        }
        return restaurantEntities;
    }
    private void insertData(RestaurantEntity entity) {
        try {
            int success = jdbcTemplate.update("""
                INSERT INTO restaurantTable (
                businessStatus, streetNumberAddress, streetNameAddress, 
                restaurantName, category, longitude, latitude
                ) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                entity.getBusinessStatus(),
                entity.getStreetNumberAddress(),
                entity.getStreetNameAddress(),
                entity.getRestaurantName(),
                entity.getCategory(),
                entity.getLongitude(),
                entity.getLatitude()
            );

            if (success > 0) {
                log.info("데이터가 성공적으로 삽입되었습니다.");
            } else {
                log.warn("데이터 삽입이 실패하였습니다.");
            }
        } catch (Exception e) {
            log.error("데이터 삽입 중 오류 발생: {}", e.getMessage());
        }
    }
    public List<RestaurantEntity> getNearbyRestaurants(Coordinates coordinates) {
        List<RestaurantEntity> nearbyRestaurants = new ArrayList<>();
        try {
            // 사용자의 현재 위치를 데이터베이스의 TM 좌표계에서 Geolocation의 GCS 좌표계로 변환
            ProjCoordinate transformedCoordinates = transCoord.transformToGCS(Double.parseDouble(String.valueOf(coordinates.getLongitude())), Double.parseDouble(String.valueOf(coordinates.getLatitude())));
            coordinates.setLongitude(String.valueOf(transformedCoordinates.x));
            coordinates.setLatitude(String.valueOf(transformedCoordinates.y));

            //100m 이내에 음식점 쿼리문
            String query = "SELECT * FROM restaurantTable WHERE ST_DISTANCE_SPHERE(POINT(longitude, latitude), POINT(?, ?)) <= 100";
            Object[] params = {coordinates.getLongitude(), coordinates.getLatitude()};

            nearbyRestaurants = jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
                RestaurantEntity restaurant = new RestaurantEntity();
                restaurant.setBusinessStatus(resultSet.getString("businessStatus"));
                restaurant.setStreetNumberAddress(resultSet.getString("streetNumberAddress"));
                restaurant.setStreetNameAddress(resultSet.getString("streetNameAddress"));
                restaurant.setRestaurantName(resultSet.getString("restaurantName"));
                restaurant.setCategory(resultSet.getString("category"));
                restaurant.setLongitude(resultSet.getString("longitude"));
                restaurant.setLatitude(resultSet.getString("latitude"));
                return restaurant;
            });
        } catch (Exception e) {
            log.error("100m 이내의 음식점을 가져오는 중 오류 발생: {}", e.getMessage());
            // 오류 처리를 원하는 방식으로 수행
        }
        return nearbyRestaurants;
    }

    private boolean isValidCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            return false;
        }
        return coordinates.getLongitude() != null && coordinates.getLatitude() != null;
    }


}