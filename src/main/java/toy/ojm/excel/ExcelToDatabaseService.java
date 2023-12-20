package toy.ojm.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.domain.Coordinates;
import toy.ojm.domain.TransCoordination;
import toy.ojm.entity.RestaurantEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelToDatabaseService {

    private final ExcelReader excelReader;
    private final JdbcTemplate jdbcTemplate;
    private final TransCoordination transCoordination;

    public void readFromExcelAndSave(String filePath, MealRecommendationRequest request) {
        List<RestaurantDTO> excelRestaurants = excelReader.read(filePath);
        List<RestaurantEntity> restaurantEntities = convertRestaurantEntities(excelRestaurants);

        for (RestaurantEntity entity : restaurantEntities) {
            ProjCoordinate transformedCoordinates = transCoordination.transformToGCS(Double.parseDouble(String.valueOf(entity.getLongitude())), Double.parseDouble(String.valueOf(entity.getLatitude())));
            entity.setLongitude(Double.valueOf(String.valueOf(transformedCoordinates.x)));
            entity.setLatitude(Double.valueOf(String.valueOf(transformedCoordinates.y)));
            insertData(entity);
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
                    businessStatus, StreetNumberAddress,streetNameAddress, 
                    restaurantName, category, longitude, latitude
                    ) 
                    VALUES (?, ?, ? , ?, ?, ?, ?)
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
                log.info("Data inserted successfully.");
            } else {
                log.warn("Failed to insert data.");
            }
        } catch (Exception e) {
            log.error("Error inserting data: {}", e.getMessage());
        }
    }

    public List<RestaurantEntity> getNearbyRestaurants(Coordinates coordinates) {
        List<RestaurantEntity> nearbyRestaurants = new ArrayList<>();
        try {
            // 사용자의 현재 위치를 데이터베이스의 TM 좌표계에서 Geolocation의 GCS 좌표계로 변환
            ProjCoordinate transformedCoordinates = transCoordination.transformToGCS(Double.parseDouble(String.valueOf(coordinates.getLongitude())), Double.parseDouble(String.valueOf(coordinates.getLatitude())));
            coordinates.setLongitude(String.valueOf(transformedCoordinates.x));
            coordinates.setLatitude(String.valueOf(transformedCoordinates.y));

            //100m 이내에 음식점 쿼리문
            String query = "SELECT * FROM restaurantTable WHERE ST_DISTANCE_SPHERE(POINT(longitude, latitude), POINT(?, ?)) <= 2000";
            Object[] params = {coordinates.getLongitude(), coordinates.getLatitude()};

            nearbyRestaurants = jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
                RestaurantEntity restaurant = new RestaurantEntity();
                restaurant.setBusinessStatus(resultSet.getString("businessStatus"));
                restaurant.setStreetNumberAddress(resultSet.getString("streetNumberAddress"));
                restaurant.setStreetNameAddress(resultSet.getString("streetNameAddress"));
                restaurant.setRestaurantName(resultSet.getString("restaurantName"));
                restaurant.setCategory(resultSet.getString("category"));
                restaurant.setLongitude(Double.valueOf(resultSet.getString("longitude")));
                restaurant.setLatitude(Double.valueOf(resultSet.getString("latitude")));
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
