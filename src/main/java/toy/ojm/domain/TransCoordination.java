package toy.ojm.domain;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;

@Component
public class TransCoordination {

    public ProjCoordinate transformToGCS(Double longitude, Double latitude) {  // GCS 좌표계로의 변환을 수행하는 메서드
        CRSFactory factory = new CRSFactory();  // CoordinateReferenceSystem을 생성하기 위한 CRSFactory 인스턴스 생성
        CoordinateReferenceSystem wgs84 = factory.createFromName("EPSG:4326"); // Geolocation의 GCS 좌표계
        CoordinateReferenceSystem tmCoord = factory.createFromName("EPSG:2097"); // 데이터베이스의 TM 좌표계

        // TM 좌표계에서 GCS 좌표계로 좌표를 변환하는 BasicCoordinateTransform 생성
        BasicCoordinateTransform transformer = new BasicCoordinateTransform(tmCoord, wgs84);

        // 변환 전 좌표값
        ProjCoordinate beforeCoord = new ProjCoordinate(longitude, latitude);
        ProjCoordinate afterCoord = new ProjCoordinate();

        // 변환된 좌표값 반환
        return transformer.transform(beforeCoord, afterCoord);

    }

}