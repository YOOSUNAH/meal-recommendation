package toy.ojm.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MealRecommendationResponse {

    private List<Item> list;

    private Integer itemCount;

    public static MealRecommendationResponse of(List<Item> list) {
        MealRecommendationResponse response = new MealRecommendationResponse();
        response.list = list;
        response.itemCount = list.size();
        return response;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    @Getter
    @Builder
    public static class Item {
        private String title;
        private String link;
        private String category;
        private String description;
        private String telephone;
        private String address;
        private String roadAddress;
        private String mapx;
        private String mapy;

//        private String signatureMenu;
    }
}
