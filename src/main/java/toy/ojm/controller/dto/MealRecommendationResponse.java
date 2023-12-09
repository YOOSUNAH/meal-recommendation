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

    public static MealRecommendationResponse empty() {
        return null;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    @Getter
    @Builder
    public static class Item {
        private String dtlStateNm; //상세영업상태명 (폐업, 영업)
        private String siteWhLaDdr;  //지번주소
        private String rdNWhLaDdr;   //도로명 주소
        private String bpLcNm;  //사업장명
        private String upTadNm;  //업태구분명
        private String x; //좌표정보(X) 경도
        private String y; //좌표정보(Y) 위도

    }
}
