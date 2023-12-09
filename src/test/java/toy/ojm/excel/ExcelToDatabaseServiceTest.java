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
        excelToDatabaseService.saveDataToDatabase("/Users/yuseon-a/Downloads/서울시일반음식점.xlsx");

        // then

    }

}