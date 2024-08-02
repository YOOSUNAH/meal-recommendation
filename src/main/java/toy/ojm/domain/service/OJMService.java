package toy.ojm.domain.service;

import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.dto.RestaurantResponseDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.FoodCategoryName;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.location.Distance;
import toy.ojm.domain.repository.CategoryRepository;
import toy.ojm.domain.repository.RestaurantRepository;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OJMService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public void recommend(CategoryRequestDto request, HttpSession session) {
        List<String> categoryList = request.getCategoryList();
        FoodCategory category = new FoodCategory();

        if(categoryList != null){
            for(String categoryElement : categoryList){
                try {
                    FoodCategoryName categoryName = FoodCategoryName.fromString(categoryElement);
                    category.setCategory(categoryName.name());
                }
               catch (IllegalArgumentException e){
                    category.setCategory("기타");
               }
            }
        }
        categoryRepository.save(category);
        session.setAttribute("selectedCategory", category);
    }

    public FoodCategory getLastCategory() {
        return categoryRepository.findAll().stream().findFirst().orElse(null);
    }

    public List<RestaurantResponseDto> getRandomRestaurants(
        double currentLat,
        double currentLon,
        List<String> selectedCategories
    ) {
        List<Restaurant> recommendRestaurants = getRecommendRestaurants(currentLat, currentLon, selectedCategories, 300);

        Collections.shuffle(recommendRestaurants);
        List<Restaurant> randomRestaurants = recommendRestaurants.stream()
            .limit(10)
            .toList();

        // TODO: 카카오 지도에 정확하게 핀 찍을 수 있도록 주소데이터 fetch(위경도 or 도로명 or 지번 or ...)
//        requestKakakoMapCoordinateForRestaurants()
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
        List<Restaurant> recommendRestaurants = getRecommendRestaurants(currentLat, currentLon, selectedCategories, 200);

        // 거리 순으로 정렬
        recommendRestaurants.sort((r1, r2) ->
            {
                double distance1 = Distance.distance(currentLat, currentLon, r1.getLatitude(), r1.getLongitude());
                double distance2 = Distance.distance(currentLat, currentLon, r2.getLatitude(), r2.getLongitude());
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
        List<String> categories,
        double maxDistance
    ) {
        List<Restaurant> restaurants = restaurantRepository.findAllByCategoryIn(
            categories);

        return restaurants.stream()
            .filter(restaurant -> Distance.distance(
                currentLat,
                currentLon,
                restaurant.getLatitude(),
                restaurant.getLongitude()) <= maxDistance)
            .collect(Collectors.toList());
    }
}







