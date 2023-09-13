package toy.ojm.controller.dto;

import lombok.Getter;
import toy.ojm.domain.ListDto;

import java.util.List;

@Getter
public class MealRecommendationResponse {

    private List<ListDto> list;

    private Integer itemCount;
    public void setList(List<ListDto> list) {
        this.list = list;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    @Getter
    private static class Item {

        private String category;
        private String name;
        private String longitude;
        private String latitude;
        private String signatureMenu;
    }
}
