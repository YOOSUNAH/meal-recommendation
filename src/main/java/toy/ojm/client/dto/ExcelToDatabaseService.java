package toy.ojm.client.dto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.repository.JdbcTemplateMemberRepository;
import toy.ojm.repository.RestaurantRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelToDatabaseService {

    private final JdbcTemplateMemberRepository jdbcTemplateMemberRepository;

    public ExcelToDatabaseService(JdbcTemplateMemberRepository jdbcTemplateMemberRepository) {
        this.jdbcTemplateMemberRepository = jdbcTemplateMemberRepository;
    }

    public void saveDataToDatabase(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(0);

            List<RestaurantEntity> restaurantEntities = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                RestaurantEntity entity = createRestaurantEntity((Row) row);
                if (entity != null) {
                    restaurantEntities.add(entity);
                }
            }

            jdbcTemplateMemberRepository.saveAll(restaurantEntities);

        } catch (IOException e) {
            e.printStackTrace();
            // 적절한 예외 처리 로직 추가
        }
    }

    private RestaurantEntity createRestaurantEntity(Row row) {
        try {
            RestaurantEntity entity = new RestaurantEntity();
            entity.setDtlStateNm(row.getCell(0).getStringCellValue());
            entity.setSiteWhLaDdr(row.getCell(1).getStringCellValue());
            entity.setRdNWhLaDdr(row.getCell(2).getStringCellValue());
            entity.setBpLcNm(row.getCell(3).getStringCellValue());
            entity.setUpTadNm(row.getCell(4).getStringCellValue());
            entity.setX(row.getCell(5).getStringCellValue());
            entity.setY(row.getCell(6).getStringCellValue());
            return entity;
        } catch (Exception e) {
            // 처리되지 않은 예외 발생 시 로그 출력
            e.printStackTrace();
            return null;
        }
    }
}