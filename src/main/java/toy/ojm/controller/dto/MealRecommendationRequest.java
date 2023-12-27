package toy.ojm.controller.dto;

import lombok.Getter;
import toy.ojm.domain.Coordinates;

import java.util.List;

@Getter
public class MealRecommendationRequest {

    private List<String> categoryList;
    private Coordinates coordinates;

}
