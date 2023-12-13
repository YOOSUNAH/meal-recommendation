package toy.ojm.excel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import toy.ojm.controller.dto.MealRecommendationRequest;

@SpringBootTest(properties = {"resources.file.path=classpath:서울강남구영업중인음식점.xlsx"})
class ExcelToDatabaseServiceTest {

    @Autowired
    private ExcelToDatabaseService excelToDatabaseService;

    @Value("${resources.file.path}")
    private String testFilePath;
    @Test
    void testReadFromExcelAndSave() {
        // given
        MealRecommendationRequest request = new MealRecommendationRequest();

        // when
        try {
            excelToDatabaseService.readFromExcelAndSave(testFilePath, request);
        } catch (Exception e) {
            e.printStackTrace(); // 테스트 실패 시 오류 메시지 출력
            throw e; // 테스트를 실패 상태로 변경
        }
        // then
    }
}