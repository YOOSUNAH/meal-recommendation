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

    public MealRecommendationResponse recommend(MealRecommendationRequest request) {
        SearchLocalRes searchResult = client.search(request.getQuery());

        if (searchResult != null && searchResult.getItems() != null) {
            List<MealRecommendationResponse.Item> filteredItems = filterByCategoryAndConvertResponseDto(searchResult, request.getCategoryList());

            return MealRecommendationResponse.of(randomize(filteredItems));
        } else {
            System.out.println("SearchLocalRes 객체나 items가 null입니다.");
            // 예외 처리 또는 기본값 반환 등으로 대체 필요
            return MealRecommendationResponse.empty(); // 예시로 empty 메소드를 호출하여 빈 응답 반환
        }

    }

    // 사용자가 선택한 카테고리 필터링
    private List<MealRecommendationResponse.Item> filterByCategoryAndConvertResponseDto(SearchLocalRes searchResult, List<String> categories
    ) {
        return searchResult.getItems()
            .stream()
            .filter(item -> categories.contains(item.getCategory()))
            .map(item -> MealRecommendationResponse.Item.builder()
                .DTLSTATENM(item.getDTLSTATENM())
                .SITEWHLADDR(item.getSITEWHLADDR())
                .RDNWHLADDR(item.getRDNWHLADDR())
                .BPLCNM(item.getBPLCNM())
                .X(item.getX())
                .Y(item.getY())
                .build())
            .collect(Collectors.toList());
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