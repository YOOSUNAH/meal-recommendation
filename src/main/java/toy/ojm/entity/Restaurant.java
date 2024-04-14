package toy.ojm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Restaurant {


    @Id
    private Long id;

    private String businessStatus; //상세영업상태명 (폐업, 영업)
   private String streetNumberAddress;  //지번주소
    private String restaurantName;  //사업장명
    private String category;  //업태구분명
    private Double longitude; //좌표정보(X) 경도
    private Double latitude; //좌표정보(Y) 위도


    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public void setStreetNumberAddress(String streetNumberAddress) {
        this.streetNumberAddress = streetNumberAddress;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLongitude(Double longitude) {
    this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
