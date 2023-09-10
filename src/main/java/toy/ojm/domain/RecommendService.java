package toy.ojm.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final NaverAPI naverAPI;


    public MealRecommendationResponse recommend(
        MealRecommendationRequest request
    ) {

        /**
         List<Restaurant> restaurantList = naverAPI.search(request.getCoordinates());

         // TODO: 규칙 수행
         // 1. 사용자가 요청한 카테고리만 필터링할 것
         // 2. 음식점 랜덤으로 10개 추출
         for (Restaurant restaurant : restaurantList) {
         }
         */

        return null;
    }
}
