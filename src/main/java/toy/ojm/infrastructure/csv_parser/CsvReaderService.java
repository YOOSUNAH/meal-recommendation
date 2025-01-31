package toy.ojm.infrastructure.csv_parser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.RestaurantRepository;
import toy.ojm.infrastructure.PublicDataConstants;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
        log.debug("##### readAndSaveCSV 진행 시작 | 현재 시간 : " + new Date().toString());
        long startTime = System.currentTimeMillis();

        try {
            // 1. csv 읽기
            List<CsvData> csvDataList = readAllFromCsv();

            // 2. DB 데이터 조회
            // list로 할때
//            List<Restaurant> existingRestaurant = restaurantRepository.findAll();
            // Map으로 할때
            Map<String, Restaurant> existingRestaurant = restaurantRepository.findAll().stream()
                .collect(Collectors.toMap(
                    Restaurant::getManagementNumber,
                    restaurant -> restaurant));


            // 3. 데이터 처리 및 저장
            List<Restaurant> restaurantsToSave = new ArrayList<>();
            for (CsvData csvdata : csvDataList) {
                if (csvdata.isClosedBusiness()||  // 폐업한 가게는 skip
                        csvdata.getLongitude() == null ||   // 좌표가 없는 가게는 skip
                        csvdata.getLatitude() == null) {
                    continue;
                }

                // list로 할때
//                Restaurant restaurant = existingRestaurant.stream()
//                        .filter(r -> r.getManagementNumber().equals(csvdata.getManagementNumber()))
//                        .findFirst()
//                        .orElse(new Restaurant());
                // Map으로 할때
                Restaurant restaurant = existingRestaurant.getOrDefault(csvdata.getManagementNumber(), new Restaurant());


                updateRestaurantInfo(restaurant, csvdata);
                restaurantsToSave.add(restaurant);

                // BATCH_SIZE 씩 저장
                if (restaurantsToSave.size() >= BATCH_SIZE) {
                    restaurantRepository.saveAll(restaurantsToSave);
                    log.debug("##### BATCH_SIZE 씩 저장 중");
                    restaurantsToSave.clear();
                }
            }

            if (!csvDataList.isEmpty()) {
                log.debug("##### 남은 식당들 저장 중 ");
                restaurantRepository.saveAll(restaurantsToSave);
            }


        } catch (IOException e) {
            log.error("##### CSV 파일을 읽는 도중 오류 발생", e);
            throw new RuntimeException(e);
        }

        log.debug("##### readAndSaveCSV 진행 끝 | 현재 시간 : " + new Date().toString());
        log.debug("##### 걸린 시간 : {} ms", System.currentTimeMillis() - startTime);
    }

    private List<CsvData> readAllFromCsv() throws IOException {
        List<CsvData> csvDataList = new ArrayList<>();
        int progressCounter = 0;
        try {
            Path csvFilePath = PublicDataConstants.DESTINATION_DIRECTORY.resolve(
                PublicDataConstants.DESTINATION_FILE_NAME + "." + PublicDataConstants.DESTINATION_FILE_EXTENSION
            );

            File csvFile = new File(csvFilePath.toString());
            if(csvFile.exists()){
                log.debug("##### CSV 파일이 있습닌다.");
            }else{
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
}

