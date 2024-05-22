package toy.ojm.domain.location;

import lombok.extern.slf4j.Slf4j;

import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;

import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Component;

// 중부원점 TM(EPSG:2097)좌표 → WGS84(EPSG:4326) 좌표 로 변환
@Component
@Slf4j
public class TransCoordination {

    public ProjCoordinate transformToWGS(Double x, Double y){
        // CRS 객체 생성
        CRSFactory factory = new CRSFactory();  // CoordinateReferenceSystem을 생성하기 위한 CRSFactory 인스턴스 생성

        // 중부원점 좌표계 정의
        String utmkName = "UTMK";
        String utmkProj = "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs";
        CoordinateReferenceSystem utmkSystem = factory.createFromParameters(utmkName, utmkProj);

        // WGS84 좌표계 정의
        String wgs84Name = "WGS84";
        String wgs84Proj = "+proj=longlat +datum=WGS84 +no_defs";
        CoordinateReferenceSystem wgs84System = factory.createFromParameters(wgs84Name, wgs84Proj);

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        // 변환 시스템 지정. (원본 시스템, 변환 시스템)
        CoordinateTransform coordinateTransform = ctFactory.createTransform(utmkSystem, wgs84System);

        // 변환할 좌표계 정보 생성
        ProjCoordinate beforeCoord = new ProjCoordinate(x, y);
        // 변환된 좌표를 담을 객체 생성
        ProjCoordinate afterCoord = new ProjCoordinate();

        // 좌표 변환
        coordinateTransform.transform(beforeCoord, afterCoord);

        // 변환된 좌표 로그 출력
        log.info("변환된 경도 : " + afterCoord.x);
        log.info("변환된 위도 : " + afterCoord.y);

        return afterCoord;
    }
}
