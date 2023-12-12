package toy.ojm.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.repository.JdbcTemplateMemberRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelToDatabaseService {

    private final JdbcTemplateMemberRepository jdbcTemplateMemberRepository;
    private final ExcelReader excelReader;

    public void saveDataToDatabase(String filePath) {
        try {
            List<RestaurantDTO> excelRestaurants = excelReader.read();
            List<RestaurantEntity> restaurantEntities = convertRestaurantEntities(excelRestaurants);
            jdbcTemplateMemberRepository.batchInsert(restaurantEntities);

            try (
                // MySQL 데이터베이스 연결
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/OJMDB", "ojm", "4027")) {

                // 데이터베이스에 데이터 삽입
                for (RestaurantEntity entity : restaurantEntities) {
                    insertData(connection, entity);
                }
            }
        } catch (SQLException e) {
            log.error("SQL error : {}", e.getMessage());
        }
    }

    private void insertData(Connection connection, RestaurantEntity entity) throws SQLException {
        String sql = "INSERT INTO restaurantTable (businessStatus, streetNumberAddress, streetNameAddress, restaurantName, category, longitude, latitude) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getBusinessStatus());
            preparedStatement.setString(2, entity.getStreetNumberAddress());
            preparedStatement.setString(3, entity.getStreetNameAddress());
            preparedStatement.setString(4, entity.getRestaurantName());
            preparedStatement.setString(5, entity.getCategory());
            preparedStatement.setString(6, entity.getLongitude());
            preparedStatement.setString(7, entity.getLatitude());

            // 쿼리 실행
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                log.info("데이터가 성공적으로 삽입되었습니다.");
            } else {
                log.warn("데이터 삽입이 실패하였습니다.");
            }
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