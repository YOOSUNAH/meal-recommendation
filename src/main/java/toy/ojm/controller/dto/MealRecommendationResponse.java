package toy.ojm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
public class MealRecommendationResponse {

    private List<Item> list; //Item 객체들의 리스트를 담는 변수
    private Integer itemCount; // list의 아이템 개수를 나타내는 변수

    public static MealRecommendationResponse of(List<Item> list) {  // List<Item>을 받는 정적(static) 메서드
        MealRecommendationResponse response = new MealRecommendationResponse(); // MealRecommendationResponse 객체를 생성
        response.list = list; // 받은 list를 해당 객체(resonse)의 list 변수에 저장
        response.itemCount = list.size(); // list 크기를 itemCount에 저장
        return response; // 객체를 반환
    }

    public void setRecommendedRestaurants(List<Item> recommendedItems) { // setRecommendedRestaurants 메서드. 추천된 음식점들을 설정하는 메서드
        this.list = recommendedItems; // 받은 recommendedItems 리스트를 list에 저장
        this.itemCount = recommendedItems.size(); // recommendedItems 크기를 itemCount 에 저장
    }

    @Getter               // Getter 어노테이션으로 해당 클래스의 Getter 메서드를 자동으로 생성
    @AllArgsConstructor  // 모든 필드에 대한 생성자를 만든다.
    public static class Item {   // Item 이라는 내부 클래스 선언
        // Item 클래스의 필드로 아래와같은 변수들이 선언됨.
        private String businessStatus;        //상세영업상태명 (폐업, 영업)
        private String streetNumberAddress;  //지번주소
        private String restaurantName;     //사업장명
        private String category;          //업태구분명
        private Double longitude;        //좌표정보(X) 경도
        private Double latitude;        //좌표정보(Y) 위도

        // Item 클래스의 기본 생성자 정의, 모든 필드를 null로 초기화
        public Item() {
            this.businessStatus = null;
            this.streetNumberAddress = null;
            this.restaurantName = null;
            this.category = null;
            this.longitude = null;
            this.latitude = null;
        }

        // 해당 필드들을 설정하는 메서드
        public void setCategory(String category) { // Item class의 category 필드를 설정하는 메서드
            this.category = category;  // 매개변수로 전달된 값을 해당 필드에 대입한다.
        }
        public void setStreetNumberAddress(String streetNumberAddress) { //Item 클래스의 streetNumberAddress 필드를 설정하는 메서드
            this.streetNumberAddress = streetNumberAddress; // 매개변수로 전달된 값을 해당 필드에 대입한다.
        }

        public void setRestaurantName(String restaurantName) { //Item 클래스의 restaurantName 필드를 설정하는 메서드
            this.restaurantName = restaurantName;  // 매개변수로 전달된 값을 해당 필드에 대입한다.
        }
    }
}