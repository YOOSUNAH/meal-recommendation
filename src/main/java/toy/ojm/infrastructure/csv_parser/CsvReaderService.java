package toy.ojm.infrastructure.csv_parser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.locationtech.proj4j.ProjCoordinate;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;
    private final CsvToDBProcessService csvToDBProcessService;
    private final Executor taskExecutor;

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
            int totalPages = (int) Math.ceil((double) totalCount / CsvDataProcessConstants.BATCH_SIZE);
            int totalSize = csvDataList.size();

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int pageNo = 0; pageNo < totalPages; pageNo++) {
                int fromIndex = pageNo * CsvDataProcessConstants.BATCH_SIZE;
                int toIndex = Math.min(fromIndex + CsvDataProcessConstants.BATCH_SIZE, totalSize);
                List<CsvData> seperateCsvDataList = csvDataList.subList(fromIndex, toIndex);

                futures.add(csvToDBProcessService.processPageAsync(pageNo, seperateCsvDataList));
            }

//            if (!csvDataList.isEmpty()) {
//                log.debug("##### 남은 식당들 저장 중 ");
//                restaurantRepository.saveAll(restaurantsToSave);
//            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
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

}