package toy.ojm.infra;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.repository.RestaurantRepository;
import toy.ojm.global.calculator.CoordinateConvertor;
import toy.ojm.global.constants.CsvConstants;
import toy.ojm.global.dto.CsvDataDto;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantCsvReader {

    private final RestaurantRepository restaurantRepository;
    private final CoordinateConvertor coordinateConvertor;
    private final TransactionTemplate txTemplate;

    @Transactional
    public void readAndSaveCSV() {
        log.debug("##### readAndSaveCSV 진행 시작 | 현재 시간 : " + new Date().toString());
        final StopWatch stopWatch = StopWatch.createStarted();

        try {
            // 1. csv 읽기
            List<CsvDataDto> csvDataDtoList = readAllFromCsv();
            log.debug("##### 1. csv read - {} ", stopWatch.getTime(TimeUnit.MILLISECONDS));
            stopWatch.reset();
            stopWatch.start();

            // 2. DB 조회
            log.debug("##### 2. db restaurant read - {} ", stopWatch.getTime(TimeUnit.MILLISECONDS));
            stopWatch.reset();
            stopWatch.start();

            long totalCount = restaurantRepository.count();
            int totalPages = (int) Math.ceil((double) totalCount / CsvConstants.BATCH_SIZE);
            int totalSize = csvDataDtoList.size();

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            // 3. DB 청크 단위 비동기 업데이트
            for (int pageNo = 0; pageNo < totalPages; pageNo++) {
                int fromIndex = pageNo * CsvConstants.BATCH_SIZE;
                int toIndex = Math.min(fromIndex + CsvConstants.BATCH_SIZE, totalSize);
                List<CsvDataDto> seperateCsvDataDtoList = csvDataDtoList.subList(fromIndex, toIndex);
                futures.add(processPageAsync(pageNo, seperateCsvDataDtoList));
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.debug("##### 3. db restaurant update - {} ms ", stopWatch.getTime(TimeUnit.MILLISECONDS));
        } catch (IOException e) {
            log.error("##### CSV 파일을 읽는 도중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }

    private List<CsvDataDto> readAllFromCsv() throws IOException {
        List<CsvDataDto> csvDataDtoList = new ArrayList<>();
        int progressCounter = 0;
        try {
            Path csvFilePath = CsvConstants.DESTINATION_DIRECTORY.resolve(
                CsvConstants.DESTINATION_FILE_NAME + "." + CsvConstants.DESTINATION_FILE_EXTENSION
            );

            File csvFile = new File(csvFilePath.toString());
            if (csvFile.exists()) {
                log.debug("##### CSV 파일이 있습닌다.");
            } else {
                log.error("##### CSV 파일 찾기에 실패했습니다. .: {}", csvFile.getAbsolutePath());
            }

            FileInputStream fis = new FileInputStream(csvFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("EUC_KR"));
            BufferedReader br = new BufferedReader(isr);

            // 첫 번째 행(제목 행)을 읽고 버림
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                progressCounter++;
                List<String> columns = Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1));
                csvDataDtoList.add(new CsvDataDto(columns));
            }

        } catch (Exception e) {
            log.error("##### readAndSaveCSV 중 오류 발생 : {}", e.getMessage());
        } finally {
            log.debug("##### {} 라인까지 읽기 완료", progressCounter);
        }
        return csvDataDtoList;
    }

    @Transactional
    public void transCoordinate() {
        List<Restaurant> newRestaurant = restaurantRepository.findAll();
        for (Restaurant restaurant : newRestaurant) {
            ProjCoordinate coordinate = coordinateConvertor.transformToWGS(
                restaurant.getLongitude(),
                restaurant.getLatitude()
            );
            restaurant.setLongitude(coordinate.x);
            restaurant.setLatitude(coordinate.y);
        }
        restaurantRepository.saveAll(newRestaurant);
    }

    @Async
    public CompletableFuture<Void> processPageAsync(
        int page,
        List<CsvDataDto> csvDataDtoList
    ) {
        return CompletableFuture.runAsync(() -> {
            log.debug("##### === Async 실행 중 (Thread: {}) page : {}  ", Thread.currentThread().getName(), page);

            try {
                // 1. 현재 처리할 CSV 데이터의 managementNumber 목록 추출
                List<String> managementNumbers = csvDataDtoList
                    .stream()
                    .map(CsvDataDto::getManagementNumber) // 람다 표현식 : csvData -> csvData.getManagementNumber()
                    .collect(Collectors.toList());

                // 2. 해당 managementNumber에 해당하는 Restaurant만 조회
                Map<String, Restaurant> batchMap = restaurantRepository.findAllByManagementNumberIn(managementNumbers)
                    .stream()
                    .collect(
                        Collectors.toMap(
                            Restaurant::getManagementNumber,
                            restaurant -> restaurant
                        )
                    );

                // 3. 데이터 처리 및 저장
                List<Restaurant> restaurantsToSave = new ArrayList<>();
                for (CsvDataDto csvdata : csvDataDtoList) {
                    // 필터링
                    if (csvdata.isClosedBusiness() ||  // 폐업한 가게는 skip
                        csvdata.getLongitude() == null ||   // 좌표가 없는 가게는 skip
                        csvdata.getLatitude() == null
                    ) {
                        continue;
                    }

                    //  기존 데이터가 있으면 업데이트, 없으면 새로 생성
                    Restaurant restaurant = batchMap.getOrDefault(csvdata.getManagementNumber(), new Restaurant());

                    setRestaurantInfo(restaurant, csvdata);
                    restaurantsToSave.add(restaurant);

                    // BATCH_SIZE 씩 저장
                    if (restaurantsToSave.size() >= CsvConstants.BATCH_SIZE) {
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

    private void setRestaurantInfo(
        Restaurant restaurant,
        CsvDataDto csvdata
    ) {
        restaurant.setManagementNumber(csvdata.getManagementNumber());
        restaurant.setBusinessStatus(csvdata.getBusinessStatus());
        restaurant.setNumber(csvdata.getNumber());
        restaurant.setAddress(csvdata.getAddress());
        restaurant.setRoadAddress(csvdata.getRoadAddress());
        restaurant.setName(csvdata.getName());
        restaurant.setCategory(csvdata.getCategory());

        // 좌표 변경
        ProjCoordinate transformed = coordinateConvertor.transformToWGS(csvdata.getLongitude(), csvdata.getLatitude());
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