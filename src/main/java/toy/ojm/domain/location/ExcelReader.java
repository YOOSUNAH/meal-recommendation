package toy.ojm.domain.location;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import toy.ojm.domain.entity.Restaurant;

public class ExcelReader {

    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("src/main/resources/sample.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        // 엑셀 index는 0부터 시작
        int rowindex = 0;
        int columnindex = 0;

        // 시트 수
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 행의 수
        int rows = sheet.getPhysicalNumberOfRows();
        for(rowindex = 1; rowindex < rows; rowindex++){

            Restaurant restaurant = new Restaurant();

            // 행 읽기
            XSSFRow row = sheet.getRow(rowindex);

            if(row != null) {
                int cells = row.getPhysicalNumberOfCells();
                for (columnindex = 0; columnindex <= cells; columnindex++){

                    XSSFCell cell = row.getCell(columnindex);
                    String value = "";

                    if(cell == null){
                        continue;
                    }else{
                        // type 별로 내용 읽기
                        switch (cell.getCellType()) {
                            case FORMULA:
                                value = cell.getCellFormula() + "";
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                    }
                    System.out.println("각 셀 내용 : " + value);
                }

                restaurant.setBusinessStatus(String.valueOf(row.getCell(7)));
//            restaurant.setNumber(String.valueOf(row.getCell(12)));
//            restaurant.setAddress(String.valueOf(row.getCell(15)));
//            restaurant.setRoadAddress(String.valueOf(row.getCell(16)));
//            restaurant.setName(String.valueOf(row.getCell(18)));
//            restaurant.setCategory(String.valueOf(row.getCell(22)));
//            restaurant.setLatitude(Double.valueOf(String.valueOf(row.getCell(23))));
//            restaurant.setLongitude(Double.valueOf(String.valueOf(row.getCell(24))));


            }

        }

    }




}
