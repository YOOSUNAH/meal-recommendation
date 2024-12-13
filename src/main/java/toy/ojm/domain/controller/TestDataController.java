package toy.ojm.domain.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.domain.entity.TestData;
import toy.ojm.domain.service.TestDataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestDataController {

    private final TestDataService testDataService;
    private final int TestDataCount = 1000;

    @GetMapping("/test-performance")
    public String testPerformance(){
        // 데이터 생성
        List<TestData> testData = testDataService.generateTestData(TestDataCount);

        // 1번 방식 실행 시간 측정
       long deleteAndInsertTime = testDataService.deleteAndInsertData(testData);

        return String.format("Delete and Insert: %d ms", deleteAndInsertTime);

    }

    @GetMapping("/test-performance2")
    public String testPerformance2(){
        // 데이터 생성
        List<TestData> testData = testDataService.generateTestData(TestDataCount);

        // 2번 방식 실행 시간 측정
        long updateTime = testDataService.updateData(testData);

        return String.format("Update: %d ms", updateTime);

    }

}
