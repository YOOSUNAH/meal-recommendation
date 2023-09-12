package toy.ojm.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.controller.dto.NaverClient;
import toy.ojm.controller.dto.SearchLocalReq;
import toy.ojm.controller.dto.SearchLocalRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
/**
 // TODO: 규칙 수행
 // 1. 사용자가 요청한 카테고리만 필터링할 것
 List<Restaurant> restaurantList = naverAPI.search(request.getCoordinates());

 // 2. 음식점 랜덤으로 10개 추출
 for (categoryList categoryList : categoryListList) {
 }
 */
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final NaverClient naverClient;

    public MealRecommendationResponse recommend(MealRecommendationRequest request) {
        String query = request.getQuery();

        SearchLocalReq searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);

        SearchLocalRes searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            SearchLocalRes.Item localItem = searchLocalRes.getItems().stream().findFirst().orElse(null);

            if (localItem != null) {
                String itemQuery = localItem.getTitle().replaceAll("<[^>]*>", "");

                // 추천된 음식점 목록을 가져옴
                List<Restaurant> recommendedRestaurants = generateRecommendedRestaurants(itemQuery);

                // 사용자가 선택한 카테고리 필터링
                String selectedCategory = request.getCategory();
                List<Restaurant> filteredRestaurants = recommendedRestaurants.stream()
                        .filter(restaurant -> restaurant.getCategory().equalsIgnoreCase(selectedCategory))
                        .collect(Collectors.toList());

                // 랜덤으로 10개 음식점 선택
                List<Restaurant> randomRestaurants = getRandomRestaurants(filteredRestaurants, 10);

                MealRecommendationResponse response = new MealRecommendationResponse();
                response.setRecommendedRestaurants(randomRestaurants);
                return response;
            }
        }

        return new MealRecommendationResponse();
    }

    private List<Restaurant> generateRecommendedRestaurants(String query) {
        // 실제 데이터를 가져오는 로직을 여기에 구현
        // 아래는 가상의 더미 데이터를 생성하는 예시
        List<Restaurant> dummyData = new ArrayList<>();
        dummyData.add(new Restaurant("한식음식점1", "한식"));
        dummyData.add(new Restaurant("중식음식점1", "중식"));
        dummyData.add(new Restaurant("양식음식점1", "양식"));
        dummyData.add(new Restaurant("일식음식점1", "일식"));
        // ... (더미 데이터 계속 추가)
        //더미 데이터 부분은 실제 데이터와 연동하여 적절하게 수정해야 한다.
        return dummyData;
    }

    private List<Restaurant> getRandomRestaurants(List<Restaurant> restaurants, int count) {
        List<Restaurant> randomRestaurants = new ArrayList<>();
        Random random = new Random();

        while (randomRestaurants.size() < count && !restaurants.isEmpty()) {
            int randomIndex = random.nextInt(restaurants.size());
            randomRestaurants.add(restaurants.remove(randomIndex));
        }

        return randomRestaurants;
    }
}





