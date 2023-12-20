package toy.ojm.domain;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;

@Component
public class TransCoordination {

    public ProjCoordinate transformToGCS(Double longitude, Double latitude) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem wgs84 = factory.createFromName("EPSG:4326"); // Geolocation의 GCS 좌표계
        CoordinateReferenceSystem tmCoord = factory.createFromName("EPSG:2097"); // 데이터베이스의 TM 좌표계

        BasicCoordinateTransform transformer = new BasicCoordinateTransform(tmCoord, wgs84);

        ProjCoordinate beforeCoord = new ProjCoordinate(longitude, latitude);
        ProjCoordinate afterCoord = new ProjCoordinate();

        return transformer.transform(beforeCoord, afterCoord);

    }

}