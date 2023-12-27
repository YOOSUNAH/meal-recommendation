package toy.ojm.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExcelReader {

    public List<RestaurantDTO> read(String filePath) {

        List<RestaurantDTO> list = new ArrayList<>();
        FileInputStream inputStream = null;
        XSSFWorkbook workbook = null;


        try {
            inputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int r = 1; r < 1457; r++) { // 첫 번째 행은 헤더일 수 있으므로 r = 1로 수정, 엑셀 행의 갯수에맞춰 r<1457로 수정
                XSSFRow row = sheet.getRow(r);

                if (row == null) {
                    continue;
                }

                XSSFCell businessStatusCell = row.getCell(7);
                XSSFCell streetNumberAddressCell = row.getCell(15);
                XSSFCell streetNameAddressCell = row.getCell(16);
                XSSFCell restaurantNameCell = row.getCell(18);
                XSSFCell categoryCell = row.getCell(22);
                XSSFCell xCell = row.getCell(23);
                XSSFCell yCell = row.getCell(24);

                if (businessStatusCell == null || streetNumberAddressCell == null || streetNameAddressCell == null ||
                    restaurantNameCell == null || categoryCell == null || xCell == null || yCell == null) {
                    continue;
                }

                // 값 가져오기
                String tempBusinessStatus = businessStatusCell.getStringCellValue();
                String tempStreetNumberAddress = streetNumberAddressCell.getStringCellValue();
                String tempStreetNameAddress = streetNameAddressCell.getStringCellValue();
                String tempRestaurantName = restaurantNameCell.getStringCellValue();
                String tempCategory = categoryCell.getStringCellValue();
                Double tempX = xCell.getNumericCellValue();
                Double tempY = yCell.getNumericCellValue();

                // log 찍어보기
                log.info("tempBusinessStatus : {}", tempBusinessStatus);
                log.info("tempStreetNumberAddress : {}", tempStreetNumberAddress);
                log.info("tempStreetNameAddress : {}", tempStreetNameAddress);
                log.info("tempRestaurantName : {}", tempRestaurantName);
                log.info("tempCategory : {}", tempCategory);
                log.info("tempX : {}", tempX);
                log.info("tempY : {}", tempY);
                log.info("");

                // 각 열의 데이터를 RestaurantDTO에 저장
                RestaurantDTO rdto = new RestaurantDTO();
                rdto.setBusinessStatus(tempBusinessStatus);
                rdto.setStreetNumberAddress(tempStreetNumberAddress);
                rdto.setStreetNameAddress(tempStreetNameAddress);
                rdto.setRestaurantName(tempRestaurantName);
                rdto.setCategory(tempCategory);
                rdto.setLatitude(tempX);
                rdto.setLongitude(tempY);

                list.add(rdto);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
