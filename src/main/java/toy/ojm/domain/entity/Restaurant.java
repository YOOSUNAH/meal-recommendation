package toy.ojm.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String managementNumber; //관리 번호

    private String businessStatus; //상세영업상태명

    private String number; //전화번호

    private String address;  //지번주소

    private String roadAddress;   //도로명 주소

    private String name;  //사업장명

    private String category;  //업태구분명

    private Double longitude; //좌표정보(X) 경도

    private Double latitude; //좌표정보(Y) 위도
}
