package toy.ojm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.domain.Coordinates;
import toy.ojm.domain.RecommendService;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.excel.ExcelToDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;
    private final ExcelToDatabaseService excelToDatabaseService;

    @PostMapping("/api/recommend")
    public ResponseEntity<MealRecommendationResponse> getRecommendation(
        @RequestBody MealRecommendationRequest request
    ) {
        // 로그 추가: 요청이 들어왔음을 확인
        log.info("API '/api/recommend'로 POST 요청이 들어왔습니다.");

        // 추천된 음식점 목록을 서비스에서 가져옴
        MealRecommendationResponse recommendation = recommendService.recommend(request);
        log.info("추천된 음식점 목록: {}", recommendation); // 추천된 음식점 목록을 로그로 출력

        if (recommendation == null) {
            log.error("추천된 음식점 목록이 null입니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("요청 들어옴");

        // ResponseEntity로 JSON 형식으로 응답 반환
        return ResponseEntity.ok().body(recommendation);
    }


    @PostMapping("/api/saveExcelToDatabase")
    public String saveExcelToDatabase(@RequestBody MealRecommendationRequest request) {
        try {
            excelToDatabaseService.readFromExcelAndSave("C:/my-files", request);
            return "데이터 저장 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "데이터 저장 실패";
        }
    }
    @GetMapping("/result")
    public String showRecommendedRestaurants(Model model) {
        // 사용자의 위치 정보를 얻어온다
        Coordinates currentLocation = getCurrentUserLocation();

        // 데이터베이스에서 100m 이내의 식당 정보를 가져온다.
        List<RestaurantEntity> recommendedRestaurants = excelToDatabaseService.getNearbyRestaurants(currentLocation);

        // recommendedRestaurants를 result.html 페이지에 전달한다.
        model.addAttribute("recommendedRestaurants", recommendedRestaurants);

        return "result";
    }
    private Coordinates getCurrentUserLocation() {
        // 예시: 임의의 위치 정보 생성
        Double latitude = 37.5811955;
        Double longitude = 127.0165415;

        Coordinates currentLocation = new Coordinates();
        currentLocation.setLatitude(String.valueOf(latitude));
        currentLocation.setLongitude(String.valueOf(longitude));

        return currentLocation;
    }
}
