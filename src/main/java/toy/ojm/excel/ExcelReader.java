package toy.ojm.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class ExcelReader {

//    public static void main(String[] args) {
//        try {
//            new ExcelReader().read();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
                // log 찍어보기
//                log.info(String.valueOf(row.getCell(0)));
//                log.info(String.valueOf(row.getCell(1)));
//                log.info(String.valueOf(row.getCell(2)));

                if (row == null) {
                    continue;
                }
                RestaurantDTO rdto = new RestaurantDTO();
                // 각 열의 데이터를 RestaurantDTO에 저장
                rdto.setBusinessStatus(row.getCell(0).getStringCellValue());
                rdto.setStreetNumberAddress(row.getCell(1).getStringCellValue());
                rdto.setStreetNameAddress(row.getCell(2).getStringCellValue());
                rdto.setRestaurantName(row.getCell(3).getStringCellValue());
                rdto.setCategory(row.getCell(4).getStringCellValue());

                // Longitude 및 Latitude 셀의 데이터를 NUMERIC 타입으로 가져오기
                if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                    rdto.setLongitude(String.valueOf(row.getCell(5).getNumericCellValue()));
                } else {
                    // NUMERIC 타입이 아닐 경우
                    System.out.println("Cell 5 in row " + r + " is not a numeric type.");
                }

                if (row.getCell(6).getCellType() == CellType.NUMERIC) {
                    rdto.setLatitude(String.valueOf(row.getCell(6).getNumericCellValue()));
                } else {
                    // NUMERIC 타입이 아닐 경우
                    System.out.println("Cell 6 in row " + r + " is not a numeric type.");
                }
                list.add(rdto);
            }
        } catch (Exception e) {
            log.error("Excel file read error: {}", e.getMessage());
        }
        return list;
    }
}
