package toy.ojm.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

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

            for (int r = 1; r < 100; r++) { // 첫 번째 행은 헤더일 수 있으므로 r = 1로 수정
                XSSFRow row = sheet.getRow(r);

                if (row == null) {
                    continue;
                }

                if (row.getCell(7) == null) {
                    break;
                }

                // log 찍어보기
                String tempBusinessStatus = row.getCell(7).getStringCellValue();
                log.info("tempBusinessStatus : {}", tempBusinessStatus);
                String tempStreetNumberAddress = row.getCell(14).getRawValue().trim();
                log.info("tempStreetNumberAddress : {}", tempStreetNumberAddress);
                String tempStreetNameAddress = row.getCell(15).getStringCellValue();
                log.info("tempStreetNameAddress : {}", tempStreetNameAddress);
                String tempRestaurantName = row.getCell(18).getStringCellValue();
                log.info("tempRestaurantName : {}", tempRestaurantName);
                String tempCategory = row.getCell(22).getStringCellValue();
                log.info("tempCategory : {}", tempCategory);
                if (row.getCell(23) == null) {
                    log.info("");
                    continue;
                }
                Double tempX = row.getCell(23).getNumericCellValue();
                log.info("tempX : {}", tempX);
                Double tempY = row.getCell(24).getNumericCellValue();
                log.info("tempY : {}", tempY);
                log.info("");

                RestaurantDTO rdto = new RestaurantDTO();

                // 각 열의 데이터를 RestaurantDTO에 저장
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
//            log.error(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
