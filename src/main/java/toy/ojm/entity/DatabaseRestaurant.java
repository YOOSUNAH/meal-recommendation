package toy.ojm.entity;

import lombok.Getter;
import lombok.Setter;

@Getter  // Getter 및 Setter 메서드를 자동으로 생성하도록 지정
@Setter
public class DatabaseRestaurant {  // 데이터를 담기 위해 변수를 선언한 클래스.   데이터베이스에서 읽거나 저장할 때 사용되며, 데이터베이스의 테이블에 해당하는 엔터티

    private String businessStatus; //상세영업상태명 (폐업, 영업)
   private String streetNumberAddress;  //지번주소
    private String restaurantName;  //사업장명
    private String Category;  //업태구분명
    private Double longitude; //좌표정보(X) 경도
    private Double latitude; //좌표정보(Y) 위도
}
