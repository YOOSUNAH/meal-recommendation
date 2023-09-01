package toy.ojm.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Restaurant {
    private String category;
    private String name;
    private String longitude;
    private String latitude;
    private String signatureMenu;


    public Restaurant(String category, String name, String longitude, String latitude, String signatureMenu) {
        this.category = category;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.signatureMenu = signatureMenu;
    }
}
