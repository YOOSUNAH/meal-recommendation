package toy.ojm.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.excel.ExcelToDatabaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * // TODO: 규칙 수행
 * // 1. 사용자가 요청한 카테고리만 필터링할 것
 * List<ListDto> ListDtoList = naverAPI.search(request.getCoordinates());
 * <p>
 * // 2. 음식점 랜덤으로 10개 추출
 * for (categoryList categoryList : categoryListList) {
 * }
 */
@Service
@RequiredArgsConstructor
public class RecommendService {
    public static final int LIST_LIMIT_COUNT = 10;
    private final ListRepository listRepository;
    private final ExcelToDatabaseService excelToDatabaseService;

    public MealRecommendationResponse recommend(MealRecommendationRequest request) {
        // 로직 구현

        return null;
    }

    // 랜덤으로 10개 음식점 선택
    public List<RestaurantEntity> randomize(List<RestaurantEntity> restaurantEntities) {
        List<RestaurantEntity> randomList = new ArrayList<>(restaurantEntities);
        Collections Collections = null;
        Collections.shuffle(randomList);
        return randomList.subList(0, Math.min(LIST_LIMIT_COUNT, randomList.size()));
    }

    public List<MealRecommendationResponse.Item> filterByCategory(Coordinates coordinates, String requestedCategory) {
        List<RestaurantEntity> nearbyRestaurants = excelToDatabaseService.getNearbyRestaurants(coordinates);
        String categoryToMatch = listRepository.getCategory(requestedCategory);
        List<RestaurantEntity> filteredRestaurants = nearbyRestaurants.stream()
            .filter(restaurant -> {
                String category = restaurant.getCategory();
                return category != null && category.equalsIgnoreCase(categoryToMatch);
            })
            .collect(Collectors.toList());

        return convertToItems(randomize(filteredRestaurants));
    }

    private List<MealRecommendationResponse.Item> convertToItems(List<RestaurantEntity> restaurantEntities) {
        return restaurantEntities.stream()
            .map(this::convertToItem)
            .collect(Collectors.toList());
    }

    private MealRecommendationResponse.Item convertToItem(RestaurantEntity restaurant) {
        MealRecommendationResponse.Item item = new MealRecommendationResponse.Item();
        item.setCategory(restaurant.getCategory());
        // 필요한 정보들을 item에 설정합니다.
        return item;
    }
}



