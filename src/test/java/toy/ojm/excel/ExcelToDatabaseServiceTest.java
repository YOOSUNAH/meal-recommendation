package toy.ojm.excel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExcelToDatabaseServiceTest {

    @Autowired
    private ExcelToDatabaseService excelToDatabaseService;

    @Test
    @Disabled
    void test(){
        // given

        // when
        try {
            excelToDatabaseService.saveDataToDatabase("/Users/yuseon-a/Downloads/서울강남구영업중인음식점.xlsx");
        } catch (Exception e) {
            e.printStackTrace(); // 테스트 실패 시 오류 메시지 출력
            throw e; // 테스트를 실패 상태로 변경
        }
        // then

    }

}