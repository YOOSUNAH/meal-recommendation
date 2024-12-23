package toy.ojm.infrastructure.csv_parser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.RestaurantRepository;
import toy.ojm.infrastructure.PublicDataConstants;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;
    private final int BATCH_SIZE = 5000;

    @Scheduled(cron = "0 55 23 * * *")
    @Transactional
    public void readAndSaveCSV() {
        log.info("readAndSaveCSV 진행 - 현재 시간 : " + new Date().toString());
        long startTime = System.currentTimeMillis();

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

            log.debug("## readAndSaveCSV 메서드 중 Resolved CSV file path: {}", csvFilePath.toString());

            csvFile = new File(csvFilePath.toString());

            fis = new FileInputStream(csvFile);
            isr = new InputStreamReader(fis, Charset.forName("EUC_KR"));
            br = new BufferedReader(isr);

            String line;

            // 첫 번째 행(제목 행)을 읽고 버림
            br.readLine();

            List<Restaurant> restaurantList = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                progressCounter++;
                if (progressCounter % 100 == 0) {
                    log.debug("progressCounter : {}", progressCounter);
                }
                List<String> aLine = Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1));

                // 폐업한 곳은 skip
                if (closedBusiness(aLine)) continue;

                // 기존에있으면 update, 기존에 없으면 새로 저장 (orElse문으로 새로운 객체 생성)
                String ManagementNumber = removeDoubleQuote(aLine.get(1));
                Restaurant restaurant = restaurantRepository.findByManagementNumber(ManagementNumber).orElse(new Restaurant());
                setRestaurantInfo(restaurant, aLine, restaurantList);

                // BATCH_SIZE 씩 저장
                if(restaurantList.size() >= BATCH_SIZE){
                    restaurantRepository.saveAll(restaurantList);
                    restaurantList.clear();
                }
            }

            if(!restaurantList.isEmpty()){
                restaurantRepository.saveAll(restaurantList);
            }


            log.info("걸린 시간 : {}", System.currentTimeMillis() - startTime);

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
    }

    private static boolean closedBusiness(List<String> aLine) {
        String businessStatus = aLine.get(7);
        return businessStatus != null && businessStatus.contains("폐업");
    }

    private void setRestaurantInfo(Restaurant restaurant, List<String> aLine, List<Restaurant> restaurantList) {
        restaurant.setManagementNumber(removeDoubleQuote(aLine.get(1)));
        restaurant.setBusinessStatus(removeDoubleQuote(aLine.get(7)));
        restaurant.setNumber(removeDoubleQuote(aLine.get(12)));
        restaurant.setAddress(removeDoubleQuote(aLine.get(15)));
        restaurant.setRoadAddress(removeDoubleQuote(aLine.get(16)));
        restaurant.setName(removeDoubleQuote(aLine.get(18)));
        restaurant.setCategory(removeDoubleQuote(aLine.get(22)));

        // 좌표 변경
        Double beforeLongitude = parseDouble(aLine.get(23), aLine);
        Double beforeLatitude = parseDouble(aLine.get(24), aLine);

        if(beforeLongitude == null || beforeLatitude == null){
            // 좌표가 정확하지 않은 가게는 skip
            log.warn(" 좌표가 정확하지 않은 가게 : {}", aLine.get(18));
            return;
        }

        ProjCoordinate transformed = transCoordination.transformToWGS(beforeLongitude, beforeLatitude);
        restaurant.setLongitude(transformed.x);
        restaurant.setLatitude(transformed.y);

        restaurantList.add(restaurant);
    }

    private Double parseDouble(
        String coordinate,
        List<String> aLine
    ) {
        try {
            return Double.parseDouble(removeDoubleQuote(coordinate));
        } catch (NumberFormatException e) {
            log.error("Invalid number format for coordinate. {} - {}", aLine.get(18), aLine.get(23));
            log.error("Invalid coordinate format: {}, {}", aLine.get(18), coordinate);
        }
        return null;
    }

    private String removeDoubleQuote(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.replaceAll("\"", "");
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // 좌표 변경하기
    public void transCoordinate() {
        List<Restaurant> newRestaurant = getAllRestaurants();
        for (Restaurant restaurant : newRestaurant) {
            ProjCoordinate coordinate = transCoordination.transformToWGS(restaurant.getLongitude(),
                restaurant.getLatitude());
            restaurant.setLongitude(coordinate.x);
            restaurant.setLatitude(coordinate.y);
        }
        restaurantRepository.saveAll(newRestaurant);
    }
}

