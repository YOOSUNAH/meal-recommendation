package toy.ojm.controller.dto;

import lombok.Getter;
import toy.ojm.domain.Coordinates;

import java.util.List;

@Getter
public class MealRecommendationRequest {

    private List<String> categoryList;
    private Coordinates coordinates;
    public String getQuery() {
        // Coordinates 클래스에서 경도와 위도를 가져와서 query를 생성
        if (coordinates != null) {
            return coordinates.getLongitude() + "," + coordinates.getLatitude();
        }
        return null; // 적절한 coordinates가 없을 경우 null을 반환하거나 예외 처리를 수행
    }

    public String getCategory() {
        // categoryList에서 첫 번째 카테고리를 가져와서 반환 (첫 번째 카테고리만 사용한다고 가정)
        if (categoryList != null && !categoryList.isEmpty()) {
            return categoryList.get(0);
        }
        return null; // categoryList가 비어 있을 경우 null을 반환하거나 예외 처리를 수행
    }

}
