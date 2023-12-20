package toy.ojm.excel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import toy.ojm.controller.dto.MealRecommendationRequest;

@SpringBootTest
class ExcelToDatabaseServiceTest {

    @Autowired
    private ExcelToDatabaseService excelToDatabaseService;

    @Test
    void testReadFromExcelAndSave() {
        // given
        MealRecommendationRequest request = new MealRecommendationRequest();

        // when
        try {
            excelToDatabaseService.readFromExcelAndSave(
                "/Users/donggeollee/IdeaProjects/ebuild-github/meal-recommendation/src/main/resources/서울시종로구음식점_20개이하.xlsx",
                request
            );
        } catch (Exception e) {
            e.printStackTrace(); // 테스트 실패 시 오류 메시지 출력
            throw e; // 테스트를 실패 상태로 변경
        }
    }
}