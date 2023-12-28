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
import toy.ojm.entity.DatabaseRestaurant;
import toy.ojm.excel.ExcelToDatabaseService;
import toy.ojm.excel.NearbyRestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import toy.ojm.domain.TransCoordination;

import java.util.List;

@Slf4j // 롬복(Lombok)을 사용해서 로그를 찍기위해 logger를 생성
@RestController  // 해당 클래스가 REST API 엔드포인트를 처리하는 컨트롤러
@RequiredArgsConstructor // : 생성자를 통해 초기화되지 않은 final 필드에 대해 생성자를 생성하는 어노테이션
public class RecommendController {

    private final RecommendService recommendService;  // RecommendService클래스의 recommendService라는 인스턴스 생성
    private final ExcelToDatabaseService excelToDatabaseService;   // ExcelToDatabaseService클래스의 excelToDatabaseService라는 인스턴스 생성
    private final NearbyRestaurantService nearbyRestaurantService;
    private final JdbcTemplate jdbcTemplate;
    private final TransCoordination transCoordination;

    @PostMapping("/api/recommend") // HTTP POST 요청 "/api/recommend"을 처리하는 메서드. "/api/recommend"경로로 들어오면 아래 메서드를 호출
    public ResponseEntity<MealRecommendationResponse> getRecommendation(   // ResponseEntity : HTTP 응답을 나타내는 객체
        @RequestBody MealRecommendationRequest request  // @RequestBody : 요청의 본문을 MealRecommendationRequest 객체로 매핑한다.
    ) {
        // 로그 추가: 요청이 들어왔음을 확인
        log.info("API '/api/recommend'로 POST 요청이 들어왔습니다.");

        // 추천된 음식점 목록을 서비스에서 가져옴
        MealRecommendationResponse recommendation = recommendService.recommend(request);  // 요청에 따른 음식점 추천 서비스를 호출
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
    @GetMapping("/result")  // HTTP GET 요청 "/result"를 처리하는 메서드
    public String showRecommendedRestaurants(Model model) {  // Model model: 뷰에 데이터를 전달하기 위한 Spring의 모델 객체
        // 사용자의 위치 정보를 얻어온다
        Coordinates currentLocation = getCurrentUserLocation();

        // NearbyRestaurantService 클래스의 인스턴스 생성
        NearbyRestaurantService nearbyRestaurantService = new NearbyRestaurantService(jdbcTemplate, transCoordination);

        // 데이터베이스에서 100m 이내의 식당 정보를 가져온다.
        List<DatabaseRestaurant> recommendedRestaurants = nearbyRestaurantService.getNearbyRestaurants(currentLocation);

        // recommendedRestaurants를 result.html 페이지에 전달한다.
        model.addAttribute("recommendedRestaurants", recommendedRestaurants);  // 뷰로 식당 정보를 전달합니다.
        return "result";
    }
    private Coordinates getCurrentUserLocation() {
        // 예시: 임의의 위치 정보 생성
        Double latitude = 37.5811955;
        Double longitude = 127.0165415;

        Coordinates currentLocation = new Coordinates();
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);

        return currentLocation;
    }
}
