package toy.ojm.controller.dto;

import lombok.Getter;
import toy.ojm.domain.Coordinates;

import java.util.List;

@Getter
public class MealRecommendationRequest {
    private List<String> categoryList;  // categoryList는 String 객체들의 리스트입니다. 음식 카테고리 목록을 담는다.
    private Coordinates coordinates;  // coordinates는 Coordinates 객체입니다. 좌표 정보를 담는다.
}
