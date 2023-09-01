package toy.ojm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import toy.ojm.domain.Restaurant;
import toy.ojm.domain.RestaurantRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/posts")
public class RestaurantController {
    private List<Restaurant> recommendedRestaurants = new ArrayList<>(); // 추천된 음식점 목록 저장
    private int recommendationCount = 0; // 추천 요청 횟수 저장
    private String previousMenu; // 이전에 선택한 메뉴를 저장하는 변수
    private boolean selectAll; // "모두 선택하기" 옵션을 선택했는지 여부를 저장하는 변수

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/recommendations")
    public ResponseEntity<List<Restaurant>> getRecommendedRestaurants(@RequestBody RestaurantRequest request) {
        if (selectAll) {
            // "모두 선택하기"를 선택한 경우 기존 목록과 함께 새로 추천
            List<Restaurant> newRecommendations = processRequest(request);
            recommendedRestaurants.addAll(newRecommendations);
        } else if (!Objects.equals(request.getMenu(), previousMenu)) {
            // 메뉴가 변경된 경우(이전 메뉴와 다를 경우)에만 새로 추천
            recommendedRestaurants = processRequest(request);
            previousMenu = request.getMenu();
        }

        return ResponseEntity.ok(recommendedRestaurants);
    }



    private List<Restaurant> processRequest(RestaurantRequest request) {
        // request 객체를 기반으로 음식점을 추천하고 그 결과를 List<Restaurant>로 반환하는 로직을 작성
        // 예시: 실제 음식점 데이터베이스에서 검색하거나 API를 호출하여 음식점 목록을 가져옴
        List<Restaurant> recommendedRestaurants = generateFakeRecommendations(request);

        // 로그를 남기는 예시
        log.info("Recommended restaurants: {}", recommendedRestaurants);

        return recommendedRestaurants;
    }

    private List<Restaurant> generateFakeRecommendations(RestaurantRequest request) {
        // 가짜 음식점 데이터를 생성하는 예시
        List<Restaurant> recommendedRestaurants = new ArrayList<>();
        recommendedRestaurants.add(new Restaurant(
                "KOREAN",
                "뱅뱅막국수 베이비",
                "11231232.2131233",
                "11231232.2131233",
                "들기름 막국수 베이비"));
        // 추가적인 음식점 데이터 생성
        return recommendedRestaurants;
    }

    @Controller
    public class NaverApiController {
        private final RestTemplate restTemplate;
        @Autowired
        public NaverApiController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }
        @GetMapping("/search")
        public ResponseEntity<String> search(@RequestParam String query) {
            String apiUrl = "https://openapi.naver.com/v1/search/local.json?query=" + query;

            // 네이버 API 요청을 보내고 응답을 문자열로 받아옴
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            return response;
        }
    }

}
