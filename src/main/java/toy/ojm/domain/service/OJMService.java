package toy.ojm.domain.service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.dto.RestaurantResponseDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.TransCoordination;
import toy.ojm.domain.repository.CategoryRepository;
import toy.ojm.domain.repository.RestaurantRepository;
import toy.ojm.domain.location.Distance;


@Service
@RequiredArgsConstructor
@Slf4j
public class OJMService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TransCoordination transCoordination;

    @Value("${kakao.rest-api-key}")
    private String kakaoApiKey;

    public void recommend(CategoryRequestDto request, HttpSession session) {
        List<String> categoryList = request.getCategoryList();
        FoodCategory category = new FoodCategory();

        if (categoryList != null) {
            for (String categoryElement : categoryList) {
                switch (categoryElement) {
                    case "한식":
                        category.setKorean(true);
                        break;
                    case "일식":
                        category.setJapanese(true);
                        break;
                    case "중식":
                        category.setChinese(true);
                        break;
                    case "양식":
                        category.setWestern(true);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown category: " + categoryElement);
                }
            }
        }
        categoryRepository.save(category);

        // 사용자 선택을 세션에 저장
        session.setAttribute("selectedCategory", category);
        System.out.println("Saved in session: " + session.getAttribute("selectedCategory"));
    }

    public FoodCategory getLastCategory() {
        return categoryRepository.findAll().stream().findFirst().orElse(null); // 단일 객체 반환
    }

    public List<RestaurantResponseDto> AroundRestaurants(double currentLat,
        double currentLon) { // List<String> selectedCategories
        List<Restaurant> restaurants = restaurantRepository.findAll();
//        Distance distanceCalculator = new Distance();
//        double maxDistance = 100.0;
        List<Restaurant> recommendRestaurants = new ArrayList<>();

        // 세션에서 선택된 카테고리 가져오기
//        FoodCategory selectedCategory = (FoodCategory) session.getAttribute("selectedCategory");
//        if (selectedCategories == null || selectedCategories.isEmpty()) {
//            throw new IllegalArgumentException("선택된 카테고리가 없습니다.");
//        }

        //  100m 이내 인 조건
//        for (Restaurant restaurant : restaurants) {
//            // 거리 구하기
//            double distance = distanceCalculator.distance(currentLat, currentLon,
//                restaurant.getLatitude(), restaurant.getLatitude());
//            if (distance <= maxDistance) {
//                nearbyRestaurants.add(restaurant);
//            }
//            for (Restaurant restaurantIn100 : nearbyRestaurants) {
//                System.out.println("100m 이내의 식당 : " + restaurantIn100.getName());
//            }
//        }

        // 영업 중인 곳만 추천해주기  "영업" "폐업"
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getBusinessStatus().equals("영업")) {
                recommendRestaurants.add(restaurant);
            }
        }
        // 랜덤 10개
        Collections.shuffle(recommendRestaurants);
        List<Restaurant> randomRestaurants = recommendRestaurants.stream()
            .limit(10)
            .collect(Collectors.toList());

        return randomRestaurants.stream()
            .map(r -> new RestaurantResponseDto(r.getName(), r.getCategory(), r.getAddress(),
                r.getNumber()))
            .collect(Collectors.toList());

    }
}







