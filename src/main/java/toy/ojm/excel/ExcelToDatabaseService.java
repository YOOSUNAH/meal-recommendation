package toy.ojm.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.repository.JdbcTemplateMemberRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelToDatabaseService {

    private final JdbcTemplateMemberRepository jdbcTemplateMemberRepository;
    private final ExcelReader excelReader;

    public void saveDataToDatabase(String filePath) {
        List<RestaurantDTO> excelRestaurants = excelReader.read();
        List<RestaurantEntity> restaurantEntities = convertRestaurantEntities(excelRestaurants);
        jdbcTemplateMemberRepository.batchInsert(restaurantEntities);
    }

    private List<RestaurantEntity> convertRestaurantEntities(List<RestaurantDTO> excelRestaurants) {
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for (RestaurantDTO excelRestaurant : excelRestaurants) {
            restaurantEntities.add(
                createRestaurantEntity(excelRestaurant)
            );
        }
        return restaurantEntities;
    }

    private RestaurantEntity createRestaurantEntity(RestaurantDTO excelRestaurant) {
        try {
            RestaurantEntity entity = new RestaurantEntity();
            entity.setDtlStateNm(excelRestaurant.getDtlStateNm());
            entity.setSiteWhLaDdr(excelRestaurant.getSiteWhLaDdr());
            entity.setRdNWhLaDdr(excelRestaurant.getRdNWhLaDdr());
            entity.setBpLcNm(row.getCell(3).getStringCellValue());
            entity.setUpTadNm(row.getCell(4).getStringCellValue());
            entity.setX(row.getCell(5).getStringCellValue());
            entity.setY(row.getCell(6).getStringCellValue());
            return entity;
        } catch (Exception e) {
            // 처리되지 않은 예외 발생 시 로그 출력
            e.printStackTrace();
            return null;
        }
    }
}