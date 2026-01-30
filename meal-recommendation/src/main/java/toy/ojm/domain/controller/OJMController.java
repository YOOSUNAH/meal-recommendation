package toy.ojm.domain.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toy.ojm.csv.CsvProcessor;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.dto.RestaurantRequestDto;
import toy.ojm.domain.dto.RestaurantResponseDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.service.OJMService;
import toy.ojm.global.dto.ResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/ojm")
public class OJMController {
  private final OJMService ojmService;
  private final CsvProcessor csvProcessor;

  @PostMapping("/category")
  public ResponseEntity<ResponseDto<Void>> getRecommendation(
      @RequestBody CategoryRequestDto request, HttpSession session) {
    ojmService.recommend(request, session);
    return ResponseDto.of(HttpStatus.OK, null);
  }

  @GetMapping("/category")
  public FoodCategory getCategory() {
    return ojmService.getLastCategory();
  }

  @PostMapping("/transCoordinate")
  public void transCoordinate() {
    csvProcessor.transCoordinate();
  }

  @GetMapping("/restaurant")
  public ResponseEntity<List<Restaurant>> saveRestaurant() {
    return ResponseEntity.ok(ojmService.getAllRestaurants());
  }

  @PostMapping("/nearbyRestaurant")
  public ResponseEntity<ResponseDto<List<RestaurantResponseDto>>> aroundRestaurants(
      @RequestBody RestaurantRequestDto restaurantRequestDto) {
    List<RestaurantResponseDto> restaurants =
        ojmService.getRandomRestaurants(
            restaurantRequestDto.getCurrentLat(),
            restaurantRequestDto.getCurrentLon(),
            restaurantRequestDto.getSelectedCategories());

    if (restaurants == null || restaurants.isEmpty()) {
      return ResponseDto.notFound("근처에 식당이 없습니다.");
    }
    return ResponseDto.of(HttpStatus.OK, restaurants);
  }

  // Todo 실제 거리와 도보상 거리가 차이가 나서 , 결과가 상이함.
  @PostMapping("/closeRestaurant")
  public ResponseEntity<ResponseDto<List<RestaurantResponseDto>>> getClosestRestaurants(
      @RequestBody RestaurantRequestDto restaurantRequestDto) {
    List<RestaurantResponseDto> restaurants =
        ojmService.getClosestRestaurants(
            restaurantRequestDto.getCurrentLat(),
            restaurantRequestDto.getCurrentLon(),
            restaurantRequestDto.getSelectedCategories());

    if (restaurants == null || restaurants.isEmpty()) {
      return ResponseDto.notFound("근처에 식당이 없습니다.");
    } else {
      return ResponseDto.of(HttpStatus.OK, restaurants);
    }
  }

  @GetMapping("/health")
  public ResponseEntity<String> healthCheck() {
    log.info(" ## === 테스트 로그 출력!");
    return ResponseEntity.ok("OK");
  }
}
