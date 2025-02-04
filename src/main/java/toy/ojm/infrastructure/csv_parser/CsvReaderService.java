package toy.ojm.infrastructure.csv_parser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.RestaurantRepository;
import toy.ojm.infrastructure.PublicDataConstants;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;
    private final int BATCH_SIZE = 1000;

    @Transactional
    public void readAndSaveCSV() {
        log.info("##### readAndSaveCSV 진행 시작 | 현재 시간 : " + new Date().toString());
        final StopWatch stopWatch = StopWatch.createStarted();

        try {
            // 1. csv 읽기
            List<CsvData> csvDataList = readAllFromCsv();
            log.info("##### 1. csv read - {} ", stopWatch.getTime(TimeUnit.MILLISECONDS));
            stopWatch.reset();
            stopWatch.start();

//             2. DB 데이터 조회
//            Map<String, Restaurant> existingRestaurant = restaurantRepository.findAll().stream()
//                .collect(Collectors.toMap(
//                    Restaurant::getManagementNumber,
//                    restaurant -> restaurant));

            log.info("##### 2. db restaurant read - {} ", stopWatch.getTime(TimeUnit.MILLISECONDS));
            stopWatch.reset();
            stopWatch.start();

            long totalCount = restaurantRepository.count();
            int totalPages = (int) Math.ceil((double) totalCount / BATCH_SIZE);
            int totalSize = csvDataList.size();

            for (int pageNo = 0; pageNo < totalPages; pageNo++) {
                log.debug("##### 비동기 호출 준비 중 | 페이지 번호: {}", pageNo);

                int fromIndex = pageNo * BATCH_SIZE;
                int toIndex = Math.min(fromIndex + BATCH_SIZE, totalSize);
                List<CsvData> seperateCsvDataList = csvDataList.subList(fromIndex, toIndex);

                processPageAsync(pageNo, seperateCsvDataList);

                fromIndex += BATCH_SIZE;
                toIndex += BATCH_SIZE;
            }

//            if (!csvDataList.isEmpty()) {
//                log.debug("##### 남은 식당들 저장 중 ");
//                restaurantRepository.saveAll(restaurantsToSave);
//            }
            log.info("##### 3. db restaurant update - {} ", stopWatch.getTime(TimeUnit.MILLISECONDS));
        } catch (IOException e) {
            log.error("##### CSV 파일을 읽는 도중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }

    private List<CsvData> readAllFromCsv() throws IOException {
        List<CsvData> csvDataList = new ArrayList<>();
        int progressCounter = 0;
        try {
            Path csvFilePath = PublicDataConstants.DESTINATION_DIRECTORY.resolve(
                    PublicDataConstants.DESTINATION_FILE_NAME + "." + PublicDataConstants.DESTINATION_FILE_EXTENSION
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
                csvDataList.add(new CsvData(columns));
            }

        } catch (Exception e) {
            log.error("readAndSaveCSV 중 오류 발생 : {}", e.getMessage());
        } finally {
            log.debug("##### {} 라인까지 읽기 완료", progressCounter);
        }
        return csvDataList;
    }

    private void updateRestaurantInfo(Restaurant restaurant, CsvData csvdata) {
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

    // api 용
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // 좌표 변경하기
    @Transactional
    public void transCoordinate() {
        List<Restaurant> newRestaurant = restaurantRepository.findAll();
        for (Restaurant restaurant : newRestaurant) {
            ProjCoordinate coordinate = transCoordination.transformToWGS(restaurant.getLongitude(),
                restaurant.getLatitude());
            restaurant.setLongitude(coordinate.x);
            restaurant.setLatitude(coordinate.y);
        }
        restaurantRepository.saveAll(newRestaurant);
    }

    @Async
    public void processPageAsync(int page, List<CsvData> csvDataList) {
        log.debug("##### === Async 실행 중 (Thread: {}) === ", Thread.currentThread().getName());
        // 호출 스택트레이스 로깅 (async가 잘 되고 있는건지 확인을 위해 추가)
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        log.debug("##### Call stack:");
        for (StackTraceElement element : stackTrace) {
            log.debug(" -> {}", element);
        }

        try{
            PageRequest pageRequest = PageRequest.of(page, BATCH_SIZE);
            Page<Restaurant> restaurantPage = restaurantRepository.findAll(pageRequest); // Todo : findByManagementNumberIn ?

            Map<String, Restaurant> batchMap = restaurantPage.getContent()
                    .stream()
                    .collect(Collectors.toMap(
                            Restaurant::getManagementNumber,
                            restaurant -> restaurant));

            // 3. 데이터 처리 및 저장
            List<Restaurant> restaurantsToSave = new ArrayList<>();
            for (CsvData csvdata : csvDataList) {
                if (csvdata.isClosedBusiness() ||  // 폐업한 가게는 skip
                        csvdata.getLongitude() == null ||   // 좌표가 없는 가게는 skip
                        csvdata.getLatitude() == null) {
                    continue;
                }

                Restaurant restaurant = batchMap.getOrDefault(csvdata.getManagementNumber(), new Restaurant());

                updateRestaurantInfo(restaurant, csvdata);
                restaurantsToSave.add(restaurant);

                // BATCH_SIZE 씩 저장
                if (restaurantsToSave.size() >= BATCH_SIZE) {
                    saveWithBatch(restaurantsToSave);

                }
            }
        }
        catch(Exception e){
            log.error("#### Thread: {}, async 중 에러 발생 {}", Thread.currentThread().getName() ,e.getMessage());
        }
    }

    @Transactional
    private void saveWithBatch(List<Restaurant> restaurantsToSave) {
        restaurantRepository.saveAll(restaurantsToSave);
        log.debug("##### {}개 저장 완료", restaurantsToSave.size());
        restaurantsToSave.clear();
    }

}