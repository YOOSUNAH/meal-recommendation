package toy.ojm.domain;

import lombok.Getter;

@Getter
public class Coordinates {

    private Double longitude;
    private Double latitude;


    public void setLongitude(String lon) {
        this.longitude = Double.parseDouble(lon);
    }

    public void setLatitude(String lat) {
        this.latitude = Double.parseDouble(lat);
    }
}
