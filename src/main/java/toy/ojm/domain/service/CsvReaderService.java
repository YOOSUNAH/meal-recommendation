package toy.ojm.domain.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Service;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.RestaurantRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderService {

    private final RestaurantRepository restaurantRepository;
    private final TransCoordination transCoordination;

    public void readCSV() {
        try {
            File file = new File("out/production/resources/csv-data/data.csv");
            BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "EUC-KR"));
            String line;

            // 첫 번째 행(제목 행)을 읽고 버림
            br.readLine();

            List<Restaurant> restaurants = new ArrayList<>();

            int lineCount = 0; // 처리된 줄의 수를 추적하는 카운터

            while ((line = br.readLine()) != null && lineCount < 50) {
                List<String> aLine = new ArrayList<>();
                String[] lineArr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                aLine = Arrays.asList(lineArr);
                //  System.out.println(aLine);

                Restaurant restaurant = new Restaurant();
                restaurant.setBusinessStatus(aLine.get(7));
                restaurant.setNumber(aLine.get(12));
                restaurant.setAddress(aLine.get(15));
                restaurant.setRoadAddress(aLine.get(16));
                restaurant.setName(aLine.get(18));
                restaurant.setCategory(aLine.get(22));

                String beforeLongitude =  aLine.get(23);
                String beforeLatitude =  aLine.get(24);

                try {
                    restaurant.setLongitude(Double.parseDouble(beforeLongitude.replaceAll("\"", "")));  // 쌍따옴표제거 추가
                } catch (NumberFormatException e) {
                    log.error("Invalid number format for longitude: " + aLine.get(23));
                    restaurant.setLongitude(0.0);
                }

                try {
                    restaurant.setLatitude(Double.parseDouble(beforeLatitude.replaceAll("\"", "")));
                } catch (NumberFormatException e) {
                    log.error("Invalid number format for latitude: " + aLine.get(24));
                    restaurant.setLatitude(0.0);
                }

                //  좌표 변환
//                ProjCoordinate coordinate =
//                    transCoordination.transformToWGS(restaurant.getLongitude(), restaurant.getLatitude());
//                restaurant.setLongitude(coordinate.x);
//                restaurant.setLatitude(coordinate.y);

                // 저장
                restaurants.add(restaurant);

                lineCount++;
                restaurantRepository.saveAll(restaurants);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

