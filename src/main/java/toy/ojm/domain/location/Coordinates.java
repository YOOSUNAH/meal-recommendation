package toy.ojm.domain.location;

import java.awt.Point;
import lombok.Getter;

@Getter
public class Coordinates {  // 좌표

    private final Double latitude; // 경도
    private final Double longitude; // 위도

    public Coordinates(Double latitude, Double longitude){
       this.latitude = latitude;
       this.longitude = longitude;
    }


}
