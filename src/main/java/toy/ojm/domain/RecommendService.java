package toy.ojm.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import toy.ojm.client.Client;
import toy.ojm.client.dto.SearchLocalRes;
import toy.ojm.controller.dto.MealRecommendationRequest;
import toy.ojm.controller.dto.MealRecommendationResponse;

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
    public static final int LIST_LIMIT_COUNT = 10;
    private final toy.ojm.client.Client client;

    public MealRecommendationResponse recommend(
        MealRecommendationRequest request
    ) {
        SearchLocalRes searchResult = client.search(request.getQuery());

        return MealRecommendationResponse.of(
            randomize(
                filterCategoryAndConvertResponseDto(
                    searchResult,
                    request.getCategoryList()
                )
            )
        );
    }

    // 사용자가 선택한 카테고리 필터링
    private List<MealRecommendationResponse.Item> filterCategoryAndConvertResponseDto(
        SearchLocalRes searchResult,
        List<String> categories
    ) {
        List<MealRecommendationResponse.Item> filteredItems = new ArrayList<>();

        if (searchResult != null && searchResult.getItems() != null) {
            filteredItems = searchResult.getItems()
                .stream()
                .filter(item -> categories.contains(item.getCategory()))
                .map(item -> MealRecommendationResponse.Item.builder()
                    .title(item.getTitle())
                    .category(item.getCategory())
                    .address(item.getAddress())
                    .roadAddress(item.getRoadAddress())
                    .build())
                .collect(Collectors.toList());
        } else {
            System.out.println("SearchLocalRes 객체나 items가 null입니다.");
        }

        return filteredItems;
    }

    // 랜덤으로 10개 음식점 선택
    private List<MealRecommendationResponse.Item> randomize(
        List<MealRecommendationResponse.Item> ListDtos
    ) {
        List<MealRecommendationResponse.Item> randomListDtos = new ArrayList<>();
        Random random = new Random();

        while (randomListDtos.size() < LIST_LIMIT_COUNT && !ListDtos.isEmpty()) {
            int randomIndex = random.nextInt(ListDtos.size());
            randomListDtos.add(ListDtos.remove(randomIndex));
        }
        return randomListDtos;
    }

}