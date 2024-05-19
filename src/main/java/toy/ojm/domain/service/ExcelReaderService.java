package toy.ojm.domain.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.repository.RestaurantRepository;


@Service
@RequiredArgsConstructor
public class ExcelReaderService {

    private final RestaurantRepository restaurantRepository;

    public void readExcelAndSaveTo() throws IOException {
        FileInputStream inputStream = new FileInputStream("src/main/resources/sample.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // 엑셀 index는 0부터 시작
        int rowindex = 0;
        int columnindex = 0;

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
//                int cells = row.getPhysicalNumberOfCells();
//                for (columnindex = 0; columnindex <= cells; columnindex++) {
//
//                    XSSFCell cell = row.getCell(columnindex);
//                    String value = "";
//
//                    if (cell == null) {
//                        continue;
//                    } else {
//                        // type 별로 내용 읽기
//                        switch (cell.getCellType()) {
//                            case FORMULA:
//                                value = cell.getCellFormula() + "";
//                                break;
//                            case STRING:
//                                value = cell.getStringCellValue() + "";
//                                break;
//                            case NUMERIC:
//                                value = cell.getNumericCellValue() + "";
//                                break;
//                            case BLANK:
//                                value = cell.getBooleanCellValue() + "";
//                                break;
//                            case ERROR:
//                                value = cell.getErrorCellValue() + "";
//                                break;
//                        }
//                    }
//                }

                restaurant.setBusinessStatus(String.valueOf(row.getCell(7)));
                restaurant.setNumber(String.valueOf(row.getCell(12)));
                restaurant.setAddress(String.valueOf(row.getCell(15)));
                restaurant.setRoadAddress(String.valueOf(row.getCell(16)));
                restaurant.setName(String.valueOf(row.getCell(18)));
                restaurant.setCategory(String.valueOf(row.getCell(22)));
                restaurant.setLatitude(Double.valueOf(String.valueOf(row.getCell(23))));
                restaurant.setLongitude(Double.valueOf(String.valueOf(row.getCell(24))));
            }
        }
        restaurantRepository.saveAll(restaurants);
        workbook.close();
        inputStream.close();
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }
}
