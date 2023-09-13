package toy.ojm.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;
import toy.ojm.controller.dto.NaverClient;

import java.io.IOException;
import java.util.ArrayList;
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
    private final NaverClient naverClient;

    public MealRecommendationResponse recommend(MealRecommendationRequest request) throws IOException {
        String query = request.getQuery();
        String selectedCategory = request.getCategory();

        // Naver API를 통해 음식점 목록을 가져옴
        List<ListDto> ListDtoList = naverClient.search(query);
        // 사용자가 선택한 카테고리 필터링
        List<ListDto> filteredListDtos = ListDtoList.stream()
                .filter(ListDto -> ListDto.getCategory().equalsIgnoreCase(selectedCategory))
                .collect(Collectors.toList());

        // 랜덤으로 10개 음식점 선택
        List<ListDto> randomListDtos = getRandomListDtos(filteredListDtos, 10);

        MealRecommendationResponse response = new MealRecommendationResponse();
        response.setList(randomListDtos);
        response.setItemCount(randomListDtos.size());
        return response;
    }
    private List<ListDto> getRandomListDtos(List<ListDto> ListDtos, int count){
        List<ListDto> randomListDtos = new ArrayList<>();
        Random random = new Random();

        while(randomListDtos.size() < count && !ListDtos.isEmpty()){
            int randomIndex = random.nextInt(ListDtos.size());
            randomListDtos.add(ListDtos.remove(randomIndex));
        }
        return randomListDtos;
    }

    public MealRecommendationResponse getRecommendation() {
        return null;
    }
}









