package toy.ojm.excel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelRestaurantData {  //  Excel 파일에서 읽은 데이터를 저장하거나 전송하기 위한 데이터 전송 객체
    private String businessStatus;
    private String streetNumberAddress;
    private String restaurantName;
    private String category;
    private Double longitude;
    private Double latitude;
}