package toy.ojm.client.dto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExcelToDatabaseServiceTest {

    @Autowired
    private ExcelToDatabaseService sut;

    @Test
    @Disabled
    void test(){
        // given

        // when
        sut.saveDataToDatabase("선아 로컬에 엑셀 파일 위치 경로 추가");

        // then

    }

}