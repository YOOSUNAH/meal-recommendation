package toy.ojm.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.RestaurantRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelReaderService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;

    // csv -> excel

    public void readExcelAndSaveTo(Path path) throws IOException {
//        FileInputStream inputStream = new FileInputStream("src/main/resources/sample.xlsx");
//        FileInputStream inputStream = new FileInputStream("src/main/resources/csv-data/sample.xlsx");

        InputStream inputStream = Files.newInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // 엑셀 index는 0부터 시작
        int rowindex = 0;
        // 시트 수
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 행의 수
        int rows = sheet.getPhysicalNumberOfRows();
        List<Restaurant> restaurants = new ArrayList<>();
        for (rowindex = 1; rowindex < rows; rowindex++) {
            // 행 읽기
            XSSFRow row = sheet.getRow(rowindex);
            if (row != null) {
                Restaurant restaurant = new Restaurant();
                restaurant.setBusinessStatus(String.valueOf(row.getCell(7)));
                restaurant.setNumber(String.valueOf(row.getCell(12)));
                restaurant.setAddress(String.valueOf(row.getCell(15)));
                restaurant.setRoadAddress(String.valueOf(row.getCell(16)));
                restaurant.setName(String.valueOf(row.getCell(18)));
                restaurant.setCategory(String.valueOf(row.getCell(22)));

//                // 먼저 변경전 좌표계 저장
//                restaurant.setLongitude(Double.valueOf(String.valueOf(row.getCell(23))));
//                restaurant.setLatitude(Double.valueOf(String.valueOf(row.getCell(24))));

                // 바로 좌표 변경해서 저장하기
                Double utmkX = Double.valueOf(String.valueOf(row.getCell(23)));
                Double utmkY = Double.valueOf(String.valueOf(row.getCell(24)));
                ProjCoordinate coordinate = transCoordination.transformToWGS(utmkX, utmkY);
                restaurant.setLongitude(coordinate.x);
                restaurant.setLatitude(coordinate.y);

                restaurants.add(restaurant);
            }
        }
        restaurantRepository.saveAll(restaurants);
        workbook.close();
        inputStream.close();
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // 좌표 변경하기
    public void transCoordinate() {
        List<Restaurant> newRestaurant = getAllRestaurants();
        for(Restaurant restaurant : newRestaurant){
            ProjCoordinate coordinate = transCoordination.transformToWGS(restaurant.getLongitude(), restaurant.getLatitude());
            restaurant.setLongitude(coordinate.x);
            restaurant.setLatitude(coordinate.y);
        }
        restaurantRepository.saveAll(newRestaurant);
    }
}
