package toy.ojm.client.dto;

import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileReader {

   public static void main(String[] args) {
      try {
         FileInputStream file = new FileInputStream("/Users/yuseon-a/Downloads/서울시일반음식점.xlsx");

         // 엑셀 파일을 읽기 위한 객체 생성
         XSSFWorkbook workbook = new XSSFWorkbook(file);

         // 첫 번째 시트를 가져옴 (0부터 시작)
         XSSFSheet sheet = workbook.getSheetAt(0);

         // 시트에서 원하는 작업 수행

         // 엑셀 파일 닫기
         workbook.close();
         file.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
