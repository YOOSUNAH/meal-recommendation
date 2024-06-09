package toy.ojm.domain.location;

import lombok.extern.slf4j.Slf4j;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;

// 중부원점 TM(EPSG:2097)좌표 → WGS84(EPSG:4326) 좌표 로 변환
@Component
@Slf4j
public class TransCoordination {

    public ProjCoordinate transformToWGS(Double x, Double y){
        // CRS 객체 생성
        CRSFactory factory = new CRSFactory();

        // 중부원점 좌표계 정의
        CoordinateReferenceSystem grs80 = factory.createFromName("EPSG:2097");
        // WGS84 좌표계 정의
        CoordinateReferenceSystem wgs84 = factory.createFromName("EPSG:4326");

        // 좌표 변환 시스템 정의
        BasicCoordinateTransform transformer = new BasicCoordinateTransform(grs80, wgs84);

        // 변환할 좌표계 정보 생성
        ProjCoordinate beforeCoord = new ProjCoordinate(x, y);
        // 변환된 좌표를 담을 객체 생성
        ProjCoordinate afterCoord = new ProjCoordinate();

        // 좌표 변환 수행
        transformer.transform(beforeCoord, afterCoord);
        // 변환된 좌표 로그 출력
//        log.info("변환된 경도 : " + afterCoord.x);
//        log.info("변환된 위도 : " + afterCoord.y);

        return afterCoord;
    }
}
