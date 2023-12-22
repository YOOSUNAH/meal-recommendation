package toy.ojm.controller.dto;

import lombok.AllArgsConstructor;
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

    public void setRecommendedRestaurants(List<Item> recommendedItems) {
        this.list = recommendedItems;
        this.itemCount = recommendedItems.size();
    }

    @Getter
    @AllArgsConstructor
    public static class Item {
        private String businessStatus;        //상세영업상태명 (폐업, 영업)
        private String streetNumberAddress;  //지번주소
//        private String streetNameAddress;   //도로명 주소
        private String restaurantName;     //사업장명
        private String category;          //업태구분명
        private Double longitude;        //좌표정보(X) 경도
        private Double latitude;        //좌표정보(Y) 위도

        // 기본 생성자 추가
        public Item() {
            this.businessStatus = null;
            this.streetNumberAddress = null;
//            this.streetNameAddress = null;
            this.restaurantName = null;
            this.category = null;
            this.longitude = null;
            this.latitude = null;
        }

        // category 설정 메서드 추가
        public void setCategory(String category) {
            this.category = category;
        }

        public void setStreetNumberAddress(String streetNumberAddress) {
            this.streetNumberAddress = streetNumberAddress;
        }

//        public void setStreetNameAddress(String streetNameAddress) {
//            this.streetNameAddress = streetNameAddress;
//        }

        public void setRestaurantName(String s) {
            this.restaurantName = restaurantName;
        }

        public void setNoRestaurantMessage(String noRestaurantMessage) {
            this.restaurantName = noRestaurantMessage;
        }
    }
}