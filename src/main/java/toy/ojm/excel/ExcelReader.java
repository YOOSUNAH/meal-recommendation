package toy.ojm.excel;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelReader {

    public static void main(String[] args) {
        try {
            new ExcelReader().read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RestaurantDTO> read() {
        FileInputStream inputStream = null;
        XSSFWorkbook workbook = null;
        try {
            inputStream = new FileInputStream("/Users/yuseon-a/Downloads/서울시일반음식점.xlsx");
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<RestaurantDTO> list = new ArrayList<>();
        try {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int r = 1; r < 100; r++) { // 첫 번째 행은 헤더일 수 있으므로 r = 1로 수정
                XSSFRow row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                RestaurantDTO rdto = new RestaurantDTO();
                // 각 열의 데이터를 RestaurantDTO에 저장
                rdto.setDtlStateNm(row.getCell(7).getStringCellValue());
                rdto.setSiteWhLaDdr(row.getCell(15).getStringCellValue());
                rdto.setRdNWhLaDdr(row.getCell(16).getStringCellValue());
                rdto.setBpLcNm(row.getCell(18).getStringCellValue());
                rdto.setUpTadNm(row.getCell(22).getStringCellValue());
                rdto.setX(row.getCell(23).getStringCellValue()); // X, Y 데이터가 문자열 형태인 경우
                rdto.setY(row.getCell(24).getStringCellValue());

                list.add(rdto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }
}
