package toy.ojm.domain.service;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.TestData;
import toy.ojm.domain.repository.TestDataRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class TestDataService {

    private final TestDataRepository testDataRepository;

    public TestDataService(TestDataRepository testDataRepository){
        this.testDataRepository = testDataRepository;
    }

    // 1번: 기존 데이터 삭제 후 추가
    @Transactional
    public long deleteAndInsertData(List<TestData> newDataList){

        long startTime = System.currentTimeMillis();

        // 모든 데이터 삭제
        testDataRepository.deleteAll();

        // 새로운 데이터 삽입
        testDataRepository.saveAll(newDataList);

        long finishTime = System.currentTimeMillis();
        long takeTime = finishTime - startTime;

        log.info("1번 기존 데이터 삭제 후 추가 takeTime : {}", takeTime);

        return takeTime;
    }


    // 2번: 기존 데이터 수정
    @Transactional
    public long updateData0(List<TestData> newDataList) {
        long startTime = System.currentTimeMillis();

        for (TestData newData : newDataList) {
            testDataRepository.findByUniqueKey(newData.getUniqueKey())
                    .ifPresentOrElse(existingData -> {
                        // 데이터가 있으면 업데이트
                        existingData.setValue(newData.getValue());
                        testDataRepository.save(existingData);
                    }, () -> {
                        // 데이터가 없으면 새로 추가
                        testDataRepository.save(newData);
                    });
        }

        long finishTime = System.currentTimeMillis();
        long takeTime = finishTime - startTime;

        log.info("2번 기존 데이터 수정 takeTime : {}", takeTime);

        return takeTime;
    }

    @Transactional
    public long updateData(List<TestData> newDataList) {
        long startTime = System.currentTimeMillis();

        // 새로운 데이터의 고유 키를 리스트로 추출
        List<String> uniqueKeys = newDataList.stream()
                .map(TestData::getUniqueKey)
                .collect(Collectors.toList());

        // 데이터베이스에서 기존 데이터를 한 번에 조회
        List<TestData> existingDataList = testDataRepository.findAllByUniqueKeyIn(uniqueKeys);

        // 데이터 업데이트 맵 생성
        Map<String, TestData> existingDataMap = existingDataList.stream()
                .collect(Collectors.toMap(TestData::getUniqueKey, existingData -> existingData));

        // 새로운 데이터 리스트를 순회하여 업데이트
        for (TestData newData : newDataList) {
            existingDataMap.computeIfPresent(newData.getUniqueKey(), (key, existingData) -> {
                existingData.setValue(newData.getValue());
                return existingData;  // 수정된 데이터를 반환
            });
        }

        // 수정된 데이터들을 일괄 저장
        testDataRepository.saveAll(existingDataMap.values());

        long finishTime = System.currentTimeMillis();
        long takeTime = finishTime - startTime;

        log.info("2번 기존 데이터 수정 takeTime : {}", takeTime);

        return takeTime;
    }



    // 10만 개 데이터 생성
    public List<TestData> generateTestData(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new TestData("key " + i, "value" + i))
                .collect(Collectors.toList());

    }

}
