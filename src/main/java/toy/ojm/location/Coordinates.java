package toy.ojm.location;

import java.awt.Point;
import lombok.Getter;

@Getter
public class Coordinates {  // 좌표

    private Double latitude; // 경도
    private Double longitude; // 위도

    private Point point;

    public Coordinates(Double latitude, Double longitude){
       this.latitude = latitude;
       this.longitude = longitude;
    }


    public void setLongitude(double x) {
        this.longitude = x;
    }

    public void setLatitude(double y) {
        this.latitude = y;
    }
}
