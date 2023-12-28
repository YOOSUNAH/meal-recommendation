package toy.ojm.domain;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.entity.DatabaseRestaurant;
import toy.ojm.excel.ExcelToDatabaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

@Service   // Spring의 Service 어노테이션
@RequiredArgsConstructor
public class RecommendService {   // 음식점 추천 서비스를 담당하는 RecommendService 클래스
    public static final int LIST_LIMIT_COUNT = 10;  // 상수 LIST_LIMIT_COUNT 선언 및 초기화
    private final ListRepository listRepository; // ListRepository 의존성 주입.  ListRepository 클래스의 listRepository라는 인스턴스.
    // RecommendService 클래스의 멤버 변수로 선언되어 의존성 주입을 통해 해당 클래스를 사용할 수 있게 된다.
    private final ExcelToDatabaseService excelToDatabaseService; // ExcelToDatabaseService 의존성 주입
    private static final Logger log = LoggerFactory.getLogger(RecommendService.class); // SLF4J Logger

    // MealRecommendationRequest의 객체 request를 전달 받아와서 음식추천을 수행하는 recommend 메서드
    // MealRecommendationRequest를 받아 음식점 추천에 대한 응답인 MealRecommendationResponse를 생성한다.
    public MealRecommendationResponse recommend(MealRecommendationRequest request) {
        MealRecommendationResponse recommendationResponse = new MealRecommendationResponse();

        // 사용자 요청에 따른 카테고리로 음식점 필터링 및 추천을 위해 filterByCategory 메서드를 호출한다.
        // filterByCategory 메서드를 호출하여 추천된 음식점 목록을 받아온다.
        List<MealRecommendationResponse.Item> recommendedItems = filterByCategory(request.getCoordinates(), request.getCategoryList());

        if (recommendedItems.isEmpty()) {
            // 추천된 음식점 목록이 비어있는 경우에 대한 처리
            log.warn("추천된 음식점 목록이 비어 있습니다."); // 추천된 음식점 목록이 비어있을 때, 로그에 경고를 남긴다.
            // 기본 값으로 설정한 음식점 목록을 만들어 response에 설정한다.
            MealRecommendationResponse.Item noRestaurantItem = new MealRecommendationResponse.Item();
            noRestaurantItem.setCategory("N/A");
            noRestaurantItem.setStreetNumberAddress("N/A");
            noRestaurantItem.setRestaurantName("N/A");
            recommendationResponse.setRecommendedRestaurants(List.of(noRestaurantItem));
        } else {
            // 추천된 음식점 목록이 비어있지 않을 때, 추천된 목록을 response에 설정한다.
            recommendationResponse.setRecommendedRestaurants(recommendedItems);
        }
        // 완성된 response를 반환한다.
        return recommendationResponse;
    }

    // 랜덤으로 10개 음식점 선택
    // randomize 메서드. 입력받은 음식점 목록을 섞어서 최대 LIST_LIMIT_COUNT 만큼의 음식점을 선택하여 반환하는 메서드.
    public List<DatabaseRestaurant> randomize(List<DatabaseRestaurant> restaurantEntities) {
        List<DatabaseRestaurant> randomList = new ArrayList<>(restaurantEntities); //randomList라는 새로운 리스트를 생성. 이 리스트는 restaurantEntities를 복제하여 새로운 객체로 만든다.
        Collections.shuffle(randomList);   // Collections 클래스의 shuffle 메서드를 사용하여 randomList의 요소들을 무작위로 섞는다.
        return randomList.subList(0, Math.min(LIST_LIMIT_COUNT, randomList.size()));  // 최대 LIST_LIMIT_COUNT와 섞인 음식점 목록의 크기 중 작은 값을 구하여 해당 개수만큼의 음식점을 선택하여 반환한다..
    }

    // filterByCategory 메서드. 사용자가 요청한 카테고리에 따라 음식점을 필터링하여 추천하는 메서드.
    // 입력된 좌표와 요청된 카테고리 목록에 따라 음식점을 필터링하여 추천하는 역할을 한다.
    public List<MealRecommendationResponse.Item> filterByCategory(Coordinates coordinates, List<String> requestedCategoryList) { //Coordinates coordinates와 List<String> requestedCategoryList를 매개변수로 받는 filterByCategory 메서드
        List<DatabaseRestaurant> nearbyRestaurants = excelToDatabaseService.getNearbyRestaurants(coordinates); //excelToDatabaseService.getNearbyRestaurants(coordinates)를 통해 coordinates를 활용하여 주변 음식점을 얻어온다.
        List<DatabaseRestaurant> filteredRestaurants;

        filteredRestaurants = nearbyRestaurants.stream() // nearbyRestaurants 리스트를 스트림으로 변환한 뒤,
            .filter(restaurant -> {  // restaurant는 각각의 RestaurantEntity 객체를 가리킨다.
                String category = restaurant.getCategory(); // 해당 음식점의 카테고리 정보를 가져옵니다.
                return category != null && requestedCategoryList.contains(category); // 해당 음식점의 카테고리가 null이 아니고, requestedCategoryList에 포함되어 있는지 확인한다.
            })
            .collect(Collectors.toList());  // 스트림에서 필터링된 요소들을 다시 리스트로 변환한다. 'Collectors.toList()'는 스트림 요소를 리스트로 모으는 컬렉터다.
        return convertToItems(filteredRestaurants); // 필터링된 RestaurantEntity 목록을 convertToItems메서드를 사용해서 MealRecommendationResponse.Item 목록으로 변환하여 반환한다.
    }


 // convertToItems 메서드. 음식점 엔터티를 MealRecommendationResponse.Item으로 변환하여 리스트로 반환하는 메서드.
    // restaurantEntities에 있는 각 RestaurantEntity를 MealRecommendationResponse.Item으로 변환하여 리스트로 모아주는 역할을 한다.
    private List<MealRecommendationResponse.Item> convertToItems(List<DatabaseRestaurant> restaurantEntities) { // List<RestaurantEntity> 형태로 주어진 음식점 엔터티 목록인 restaurantEntities를 전달값으로 받는 convertToItems 메서드
        return restaurantEntities.stream() // 목록을 스트림으로 변환한다.
            .map(this::convertToItem)  // map은 각 요소를 변환하여 새로운 스트림을 생성한다. RestaurantEntity객체를 convertToItem메서드를 통해 MealRecommendationResponse.Item으로 변환한다.
            .collect(Collectors.toList()); // 변환된 MealRecommendationResponse.Item들을 리스트로 모아 반환한다.
    }
    // convertToItem 메서드. 하나의 음식점 엔터티를 MealRecommendationResponse.Item으로 변환하여 반환하는 메서드.
    private MealRecommendationResponse.Item convertToItem(DatabaseRestaurant restaurant) {
        MealRecommendationResponse.Item item = new MealRecommendationResponse.Item();
        item.setCategory(restaurant.getCategory());
        return item;
    }
}



