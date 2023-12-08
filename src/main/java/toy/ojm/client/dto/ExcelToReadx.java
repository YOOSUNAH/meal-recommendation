package toy.ojm.client.dto;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelToReadx {

    public static void main(String[] args) {
        try {
            new ExcelToReadx().startx();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startx() {
        try {
            FileInputStream inputStream = new FileInputStream("/Users/yuseon-a/Downloads/서울시일반음식점.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            int sheetCn = workbook.getNumberOfSheets();

            for (int cn = 0; cn < sheetCn; cn++) {
                XSSFSheet sheet = workbook.getSheetAt(cn);
                int rows = sheet.getPhysicalNumberOfRows();

                for (int r = 1; r < rows; r++) { // 첫 번째 행은 헤더일 수 있으므로 r = 1로 수정
                    XSSFRow row = sheet.getRow(r);
                    if (row != null) {
                        ArrayList<RestaurantDTO> list = new ArrayList<>();
                        RestaurantDTO rdto = new RestaurantDTO();

                        // 각 열의 데이터를 RestaurantDTO에 저장
                        rdto.setDTLSTATENM(row.getCell(0).getStringCellValue());
                        rdto.setSITEWHLADDR(row.getCell(1).getStringCellValue());
                        rdto.setRDNWHLADDR(row.getCell(2).getStringCellValue());
                        rdto.setBPLCNM(row.getCell(3).getStringCellValue());
                        rdto.setUPTAENM(row.getCell(4).getStringCellValue());
                        rdto.setX(row.getCell(5).getStringCellValue()); // X, Y 데이터가 문자열 형태인 경우
                        rdto.setY(row.getCell(6).getStringCellValue());

                        list.add(rdto);

                        Locationprintx lp = new Locationprintx();
                        lp.printx(list); // Locationprintx 클래스의 printx 메서드에 데이터 전달
                    }
                }
            }

            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
