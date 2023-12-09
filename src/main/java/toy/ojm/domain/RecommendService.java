package toy.ojm.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public MealRecommendationResponse recommend(MealRecommendationRequest request) {

        return null;
    }

    // 랜덤으로 10개 음식점 선택
    private List<MealRecommendationResponse.Item> randomize(List<MealRecommendationResponse.Item> ListDtos) {
        List<MealRecommendationResponse.Item> randomListDtos = new ArrayList<>();
        Random random = new Random();

        while (randomListDtos.size() < LIST_LIMIT_COUNT && !ListDtos.isEmpty()) {
            int randomIndex = random.nextInt(ListDtos.size());
            randomListDtos.add(ListDtos.remove(randomIndex));
        }
        return randomListDtos;
    }
}