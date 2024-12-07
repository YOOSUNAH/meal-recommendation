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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;

    @Transactional
    public void readAndSaveCSV() {
        Path csvFilePath;
        File csvFile;
        BufferedReader br = null;
        InputStreamReader isr = null;
        FileInputStream fis = null;

        int progressCounter = 0;
        try {
            csvFilePath = Paths.get(
                new ClassPathResource(PublicDataConstants.DESTINATION_DIRECTORY).getFile().getAbsolutePath()).resolve(PublicDataConstants.DESTINATION_FILE_NAME +
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

            List<Restaurant> restaurants = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                progressCounter++;
                if (progressCounter % 100 == 0) {
                    log.debug("progressCounter : {}", progressCounter);
                }
                List<String> aLine = Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1));

                String businessStatus = aLine.get(7);
                if (businessStatus != null && businessStatus.contains("폐업")) {
                    continue;
                }

                Restaurant restaurant = new Restaurant();
                restaurant.setBusinessStatus(removeDoubleQuote(aLine.get(7)));
                restaurant.setNumber(removeDoubleQuote(aLine.get(12)));
                restaurant.setAddress(removeDoubleQuote(aLine.get(15)));
                restaurant.setRoadAddress(removeDoubleQuote(aLine.get(16)));
                restaurant.setName(removeDoubleQuote(aLine.get(18)));
                restaurant.setCategory(removeDoubleQuote(aLine.get(22)));

                String beforeLongitude = aLine.get(23);
                String beforeLatitude = aLine.get(24);

                restaurant.setLongitude(parseDouble(beforeLongitude, aLine));
                restaurant.setLatitude(parseDouble(beforeLatitude, aLine));

                // 좌표 변경
                ProjCoordinate transformed = transCoordination.transformToWGS(
                    restaurant.getLongitude(),
                    restaurant.getLatitude()
                );
                restaurant.setLongitude(transformed.x);
                restaurant.setLatitude(transformed.y);

                restaurants.add(restaurant);
                if (restaurants.size() == 10000) {
                    restaurantRepository.saveAll(restaurants);
                    restaurants.clear();
                }
            }
            restaurantRepository.saveAll(restaurants);

        } catch (Exception e) {
            log.error(e.getMessage());
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

    private double parseDouble(
        String coordinate,
        List<String> aLine
    ) {
        try {
            return Double.parseDouble(removeDoubleQuote(coordinate));
        } catch (NumberFormatException e) {
            log.error("Invalid number format for coordinate. {} - {}", aLine.get(18), aLine.get(23));
        }
        return 0;
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

