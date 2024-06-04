package toy.ojm.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantResponseDto {
    private String name;  //사업장명
    private String category;  //업태구분명
    private String address;  //지번주소
    private String number; // 전화번호

    public RestaurantResponseDto(String name, String category, String address, String number){
        this.name = name;
        this.category = category;
        this.address = address;
        this.number = number;
    }
}
