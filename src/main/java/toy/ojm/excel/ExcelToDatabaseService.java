package toy.ojm.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.repository.JdbcTemplateMemberRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelToDatabaseService {
 /*
 난 데이터 베이스에 서울특별시 음식점 데이터가 있어. 음식점 데이터는 좌표값을 가지고 있어. 크롬 브라우저를 이용하여 사용자로부터  현재 위치를 받아와서,
 해당 위치 100M 이내의 음식점들을 찾는 MySQL 8 쿼리를 작성하고 싶어!
  */
//    private final JdbcTemplateMemberRepository jdbcTemplateMemberRepository;
    private final ExcelReader excelReader;
    private final JdbcTemplate jdbcTemplate;

    public void readFromExcelAndSave(String filePath) {
        List<RestaurantDTO> excelRestaurants = excelReader.read(filePath);
        List<RestaurantEntity> restaurantEntities = convertRestaurantEntities(excelRestaurants);
//        jdbcTemplateMemberRepository.batchInsert(restaurantEntities);

        // 데이터베이스에 데이터 삽입
        for (RestaurantEntity entity : restaurantEntities) {
            insertData(entity);
        }
    }

    private void insertData(RestaurantEntity entity) {
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

        // 쿼리 실행
        if (success > 0) {
            log.info("데이터가 성공적으로 삽입되었습니다.");
        } else {
            log.warn("데이터 삽입이 실패하였습니다.");
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

//        private RestaurantEntity createRestaurantEntity (RestaurantDTO excelRestaurant){
//            try {
//                RestaurantEntity entity = new RestaurantEntity();
//                entity.setBusinessStatus(excelRestaurant.getBusinessStatus());
//                entity.setStreetNumberAddress(excelRestaurant.getStreetNumberAddress());
//                entity.setStreetNameAddress(excelRestaurant.getStreetNameAddress());
//                entity.setRestaurantName(excelRestaurant.getRestaurantName());
//                entity.setCategory(excelRestaurant.getCategory());
//                entity.setLongitude(excelRestaurant.getLongitude());
//                entity.setLatitude(excelRestaurant.getLatitude());
//                return entity;
//            } catch (Exception e) {
//                // 처리되지 않은 예외 발생 시 로그 출력
//                e.printStackTrace();
//                return null;
//            }
//        }
    }
}
