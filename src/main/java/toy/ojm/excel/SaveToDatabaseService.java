package toy.ojm.excel;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.locationtech.proj4j.ProjCoordinate;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.stereotype.Service;
    import toy.ojm.entity.DatabaseRestaurant;
    import toy.ojm.domain.TransCoordination;

    import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveToDatabaseService {
    private final JdbcTemplate jdbcTemplate;
    private final TransCoordination transCoordination;

    public void saveRestaurants(List<DatabaseRestaurant> restaurantEntities) {
        for (DatabaseRestaurant entity : restaurantEntities) {
            ProjCoordinate transformedCoordinates = transCoordination.transformToGCS(
                entity.getLongitude(), entity.getLatitude()
            );
            entity.setLongitude(transformedCoordinates.x);
            entity.setLatitude(transformedCoordinates.y);
            insertData(entity);
        }
    }
        private void insertData(DatabaseRestaurant entity) { // RestaurantEntity를 데이터베이스에 삽입하는 메서드
            try {
                if (entity.getBusinessStatus() != null && entity.getBusinessStatus().equalsIgnoreCase("영업")) {  // '영업' 상태의 데이터만 데이터베이스에 삽입
                    int success = jdbcTemplate.update("""
                        INSERT INTO restaurantTable (
                        businessStatus, StreetNumberAddress, 
                        restaurantName, category, longitude, latitude
                        ) 
                        VALUES (?, ?, ?, ?, ?, ?)
                        """,
                        entity.getBusinessStatus(),
                        entity.getStreetNumberAddress(),
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
                } else {
                    log.warn("Ignoring non-'영업' business status data.");
                }
            } catch (Exception e) {
                log.error("Error inserting data: {}", e.getMessage());
            }
        }
    }
