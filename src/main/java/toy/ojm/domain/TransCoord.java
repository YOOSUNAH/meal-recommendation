package toy.ojm.domain;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;

@Component
public class TransCoord {

    public ProjCoordinate transformToGCS(Double strLon, Double strLat) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem tmCoord = factory.createFromName("EPSG:2097"); // 데이터베이스의 TM 좌표계
        CoordinateReferenceSystem wgs84 = factory.createFromName("EPSG:4326"); // Geolocation의 GCS 좌표계

        BasicCoordinateTransform transformer = new BasicCoordinateTransform(tmCoord, wgs84);

        ProjCoordinate beforeCoord = new ProjCoordinate(strLon, strLat);
        ProjCoordinate afterCoord = new ProjCoordinate();

        transformer.transform(beforeCoord, afterCoord);

        return afterCoord;
    }

}