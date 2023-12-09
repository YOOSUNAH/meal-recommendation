package toy.ojm.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantEntity {

    private Long id;
    private String dtlStateNm; //상세영업상태명 (폐업, 영업)
    private String siteWhLaDdr;  //지번주소
    private String rdNWhLaDdr;   //도로명 주소
    private String bpLcNm;  //사업장명
    private String upTadNm;  //업태구분명
    private String x; //좌표정보(X) 경도
    private String y; //좌표정보(Y) 위도
}
