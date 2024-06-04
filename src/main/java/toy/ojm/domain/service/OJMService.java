package toy.ojm.domain.service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.dto.RestaurantResponseDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.Distance;
import toy.ojm.domain.repository.CategoryRepository;
import toy.ojm.domain.repository.RestaurantRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class OJMService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

//    @Value("${kakao.rest-api-key}")
//    private String kakaoApiKey;

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
                    case "기타":
                        category.setEtc(true);
                        break;
                    default:
                        category.setEtc(true);
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

    public List<RestaurantResponseDto> AroundRestaurants(
        double currentLat,
        double currentLon,
        List<String> selectedCategories) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<Restaurant> recommendRestaurants = new ArrayList<>();
        Distance distanceCalculator = new Distance();
        double maxDistance = 100;

        // 조건 : 영업 중인 곳만 추천
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getBusinessStatus().equals("영업")) {

                //  조건 : 100m 이내인 곳만 추천
                double distance = distanceCalculator.distance(currentLat, currentLon,
                    restaurant.getLatitude(), restaurant.getLongitude());
                // 조건 : 해당 카테고리만 추천
                if (distance <= maxDistance && categoryRestaurant(selectedCategories, restaurant)) {
                    recommendRestaurants.add(restaurant);
                    log.info("100m 이내의 식당 : " + restaurant.getName());
                }
            }
        }

        // 랜덤 10개
        Collections.shuffle(recommendRestaurants);
        List<Restaurant> randomRestaurants = recommendRestaurants.stream()
            .limit(10)
            .toList();

        return randomRestaurants.stream()
            .map(r -> new RestaurantResponseDto(r.getName(), r.getCategory(), r.getAddress(),
                r.getNumber()))
            .collect(Collectors.toList());
    }

    // 카테고리 확인
    private boolean categoryRestaurant(List<String> selectedCategories, Restaurant restaurant) {
        for (String category : selectedCategories) {
            if (restaurant.getCategory().equals(category)) {
                return true;
            }
        }
        return false;
    }
}







