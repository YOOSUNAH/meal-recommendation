package toy.ojm.domain;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.entity.RestaurantEntity;
import toy.ojm.excel.ExcelToDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(RecommendService.class);

    public MealRecommendationResponse recommend(MealRecommendationRequest request) {
        MealRecommendationResponse recommendationResponse = new MealRecommendationResponse();

        // 사용자 요청에 따른 카테고리로 음식점 필터링 및 추천
        List<MealRecommendationResponse.Item> recommendedItems = filterByCategory(request.getCoordinates(), request.getCategoryList());

        if (recommendedItems.isEmpty()) {
            // 추천된 음식점 목록이 비어있는 경우에 대한 처리
            log.warn("추천된 음식점 목록이 비어 있습니다.");
            MealRecommendationResponse.Item noRestaurantItem = new MealRecommendationResponse.Item();
            noRestaurantItem.setCategory("N/A");
            noRestaurantItem.setStreetNumberAddress("N/A");
            noRestaurantItem.setRestaurantName("N/A");
            noRestaurantItem.setNoRestaurantMessage("근처에 해당되는 음식점이 존재하지 않습니다.");
            recommendationResponse.setRecommendedRestaurants(List.of(noRestaurantItem));
        } else {
            // 추천된 음식점 목록 설정
            recommendationResponse.setRecommendedRestaurants(recommendedItems);
        }
        return recommendationResponse;
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
        List<RestaurantEntity> filteredRestaurants;

        if (categoryToMatch.equalsIgnoreCase("전체")) {
            // 전체를 선택한 경우 모든 음식점 중에서 랜덤하게 10곳을 선택
            filteredRestaurants = randomize(nearbyRestaurants);
        } else {
            filteredRestaurants = nearbyRestaurants.stream()
                .filter(restaurant -> {
                    String category = restaurant.getCategory();
                    return category != null && category.equalsIgnoreCase(categoryToMatch);
                })
                .collect(Collectors.toList());
        }

        return convertToItems(filteredRestaurants);
    }


    private List<MealRecommendationResponse.Item> convertToItems(List<RestaurantEntity> restaurantEntities) {
        return restaurantEntities.stream()
            .map(this::convertToItem)
            .collect(Collectors.toList());
    }

    private MealRecommendationResponse.Item convertToItem(RestaurantEntity restaurant) {
        MealRecommendationResponse.Item item = new MealRecommendationResponse.Item();
        item.setCategory(restaurant.getCategory());
        return item;
    }
}



