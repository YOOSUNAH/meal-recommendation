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
import toy.ojm.domain.repository.RestaurantQueryRepository;
import toy.ojm.domain.repository.RestaurantRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class OJMService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantQueryRepository restaurantQueryRepository;

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
    }

    public FoodCategory getLastCategory() {
        return categoryRepository.findAll().stream().findFirst().orElse(null); // 단일 객체 반환
    }

    public List<RestaurantResponseDto> getRandomRestaurants(
        double currentLat,
        double currentLon,
        List<String> selectedCategories
    ) {
        List<Restaurant> recommendRestaurants = getRecommendRestaurants(currentLat, currentLon, selectedCategories, 200);

        // 랜덤 10개
        Collections.shuffle(recommendRestaurants);
        List<Restaurant> randomRestaurants = recommendRestaurants.stream()
            .limit(10)
            .toList();

        return randomRestaurants.stream()
            .map(r -> new RestaurantResponseDto(
                r.getName(),
                r.getCategory(),
                r.getAddress(),
                r.getNumber()))
            .toList();
    }

    // Todo 실제 거리와 도보상 거리가 차이가 나서 , 결과가 상이함.
    public List<RestaurantResponseDto> getClosestRestaurants(
        double currentLat,
        double currentLon,
        List<String> selectedCategories
    ) {

        Distance distanceCalculator = new Distance();

        List<Restaurant> recommendRestaurants = getRecommendRestaurants(currentLat, currentLon, selectedCategories, 200);

        // 거리 순으로 정렬
        recommendRestaurants.sort((r1, r2) ->
            {
                double distance1 = distanceCalculator.distance(currentLat, currentLon, r1.getLatitude(), r1.getLongitude());
                double distance2 = distanceCalculator.distance(currentLat, currentLon, r2.getLatitude(), r2.getLongitude());
                return Double.compare(distance1, distance2);
            }
            );

        return recommendRestaurants.stream()
            .map(r -> new RestaurantResponseDto(r.getName(), r.getCategory(), r.getAddress(), r.getNumber()))
            .toList();
    }


    public List<Restaurant> getRecommendRestaurants(
        double currentLat,
        double currentLon,
        List<String> selectedCategories,
        double maxDistance
    ) {
        List<Restaurant> recommendRestaurants = new ArrayList<>();
        Distance distanceCalculator = new Distance();

        for (String category : selectedCategories) {
            // 조건 : 해당 카테고리만 추천
            List<Restaurant> restaurantsWithCategory = restaurantQueryRepository.findAllByCategory(category);

            for (Restaurant restaurant :restaurantsWithCategory ){
                //  조건 : maxDistance 이내인 곳만 추천
                double distance = distanceCalculator.distance(currentLat, currentLon,
                    restaurant.getLatitude(), restaurant.getLongitude());
                if(distance <= maxDistance){
                    recommendRestaurants.add(restaurant);
                    log.info("지정 거리 이내의 식당 : " + restaurant.getName());
                }
            }
        }
        return recommendRestaurants;
    }
}







