package toy.ojm.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.stereotype.Component;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExcelReader {  //  Excel 파일을 읽어서 RestaurantDTO 객체 리스트로 변환하는데 사용된다.

    public List<ExcelRestaurantData> read(String filePath) {  // filePath를 전달값으로 받는 Excel 파일에서 데이터를 읽는 메서드
        List<ExcelRestaurantData> list = new ArrayList<>();  // RestaurantDTO 객체를 저장할 리스트를 생성
        FileInputStream inputStream = null;  // 파일 입력 스트림과 워크북 객체를 초기화
        XSSFWorkbook workbook = null;  // 파일 입력 스트림과 워크북 객체를 초기화

        try {
            inputStream = new FileInputStream(filePath); // 파일을 읽어들이기 위해 inputStream 이라는 FileInputStream 객체를 생성한다.
            workbook = new XSSFWorkbook(inputStream); // 엑셀 파일을 XSSFWorkbook으로 로드한다.
            // XSSFWorkbook은 Apache POI 라이브러리에서 제공하는 클래스 중 하나로, 이 클래스는 Microsoft Excel 파일의 .xlsx 형식을 읽고 쓰는 데 사용된다.

            XSSFSheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져온다.
            int rows = sheet.getPhysicalNumberOfRows(); // 시트의 행의 개수를 가져와 rows 라는 변수에 저장한다.

            // 행을 반복하여 Excel 파일의 각 행을 확인한다.
            for (int r = 1; r < 1457; r++) { // 엑셀 행의 갯수에 맞춰 반복 (첫 번째 행은 헤더일 수 있으므로 r = 1로 수정), 엑셀 행의 갯수에맞춰 r<1457로 수정
                XSSFRow row = sheet.getRow(r); // 특정 행을 가져온다.
                if (row == null) {
                    continue; // 행이 비어있다면 다음 행으로 넘어간다.
                }
                // 각 셀에서 데이터를 가져온다.
                XSSFCell businessStatusCell = row.getCell(7);
                XSSFCell streetNumberAddressCell = row.getCell(15);
                XSSFCell restaurantNameCell = row.getCell(18);
                XSSFCell categoryCell = row.getCell(22);
                XSSFCell xCell = row.getCell(23);
                XSSFCell yCell = row.getCell(24);
                if (businessStatusCell == null || streetNumberAddressCell == null ||
                    restaurantNameCell == null || categoryCell == null || xCell == null || yCell == null) {
                    continue; // 셀에 데이터가 비어있다면 다음 행으로 넘어간다.
                }
                // 셀로부터 데이터를(값을) 가져와서 임시 변수에 저장한다.
                String tempBusinessStatus = businessStatusCell.getStringCellValue();
                String tempStreetNumberAddress = streetNumberAddressCell.getStringCellValue();
                String tempRestaurantName = restaurantNameCell.getStringCellValue();
                String tempCategory = categoryCell.getStringCellValue();
                Double tempX = xCell.getNumericCellValue();
                Double tempY = yCell.getNumericCellValue();

                // log 찍어보기 (log에 임시 변수들의 값을 출력)
                log.info("tempBusinessStatus : {}", tempBusinessStatus);
                log.info("tempStreetNumberAddress : {}", tempStreetNumberAddress);
                log.info("tempRestaurantName : {}", tempRestaurantName);
                log.info("tempCategory : {}", tempCategory);
                log.info("tempX : {}", tempX);
                log.info("tempY : {}", tempY);
                log.info("");

                // 각 열의 데이터를 RestaurantDTO에 저장
                ExcelRestaurantData rdto = new ExcelRestaurantData(); // RestaurantDTO 객체를 생성한다.
                rdto.setBusinessStatus(tempBusinessStatus);  // RestaurantDTO 객체에 임시변수들을 저장한다.
                rdto.setStreetNumberAddress(tempStreetNumberAddress);
                rdto.setRestaurantName(tempRestaurantName);
                rdto.setCategory(tempCategory);
                rdto.setLatitude(tempX);
                rdto.setLongitude(tempY);

                list.add(rdto); // 생성된 RestaurantDTO 객체를 리스트에 추가
            }
        } catch (Exception e) {
            log.error(e.getMessage());  // 예외가 발생하면 오류를 로그에 기록하고 출력
            e.printStackTrace(); // 스택 트레이스 출력
        } finally {
            try {
                if (workbook != null) {
                    workbook.close(); // 사용이 끝난 워크북을 닫는다.
                }
                if (inputStream != null) {
                    inputStream.close(); // 사용이 끝난 입력 스트림을 닫는다.
                }
            } catch (Exception ex) {
                log.error("Error closing resources: {}", ex.getMessage());
            }
        }
        return list;  // 데이터가 저장된 RestaurantDTO 객체 리스트를 반환한다.
    }
}
