package toy.ojm.domain.controller;

import jakarta.servlet.http.HttpSession;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.dto.RestaurantRequestDto;
import toy.ojm.domain.dto.RestaurantResponseDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.service.CsvReaderService;
import toy.ojm.domain.service.OJMService;
import toy.ojm.global.ResponseDto;
import toy.ojm.infrastructure.restaurant_openapi.PublicDataDownloader;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm")
public class OJMController {

    private final OJMService ojmService;
    private final PublicDataDownloader publicDataDownloader;
    private final CsvReaderService csvReaderService;

    @PostMapping("/category")
    public ResponseEntity<ResponseDto<Void>> getRecommendation(
        @RequestBody CategoryRequestDto request,
        HttpSession session
    ) {
        ojmService.recommend(request, session);
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/category")
    public FoodCategory getCategory(
        HttpSession session
    ) {
        return ojmService.getLastCategory();
    }

    @PostMapping("/transCoordinate")
    public void transCoordinate(
    ) {
        csvReaderService.transCoordinate();
        log.info("좌표 변환 요청 API 성공!");
    }

    @PostMapping("/restaurant/crawl")
    public ResponseEntity<Path> saveRestaurantByCrawling(){
        Path path = publicDataDownloader.downloadCsvFile();
        log.info("크롤링한  데이터 java로 불러와서 저장하기");
        return ResponseEntity.ok(path);
    }

    @PostMapping("/csv")
    public ResponseEntity<ResponseDto<Void>> csvReadAndSave() {
        csvReaderService.readAndSaveCSV();
        log.info("csv 읽어오기");
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<Restaurant>> saveRestaurant() {
        List<Restaurant> restaurants = csvReaderService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @PostMapping("/nearbyRestaurant")
    public ResponseEntity<ResponseDto<List<RestaurantResponseDto>>> AroundRestaurants(
        @RequestBody RestaurantRequestDto restaurantRequestDto
    ) {
        List<RestaurantResponseDto> restaurants = ojmService.AroundRestaurants(
            restaurantRequestDto.getCurrentLat(),
            restaurantRequestDto.getCurrentLon(),
            restaurantRequestDto.getSelectedCategories()
        );

        if (restaurants == null || restaurants.isEmpty()) {
            return ResponseDto.notFound("근처에 식당이 없습니다.");
        } else {
            return ResponseDto.of(HttpStatus.OK, restaurants);
        }
    }
}
