package toy.ojm.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantEntity {

    private String businessStatus; //상세영업상태명 (폐업, 영업)
   private String streetNumberAddress;  //지번주소
    private String restaurantName;  //사업장명
    private String Category;  //업태구분명
    private Double longitude; //좌표정보(X) 경도
    private Double latitude; //좌표정보(Y) 위도
}
