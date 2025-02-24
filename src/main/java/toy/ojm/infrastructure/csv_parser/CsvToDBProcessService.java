package toy.ojm.infrastructure.csv_parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.RestaurantRepository;
import toy.ojm.domain.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvToDBProcessService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;
    private final TransactionTemplate txTemplate;

    @Async
    public CompletableFuture<Void> processPageAsync(
            int page,
            List<CsvData> csvDataList
    ) {
        return CompletableFuture.runAsync(() -> {
            log.debug("##### === Async 실행 중 (Thread: {}) page : {}  ", Thread.currentThread().getName(), page);

            try {
                // 1. 현재 처리할 CSV 데이터의 managementNumber 목록 추출
                List<String> managementNumbers = csvDataList
                        .stream()
                        .map(CsvData::getManagementNumber) // 람다 표현식 : csvData -> csvData.getManagementNumber()
                        .collect(Collectors.toList());

                // 2. 해당 managementNumber에 해당하는 Restaurant만 조회
                Map<String, Restaurant> batchMap = restaurantRepository.findAllByManagementNumberIn(managementNumbers)
                        .stream()
                        .collect(Collectors.toMap(
                                Restaurant::getManagementNumber,
                                restaurant -> restaurant));

                // 3. 데이터 처리 및 저장
                List<Restaurant> restaurantsToSave = new ArrayList<>();
                for (CsvData csvdata : csvDataList) {
                    // 필터링
                    if (csvdata.isClosedBusiness() ||  // 폐업한 가게는 skip
                            csvdata.getLongitude() == null ||   // 좌표가 없는 가게는 skip
                            csvdata.getLatitude() == null) {
                        continue;
                    }

                    //  기존 데이터가 있으면 업데이트, 없으면 새로 생성
                    Restaurant restaurant = batchMap.getOrDefault(csvdata.getManagementNumber(), new Restaurant());

                    setRestaurantInfo(restaurant, csvdata);
                    restaurantsToSave.add(restaurant);

                    // BATCH_SIZE 씩 저장
                    if (restaurantsToSave.size() >= CsvDataProcessConstants.BATCH_SIZE) {
                        execute(() -> saveWithBatch(new ArrayList<>(restaurantsToSave))); // 복사본으로 전달
                        log.debug("##### {}개 저장 완료", restaurantsToSave.size());
                    }
                }

                if (!restaurantsToSave.isEmpty()) {
                    execute(() -> saveWithBatch(new ArrayList<>(restaurantsToSave)));
                    log.debug("##### 남은 식당들 저장");
                }

            } catch (Exception e) {
                log.error("#### Thread: {}, async 중 에러 발생 {}", Thread.currentThread().getName(), e.getMessage());
            }
        });
    }

    private void setRestaurantInfo(Restaurant restaurant, CsvData csvdata) {
        restaurant.setManagementNumber(csvdata.getManagementNumber());
        restaurant.setBusinessStatus(csvdata.getBusinessStatus());
        restaurant.setNumber(csvdata.getNumber());
        restaurant.setAddress(csvdata.getAddress());
        restaurant.setRoadAddress(csvdata.getRoadAddress());
        restaurant.setName(csvdata.getName());
        restaurant.setCategory(csvdata.getCategory());

        // 좌표 변경
        ProjCoordinate transformed = transCoordination.transformToWGS(csvdata.getLongitude(), csvdata.getLatitude());
        restaurant.setLongitude(transformed.x);
        restaurant.setLatitude(transformed.y);
    }

    private void execute(Runnable runnable) {
        txTemplate.execute((status) -> {
            try {
                runnable.run();
            } catch (Exception e) {
                status.setRollbackOnly();
            }
            return null;
        });
    }

    private void saveWithBatch(List<Restaurant> restaurantsToSave) {
        restaurantRepository.saveAll(restaurantsToSave);
        restaurantsToSave.clear();
    }
}