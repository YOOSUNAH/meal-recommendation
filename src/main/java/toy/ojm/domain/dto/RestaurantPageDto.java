package toy.ojm.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class RestaurantPageDto {

    private String managementNumber; //관리 번호
    private String businessStatus; //상세영업상태명
    private String number; //전화번호
    private String address;  //지번주소
    private String roadAddress;   //도로명 주소
    private String name;  //사업장명
    private String category;  //업태구분명
    private Double longitude; //좌표정보(X) 경도
    private Double latitude; //좌표정보(Y) 위도


    public RestaurantPageDto(
            String managementNumber,
            String businessStatus,
            String number,
            String address,
            String roadAddress,
            String name,
            String category,
            Double longitude,
            Double latitude
    ) {
        this.managementNumber = managementNumber;
        this.businessStatus = businessStatus;
        this.number = number;
        this.address = address;
        this.roadAddress = roadAddress;
        this.name = name;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
