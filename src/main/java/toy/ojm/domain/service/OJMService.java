package toy.ojm.domain.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import toy.ojm.domain.dto.CategoryRequestDto;
import toy.ojm.domain.dto.RestaurantResponseDto;
import toy.ojm.domain.entity.FoodCategory;
import toy.ojm.domain.entity.FoodCategoryName;
import toy.ojm.domain.entity.Restaurant;
import toy.ojm.domain.repository.CategoryRepository;
import toy.ojm.domain.repository.RestaurantRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class OJMService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public void recommend(
            CategoryRequestDto request,
            HttpSession session) {
        List<String> categoryNames = request.getCategoryList();
        final FoodCategory category = createLastSelectedCategory(categoryNames);
        categoryRepository.save(category);
        session.setAttribute("selectedCategory", category);
    }

    private @NotNull FoodCategory createLastSelectedCategory(List<String> categoryNames) {
        FoodCategory category = new FoodCategory();

        if (categoryNames != null) {
            for (String categoryElement : categoryNames) {
                try {
                    FoodCategoryName categoryName = FoodCategoryName.fromString(categoryElement);
                    category.setCategory(categoryName.name());
                } catch (IllegalArgumentException e) {
                    category.setCategory("기타");
                }
            }
        }
        return category;
    }

    public FoodCategory getLastCategory() {
        return categoryRepository.findAll().stream().findFirst().orElse(null);
    }

    public List<RestaurantResponseDto> getRandomRestaurants(
            double currentLat,
            double currentLon,
            List<String> selectedCategories) {
        List<Restaurant> recommendRestaurants = getRecommendRestaurants(
                currentLat,
                currentLon,
                selectedCategories,
                300
        );

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

    public List<RestaurantResponseDto> getClosestRestaurants(
            double currentLat,
            double currentLon,
            List<String> selectedCategories) {
        List<Restaurant> recommendRestaurants = getRecommendRestaurants(
                currentLat,
                currentLon,
                selectedCategories,
                200
        );

        // 거리 순으로 정렬
        recommendRestaurants.sort((r1, r2) -> {
                    double distance1 = RestaurantDistanceCalculateService.distance(currentLat, currentLon, r1.getLatitude(), r1.getLongitude());
                    double distance2 = RestaurantDistanceCalculateService.distance(currentLat, currentLon, r2.getLatitude(), r2.getLongitude());
                    return Double.compare(distance1, distance2);
                }
        );

        return recommendRestaurants.stream()
                .map(r -> new RestaurantResponseDto(
                        r.getName(),
                        r.getCategory(),
                        r.getAddress(),
                        r.getNumber()
                ))
                .toList();
    }

    public List<Restaurant> getRecommendRestaurants(
            double currentLat,
            double currentLon,
            List<String> categories,
            double maxDistance) {
        List<Restaurant> restaurants = restaurantRepository.findAllByCategoryIn(categories);

        return restaurants.stream()
                .filter(restaurant -> RestaurantDistanceCalculateService.distance(
                        currentLat,
                        currentLon,
                        restaurant.getLatitude(),
                        restaurant.getLongitude()) <= maxDistance)
                .collect(Collectors.toList());
    }

    // api 용
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
}







