package toy.ojm.excel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDTO {
    private String businessStatus;
    private String streetNumberAddress;
    private String streetNameAddress;
    private String restaurantName;
    private String category;
    private Double longitude;
    private Double latitude;
}