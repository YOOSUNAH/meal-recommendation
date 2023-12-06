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

}
