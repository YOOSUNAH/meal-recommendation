package toy.ojm.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MealRecommendationResponse {

    private List<Item> list;

    private Integer itemCount;

    @Getter
    private static class Item {

        private String category;
        private String name;
        private String longitude;
        private String latitude;
        private String signatureMenu;

    }


}
