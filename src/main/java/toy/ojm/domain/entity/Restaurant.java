package toy.ojm.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessStatus; //상세영업상태명

    private String number; // 전화번호

    private String address;  //지번주소

    private String roadAddress;   //도로명 주소

    private String name;  //사업장명

    private String category;  //업태구분명

    private Double longitude; //좌표정보(X) 경도

    private Double latitude; //좌표정보(Y) 위도


}
