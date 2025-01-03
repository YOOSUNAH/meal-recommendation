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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;
    private final int BATCH_SIZE = 5000;

    @Transactional
    public void readAndSaveCSV() {
        log.info("readAndSaveCSV 진행 - 현재 시간 : " + new Date().toString());
        long startTime = System.currentTimeMillis();

        try {

            // 1. csv 읽기
            List<CsvData> csvDataList = readAllFromCsv();

            // 2. DB 데이터 조회
            Map<String, Restaurant> existingRestaurant = restaurantRepository.findAll().stream()
                    .collect(Collectors.toMap(
                            Restaurant::getManagementNumber,
                            restaurant -> restaurant));

            // 3. 데이터 처리 및 저장
            List<Restaurant> restaurantsToSave = new ArrayList<>();
            for(CsvData csvdata : csvDataList){
                if(csvdata.isClosedBusiness()){
                    continue;
                }

                Restaurant restaurant = existingRestaurant.getOrDefault(csvdata.getManagementNumber(), new Restaurant());

                updateRestaurantInfo(restaurant, csvdata);

                restaurantsToSave.add(restaurant);

                // BATCH_SIZE 씩 저장
                if(restaurantsToSave.size() >= BATCH_SIZE){
                    restaurantRepository.saveAll(restaurantsToSave);
                    restaurantsToSave.clear();
                }
            }

            if(!csvDataList.isEmpty()){
                restaurantRepository.saveAll(restaurantsToSave);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            log.info("걸린 시간 : {}", System.currentTimeMillis() - startTime);
    }

    private List<CsvData> readAllFromCsv() throws IOException {

        List<CsvData> csvDataList = new ArrayList<>();

        Path csvFilePath;
        File csvFile;
        BufferedReader br = null;
        InputStreamReader isr = null;
        FileInputStream fis = null;

        int progressCounter = 0;
        try {
            csvFilePath = Paths.get(
                            new ClassPathResource(PublicDataConstants.DESTINATION_DIRECTORY).getFile().getAbsolutePath())
                    .resolve(PublicDataConstants.DESTINATION_FILE_NAME +
                            "." +
                            PublicDataConstants.DESTINATION_FILE_EXTENSION
                    );


            csvFile = new File(csvFilePath.toString());

            fis = new FileInputStream(csvFile);
            isr = new InputStreamReader(fis, Charset.forName("EUC_KR"));
            br = new BufferedReader(isr);

            String line;

            // 첫 번째 행(제목 행)을 읽고 버림
            br.readLine();

            while ((line = br.readLine()) != null) {
                progressCounter++;
                List<String> columns = Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1));
                csvDataList.add(new CsvData(columns));
            }
          } catch (Exception e) {
        log.error("readAndSaveCSV 중 오류 발생 : {}" ,e.getMessage());
    } finally {
        log.info("##### {} 라인까지 완료", progressCounter);
        if (br != null) {
            try {
                br.close();
            } catch (IOException ignore) {
            }
        }
        if (isr != null) {
            try {
                isr.close();
            } catch (IOException ignore) {
            }
        }
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException ignore) {
            }
        }
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
        Double beforeLongitude = csvdata.getLongitude();
        Double beforeLatitude = csvdata.getLatitude();

        if(beforeLongitude == null || beforeLatitude == null){
            // 좌표가 정확하지 않은 가게는 skip
            log.warn(" 좌표가 정확하지 않은 가게 : {}", csvdata.getName());
            return;
        }

        ProjCoordinate transformed = transCoordination.transformToWGS(beforeLongitude, beforeLatitude);
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

