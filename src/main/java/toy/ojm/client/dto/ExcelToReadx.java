package toy.ojm.client.dto;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFCell;
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

        public String startx() {
            XSSFRow row;
            XSSFCell cell;
            String x  = null;

            try {
                FileInputStream inputStream = new FileInputStream("/Users/yuseon-a/Downloads/서울시일반음식점.xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                int sheetCn = workbook.getNumberOfSheets();

                for (int cn = 0; cn < sheetCn; cn++) {
                    XSSFSheet sheet = workbook.getSheetAt(cn);
                    int rows = sheet.getPhysicalNumberOfRows();

                    for (int r = 0; r < rows; r++) {
                       row = sheet.getRow(r);
                        if (row != null) {
                            ArrayList<RestaurantDTO> list = new ArrayList<RestaurantDTO>();
                            for (int c = 0; c < 1; c++) {
                                 cell = row.getCell(c);
                                if (cell != null) {
                                    String value = null;

                                    switch (cell.getCellType()) {

                                        case XSSFCell.CELL_TYPE_NUMERIC :

                                            value = "" + cell.getNumericCellValue();
                                            RestaurantDTO rdto = new RestaurantDTO();
                                            rdto.setX(value);
                                            list.add(rdto);
                                            break;
                                        // Handle other cell types as needed
                                    }
                                }
                            }
                           Locationprintx lp = new Locationprintx();
                            lp.printx();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }
    }

