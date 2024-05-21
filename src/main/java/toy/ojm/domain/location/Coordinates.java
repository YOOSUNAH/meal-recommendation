package toy.ojm.domain.location;

import java.awt.Point;
import lombok.Getter;

@Getter
public class Coordinates {  // 좌표

    private final Double longitude; // 경도
    private final Double latitude; // 위도

    public Coordinates(Double longitude, Double latitude){
       this.longitude = longitude;
        this.latitude = latitude;
    }


}
