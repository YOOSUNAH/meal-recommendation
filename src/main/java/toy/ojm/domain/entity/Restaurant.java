package toy.ojm.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Restaurant {

    @Id
    private Long id;

   private String address;
    private String restaurantName;
    private String category;
    private Double longitude; //좌표정보(X) 경도
    private Double latitude; //좌표정보(Y) 위도

    public void setId(Long id) {
        this.id = id;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
